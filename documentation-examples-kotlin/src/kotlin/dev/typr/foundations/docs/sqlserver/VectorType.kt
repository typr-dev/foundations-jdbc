package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class VectorType {
    //start
    val vectorType: SqlServerType<ByteArray> = SqlServerTypes.vector
    //stop
}
