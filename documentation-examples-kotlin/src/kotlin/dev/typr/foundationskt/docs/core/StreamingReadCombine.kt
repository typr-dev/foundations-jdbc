package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class StreamingReadCombine {
    lateinit var tx: Transactor

    //start
    // Open two cursors simultaneously on the same connection
    fun mergedNames(): List<String> {
        val activeUsers = Fragment.of("SELECT name FROM users WHERE active")
            .streamingQuery(PgTypes.text, 512)
        val archivedUsers = Fragment.of("SELECT name FROM archived_users")
            .streamingQuery(PgTypes.text, 512)

        return activeUsers.combine(archivedUsers).map { (active, archived) ->
            active.toList() + archived.toList()
        }.transact(tx)
    }
    //stop
}
