package it.proconsole.learning.shortestpath.parallelization.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraSerialTest extends DijkstraShortestPathTest {
  private final ShortestPath shortestPath = new DijkstraSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }

  @Test
  void name() {
    assertEquals("Dijkstra serial", shortestPath.name());
  }
}