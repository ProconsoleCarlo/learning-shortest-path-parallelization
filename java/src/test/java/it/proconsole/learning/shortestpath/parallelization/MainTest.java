package it.proconsole.learning.shortestpath.parallelization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {
  @Test
  void main() {
    String[] args = {"vertices=10","density=0.5","sourceNode=0"};

    assertDoesNotThrow(() -> Main.main(args));
  }
}