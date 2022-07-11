package it.proconsole.learning.shortestpath.parallelization.model;

import java.util.Arrays;

public class DistancesWithFinalization extends Distances {
  private final boolean[] finalized;

  public DistancesWithFinalization(int vertices, int sourceNode) {
    super(vertices, sourceNode);
    this.finalized = new boolean[vertices];
  }

  public boolean isFinalized(int node) {
    return finalized[node];
  }

  public void setFinalized(int node) {
    this.finalized[node] = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DistancesWithFinalization that)) return false;
    if (!super.equals(o)) return false;
    return Arrays.equals(finalized, that.finalized);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Arrays.hashCode(finalized);
    return result;
  }

  @Override
  public String toString() {
    return "DistancesWithFinalization{" +
            "values=" + Arrays.toString(getValues()) +
            "finalized=" + Arrays.toString(finalized) +
            "} ";
  }
}
