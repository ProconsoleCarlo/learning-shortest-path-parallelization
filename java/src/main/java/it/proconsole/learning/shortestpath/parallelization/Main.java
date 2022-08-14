package it.proconsole.learning.shortestpath.parallelization;

import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraPriorityQueueSerial;
import it.proconsole.learning.shortestpath.parallelization.algorithm.DijkstraSerial;
import it.proconsole.learning.shortestpath.parallelization.model.AdjacencyMapGraph;
import it.proconsole.learning.shortestpath.parallelization.model.AdjacencyMapGraphFactory;
import it.proconsole.learning.shortestpath.parallelization.model.MatrixGraphFactory;
import it.proconsole.learning.shortestpath.parallelization.util.AddictiveRemovalGraphGenerator;
import it.proconsole.learning.shortestpath.parallelization.util.LoggerResultPrinter;
import it.proconsole.learning.shortestpath.parallelization.util.NamedArgsExtractor;
import it.proconsole.learning.shortestpath.parallelization.util.SerialParallelComparator;

import java.util.Random;

import static it.proconsole.learning.shortestpath.parallelization.util.EdgeWeightGenerator.Builder.aEdgeWeightGenerator;

public class Main {
  private static final int VERTICES = 1000;
  private static final float DENSITY = 0.55f;
  private static final int SOURCE_NODE = 0;

  public static void main(String[] args) {
    var namedArgs = new NamedArgsExtractor(args);
    var vertices = namedArgs.getInteger("vertices").orElse(VERTICES);
    var density = namedArgs.getFloat("density").orElse(DENSITY);
    int sourceNode = namedArgs.getInteger("sourceNode").orElse(SOURCE_NODE);

    var graphGenerator = new AddictiveRemovalGraphGenerator(new MatrixGraphFactory(), aEdgeWeightGenerator(new Random()).build());
    var graphGenerator2 = new AddictiveRemovalGraphGenerator(new AdjacencyMapGraphFactory(), aEdgeWeightGenerator(new Random()).build());
    var resultPrinter = new LoggerResultPrinter();


    var manualGraph = new AdjacencyMapGraph(vertices);
    manualGraph.addEdge(0, 1, 3);
    manualGraph.addEdge(0, 7, 4);
    manualGraph.addEdge(1, 0, 3);
    manualGraph.addEdge(1, 2, 2);
    manualGraph.addEdge(1, 3, 5);
    manualGraph.addEdge(2, 1, 2);
    manualGraph.addEdge(2, 4, 4);
    manualGraph.addEdge(2, 5, 5);
    manualGraph.addEdge(3, 1, 5);
    manualGraph.addEdge(3, 6, 2);
    manualGraph.addEdge(3, 7, 3);
    manualGraph.addEdge(4, 2, 4);
    manualGraph.addEdge(4, 5, 5);
    manualGraph.addEdge(5, 2, 5);
    manualGraph.addEdge(5, 4, 5);
    manualGraph.addEdge(5, 7, 5);
    manualGraph.addEdge(6, 3, 2);
    manualGraph.addEdge(6, 7, 2);
    manualGraph.addEdge(7, 0, 4);
    manualGraph.addEdge(7, 3, 3);
    manualGraph.addEdge(7, 5, 5);
    manualGraph.addEdge(7, 6, 2);

    var graph = graphGenerator.generate(vertices, density);
    var graph2 = graphGenerator2.generate(vertices, density);
    var dijkstra = new SerialParallelComparator(new DijkstraSerial(), new DijkstraPriorityQueueSerial());
    // var bellmanFord = new SerialParallelComparator(new BellmanFordSerial(), new BellmanFordParallel());
    //var dijkstraResult = dijkstra.compareWith(graph, sourceNode);
    var dijkstraResult2 = dijkstra.compareWith(graph2, sourceNode);
    //var bellmanFordResult = bellmanFord.compareWith(graph, sourceNode);

    //resultPrinter.print(dijkstraResult);
    resultPrinter.print(dijkstraResult2);
    //resultPrinter.print(bellmanFordResult);
  }
}
