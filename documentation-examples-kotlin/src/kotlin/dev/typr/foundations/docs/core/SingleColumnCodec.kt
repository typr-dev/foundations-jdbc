package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SingleColumnCodec {
    //start
    val idCodec: RowCodec<Int> = RowCodec.of(PgTypes.int4)
    //stop
}
