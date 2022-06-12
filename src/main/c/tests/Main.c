/*
 * Main.c
 *
 *  Created on: 22/dic/2014
 *     Author: Carlo Bobba, Eleonora Aiello
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>

#include "../Algorithms/BellmanFordParallelV1.h"
#include "../Algorithms/BellmanFordSerial.h"
#include "../Algorithms/DijkstraSerial.h"


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

	int V = 5000;  // Number of vertices in graph
	int E = 80000;  // Number of edges in graph
	struct Graph* graph = createGraph(V, E);
	buildGraph(E, V, graph, true);
	//doSerialDijkstra();
	//doSerialBellmanFord(E, V, graph);
	//doBellmanFordParallel(E, V, graph);

	checkCorrectness(doSerialBellmanFord(E, V, graph), doBellmanFordParallel(E, V, graph));

    return 0;
}

void checkCorrectness(int* dist1, int* dist2){
	if (sizeof(dist1)/sizeof(int) != sizeof(dist2)/sizeof(int)) {
		printf("\nError: the two arrays have different lenght!\n");
	} else {
		int lenght = sizeof(dist1)/sizeof(int);
		int i;
		int error = 0;
		while(i < lenght) {
			if (dist1[i] != dist2[i]) {
				error = 1;
				break;
			}else {
				i++;
			}
		}
		if (error != 0) {
			printf("\nError: the two arrays differs for some values!\n");
		}else {
			printf("\nPerfect: the arrays are equals! :D");
		}
	}
}
void doSerialDijkstra() {
	/*
	* Dijkstra algorithm
	* TODO: In Dijkstra.c togliere la definizione di V (che è il 9) che verrà preso dalla size di graph se è possibile farlo
	* 		altrimenti lo passiamo come parametro alla funzione qui
	*/
	// Let us create the example graph discussed above
	int graph[9][9] = {{0, 4, 0, 0, 0, 0, 0, 8, 0},
	                   {4, 0, 8, 0, 0, 0, 0, 11, 0},
	                   {0, 8, 0, 7, 0, 4, 0, 0, 2},
	                   {0, 0, 7, 0, 9, 14, 0, 0, 0},
	                   {0, 0, 0, 9, 0, 10, 0, 0, 0},
	                   {0, 0, 4, 0, 10, 0, 2, 0, 0},
	                   {0, 0, 0, 14, 0, 2, 0, 1, 6},
	                   {8, 11, 0, 0, 0, 0, 1, 0, 7},
					   {0, 0, 2, 0, 0, 0, 6, 7, 0}
	                   };
	dijkstra(graph, 0);
}
/*
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
		/*if (i ==2) {
			weight = -;
		}*/
		graph->edge[i].weight = weight;
	}
}

int* doBellmanFordParallel(int E, int V, struct Graph* graph) {

//	buildGraph(E, V, graph);
	return BellmanFordParallelV1(graph, 0);
}

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
