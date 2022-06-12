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
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>



// A utility function to find the vertex with minimum distance value, from
// the set of vertices not yet included in shortest path tree
int minDistance(int dist[], int graphWidth, bool sptSet[]) {
	// Initialize min value
	int min = INT_MAX, min_index;
	int v;
	for (v = 0; v < graphWidth; v++)
		if (sptSet[v] == false && dist[v] <= min) {
			min = dist[v];
			min_index = v;
		}
	return min_index;
}

// Funtion that implements Dijkstra's single source shortest path algorithm
// for a graph represented using adjacency matrix representation
int* dijkstra(int** graph, int graphWidth, int sourceNode) {
	/*
	 * dist[i] will hold the shortest distance from sourceNode to the i-node
	 */
	int* distance = (int*) malloc(graphWidth*sizeof(int));

	/*
	 * shortestPathFinalized[i] will true if vertex i is included in shortest path tree or shortest distance from src to i is finalized
	 */
	bool* shortestPathFinalized = (bool*) malloc(graphWidth*sizeof(bool));

    // Initialize all distances as INFINITE and stpSet[] as false
    int i;
    for (i = 0; i < graphWidth; i++) {
        distance[i] = INT_MAX;
        shortestPathFinalized[i] = false;
    }
    // Distance of source vertex from itself is always 0
    distance[sourceNode] = 0;

    // Find shortest path for all vertices
    int count;
    for (count = 0; count < graphWidth; count++) {
    	// Pick the minimum distance vertex from the set of vertices not
    	// yet processed. u is always equal to src in first iteration.
    	int u = minDistance(distance, graphWidth, shortestPathFinalized);

    	// Mark the picked vertex as processed
    	shortestPathFinalized[u] = true;

    	// Update distance value of the adjacent vertices of the picked vertex.
    	int v;
    	for (v = 0; v < graphWidth; v++) {

    		// Update distance[v] only if is not in sptSet, there is an edge from
    		// u to v, and total weight of path from src to  v through u is
    		// smaller than current value of distance[v]
    		if (!shortestPathFinalized[v] && graph[u][v] && distance[u] != INT_MAX && distance[u]+graph[u][v] < distance[v]) {
    			distance[v] = distance[u] + graph[u][v];
    		}
    	}
    }
    return distance;
}
