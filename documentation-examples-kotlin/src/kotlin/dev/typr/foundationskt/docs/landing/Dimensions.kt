package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class Dimensions {
    //start
    data class Dim(
        val width: Double, val height: Double,
        val depth: Double, val unit: String
    )

    // PgStruct handles PostgreSQL's composite wire format
    val pgStruct: PgStruct<Dim> =
        PgStruct.builder<Dim>("dimensions")
            .field("width", PgTypes.float8, Dim::width)
            .field("height", PgTypes.float8, Dim::height)
            .field("depth", PgTypes.float8, Dim::depth)
            .field("unit", PgTypes.text, Dim::unit)
            .build(::Dim)

    val pgType: PgType<Dim> = pgStruct.asType()
    //stop
}
