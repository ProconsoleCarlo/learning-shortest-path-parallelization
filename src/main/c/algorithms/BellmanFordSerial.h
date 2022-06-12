/*
 * BellmanFordSerial.h
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.sanfoundry.com/java-program-implement-bellmanford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 */
#include <stdbool.h>

int* bellmanFordSerial(int** graph, bool containsNegativeLinks, int graphWidth, int sourceNode);
