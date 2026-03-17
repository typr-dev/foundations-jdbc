package dev.typr.foundations.fixtures.directives;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class DirectiveFixtures {

  public Operation<List<Integer>> toBeSkipped() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> needsManual(Runnable r) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> normalMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
