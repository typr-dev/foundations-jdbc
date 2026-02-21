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

    val selectAll: Operation<List<User>> =
        sql { "SELECT ${userCodec.columnList} FROM users ORDER BY name" }
            .query(userCodec.all())
            .named("UserRepo.selectAll")

    val selectById: Template<Int, User?> =
        sql { "SELECT ${userCodec.columnList} FROM users WHERE id = " }
            .param(PgTypes.int4)
            .query(userCodec.maxOne())

}
//stop
