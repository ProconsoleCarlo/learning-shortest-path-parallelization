package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;

public record Graph(int[][] values) {
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
