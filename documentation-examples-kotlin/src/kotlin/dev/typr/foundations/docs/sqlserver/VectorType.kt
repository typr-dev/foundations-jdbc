package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class VectorType {
    //start
    val vectorType: SqlServerType<ByteArray> = SqlServerTypes.vector
    //stop
}
