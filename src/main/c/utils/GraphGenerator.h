/*
 * GraphGenerator.h
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: A simple random graph generator
 */

#include <stdbool.h>
/*
 * Function that generate a random graph represented with an adjacency matrix
 * int vertices The number of vertices of the graph
 * int edges The number of edges of the graph
 * bool negativeValueAllowed If are allowed negative weight in the graph or not
 */
int** generateGraphAsAdjacencyMatrix (int vertices, int edges, bool negativeValueAllowed);

