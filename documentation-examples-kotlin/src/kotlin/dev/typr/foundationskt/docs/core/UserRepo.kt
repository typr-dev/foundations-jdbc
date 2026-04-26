package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
//start
object UserRepo {
    data class User(val id: Int, val name: String)

    val userCodec: RowCodecNamed<User> =
        RowCodec.namedBuilder<User>()
            .field("id", PgTypes.int4, User::id)
            .field("name", PgTypes.text, User::name)
            .build(::User)

    val selectAll: OperationRead<List<User>> =
        sql { "SELECT ${userCodec.columnList} FROM users ORDER BY name" }
            .query(userCodec.all())
            .named("UserRepo.selectAll")

    fun selectById(id: Int): OperationRead<User?> =
        sql { "SELECT ${userCodec.columnList} FROM users WHERE id = " }
            .value(PgTypes.int4, id)
            .query(userCodec.maxOne())
}
//stop
