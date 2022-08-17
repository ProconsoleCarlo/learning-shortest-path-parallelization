package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.graph.MatrixGraph;

public class MatrixGraphFactory implements GraphFactory {
  @Override
  public Graph create(int vertices) {
    return new MatrixGraph(vertices);
  }
}
