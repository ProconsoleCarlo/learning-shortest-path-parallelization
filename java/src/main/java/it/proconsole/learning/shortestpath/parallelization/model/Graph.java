package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.List;

public interface Graph {
  int ZERO_WEIGHT = 0;

  void setNode(int x, int y, int value);

  void setSymmetricNode(int x, int y, int value);

  void removeEdge(int x, int y);

  int getNode(int x, int y);

  boolean hasNegativeEdges();

  boolean isNodeZero(int x, int y);

  int vertices();

  List<Edge> neighboursOf(int x);
}
