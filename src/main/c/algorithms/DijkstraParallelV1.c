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

void getMinDistancePV1(int dist[], int graphWidth, bool sptSet[]) {
	minDistance = INT_MAX;
	int v = 0;
	for (v = 0; v < graphWidth; v++) {
		if (!sptSet[v] && dist[v] <= minDistance) {
			minDistance = dist[v];
			minVertex = v;
		}
	}
}

void updateDistancesPV1(int graphWidth, bool* shortestPathFinalized, int** graph, int* distance) {
	int v;
	#pragma omp for private(v)
	for (v = 0; v < graphWidth; v++) {
		if (!shortestPathFinalized[v] && graph[minVertex][v] && distance[minVertex] != INT_MAX && distance[minVertex] + graph[minVertex][v] < distance[v]) {
			distance[v] = distance[minVertex] + graph[minVertex][v];
		}
	}
}

int* dijkstraPV1(int** graph, int graphWidth, int sourceNode) {
	double end, start = omp_get_wtime();
	int* distance = (int*) malloc(graphWidth*sizeof(int));
	bool* shortestPathFinalized = (bool*) malloc(graphWidth*sizeof(bool));

    #pragma omp parallel
    {
		int i;
		#pragma omp for private(i)
		for (i = 0; i < graphWidth; i++) {
			distance[i] = INT_MAX;
			shortestPathFinalized[i] = false;
		}
		#pragma omp single
    	{
    		distance[sourceNode] = 0;
    	}
    	int count;
    	for (count = 0; count < graphWidth; count++) {
			#pragma omp single
    		{
    			getMinDistancePV1(distance, graphWidth, shortestPathFinalized);

    			shortestPathFinalized[minVertex] = true;
    		}
    		updateDistancesPV1(graphWidth, shortestPathFinalized, graph, distance);
    	}
    }
    end = omp_get_wtime();
    printf("Elapsed time for parallel %f\n", end-start);
    return distance;
}
