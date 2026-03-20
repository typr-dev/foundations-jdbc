package dev.typr.foundationssc.docs.dbtypes
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import org.postgresql.geometric.PGpoint
import java.time.LocalDate

@SuppressWarnings(Array("unused"))
object TypeSafeDbTypes:
  // start
  // PostgreSQL types
  val intArray: PgType[Array[Int]] = PgTypes.int4ArrayUnboxed
  val dateRange: PgType[Range[LocalDate]] = PgTypes.daterange

  // MariaDB types
  val json: MariaType[Json] = MariaTypes.json

  // DuckDB types
  val map: DuckDbType[Map[String, Int]] = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer)
  // stop
