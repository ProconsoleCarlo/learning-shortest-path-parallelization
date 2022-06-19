/*
 * BellmanFordParallelV1.c
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.sanfoundry.com/java-program-implement-bellmanford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 * 				The program is for adjacency matrix representation of the graph.
 */

#include <limits.h>
#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

int cycles = 0;

void initializeDistancesPV1(int vertices, int sourceNode, int* distances) {
	int i;
	#pragma omp for private(i)
	for (i = 0; i < vertices; i++) {
		distances[i] = INT_MAX;
	}
	distances[sourceNode] = 0;
}

void relaxEdgesPV1(int** graph, int vertices, int* distances) {
	int node, src, dest;
	#pragma omp for private(node, src, dest) schedule(dynamic)
	for (node = 0; node < vertices - 1; node++) {
		for (src = 0; src < vertices; src++) {
			for (dest = 0; dest < vertices; dest++) {
				if (graph[src][dest] != 0) {
					if (distances[dest]	> distances[src] + graph[src][dest]&& distances[src] != INT_MAX) {
						distances[dest] = distances[src] + graph[src][dest];
					}
				}
			}
		}
	}
}

void checkCyclesPresencePV1(int** graph, int vertices, int* distances) {
	int src, dest;
	#pragma omp for private(src, dest) reduction(+: cycles)
	for (src = 0; src < vertices; ++src) {
		for (dest = 0; dest < vertices; ++dest) {
			if (graph[src][dest] != 0) {
				if (distances[dest] > distances[src] + graph[src][dest]) {
					cycles++;
				}
			}
		}
	}
	#pragma omp single
	{
		if (cycles != 0) {
			printf("The graph contains negative edge cycle %d times followed!\n", cycles);
		}
	}
}

/*
 * Compute the minimum path in a graph represented with adiacency matrix
 * using Bellman-Ford algorithm
 * int** graph The graph in the adiacency matrix form
 * bool negativeEdgesAllowed If edges can have a negative weight it's necessary to check the presence of cycles
 * int vertices The number of vertices in the graph
 * int sourceNode The node from which compute the distances
 * return The minimum distances from sourceNode
 */
int* bellmanFordParallelV1(int** graph, bool negativeEdgesAllowed, int vertices, int sourceNode) {
	double endTime, startTime = omp_get_wtime();

	int* distances = (int*) malloc(vertices*sizeof(int));

	#pragma omp parallel
	{
		initializeDistancesPV1(vertices, sourceNode, distances);

		relaxEdgesPV1(graph, vertices, distances);

		if (negativeEdgesAllowed) {
			checkCyclesPresencePV1(graph, vertices, distances);
		}
	}
    endTime = omp_get_wtime();
    printf("Elapsed time for parallel BellmanFord: %f\n", endTime-startTime);
	return distances;
}

