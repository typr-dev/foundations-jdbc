package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object StreamingReadFootgun:
  var tx: Transactor = null // placeholder

  // start
  // WRONG: the cursor escapes the transaction — connection is already closed!
  def broken(): Cursor[String] =
    Fragment
      .of("SELECT name FROM users")
      .streamingQuery(PgTypes.text, 512)
      .transact(tx) // connection closes here, cursor is dead

  // CORRECT: process the cursor inside map, before the connection closes
  def correct(): Long =
    Fragment
      .of("SELECT name FROM users")
      .streamingQuery(PgTypes.text, 512)
      .map { cursor =>
        var count = 0L
        cursor.foreach(_ => count += 1)
        count
      }
      .transact(tx)
  // stop
