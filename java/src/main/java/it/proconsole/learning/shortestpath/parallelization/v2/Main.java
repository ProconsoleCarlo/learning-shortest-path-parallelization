package it.proconsole.learning.shortestpath.parallelization.v2;

import it.proconsole.learning.shortestpath.parallelization.v2.algorithm.*;
import it.proconsole.learning.shortestpath.parallelization.v2.benchmark.BenchmarkConfig;
import it.proconsole.learning.shortestpath.parallelization.v2.benchmark.BenchmarkRunner;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.GraphGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Main entry point for the v2 shortest path parallelization benchmark. Usage: java Main [options]
 * Options: --vertices=N Number of vertices (default: 10000) --density=D Edge density 0.0-1.0
 * (default: 0.3) --minWeight=W Minimum edge weight (default: 1) --maxWeight=W Maximum edge weight
 * (default: 100) --warmup=N Warmup iterations (default: 3) --iterations=N Measurement iterations
 * (default: 10) --source=N Source vertex (default: 0) --threads=N Number of threads (default:
 * available processors) --delta=D Delta for Delta-Stepping (default: auto) --no-validate Skip
 * result validation
 */
public class Main {

  private static final int DEFAULT_VERTICES = 10000;
  private static final double DEFAULT_DENSITY = 0.3;
  private static final int DEFAULT_MIN_WEIGHT = 1;
  private static final int DEFAULT_MAX_WEIGHT = 100;
  private static final int DEFAULT_WARMUP = 3;
  private static final int DEFAULT_ITERATIONS = 10;
  private static final int DEFAULT_SOURCE = 0;

  public static void main(String[] args) {
    try {
      // Parse command line arguments
      CommandLineArgs cmdArgs = parseArguments(args);

      // Print header
      printHeader();

      // Configure ForkJoinPool if custom thread count specified
      ForkJoinPool customPool = null;
      if (cmdArgs.threads > 0) {
        customPool = new ForkJoinPool(cmdArgs.threads);
        System.out.println("Using custom thread pool with " + cmdArgs.threads + " threads");
      } else {
        System.out.println(
            "Using default parallelism: "
                + Runtime.getRuntime().availableProcessors()
                + " processors");
      }
      System.out.println();

      // Create benchmark configuration
      BenchmarkConfig config =
          BenchmarkConfig.builder()
              .vertices(cmdArgs.vertices)
              .density(cmdArgs.density)
              .minWeight(cmdArgs.minWeight)
              .maxWeight(cmdArgs.maxWeight)
              .warmupIterations(cmdArgs.warmup)
              .measurementIterations(cmdArgs.iterations)
              .sourceVertex(cmdArgs.source)
              .validateResults(cmdArgs.validate)
              .build();

      // Create graph generator
      GraphGenerator graphGenerator =
          cmdArgs.seed >= 0 ? new GraphGenerator(cmdArgs.seed) : new GraphGenerator();

      // Create algorithms
      List<ShortestPathAlgorithm> algorithms = createAlgorithms(customPool, cmdArgs.delta);

      // Run benchmark
      BenchmarkRunner runner = new BenchmarkRunner(config, graphGenerator, algorithms);
      runner.run();

      // Shutdown custom pool if used
      if (customPool != null) {
        customPool.shutdown();
      }

    } catch (IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
      System.err.println();
      printUsage();
      System.exit(1);
    } catch (Exception e) {
      System.err.println("Unexpected error: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static List<ShortestPathAlgorithm> createAlgorithms(ForkJoinPool customPool, int delta) {
    return Arrays.asList(
        new DijkstraSerialOptimized(),
        customPool != null
            ? new DijkstraDeltaStepping(delta, customPool)
            : new DijkstraDeltaStepping(delta),
        new BellmanFordSerial(),
        customPool != null
            ? new BellmanFordParallelDoubleBuffer(customPool, true)
            : new BellmanFordParallelDoubleBuffer(),
        customPool != null
            ? new BellmanFordParallelOptimized(customPool, true)
            : new BellmanFordParallelOptimized());
  }

  private static CommandLineArgs parseArguments(String[] args) {
    CommandLineArgs cmdArgs = new CommandLineArgs();

    for (String arg : args) {
      if (arg.equals("--help") || arg.equals("-h")) {
        printUsage();
        System.exit(0);
      } else if (arg.equals("--no-validate")) {
        cmdArgs.validate = false;
      } else if (arg.startsWith("--")) {
        String[] parts = arg.substring(2).split("=", 2);
        if (parts.length != 2) {
          throw new IllegalArgumentException("Invalid argument format: " + arg);
        }

        String key = parts[0];
        String value = parts[1];

        switch (key) {
          case "vertices" -> cmdArgs.vertices = parseInt(value, "vertices");
          case "density" -> cmdArgs.density = parseDouble(value);
          case "minWeight" -> cmdArgs.minWeight = parseInt(value, "minWeight");
          case "maxWeight" -> cmdArgs.maxWeight = parseInt(value, "maxWeight");
          case "warmup" -> cmdArgs.warmup = parseInt(value, "warmup");
          case "iterations" -> cmdArgs.iterations = parseInt(value, "iterations");
          case "source" -> cmdArgs.source = parseInt(value, "source");
          case "threads" -> cmdArgs.threads = parseInt(value, "threads");
          case "delta" -> cmdArgs.delta = parseInt(value, "delta");
          case "seed" -> cmdArgs.seed = parseLong(value);
          default -> throw new IllegalArgumentException("Unknown option: " + key);
        }
      } else {
        throw new IllegalArgumentException("Invalid argument: " + arg);
      }
    }

    return cmdArgs;
  }

  private static int parseInt(String value, String name) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(name + " must be an integer: " + value);
    }
  }

  private static long parseLong(String value) {
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("seed" + " must be a long: " + value);
    }
  }

  private static double parseDouble(String value) {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("density" + " must be a number: " + value);
    }
  }

  private static void printHeader() {
    System.out.println();
    System.out.println(
        "╔═══════════════════════════════════════════════════════════════════════════════╗");
    System.out.println(
        "║     SHORTEST PATH PARALLELIZATION BENCHMARK - V2                             ║");
    System.out.println(
        "║     Advanced parallel implementations with optimized data structures          ║");
    System.out.println(
        "╚═══════════════════════════════════════════════════════════════════════════════╝");
    System.out.println();
  }

  private static void printUsage() {
    System.out.println("Usage: java Main [options]");
    System.out.println();
    System.out.println("Options:");
    System.out.println(
        "  --vertices=N          Number of vertices (default: " + DEFAULT_VERTICES + ")");
    System.out.println(
        "  --density=D           Edge density 0.0-1.0 (default: " + DEFAULT_DENSITY + ")");
    System.out.println(
        "  --minWeight=W         Minimum edge weight (default: " + DEFAULT_MIN_WEIGHT + ")");
    System.out.println(
        "  --maxWeight=W         Maximum edge weight (default: " + DEFAULT_MAX_WEIGHT + ")");
    System.out.println(
        "  --warmup=N            Warmup iterations (default: " + DEFAULT_WARMUP + ")");
    System.out.println(
        "  --iterations=N        Measurement iterations (default: " + DEFAULT_ITERATIONS + ")");
    System.out.println("  --source=N            Source vertex (default: " + DEFAULT_SOURCE + ")");
    System.out.println("  --threads=N           Number of threads (default: available processors)");
    System.out.println("  --delta=D             Delta for Delta-Stepping (default: auto)");
    System.out.println("  --seed=S              Random seed for reproducibility (default: random)");
    System.out.println("  --no-validate         Skip result validation");
    System.out.println("  --help, -h            Show this help message");
    System.out.println();
    System.out.println("Examples:");
    System.out.println("  java Main --vertices=5000 --density=0.4");
    System.out.println("  java Main --vertices=10000 --threads=8 --delta=50");
    System.out.println("  java Main --vertices=1000 --density=0.8 --iterations=20");
  }

  private static class CommandLineArgs {
    int vertices = DEFAULT_VERTICES;
    double density = DEFAULT_DENSITY;
    int minWeight = DEFAULT_MIN_WEIGHT;
    int maxWeight = DEFAULT_MAX_WEIGHT;
    int warmup = DEFAULT_WARMUP;
    int iterations = DEFAULT_ITERATIONS;
    int source = DEFAULT_SOURCE;
    int threads = -1; // -1 means use default
    int delta = 0; // 0 means auto
    long seed = -1; // -1 means random
    boolean validate = true;
  }
}
