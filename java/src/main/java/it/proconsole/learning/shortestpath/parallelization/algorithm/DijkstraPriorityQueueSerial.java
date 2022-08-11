package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraPriorityQueueSerial implements DijkstraShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    //problema con grafi disconnessi per cui PQ si svuota...
    throwsIfNegativeEdges(graph);
    var visitedNodes = new HashSet<Integer>(graph.vertices());
    var priorityQueue = new PriorityQueue<Edge>(graph.vertices(), Comparator.comparingInt(edge -> edge.cost));

    var distances = new Distances(graph.vertices(), sourceNode);

    priorityQueue.add(new Edge(sourceNode, 0));
    while (visitedNodes.size() != graph.vertices()) {
      var ux = priorityQueue.remove();
      visitedNodes.add(ux.destination());
      adjacentNodesGraph(graph, priorityQueue, distances, visitedNodes, ux.destination);
    }
    return distances;
  }

  private void adjacentNodesGraph(Graph graph, PriorityQueue<Edge> priorityQueue, Distances distances, Set<Integer> visitedNodes, int ux) {
    int edgeDistance;
    int newDistance;

    var neighbours = ((AdjacencyMapGraph) graph).neighboursList(ux);
    for (Edge entry : neighbours) {
      var neighbour = entry.destination();
      var cost = entry.cost();
      if (!visitedNodes.contains(neighbour)) {
        edgeDistance = cost;
        newDistance = distances.getDistance(ux) + edgeDistance;
        if (newDistance < distances.getDistance(neighbour))
          distances.setDistance(neighbour, newDistance);

        priorityQueue.add(new Edge(neighbour, distances.getDistance(neighbour)));
      }
    }
  }

  @Override
  public String name() {
    return "Dijkstra PQ serial";
  }

  @Override
  public void updateDistances(DistancesWithFinalization distances, Graph graph, int minVertex) {

  }

  public record Edge(int destination, int cost) {
  }
}
