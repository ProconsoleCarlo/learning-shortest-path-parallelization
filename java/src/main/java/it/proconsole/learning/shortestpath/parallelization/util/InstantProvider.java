package it.proconsole.learning.shortestpath.parallelization.util;

import java.time.Clock;
import java.time.Instant;

public class InstantProvider {
  private final Clock clock;

  public InstantProvider(Clock clock) {
    this.clock = clock;
  }

  public Instant now() {
    return Instant.now(clock);
  }
}
