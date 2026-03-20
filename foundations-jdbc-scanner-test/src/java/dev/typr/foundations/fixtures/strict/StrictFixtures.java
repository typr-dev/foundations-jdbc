package dev.typr.foundations.fixtures.strict;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class StrictFixtures {

  public Operation<List<Integer>> uninvokable(Runnable r) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> workingMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
