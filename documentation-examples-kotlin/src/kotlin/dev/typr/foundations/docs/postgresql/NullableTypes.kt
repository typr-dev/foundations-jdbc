package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.DbType
import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.nullable

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: PgType<Int> = PgTypes.int4
    val nullable: DbType<Int?> = PgTypes.int4.nullable  // null values allowed
    //stop
}
