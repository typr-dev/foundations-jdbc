package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class ConnectionSettingsSetup {
    val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()

    // start
    val settings = ConnectionSettings.builder()
        .transactionIsolation(TransactionIsolation.READ_COMMITTED)
        .readOnly(true)
        .schema("app")
        .connectionInitSql("SET search_path TO app")
        .build()

    val tx = Transactor.create(config, settings)
    // stop
}
