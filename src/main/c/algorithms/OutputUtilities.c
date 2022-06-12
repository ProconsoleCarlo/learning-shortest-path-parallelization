/*
 * OutputUtilities.c
 *
 *  Created on: 23/dic/2014
 *      Author: Carlo
 */

#include <stdio.h>
#include "OutputUtilities.h"

// A utility function used to print the solution
void printArr(int dist[], int n) {
    printf("Vertex   Distance from Source\n");
    int i;
    for (i = 0; i < n; ++i) {
        printf("%d \t\t %d\n", i, dist[i]);
    }
}

