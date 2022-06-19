package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixGraphTest {
  private static final int VERTICES = 5;
  private static final int X = 1;
  private static final int Y = 2;
  private static final int A_WEIGHT = 9;
  private static final int A_NEGATIVE_WEIGHT = -4;

  private final Graph graph = new MatrixGraph(VERTICES);

  @Test
  void setAndGetNode() {
    graph.setNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph.getNode(X, Y));
  }

  @Test
  void setSymmetricNode() {
    graph.setSymmetricNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph.getNode(X, Y));
    assertEquals(A_WEIGHT, graph.getNode(Y, X));
  }

  @Test
  void hasNegativeEdges() {
    graph.setSymmetricNode(X, Y, A_WEIGHT);

    assertFalse(graph.hasNegativeEdges());

    graph.setSymmetricNode(X, Y, A_NEGATIVE_WEIGHT);

    assertTrue(graph.hasNegativeEdges());
  }

  @Test
  void isNodeZero() {
    graph.setNode(X, Y, A_WEIGHT);

    assertFalse(graph.isNodeZero(X, Y));

    graph.setNode(X, Y, Graph.ZERO_WEIGHT);

    assertTrue(graph.isNodeZero(X, Y));
  }

  @Test
  void length() {
    assertEquals(VERTICES, graph.length());
  }
}