package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class OperationUpdates {
  Fragment fragment = Fragment.of("DELETE FROM users WHERE last_login < now() - interval '1 year'");

  // start
  // Get the affected row count
  Operation<Integer> rowCount = fragment.update();

  // Ignore the row count (DDL, fire-and-forget DML)
  Operation<Void> ignored = fragment.execute();
  // stop
}
