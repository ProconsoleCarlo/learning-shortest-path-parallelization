package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.algorithm.ShortestPath;
import it.proconsole.learning.shortestpath.parallelization.graph.Distances;
import it.proconsole.learning.shortestpath.parallelization.graph.MatrixGraph;
import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.SerialParallelResult;
import it.proconsole.learning.shortestpath.parallelization.util.InstantProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SerialParallelComparatorTest {
  private static final int VERTICES = 5;
  private static final int SOURCE_NODE = 0;
  private static final String SERIAL_SHORTEST_PATH_NAME = "Serial";
  private static final String PARALLEL_SHORTEST_PATH_NAME = "Parallel";
  private static final Instant SERIAL_START_MILLIS = Instant.ofEpochMilli(10);
  private static final Instant PARALLEL_START_MILLIS = Instant.ofEpochMilli(30);
  private static final int SERIAL_MILLIS = 10;
  private static final int PARALLEL_MILLIS = 5;
  private static final int SPEED_UP = 2;

  @Mock
  private ShortestPath serialShortestPath;
  @Mock
  private ShortestPath parallelShortestPath;
  @Mock
  private InstantProvider instantProvider;

  private SerialParallelComparator comparator;

  @BeforeEach
  void setUp() {
    comparator = new SerialParallelComparator(serialShortestPath, parallelShortestPath, instantProvider);
  }

  @Test
  void compareWhenParallelIsCorrect() {
    var graph = new MatrixGraph(VERTICES);
    var distances = new Distances(VERTICES, SOURCE_NODE);
    when(instantProvider.now()).thenReturn(
            SERIAL_START_MILLIS,
            SERIAL_START_MILLIS.plusMillis(SERIAL_MILLIS),
            PARALLEL_START_MILLIS,
            PARALLEL_START_MILLIS.plusMillis(PARALLEL_MILLIS)
    );
    when(serialShortestPath.compute(graph, SOURCE_NODE)).thenReturn(distances);
    when(serialShortestPath.name()).thenReturn(SERIAL_SHORTEST_PATH_NAME);
    when(parallelShortestPath.compute(graph, SOURCE_NODE)).thenReturn(distances);
    when(parallelShortestPath.name()).thenReturn(PARALLEL_SHORTEST_PATH_NAME);

    var actual = comparator.compareWith(graph, SOURCE_NODE);

    var expected = new SerialParallelResult(
            new Algorithm(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS),
            new Algorithm(PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS),
            SPEED_UP,
            true
    );
    assertEquals(expected, actual);
    verifyNoMoreInteractions(serialShortestPath, parallelShortestPath);
  }

  @Test
  void compareWhenParallelIsNotCorrect() {
    var graph = new MatrixGraph(VERTICES);
    var serialDistances = new Distances(VERTICES, SOURCE_NODE);
    var parallelDistances = new Distances(VERTICES, SOURCE_NODE);
    parallelDistances.setDistance(1, 1);
    when(instantProvider.now()).thenReturn(
            SERIAL_START_MILLIS,
            SERIAL_START_MILLIS.plusMillis(SERIAL_MILLIS),
            PARALLEL_START_MILLIS,
            PARALLEL_START_MILLIS.plusMillis(PARALLEL_MILLIS)
    );
    when(serialShortestPath.compute(graph, SOURCE_NODE)).thenReturn(serialDistances);
    when(serialShortestPath.name()).thenReturn(SERIAL_SHORTEST_PATH_NAME);
    when(parallelShortestPath.compute(graph, SOURCE_NODE)).thenReturn(parallelDistances);
    when(parallelShortestPath.name()).thenReturn(PARALLEL_SHORTEST_PATH_NAME);

    var actual = comparator.compareWith(graph, SOURCE_NODE);

    var expected = new SerialParallelResult(
            new Algorithm(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS),
            new Algorithm(PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS),
            SPEED_UP,
            false
    );
    assertEquals(expected, actual);
    verifyNoMoreInteractions(serialShortestPath, parallelShortestPath);
  }
}