package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import java.util.UUID

@Suppress("unused")
class ArrayTypes {
    //start
    // Any scalar type can be made into a PostgreSQL array via `.array()`.
    val intArray: PgType<List<Int>> = PgTypes.int4.array()
    val textArray: PgType<List<String>> = PgTypes.text.array()
    val uuidArray: PgType<List<UUID>> = PgTypes.uuid.array()

    // Multi-dimensional arrays compose: `int4[][]` in SQL.
    val intMatrix: PgType<List<List<Int>>> = PgTypes.int4.array().array()
    //stop
}
