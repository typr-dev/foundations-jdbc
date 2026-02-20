package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class StreamingInsertSingle {

    //start
    // Insert a list of strings using COPY
    fun insertNames(names: Iterator<String>, tx: Transactor): Long {
        return streamingInsert
            .of("COPY users(name) FROM STDIN", 1000, names, PgTypes.text.pgText())
            .transact(tx)
    }
    //stop
}
