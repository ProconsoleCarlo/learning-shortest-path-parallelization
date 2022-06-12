/*
 * DijkstraSerial.c
 *
 * Created on: 22/dic/2014
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

void getMinDistanceSerial(int* distances, int vertices, bool* shortestPathFinalized) {
	minDistance = INT_MAX;
	int vertex;
	for (vertex = 0; vertex < vertices; vertex++) {
		if (!shortestPathFinalized[vertex] && distances[vertex] <= minDistance) {
			minDistance = distances[vertex];
			minVertex = vertex;
		}
	}
}

void updateDistancesSerial(int vertices, bool* shortestPathFinalized, int** graph, int* distances) {
	int vertex;
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
int* dijkstraSerial(int** graph, int vertices, int sourceNode) {
	double endTime, startTime = omp_get_wtime();
	double endInitTime, startInitTime = omp_get_wtime();
	int* distances = (int*) malloc(vertices*sizeof(int));
	bool* shortestPathFinalized = (bool*) malloc(vertices*sizeof(bool));
	int i;
    for (i = 0; i < vertices; i++) {
        distances[i] = INT_MAX;
        shortestPathFinalized[i] = false;
    }
    distances[sourceNode] = 0;
    endInitTime = omp_get_wtime();
    printf("Elapsed time to initialize distances: %f\n", endInitTime-startInitTime);

	double getMinDistanceTime = 0, updateDistancesTime = 0;
	double time1, time2;
    int count;
    for (count = 0; count < vertices; count++) {
    	time1 = omp_get_wtime();
    	getMinDistanceSerial(distances, vertices, shortestPathFinalized);
    	shortestPathFinalized[minVertex] = true;
    	time2 = omp_get_wtime();
    	getMinDistanceTime = getMinDistanceTime + time2 - time1;
    	time1 = omp_get_wtime();
    	updateDistancesSerial(vertices, shortestPathFinalized, graph, distances);
    	time2 = omp_get_wtime();
    	updateDistancesTime = updateDistancesTime + time2 - time1;
    }
    printf("Elapsed time for getMinDistance: %f\n", getMinDistanceTime);
    printf("Elapsed time for updateDistance: %f\n", updateDistancesTime);
    endTime = omp_get_wtime();
    printf("Elapsed time for serial  Dijkstra %f\n", endTime-startTime);
    return distances;
}
