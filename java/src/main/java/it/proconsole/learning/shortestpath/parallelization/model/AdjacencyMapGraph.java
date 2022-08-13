package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AdjacencyMapGraph implements Graph {
  private final int vertices;
  private final Map<Integer, List<Edge>> values;

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
    //bug overwrite non va in adjacency
    if (value == Graph.ZERO_WEIGHT) {
      removeEdge(x, y);
    } else {
      getEdgesOf(x).add(new Edge(y, value));
    }
  }

  @Override
  public void setSymmetricNode(int x, int y, int value) {
    setNode(x, y, value);
    setNode(y, x, value);
  }

  @Override
  public void removeEdge(int x, int y) {
    getEdgesOf(x).removeIf(it -> it.destination() == y);
    getEdgesOf(y).removeIf(it -> it.destination() == x);
  }

  @Override
  public int getNode(int x, int y) {
    return Optional.ofNullable(values.get(x))
            .flatMap(it -> it.stream().filter(edge -> edge.destination() == y).findFirst())
            .map(Edge::cost)
            .orElse(Graph.ZERO_WEIGHT);
  }

  @Override
  public List<Edge> neighboursOf(int x) {
    return Optional.ofNullable(values.get(x))
            .orElse(Collections.emptyList());
  }

  @Override
  public boolean hasNegativeEdges() {
    return values.values().stream()
            .flatMap(Collection::stream)
            .anyMatch(it -> it.cost() < Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean isNodeZero(int x, int y) {
    return getNode(x, y) == Graph.ZERO_WEIGHT;
  }

  private List<Edge> getEdgesOf(int id) {
    return values.computeIfAbsent(id, k -> new ArrayList<>());
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
}
