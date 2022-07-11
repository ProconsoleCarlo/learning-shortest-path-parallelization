package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.stream.IntStream;

public class DijkstraSerial implements ShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    var distances = new DistancesWithFinalization(graph.length(), sourceNode);

    var vertices = graph.length();
    for (var count = 0; count < vertices - 1; count++) {
      var minVertex = getMinDistanceSerial(distances, vertices);
      distances.setFinalized(minVertex);

      updateDistancesSerial(vertices, distances, graph, minVertex);

    }
    return distances;
  }

  private int getMinDistanceSerial(DistancesWithFinalization distances, int vertices) {
    var minVertex = 0;
    var minDistance = Integer.MAX_VALUE;
    for (var vertex = 0; vertex < vertices; vertex++) {
      if (!distances.isFinalized(vertex) && distances.getDistance(vertex) <= minDistance) {
        minDistance = distances.getDistance(vertex);
        minVertex = vertex;
      }
    }
    return minVertex;
  }

  private void updateDistancesSerial(int vertices, DistancesWithFinalization distances, Graph graph, int minVertex) {
    for (int vertex = 0; vertex < vertices; vertex++) {
      if (!distances.isFinalized(vertex) && graph.getNode(minVertex, vertex) != 0 && !distances.isInfinite(minVertex) && distances.getDistance(minVertex) + graph.getNode(minVertex, vertex) < distances.getDistance(vertex)) {
        distances.setDistance(vertex, distances.getDistance(minVertex) + graph.getNode(minVertex, vertex));
      }
    }
    IntStream.range(0, vertices)
            .filter(vertex -> !distances.isFinalized(vertex)
                    && graph.getNode(minVertex, vertex) != 0
                    && !distances.isInfinite(minVertex)
                    && distances.getDistance(minVertex) + graph.getNode(minVertex, vertex) < distances.getDistance(vertex))
            .forEach(vertex -> distances.setDistance(vertex, distances.getDistance(minVertex) + graph.getNode(minVertex, vertex)));
  }
}
