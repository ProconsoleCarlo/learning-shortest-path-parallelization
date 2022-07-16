package it.proconsole.learning.shortestpath.parallelization;

import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordParallelShortestPath;
import it.proconsole.learning.shortestpath.parallelization.algorithm.BellmanFordSerialShortestPath;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraParallel;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraSerial;
import it.proconsole.learning.shortestpath.parallelization.util.LoggerResultPrinter;
import it.proconsole.learning.shortestpath.parallelization.util.MatrixGraphGenerator;
import it.proconsole.learning.shortestpath.parallelization.util.SerialParallelComparator;

import java.util.Random;

import static it.proconsole.learning.shortestpath.parallelization.util.EdgeWeightGenerator.Builder.aEdgeWeightGenerator;

public class Main {
  private static final int VERTICES = 1024;
  private static final float DENSITY = 0.25F;
  private static final int SOURCE_NODE = 0;

  public static void main(String[] args) {
    var graphGenerator = new MatrixGraphGenerator(aEdgeWeightGenerator(new Random()).build());
    var resultPrinter = new LoggerResultPrinter();

    var graph = graphGenerator.generate(VERTICES, DENSITY);
    var dijkstra = new SerialParallelComparator(new DijkstraSerial(), new DijkstraParallel());
    var bellmanFord = new SerialParallelComparator(new BellmanFordSerialShortestPath(), new BellmanFordParallelShortestPath());

    var dijkstraResult = dijkstra.compareWith(graph, SOURCE_NODE);
    var bellmanFordResult = bellmanFord.compareWith(graph, SOURCE_NODE);

    resultPrinter.print(dijkstraResult);
    resultPrinter.print(bellmanFordResult);
  }
}
