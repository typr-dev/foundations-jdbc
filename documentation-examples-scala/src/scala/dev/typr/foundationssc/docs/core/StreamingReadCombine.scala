package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object StreamingReadCombine:
  var tx: Transactor = null // placeholder

  //start
  // Open two cursors simultaneously on the same connection
  def mergedNames(): List[String] =
    val activeUsers = Fragment.of("SELECT name FROM users WHERE active")
      .streamingQuery(PgTypes.text, 512)
    val archivedUsers = Fragment.of("SELECT name FROM archived_users")
      .streamingQuery(PgTypes.text, 512)

    activeUsers.combine(archivedUsers).map { (active, archived) =>
      active.toList ++ archived.toList
    }.transact(tx)
  //stop
