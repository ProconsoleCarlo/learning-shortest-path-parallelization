import { Edge, matrixGraph } from './graph';

describe('matrixGraph', () => {
	const VERTICES = 5;
	const X = 1;
	const Y = 2;
	const W = 3;
	const Z = 4;
	const A_WEIGHT = 9;
	const A_NEGATIVE_WEIGHT = -4;

	test('addBidirectionalEdge', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addBidirectionalEdge(X, Y, A_WEIGHT);

		expect(graph.getCost(X, Y)).toBe(A_WEIGHT);
		expect(graph.getCost(Y, X)).toBe(A_WEIGHT);
	});

	test('addEdgeAndGetCost', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addEdge(X, Y, A_WEIGHT);

		expect(graph.getCost(X, Y)).toBe(A_WEIGHT);

		graph.addEdge(X, Y, A_NEGATIVE_WEIGHT);

		expect(graph.getCost(X, Y)).toBe(A_NEGATIVE_WEIGHT);
	});

	test('hasNegativeEdges', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addEdge(X, Y, A_WEIGHT);

		expect(graph.hasNegativeEdges()).toBe(false);

		graph.addEdge(X, Y, A_NEGATIVE_WEIGHT);

		expect(graph.hasNegativeEdges()).toBe(true);
	});

	test('haveConnection', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addEdge(X, Y, A_WEIGHT);

		expect(graph.haveConnection(X, Y)).toBe(true);

		graph.removeEdge(X, Y);

		expect(graph.haveConnection(X, Y)).toBe(false);
	});

	test('neighboursOf', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addEdge(X, Y, A_WEIGHT);
		graph.addEdge(X, Z, A_WEIGHT);
		graph.addEdge(Y, W, A_WEIGHT);

		const expected: Edge[] = [
			{destination: Y, cost: A_WEIGHT},
			{destination: Z, cost: A_WEIGHT}
		];
		expect(graph.neighboursOf(X)).toBe(expected);
	});

	test('removeEdge', () => {
		const graph = matrixGraph(VERTICES)();

		graph.addBidirectionalEdge(X, Y, A_WEIGHT);

		expect(graph.haveConnection(X, Y)).toBe(true);
		expect(graph.haveConnection(Y, X)).toBe(true);

		graph.removeEdge(X, Y);

		expect(graph.haveConnection(X, Y)).toBe(false);
		expect(graph.haveConnection(Y, X)).toBe(false);
	});

	test('vertices', () => {
		const graph = matrixGraph(VERTICES)();

		expect(graph.vertices()).toBe(VERTICES);
	});
});
