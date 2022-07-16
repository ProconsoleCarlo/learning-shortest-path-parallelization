package it.proconsole.learning.shortestpath.parallelization.model;

public record SerialParallelResult(
        Algorithm serial,
        Algorithm parallel,
        float speedUp,
        boolean correct
) {
  public record Algorithm(
          String name,
          long millis
  ) {
  }
}

