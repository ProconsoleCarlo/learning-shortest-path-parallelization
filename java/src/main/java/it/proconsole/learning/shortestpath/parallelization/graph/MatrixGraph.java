package it.proconsole.learning.shortestpath.parallelization.graph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public final class MatrixGraph implements Graph {
  private final int vertices;
  private final int[][] values;

  public MatrixGraph(int vertices) {
    this.vertices = vertices;
    this.values = new int[vertices][vertices];
  }

  @Override
  public void addBidirectionalEdge(int from, int to, int cost) {
    values[from][to] = cost;
    values[to][from] = cost;
  }

  @Override
  public void addEdge(int from, int to, int cost) {
    values[from][to] = cost;
  }

  @Override
  public int getCost(int from, int to) {
    return values[from][to];
  }

  @Override
  public boolean hasNegativeEdges() {
    return Arrays.stream(values).parallel()
            .flatMapToInt(Arrays::stream)
            .anyMatch(cost -> cost < Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean haveConnection(int from, int to) {
    return values[from][to] != Graph.ZERO_WEIGHT;
  }

  @Override
  public List<Edge> neighboursOf(int node) {
    return IntStream.range(0, values[node].length)
            .filter(y -> values[node][y] != Graph.ZERO_WEIGHT)
            .mapToObj(y -> new Edge(y, values[node][y]))
            .toList();
  }

  @Override
  public void removeEdge(int from, int to) {
    addBidirectionalEdge(from, to, Graph.ZERO_WEIGHT);
  }

  @Override
  public int vertices() {
    return vertices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MatrixGraph graph)) return false;
    return Arrays.deepEquals(values, graph.values);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(values);
  }
}
