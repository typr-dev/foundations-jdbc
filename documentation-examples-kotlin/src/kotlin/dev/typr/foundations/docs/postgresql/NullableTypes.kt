package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: PgType<Int> = PgTypes.int4
    val nullable: PgType<Int?> = PgTypes.int4.opt()
    //stop
}
