package it.proconsole.learning.shortestpath.parallelization.v2.graph;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Matrix-based graph representation using 2D array. Optimal for dense graphs (density > 0.5) with V
 * < 10000. Provides O(1) access to edge weights but O(V²) memory complexity.
 */
public final class MatrixGraph implements Graph {

  private final int vertexCount;
  private final int[][] adjacencyMatrix;
  private int edgeCount;
  private boolean hasNegativeEdges;

  /** Constructs a MatrixGraph from the builder's edge data. */
  MatrixGraph(int vertexCount, List<List<Edge>> adjacencyList) {
    this.vertexCount = vertexCount;
    this.adjacencyMatrix = new int[vertexCount][vertexCount];
    this.edgeCount = 0;
    this.hasNegativeEdges = false;

    // Initialize matrix from adjacency list
    for (int v = 0; v < vertexCount; v++) {
      for (Edge edge : adjacencyList.get(v)) {
        adjacencyMatrix[v][edge.destination()] = edge.weight();
        edgeCount++;
        if (edge.weight() < 0) {
          hasNegativeEdges = true;
        }
      }
    }
  }

  @Override
  public int vertexCount() {
    return vertexCount;
  }

  @Override
  public Iterable<Edge> getEdges(int vertex) {
    validateVertex(vertex);
    return () -> new EdgeIterator(vertex);
  }

  @Override
  public boolean hasNegativeEdges() {
    return hasNegativeEdges;
  }

  @Override
  public int edgeCount() {
    return edgeCount;
  }

  @Override
  public GraphType getType() {
    return GraphType.MATRIX;
  }

  private void validateVertex(int vertex) {
    if (vertex < 0 || vertex >= vertexCount) {
      throw new IllegalArgumentException(
          "Vertex " + vertex + " is out of bounds [0, " + vertexCount + ")");
    }
  }

  /** Iterator for edges of a specific vertex. Scans the entire row in O(V) time. */
  private class EdgeIterator implements Iterator<Edge> {
    private final int vertex;
    private int currentDestination;
    private Edge nextEdge;

    EdgeIterator(int vertex) {
      this.vertex = vertex;
      this.currentDestination = 0;
      findNext();
    }

    private void findNext() {
      nextEdge = null;
      while (currentDestination < vertexCount) {
        int weight = adjacencyMatrix[vertex][currentDestination];
        if (weight != NO_EDGE) {
          nextEdge = new Edge(currentDestination, weight);
          currentDestination++;
          return;
        }
        currentDestination++;
      }
    }

    @Override
    public boolean hasNext() {
      return nextEdge != null;
    }

    @Override
    public Edge next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Edge result = nextEdge;
      findNext();
      return result;
    }
  }
}
