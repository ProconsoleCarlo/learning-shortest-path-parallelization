package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.exception.NegativeEdgesException;
import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

public interface ShortestPath {
  Distances compute(Graph graph, int sourceNode);

  String name();

  default void throwsIfNegativeEdges(Graph graph) {
    if (graph.hasNegativeEdges()) {
      throw new NegativeEdgesException();
    }
  }
}