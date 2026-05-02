package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class IntegerTypes {
    //start
    val integerType: SqliteType<Long> = SqliteTypes.integer
    val bigintType: SqliteType<Long> = SqliteTypes.bigint
    val intType: SqliteType<Int> = SqliteTypes.int_
    val smallintType: SqliteType<Short> = SqliteTypes.smallint
    val tinyintType: SqliteType<Byte> = SqliteTypes.tinyint
    //stop
}
