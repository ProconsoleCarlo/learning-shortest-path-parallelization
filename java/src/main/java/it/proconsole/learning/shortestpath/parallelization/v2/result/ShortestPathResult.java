package it.proconsole.learning.shortestpath.parallelization.v2.result;

import java.util.Arrays;

/**
 * Result of the shortest path computation containing distances and predecessors. Immutable and
 * thread-safe.
 */
public final class ShortestPathResult {

  public static final int INFINITY = Integer.MAX_VALUE;
  public static final int NO_PREDECESSOR = -1;

  private final int sourceVertex;
  private final int[] distances;
  private final int[] predecessors;

  /** Creates a result with distances and predecessors. */
  public ShortestPathResult(int sourceVertex, int[] distances, int[] predecessors) {
    if (distances.length != predecessors.length) {
      throw new IllegalArgumentException("Distances and predecessors arrays must have same length");
    }
    this.sourceVertex = sourceVertex;
    this.distances = Arrays.copyOf(distances, distances.length);
    this.predecessors = Arrays.copyOf(predecessors, predecessors.length);
  }

  /** Returns a copy of the distances array. */
  public int[] getDistances() {
    return Arrays.copyOf(distances, distances.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ShortestPathResult that)) return false;
    return sourceVertex == that.sourceVertex
        && Arrays.equals(distances, that.distances)
        && Arrays.equals(predecessors, that.predecessors);
  }

  @Override
  public int hashCode() {
    int result = sourceVertex;
    result = 31 * result + Arrays.hashCode(distances);
    result = 31 * result + Arrays.hashCode(predecessors);
    return result;
  }
}
