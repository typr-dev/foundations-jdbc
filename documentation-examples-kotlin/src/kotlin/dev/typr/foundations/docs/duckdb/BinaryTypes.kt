package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes

@Suppress("unused")
class BinaryTypes {
    //start
    val blobType: DuckDbType<ByteArray> = DuckDbTypes.blob
    //stop
}
