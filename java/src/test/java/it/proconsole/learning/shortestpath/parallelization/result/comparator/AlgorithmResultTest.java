package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmResultTest {
  @Test
  void toAlgorithm() {
    var algorithmResult = new AlgorithmResult("Algorithm name", 100L, new Distances(10, 0));
    var expected = new Algorithm("Algorithm name", 100L);

    assertEquals(expected, algorithmResult.toAlgorithm());
  }
}