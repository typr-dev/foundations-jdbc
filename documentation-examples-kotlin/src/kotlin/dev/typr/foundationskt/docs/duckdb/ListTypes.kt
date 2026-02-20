package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Suppress("unused")
class ListTypes {
    //start
    // Pre-defined list types with optimized native JNI support
    val listInt: DuckDbType<List<Int>> = DuckDbTypes.listInteger
    val listStr: DuckDbType<List<String>> = DuckDbTypes.listVarchar
    val listDouble: DuckDbType<List<Double>> = DuckDbTypes.listDouble

    // Types that use SQL literal conversion (slightly slower but correct)
    val listUuid: DuckDbType<List<UUID>> = DuckDbTypes.listUuid
    val listDate: DuckDbType<List<LocalDate>> = DuckDbTypes.listDate
    val listDecimal: DuckDbType<List<BigDecimal>> = DuckDbTypes.listDecimal
    //stop
}
