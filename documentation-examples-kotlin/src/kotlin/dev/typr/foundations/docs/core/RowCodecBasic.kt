package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Instant

@Suppress("unused")
class RowCodecBasic {
    //start
    data class Person(val id: Int, val name: String, val createdAt: Instant)

    val personCodec: RowCodec<Person> =
        RowCodec.builder<Person>()
            .field(PgTypes.int4, Person::id)
            .field(PgTypes.text, Person::name)
            .field(PgTypes.timestamptz, Person::createdAt)
            .build(::Person)
    //stop
}
