/*
 * ArraysUtilities.h
 *
 * Created on: 24/dic/2014
 * Author: Carlo Bobba
 * Description: Some utilities to manage arrays
 */

/*
 * Function that make the symmetric of an upper sided triangle matrix
 * int** matrix The upper sided triangle matrix
 * int width
 */
int** makeMatrixSymmetric(int** matrix, int width);
/*
 * Function that check if two arrays contains the same values
 * int* array1 The first array
 * int lenght1 The lenght of the first array
 * int* array2 The second array
 * int lenght2 The lenght of the second array
 * return true il the two arrays contains the same value, false otherwise
 */
bool areArraysEquals(int* array1, int lenght1, int* array2, int lenght2);
/*
 * A function that print in a good way the values of an array
 * int* array The array to print
 * int lenght The lenght of the array
 * char* indexDescription The label of the indices (
 * 						  If "" (null) the indices will not be printed
 * char* valueDescription The label of the values
 */
void printArray(int* array, int lenght, char* indexDescription, char* valueDescription);
