package it.proconsole.learning.shortestpath.parallelization.model;

class MatrixGraphFactoryTest extends GraphFactoryTest {
  @Override
  GraphFactory graphFactory() {
    return new MatrixGraphFactory();
  }

  @Override
  Graph expected() {
    return new MatrixGraph(VERTICES);
  }
}