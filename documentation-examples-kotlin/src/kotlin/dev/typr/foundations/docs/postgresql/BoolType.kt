package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class BoolType {
    //start
    val boolType: PgType<Boolean> = PgTypes.bool
    //stop
}
