/*
 * Main.c
 *
 *  Created on: 22/dic/2014
 *     Author: Carlo Bobba, Eleonora Aiello
 */

#include <stdbool.h>
#include <stdlib.h>
#include <sys/types.h>

#include "../Algorithms/BellmanFordParallelV1.h"
#include "../Algorithms/BellmanFordSerial.h"
#include "../Utils/GraphGenerator.h"
#include "TestDijkstraSerial.h"


/*
 * TODO se possibile spostare la struttura dati in graph.c
 * 	ossia definire la struttura dati Edge e Graph in graph.c e poi usarla dove serve
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

int main() {
	void doSerialDijkstra();
	int* doSerialBellmanFord();
	int* doBellmanFordParallel();
	void checkCorrectness(int*, int*);
	void buildGraph(int, int, struct Graph*, bool );

	int vertices = 5;  // Number of vertices in graph
	int edges = 6;  // Number of edges in graph
	struct Graph* graphOld = createGraph(vertices, edges);
	buildGraph(edges, vertices, graphOld, false);

	//bool check = areArraysEquals(doSerialBellmanFord(edges, vertices, graphOld), vertices, doBellmanFordParallel(edges, vertices, graphOld), vertices);
	/*if (check) {
		printf("perfect!");
	}else {
		printf("error");
	}*/
	int** graph = generateGraphAsAdjacencyMatrix(vertices, edges, false);
	testDijkstraSerial(graph, 5, 0);
	//testGraphGenerator(23000, 60000, false);
    return 0;
}

/*
 * @Deprecated
 * Metodo per creare un grafo grande a sufficienza per misurare le prestazioni.
 * L'algoritmo è da migliorare perchè genera un po di nodi isolati (viene distanza l'intero piu grande rappresentabile)
 * (problema minore se il numero di connessioni è sufficientemente elevato(10 volte il numero di nodi))
 * E Il numero di collegamenti
 * V Il numero di nodi
 * Grap* Il grafo in cui mettere nodi e collegamenti
 */
void buildGraph(int edges, int vertices, struct Graph* graph, bool negativeAllowed) {
	int i;
	time_t t;
	srand((unsigned) time(&t));
	for (i = 0; i < edges; i++) {
		graph->edge[i].src = rand() % vertices;
		graph->edge[i].dest = rand() % vertices;
		int weight = rand() % 10;
		if (weight == 0) {
			if (negativeAllowed) {
				int temp = rand() % 2;
				weight = - temp;

			}else {
				weight = 1;
			}
		}
		graph->edge[i].weight = weight;
	}
}
/*
 * @Deprecated
 */
int* doBellmanFordParallel(int E, int V, struct Graph* graph) {
	return BellmanFordParallelV1(graph, 0);
}

/*
 * @Deprecated
 */
int* doSerialBellmanFord(int E, int V, struct Graph* graph) {
	// Let us create the graph given in above example
	/*
	// add edge 0-1 (or A-B in above figure)
	graph->edge[0].src = 0;
	graph->edge[0].dest = 1;
	graph->edge[0].weight = -1;

	// add edge 0-2 (or A-C in above figure)
	graph->edge[1].src = 0;
	graph->edge[1].dest = 2;
	graph->edge[1].weight = 4;

	// add edge 1-2 (or B-C in above figure)
	graph->edge[2].src = 1;
	graph->edge[2].dest = 2;
	graph->edge[2].weight = 3;

	// add edge 1-3 (or B-D in above figure)
	graph->edge[3].src = 1;
	graph->edge[3].dest = 3;
	graph->edge[3].weight = 2;

	// add edge 1-4 (or A-E in above figure)
	graph->edge[4].src = 1;
	graph->edge[4].dest = 4;
	graph->edge[4].weight = 2;

	// add edge 3-2 (or D-C in above figure)
	graph->edge[5].src = 3;
	graph->edge[5].dest = 2;
	graph->edge[5].weight = 5;

	// add edge 3-1 (or D-B in above figure)
	graph->edge[6].src = 3;
	graph->edge[6].dest = 1;
	graph->edge[6].weight = 1;

	// add edge 4-3 (or E-D in above figure)
	graph->edge[7].src = 4;
	graph->edge[7].dest = 3;
	graph->edge[7].weight = -3;
*/
	return BellmanFord(graph, 0);
}
