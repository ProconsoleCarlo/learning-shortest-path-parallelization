/*
 * DijkstraParallelV2.c
 * Doesn't obtain a speed up, it cause *5 time
 *
 * Created on: 30/dic/2014
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
int* minDistances;
int* minVertices;

void getMinDistancePV2(int* distances, int graphWidth, bool* shortestPathFinalized) {
	minDistances[omp_get_thread_num()] = INT_MAX;
	int vertex = 0;
	#pragma omp for private(vertex)
	for (vertex = 0; vertex < graphWidth; vertex++) {
		if (!shortestPathFinalized[vertex] && distances[vertex] <= minDistances[omp_get_thread_num()]) {
			minDistances[omp_get_thread_num()] = distances[vertex];
			minVertices[omp_get_thread_num()] = vertex;
		}
	}
	#pragma omp single
	{
		minDistance = minDistances[omp_get_thread_num()];
		minVertex = minVertices[omp_get_thread_num()];
		for (vertex = 0; vertex < omp_get_num_procs(); ++vertex) {
			if (minDistance > minDistances[vertex]) {
				minDistance = minDistances[vertex];
				minVertex = minVertices[vertex];
			}
		}
	}
}

void updateDistancesPV2(int vertices, bool* shortestPathFinalized, int** graph, int* distances) {
	int vertex;
	#pragma omp for private(vertex)
	for (vertex = 0; vertex < vertices; vertex++) {
		if (!shortestPathFinalized[vertex] && graph[minVertex][vertex] && distances[minVertex] != INT_MAX && distances[minVertex] + graph[minVertex][vertex] < distances[vertex]) {
			distances[vertex] = distances[minVertex] + graph[minVertex][vertex];
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
int* dijkstraPV2(int** graph, int vertices, int sourceNode) {
	double endTime, startTime = omp_get_wtime();

	int* distances = (int*) malloc(vertices*sizeof(int));
	bool* shortestPathFinalized = (bool*) malloc(vertices*sizeof(bool));
	minDistances = (int*) malloc(omp_get_num_procs()*sizeof(int));
	minVertices = (int*) malloc(omp_get_num_procs()*sizeof(int));

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
    		getMinDistancePV2(distances, vertices, shortestPathFinalized);
			#pragma omp single
    		{
    			shortestPathFinalized[minVertex] = true;
    		}
    		updateDistancesPV2(vertices, shortestPathFinalized, graph, distances);
    	}
    }
    endTime = omp_get_wtime();
    printf("Elapsed time for parallel Dijkstra %f\n", endTime-startTime);
    return distances;
}
