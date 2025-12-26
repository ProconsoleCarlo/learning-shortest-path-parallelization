package it.proconsole.learning.shortestpath.parallelization.v2.algorithm;

import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;

/** Common interface for shortest path algorithms. Implementations can be serial or parallel. */
public interface ShortestPathAlgorithm {

  /**
   * Computes shortest paths from the source vertex to all other vertices.
   *
   * @param graph The input graph
   * @param sourceVertex The source vertex
   * @return Result containing distances and predecessors
   * @throws IllegalArgumentException if the graph contains negative edges and algorithm doesn't
   *     support them
   */
  ShortestPathResult compute(Graph graph, int sourceVertex);

  /** Returns the name of this algorithm. */
  String getName();

  /** Returns true if this algorithm supports graphs with negative edge weights. */
  boolean supportsNegativeWeights();

  /** Returns true if this is a parallel implementation. */
  boolean isParallel();

  /**
   * Validates that the graph is compatible with this algorithm. Throws exception if graph contains
   * negative edges and algorithm doesn't support them.
   */
  default void validateGraph(Graph graph) {
    if (graph.hasNegativeEdges() && !supportsNegativeWeights()) {
      throw new IllegalArgumentException(
          getName() + " does not support graphs with negative edge weights");
    }
  }
}
