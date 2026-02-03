package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class UserId(val value: Long)

    // Create MariaType from bigint
    val userIdType: MariaType<UserId> = MariaTypes.bigint.bimap(::UserId, UserId::value)
    //stop
}
