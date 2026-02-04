package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Uint1
import dev.typr.foundations.data.Uint4
import dev.typr.foundations.data.Uint8

@Suppress("unused")
class IntegerTypesUnsigned {
    //start
    val unsignedTiny: MariaType<Uint1> = MariaTypes.tinyintUnsigned
    val unsignedInt: MariaType<Uint4> = MariaTypes.intUnsigned
    val unsignedBig: MariaType<Uint8> = MariaTypes.bigintUnsigned
    //stop
}
