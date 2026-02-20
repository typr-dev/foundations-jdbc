package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object FragmentCombinators:
  var conn: Connection = null // placeholder

  //start
  // Dynamic UPDATE — only set the fields that changed
  def updateUser(userId: Int, newName: Option[String], newEmail: Option[String]): Unit =
    val sets = List(
      newName.map(n => sql"name = ${PgTypes.text(n)}"),
      newEmail.map(e => sql"email = ${PgTypes.text(e)}")
    ).flatten

    if sets.nonEmpty then
      sql"UPDATE users ${Fragment.set(sets)} WHERE id = ${PgTypes.int4(userId)}"
        .update()
        .run(conn)
  //stop
