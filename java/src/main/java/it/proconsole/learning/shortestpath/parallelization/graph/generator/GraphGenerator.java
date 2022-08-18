package it.proconsole.learning.shortestpath.parallelization.graph.generator;

import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

public interface GraphGenerator {
  Graph generate(int vertices, float density);

  Graph generate(int vertices, int edges);
}
