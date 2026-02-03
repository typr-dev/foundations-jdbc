package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.Vector
import java.util.{Map as JMap}

@SuppressWarnings(Array("unused"))
object SpecialTypes:
  //start
  val hstoreType: PgType[JMap[String, String]] = PgTypes.hstore
  val vectorType: PgType[Vector] = PgTypes.vector
  //stop
