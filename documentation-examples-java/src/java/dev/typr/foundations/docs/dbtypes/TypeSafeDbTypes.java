package dev.typr.foundations.docs.dbtypes;

import dev.typr.foundations.*;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.Range;
import org.postgresql.geometric.PGpoint;
import java.time.LocalDate;
import java.util.Map;

@SuppressWarnings("unused")
public class TypeSafeDbTypes {
    //start
    // PostgreSQL types
    PgType<int[]> intArray = PgTypes.int4ArrayUnboxed;
    PgType<Range<LocalDate>> dateRange = PgTypes.daterange;
    PgType<PGpoint> point = PgTypes.point;

    // MariaDB types
    MariaType<Json> json = MariaTypes.json;

    // DuckDB types
    DuckDbType<Map<String, Integer>> map = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer);
    //stop
}
