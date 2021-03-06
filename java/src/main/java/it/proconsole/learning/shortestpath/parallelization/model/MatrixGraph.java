package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;

public final class MatrixGraph implements Graph {
  private final int vertices;
  private final int[][] values;

  public MatrixGraph(int vertices) {
    this.vertices = vertices;
    this.values = new int[vertices][vertices];
  }

  @Override
  public void setNode(int x, int y, int value) {
    this.values[x][y] = value;
  }

  @Override
  public void setSymmetricNode(int x, int y, int value) {
    this.values[x][y] = value;
    this.values[y][x] = value;
  }

  @Override
  public int getNode(int x, int y) {
    return values[x][y];
  }

  @Override
  public boolean hasNegativeEdges() {
    return Arrays.stream(values).parallel()
            .flatMapToInt(Arrays::stream)
            .anyMatch(node -> node < Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean isNodeZero(int x, int y) {
    return values[x][y] == Graph.ZERO_WEIGHT;
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
