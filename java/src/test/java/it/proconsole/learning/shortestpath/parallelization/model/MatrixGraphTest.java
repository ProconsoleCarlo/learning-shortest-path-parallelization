package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixGraphTest extends GraphTest {
  private final Graph graph = new MatrixGraph(VERTICES);

  @Override
  Graph graph() {
    return graph;
  }

  @Test
  void isNodeZero() {
    graph.setNode(X, Y, A_WEIGHT);

    assertFalse(graph.isNodeZero(X, Y));

    graph.setNode(X, Y, Graph.ZERO_WEIGHT);

    assertTrue(graph.isNodeZero(X, Y));
  }
}