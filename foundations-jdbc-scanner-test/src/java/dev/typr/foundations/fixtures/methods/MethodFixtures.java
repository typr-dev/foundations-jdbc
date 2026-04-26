package dev.typr.foundations.fixtures.methods;

import dev.typr.foundations.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class MethodFixtures {

  public final OperationRead<List<Integer>> fieldQuery =
      Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public static final OperationRead<List<Integer>> staticField =
      Fragment.of("SELECT 2").queryAll(PgTypes.int4);

  public OperationRead<List<Integer>> noArgMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<List<Integer>> withArgs(int limit, String name) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<Optional<String>> withStringArg(String email) {
    return Fragment.of("SELECT name FROM users WHERE email = ")
        .value(PgTypes.text, email)
        .query(RowCodec.of(PgTypes.text).maxOne());
  }

  private OperationRead<List<Integer>> privateMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public static OperationRead<List<Integer>> staticMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public static OperationRead<List<Integer>> staticMethodWithArgs(int limit, String name) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public String notAnalyzable() {
    return "hello";
  }
}
