package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import java.util.UUID

@SuppressWarnings(Array("unused"))
object ArrayTypes:
  //start
  // Boxed arrays
  val intArrayBoxed: PgType[Array[Integer]] = PgTypes.int4Array

  // Unboxed arrays (more efficient)
  val intArrayUnboxed: PgType[Array[Int]] = PgTypes.int4ArrayUnboxed

  // Text arrays
  val textArray: PgType[Array[String]] = PgTypes.textArray

  // Any type can be made into an array
  val uuidArray: PgType[Array[UUID]] = PgTypes.uuidArray
  //stop
