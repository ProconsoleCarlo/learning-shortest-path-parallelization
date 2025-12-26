package it.proconsole.learning.shortestpath.parallelization.v2.graph;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Compact Compressed Sparse Row (CSR) representation of a graph. Optimal for sparse graphs with O(V
 * + E) memory complexity. Immutable after construction for thread-safety.
 */
public final class CompactAdjacencyListGraph implements Graph {

  private final int vertexCount;
  private final int edgeCount;
  private final int[] offsets; // Starting index in destinations/weights for each vertex
  private final int[] destinations; // Destination vertices
  private final int[] weights; // Edge weights
  private final boolean hasNegativeEdges;

  /** Constructs a CompactAdjacencyListGraph from the builder's edge data. */
  CompactAdjacencyListGraph(int vertexCount, List<List<Edge>> adjacencyList) {
    this.vertexCount = vertexCount;

    // Calculate total edges and offsets
    this.offsets = new int[vertexCount + 1];
    int totalEdges = 0;
    for (int v = 0; v < vertexCount; v++) {
      offsets[v] = totalEdges;
      totalEdges += adjacencyList.get(v).size();
    }
    offsets[vertexCount] = totalEdges;
    this.edgeCount = totalEdges;

    // Flatten adjacency list into CSR format
    this.destinations = new int[totalEdges];
    this.weights = new int[totalEdges];

    boolean hasNegative = false;
    int index = 0;
    for (int v = 0; v < vertexCount; v++) {
      for (Edge edge : adjacencyList.get(v)) {
        destinations[index] = edge.destination();
        weights[index] = edge.weight();
        if (edge.weight() < 0) {
          hasNegative = true;
        }
        index++;
      }
    }
    this.hasNegativeEdges = hasNegative;
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
    return GraphType.ADJACENCY_LIST;
  }

  private void validateVertex(int vertex) {
    if (vertex < 0 || vertex >= vertexCount) {
      throw new IllegalArgumentException(
          "Vertex " + vertex + " is out of bounds [0, " + vertexCount + ")");
    }
  }

  /**
   * Iterator for edges of a specific vertex. Provides efficient O(degree) iteration over outgoing
   * edges.
   */
  private class EdgeIterator implements Iterator<Edge> {
    private final int endIndex;
    private int currentIndex;

    private EdgeIterator(int vertex) {
      this.currentIndex = offsets[vertex];
      this.endIndex = offsets[vertex + 1];
    }

    @Override
    public boolean hasNext() {
      return currentIndex < endIndex;
    }

    @Override
    public Edge next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Edge edge = new Edge(destinations[currentIndex], weights[currentIndex]);
      currentIndex++;
      return edge;
    }
  }
}
