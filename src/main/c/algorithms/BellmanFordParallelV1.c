/*
 * BellmanFordParallelV1.c
 *
 * Created on: 31/dic/2014
 * Author: Carlo Bobba, Eleonora Aiello
 * Credits: http://www.geeksforgeeks.org/dynamic-programming-set-23-bellman-ford-algorithm/
 * Description: A C / C++ program for Bellman-Ford's single source shortest path algorithm.
 */

#include <limits.h>
#include <omp.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

int* bellmanFordParallel(int** graph, bool containsNegativeLinks, int graphWidth, int sourceNode) {
	double end, start = omp_get_wtime();
	/*
	 * Usare OpenMP
	 */
	int* distances = (int*) malloc(graphWidth*sizeof(int));
	int i;
	for (i = 0; i < graphWidth; i++) {
		distances[i] = INT_MAX;
	}
	distances[sourceNode] = 0;

	int node, src, dest;
	#pragma omp parallel for private(node, src, dest)
	for (node = 0; node < graphWidth; node++) {
		for (src = 0; src < graphWidth; src++) {
			for (dest = 0; dest < graphWidth; dest++) {
				if (graph[src][dest] != 0) {
					if (distances[dest] > distances[src] + graph[src][dest] && distances[src] != INT_MAX) {
						distances[dest] = distances[src] + graph[src][dest];
					}
				}
			}
		}
	}
	/*
	 * Mettendo così, se si sa che il grafo non ha weight negativi si velocizza notevolmente il processo
	 * Ci sarebbe da fare che invece di fare una printf si incrementi un indice per ogni negative edge cycle
	 * e solo alla fine stampa se ci sono negative cycle e quanti.
	 *
	 * Usare OpenMP
	 */
	if (containsNegativeLinks) {
		for (src = 0; src < graphWidth; ++src) {
			for (dest = 0; dest < graphWidth; ++dest) {
				if (graph[src][dest] != 0) {
					if (distances[dest] > distances[src] + graph[src][dest]) {
						printf("The graph contains negative edge cycle!");
					}
				}
			}
		}
	}
    end = omp_get_wtime();
    printf("Elapsed time for parallel BellmanFord%f\n", end-start);
	return distances;
}

