package it.proconsole.learning.shortestpath.parallelization.graph;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistancesTest {
  private static final int SOURCE_NODE = 0;
  private static final int A_NODE = 1;
  private static final int A_DISTANCE = 9;

  private final Distances distances = new Distances(5, SOURCE_NODE);

  @Test
  void atBeginningAllNodesHaveInfiniteDistanceExceptSourceNode() {
    IntStream.range(0, 5)
            .forEach(x -> {
                      var distance = distances.getDistance(x);
                      if (x == SOURCE_NODE) {
                        assertEquals(0, distance);
                      } else {
                        assertEquals(Integer.MAX_VALUE, distance);
                      }
                    }
            );
  }

  @Test
  void setAndGetDistance() {
    distances.setDistance(A_NODE, A_DISTANCE);

    assertEquals(A_DISTANCE, distances.getDistance(A_NODE));
  }

  @Test
  void isInfinite() {
    distances.setDistance(A_NODE, A_DISTANCE);

    assertFalse(distances.isInfinite(A_NODE));

    distances.setDistance(A_NODE, Integer.MAX_VALUE);

    assertTrue(distances.isInfinite(A_NODE));
  }
}