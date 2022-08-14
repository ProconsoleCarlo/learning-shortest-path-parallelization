package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.stream.IntStream;

public class DijkstraParallel implements DijkstraShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    throwsIfNegativeEdges(graph);
    var distances = new DistancesWithFinalization(graph.vertices(), sourceNode);
    IntStream.range(0, graph.vertices())
            .map(count -> getMinDistance(distances, graph.vertices()))
            .forEach(minVertex -> {
              distances.setFinalized(minVertex);
              updateDistances(distances, graph, minVertex);
            });
    return distances;
  }

  @Override
  public String name() {
    return "Dijkstra parallel";
  }

  @Override
  public void updateDistances(DistancesWithFinalization distances, Graph graph, int minVertex) {
    IntStream.range(0, graph.vertices())
            .parallel()
            .filter(vertex -> !distances.isFinalized(vertex)
                    && graph.haveConnection(minVertex, vertex)
                    && !distances.isInfinite(minVertex)
                    && distances.getDistance(minVertex) + graph.getCost(minVertex, vertex) < distances.getDistance(vertex))
            .forEach(vertex -> distances.setDistance(vertex, distances.getDistance(minVertex) + graph.getCost(minVertex, vertex)));
  }
}
