package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes

@Suppress("unused")
class BinaryTypes {
    //start
    val bytesType: PgType<ByteArray> = PgTypes.bytea
    //stop
}
