package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object ExecuteVoid:
  var tx: Transactor = null // placeholder

  // start
  def applySchema(): Unit =
    sql"CREATE TABLE users (id INT, name VARCHAR(100))"
      .execute()
      .combine(sql"CREATE INDEX idx_users_name ON users (name)".execute())
      .voided()
      .transact(tx)
  // stop
