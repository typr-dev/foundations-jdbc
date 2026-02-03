package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes

@Suppress("unused")
class TextSearchTypes {
    //start
    // Text search types are available via PgTypes
    // Note: tsvector and tsquery have specialized handling
    val textType: PgType<String> = PgTypes.text
    //stop
}
