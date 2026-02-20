package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection
import java.time.Instant

@Suppress("unused")
class FragmentBuilding {
    data class User(val id: Int, val name: String, val status: String, val createdAt: Instant)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
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
        sql { """
            SELECT * FROM users
            WHERE id = ${PgTypes.int4(userId)}
                AND status = ${PgTypes.text("active")}
                AND created_at > ${PgTypes.timestamptz(cutoffDate)}
        """ }

    // Execute safely — parameters are bound, not interpolated
    val users: List<User> =
        query.query(userCodec.all()).run(connection)
    //stop
}
