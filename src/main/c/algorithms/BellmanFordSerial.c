/*
 * BellmanFordSerial.c
 *
 *  Created on: 23/dic/2014
 *      Author: Carlo Bobba, Eleonora Aiello
 *      Credits: http://www.geeksforgeeks.org/dynamic-programming-set-23-bellman-ford-algorithm/
 */
// A C / C++ program for Bellman-Ford's single source shortest path algorithm.

#include <limits.h>
#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "../Utils/ArraysUtilities.h"



/*
 * Da spostare se possbile (vedi main.c)
 */
// a structure to represent a weighted edge in graph
struct Edge {
    int src, dest, weight;
};

// a structure to represent a connected, directed and weighted graph
struct Graph {
    // V-> Number of vertices, E-> Number of edges
    int V, E;

    // graph is represented as an array of edges.
    struct Edge* edge;
};

/*
// Creates a graph with V vertices and E edges
struct Graph* createGraph(int V, int E)
{
    struct Graph* graph = (struct Graph*) malloc( sizeof(struct Graph) );
    graph->V = V;
    graph->E = E;

    graph->edge = (struct Edge*) malloc( graph->E * sizeof( struct Edge ) );

    return graph;
}
*/

// The main function that finds shortest distances from src to all other
// vertices using Bellman-Ford algorithm.  The function also detects negative
// weight cycle
int* BellmanFord(struct Graph* graph, int src) {

	int V = graph->V;
    int E = graph->E;
    int* dist = (int*) calloc(V, sizeof(int));
    //int dist[V];
    double end, start = omp_get_wtime();

    // Step 1: Initialize distances from src to all other vertices as INFINITE
    int i;
    for (i = 0; i < V; i++) {
        dist[i]   = INT_MAX;
    }
    dist[src] = 0;

    // Step 2: Relax all edges |V| - 1 times. A simple shortest path from src
    // to any other vertex can have at-most |V| - 1 edges
    for (i = 1; i <= V-1; i++) {
    	int j;
        for (j = 0; j < E; j++) {
            int u = graph->edge[j].src;
            int v = graph->edge[j].dest;
            int weight = graph->edge[j].weight;
            if (dist[u] != INT_MAX && dist[u] + weight < dist[v])
                dist[v] = dist[u] + weight;
        }
    }

    // Step 3: check for negative-weight cycles.  The above step guarantees
    // shortest distances if graph doesn't contain negative weight cycle.
    // If we get a shorter path, then there is a cycle.
    bool error = false;
    for (i = 0; i < E; i++) {
    	if (!error) {
    		int u = graph->edge[i].src;
    		int v = graph->edge[i].dest;
    		int weight = graph->edge[i].weight;
    		if (dist[u] != INT_MAX && dist[u] + weight < dist[v]) {
    			error = true;
    		}
    	}
    }
    if (error) {
    	printf("Graph contains negative weight cycle by thread %d\n", omp_get_thread_num());
    }

    end = omp_get_wtime();

    char indexDescription[20] = "Nodo";
    char valueDescription[20] = "Costo";
    printArray(dist, V, indexDescription, valueDescription);
    printf("Elapsed time for serial %f\n", end-start);

    return dist;
}
