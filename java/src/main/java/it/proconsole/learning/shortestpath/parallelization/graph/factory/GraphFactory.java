package it.proconsole.learning.shortestpath.parallelization.graph.factory;

import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

public interface GraphFactory {
  Graph create(int vertices);
}


