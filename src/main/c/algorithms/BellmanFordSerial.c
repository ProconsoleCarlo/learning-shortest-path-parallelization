/*
 * BellmanFordSerial.c
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.sanfoundry.com/java-program-implement-bellmanford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 * 				The program is for adjacency matrix representation of the graph.
 */

#include <limits.h>
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

void initializeDistancesSerial(int vertices, int sourceNode, int* distances) {
	int i;
	for (i = 0; i < vertices; i++) {
		distances[i] = INT_MAX;
	}
	distances[sourceNode] = 0;
}

void relaxEdgesSerial(int** graph, int vertices, int* distances) {
	int node, src, dest;
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

void checkCyclesPresenceSerial(int** graph, int vertices, int* distances) {
	int src, dest, cycles = 0;
	for (src = 0; src < vertices; ++src) {
		for (dest = 0; dest < vertices; ++dest) {
			if (graph[src][dest] != 0) {
				if (distances[dest] > distances[src] + graph[src][dest]) {
					cycles++;
				}
			}
		}
	}
	if (cycles != 0) {
		printf("The graph contains negative edge cycle %d times followed!\n", cycles);
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
int* bellmanFordSerial(int** graph, bool negativeEdgesAllowed, int vertices, int sourceNode) {
	double end, start = omp_get_wtime();

//	double endInitTime, startInitTime = omp_get_wtime();
	int* distances = (int*) malloc(vertices*sizeof(int));
	initializeDistancesSerial(vertices, sourceNode, distances);
//	endInitTime = omp_get_wtime();
//	printf("Elapsed time to initialize distances: %f\n", endInitTime-startInitTime);

//	double endRelaxTime, startRelaxTime = omp_get_wtime();
	relaxEdgesSerial(graph, vertices, distances);
//	endRelaxTime = omp_get_wtime();
//	printf("Elapsed time for relaxation of nodes: %f\n", endRelaxTime-startRelaxTime);

	if (negativeEdgesAllowed) {
		checkCyclesPresenceSerial(graph, vertices, distances);
	}
    end = omp_get_wtime();
    printf("Elapsed time for serial BellmanFord: %f\n", end-start);
	return distances;
}
