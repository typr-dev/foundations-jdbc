package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingSequence {
    lateinit var tx: Transactor

    //start
    // Execute a list of operations and collect all results
    val names = listOf("Alice", "Bob", "Charlie")

    fun insertAll(): List<Int> {
        val inserts: List<Operation<Int>> = names.map { name ->
            sql { "INSERT INTO users(name) VALUES(${PgTypes.text(name)}) RETURNING id" }
                .query(RowCodec.of(PgTypes.int4).exactlyOne())
        }

        return Operation.sequence(inserts).transact(tx)
    }
    //stop
}
