package it.proconsole.learning.shortestpath.parallelization.algorithm;

class DijkstraSerialTest extends ShortestPathTest {
  private static final int SOURCE_NODE = 0;

  private final ShortestPath shortestPath = new DijkstraSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }

  @Override
  public int sourceNode() {
    return SOURCE_NODE;
  }
}