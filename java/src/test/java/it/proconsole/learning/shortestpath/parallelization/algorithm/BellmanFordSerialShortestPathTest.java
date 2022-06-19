package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BellmanFordSerialShortestPathTest {
  private static final int SOURCE_NODE = 0;

  private final ShortestPath shortestPath = new BellmanFordSerialShortestPath();

  @Test
  void compute() {
    var graph = new MatrixGraph(4);
    graph.setSymmetricNode(0, 1, 2);
    graph.setSymmetricNode(1, 2, 3);
    graph.setSymmetricNode(2, 3, 5);

    var actual = shortestPath.compute(graph, SOURCE_NODE);

    var expected = new Distances(4, SOURCE_NODE);
    expected.setDistance(1, 2);
    expected.setDistance(2, 5);
    expected.setDistance(3, 10);
    assertEquals(expected, actual);
  }

  @Test
  void computeWithCycles() {
    var graph = new MatrixGraph(4);
    graph.setSymmetricNode(0, 1, 2);
    graph.setSymmetricNode(1, 2, -1);
    graph.setSymmetricNode(2, 3, 5);

    var actual = shortestPath.compute(graph, SOURCE_NODE);

    var expected = new Distances(4, SOURCE_NODE);
    expected.setDistance(1, -4);
    expected.setDistance(2, -3);
    expected.setDistance(3, 2);
    assertEquals(expected, actual);
  }
}