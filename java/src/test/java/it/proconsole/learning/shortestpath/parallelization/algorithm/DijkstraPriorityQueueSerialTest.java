package it.proconsole.learning.shortestpath.parallelization.algorithm;

class DijkstraPriorityQueueSerialTest extends DijkstraShortestPathTest {
  private final ShortestPath shortestPath = new DijkstraPriorityQueueSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }
}