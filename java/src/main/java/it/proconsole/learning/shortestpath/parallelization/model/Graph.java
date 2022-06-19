package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class Graph {
  public static final int ZERO_WEIGHT = 0;

  private final int[][] values;

  public Graph(int vertices) {
    this.values = new int[vertices][vertices];
  }

  public void setNode(int x, int y, int value) {
    this.values[x][y] = value;
  }

  public void setSymmetricNode(int x, int y, int value) {
    this.values[x][y] = value;
    this.values[y][x] = value;
  }

  public int getNode(int x, int y) {
    return values[x][y];
  }

  public boolean hasNegativeEdges() {
    return IntStream.range(0, values.length)
            .parallel()
            .anyMatch(x -> IntStream.range(0, x).parallel().anyMatch(y -> values[x][y] < 0));
  }

  public boolean isNodeZero(int x, int y) {
    return values[x][y] == ZERO_WEIGHT;
  }

  public int length() {
    return values.length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Graph graph)) return false;
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
