package it.proconsole.learning.shortestpath.parallelization.v2.algorithm;

import it.proconsole.learning.shortestpath.parallelization.v2.graph.Edge;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;
import java.util.Arrays;

/**
 * Serial implementation of Bellman-Ford shortest path algorithm. Supports graphs with negative edge
 * weights. Time complexity: O(V * E)
 */
public final class BellmanFordSerial implements ShortestPathAlgorithm {

  private final boolean detectNegativeCycles;

  public BellmanFordSerial() {
    this(true);
  }

  public BellmanFordSerial(boolean detectNegativeCycles) {
    this.detectNegativeCycles = detectNegativeCycles;
  }

  @Override
  public ShortestPathResult compute(Graph graph, int sourceVertex) {
    int vertexCount = graph.vertexCount();
    int[] distances = new int[vertexCount];
    int[] predecessors = new int[vertexCount];

    Arrays.fill(distances, ShortestPathResult.INFINITY);
    Arrays.fill(predecessors, ShortestPathResult.NO_PREDECESSOR);
    distances[sourceVertex] = 0;

    // Relax edges V-1 times
    for (int i = 0; i < vertexCount - 1; i++) {
      boolean changed = false;

      for (int u = 0; u < vertexCount; u++) {
        if (distances[u] == ShortestPathResult.INFINITY) {
          continue;
        }

        for (Edge edge : graph.getEdges(u)) {
          int v = edge.destination();
          int weight = edge.weight();
          int newDist = distances[u] + weight;

          if (newDist < distances[v]) {
            distances[v] = newDist;
            predecessors[v] = u;
            changed = true;
          }
        }
      }

      // Early termination if no changes
      if (!changed) {
        break;
      }
    }

    // Check for negative cycles
    if (detectNegativeCycles) {
      for (int u = 0; u < vertexCount; u++) {
        if (distances[u] == ShortestPathResult.INFINITY) {
          continue;
        }

        for (Edge edge : graph.getEdges(u)) {
          int v = edge.destination();
          int weight = edge.weight();

          if (distances[u] + weight < distances[v]) {
            throw new IllegalStateException(
                "Graph contains a negative cycle reachable from source");
          }
        }
      }
    }

    return new ShortestPathResult(sourceVertex, distances, predecessors);
  }

  @Override
  public String getName() {
    return "Bellman-Ford Serial";
  }

  @Override
  public boolean supportsNegativeWeights() {
    return true;
  }

  @Override
  public boolean isParallel() {
    return false;
  }
}
