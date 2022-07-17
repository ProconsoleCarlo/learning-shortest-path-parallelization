package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerResultPrinterTest {
  private static final String SERIAL_SHORTEST_PATH_NAME = "Serial";
  private static final String PARALLEL_SHORTEST_PATH_NAME = "Parallel";
  private static final int SERIAL_MILLIS = 10;
  private static final int PARALLEL_MILLIS = 5;

  private final LoggerResultPrinter printer = new LoggerResultPrinter();

  private static List<Arguments> speedUpCases() {
    return List.of(
            Arguments.of(0.5F, "Parallel execution is 0.5 times SLOWER that serial"),
            Arguments.of(1F, "Parallel execution has the SAME speed of serial"),
            Arguments.of(1.5F, "Parallel execution is 1.5 FASTER that serial")
    );
  }

  @ParameterizedTest
  @MethodSource("speedUpCases")
  void whenSpeedUpIsLessThan1(float speedUp, String speedUpResult) {
    var result = new SerialParallelResult(
            new SerialParallelResult.Algorithm(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS),
            new SerialParallelResult.Algorithm(PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS),
            speedUp,
            true
    );

    var actual = printer.print(result);

    var expected = """
            %s took %d millis
            %s took %d millis
            Parallel execution is CORRECT (same result as serial)
            %s"""
            .formatted(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS,
                    PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS,
                    speedUpResult
            );

    assertEquals(expected, actual);
  }

  @Test
  void whenParallelResultIsNotCorrect() {
    var speedUp = 1.5F;
    var result = new SerialParallelResult(
            new SerialParallelResult.Algorithm(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS),
            new SerialParallelResult.Algorithm(PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS),
            speedUp,
            false
    );

    var actual = printer.print(result);

    var expected = """
            %s took %d millis
            %s took %d millis
            Parallel execution is WRONG (different result from serial)
            Parallel execution is %s FASTER that serial"""
            .formatted(SERIAL_SHORTEST_PATH_NAME, SERIAL_MILLIS,
                    PARALLEL_SHORTEST_PATH_NAME, PARALLEL_MILLIS,
                    speedUp
            );

    assertEquals(expected, actual);
  }
}