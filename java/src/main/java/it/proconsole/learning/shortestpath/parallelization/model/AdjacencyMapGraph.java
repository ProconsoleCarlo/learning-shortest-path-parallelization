package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AdjacencyMapGraph implements Graph {
  private final int vertices;
  private final Map<Integer, Node> values;

  public AdjacencyMapGraph(int vertices) {
    this.vertices = vertices;
    this.values = new HashMap<>(vertices);
  }

  @Override
  public void setNode(int x, int y, int value) {
    values.computeIfAbsent(x, Node::new).addEdge(y, value);
  }

  @Override
  public void setSymmetricNode(int x, int y, int value) {
    values.computeIfAbsent(x, Node::new).addEdge(y, value);
    values.computeIfAbsent(y, Node::new).addEdge(x, value);
  }

  @Override
  public int getNode(int x, int y) {
    return Optional.ofNullable(values.get(x))
            .map(it -> it.getEdge(y))
            .orElse(Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean hasNegativeEdges() {
    return values.values().stream()
            .flatMap(node -> node.edges().values().stream())
            .anyMatch(it -> it < Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean isNodeZero(int x, int y) {
    return getNode(x, y) == Graph.ZERO_WEIGHT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AdjacencyMapGraph that)) return false;
    return vertices == that.vertices && values.equals(that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vertices, values);
  }

  @Override
  public int vertices() {
    return vertices;
  }

  static class Node {
    private final int name;
    private final Map<Integer, Integer> edges = new HashMap<>();

    public Node(int name) {
      this.name = name;
    }

    public int name() {
      return name;
    }

    public Map<Integer, Integer> edges() {
      return edges;
    }

    public void addEdge(int destination, int cost) {
      edges.put(destination, cost);
    }

    public int getEdge(int destination) {
      return edges.getOrDefault(destination, Graph.ZERO_WEIGHT);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Node node)) return false;
      return name == node.name && edges.equals(node.edges);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, edges);
    }
  }
}
