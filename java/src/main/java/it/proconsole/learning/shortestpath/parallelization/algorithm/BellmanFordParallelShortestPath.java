package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.stream.IntStream;

public class BellmanFordParallelShortestPath implements BellmanFordShortestPath {
  @Override
  public Distances compute(Graph graph, int sourceNode) {
    throwsIfNegativeEdges(graph);
    var distances = new Distances(graph.vertices(), sourceNode);

    relaxEdges(graph, distances);

    if (graph.hasNegativeEdges()) {
      checkCyclesPresence(graph, distances);
    }

    return distances;
  }

  private void relaxEdges(Graph graph, Distances distances) {
    var vertices = graph.vertices();
    IntStream.range(0, vertices - 1)
            .parallel()
            .forEach(node -> IntStream.range(0, vertices)
                    .forEach(src -> IntStream.range(0, vertices)
                            .filter(dest -> !distances.isInfinite(src)
                                    && isDistanceNotFinalized(graph, distances, src, dest)
                            ).forEach(dest -> distances.setDistance(dest, distances.getDistance(src) + graph.getNode(src, dest)))
                    )
            );
  }
}
