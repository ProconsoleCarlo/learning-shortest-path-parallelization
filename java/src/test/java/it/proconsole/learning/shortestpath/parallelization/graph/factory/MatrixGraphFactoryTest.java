package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.graph.MatrixGraph;

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