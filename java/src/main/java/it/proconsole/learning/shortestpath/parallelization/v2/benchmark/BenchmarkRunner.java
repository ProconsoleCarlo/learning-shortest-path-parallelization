package it.proconsole.learning.shortestpath.parallelization.v2.benchmark;

import it.proconsole.learning.shortestpath.parallelization.v2.algorithm.ShortestPathAlgorithm;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.GraphGenerator;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Benchmark runner for comparing the shortest path algorithm implementations. Executes warmup and
 * measurement iterations, validates correctness, and collects statistics.
 */
public final class BenchmarkRunner {

  private final BenchmarkConfig config;
  private final GraphGenerator graphGenerator;
  private final List<ShortestPathAlgorithm> algorithms;

  public BenchmarkRunner(
      BenchmarkConfig config,
      GraphGenerator graphGenerator,
      List<ShortestPathAlgorithm> algorithms) {
    this.config = config;
    this.graphGenerator = graphGenerator;
    this.algorithms = algorithms;
  }

  /** Executes the benchmark and returns results for all algorithms. */
  public void run() {
    System.out.println("=".repeat(80));
    System.out.println("BENCHMARK CONFIGURATION");
    System.out.println("=".repeat(80));
    System.out.println(config);
    System.out.println();

    // Generate graph
    System.out.println("Generating graph...");
    Graph graph =
        graphGenerator.generate(
            config.getVertices(),
            config.getDensity(),
            config.getMinWeight(),
            config.getMaxWeight(),
            false,
            Graph.GraphType.ADJACENCY_LIST);
    System.out.printf(
        "Graph generated: %d vertices, %d edges, type=%s, density=%.2f%n",
        graph.vertexCount(), graph.edgeCount(), graph.getType(), config.getDensity());
    System.out.println();

    List<BenchmarkResult> results = new ArrayList<>();

    for (ShortestPathAlgorithm algorithm : algorithms) {
      System.out.println("-".repeat(80));
      System.out.println("Algorithm: " + algorithm.getName());
      System.out.println("-".repeat(80));

      // Warmup
      if (config.getWarmupIterations() > 0) {
        System.out.println("Warmup (" + config.getWarmupIterations() + " iterations)...");
        for (int i = 0; i < config.getWarmupIterations(); i++) {
          algorithm.compute(graph, config.getSourceVertex());
        }
        System.out.println("Warmup completed.");
      }

      // Measurement
      System.out.println(
          "Running " + config.getMeasurementIterations() + " measurement iterations...");
      long totalTime = 0;
      ShortestPathResult lastResult = null;

      for (int i = 0; i < config.getMeasurementIterations(); i++) {
        long startTime = System.nanoTime();
        lastResult = algorithm.compute(graph, config.getSourceVertex());
        long endTime = System.nanoTime();

        long iterationTime = (endTime - startTime) / 1_000_000; // Convert to ms
        totalTime += iterationTime;

        System.out.printf("  Iteration %d: %d ms%n", i + 1, iterationTime);
      }

      long avgTime = totalTime / config.getMeasurementIterations();
      System.out.printf("Average time: %d ms%n", avgTime);

      results.add(
          new BenchmarkResult(
              algorithm.getName(),
              avgTime,
              lastResult != null ? lastResult.getDistances() : null,
              algorithm.isParallel()));

      System.out.println();
    }

    // Validate results
    if (config.isValidateResults() && results.size() > 1) {
      System.out.println("=".repeat(80));
      System.out.println("VALIDATING RESULTS");
      System.out.println("=".repeat(80));

      boolean allCorrect = validateResults(results);
      if (allCorrect) {
        System.out.println("✓ All algorithms produced identical results!");
      } else {
        System.out.println("✗ WARNING: Algorithms produced different results!");
      }
      System.out.println();
    }

    // Summary
    printSummary(results);
  }

  /** Validates that all algorithms produced the same results. */
  private boolean validateResults(List<BenchmarkResult> results) {
    if (results.isEmpty()) {
      return true;
    }

    int[] referenceDistances = results.get(0).distances();

    for (int i = 1; i < results.size(); i++) {
      int[] distances = results.get(i).distances();
      if (!Arrays.equals(referenceDistances, distances)) {
        System.out.printf(
            "✗ Mismatch between %s and %s%n",
            results.get(0).algorithmName(), results.get(i).algorithmName());
        return false;
      }
    }

    return true;
  }

  /** Prints a summary table of benchmark results. */
  private void printSummary(List<BenchmarkResult> results) {
    System.out.println("=".repeat(80));
    System.out.println("BENCHMARK SUMMARY");
    System.out.println("=".repeat(80));

    System.out.printf("%-40s %15s %15s %15s%n", "Algorithm", "Time (ms)", "Speedup", "Efficiency");
    System.out.println("-".repeat(80));

    for (BenchmarkResult result : results) {
      // Find the appropriate baseline for this algorithm
      BenchmarkResult baseline = findBaselineForAlgorithm(results, result);

      double speedup = (double) baseline.executionTimeMs() / result.executionTimeMs();
      double efficiency =
          result.isParallel() ? speedup / Runtime.getRuntime().availableProcessors() * 100 : 100.0;

      System.out.printf(
          "%-40s %15d %15.2fx %14.1f%%%n",
          result.algorithmName(), result.executionTimeMs(), speedup, efficiency);
    }

    System.out.println("=".repeat(80));
  }

  /**
   * Finds the appropriate baseline (serial version) for an algorithm. For serial algorithms, the
   * baseline is itself. For parallel algorithms, finds the matching serial version by algorithm
   * family.
   */
  private BenchmarkResult findBaselineForAlgorithm(
      List<BenchmarkResult> results, BenchmarkResult algorithm) {
    // If it's already serial, it's its own baseline
    if (!algorithm.isParallel()) {
      return algorithm;
    }

    // For parallel algorithms, find the serial version of the same algorithm family
    String algorithmName = algorithm.algorithmName().toLowerCase();

    // Determine algorithm family
    String family = null;
    if (algorithmName.contains("dijkstra")) {
      family = "dijkstra";
    } else if (algorithmName.contains("bellman")) {
      family = "bellman";
    }

    // Find serial version of the same family
    if (family != null) {
      final String searchFamily = family;
      return results.stream()
          .filter(r -> !r.isParallel())
          .filter(r -> r.algorithmName().toLowerCase().contains(searchFamily))
          .findFirst()
          .orElse(algorithm); // Fallback to itself if no serial version found
    }

    // Default fallback: first serial algorithm
    return results.stream().filter(r -> !r.isParallel()).findFirst().orElse(algorithm);
  }
}
