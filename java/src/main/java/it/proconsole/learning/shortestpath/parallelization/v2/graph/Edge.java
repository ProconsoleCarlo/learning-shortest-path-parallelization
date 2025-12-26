package it.proconsole.learning.shortestpath.parallelization.v2.graph;

/**
 * Represents an edge in a graph with a destination vertex and a weight. Immutable record for
 * thread-safety.
 */
public record Edge(int destination, int weight) {

  public Edge {
    if (destination < 0) {
      throw new IllegalArgumentException("Destination must be non-negative");
    }
  }
}
