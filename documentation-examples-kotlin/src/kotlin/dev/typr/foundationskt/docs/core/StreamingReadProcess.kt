package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class StreamingReadProcess {
    lateinit var tx: Transactor

    //start
    // Process rows lazily without loading all into memory
    fun countExpensiveProducts(): Long {
        val streaming = Fragment.of("SELECT price FROM products")
            .streamingQuery(PgTypes.int4, 512)

        return streaming.map { cursor ->
            cursor.asSequence().count { it > 100 }.toLong()
        }.transact(tx)
    }
    //stop
}
