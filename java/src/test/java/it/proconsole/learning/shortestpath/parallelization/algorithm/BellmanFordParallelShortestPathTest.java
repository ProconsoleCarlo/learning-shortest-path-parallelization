package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.exception.NegativeEdgesException;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BellmanFordParallelShortestPathTest extends ShortestPathTest {
  private static final int SOURCE_NODE = 0;

  private final ShortestPath shortestPath = new BellmanFordParallelShortestPath();

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
    var graph = new MatrixGraph(2);
    graph.setSymmetricNode(0, 1, -1);
    var shortestPath = shortestPath();

    assertThrows(NegativeEdgesException.class, () -> shortestPath.compute(graph, SOURCE_NODE));
  }
}