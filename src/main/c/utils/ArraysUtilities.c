/*
 * ArraysUtilities.c
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba
 * Description: Some utilities to manage arrays
 */

#include <stdbool.h>
#include <stdio.h>
#include <string.h>

/*
 * Function that check if two arrays contains the same values
 * int* array1 The first array
 * int lenght1 The lenght of the first array
 * int* array2 The second array
 * int lenght2 The lenght of the second array
 * return true il the two arrays contains the same value, false otherwise
 */
bool areArraysEquals(int* array1, int lenght1, int* array2, int lenght2) {
	if (lenght1 != lenght2) {
		return false;
	} else {
		int lenght = lenght1;
		int i = 0;
		int error = 0;
		while(i < lenght) {
			if (array1[i] != array2[i]) {
				error = 1;
				break;
			}else {
				i++;
			}
		}
		if (error != 0) {
			return false;
		}else {
			return true;
		}
	}
}

/*
 * A function that print in a good way the values of an array
 * int* array The array to print
 * int lenght The lenght of the array
 * char* indexDescription The label of the indices (
 * 						  If "" (null) the indices will not be printed
 * char* valueDescription The label of the values
 */
void printArray(int* array, int lenght, char* indexDescription, char* valueDescription) {
	if (strcmp(indexDescription, "") == 0) {
		printf("%s\n", valueDescription);
		int i;
		for (i = 0; i < lenght; ++i) {
			printf("%d\n", array[i]);
		}
	}else {
		printf("%s \t|\t %s\n", indexDescription, valueDescription);
		int i;
		for (i = 0; i < lenght; ++i) {
			printf("%d \t\t|\t %d\n", i, array[i]);
		}
	}
}

/*
 * A function that print in a good way the values of a matrix
 * int** matrix The matrix to print
 * int width The width of the matrix
 * int height The height of the matrix
 */
void printMatrix(int** matrix, int width, int height) {
	int i, j;
	for (i = 0; i < height; ++i) {
		for (j = 0; j < width; ++j) {
			printf("%d\t", matrix[i][j]);
		}
		printf("\n");
	}
}
