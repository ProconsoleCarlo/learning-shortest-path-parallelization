/*
 * TestDijkstraSerial.c
 *
 * Created on: 25/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: A test to do the Dijkstra serial algorithm
 */

#include <stdio.h>

#include "../Algorithms/DijkstraSerial.h"
#include "../Utils/ArraysUtilities.h"

/*
 * Function that execute the Dijkstra serial algorithm
 * int** graph The graph representation as adiacency matrix
 * int graphWidth The width of the squared graph
 * int sourceNode The node from which find the shortest path
 */
void testDijkstraSerial(int** graph, int graphWidth, int sourceNode) {
	int* distance = dijkstraPV1(graph, graphWidth, 0);

	printf("Graph adiacency matrix\n");
	int i, j;
	for (i = 0; i < graphWidth; ++i) {
		for (j = 0; j < graphWidth; ++j) {
			printf("%d\t", graph[i][j]);
		}
		printf("\n");
	}
	printArray(distance, graphWidth, "Node", "Distance from source");
}
