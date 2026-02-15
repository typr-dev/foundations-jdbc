package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Instant

@Suppress("unused")
class NullableColumns {
    //start
    data class Person(val id: Int, val name: String, val createdAt: Instant?)

    val personParser: RowParser<Person> = RowParser.builder<Person>()
        .field(PgTypes.int4, Person::id)
        .field(PgTypes.text, Person::name)
        .field(PgTypes.timestamptz.opt(), Person::createdAt)
        .build(::Person)
    //stop
}
