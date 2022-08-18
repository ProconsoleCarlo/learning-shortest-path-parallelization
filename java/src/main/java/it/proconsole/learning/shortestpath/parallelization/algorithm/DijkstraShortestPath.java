package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.DistancesWithFinalization;

interface DijkstraShortestPath extends ShortestPath {
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
