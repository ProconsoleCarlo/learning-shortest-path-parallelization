package it.proconsole.learning.shortestpath.parallelization.graph;

import java.util.List;

public interface Graph {
  int ZERO_WEIGHT = 0;

  void addBidirectionalEdge(int from, int to, int cost);

  void addEdge(int from, int to, int cost);

  int getCost(int from, int to);

  boolean hasNegativeEdges();

  boolean haveConnection(int from, int to);

  List<Edge> neighboursOf(int node);

  void removeEdge(int from, int to);

  int vertices();
}
