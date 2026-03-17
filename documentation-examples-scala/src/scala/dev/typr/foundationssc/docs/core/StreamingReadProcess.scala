package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object StreamingReadProcess:
  var tx: Transactor = null // placeholder

  // start
  // Process rows lazily without loading all into memory
  def countExpensiveProducts(): Long =
    val streaming = Fragment
      .of("SELECT price FROM products")
      .streamingQuery(PgTypes.int4, 512)

    streaming
      .map { cursor =>
        cursor.count(_ > 100).toLong
      }
      .transact(tx)
  // stop
