package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class GraphFactoryTest {
  static final int VERTICES = 10;

  abstract GraphFactory graphFactory();

  abstract Graph expected();

  @Test
  void create() {
    var current = graphFactory().create(VERTICES);

    assertEquals(expected(), current);
  }
}