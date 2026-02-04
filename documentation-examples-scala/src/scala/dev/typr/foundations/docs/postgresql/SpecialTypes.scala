package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.util.{Map as JMap}

@SuppressWarnings(Array("unused"))
object SpecialTypes:
  //start
  val hstoreType: PgType[Map[String, String]] = PgTypes.hstore
  val vectorType: PgType[Vector] = PgTypes.vector
  //stop
