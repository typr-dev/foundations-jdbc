package dev.typr.foundations.docs.dbtypes

import dev.typr.foundations.*
import dev.typr.foundations.data.{Json, Range}
import org.postgresql.geometric.PGpoint
import java.time.LocalDate

@SuppressWarnings(Array("unused"))
object TypeSafeDbTypes:
  //start
  // PostgreSQL types
  val intArray: PgType[Array[Int]] = PgTypes.int4ArrayUnboxed
  val dateRange: PgType[Range[LocalDate]] = PgTypes.daterange

  // MariaDB types
  val json: MariaType[Json] = MariaTypes.json

  // DuckDB types
  val map: DuckDbType[java.util.Map[String, Integer]] = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer)
  //stop
