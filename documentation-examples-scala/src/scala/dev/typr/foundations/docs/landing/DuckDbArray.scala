package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object DuckDbArray:
  val tx: Transactor = null // placeholder

  //start
  // DuckDB arrays are first-class typed values
  def getTagSets(): List[Array[String]] =
    Fragment.lit("SELECT tags FROM posts WHERE published = true")
      .query(RowParser.of(DuckDbTypes.varcharArray).all())
      .transact(tx)
  //stop
