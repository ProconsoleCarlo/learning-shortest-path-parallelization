package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.algorithm.ShortestPath;
import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.MatrixGraph;
import it.proconsole.learning.shortestpath.parallelization.util.InstantProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlgorithmComparatorTest {
  private static final int VERTICES = 5;
  private static final int SOURCE_NODE = 0;
  private static final Distances DISTANCES = new Distances(VERTICES, SOURCE_NODE);
  private static final String FIRST_SHORTEST_PATH_NAME = "First algorithm";
  private static final String SECOND_SHORTEST_PATH_NAME = "Second algorithm";
  private static final String THIRD_SHORTEST_PATH_NAME = "Third algorithm";
  private static final Instant START_MILLIS = Instant.ofEpochMilli(10);

  @Mock
  private ShortestPath firstShortestPath;
  @Mock
  private ShortestPath secondShortestPath;
  @Mock
  private ShortestPath thirdShortestPath;
  @Mock
  private InstantProvider instantProvider;

  private AlgorithmComparator comparator;

  private static List<Arguments> distances() {
    var wrongDistances = new Distances(VERTICES, SOURCE_NODE);
    wrongDistances.setDistance(1, 1);
    return List.of(
            Arguments.of(DISTANCES, true),
            Arguments.of(wrongDistances, false)
    );
  }

  @BeforeEach
  void setUp() {
    comparator = new AlgorithmComparator(instantProvider, firstShortestPath, secondShortestPath, thirdShortestPath);
  }

  @ParameterizedTest
  @MethodSource("distances")
  void compareWith(Distances distances, boolean correct) {
    var graph = new MatrixGraph(VERTICES);
    when(instantProvider.now()).thenReturn(
            START_MILLIS,
            START_MILLIS.plusMillis(10),
            START_MILLIS,
            START_MILLIS.plusMillis(5),
            START_MILLIS,
            START_MILLIS.plusMillis(20)
    );
    when(firstShortestPath.compute(graph, SOURCE_NODE)).thenReturn(DISTANCES);
    when(firstShortestPath.name()).thenReturn(FIRST_SHORTEST_PATH_NAME);
    when(secondShortestPath.compute(graph, SOURCE_NODE)).thenReturn(distances);
    when(secondShortestPath.name()).thenReturn(SECOND_SHORTEST_PATH_NAME);
    when(thirdShortestPath.compute(graph, SOURCE_NODE)).thenReturn(DISTANCES);
    when(thirdShortestPath.name()).thenReturn(THIRD_SHORTEST_PATH_NAME);

    var current = comparator.compareWith(graph, SOURCE_NODE);

    var expected = new ComparatorResult(
            List.of(
                    new Algorithm(FIRST_SHORTEST_PATH_NAME, 10),
                    new Algorithm(SECOND_SHORTEST_PATH_NAME, 5),
                    new Algorithm(THIRD_SHORTEST_PATH_NAME, 20)
            ),
            correct
    );
    assertEquals(expected, current);
  }
}