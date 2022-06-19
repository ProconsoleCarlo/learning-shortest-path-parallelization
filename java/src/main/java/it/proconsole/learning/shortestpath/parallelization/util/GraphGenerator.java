package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.Graph;

public interface GraphGenerator {
  Graph generate(int vertices, float density);

  Graph generate(int vertices, int edges);
}
