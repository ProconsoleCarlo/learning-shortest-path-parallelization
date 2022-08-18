package it.proconsole.learning.shortestpath.parallelization.result.printer;

import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.ComparatorResult;
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
    var message = """
            %s
            %s
            """.formatted(algorithmResults, printCorrectness(result));
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
}
