package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult;
import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerResultPrinter {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
