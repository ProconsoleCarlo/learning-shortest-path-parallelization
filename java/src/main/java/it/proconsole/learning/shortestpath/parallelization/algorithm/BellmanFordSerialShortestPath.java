package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.stream.IntStream;

public class BellmanFordSerialShortestPath implements ShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    var distances = new Distances(graph.length(), sourceNode);

    relaxEdges(graph, distances);

    if (graph.hasNegativeEdges()) {
      checkCyclesPresence(graph, distances);
    }

    return distances;
  }

  private void relaxEdges(Graph graph, Distances distances) {
    var vertices = graph.length();
    IntStream.range(0, vertices - 1)
            .forEach(node -> IntStream.range(0, vertices)
                    .forEach(src -> IntStream.range(0, vertices)
                            .filter(dest -> !distances.isInfinite(src)
                                    && isDistanceNotFinalized(graph, distances, src, dest)
                            ).forEach(dest -> distances.setDistance(dest, distances.getDistance(src) + graph.getNode(src, dest)))
                    )
            );
  }

  private void checkCyclesPresence(Graph graph, Distances distances) {
    var vertices = graph.length();
    var cycles = IntStream.range(0, vertices)
            .mapToLong(src -> IntStream.range(0, vertices)
                    .filter(dest -> isDistanceNotFinalized(graph, distances, src, dest))
                    .count()
            ).sum();
    if (cycles != 0) {
      System.err.println("The graph contains negative edge cycle " + cycles + " times followed!");
    }
  }

  private boolean isDistanceNotFinalized(Graph graph, Distances distances, int src, int dest) {
    return !graph.isNodeZero(src, dest) && isDistanceMoreThanCost(graph, distances, src, dest);
  }

  private boolean isDistanceMoreThanCost(Graph graph, Distances distances, int src, int dest) {
    return distances.getDistance(dest) > distances.getDistance(src) + graph.getNode(src, dest);
  }
}
