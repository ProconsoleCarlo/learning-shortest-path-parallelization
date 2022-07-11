package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistancesWithFinalizationTest extends DistancesTest {
  private static final int SOURCE_NODE = 0;
  private static final int A_NODE = 1;
  private static final int VERTICES = 5;

  private final DistancesWithFinalization distances = new DistancesWithFinalization(VERTICES, SOURCE_NODE);

  @Test
  void atBeginningAllNodesAreNotFinalized() {
    IntStream.range(0, VERTICES)
            .forEach(node -> assertFalse(distances.isFinalized(node)));
  }

  @Test
  void setAndGetFinalized() {
    assertFalse(distances.isFinalized(A_NODE));

    distances.setFinalized(A_NODE);

    assertTrue(distances.isFinalized(A_NODE));
  }
}