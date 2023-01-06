package it.proconsole.learning.shortestpath.parallelization.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NamedArgsExtractor {
  private static final Predicate<String> ARG_PREDICATE = Pattern.compile("(\\w+)=(\\w+)").asPredicate();

  private final Map<String, String> namedParameters;

  public NamedArgsExtractor(String[] args) {
    this.namedParameters = Arrays.stream(args)
            .filter(ARG_PREDICATE)
            .map(arg -> arg.split("="))
            .collect(Collectors.toMap(x -> x[0].toLowerCase(), x -> x[1]));
  }

  public Optional<Double> getDouble(String argName) {
    try {
      return Optional.ofNullable(namedParameters.get(argName.toLowerCase()))
              .map(Double::parseDouble);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public Optional<Float> getFloat(String argName) {
    try {
      return Optional.ofNullable(namedParameters.get(argName.toLowerCase()))
              .map(Float::parseFloat);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public Optional<Integer> getInteger(String argName) {
    try {
      return Optional.ofNullable(namedParameters.get(argName.toLowerCase()))
              .map(Integer::parseInt);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public Optional<Long> getLong(String argName) {
    try {
      return Optional.ofNullable(namedParameters.get(argName.toLowerCase()))
              .map(Long::parseLong);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public Optional<String> getString(String argName) {
    return Optional.ofNullable(namedParameters.get(argName.toLowerCase()));
  }
}
