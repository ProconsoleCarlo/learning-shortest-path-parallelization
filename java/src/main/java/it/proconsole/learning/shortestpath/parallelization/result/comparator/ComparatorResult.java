package it.proconsole.learning.shortestpath.parallelization.result.comparator;

import java.util.List;

public record ComparatorResult(
        List<Algorithm> algorithms,
        boolean correct
) {
}
