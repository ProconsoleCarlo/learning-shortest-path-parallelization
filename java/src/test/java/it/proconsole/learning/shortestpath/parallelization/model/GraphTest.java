package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class GraphTest {
  static final int VERTICES = 5;
  static final int X = 1;
  static final int Y = 2;
  static final int W = 3;
  static final int Z = 4;
  static final int A_WEIGHT = 9;
  private static final int A_NEGATIVE_WEIGHT = -4;

  abstract Graph graph();

  @Test
  void setAndGetNode() {
    graph().setNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getNode(X, Y));

    graph().setNode(X, Y, A_NEGATIVE_WEIGHT);

    assertEquals(A_NEGATIVE_WEIGHT, graph().getNode(X, Y));
  }

  @Test
  void setSymmetricNode() {
    graph().setSymmetricNode(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getNode(X, Y));
    assertEquals(A_WEIGHT, graph().getNode(Y, X));
  }

  @Test
  void removeEdge() {
    graph().setSymmetricNode(X, Y, A_WEIGHT);

    assertNotEquals(Graph.ZERO_WEIGHT, graph().getNode(X, Y));
    assertNotEquals(Graph.ZERO_WEIGHT, graph().getNode(Y, X));

    graph().removeEdge(X, Y);

    assertEquals(Graph.ZERO_WEIGHT, graph().getNode(X, Y));
    assertEquals(Graph.ZERO_WEIGHT, graph().getNode(Y, X));
  }

  @Test
  void hasNegativeEdges() {
    graph().setSymmetricNode(X, Y, A_WEIGHT);

    assertFalse(graph().hasNegativeEdges());

    graph().setSymmetricNode(X, Y, A_NEGATIVE_WEIGHT);

    assertTrue(graph().hasNegativeEdges());
  }

  @Test
  void isNodeZero() {
    graph().setNode(X, Y, A_WEIGHT);

    assertFalse(graph().isNodeZero(X, Y));

    graph().setNode(X, Y, Graph.ZERO_WEIGHT);

    assertTrue(graph().isNodeZero(X, Y));
  }

  @Test
  void vertices() {
    assertEquals(VERTICES, graph().vertices());
  }

  @Test
  void neighboursOf() {
    graph().setNode(X, Y, A_WEIGHT);
    graph().setNode(X, Z, A_WEIGHT);
    graph().setNode(Y, W, A_WEIGHT);

    var current = graph().neighboursOf(X);

    var edges = List.of(new Edge(Y, A_WEIGHT), new Edge(Z, A_WEIGHT));
    assertEquals(edges, current);
  }
}
