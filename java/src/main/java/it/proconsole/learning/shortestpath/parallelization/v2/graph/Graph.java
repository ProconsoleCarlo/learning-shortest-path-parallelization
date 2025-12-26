package it.proconsole.learning.shortestpath.parallelization.v2.graph;

/**
 * Common interface for graph representations. Supports both sparse (adjacency list) and dense
 * (matrix) implementations.
 */
public interface Graph {

  /** Special weight value indicating no edge exists between two vertices. */
  int NO_EDGE = 0;

  /** Returns the number of vertices in the graph. */
  int vertexCount();

  /**
   * Returns an iterable of all outgoing edges from the given vertex. Optimized for iteration in
   * algorithms like Dijkstra and Bellman-Ford.
   */
  Iterable<Edge> getEdges(int vertex);

  /** Returns true if the graph contains any edges with negative weights. */
  boolean hasNegativeEdges();

  /** Returns the total number of edges in the graph. */
  int edgeCount();

  /** Returns the type of graph implementation. */
  GraphType getType();

  enum GraphType {
    ADJACENCY_LIST,
    MATRIX
  }
}
