package dev.typr.foundationssc.docs.core

import dev.typr.foundationssc.*
import dev.typr.foundationssc.connect.*

// start
class ConnectionSettingsSetup:
  val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()

  val settings = ConnectionSettings
    .builder()
    .transactionIsolation(TransactionIsolation.READ_COMMITTED)
    .readOnly(true)
    .schema("app")
    .connectionInitSql("SET search_path TO app")
    .build()

  val tx = Transactor.create(config, settings)
// stop
