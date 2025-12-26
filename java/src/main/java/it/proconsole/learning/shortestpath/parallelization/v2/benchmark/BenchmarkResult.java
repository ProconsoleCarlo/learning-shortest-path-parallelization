package it.proconsole.learning.shortestpath.parallelization.v2.benchmark;

/** Result of a single algorithm benchmark execution. */
public record BenchmarkResult(
    String algorithmName, long executionTimeMs, int[] distances, boolean isParallel) {}
