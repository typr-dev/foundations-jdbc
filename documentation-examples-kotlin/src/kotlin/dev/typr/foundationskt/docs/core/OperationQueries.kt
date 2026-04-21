package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class OperationQueries {
    data class User(val id: Int, val name: String)

    val userCodec: RowCodec<User> = RowCodec.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .build(::User)

    val fragment: Fragment = sql { "SELECT id, name FROM users" }

    //start
    // Multi-column: pass a RowCodec with a result mode
    val allUsers: OperationRead<List<User>> = fragment.query(userCodec.all())
    val maybeUser: OperationRead<User?> = fragment.query(userCodec.maxOne())
    val oneUser: OperationRead<User> = fragment.query(userCodec.exactlyOne())

    // Single-column: shorthand methods skip the codec
    val allIds: OperationRead<List<Int>> =
        sql { "SELECT id FROM users" }.queryAll(PgTypes.int4)
    val maybeName: OperationRead<String?> =
        sql { "SELECT name FROM users LIMIT 1" }.queryMaxOne(PgTypes.text)
    val count: OperationRead<Int> =
        sql { "SELECT count(*) FROM users" }.queryExactlyOne(PgTypes.int4)
    //stop
}
