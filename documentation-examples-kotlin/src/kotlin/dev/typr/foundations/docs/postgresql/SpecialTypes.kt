package dev.typr.foundations.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SpecialTypes {
    //start
    val hstoreType: PgType<Map<String, String>> = PgTypes.hstore
    val vectorType: PgType<Vector> = PgTypes.vector
    //stop
}
