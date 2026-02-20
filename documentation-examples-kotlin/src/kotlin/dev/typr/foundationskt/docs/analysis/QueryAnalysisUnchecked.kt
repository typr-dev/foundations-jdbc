package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class QueryAnalysisUnchecked {
    //start
    data class Stats(val name: String, val count: Int)

    // .unchecked() skips type checking entirely for this column
    val statsCodec: RowCodec<Stats> =
        RowCodec.builder<Stats>()
            .field(PgTypes.text, Stats::name)
            .field(PgTypes.int4.unchecked(), Stats::count)
            .build(::Stats)
    //stop
}
