package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableTypes {
    //start
    PgType<Integer> notNull = PgTypes.int4;
    PgType<Optional<Integer>> nullable = PgTypes.int4.opt();  // null values allowed
    //stop
}
