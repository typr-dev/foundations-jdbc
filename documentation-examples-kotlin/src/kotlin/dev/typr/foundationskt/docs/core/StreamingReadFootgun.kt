package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class StreamingReadFootgun {
    lateinit var tx: Transactor

    //start
    // WRONG: the cursor escapes the transaction — connection is already closed!
    fun broken(): Cursor<String> {
        return Fragment.of("SELECT name FROM users")
            .streamingQuery(PgTypes.text, 512)
            .transact(tx) // connection closes here, cursor is dead
    }

    // CORRECT: process the cursor inside map, before the connection closes
    fun correct(): Long {
        return Fragment.of("SELECT name FROM users")
            .streamingQuery(PgTypes.text, 512)
            .map { cursor ->
                var count = 0L
                for (name in cursor) count++
                count
            }
            .transact(tx)
    }
    //stop
}
