package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class ListTypes {
    //start
    // Pre-defined list types with optimized native JNI support
    DuckDbType<List<Integer>> listInt = DuckDbTypes.listInteger;
    DuckDbType<List<String>> listStr = DuckDbTypes.listVarchar;
    DuckDbType<List<Double>> listDouble = DuckDbTypes.listDouble;

    // Types that use SQL literal conversion (slightly slower but correct)
    DuckDbType<List<UUID>> listUuid = DuckDbTypes.listUuid;
    DuckDbType<List<LocalDate>> listDate = DuckDbTypes.listDate;
    DuckDbType<List<BigDecimal>> listDecimal = DuckDbTypes.listDecimal;
    //stop
}
