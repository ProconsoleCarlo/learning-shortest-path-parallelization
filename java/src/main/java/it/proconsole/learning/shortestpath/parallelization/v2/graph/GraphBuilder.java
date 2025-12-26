package it.proconsole.learning.shortestpath.parallelization.v2.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for constructing immutable Graph instances. Supports both CompactAdjacencyListGraph and
 * MatrixGraph with automatic selection.
 */
public final class GraphBuilder {

  private final int vertexCount;
  private final List<List<Edge>> adjacencyList;
  private int edgeCount;

  /** Creates a new GraphBuilder for a graph with the specified number of vertices. */
  public GraphBuilder(int vertexCount) {
    if (vertexCount <= 0) {
      throw new IllegalArgumentException("Vertex count must be positive");
    }
    this.vertexCount = vertexCount;
    this.adjacencyList = new ArrayList<>(vertexCount);
    for (int i = 0; i < vertexCount; i++) {
      adjacencyList.add(new ArrayList<>());
    }
    this.edgeCount = 0;
  }

  /**
   * Adds a directed edge from 'from' to 'to' with the specified weight. Ignores edges with weight
   * NO_EDGE (0).
   */
  public void addEdge(int from, int to, int weight) {
    validateVertex(from);
    validateVertex(to);

    if (weight == Graph.NO_EDGE) {
      return; // Ignore zero-weight edges
    }

    adjacencyList.get(from).add(new Edge(to, weight));
    edgeCount++;
  }

  /** Adds a bidirectional edge between 'from' and 'to' with the specified weight. */
  public void addBidirectionalEdge(int from, int to, int weight) {
    addEdge(from, to, weight);
    addEdge(to, from, weight);
  }

  /** Builds a CompactAdjacencyListGraph (CSR format). Optimal for sparse graphs. */
  public CompactAdjacencyListGraph buildAdjacencyList() {
    return new CompactAdjacencyListGraph(vertexCount, adjacencyList);
  }

  /** Builds a MatrixGraph. Optimal for dense graphs with density > 0.5 and V < 10000. */
  public MatrixGraph buildMatrix() {
    return new MatrixGraph(vertexCount, adjacencyList);
  }

  /** Builds a graph of the specified type. */
  public Graph build(Graph.GraphType type) {
    return switch (type) {
      case ADJACENCY_LIST -> buildAdjacencyList();
      case MATRIX -> buildMatrix();
    };
  }

  /** Returns the current number of edges added to the builder. */
  public int getEdgeCount() {
    return edgeCount;
  }

  private void validateVertex(int vertex) {
    if (vertex < 0 || vertex >= vertexCount) {
      throw new IllegalArgumentException(
          "Vertex " + vertex + " is out of bounds [0, " + vertexCount + ")");
    }
  }
}
