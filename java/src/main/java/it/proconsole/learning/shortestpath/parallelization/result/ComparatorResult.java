package it.proconsole.learning.shortestpath.parallelization.result;

import java.util.List;

public record ComparatorResult(
        List<Algorithm> algorithms,
        boolean correct
) {
}
