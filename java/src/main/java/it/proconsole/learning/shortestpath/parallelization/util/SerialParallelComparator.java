package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.algorithm.ShortestPath;
import it.proconsole.learning.shortestpath.parallelization.model.Distances;
import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult;
import it.proconsole.learning.shortestpath.parallelization.model.SerialParallelResult.Algorithm;

import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;

public class SerialParallelComparator {
  private final ShortestPath serial;
  private final ShortestPath parallel;
  private final InstantProvider instantProvider;

  public SerialParallelComparator(ShortestPath serial, ShortestPath parallel, InstantProvider instantProvider) {
    this.serial = serial;
    this.parallel = parallel;
    this.instantProvider = instantProvider;
  }

  public SerialParallelComparator(ShortestPath serial, ShortestPath parallel) {
    this.serial = serial;
    this.parallel = parallel;
    this.instantProvider = new InstantProvider(Clock.systemUTC());
  }

  public SerialParallelResult compareWith(Graph graph, int sourceNode) {
    var serialResult = computeWithDuration(serial, graph, sourceNode);
    var parallelResult = computeWithDuration(parallel, graph, sourceNode);

    return new SerialParallelResult(
            serialResult.toAlgorithm(),
            parallelResult.toAlgorithm(),
            computeSpeedUp(serialResult, parallelResult),
            checkCorrectness(serialResult, parallelResult)
    );
  }

  private AlgorithmResult computeWithDuration(ShortestPath algorithm, Graph graph, int sourceNode) {
    var start = instantProvider.now();
    var distances = algorithm.compute(graph, sourceNode);
    //System.err.println(distances);
    var end = instantProvider.now();
    return new AlgorithmResult(algorithm.name(), Duration.between(start, end).toMillis(), distances);
  }

  private float computeSpeedUp(AlgorithmResult serial, AlgorithmResult parallel) {
    return (float) serial.millis() / parallel.millis();
  }

  private boolean checkCorrectness(AlgorithmResult serial, AlgorithmResult parallel) {
    return Arrays.equals(serial.distances.getValues(), parallel.distances.getValues());
  }

  public record AlgorithmResult(
          String name,
          long millis,
          Distances distances
  ) {
    public Algorithm toAlgorithm() {
      return new Algorithm(name, millis);
    }
  }
}
