package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.algorithm.ShortestPath;
import it.proconsole.learning.shortestpath.parallelization.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.result.Algorithm;
import it.proconsole.learning.shortestpath.parallelization.result.ComparatorResult;
import it.proconsole.learning.shortestpath.parallelization.util.InstantProvider;

import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AlgorithmComparator {
  private final List<ShortestPath> algorithms;
  private final InstantProvider instantProvider;

  public AlgorithmComparator(InstantProvider instantProvider, ShortestPath... algorithms) {
    this.instantProvider = instantProvider;
    this.algorithms = List.of(algorithms);
  }

  public AlgorithmComparator(ShortestPath... algorithms) {
    this.instantProvider = new InstantProvider(Clock.systemUTC());
    this.algorithms = List.of(algorithms);
  }

  public ComparatorResult compareWith(Graph graph, int sourceNode) {
    var algorithmResults = algorithms.stream()
            .map(algorithm -> computeWithDuration(algorithm, graph, sourceNode))
            .toList();
    return new ComparatorResult(
            getAlgorithms(algorithmResults),
            checkCorrectness(algorithmResults)
    );
  }

  private List<Algorithm> getAlgorithms(List<AlgorithmResult> algorithmResults) {
    return algorithmResults.stream()
            .map(AlgorithmResult::toAlgorithm)
            .toList();
  }

  private boolean checkCorrectness(List<AlgorithmResult> algorithmResults) {
    return algorithmResults.stream().findFirst()
            .map(AlgorithmResult::distances)
            .map(firstResult -> algorithmResults.stream()
                    .skip(1)
                    .allMatch(it -> Arrays.equals(it.distances().values(), firstResult.values()))
            ).orElse(false);
  }

  private AlgorithmResult computeWithDuration(ShortestPath algorithm, Graph graph, int sourceNode) {
    var start = instantProvider.now();
    var distances = algorithm.compute(graph, sourceNode);
    var end = instantProvider.now();
    return new AlgorithmResult(algorithm.name(), Duration.between(start, end).toMillis(), distances);
  }
}
