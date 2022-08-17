package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;

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
  public String name() {
    return "Dijkstra serial";
  }

  private void updateDistances(DistancesWithFinalization distances, Graph graph, int minVertex) {
    for (int vertex = 0; vertex < graph.vertices(); vertex++) {
      if (!distances.isFinalized(vertex)
              && graph.haveConnection(minVertex, vertex)
              && !distances.isInfinite(minVertex)
              && distances.getDistance(minVertex) + graph.getCost(minVertex, vertex) < distances.getDistance(vertex)
      ) {
        distances.setDistance(vertex, distances.getDistance(minVertex) + graph.getCost(minVertex, vertex));
      }
    }
  }
}
