package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

import java.util.UUID;

@SuppressWarnings("unused")
public class ArrayTypes {
    //start
    // Boxed arrays
    PgType<Integer[]> intArrayBoxed = PgTypes.int4Array;

    // Unboxed arrays (more efficient)
    PgType<int[]> intArrayUnboxed = PgTypes.int4ArrayUnboxed;

    // Text arrays
    PgType<String[]> textArray = PgTypes.textArray;

    // Any type can be made into an array
    PgType<UUID[]> uuidArray = PgTypes.uuidArray;
    //stop
}
