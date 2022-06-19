package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class MatrixGraph implements Graph {
  private final int[][] values;

  public MatrixGraph(int vertices) {
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
    return IntStream.range(0, values.length)
            .parallel()
            .anyMatch(x -> IntStream.range(0, values.length)
                    .anyMatch(y -> values[x][y] < 0)
            );
  }

  @Override
  public boolean isNodeZero(int x, int y) {
    return values[x][y] == ZERO_WEIGHT;
  }

  @Override
  public int length() {
    return values.length;
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

  @Override
  public String toString() {
    return "Graph{" +
            "values=" + Arrays.toString(values) +
            '}';
  }
}
