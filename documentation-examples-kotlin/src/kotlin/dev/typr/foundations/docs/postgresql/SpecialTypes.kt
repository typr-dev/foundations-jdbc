package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.data.Vector

@Suppress("unused")
class SpecialTypes {
    //start
    val hstoreType: PgType<Map<String, String>> = PgTypes.hstore
    val vectorType: PgType<Vector> = PgTypes.vector
    //stop
}
