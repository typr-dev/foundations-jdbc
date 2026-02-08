package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object DuckDbArray:
  val tx: Transactor = null // placeholder

  //start
  // DuckDB arrays are first-class typed values
  def getTagSets(): List[Array[String]] =
    sql"SELECT tags FROM posts WHERE published = true"
      .query(RowParser.of(DuckDbTypes.varcharArray).all())
      .transact(tx)
  //stop
