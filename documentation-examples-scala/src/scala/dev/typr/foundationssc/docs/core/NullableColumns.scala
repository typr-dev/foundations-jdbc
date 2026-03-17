package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object NullableColumns:
  // start
  case class Person(id: Int, name: String, createdAt: Option[Instant])

  val personCodec: RowCodec[Person] = RowCodec
    .builder[Person]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.timestamptz.opt)(_.createdAt)
    .build(Person.apply)
  // stop
