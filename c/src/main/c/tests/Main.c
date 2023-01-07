/*
 * Main.c
 *
 * Created on: 22/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: The main to run the program
 */

#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "../algorithms/BellmanFordParallelV1.h"
#include "../algorithms/BellmanFordSerial.h"
#include "../algorithms/DijkstraParallelV1.h"
#include "../algorithms/DijkstraSerial.h"
#include "../utils/ArraysUtilities.h"
#include "../utils/GraphGenerator.h"

int main() {
	//Parameters
	//Number of vertices contained in the graph
	//WARNING: For Bellman-Ford it is recommended to use less than 5000 vertices due to its long elaboration!
	int vertices = 1024;

	//Density of the graph: how many edges there will be proportionally to the maximum possible number
	double density = 0.75;

	//The vertex from which computing the distances
	int sourceVertex = 0;

	//If negative edges are allowed in the graph
	//For the project is supposed FALSE
	bool negativeEdgesAllowed = false;

	//Print the graph and the distances computed on the standard output
	//WARNING: set TRUE only if the graph has less than 100 vertices, otherwise there is a lot of overhead!
	bool printOutput = false;

	//Program execution
	int maxEdges = (vertices*vertices-vertices)/2;
	int edges = maxEdges*density;
	printf("Generating a random graph with %d vertices and %d edges...\n", vertices, edges);
	double start = omp_get_wtime(), end;
	int** graph = generateGraphAsAdjacencyMatrix(vertices, edges, negativeEdgesAllowed);
	end = omp_get_wtime();
	printf("Time to generate the random graph: %f s\n", end-start);
	int* distances1;
	int* distances2;
	int* distances3;
	int* distances4;
	printf("\n-----Dijkstra serial---------------------\n");
	distances1 = dijkstraSerial(graph, vertices, sourceVertex);
	printf("\n-----Dijkstra parallel-------------------\n");
	distances2 = dijkstraPV1(graph, vertices, sourceVertex);
	printf("\n-----Bellman Ford serial-----------------\n");
	distances3 = bellmanFordSerial(graph, negativeEdgesAllowed, vertices, sourceVertex);
	printf("\n-----Bellman Ford Parallel---------------\n");
	distances4 = bellmanFordParallelV1(graph, negativeEdgesAllowed, vertices, sourceVertex);
	printf("\n-----------------------------------------\n");

	//Checking the correctness of results
	printf("The Dijkstras results are ");
	bool checkDijkstra = areArraysEquals(distances1, vertices, distances2, vertices);
	if (checkDijkstra) {
		printf("correct\n");
	} else {
		printf("erroneous\n");
	}
	printf("The Bellman-Ford results are ");
	bool checkBellmanFord = areArraysEquals(distances3, vertices, distances4, vertices);
	if (!negativeEdgesAllowed) {
		if (checkBellmanFord) {
			printf("correct\n");
		} else {
			printf("erroneous\n");
		}
	} else {
		printf("not automatically checkable./nIf there were negative cycles there isn't a solution, otherwise they are correct.\n");
	}

	//Print on the standard output the graph and the minimum distances
	if (printOutput) {
		printf("\n-----Adjacency matrix of the graph-------\n");
		printMatrix(graph, vertices, vertices);
		printf("\n-----Dijkstra serial---------------------\n");
		printArray(distances1, vertices, "Source to", "Cost");
		printf("\n-----Dijkstra parallel-------------------\n");
		printArray(distances2, vertices, "Source to", "Cost");
		printf("\n-----Bellman Ford serial-----------------\n");
		printArray(distances3, vertices, "Source to", "Cost");
		printf("\n-----Bellman Ford Parallel---------------\n");
		printArray(distances4, vertices, "Source to", "Cost");
	}

	free(distances1);
	free(distances2);
	free(distances3);
	free(distances4);
	free(graph);

    return 0;
}
