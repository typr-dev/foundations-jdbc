package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Suppress("unused")
class ListTypes {
    //start
    // Any type can be made into a list with .list()
    val listInt: DuckDbType<List<Int>> = DuckDbTypes.integer.list()
    val listStr: DuckDbType<List<String>> = DuckDbTypes.varchar.list()
    val listDouble: DuckDbType<List<Double>> = DuckDbTypes.double_.list()
    val listUuid: DuckDbType<List<UUID>> = DuckDbTypes.uuid.list()
    val listDate: DuckDbType<List<LocalDate>> = DuckDbTypes.date.list()
    val listDecimal: DuckDbType<List<BigDecimal>> = DuckDbTypes.decimal.list()
    //stop
}
