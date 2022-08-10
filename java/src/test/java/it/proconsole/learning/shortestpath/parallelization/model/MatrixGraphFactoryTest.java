package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixGraphFactoryTest {
  @Test
  void create() {
    var current = new MatrixGraphFactory().create(12);

    assertEquals(new MatrixGraph(12), current);
  }
}