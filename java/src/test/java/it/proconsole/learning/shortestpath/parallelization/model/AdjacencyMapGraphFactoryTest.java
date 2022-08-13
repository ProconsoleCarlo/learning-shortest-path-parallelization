package it.proconsole.learning.shortestpath.parallelization.model;

class AdjacencyMapGraphFactoryTest extends GraphFactoryTest {
  @Override
  GraphFactory graphFactory() {
    return new AdjacencyMapGraphFactory();
  }

  @Override
  Graph expected() {
    return new AdjacencyMapGraph(VERTICES);
  }
}