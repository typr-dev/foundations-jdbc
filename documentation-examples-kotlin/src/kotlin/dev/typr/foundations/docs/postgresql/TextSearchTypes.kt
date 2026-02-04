package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class TextSearchTypes {
    //start
    // Text search types are available via PgTypes
    // Note: tsvector and tsquery have specialized handling
    val textType: PgType<String> = PgTypes.text
    //stop
}
