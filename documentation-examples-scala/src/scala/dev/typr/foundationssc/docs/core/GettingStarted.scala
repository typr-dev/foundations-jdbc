package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.connect.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object GettingStarted:
  // start
  val tx: Transactor =
    SimpleDataSource
      .create(DuckDbConfig.inMemory().build())
      .transactor()

  val result: Int = sql"SELECT 42"
    .queryExactlyOne(DuckDbTypes.integer)
    .transact(tx)
  // stop
