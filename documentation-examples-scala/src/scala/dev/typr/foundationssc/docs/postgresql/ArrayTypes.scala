package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.util.UUID

@SuppressWarnings(Array("unused"))
object ArrayTypes:
  // start
  // Boxed arrays
  val intArrayBoxed: PgType[Array[Int]] = PgTypes.int4.array()

  // Unboxed arrays (more efficient)
  val intArrayUnboxed: PgType[Array[Int]] = PgTypes.int4ArrayUnboxed

  // Text arrays
  val textArray: PgType[Array[String]] = PgTypes.text.array()

  // Any type can be made into an array
  val uuidArray: PgType[Array[UUID]] = PgTypes.uuid.array()
  // stop
