package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object ComposingSequence:
  var tx: Transactor = null // placeholder

  //start
  val names: List[String] =
    List("Alice", "Bob", "Charlie")

  def insertAll(): List[Int] =
    val inserts: List[Operation[Int]] =
      names.map { name =>
        sql"""INSERT INTO users(name)
              VALUES(${PgTypes.text(name)})
              RETURNING id"""
          .query(RowCodec.of(PgTypes.int4).exactlyOne())
      }

    Operation.sequence(inserts).transact(tx)
  //stop
