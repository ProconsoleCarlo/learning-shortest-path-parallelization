package it.proconsole.learning.shortestpath.parallelization;

import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordParallel;
import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordSerial;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraParallel;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraSerial;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraphFactory;
import it.proconsole.learning.shortestpath.parallelization.util.AddictiveRemovalGraphGenerator;
import it.proconsole.learning.shortestpath.parallelization.util.LoggerResultPrinter;
import it.proconsole.learning.shortestpath.parallelization.util.NamedArgsExtractor;
import it.proconsole.learning.shortestpath.parallelization.util.SerialParallelComparator;

import java.util.Random;

import static it.proconsole.learning.shortestpath.parallelization.util.EdgeWeightGenerator.Builder.aEdgeWeightGenerator;

public class Main {
  private static final int VERTICES = 1024;
  private static final float DENSITY = 0.25F;
  private static final int SOURCE_NODE = 0;

  public static void main(String[] args) {
    var namedArgs = new NamedArgsExtractor(args);
    var vertices = namedArgs.getInteger("vertices").orElse(VERTICES);
    var density = namedArgs.getFloat("density").orElse(DENSITY);
    int sourceNode = namedArgs.getInteger("sourceNode").orElse(SOURCE_NODE);

    var graphGenerator = new AddictiveRemovalGraphGenerator(new MatrixGraphFactory(), aEdgeWeightGenerator(new Random()).build());
    var resultPrinter = new LoggerResultPrinter();

    var graph = graphGenerator.generate(vertices, density);
    var dijkstra = new SerialParallelComparator(new DijkstraSerial(), new DijkstraParallel());
    var bellmanFord = new SerialParallelComparator(new BellmanFordSerial(), new BellmanFordParallel());

    var dijkstraResult = dijkstra.compareWith(graph, sourceNode);
    var bellmanFordResult = bellmanFord.compareWith(graph, sourceNode);

    resultPrinter.print(dijkstraResult);
    resultPrinter.print(bellmanFordResult);
  }
}
