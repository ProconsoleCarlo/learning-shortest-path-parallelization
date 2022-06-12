/*
 * Main.c
 *
 *  Created on: 22/dic/2014
 *      Author: Carlo
 */

#include "../Algorithms/DijkstraSerial.h"

#define V 9

int main() {
	void doSerialDijkstra();
	doSerialDijkstra();


    return 0;
}
void doSerialDijkstra() {
	/*
	* Dijkstra algorithm
	* TODO: In Dijkstra.c togliere la definizione di V che verr� preso dalla size di graph se � possibile farlo
	* 		altrimenti lo passiamo come parametro alla funzione qui
	*/
	// Let us create the example graph discussed above
	int graph[V][V] = {{0, 4, 0, 0, 0, 0, 0, 8, 0},
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
