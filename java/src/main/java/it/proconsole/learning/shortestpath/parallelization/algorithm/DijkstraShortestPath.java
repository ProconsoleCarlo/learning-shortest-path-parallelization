package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.exception.NegativeEdgesException;
import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

public interface DijkstraShortestPath extends ShortestPath{
  void updateDistances(DistancesWithFinalization distances, Graph graph, int minVertex);

  default void throwsIfNegativeEdges(Graph graph) {
    if (graph.hasNegativeEdges()) {
      throw new NegativeEdgesException();
    }
  }

  default int getMinDistance(DistancesWithFinalization distances, int vertices) {
    var minVertex = -1;
    var minDistance = Integer.MAX_VALUE;
    for (var vertex = 0; vertex < vertices; vertex++) {
      if (!distances.isFinalized(vertex) && distances.getDistance(vertex) <= minDistance) {
        minDistance = distances.getDistance(vertex);
        minVertex = vertex;
      }
    }
    return minVertex;
  }
}
