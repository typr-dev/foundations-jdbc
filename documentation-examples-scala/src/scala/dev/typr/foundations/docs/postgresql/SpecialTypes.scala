package dev.typr.foundations.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.util.{Map as JMap}

@SuppressWarnings(Array("unused"))
object SpecialTypes:
  //start
  val hstoreType: PgType[Map[String, String]] = PgTypes.hstore
  val vectorType: PgType[Vector] = PgTypes.vector
  //stop
