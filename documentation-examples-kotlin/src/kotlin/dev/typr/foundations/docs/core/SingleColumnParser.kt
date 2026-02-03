package dev.typr.foundations.docs.core

import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser

@Suppress("unused")
class SingleColumnParser {
    //start
    val idParser: RowParser<Int> = RowParser.of(PgTypes.int4)
    //stop
}
