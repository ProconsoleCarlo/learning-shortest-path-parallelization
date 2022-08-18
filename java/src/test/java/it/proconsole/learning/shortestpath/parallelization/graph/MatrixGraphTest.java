package it.proconsole.learning.shortestpath.parallelization.graph;

class MatrixGraphTest extends GraphTest {
  private final Graph graph = new MatrixGraph(VERTICES);

  @Override
  Graph graph() {
    return graph;
  }
}