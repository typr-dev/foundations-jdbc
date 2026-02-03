package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import java.util.UUID

@Suppress("unused")
class ArrayTypes {
    //start
    // Boxed arrays
    val intArrayBoxed: PgType<Array<Int>> = PgTypes.int4Array

    // Unboxed arrays (more efficient)
    val intArrayUnboxed: PgType<IntArray> = PgTypes.int4ArrayUnboxed

    // Text arrays
    val textArray: PgType<Array<String>> = PgTypes.textArray

    // Any type can be made into an array
    val uuidArray: PgType<Array<UUID>> = PgTypes.uuidArray
    //stop
}
