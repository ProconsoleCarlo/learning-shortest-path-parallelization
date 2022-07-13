package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class ShortestPathTest {
  public abstract ShortestPath shortestPath();

  public abstract int sourceNode();

  @Test
  void compute() {
    var graph = givenAGraph();

    var actual = shortestPath().compute(graph, sourceNode());

    var expected = new Distances(4, sourceNode());
    expected.setDistance(1, 2);
    expected.setDistance(2, 5);
    expected.setDistance(3, 10);
    assertEquals(expected, actual);
  }

  MatrixGraph givenAGraph() {
    var graph = new MatrixGraph(4);
    graph.setSymmetricNode(0, 1, 2);
    graph.setSymmetricNode(1, 2, 3);
    graph.setSymmetricNode(2, 3, 5);
    return graph;
  }
}
