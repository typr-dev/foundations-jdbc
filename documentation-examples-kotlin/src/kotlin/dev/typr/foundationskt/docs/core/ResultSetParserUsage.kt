package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.ResultSet

import java.time.Instant

@Suppress("unused")
class ResultSetParserUsage {
    data class Person(val id: Int, val name: String, val createdAt: Instant)

    val personCodec: RowCodec<Person> =
        RowCodec.builder<Person>()
            .field(PgTypes.int4, Person::id)
            .field(PgTypes.text, Person::name)
            .field(PgTypes.timestamptz, Person::createdAt)
            .build(::Person)

    lateinit var resultSet: ResultSet

    //start
    // Parse at most one result - returns Person? (nullable)
    val singleParser: ResultSetParser<Person?> = personCodec.maxOne()

    // Parse all results as a list
    val listParser: ResultSetParser<List<Person>> = personCodec.all()

    // Execute with ResultSet
    fun parse(): Person? = singleParser.apply(resultSet)
    //stop
}
