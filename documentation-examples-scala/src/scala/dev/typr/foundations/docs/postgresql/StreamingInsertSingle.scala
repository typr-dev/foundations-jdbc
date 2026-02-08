package dev.typr.foundations.docs.postgresql

import dev.typr.scalafoundations.*

@SuppressWarnings(Array("unused"))
object StreamingInsertSingle:

  //start
  // Insert a list of strings using COPY
  def insertNames(names: Iterator[String], tx: Transactor): Long =
    streamingInsert
      .of("COPY users(name) FROM STDIN", 1000, names, PgTypes.text.pgText())
      .transact(tx)
  //stop
