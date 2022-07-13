package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

interface BellmanFordShortestPath extends ShortestPath {
  Logger logger = LoggerFactory.getLogger(BellmanFordShortestPath.class);

  default void checkCyclesPresence(Graph graph, Distances distances) {
    var vertices = graph.vertices();
    var cycles = IntStream.range(0, vertices)
            .mapToLong(src -> IntStream.range(0, vertices)
                    .filter(dest -> isDistanceNotFinalized(graph, distances, src, dest))
                    .count()
            ).sum();
    if (cycles != 0) {
      logger.warn("The graph contains negative edge cycle {} times followed!", cycles);
    }
  }

  default boolean isDistanceNotFinalized(Graph graph, Distances distances, int src, int dest) {
    return !graph.isNodeZero(src, dest) && isDistanceMoreThanCost(graph, distances, src, dest);
  }

  private boolean isDistanceMoreThanCost(Graph graph, Distances distances, int src, int dest) {
    return distances.getDistance(dest) > distances.getDistance(src) + graph.getNode(src, dest);
  }
}
