package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Distances {
  private final int[] values;

  public Distances(int vertices, int sourceNode) {
    this.values = IntStream.range(0, vertices).parallel().map(i -> Integer.MAX_VALUE).toArray();
    setDistance(sourceNode, Graph.ZERO_WEIGHT);
  }

  public int getDistance(int node) {
    return this.values[node];
  }

  public void setDistance(int node, int value) {
    this.values[node] = value;
  }

  public boolean isInfinite(int node) {
    return this.values[node] == Integer.MAX_VALUE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Distances distances)) return false;
    return Arrays.equals(values, distances.values);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(values);
  }
}
