package it.proconsole.learning.shortestpath.parallelization.result.printer;

import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.ComparatorResult;
import it.proconsole.learning.shortestpath.parallelization.result.SerialParallelResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.stream.Collectors;

public class LoggerResultPrinter {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public String print(ComparatorResult result) {
    var algorithmResults = result.algorithms().stream()
            .sorted(Comparator.comparingLong(Algorithm::millis))
            .map(this::printTiming)
            .collect(Collectors.joining("\n"));
    return """
            %s
            %s
            """.formatted(algorithmResults, printCorrectness(result));
  }

  public String print(SerialParallelResult result) {
    var serialOutput = printTiming(result.serial());
    var parallelOutput = printTiming(result.parallel());
    var correctness = printCorrectness(result);
    var speedUp = printSpeedUp(result);
    var message = """
            %s
            %s
            %s
            %s""".formatted(serialOutput, parallelOutput, correctness, speedUp);
    logger.info(message);
    return message;
  }

  private String printTiming(Algorithm algorithm) {
    return "%s took %d millis".formatted(algorithm.name(), algorithm.millis());
  }

  private String printCorrectness(ComparatorResult result) {
    return result.correct()
            ? "Algorithms execution is CORRECT (same results)"
            : "Algorithms execution is WRONG (different results)";
  }

  private String printCorrectness(SerialParallelResult result) {
    return result.correct()
            ? "Parallel execution is CORRECT (same result as serial)"
            : "Parallel execution is WRONG (different result from serial)";
  }

  private String printSpeedUp(SerialParallelResult result) {
    if (result.speedUp() < 1) {
      return "Parallel execution is %s times SLOWER that serial".formatted(result.speedUp());
    } else if (result.speedUp() == 1) {
      return "Parallel execution has the SAME speed of serial";
    } else {
      return "Parallel execution is %s FASTER that serial".formatted(result.speedUp());
    }
  }
}
