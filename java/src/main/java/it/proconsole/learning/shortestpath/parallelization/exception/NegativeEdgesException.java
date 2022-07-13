package it.proconsole.learning.shortestpath.parallelization.exception;

public class NegativeEdgesException extends RuntimeException {
  public NegativeEdgesException() {
    super("Negative edges are not supported by this algorithm");
  }
}
