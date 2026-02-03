package dev.typr.foundations.docs.core

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import java.sql.Connection
import java.time.Instant

@Suppress("unused")
class FragmentBuilding {
    data class User(val id: Int, val name: String, val status: String, val createdAt: Instant)

    val userParser: RowParser<User> = RowParser.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::status)
        .field(PgTypes.timestamptz, User::createdAt)
        .build(::User)

    val connection: Connection? = null // placeholder
    val userId: Int = 1
    val cutoffDate: Instant = Instant.now()

    //start
    val query: Fragment = Fragment.interpolate("SELECT * FROM users WHERE id = ")
        .param(PgTypes.int4, userId)
        .sql(" AND status = ")
        .param(PgTypes.text, "active")
        .sql(" AND created_at > ")
        .param(PgTypes.timestamptz, cutoffDate)
        .done()

    // Execute safely — parameters are bound, not interpolated
    val users: List<User> = query.query(userParser.all()).runUnchecked(connection)
    //stop
}
