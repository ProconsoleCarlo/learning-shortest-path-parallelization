package it.proconsole.learning.shortestpath.parallelization.util;

import it.proconsole.learning.shortestpath.parallelization.model.Graph;
import it.proconsole.learning.shortestpath.parallelization.model.GraphFactory;

import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import static it.proconsole.learning.shortestpath.parallelization.model.Graph.ZERO_WEIGHT;

public class AddictiveRemovalGraphGenerator implements GraphGenerator {
  private final RandomGenerator random = new Random();

  private final GraphFactory graphFactory;
  private final EdgeWeightGenerator edgeWeightGenerator;

  public AddictiveRemovalGraphGenerator(GraphFactory graphFactory, EdgeWeightGenerator edgeWeightGenerator) {
    this.graphFactory = graphFactory;
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
      return removalAlgorithm(vertices, edges);
    } else {
      return addictiveAlgorithm(vertices, edges);
    }
  }

  private int getMaxEdges(int vertices) {
    return (vertices * vertices - vertices) / 2;
  }

  private Graph addictiveAlgorithm(int vertices, int edges) {
    var graph = graphFactory.create(vertices);
    var nodes = 0;
    while (nodes < edges) {
      var x = random.nextInt(vertices);
      var y = x + random.nextInt(vertices - x);
      if (x != y && graph.getNode(x, y) == ZERO_WEIGHT) {
        var value = edgeWeightGenerator.getValue();
        graph.setSymmetricNode(x, y, value);
        nodes++;
      }
    }
    return graph;
  }

  private Graph removalAlgorithm(int vertices, int edges) {
    var graph = graphFactory.create(vertices);
    IntStream.range(0, vertices)
            .forEach(i -> IntStream.range(0, i)
                    .forEach(j -> graph.setSymmetricNode(i, j, edgeWeightGenerator.getValue()))
            );

    var edgesToRemove = getMaxEdges(vertices) - edges;
    while (edgesToRemove > 0) {
      var x = random.nextInt(vertices);
      var y = x + random.nextInt(vertices - x);
      if (x != y && graph.getNode(x, y) != ZERO_WEIGHT) {
        //problema: 0 mette un edge in AdjacencyMap, dovrebbe togliere l'elemento dalla mappa invece
        graph.setSymmetricNode(x, y, ZERO_WEIGHT);
        edgesToRemove--;
      }
    }
    return graph;
  }
}