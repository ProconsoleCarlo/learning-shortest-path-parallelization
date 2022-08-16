package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;
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