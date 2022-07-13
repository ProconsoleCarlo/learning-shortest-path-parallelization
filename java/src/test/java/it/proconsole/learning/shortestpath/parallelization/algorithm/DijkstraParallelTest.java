package it.proconsole.learning.shortestpath.parallelization.algorithm;

class DijkstraParallelTest extends DijkstraShortestPathTest {
  private final ShortestPath shortestPath = new DijkstraParallel();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }
}