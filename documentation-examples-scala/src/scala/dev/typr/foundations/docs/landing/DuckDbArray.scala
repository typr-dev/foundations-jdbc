package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object DuckDbArray:
  val tx: Transactor = null // placeholder

  //start
  // DuckDB arrays are first-class typed values
  def getTagSets(): List[Array[String]] =
    val op: SqlFunction[Connection, List[Array[String]]] = conn =>
      Fragment.lit("SELECT tags FROM posts WHERE published = true")
        .query(RowParser.of(DuckDbTypes.varcharArray).all().underlying)
        .run(conn)
    tx.execute(op)
  //stop
