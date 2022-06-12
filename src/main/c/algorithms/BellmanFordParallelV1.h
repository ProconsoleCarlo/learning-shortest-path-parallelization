/*
 * BellmanFordParallelV1.h
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.geeksforgeeks.org/dynamic-programming-set-23-bellman-ford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 */

#include <stdbool.h>

/*
 * Compute the minimum path in a graph represented with adiacency matrix
 * using Bellman-Ford algorithm
 * int** graph The graph in the adiacency matrix form
 * bool negativeEdgesAllowed If edges can have a negative weight it's necessary to check the presence of cycles
 * int vertices The number of vertices in the graph
 * int sourceNode The node from which compute the distances
 * return The minimum distances from sourceNode
 */
int* bellmanFordParallelV1(int** graph, bool negativeEdgesAllowed, int vertices, int sourceNode);
