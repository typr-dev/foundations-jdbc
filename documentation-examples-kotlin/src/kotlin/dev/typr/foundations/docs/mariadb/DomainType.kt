package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class UserId(val value: Long)

    // Create MariaType from bigint
    val userIdType: MariaType<UserId> = MariaTypes.bigint.bimap(::UserId, UserId::value)
    //stop
}
