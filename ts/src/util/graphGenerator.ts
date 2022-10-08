import { Graph } from '../model/types';

interface GraphGenerator {
	withDensity(vertices: number, density: number): Graph;

	withEdges(vertices: number, edges: number): Graph;
}

export const createGraphGenerator = (): GraphGenerator => {


	const getMaxEdges = (vertices: number): number => (vertices * vertices - vertices) / 2;

	const addictiveAlgorithm = (vertices: number, edges: number): Graph => {
		const matrix: Graph = [];
		for (let i = 0; i < vertices; i++) {
			matrix[i] = new Array();
		}
		const row: number[] | undefined = matrix[vertices - 1];
		if (row) {
			row[vertices - 1] = 1;
		}
		matrix[vertices - 1]![vertices - 1] = 1;
		return matrix;
	};

	return {
		withDensity(vertices: number, density: number): Graph {
			const edges = getMaxEdges(vertices) * density;
			return this.withEdges(vertices, edges);
		},
		withEdges(vertices: number, edges: number): Graph {
			return addictiveAlgorithm(vertices, edges);
		}
	};
};
