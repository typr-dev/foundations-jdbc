package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: SqliteType<Long> = SqliteTypes.integer
    val nullable: SqliteType<Long?> = SqliteTypes.integer.opt()
    //stop
}
