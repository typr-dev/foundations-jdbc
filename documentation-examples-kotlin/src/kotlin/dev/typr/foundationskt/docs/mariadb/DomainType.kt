package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class UserId(val value: Long)

    // Create MariaType from bigint
    val userIdType: MariaType<UserId> = MariaTypes.bigint.transform(::UserId, UserId::value)
    //stop
}
