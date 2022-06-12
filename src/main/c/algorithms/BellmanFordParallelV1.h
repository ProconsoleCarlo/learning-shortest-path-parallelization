/*
 * BellmanFordSerial.h
 *
 *  Created on: 23/dic/2014
 *      Author: Carlo Bobba, Eleonora Aiello
 */

struct Graph* createGraph(int V, int E);
int* BellmanFordParallelV1(struct Graph* graph, int src);
