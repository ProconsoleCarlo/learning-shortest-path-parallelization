package it.proconsole.learning.shortestpath.parallelization.model;

public interface Graph {
  int ZERO_WEIGHT = 0;

  void setNode(int x, int y, int value);

  void setSymmetricNode(int x, int y, int value);

  int getNode(int x, int y);

  boolean hasNegativeEdges();

  boolean isNodeZero(int x, int y);

  int length();
}
