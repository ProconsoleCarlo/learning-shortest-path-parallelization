package it.proconsole.learning.shortestpath.parallelization.result.printer;

import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.ComparatorResult;
import it.proconsole.learning.shortestpath.parallelization.result.SerialParallelResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerResultPrinterTest {
  private static final String SLOWER_SHORTEST_PATH_NAME = "Serial";
  private static final String FASTER_SHORTEST_PATH_NAME = "Parallel";
  private static final int SLOWER_MILLIS = 10;
  private static final int FASTER_MILLIS = 5;

  private final LoggerResultPrinter printer = new LoggerResultPrinter();

  private static List<Arguments> speedUpCases() {
    return List.of(
            Arguments.of(0.5F, "Parallel execution is 0.5 times SLOWER that serial"),
            Arguments.of(1F, "Parallel execution has the SAME speed of serial"),
            Arguments.of(1.5F, "Parallel execution is 1.5 FASTER that serial")
    );
  }

  private static List<Arguments> comparatorResults() {
    return List.of(
            Arguments.of(true, "Algorithms execution is CORRECT (same results)"),
            Arguments.of(false, "Algorithms execution is WRONG (different results)")
    );
  }

  @ParameterizedTest
  @MethodSource("speedUpCases")
  void whenSpeedUpIsLessThan1(float speedUp, String speedUpResult) {
    var result = new SerialParallelResult(
            new Algorithm(SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS),
            new Algorithm(FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS),
            speedUp,
            true
    );

    var actual = printer.print(result);

    var expected = """
            %s took %d millis
            %s took %d millis
            Parallel execution is CORRECT (same result as serial)
            %s"""
            .formatted(SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS,
                    FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS,
                    speedUpResult
            );

    assertEquals(expected, actual);
  }

  @Test
  void whenParallelResultIsNotCorrect() {
    var speedUp = 1.5F;
    var result = new SerialParallelResult(
            new Algorithm(SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS),
            new Algorithm(FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS),
            speedUp,
            false
    );

    var actual = printer.print(result);

    var expected = """
            %s took %d millis
            %s took %d millis
            Parallel execution is WRONG (different result from serial)
            Parallel execution is %s FASTER that serial"""
            .formatted(SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS,
                    FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS,
                    speedUp
            );

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("comparatorResults")
  void whenResultIsNotCorrect(boolean correct, String correctnessMessage) {
    var result = new ComparatorResult(
            List.of(
                    new Algorithm(SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS),
                    new Algorithm(FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS)
            ),
            correct
    );

    var actual = printer.print(result);

    var expected = """
            %s took %d millis
            %s took %d millis
            %s
            """
            .formatted(
                    FASTER_SHORTEST_PATH_NAME, FASTER_MILLIS,
                    SLOWER_SHORTEST_PATH_NAME, SLOWER_MILLIS,
                    correctnessMessage
            );
    assertEquals(expected, actual);
  }
}