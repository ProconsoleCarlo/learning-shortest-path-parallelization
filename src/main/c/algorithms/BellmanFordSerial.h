/*
 * BellmanFordSerial.h
 *
 *  Created on: 23/dic/2014
 *      Author: Carlo Bobba, Eleonora Aiello
 */

struct Graph* createGraph(int V, int E);
void BellmanFord(struct Graph* graph, int src);
