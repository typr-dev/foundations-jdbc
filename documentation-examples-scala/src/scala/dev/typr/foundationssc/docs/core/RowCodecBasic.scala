package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.time.Instant

@SuppressWarnings(Array("unused"))
object RowCodecBasic:
  //start
  case class Person(id: Int, name: String, createdAt: Instant)

  val personCodec: RowCodec[Person] = RowCodec.builder[Person]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.timestamptz)(_.createdAt)
    .build(Person.apply)
  //stop
