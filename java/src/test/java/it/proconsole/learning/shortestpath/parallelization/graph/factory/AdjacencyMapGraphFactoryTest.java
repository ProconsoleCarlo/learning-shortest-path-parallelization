package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

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