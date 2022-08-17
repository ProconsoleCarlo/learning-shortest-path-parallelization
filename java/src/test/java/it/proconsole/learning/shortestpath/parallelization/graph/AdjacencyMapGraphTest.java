package it.proconsole.learning.shortestpath.parallelization.graph;

class AdjacencyMapGraphTest extends GraphTest {
  private final Graph graph = new AdjacencyMapGraph(VERTICES);

  @Override
  Graph graph() {
    return graph;
  }
}