package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class GraphTest {
  static final int VERTICES = 5;
  static final int X = 1;
  static final int Y = 2;
  static final int A_WEIGHT = 9;
  private static final int A_NEGATIVE_WEIGHT = -4;

  abstract Graph graph();

  @Test
  void setAndGetNode() {
    graph().setNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getNode(X, Y));
  }

  @Test
  void setSymmetricNode() {
    graph().setSymmetricNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getNode(X, Y));
    assertEquals(A_WEIGHT, graph().getNode(Y, X));
  }

  @Test
  void hasNegativeEdges() {
    graph().setSymmetricNode(X, Y, A_WEIGHT);

    assertFalse(graph().hasNegativeEdges());

    graph().setSymmetricNode(X, Y, A_NEGATIVE_WEIGHT);

    assertTrue(graph().hasNegativeEdges());
  }

  @Test
  void vertices() {
    assertEquals(VERTICES, graph().vertices());
  }
}
