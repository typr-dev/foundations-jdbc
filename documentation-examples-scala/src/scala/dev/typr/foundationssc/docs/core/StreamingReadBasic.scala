package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object StreamingReadBasic:
  var tx: Transactor = null // placeholder

  //start
  // Stream rows lazily and materialize into a list
  def allNames(): List[String] =
    val streaming = sql"SELECT name FROM users ORDER BY id"
      .streamingQuery(PgTypes.text, 512)

    streaming
      .map(_.toList)
      .transact(tx)
  //stop
