package it.proconsole.learning.shortestpath.parallelization.algorithm;

class DijkstraSerialTest extends DijkstraShortestPathTest {
  private final ShortestPath shortestPath = new DijkstraSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }
}