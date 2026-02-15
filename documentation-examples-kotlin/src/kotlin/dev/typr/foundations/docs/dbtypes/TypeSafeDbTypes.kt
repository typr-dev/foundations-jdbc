package dev.typr.foundations.docs.dbtypes

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import org.postgresql.geometric.PGpoint
import java.time.LocalDate

@Suppress("unused")
class TypeSafeDbTypes {
    //start
    // PostgreSQL types
    val intArray: PgType<IntArray> = PgTypes.int4ArrayUnboxed
    val dateRange: PgType<Range<LocalDate>> = PgTypes.daterange

    // MariaDB types
    val json: MariaType<Json> = MariaTypes.json

    // DuckDB types
    val map: DuckDbType<Map<String, Int>> = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer)
    //stop
}
