/*
 * TestGraphGenerator.c
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: A test to check the correct working of GraphGenerator.c
 */

#include <stdio.h>
#include <stdbool.h>

#include "../Utils/GraphGenerator.h"

/*
 * A test to check the correct working of GraphGenerator
 * int vertices The number of vertices of the graph
 * int edges The number of edges of the graph
 * bool negativeValueAllowed If are allowed negative weight in the graph or not
 */
void testGraphGenerator (int vertices, int edges, bool negativeValueAllowed) {
	int** graphTest = generateGraphAsAdjacencyMatrix(vertices, edges, negativeValueAllowed);

	printf("Graph generated\n");
	int i, j;
	for (i = 0; i < vertices; ++i) {
		for (j = 0; j < vertices; ++j) {
			printf("%d ", graphTest[i][j]);
		}
		printf("\n");
	}
}
