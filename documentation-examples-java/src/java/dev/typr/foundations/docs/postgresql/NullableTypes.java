package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgOptType;
import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class NullableTypes {
    //start
    PgType<Integer> notNull = PgTypes.int4;
    PgOptType<Integer> nullable = PgTypes.int4.opt();  // null values allowed
    //stop
}
