package it.proconsole.learning.shortestpath.parallelization.result.printer;

import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.ComparatorResult;
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

  private static List<Arguments> comparatorResults() {
    return List.of(
            Arguments.of(true, "Algorithms execution is CORRECT (same results)"),
            Arguments.of(false, "Algorithms execution is WRONG (different results)")
    );
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