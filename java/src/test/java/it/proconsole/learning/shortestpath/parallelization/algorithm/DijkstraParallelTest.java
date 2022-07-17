package it.proconsole.learning.shortestpath.parallelization.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraParallelTest extends DijkstraShortestPathTest {
  private final ShortestPath shortestPath = new DijkstraParallel();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }

  @Test
  void name() {
    assertEquals("Dijkstra parallel", shortestPath.name());
  }
}