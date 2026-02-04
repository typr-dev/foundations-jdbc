package dev.typr.foundations.docs.core

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class SingleColumnParser {
    //start
    val idParser: RowParser<Int> = RowParser.of(PgTypes.int4)
    //stop
}
