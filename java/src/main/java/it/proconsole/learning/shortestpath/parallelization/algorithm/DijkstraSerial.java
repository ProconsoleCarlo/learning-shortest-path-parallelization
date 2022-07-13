package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

public class DijkstraSerial implements DijkstraShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    throwsIfNegativeEdges(graph);
    var distances = new DistancesWithFinalization(graph.vertices(), sourceNode);
    for (var count = 0; count < graph.vertices(); count++) {
      var minVertex = getMinDistance(distances, graph.vertices());
      distances.setFinalized(minVertex);

      updateDistances(distances, graph, minVertex);
    }
    return distances;
  }

  @Override
  public void updateDistances(DistancesWithFinalization distances, Graph graph, int minVertex) {
    for (int vertex = 0; vertex < graph.vertices(); vertex++) {
      if (!distances.isFinalized(vertex)
              && graph.getNode(minVertex, vertex) != 0
              && !distances.isInfinite(minVertex)
              && distances.getDistance(minVertex) + graph.getNode(minVertex, vertex) < distances.getDistance(vertex)) {
        distances.setDistance(vertex, distances.getDistance(minVertex) + graph.getNode(minVertex, vertex));
      }
    }
  }
}
