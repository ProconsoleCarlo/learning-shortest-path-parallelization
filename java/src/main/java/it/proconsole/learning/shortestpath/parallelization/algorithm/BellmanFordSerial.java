package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

public class BellmanFordSerial implements BellmanFordShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    var distances = new Distances(graph.vertices(), sourceNode);

    relaxEdges(graph, distances);

    if (graph.hasNegativeEdges()) {
      checkCyclesPresence(graph, distances);
    }

    return distances;
  }

  @Override
  public String name() {
    return "Bellman-Ford serial";
  }

  private void relaxEdges(Graph graph, Distances distances) {
    var vertices = graph.vertices();
    for (int node = 0; node < vertices - 1; node++) {
      for (int src = 0; src < vertices; src++) {
        for (int dest = 0; dest < vertices; dest++) {
          if (!distances.isInfinite(src) && isDistanceNotFinalized(graph, distances, src, dest)) {
            distances.setDistance(dest, distances.getDistance(src) + graph.getCost(src, dest));
          }
        }
      }
    }
  }
}
