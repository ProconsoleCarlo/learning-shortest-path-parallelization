package it.proconsole.learning.shortestpath.parallelization.graph;

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
  public void addBidirectionalEdge(int from, int to, int cost) {
    addEdge(from, to, cost);
    addEdge(to, from, cost); //NOSONAR parameters must have this order
  }

  @Override
  public void addEdge(int from, int to, int cost) {
    if (cost == Graph.ZERO_WEIGHT) {
      removeEdge(from, to);
    } else {
      if (getCost(from, to) != Graph.ZERO_WEIGHT) {
        removeEdge(from, to);
      }
      getEdgesOf(from).add(new Edge(to, cost));
    }
  }

  @Override
  public int getCost(int from, int to) {
    return Optional.ofNullable(values.get(from))
            .flatMap(edges -> edges.stream().filter(edge -> edge.destination() == to).findFirst())
            .map(Edge::cost)
            .orElse(Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean hasNegativeEdges() {
    return values.values().stream()
            .flatMap(Collection::stream)
            .anyMatch(edge -> edge.cost() < Graph.ZERO_WEIGHT);
  }

  @Override
  public boolean haveConnection(int from, int to) {
    return getCost(from, to) != Graph.ZERO_WEIGHT;
  }

  @Override
  public List<Edge> neighboursOf(int node) {
    return Optional.ofNullable(values.get(node))
            .orElse(Collections.emptyList());
  }

  @Override
  public void removeEdge(int from, int to) {
    getEdgesOf(from).removeIf(edge -> edge.destination() == to);
    getEdgesOf(to).removeIf(edge -> edge.destination() == from);
  }

  @Override
  public int vertices() {
    return vertices;
  }

  private List<Edge> getEdgesOf(int id) {
    return values.computeIfAbsent(id, ignored -> new ArrayList<>());
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
