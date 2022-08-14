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
  void addBidirectionalEdge() {
    graph().addBidirectionalEdge(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getCost(X, Y));
    assertEquals(A_WEIGHT, graph().getCost(Y, X));
  }

  @Test
  void addEdgeAndGetCost() {
    graph().addEdge(X, Y, A_WEIGHT);

    assertEquals(A_WEIGHT, graph().getCost(X, Y));

    graph().addEdge(X, Y, A_NEGATIVE_WEIGHT);

    assertEquals(A_NEGATIVE_WEIGHT, graph().getCost(X, Y));
  }

  @Test
  void hasNegativeEdges() {
    graph().addBidirectionalEdge(X, Y, A_WEIGHT);

    assertFalse(graph().hasNegativeEdges());

    graph().addBidirectionalEdge(X, Y, A_NEGATIVE_WEIGHT);

    assertTrue(graph().hasNegativeEdges());
  }

  @Test
  void haveConnection() {
    graph().addEdge(X, Y, A_WEIGHT);

    assertTrue(graph().haveConnection(X, Y));

    graph().addEdge(X, Y, Graph.ZERO_WEIGHT);

    assertFalse(graph().haveConnection(X, Y));
  }

  @Test
  void neighboursOf() {
    graph().addEdge(X, Y, A_WEIGHT);
    graph().addEdge(X, Z, A_WEIGHT);
    graph().addEdge(Y, W, A_WEIGHT);

    var current = graph().neighboursOf(X);

    var edges = List.of(new Edge(Y, A_WEIGHT), new Edge(Z, A_WEIGHT));
    assertEquals(edges, current);
  }

  @Test
  void removeEdge() {
    graph().addBidirectionalEdge(X, Y, A_WEIGHT);

    assertNotEquals(Graph.ZERO_WEIGHT, graph().getCost(X, Y));
    assertNotEquals(Graph.ZERO_WEIGHT, graph().getCost(Y, X));

    graph().removeEdge(X, Y);

    assertEquals(Graph.ZERO_WEIGHT, graph().getCost(X, Y));
    assertEquals(Graph.ZERO_WEIGHT, graph().getCost(Y, X));
  }

  @Test
  void vertices() {
    assertEquals(VERTICES, graph().vertices());
  }
}
