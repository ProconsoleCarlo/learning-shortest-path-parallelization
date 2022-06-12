/*
 * Main.c
 *
 *  Created on: 22/dic/2014
 *     Author: Carlo Bobba, Eleonora Aiello
 */

#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "../Algorithms/BellmanFordParallelV1.h"
#include "../Algorithms/BellmanFordSerial.h"
#include "../Utils/ArraysUtilities.h"
#include "../Utils/GraphGenerator.h"


int main() {

	int vertices = 10;
	int maxEdges = (vertices*vertices-vertices)/2;
	double density = 0.75;
	int edges = maxEdges*density;
	bool negativeEdgesAllowed = false;
	printf("Generating a random graph with %d vertices and %d edges\n", vertices, edges);
	double start = omp_get_wtime(), end;
	int** graph = generateGraphAsAdjacencyMatrix(vertices, edges, negativeEdgesAllowed);
	end = omp_get_wtime();
	printf("Time to generate the random graph: %f s\n", end-start);
	//int* distances1 = dijkstraPV1(graph, vertices, 0);
	int* distances1 = bellmanFordSerial(graph, negativeEdgesAllowed, vertices, 0);
	int* distances2 = bellmanFordParallelV1(graph, negativeEdgesAllowed, vertices, 0);
	//int* distances2 = dijkstraSerial(graph, vertices, 0);
	printf("The result is ");
	bool check = areArraysEquals(distances1, vertices, distances2, vertices);
	if (check) {
		printf("correct\n");
	}else {
		printf("erroneous\n");
	}

	//printMatrix(graph, vertices, vertices);
	//printArray(distances1, vertices, "From ", "cost is");
	//printArray(distances2, vertices, "From ", "cost is");
	free(graph);

    return 0;
}

