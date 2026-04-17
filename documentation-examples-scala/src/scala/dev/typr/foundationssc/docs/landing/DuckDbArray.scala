package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object DuckDbArray:
  val tx: Transactor = null // placeholder

  // start
  // DuckDB LIST columns are first-class typed values
  def getTagSets(): List[List[String]] =
    sql"SELECT tags FROM posts WHERE published = true"
      .query(RowCodec.of(DuckDbTypes.varchar.list).all())
      .transact(tx)
  // stop
