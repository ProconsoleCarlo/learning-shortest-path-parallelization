package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.algorithm.ShortestPath;
import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult;

import java.time.Duration;
import java.time.Instant;

public class SerialParallelComparator {
  private final ShortestPath serial;
  private final ShortestPath parallel;

  public SerialParallelComparator(ShortestPath serial, ShortestPath parallel) {
    this.serial = serial;
    this.parallel = parallel;
  }

  public SerialParallelResult compareWith(Graph graph, int sourceNode) {
    var serialResult = countMillisFor(serial, graph, sourceNode);
    var parallelResult = countMillisFor(parallel, graph, sourceNode);

    return new SerialParallelResult(
            serialResult.toAlgorithm(),
            parallelResult.toAlgorithm(),
            computeSpeedUp(serialResult, parallelResult),
            checkCorrectness(serialResult, parallelResult)
    );
  }

  private AlgorithmResult countMillisFor(ShortestPath algorithm, Graph graph, int sourceNode) {
    var start = Instant.now();
    var distances = algorithm.compute(graph, sourceNode);
    var end = Instant.now();
    return new AlgorithmResult(algorithm.name(), Duration.between(start, end).toMillis(), distances);
  }

  private float computeSpeedUp(AlgorithmResult serial, AlgorithmResult parallel) {
    return (float) serial.millis() / parallel.millis();
  }

  private boolean checkCorrectness(AlgorithmResult serial, AlgorithmResult parallel) {
    return serial.distances.equals(parallel.distances);
  }

  public record AlgorithmResult(
          String name,
          long millis,
          Distances distances
  ) {
    public SerialParallelResult.Algorithm toAlgorithm() {
      return new SerialParallelResult.Algorithm(name, millis);
    }
  }
}
