import { Graph, matrixGraph } from '../graph/graph';

describe('GraphGenerator', () => {

	test('withEdges', () => {
		const matrixGraph1: Graph = matrixGraph(1)();
		matrixGraph1.addEdge(1, 2, 3);
	});
});
