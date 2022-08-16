package it.proconsole.learning.shortestpath.parallelization.result.comparator;

public record SerialParallelResult(
        Algorithm serial,
        Algorithm parallel,
        float speedUp,
        boolean correct
) {
}

