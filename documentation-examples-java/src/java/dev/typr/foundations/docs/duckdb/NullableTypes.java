package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbOptType;
import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

@SuppressWarnings("unused")
public class NullableTypes {
    //start
    DuckDbType<Integer> notNull = DuckDbTypes.integer;
    DuckDbOptType<Integer> nullable = DuckDbTypes.integer.opt();
    //stop
}
