package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
  public int vertices() {
    return vertices;
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
  public List<Edge> neighboursOf(int x) {
    return Optional.ofNullable(values.get(x))
            .map(node -> node.edges)
            .orElse(Collections.emptyList());
  }

  @Override
  public boolean hasNegativeEdges() {
    return values.values().stream()
            .flatMap(node -> node.edges().stream())
            .anyMatch(it -> it.cost() < Graph.ZERO_WEIGHT);
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

  static class Node {
    private final int id;
    private final List<Edge> edges = new ArrayList<>();

    public Node(int id) {
      this.id = id;
    }

    public int id() {
      return id;
    }

    public List<Edge> edges() {
      return edges;
    }

    public void addEdge(int destination, int cost) {
      if (getEdge(destination) != Graph.ZERO_WEIGHT) {
        edges.add(new Edge(destination, cost));
      }
    }

    public int getEdge(int destination) {
      return edges.stream()
              .filter(it -> it.destination() == destination).findFirst()
              .map(Edge::cost)
              .orElse(Graph.ZERO_WEIGHT);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Node node)) return false;
      return id == node.id && edges.equals(node.edges);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, edges);
    }
  }
}
