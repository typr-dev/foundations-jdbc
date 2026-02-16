package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SingleColumnParser {
    //start
    val idParser: RowParser<Int> = RowParser.of(PgTypes.int4)
    //stop
}
