/*
 * GraphGenerator.h
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Description: A simple random graph generator
 */

#include <stdbool.h>
/*
 * Function that generate a random graph represented with an adjacency matrix.
 * The way the algorithm works depends on the density of links in the graph. (See "Statistiche GraphGenerator.xlsx" for more info.
 * int vertices The number of vertices of the graph
 * int edges The number of edges of the graph (If it's more than the maximum, the number of edges will be the maximum)
 * bool negativeEdgesAllowed If are allowed negative weight for the edges in the graph or not
 */
int** generateGraphAsAdjacencyMatrix (int vertices, int edges, bool negativeEdgesAllowed);
