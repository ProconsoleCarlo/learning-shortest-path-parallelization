package it.proconsole.learning.shortestpath.parallelization;

import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordParallel;
import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordSerial;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraParallel;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraPriorityQueueSerial;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraSerial;
import it.proconsole.learning.shortestpath.parallelization.graph.factory.AdjacencyMapGraphFactory;
import it.proconsole.learning.shortestpath.parallelization.graph.factory.GraphFactory;
import it.proconsole.learning.shortestpath.parallelization.graph.factory.MatrixGraphFactory;
import it.proconsole.learning.shortestpath.parallelization.graph.generator.AddictiveRemovalGraphGenerator;
import it.proconsole.learning.shortestpath.parallelization.result.comparator.AlgorithmComparator;
import it.proconsole.learning.shortestpath.parallelization.result.printer.LoggerResultPrinter;
import it.proconsole.learning.shortestpath.parallelization.util.NamedArgsExtractor;

import java.util.Map;
import java.util.Random;

import static it.proconsole.learning.shortestpath.parallelization.graph.generator.EdgeWeightGenerator.Builder.aEdgeWeightGenerator;

public class Main {
  private static final int VERTICES = 100;
  private static final float DENSITY = 0.25f;
  private static final int SOURCE_NODE = 0;
  private static final Map<String, GraphFactory> GRAPH_FACTORIES = Map.of(
          "MatrixGraph", new MatrixGraphFactory(),
          "AdjacencyMap", new AdjacencyMapGraphFactory()
  );

  public static void main(String[] args) {
    var namedArgs = new NamedArgsExtractor(args);
    int vertices = namedArgs.getInteger("vertices").orElse(VERTICES);
    float density = namedArgs.getFloat("density").orElse(DENSITY);
    int sourceNode = namedArgs.getInteger("sourceNode").orElse(SOURCE_NODE);
    var graphFactory = namedArgs.getString("graphType")
            .map(GRAPH_FACTORIES::get)
            .orElseGet(MatrixGraphFactory::new);
    var graphGenerator = new AddictiveRemovalGraphGenerator(graphFactory, aEdgeWeightGenerator(new Random()).build());
    var resultPrinter = new LoggerResultPrinter();

    var graph = graphGenerator.generate(vertices, density);
    var dijkstra = new AlgorithmComparator(new DijkstraSerial(), new DijkstraPriorityQueueSerial(), new DijkstraParallel());
    var bellmanFord = new AlgorithmComparator(new BellmanFordSerial(), new BellmanFordParallel());
    var dijkstraResult = dijkstra.compareWith(graph, sourceNode);
    var bellmanFordResult = bellmanFord.compareWith(graph, sourceNode);

    resultPrinter.print(dijkstraResult);
    resultPrinter.print(bellmanFordResult);
  }
}
