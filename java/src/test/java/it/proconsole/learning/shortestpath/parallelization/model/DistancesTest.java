package it.proconsole.learning.shortestpath.parallelization.model;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistancesTest {
  private static final int VERTICES = 5;
  private static final int SOURCE_NODE = 0;

  private final Distances distances = new Distances(VERTICES, SOURCE_NODE);

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
    var node = 1;
    var distance = 9;

    distances.setDistance(node, distance);

    assertEquals(distance, distances.getDistance(node));
  }

  @Test
  void isInfinite() {
    var node = 1;
    var distance = 9;
    var infiniteDistance = Integer.MAX_VALUE;

    distances.setDistance(node, distance);

    assertFalse(distances.isInfinite(node));

    distances.setDistance(node, infiniteDistance);

    assertTrue(distances.isInfinite(node));
  }
}