package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Vector

@Suppress("unused")
class SpecialTypes {
    //start
    val hstoreType: PgType<Map<String, String>> = PgTypes.hstore
    val vectorType: PgType<Vector> = PgTypes.vector
    //stop
}
