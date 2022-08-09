package it.proconsole.learning.shortestpath.parallelization.model;

public class AdjacencyMapGraphFactory implements GraphFactory {
  @Override
  public Graph create(int vertices) {
    return new AdjacencyMapGraph(vertices);
  }
}
