package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class ShortestPathTest {
  public abstract int sourceNode();

  public abstract ShortestPath shortestPath();

  @Test
  void compute() {
    var graph = givenAGraph();

    var actual = shortestPath().compute(graph, sourceNode());

    var expected = new Distances(IntStream.of(0, 3, 5, 7, 9, 9, 6, 4).toArray());
    assertEquals(expected, actual);
  }

  Graph givenAGraph() {
    var graph = new AdjacencyMapGraph(8);
    graph.addEdge(0, 1, 3);
    graph.addEdge(0, 7, 4);
    graph.addEdge(1, 0, 3);
    graph.addEdge(1, 2, 2);
    graph.addEdge(1, 3, 5);
    graph.addEdge(2, 1, 2);
    graph.addEdge(2, 4, 4);
    graph.addEdge(2, 5, 5);
    graph.addEdge(3, 1, 5);
    graph.addEdge(3, 6, 2);
    graph.addEdge(3, 7, 3);
    graph.addEdge(4, 2, 4);
    graph.addEdge(4, 5, 5);
    graph.addEdge(5, 2, 5);
    graph.addEdge(5, 4, 5);
    graph.addEdge(5, 7, 5);
    graph.addEdge(6, 3, 2);
    graph.addEdge(6, 7, 2);
    graph.addEdge(7, 0, 4);
    graph.addEdge(7, 3, 3);
    graph.addEdge(7, 5, 5);
    graph.addEdge(7, 6, 2);
    return graph;
  }
}
