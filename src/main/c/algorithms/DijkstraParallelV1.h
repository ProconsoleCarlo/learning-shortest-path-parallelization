/*
 * DijkstraParallelV1.h
 *
 * Created on: 27/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.geeksforgeeks.org/greedy-algorithms-set-6-dijkstras-shortest-path-algorithm/
 * Descrition: A C / C++ program for Dijkstra's single source shortest path algorithm.
 * 			   The program is for adjacency matrix representation of the graph.
 */

/*
 * Compute the minimum path in a graph represented with adiacency matrix
 * using Dijkstra algorithm
 * int** graph The graph in the adiacency matrix form
 * int vertices The number of vertices in the graph
 * int sourceNode The node from which compute the distances
 * return The minimum distances from sourceNode
 */
int* dijkstraSerial(int** graph, int vertices, int sourceNode);
