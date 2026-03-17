package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.*;

@SuppressWarnings("unused")
public class GettingStarted {
  // start
  Transactor tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor();

  int result = Fragment.of("SELECT 42").queryExactlyOne(DuckDbTypes.integer).transact(tx);
  // stop
}
