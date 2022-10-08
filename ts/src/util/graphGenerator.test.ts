import { Graph } from '../model/types';
import { createGraphGenerator } from './graphGenerator';

describe('GraphGenerator', () => {
	const graphGenerator = createGraphGenerator();
	test('withEdges', () => {
		const graph: Graph = graphGenerator.withEdges(5, 5);

		const row = graph;
		const column = graph[4]!;
		expect(row.length).toBe(5);
		expect(column.length).toBe(5);
	});
});
