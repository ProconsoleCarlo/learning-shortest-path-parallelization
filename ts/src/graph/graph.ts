export interface Graph {
	addBidirectionalEdge: (from: number, to: number, cost: number) => void,
	addEdge: (from: number, to: number, cost: number) => void,
	getCost: (from: number, to: number) => number | undefined,
	hasNegativeEdges: () => boolean,
	haveConnection: (from: number, to: number) => boolean,
	neighboursOf: (node: number) => Edge[],
	removeEdge: (from: number, to: number) => void,
	vertices: () => number,
}

export type Edge = {
	destination: number,
	cost: number
}

export const matrixGraph = (vertices: number) => (): Graph => {
	const values: number[][] | undefined[][] = new Array(vertices).fill(0).map(() => new Array(vertices));
	return {
		addBidirectionalEdge: (from: number, to: number, cost: number): void => {
			values[from]!![to] = cost;
			values[to]!![from] = cost;
		},
		addEdge: (from: number, to: number, cost: number): void => {
			values[from]!![to] = cost;
		},
		getCost: (from: number, to: number): number | undefined => values[from]!![to],
		hasNegativeEdges: (): boolean => {
			return values.map(it => it.some(isNegative)).some(isTrue);
		},
		haveConnection: (from: number, to: number): boolean => values[from]!![to] != undefined,
		neighboursOf: (node: number): Edge[] => {
			const value: number[] | undefined[] = values[node];
			value.filter(cost => cost != undefined);
			return [];
		},
		removeEdge: (from: number, to: number): void => {
			values[from]!![to] = undefined;
			values[to]!![from] = undefined;
		},
		vertices: (): number => vertices,
	};
};

const isNegative = (element: number | undefined) => element ? element < 0 : false;
const isTrue = (element: boolean) => element;
