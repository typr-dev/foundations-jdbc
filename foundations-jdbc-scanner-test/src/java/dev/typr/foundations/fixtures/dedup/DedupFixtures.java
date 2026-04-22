package dev.typr.foundations.fixtures.dedup;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class DedupFixtures {

  public final OperationRead<List<Integer>> query = Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public final OperationRead<List<Integer>> userQuery =
      Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public OperationRead<List<Integer>> query() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<List<Integer>> getUserQuery() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public OperationRead<List<Integer>> query(int limit) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
