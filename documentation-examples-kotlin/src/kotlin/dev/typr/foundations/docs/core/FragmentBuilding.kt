package dev.typr.foundations.docs.core

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
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

    lateinit var connection: Connection
    val userId: Int = 1
    val cutoffDate: Instant = Instant.now()

    //start
    val query: Fragment =
        Fragment.of("SELECT * FROM users WHERE id = ")
            .value(PgTypes.int4, userId)
            .append(" AND status = ")
            .value(PgTypes.text, "active")
            .append(" AND created_at > ")
            .value(PgTypes.timestamptz, cutoffDate)

    // Execute safely — parameters are bound, not interpolated
    val users: List<User> =
        query.query(userParser.all()).run(connection)
    //stop
}
