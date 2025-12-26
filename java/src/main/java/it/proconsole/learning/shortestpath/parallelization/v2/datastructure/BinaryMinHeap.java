package it.proconsole.learning.shortestpath.parallelization.v2.datastructure;

/**
 * Binary min-heap with efficient decrease-key operation. Uses position tracking for O(log n)
 * decrease-key.
 */
public final class BinaryMinHeap {

  private static final int INITIAL_CAPACITY = 16;
  private final int[] distances; // distances[vertex] = distance value
  private final int[] positions; // positions[vertex] = position in heap
  private int[] heap; // heap[i] = vertex at position i
  private int size;

  /** Creates a binary min-heap for the specified number of vertices. */
  public BinaryMinHeap(int vertexCount) {
    this.heap = new int[Math.max(INITIAL_CAPACITY, vertexCount)];
    this.distances = new int[vertexCount];
    this.positions = new int[vertexCount];
    this.size = 0;

    // Initialize positions to -1 (not in heap)
    for (int i = 0; i < vertexCount; i++) {
      positions[i] = -1;
    }
  }

  /** Returns true if the heap is empty. */
  public boolean isEmpty() {
    return size == 0;
  }

  /** Inserts a vertex with the given distance. */
  public void insert(int vertex, int distance) {
    if (positions[vertex] != -1) {
      throw new IllegalStateException("Vertex " + vertex + " is already in heap");
    }

    ensureCapacity();

    heap[size] = vertex;
    distances[vertex] = distance;
    positions[vertex] = size;
    size++;

    bubbleUp(size - 1);
  }

  /** Extracts and returns the vertex with minimum distance. */
  public int extractMin() {
    if (isEmpty()) {
      throw new IllegalStateException("Heap is empty");
    }

    int minVertex = heap[0];
    positions[minVertex] = -1;

    size--;
    if (size > 0) {
      heap[0] = heap[size];
      positions[heap[0]] = 0;
      bubbleDown(0);
    }

    return minVertex;
  }

  /** Decreases the distance of a vertex. The new distance must be smaller than current. */
  public void decreaseKey(int vertex, int newDistance) {
    int pos = positions[vertex];
    if (pos == -1) {
      throw new IllegalStateException("Vertex " + vertex + " is not in heap");
    }
    if (newDistance > distances[vertex]) {
      throw new IllegalArgumentException("New distance must be smaller than current distance");
    }

    distances[vertex] = newDistance;
    bubbleUp(pos);
  }

  /** Returns true if the vertex is currently in the heap. */
  public boolean contains(int vertex) {
    return positions[vertex] != -1;
  }

  private void bubbleUp(int pos) {
    int vertex = heap[pos];
    int distance = distances[vertex];

    while (pos > 0) {
      int parentPos = (pos - 1) / 2;
      int parentVertex = heap[parentPos];

      if (distances[parentVertex] <= distance) {
        break;
      }

      // Swap with parent
      heap[pos] = parentVertex;
      positions[parentVertex] = pos;
      pos = parentPos;
    }

    heap[pos] = vertex;
    positions[vertex] = pos;
  }

  private void bubbleDown(int pos) {
    int vertex = heap[pos];
    int distance = distances[vertex];
    int half = size / 2;

    while (pos < half) {
      int leftChild = 2 * pos + 1;
      int rightChild = leftChild + 1;
      int minChild = leftChild;

      if (rightChild < size && distances[heap[rightChild]] < distances[heap[leftChild]]) {
        minChild = rightChild;
      }

      if (distance <= distances[heap[minChild]]) {
        break;
      }

      // Swap with min child
      heap[pos] = heap[minChild];
      positions[heap[pos]] = pos;
      pos = minChild;
    }

    heap[pos] = vertex;
    positions[vertex] = pos;
  }

  private void ensureCapacity() {
    if (size >= heap.length) {
      int newCapacity = heap.length * 2;
      int[] newHeap = new int[newCapacity];
      System.arraycopy(heap, 0, newHeap, 0, size);
      heap = newHeap;
    }
  }
}
