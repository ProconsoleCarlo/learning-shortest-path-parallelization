package it.proconsole.learning.shortestpath.parallelization.util;

import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.random.RandomGenerator;

import static it.proconsole.learning.shortestpath.parallelization.model.Graph.ZERO_WEIGHT;

public class EdgeWeightGenerator {
  private final RandomGenerator randomGenerator;
  private final int maxWeight;
  @Nullable
  private final Integer maxNegativeWeight;

  private EdgeWeightGenerator(RandomGenerator randomGenerator, Integer maxWeight, @Nullable Integer maxNegativeWeight) {
    this.randomGenerator = randomGenerator;
    this.maxWeight = maxWeight;
    this.maxNegativeWeight = maxNegativeWeight;
  }

  public int getValue() {
    var weight = randomBetween(ZERO_WEIGHT, maxWeight);
    if (weight == ZERO_WEIGHT) {
      return Optional.ofNullable(maxNegativeWeight)
              .map(it -> randomBetween(maxNegativeWeight, ZERO_WEIGHT))
              .orElse(maxWeight);
    } else {
      return weight;
    }
  }

  private int randomBetween(int from, int to) {
    return randomGenerator.nextInt(from, to);
  }

  public static class Builder {
    private final RandomGenerator randomGenerator;
    private int maxWeight;
    @Nullable
    private Integer maxNegativeWeight;

    private Builder(RandomGenerator randomGenerator, int maxWeight, @Nullable Integer maxNegativeWeight) {
      this.randomGenerator = randomGenerator;
      this.maxWeight = maxWeight;
      this.maxNegativeWeight = maxNegativeWeight;
    }

    public static Builder aEdgeWeightGenerator(RandomGenerator randomGenerator) {
      return new Builder(randomGenerator, 5, null);
    }

    public Builder withMaxWeight(int maxWeight) {
      if (maxWeight < 0) throw new IllegalArgumentException("MaxWeight must be positive");
      this.maxWeight = maxWeight;
      return this;
    }

    public Builder withMaxNegativeWeight(Integer maxNegativeWeight) {
      if (maxNegativeWeight >= 0) throw new IllegalArgumentException("MaxNegativeWeight must be negative");
      this.maxNegativeWeight = maxNegativeWeight;
      return this;
    }

    public EdgeWeightGenerator build() {
      return new EdgeWeightGenerator(randomGenerator, maxWeight, maxNegativeWeight);
    }
  }
}
