package it.proconsole.learning.shortestpath.parallelization.v2.algorithm;

import it.proconsole.learning.shortestpath.parallelization.v2.datastructure.BinaryMinHeap;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Edge;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;
import java.util.Arrays;

/**
 * Optimized serial implementation of Dijkstra's shortest path algorithm. Uses a binary min-heap
 * with efficient decrease-key operation. Time complexity: O((V + E) log V)
 */
public final class DijkstraSerialOptimized implements ShortestPathAlgorithm {

  @Override
  public ShortestPathResult compute(Graph graph, int sourceVertex) {
    validateGraph(graph);

    int vertexCount = graph.vertexCount();
    int[] distances = new int[vertexCount];
    int[] predecessors = new int[vertexCount];

    Arrays.fill(distances, ShortestPathResult.INFINITY);
    Arrays.fill(predecessors, ShortestPathResult.NO_PREDECESSOR);
    distances[sourceVertex] = 0;

    BinaryMinHeap heap = new BinaryMinHeap(vertexCount);
    heap.insert(sourceVertex, 0);

    while (!heap.isEmpty()) {
      int u = heap.extractMin();
      int distU = distances[u];

      // Relax all edges from u
      for (Edge edge : graph.getEdges(u)) {
        int v = edge.destination();
        int weight = edge.weight();

        if (distU != ShortestPathResult.INFINITY) {
          int newDist = distU + weight;

          if (newDist < distances[v]) {
            distances[v] = newDist;
            predecessors[v] = u;

            if (heap.contains(v)) {
              heap.decreaseKey(v, newDist);
            } else {
              heap.insert(v, newDist);
            }
          }
        }
      }
    }

    return new ShortestPathResult(sourceVertex, distances, predecessors);
  }

  @Override
  public String getName() {
    return "Dijkstra Serial (Optimized)";
  }

  @Override
  public boolean supportsNegativeWeights() {
    return false;
  }

  @Override
  public boolean isParallel() {
    return false;
  }
}
