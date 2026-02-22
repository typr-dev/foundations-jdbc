package dev.typr.foundationssc.docs.postgresql

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object StreamingInsertSingle:

  //start
  // Insert a list of strings using COPY
  def insertNames(names: Iterator[String], tx: Transactor): Long =
    streamingInsert
      .of("COPY users(name) FROM STDIN", 1000, names, PgTypes.text.pgText())
      .transact(tx)
  //stop
