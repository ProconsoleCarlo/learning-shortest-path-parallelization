package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.graph.Edge;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class DijkstraPriorityQueueSerial implements ShortestPath {
  private final PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::cost));

  @Override
  public Distances compute(Graph graph, int sourceNode) {
    throwsIfNegativeEdges(graph);

    var distances = new DistancesWithFinalization(graph.vertices(), sourceNode);
    priorityQueue.add(new Edge(sourceNode, 0));
    while (!priorityQueue.isEmpty()) {
      var minNode = priorityQueue.remove().destination();
      distances.setFinalized(minNode);
      extracted(distances, graph, minNode);
    }
    finalizeIsolatedNodes(distances);
    return distances;
  }

  private void extracted(DistancesWithFinalization distances, Graph graph, int minNode) {
    int edgeDistance;
    int newDistance;
    for (Edge edge : graph.neighboursOf(minNode)) {
      var neighbour = edge.destination();
      if (!distances.isFinalized(neighbour)) {
        edgeDistance = edge.cost();
        newDistance = distances.getDistance(minNode) + edgeDistance;
        if (newDistance < distances.getDistance(neighbour)) {
          distances.setDistance(neighbour, newDistance);
        }
        priorityQueue.add(new Edge(neighbour, distances.getDistance(neighbour)));
      }
    }
  }

  private void finalizeIsolatedNodes(DistancesWithFinalization distances) {
    IntStream.range(0, distances.vertices())
            .filter(distances::isInfinite)
            .forEach(distances::setFinalized);
  }

  @Override
  public String name() {
    return "Dijkstra with priority queue serial";
  }
}
