package dev.typr.foundations.fixtures.instance;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class InstanceFixtures {

  public final Operation<List<Integer>> fieldOp = Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public Operation<List<Integer>> instanceMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> uninvokableMethod(Runnable r) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
