/*
 * GraphGenerator.c
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: A simple random graph generator
 */

#include <stdbool.h>
#include <stdlib.h>
#include <sys/types.h>

#include "ArraysUtilities.h"


int MAX_WEIGHT = 5;
int MAX_NEGATIVE_WEIGHT = 2;
time_t t;

/*
 * This function catch the case in which the weight randomly generated is zero:
 * if negative values are allowed, than the weight will be a random negative value
 * else the weight will be the max possible weight
 */
int catchZerosWeightCase(int weight, bool negativeValueAllowed) {
	if (weight == 0) {
		if (negativeValueAllowed) {
			weight = - (rand() % (MAX_NEGATIVE_WEIGHT)) - 1 ;
		} else {
			weight = MAX_WEIGHT;
		}
	}
	return weight;
}
/*
 * This algorithm first add random values to all the possible edges,
 * than remove the correct number of edges
 *  to obtain a graph with the requested number of edges.
 */
void removalAlgorithm(int edges, int maxEdges, int vertices, bool negativeValueAllowed, int** matrixGraph) {
	if (edges > maxEdges) {
		edges = maxEdges;
	}
	int i = 0, j = 0;
	for (i = 0; i < vertices; ++i) {
		for (j = i + 1; j < vertices; ++j) {
			int weight = rand() % (MAX_WEIGHT + 1);
			weight = catchZerosWeightCase(weight, negativeValueAllowed);
			matrixGraph[i][j] = weight;
		}
	}
	int nodes = maxEdges;
	while (edges < nodes) {
		int src = rand() % vertices;
		int dest = src + (rand() % (vertices - src));
		if (dest != src) {
			if (matrixGraph[src][dest] != 0) {
				matrixGraph[src][dest] = 0;
				nodes--;
			}
		}
	}
}
/*
 * This algorithm add random values to the requested number of edges
 */
void addictiveAlgorithm(int edges, int vertices, bool negativeValueAllowed, int** matrixGraph) {
	int nodes = 0;
	while (nodes < edges) {
		int src = rand() % vertices;
		int dest = src + (rand() % (vertices - src));
		int weight = rand() % (MAX_WEIGHT + 1);
		if (dest != src) {
			if (matrixGraph[src][dest] == 0) {
				weight = catchZerosWeightCase(weight, negativeValueAllowed);
				matrixGraph[src][dest] = weight;
				nodes++;
			}
		}
	}
}

/*
 * Function that generate a random graph represented with an adjacency matrix.
 * The way the algorithm works depends on the density of links in the graph. (See "Statistiche GraphGenerator.xlsx" for more info.
 * int vertices The number of vertices of the graph
 * int edges The number of edges of the graph (If it's more than the maximum, the number of edges is the maximum)
 * bool negativeValueAllowed If are allowed negative weight in the graph or not
 */
int** generateGraphAsAdjacencyMatrix (int vertices, int edges, bool negativeValueAllowed) {
	srand((unsigned) time(&t));

	int** matrixGraph = (int**) calloc(vertices, sizeof(int*));
	int i;
	for (i = 0; i < vertices; i++) {
		matrixGraph[i] = calloc(vertices, sizeof(int));
	}

	int maxEdges = (vertices*vertices-vertices)/2;

	if (edges > maxEdges*0.475) {
		removalAlgorithm(edges, maxEdges, vertices, negativeValueAllowed, matrixGraph);
	}else {
		addictiveAlgorithm(edges, vertices, negativeValueAllowed, matrixGraph);
	}
	return makeMatrixSymmetric(matrixGraph, vertices);
}
