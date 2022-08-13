package it.proconsole.learning.shortestpath.parallelization.model;

class MatrixGraphTest extends GraphTest {
  private final Graph graph = new MatrixGraph(VERTICES);

  @Override
  Graph graph() {
    return graph;
  }
}