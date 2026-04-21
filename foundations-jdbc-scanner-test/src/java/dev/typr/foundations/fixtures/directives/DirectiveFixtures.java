package dev.typr.foundations.fixtures.directives;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class DirectiveFixtures {

  public OperationRead<List<Integer>> toBeSkipped() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<List<Integer>> needsManual(Runnable r) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<List<Integer>> normalMethod() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
