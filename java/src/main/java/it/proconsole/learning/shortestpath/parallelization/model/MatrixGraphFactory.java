package it.proconsole.learning.shortestpath.parallelization.model;

public class MatrixGraphFactory implements GraphFactory {
  @Override
  public Graph create(int vertices) {
    return new MatrixGraph(vertices);
  }
}
