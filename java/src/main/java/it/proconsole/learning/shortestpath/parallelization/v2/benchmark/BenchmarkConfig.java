package it.proconsole.learning.shortestpath.parallelization.v2.benchmark;

/** Configuration for benchmark execution. */
public final class BenchmarkConfig {

  private final int vertices;
  private final double density;
  private final int minWeight;
  private final int maxWeight;
  private final int warmupIterations;
  private final int measurementIterations;
  private final int sourceVertex;
  private final boolean validateResults;

  private BenchmarkConfig(Builder builder) {
    this.vertices = builder.vertices;
    this.density = builder.density;
    this.minWeight = builder.minWeight;
    this.maxWeight = builder.maxWeight;
    this.warmupIterations = builder.warmupIterations;
    this.measurementIterations = builder.measurementIterations;
    this.sourceVertex = builder.sourceVertex;
    this.validateResults = builder.validateResults;
  }

  public static Builder builder() {
    return new Builder();
  }

  public int getVertices() {
    return vertices;
  }

  public double getDensity() {
    return density;
  }

  public int getMinWeight() {
    return minWeight;
  }

  public int getMaxWeight() {
    return maxWeight;
  }

  public int getWarmupIterations() {
    return warmupIterations;
  }

  public int getMeasurementIterations() {
    return measurementIterations;
  }

  public int getSourceVertex() {
    return sourceVertex;
  }

  public boolean isValidateResults() {
    return validateResults;
  }

  @Override
  public String toString() {
    return String.format(
        "BenchmarkConfig{vertices=%d, density=%.2f, weights=[%d,%d], warmup=%d, iterations=%d, source=%d, validate=%b}",
        vertices,
        density,
        minWeight,
        maxWeight,
        warmupIterations,
        measurementIterations,
        sourceVertex,
        validateResults);
  }

  public static class Builder {
    private int vertices = 1000;
    private double density = 0.3;
    private int minWeight = 1;
    private int maxWeight = 100;
    private int warmupIterations = 3;
    private int measurementIterations = 10;
    private int sourceVertex = 0;
    private boolean validateResults = true;

    public Builder vertices(int vertices) {
      if (vertices <= 0) {
        throw new IllegalArgumentException("Vertices must be positive");
      }
      this.vertices = vertices;
      return this;
    }

    public Builder density(double density) {
      if (density < 0.0 || density > 1.0) {
        throw new IllegalArgumentException("Density must be between 0.0 and 1.0");
      }
      this.density = density;
      return this;
    }

    public Builder minWeight(int minWeight) {
      this.minWeight = minWeight;
      return this;
    }

    public Builder maxWeight(int maxWeight) {
      this.maxWeight = maxWeight;
      return this;
    }

    public Builder warmupIterations(int warmupIterations) {
      if (warmupIterations < 0) {
        throw new IllegalArgumentException("Warmup iterations must be non-negative");
      }
      this.warmupIterations = warmupIterations;
      return this;
    }

    public Builder measurementIterations(int measurementIterations) {
      if (measurementIterations <= 0) {
        throw new IllegalArgumentException("Measurement iterations must be positive");
      }
      this.measurementIterations = measurementIterations;
      return this;
    }

    public Builder sourceVertex(int sourceVertex) {
      if (sourceVertex < 0) {
        throw new IllegalArgumentException("Source vertex must be non-negative");
      }
      this.sourceVertex = sourceVertex;
      return this;
    }

    public Builder validateResults(boolean validateResults) {
      this.validateResults = validateResults;
      return this;
    }

    public BenchmarkConfig build() {
      if (sourceVertex >= vertices) {
        throw new IllegalArgumentException("Source vertex must be less than number of vertices");
      }
      if (minWeight > maxWeight) {
        throw new IllegalArgumentException("Min weight cannot exceed max weight");
      }
      return new BenchmarkConfig(this);
    }
  }
}
