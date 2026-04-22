package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.PgConfig;

@SuppressWarnings("unused")
public class TransactorConfig {
  PgConfig config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build();
  // start
  Transactor tx = Transactor.create(config);
  Transactor testTx = tx.rollbackOnly();
  // stop
}
