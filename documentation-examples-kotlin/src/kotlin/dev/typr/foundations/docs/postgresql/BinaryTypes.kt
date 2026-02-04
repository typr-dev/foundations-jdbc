package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val bytesType: PgType<ByteArray> = PgTypes.bytea
    //stop
}
