package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class TextSearchTypes {
    //start
    // Text search types are available via PgTypes
    // Note: tsvector and tsquery have specialized handling
    val textType: PgType<String> = PgTypes.text
    //stop
}
