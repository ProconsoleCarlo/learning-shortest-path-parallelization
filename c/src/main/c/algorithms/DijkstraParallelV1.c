/*
 * DijkstraParallelV1.c
 *
 * Created on: 27/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.geeksforgeeks.org/greedy-algorithms-set-6-dijkstras-shortest-path-algorithm/
 * Descrition: A C / C++ program for Dijkstra's single source shortest path algorithm.
 * 			   The program is for adjacency matrix representation of the graph.
 */

#include <limits.h>
#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

int minDistance, minVertex;

void getMinDistancePV1(int* distances, int vertices, bool* shortestPathFinalized) {
	minDistance = INT_MAX;
	int vertex;
	for (vertex = 0; vertex < vertices; vertex++) {
		if (!shortestPathFinalized[vertex] && distances[vertex] <= minDistance) {
			minDistance = distances[vertex];
			minVertex = vertex;
		}
	}
}

void updateDistancesPV1(int vertices, bool* shortestPathFinalized, int** graph, int* distances) {
	int v;
	#pragma omp for private(v)
	for (v = 0; v < vertices; v++) {
		if (!shortestPathFinalized[v] && graph[minVertex][v] && distances[minVertex] != INT_MAX && distances[minVertex] + graph[minVertex][v] < distances[v]) {
			distances[v] = distances[minVertex] + graph[minVertex][v];
		}
	}
}
/*
 * Compute the minimum path in a graph represented with adiacency matrix
 * using Dijkstra algorithm
 * int** graph The graph in the adiacency matrix form
 * int vertices The number of vertices in the graph
 * int sourceNode The node from which compute the distances
 * return The minimum distances from sourceNode
 */
int* dijkstraPV1(int** graph, int vertices, int sourceNode) {
	double endTime, startTime = omp_get_wtime();

	int* distances = (int*) malloc(vertices*sizeof(int));
	bool* shortestPathFinalized = (bool*) malloc(vertices*sizeof(bool));

    #pragma omp parallel
    {
		int i;
		#pragma omp for private(i)
		for (i = 0; i < vertices; i++) {
			distances[i] = INT_MAX;
			shortestPathFinalized[i] = false;
		}
		#pragma omp single
    	{
    		distances[sourceNode] = 0;
    	}

    	int count;
    	for (count = 0; count < vertices; count++) {
			#pragma omp single
    		{
    			getMinDistancePV1(distances, vertices, shortestPathFinalized);

    			shortestPathFinalized[minVertex] = true;
    		}
    		updateDistancesPV1(vertices, shortestPathFinalized, graph, distances);
    	}
    }
    endTime = omp_get_wtime();
    printf("Elapsed time for parallel Dijkstra %f\n", endTime-startTime);
    return distances;
}
