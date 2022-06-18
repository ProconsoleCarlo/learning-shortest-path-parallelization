package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.Graph;

import java.util.Random;
import java.util.random.RandomGenerator;

import static it.proconsole.learning.shortestpath.parallelization.util.EdgeWeightGenerator.ZERO_WEIGHT;

public class VanillaGraphGenerator implements GraphGenerator {
  private final RandomGenerator random = new Random();

  private final EdgeWeightGenerator edgeWeightGenerator;

  public VanillaGraphGenerator(EdgeWeightGenerator edgeWeightGenerator) {
    this.edgeWeightGenerator = edgeWeightGenerator;
  }

  @Override
  public Graph generate(int vertices, float density) {
    var edges = (int) (getMaxEdges(vertices) * density);
    return generate(vertices, edges);
  }

  @Override
  public Graph generate(int vertices, int edges) {
    var density = (float) edges / getMaxEdges(vertices);
    if (density > 0.475) {
      return new Graph(removalAlgorithm(vertices, edges));
    } else {
      return new Graph(addictiveAlgorithm(vertices, edges));
    }
  }

  private int getMaxEdges(int vertices) {
    return (vertices * vertices - vertices) / 2;
  }

  private int[][] addictiveAlgorithm(int vertices, int edges) {
    var matrix = new int[vertices][vertices];
    var nodes = 0;
    while (nodes < edges) {
      var x = random.nextInt(vertices);
      var y = x + random.nextInt(vertices - x);
      if (x != y && matrix[x][y] == ZERO_WEIGHT) {
        var value = edgeWeightGenerator.getValue();
        matrix[x][y] = value;
        matrix[y][x] = value;
        nodes++;
      }
    }
    return matrix;
  }

  private int[][] removalAlgorithm(int vertices, int edges) {
    var matrix = new int[vertices][vertices];
    for (int i = 0; i < vertices; i++) {
      for (int j = 0; j < i; j++) {
        var value = edgeWeightGenerator.getValue();
        matrix[i][j] = value;
        matrix[j][i] = value;
      }
    }

    var edgesToRemove = getMaxEdges(vertices) - edges;
    while (edgesToRemove > 0) {
      var x = random.nextInt(vertices);
      var y = x + random.nextInt(vertices - x);
      if (x != y && matrix[x][y] != ZERO_WEIGHT) {
        matrix[x][y] = ZERO_WEIGHT;
        matrix[y][x] = ZERO_WEIGHT;
        edgesToRemove--;
      }
    }
    return matrix;
  }
}