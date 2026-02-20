package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class OperationReturning {
    data class User(val id: Int, val name: String)

    val userCodec: RowCodec<User> = RowCodec.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .build(::User)

    //start
    // INSERT ... RETURNING id, name
    val insertedUsers: Operation<List<User>> =
        Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
            .updateReturning(userCodec.all())

    val insertedUser: Operation<User> =
        Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
            .updateReturning(userCodec.exactlyOne())
    //stop
}
