package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: PgType<Int> = PgTypes.int4
    val nullable: DbType<Int?> = PgTypes.int4.nullable  // null values allowed
    //stop
}
