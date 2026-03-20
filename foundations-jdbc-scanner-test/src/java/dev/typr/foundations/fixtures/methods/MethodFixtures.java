package dev.typr.foundations.fixtures.methods;

import dev.typr.foundations.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class MethodFixtures {

  public final Operation<List<Integer>> fieldQuery = Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public Operation<List<Integer>> noArgMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> withArgs(int limit, String name) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Template<String, Optional<String>> templateMethod() {
    return Fragment.of("SELECT name FROM users WHERE email = ")
        .param(PgTypes.text)
        .query(RowCodec.of(PgTypes.text).maxOne());
  }

  private Operation<List<Integer>> privateMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public static Operation<List<Integer>> staticMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public String notAnalyzable() {
    return "hello";
  }
}
