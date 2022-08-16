package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import it.proconsole.learning.shortestpath.parallelization.model.Distances;

public record AlgorithmResult(
        String name,
        long millis,
        Distances distances
) {
  public Algorithm toAlgorithm() {
    return new Algorithm(name, millis);
  }
}
