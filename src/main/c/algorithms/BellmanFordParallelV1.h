/*
 * BellmanFordParallelV1.h
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.geeksforgeeks.org/dynamic-programming-set-23-bellman-ford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 */

#include <stdbool.h>

int* bellmanFordParallel(int** graph, bool containsNegativeLinks, int graphWidth, int sourceNode);
