package it.proconsole.learning.shortestpath.parallelization.graph.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.random.RandomGenerator;

import static it.proconsole.learning.shortestpath.parallelization.graph.Graph.ZERO_WEIGHT;
import static it.proconsole.learning.shortestpath.parallelization.graph.generator.EdgeWeightGenerator.Builder.aEdgeWeightGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EdgeWeightGeneratorTest {
  private static final int MAX_WEIGHT = 5;
  private static final int MAX_NEGATIVE_WEIGHT = -3;
  private static final int A_POSITIVE_WEIGHT = 4;
  private static final int A_NEGATIVE_WEIGHT = -1;

  @Mock
  private RandomGenerator randomGenerator;

  @Test
  void getPositiveWeight() {
    var generator = aEdgeWeightGenerator(randomGenerator).build();
    when(randomGenerator.nextInt(anyInt(), anyInt())).thenReturn(A_POSITIVE_WEIGHT);

    var actual = generator.getValue();

    assertEquals(A_POSITIVE_WEIGHT, actual);
    verifyNoMoreInteractions(randomGenerator);
  }

  @Test
  void getMaxPositiveWeight() {
    var generator = aEdgeWeightGenerator(randomGenerator).withMaxWeight(MAX_WEIGHT).build();
    when(randomGenerator.nextInt(anyInt(), anyInt())).thenReturn(ZERO_WEIGHT);

    var actual = generator.getValue();

    assertEquals(MAX_WEIGHT, actual);
    verifyNoMoreInteractions(randomGenerator);
  }

  @Test
  void getNegativeWeight() {
    var generator = aEdgeWeightGenerator(randomGenerator).withMaxNegativeWeight(MAX_NEGATIVE_WEIGHT).build();
    when(randomGenerator.nextInt(anyInt(), anyInt()))
            .thenReturn(ZERO_WEIGHT)
            .thenReturn(A_NEGATIVE_WEIGHT);

    var actual = generator.getValue();

    assertEquals(A_NEGATIVE_WEIGHT, actual);
    verifyNoMoreInteractions(randomGenerator);
  }

  @Test
  void throwIfInvalidWeights() {
    var generator = aEdgeWeightGenerator(randomGenerator);
    assertThrows(IllegalArgumentException.class, () -> generator.withMaxWeight(A_NEGATIVE_WEIGHT));
    assertThrows(IllegalArgumentException.class, () -> generator.withMaxNegativeWeight(A_POSITIVE_WEIGHT));
  }
}