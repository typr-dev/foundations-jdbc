package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ComposingAllOf:
  var tx: Transactor = null // placeholder

  // start
  // Run multiple writes in one transaction
  val insertUser: Operation[Int] =
    sql"INSERT INTO users(name) VALUES(${PgTypes.text("Alice")})"
      .update()
  val insertAudit: Operation[Int] =
    sql"INSERT INTO audit_log(action) VALUES(${PgTypes.text("user_created")})"
      .update()
  val updateStats: Operation[Int] =
    sql"UPDATE stats SET user_count = user_count + 1"
      .update()

  def createUserWithAudit(): Unit =
    insertUser
      .combine(insertAudit)
      .combine(updateStats)
      .voided()
      .transact(tx)
  // stop
