package it.proconsole.learning.shortestpath.parallelization.v2.algorithm;

import it.proconsole.learning.shortestpath.parallelization.v2.graph.Edge;
import it.proconsole.learning.shortestpath.parallelization.v2.graph.Graph;
import it.proconsole.learning.shortestpath.parallelization.v2.result.ShortestPathResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Delta-Stepping parallel implementation of Dijkstra's algorithm. Organizes vertices into buckets
 * based on distance ranges for parallel processing.
 *
 * <p>Algorithm: - Vertices are placed in buckets [iΔ, (i+1)Δ) based on their tentative distances -
 * Light edges (weight ≤ Δ) are processed within the same bucket iteration - Heavy edges (weight >
 * Δ) place vertices in future buckets - Buckets are processed sequentially, but vertices within a
 * bucket are relaxed in parallel
 */
public final class DijkstraDeltaStepping implements ShortestPathAlgorithm {

  private static final int DEFAULT_PARALLELISM = Runtime.getRuntime().availableProcessors();
  private static final int CHUNK_SIZE = 64; // Vertices per task to reduce overhead

  private final int delta;
  private final ForkJoinPool pool;

  /**
   * Creates a Delta-Stepping algorithm with specified delta. If delta is 0 or negative, it will be
   * computed automatically based on graph properties.
   */
  public DijkstraDeltaStepping(int delta) {
    this(delta, null);
  }

  /** Creates a Delta-Stepping algorithm with specified delta and custom ForkJoinPool. */
  public DijkstraDeltaStepping(int delta, ForkJoinPool pool) {
    this.delta = delta;
    this.pool = pool != null ? pool : new ForkJoinPool(DEFAULT_PARALLELISM);
  }

  @Override
  public ShortestPathResult compute(Graph graph, int sourceVertex) {
    validateGraph(graph);

    int vertexCount = graph.vertexCount();

    // Determine delta if not provided or invalid
    int effectiveDelta = delta > 0 ? delta : computeAutoDelta();

    // Initialize distances atomically
    AtomicIntegerArray distances = new AtomicIntegerArray(vertexCount);
    for (int i = 0; i < vertexCount; i++) {
      distances.set(i, ShortestPathResult.INFINITY);
    }
    distances.set(sourceVertex, 0);

    // Predecessors array (thread-safe updates via synchronized blocks when needed)
    int[] predecessors = new int[vertexCount];
    Arrays.fill(predecessors, ShortestPathResult.NO_PREDECESSOR);

    // Buckets for vertices grouped by distance range
    int maxBuckets = (vertexCount * 1000 / effectiveDelta) + 1; // Estimate max buckets needed
    @SuppressWarnings("unchecked")
    ConcurrentLinkedQueue<Integer>[] buckets = new ConcurrentLinkedQueue[maxBuckets];
    for (int i = 0; i < maxBuckets; i++) {
      buckets[i] = new ConcurrentLinkedQueue<>();
    }

    // Add source to first bucket
    buckets[0].add(sourceVertex);

    // Process buckets sequentially
    for (int currentBucket = 0; currentBucket < maxBuckets; currentBucket++) {
      ConcurrentLinkedQueue<Integer> bucket = buckets[currentBucket];

      if (bucket.isEmpty()) {
        continue;
      }

      // Process all vertices in current bucket until it's empty
      while (!bucket.isEmpty()) {
        List<Integer> vertices = new ArrayList<>();
        Integer v;
        while ((v = bucket.poll()) != null) {
          vertices.add(v);
        }

        if (vertices.isEmpty()) {
          break;
        }

        // Relax edges in parallel
        pool.invoke(
            new RelaxEdgesTask(
                graph,
                vertices,
                distances,
                predecessors,
                buckets,
                effectiveDelta,
                0,
                vertices.size()));
      }
    }

    // Convert atomic array to regular array
    int[] finalDistances = new int[vertexCount];
    for (int i = 0; i < vertexCount; i++) {
      finalDistances[i] = distances.get(i);
    }

    return new ShortestPathResult(sourceVertex, finalDistances, predecessors);
  }

  /**
   * Computes an automatic delta value based on graph properties. Heuristic: average edge weight /
   * parallelism level
   */
  private int computeAutoDelta() {
    // Simple heuristic: assume average weight is around 100, scale by parallelism
    int parallelism = pool.getParallelism();
    return Math.max(1, 100 / parallelism);
  }

  @Override
  public String getName() {
    return "Dijkstra Delta-Stepping (Parallel, Δ=" + delta + ")";
  }

  @Override
  public boolean supportsNegativeWeights() {
    return false;
  }

  @Override
  public boolean isParallel() {
    return true;
  }

  /** ForkJoin task for parallel edge relaxation. */
  private static class RelaxEdgesTask extends RecursiveAction {
    private final Graph graph;
    private final List<Integer> vertices;
    private final AtomicIntegerArray distances;
    private final int[] predecessors;
    private final ConcurrentLinkedQueue<Integer>[] buckets;
    private final int delta;
    private final int start;
    private final int end;

    RelaxEdgesTask(
        Graph graph,
        List<Integer> vertices,
        AtomicIntegerArray distances,
        int[] predecessors,
        ConcurrentLinkedQueue<Integer>[] buckets,
        int delta,
        int start,
        int end) {
      this.graph = graph;
      this.vertices = vertices;
      this.distances = distances;
      this.predecessors = predecessors;
      this.buckets = buckets;
      this.delta = delta;
      this.start = start;
      this.end = end;
    }

    @Override
    protected void compute() {
      int length = end - start;

      if (length <= CHUNK_SIZE) {
        // Process vertices sequentially in this chunk
        for (int i = start; i < end; i++) {
          int u = vertices.get(i);
          int distU = distances.get(u);

          if (distU == ShortestPathResult.INFINITY) {
            continue;
          }

          // Relax all edges from u
          for (Edge edge : graph.getEdges(u)) {
            int v = edge.destination();
            int weight = edge.weight();
            int newDist = distU + weight;

            // Atomic compare-and-swap to update distance
            int oldDist;
            do {
              oldDist = distances.get(v);
              if (newDist >= oldDist) {
                break; // No improvement
              }
            } while (!distances.compareAndSet(v, oldDist, newDist));

            if (newDist < oldDist) {
              // Successfully updated distance
              synchronized (predecessors) {
                if (distances.get(v) == newDist) {
                  predecessors[v] = u;
                }
              }

              // Add to appropriate bucket
              int bucketIndex = Math.min(newDist / delta, buckets.length - 1);
              buckets[bucketIndex].add(v);
            }
          }
        }
      } else {
        // Split task
        int mid = start + length / 2;
        RelaxEdgesTask left =
            new RelaxEdgesTask(
                graph, vertices, distances, predecessors, buckets, delta, start, mid);
        RelaxEdgesTask right =
            new RelaxEdgesTask(graph, vertices, distances, predecessors, buckets, delta, mid, end);
        invokeAll(left, right);
      }
    }
  }
}
