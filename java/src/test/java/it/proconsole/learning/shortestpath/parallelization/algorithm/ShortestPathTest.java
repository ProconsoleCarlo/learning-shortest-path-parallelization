package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class ShortestPathTest {
  public abstract int sourceNode();

  public abstract ShortestPath shortestPath();

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

  Graph givenAGraph() {
    var graph = new AdjacencyMapGraph(4);
    graph.addBidirectionalEdge(0, 1, 2);
    graph.addBidirectionalEdge(1, 2, 3);
    graph.addBidirectionalEdge(2, 3, 5);
    return graph;
  }
}
