package dev.typr.foundations.fixtures.dedup;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class DedupFixtures {

  public final Operation<List<Integer>> query = Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public final Operation<List<Integer>> userQuery = Fragment.of("SELECT 1").queryAll(PgTypes.int4);

  public Operation<List<Integer>> query() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> getUserQuery() {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }

  public Operation<List<Integer>> query(int limit) {
    return Fragment.of("SELECT 1").queryAll(PgTypes.int4);
  }
}
