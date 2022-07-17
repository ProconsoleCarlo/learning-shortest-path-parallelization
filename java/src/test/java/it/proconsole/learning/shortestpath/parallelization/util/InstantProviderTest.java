package it.proconsole.learning.shortestpath.parallelization.util;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstantProviderTest {
  @Test
  void now() {
    var now = Instant.parse("2022-07-04T16:44:04.00Z");
    var provider = new InstantProvider(Clock.fixed(now, ZoneId.systemDefault()));

    assertEquals(now, provider.now());
  }
}