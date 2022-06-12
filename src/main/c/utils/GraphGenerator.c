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

/*
 * Function that generate a random graph represented with an adjacency matrix
 * int vertices The number of vertices of the graph
 * int edges The number of edges of the graph
 * bool negativeValueAllowed If are allowed negative weight in the graph or not
 */
int** generateGraphAsAdjacencyMatrix (int vertices, int edges, bool negativeValueAllowed) {
	//Alloco la matrice in modo da poterci accedere come matrixGraph[i][j]
	int** matrixGraph = (int**) calloc(vertices, sizeof(int*));
	int i;
	for (i = 0; i < vertices; i++) {
		matrixGraph[i] = calloc(vertices, sizeof(int));
	}
	/*
	 * Inizializzazione classe
	 * TODO: i valori max sarebbe bello farli con una DEFINE, ma non sono in grado,
	 * se provo a mettere define in alto dopo gli include mi da errore...
	 */
	int MAX_WEIGHT = 5;
	int MAX_NEGATIVE_WEIGHT = 2;
	time_t t;
	srand((unsigned) time(&t));
	int nodes = 0;

	while(nodes < edges) {
		int src = rand() % vertices;
		int temp = rand() % (vertices-src);
		int dest = src + temp;
		int weight = rand() % (MAX_WEIGHT + 1);
		if (dest != src) {
			if (matrixGraph[src][dest] == 0) {
				if (weight != 0) {
					matrixGraph[src][dest] = weight;
					nodes++;
				}else if (weight == 0 && negativeValueAllowed) {
					matrixGraph[src][dest] = - rand() % (MAX_NEGATIVE_WEIGHT + 1);
					nodes++;
				}
			}
		}
	}
	return makeMatrixSymmetric(matrixGraph, vertices);
}
