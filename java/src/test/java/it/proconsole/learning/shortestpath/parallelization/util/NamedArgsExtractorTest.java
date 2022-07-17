package it.proconsole.learning.shortestpath.parallelization.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedArgsExtractorTest {
  @Test
  void getDouble() {
    String[] args = {"noNamed","validDouble=1.2","invalidDouble=a"};
    var extractor = new NamedArgsExtractor(args);

    var actualValid = extractor.getDouble("validDouble");
    var actualInvalid = extractor.getDouble("invalidDouble");

    assertTrue(actualValid.isPresent());
    assertEquals(1.2, actualValid.get());
    assertFalse(actualInvalid.isPresent());
  }

  @Test
  void getFloat() {
    String[] args = {"noNamed","validFloat=1.2","invalidFloat=a"};
    var extractor = new NamedArgsExtractor(args);

    var actualValid = extractor.getFloat("validFloat");
    var actualInvalid = extractor.getFloat("invalidFloat");

    assertTrue(actualValid.isPresent());
    assertEquals(1.2F, actualValid.get());
    assertFalse(actualInvalid.isPresent());
  }

  @Test
  void getInteger() {
    String[] args = {"noNamed","validInt=1","invalidInt=a"};
    var extractor = new NamedArgsExtractor(args);

    var actualValid = extractor.getInteger("validInt");
    var actualInvalid = extractor.getInteger("invalidInt");

    assertTrue(actualValid.isPresent());
    assertEquals(1, actualValid.get());
    assertFalse(actualInvalid.isPresent());
  }

  @Test
  void getLong() {
    String[] args = {"noNamed","validLong=1","invalidLong=a"};
    var extractor = new NamedArgsExtractor(args);

    var actualValid = extractor.getLong("validLong");
    var actualInvalid = extractor.getLong("invalidLong");

    assertTrue(actualValid.isPresent());
    assertEquals(1L, actualValid.get());
    assertFalse(actualInvalid.isPresent());
  }

  @Test
  void getString() {
    String[] args = {"noNamed","validString=pippo","invalidString=$ac"};
    var extractor = new NamedArgsExtractor(args);

    var actualValid = extractor.getString("validString");
    var actualInvalid = extractor.getString("invalidString");

    assertTrue(actualValid.isPresent());
    assertEquals("pippo", actualValid.get());
    assertFalse(actualInvalid.isPresent());
  }
}