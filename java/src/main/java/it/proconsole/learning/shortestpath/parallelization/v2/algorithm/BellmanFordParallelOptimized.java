package it.proconsole.learning.shortestpath.parallelization.v2.algorithm;

import it.proconsole.learning.shortestpath.parallelization.v2.graph.Edge;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Optimized parallel Bellman-Ford implementation using fine-grained locking. Uses individual locks
 * per vertex for better concurrency than global locking and better performance than
 * AtomicIntegerArray for this use case.
 */
public final class BellmanFordParallelOptimized implements ShortestPathAlgorithm {

  private static final int DEFAULT_PARALLELISM = Runtime.getRuntime().availableProcessors();
  private static final int CHUNK_SIZE = 512; // Larger chunks for better performance
  private static final int LOCK_STRIPING = 1024; // Number of locks (reduce memory overhead)

  private final ForkJoinPool pool;
  private final boolean detectNegativeCycles;

  public BellmanFordParallelOptimized() {
    this(null, true);
  }

  public BellmanFordParallelOptimized(ForkJoinPool pool, boolean detectNegativeCycles) {
    this.pool = pool != null ? pool : new ForkJoinPool(DEFAULT_PARALLELISM);
    this.detectNegativeCycles = detectNegativeCycles;
  }

  @Override
  public ShortestPathResult compute(Graph graph, int sourceVertex) {
    int vertexCount = graph.vertexCount();

    // Use regular arrays for better cache performance
    int[] currentDistances = new int[vertexCount];
    int[] nextDistances = new int[vertexCount];
    int[] predecessors = new int[vertexCount];

    Arrays.fill(currentDistances, ShortestPathResult.INFINITY);
    Arrays.fill(nextDistances, ShortestPathResult.INFINITY);
    Arrays.fill(predecessors, ShortestPathResult.NO_PREDECESSOR);

    currentDistances[sourceVertex] = 0;
    nextDistances[sourceVertex] = 0;

    // Lock striping: use fewer locks than vertices to reduce memory overhead
    int lockCount = Math.min(LOCK_STRIPING, vertexCount);
    ReentrantLock[] locks = new ReentrantLock[lockCount];
    for (int i = 0; i < lockCount; i++) {
      locks[i] = new ReentrantLock();
    }

    // Relax edges V-1 times
    for (int iteration = 0; iteration < vertexCount - 1; iteration++) {
      // Copy current to next at start of iteration
      System.arraycopy(currentDistances, 0, nextDistances, 0, vertexCount);

      AtomicBoolean changed = new AtomicBoolean(false);

      // Parallel edge relaxation
      pool.invoke(
          new RelaxationTask(
              graph,
              currentDistances,
              nextDistances,
              predecessors,
              changed,
              locks,
              0,
              vertexCount));

      // Swap buffers
      int[] temp = currentDistances;
      currentDistances = nextDistances;
      nextDistances = temp;

      // Early termination if no changes
      if (!changed.get()) {
        break;
      }
    }

    // Check for negative cycles
    if (detectNegativeCycles) {
      checkNegativeCycles(graph, currentDistances);
    }

    return new ShortestPathResult(sourceVertex, currentDistances, predecessors);
  }

  private void checkNegativeCycles(Graph graph, int[] distances) {
    int vertexCount = graph.vertexCount();

    for (int u = 0; u < vertexCount; u++) {
      if (distances[u] == ShortestPathResult.INFINITY) {
        continue;
      }

      for (Edge edge : graph.getEdges(u)) {
        int v = edge.destination();
        int weight = edge.weight();

        if (distances[u] + weight < distances[v]) {
          throw new IllegalStateException("Graph contains a negative cycle reachable from source");
        }
      }
    }
  }

  @Override
  public String getName() {
    return "Bellman-Ford Parallel (Optimized)";
  }

  @Override
  public boolean supportsNegativeWeights() {
    return true;
  }

  @Override
  public boolean isParallel() {
    return true;
  }

  /** ForkJoin task for parallel edge relaxation with fine-grained locking. */
  private static class RelaxationTask extends RecursiveAction {
    private final Graph graph;
    private final int[] currentDistances;
    private final int[] nextDistances;
    private final int[] predecessors;
    private final AtomicBoolean changed;
    private final ReentrantLock[] locks;
    private final int start;
    private final int end;

    RelaxationTask(
        Graph graph,
        int[] currentDistances,
        int[] nextDistances,
        int[] predecessors,
        AtomicBoolean changed,
        ReentrantLock[] locks,
        int start,
        int end) {
      this.graph = graph;
      this.currentDistances = currentDistances;
      this.nextDistances = nextDistances;
      this.predecessors = predecessors;
      this.changed = changed;
      this.locks = locks;
      this.start = start;
      this.end = end;
    }

    @Override
    protected void compute() {
      int length = end - start;

      if (length <= CHUNK_SIZE) {
        // Process vertices in this chunk
        for (int u = start; u < end; u++) {
          int distU = currentDistances[u];
          if (distU == ShortestPathResult.INFINITY) {
            continue;
          }

          for (Edge edge : graph.getEdges(u)) {
            int v = edge.destination();
            int weight = edge.weight();
            int newDist = distU + weight;

            // Quick check without lock
            if (newDist >= nextDistances[v]) {
              continue;
            }

            // Lock the target vertex (using lock striping)
            int lockIndex = v % locks.length;
            locks[lockIndex].lock();
            try {
              // Double-check after acquiring lock
              if (newDist < nextDistances[v]) {
                nextDistances[v] = newDist;
                predecessors[v] = u;
                changed.set(true);
              }
            } finally {
              locks[lockIndex].unlock();
            }
          }
        }
      } else {
        // Split task
        int mid = start + length / 2;
        RelaxationTask left =
            new RelaxationTask(
                graph, currentDistances, nextDistances, predecessors, changed, locks, start, mid);
        RelaxationTask right =
            new RelaxationTask(
                graph, currentDistances, nextDistances, predecessors, changed, locks, mid, end);
        invokeAll(left, right);
      }
    }
  }
}
