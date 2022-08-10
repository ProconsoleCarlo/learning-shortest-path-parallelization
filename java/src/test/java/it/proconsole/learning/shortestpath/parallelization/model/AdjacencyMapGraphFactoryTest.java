package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdjacencyMapGraphFactoryTest {
  @Test
  void create() {
    var current = new AdjacencyMapGraphFactory().create(12);

    assertEquals(new AdjacencyMapGraph(12), current);
  }
}