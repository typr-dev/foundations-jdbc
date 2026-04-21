package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.*;

@SuppressWarnings("unused")
public class ConnectionSettingsSetup {
  DatabaseConfig config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build();

  void example() {
    // start
    var settings =
        ConnectionSettings.builder()
            .transactionIsolation(TransactionIsolation.READ_COMMITTED)
            .readOnly(true)
            .schema("app")
            .connectionInitSql("SET search_path TO app")
            .build();

    var tx = Transactor.create(config, settings);
    // stop
  }
}
