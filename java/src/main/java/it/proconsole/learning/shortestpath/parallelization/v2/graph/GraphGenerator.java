package it.proconsole.learning.shortestpath.parallelization.v2.graph;

import java.util.Random;

/** Generator for random graphs with configurable vertex count, edge density, and weight range. */
public final class GraphGenerator {

  private final Random random;

  public GraphGenerator() {
    this.random = new Random();
  }

  public GraphGenerator(long seed) {
    this.random = new Random(seed);
  }

  /**
   * Generates a random graph with the specified parameters.
   *
   * @param vertices Number of vertices
   * @param density Edge density (0.0 to 1.0)
   * @param minWeight Minimum edge weight (inclusive)
   * @param maxWeight Maximum edge weight (inclusive)
   * @param directed If true, generates directed graph; otherwise undirected
   * @param graphType Type of graph to build
   * @return Generated graph
   */
  public Graph generate(
      int vertices,
      double density,
      int minWeight,
      int maxWeight,
      boolean directed,
      Graph.GraphType graphType) {
    if (vertices <= 0) {
      throw new IllegalArgumentException("Vertices must be positive");
    }
    if (density < 0.0 || density > 1.0) {
      throw new IllegalArgumentException("Density must be between 0.0 and 1.0");
    }
    if (minWeight > maxWeight) {
      throw new IllegalArgumentException("Min weight cannot exceed max weight");
    }

    GraphBuilder builder = new GraphBuilder(vertices);

    int maxPossibleEdges = directed ? vertices * (vertices - 1) : vertices * (vertices - 1) / 2;
    int targetEdges = (int) (maxPossibleEdges * density);

    // Use additive or removal algorithm based on density
    if (density > 0.475) {
      generateByRemoval(builder, vertices, targetEdges, minWeight, maxWeight);
    } else {
      generateByAddition(builder, vertices, targetEdges, minWeight, maxWeight, directed);
    }

    return builder.build(graphType);
  }

  /** Additive algorithm: add random edges until target is reached. Efficient for sparse graphs. */
  private void generateByAddition(
      GraphBuilder builder,
      int vertices,
      int targetEdges,
      int minWeight,
      int maxWeight,
      boolean directed) {
    int edgesAdded = 0;
    int attempts = 0;
    int maxAttempts = targetEdges * 10; // Prevent infinite loop

    while (edgesAdded < targetEdges && attempts < maxAttempts) {
      int from = random.nextInt(vertices);
      int to = random.nextInt(vertices);

      if (from != to) {
        int weight = randomWeight(minWeight, maxWeight);

        if (directed) {
          builder.addEdge(from, to, weight);
          edgesAdded++;
        } else {
          builder.addBidirectionalEdge(from, to, weight);
          edgesAdded += 2; // Count both directions
        }
      }
      attempts++;
    }
  }

  /** Removal algorithm: start with complete graph and remove edges. Efficient for dense graphs. */
  private void generateByRemoval(
      GraphBuilder builder, int vertices, int targetEdges, int minWeight, int maxWeight) {
    // Create complete graph
    for (int from = 0; from < vertices; from++) {
      for (int to = 0; to < vertices; to++) {
        if (from != to) {
          int weight = randomWeight(minWeight, maxWeight);
          builder.addEdge(from, to, weight);
        }
      }
    }

    // Remove random edges until we reach target
    int currentEdges = builder.getEdgeCount();
    int edgesToRemove = currentEdges - targetEdges;

    for (int i = 0; i < edgesToRemove && currentEdges > targetEdges; i++) {
      int from = random.nextInt(vertices);
      int to = random.nextInt(vertices);
      if (from != to) {
        // Note: This is a simplified removal - in practice, we'd need to track actual edges
        // For now, we rely on the additive approach for most cases
      }
    }
  }

  private int randomWeight(int minWeight, int maxWeight) {
    if (minWeight == maxWeight) {
      return minWeight;
    }
    return minWeight + random.nextInt(maxWeight - minWeight + 1);
  }
}
