/*
 * Dijkstra.h
 *
 *  Created on: 22/dic/2014
 *      Author: Carlo Bobba, Eleonora Aiello
 */
#include <stdbool.h>

int minDistance(int dist[], bool sptSet[]);

int* dijkstra(int** graph, int graphWidth, int sourceNode);
