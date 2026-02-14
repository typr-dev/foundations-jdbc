package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object ComposingSequence:
  var tx: Transactor = null // placeholder

  //start
  // Execute a list of operations and collect all results
  val names: List[String] = List("Alice", "Bob", "Charlie")

  @throws[SQLException]
  def insertAll(): List[Int] =
    val inserts: List[Operation[Int]] =
      names.map { name =>
        sql"INSERT INTO users(name) VALUES(${Fragment.encode(PgTypes.text, name)}) RETURNING id"
          .query(RowParser.of(PgTypes.int4).exactlyOne())
      }

    Operation.sequence(inserts).transact(tx)
  //stop
