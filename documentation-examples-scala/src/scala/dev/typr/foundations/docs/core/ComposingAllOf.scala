package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object ComposingAllOf:
  var tx: Transactor = null // placeholder

  //start
  // Run multiple writes in one transaction, discard individual results
  val insertUser: Operation[Int] =
    sql"INSERT INTO users(name) VALUES(${Fragment.encode(PgTypes.text, "Alice")})"
      .update()
  val insertAudit: Operation[Int] =
    sql"INSERT INTO audit_log(action) VALUES(${Fragment.encode(PgTypes.text, "user_created")})"
      .update()
  val updateStats: Operation[Int] =
    sql"UPDATE stats SET user_count = user_count + 1"
      .update()

  @throws[SQLException]
  def createUserWithAudit(): Unit =
    Operation.allOf(insertUser, insertAudit, updateStats).transact(tx)
  //stop
