package it.proconsole.learning.shortestpath.parallelization.algorithm;

import it.proconsole.learning.shortestpath.parallelization.model.DistancesWithFinalization;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraSerialTest extends ShortestPathTest {
  private static final int SOURCE_NODE = 0;

  private final ShortestPath shortestPath = new DijkstraSerial();

  @Override
  public ShortestPath shortestPath() {
    return shortestPath;
  }

  @Override
  public int sourceNode() {
    return SOURCE_NODE;
  }

  @Test
  void allNodesAreFinalized() {
    var graph = givenAGraph();

    var actual = shortestPath().compute(graph, sourceNode());

    assertTrue(actual instanceof DistancesWithFinalization);
    IntStream.range(0, graph.length())
            .forEach(x -> assertTrue(((DistancesWithFinalization) actual).isFinalized(x)));
  }
}