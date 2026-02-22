package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: PgType<Int> = PgTypes.int4
    val nullable: PgType<Int?> = PgTypes.int4.opt()
    //stop
}
