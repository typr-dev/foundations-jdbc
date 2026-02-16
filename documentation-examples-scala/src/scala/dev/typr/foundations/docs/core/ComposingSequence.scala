package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object ComposingSequence:
  var tx: Transactor = null // placeholder

  //start
  val names: List[String] =
    List("Alice", "Bob", "Charlie")

  @throws[SQLException]
  def insertAll(): List[Int] =
    val inserts: List[Operation[Int]] =
      names.map { name =>
        sql"""INSERT INTO users(name)
              VALUES(${PgTypes.text(name)})
              RETURNING id"""
          .query(RowParser.of(PgTypes.int4).exactlyOne())
      }

    Operation.sequence(inserts).transact(tx)
  //stop
