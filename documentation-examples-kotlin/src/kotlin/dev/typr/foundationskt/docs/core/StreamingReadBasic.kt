package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class StreamingReadBasic {
    lateinit var tx: Transactor

    //start
    // Stream rows lazily and materialize into a list
    fun allNames(): List<String> {
        val streaming = Fragment.of("SELECT name FROM users ORDER BY id")
            .streamingQuery(PgTypes.text, 512)

        return streaming
            .map { it.toList() }
            .transact(tx)
    }
    //stop
}
