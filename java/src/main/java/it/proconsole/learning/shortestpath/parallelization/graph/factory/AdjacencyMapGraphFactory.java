package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

public class AdjacencyMapGraphFactory implements GraphFactory {
  @Override
  public Graph create(int vertices) {
    return new AdjacencyMapGraph(vertices);
  }
}
