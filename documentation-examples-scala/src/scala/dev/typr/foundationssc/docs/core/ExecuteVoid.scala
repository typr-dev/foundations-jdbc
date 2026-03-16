package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object ExecuteVoid:
  var tx: Transactor = null // placeholder

  //start
  def applySchema(): Unit =
    tx.executeVoid { conn =>
      sql"CREATE TABLE users (id INT, name VARCHAR(100))".execute().run(conn)
      sql"CREATE INDEX idx_users_name ON users (name)".execute().run(conn)
    }
  //stop
