/*
 * Dijkstra.h
 *
 *  Created on: 22/dic/2014
 *      Author: Carlo Bobba, Eleonora Aiello
 */
#include <stdbool.h>

int minDistance(int dist[], bool sptSet[]);
int printSolution(int dist[], int n);
/*
 * Per fare l'header ho messo int** graph invece di int graph[][]
 */
void dijkstra(int** graph, int src);
