package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class ExecuteVoid {
    lateinit var tx: Transactor

    //start
    fun applySchema() {
        val createTable = Fragment.of("CREATE TABLE users (id INT, name VARCHAR(100))").execute()
        val createIndex = Fragment.of("CREATE INDEX idx_users_name ON users (name)").execute()
        tx.execute(Operation.allOf(createTable, createIndex))
    }
    //stop
}
