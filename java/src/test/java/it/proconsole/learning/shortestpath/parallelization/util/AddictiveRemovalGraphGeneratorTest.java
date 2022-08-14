package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import it.proconsole.learning.shortestpath.parallelization.model.GraphFactory;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddictiveRemovalGraphGeneratorTest {
  private static final int VERTICES = 100;
  private static List<Graph> GRAPHS;

  private final Random random = new Random();

  @Mock
  private GraphFactory graphFactory;
  @Mock
  private EdgeWeightGenerator edgeWeightGenerator;

  private GraphGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new AddictiveRemovalGraphGenerator(graphFactory, edgeWeightGenerator);

    GRAPHS = List.of(new MatrixGraph(VERTICES), new AdjacencyMapGraph(VERTICES));
  }

  @Test
  void generateAGraphWithNumberOfEdges() {
    GRAPHS.forEach(graph -> {
      when(graphFactory.create(VERTICES)).thenReturn(graph);
      when(edgeWeightGenerator.getValue()).thenReturn(random.nextInt());
      var edges = 50;

      var current = generator.generate(VERTICES, edges);

      assertMainDiagonalHasZeros(current, edges);
    });
  }

  @ParameterizedTest
  @ValueSource(floats = {0.1f, 0.9f})
  void generateAGraphWithEdgeDensity(float density) {
    GRAPHS.forEach(graph -> {
      when(graphFactory.create(VERTICES)).thenReturn(graph);
      when(edgeWeightGenerator.getValue()).thenReturn(random.nextInt());

      var current = generator.generate(VERTICES, density);

      var expectedEdges = (int) ((VERTICES * VERTICES - VERTICES) / 2 * density);
      assertMainDiagonalHasZeros(current, expectedEdges);
    });
  }

  private void assertMainDiagonalHasZeros(Graph graph, int expectedEdges) {
    var actualEdges = 0;
    for (var i = 0; i < graph.vertices(); i++) {
      if (graph.getCost(i, i) != Graph.ZERO_WEIGHT) {
        fail("Main diagonal has some non zero values");
      }
      for (var j = 0; j < i; j++) {
        if (graph.getCost(i, j) != graph.getCost(j, i)) {
          fail("Matrix is not symmetric");
        }
        if (graph.getCost(i, j) != Graph.ZERO_WEIGHT) {
          actualEdges++;
        }
      }
    }
    assertEquals(expectedEdges, actualEdges);
  }
}