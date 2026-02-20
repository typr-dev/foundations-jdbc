package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object TextSearchTypes:
  //start
  // Text search types are available via PgTypes
  // Note: tsvector and tsquery have specialized handling
  val textType: PgType[String] = PgTypes.text
  //stop
