package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.exception.NegativeEdgesException;
import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

public interface ShortestPath {
  Distances compute(Graph graph, int sourceNode);

  default void throwsIfNegativeEdges(Graph graph) {
    if (graph.hasNegativeEdges()) {
      throw new NegativeEdgesException();
    }
  }
}