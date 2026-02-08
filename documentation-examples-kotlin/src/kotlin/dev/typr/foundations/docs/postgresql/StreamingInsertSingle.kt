package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*

@Suppress("unused")
class StreamingInsertSingle {

    //start
    // Insert a list of strings using COPY
    fun insertNames(names: List<String>, tx: Transactor): Long {
        return streamingInsert
            .of("COPY users(name) FROM STDIN", 1000, names.iterator(), PgTypes.text.pgText())
            .transact(tx)
    }
    //stop
}
