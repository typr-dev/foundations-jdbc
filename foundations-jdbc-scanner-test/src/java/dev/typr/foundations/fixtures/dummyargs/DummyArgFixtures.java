package dev.typr.foundations.fixtures.dummyargs;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class DummyArgFixtures {

  public enum Color {
    RED,
    GREEN,
    BLUE
  }

  public record Filter(String name, int limit) {}

  public static class Config {
    public Config(String host, int port) {}
  }

  public Operation<List<Integer>> withEnum(Color c) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withRecord(Filter f) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withLocalDate(LocalDate d) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withInstant(Instant i) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withOffsetDateTime(OffsetDateTime odt) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withUUID(UUID id) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withList(List<String> items) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withOptional(Optional<String> o) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withMap(Map<String, Integer> m) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withBigDecimal(BigDecimal bd) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withPrimitives(
      boolean b, byte by, short s, int i, long l, float f, double d, char c) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withArray(int[] arr) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withCustomClass(Config config) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
