package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val blobType: DuckDbType<ByteArray> = DuckDbTypes.blob
    //stop
}
