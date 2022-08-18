package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.MatrixGraph;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BellmanFordSerialTest extends ShortestPathTest {
  private static final int SOURCE_NODE = 0;

  private final ShortestPath shortestPath = new BellmanFordSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }

  @Override
  public int sourceNode() {
    return SOURCE_NODE;
  }

  @Test
  void computeWithCycles() {
    var graph = new MatrixGraph(4);
    graph.addBidirectionalEdge(0, 1, 2);
    graph.addBidirectionalEdge(1, 2, -1);
    graph.addBidirectionalEdge(2, 3, 5);

    var actual = shortestPath.compute(graph, SOURCE_NODE);

    var expected = new Distances(IntStream.of(0, -4, -3, 2).toArray());
    assertEquals(expected, actual);
  }

  @Test
  void name() {
    assertEquals("Bellman-Ford serial", shortestPath.name());
  }
}