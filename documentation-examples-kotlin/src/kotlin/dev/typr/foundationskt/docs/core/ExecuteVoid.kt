package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class ExecuteVoid {
    lateinit var tx: Transactor

    //start
    fun applySchema() {
        tx.executeVoid { conn ->
            sql { "CREATE TABLE users (id INT, name VARCHAR(100))" }.execute().run(conn)
            sql { "CREATE INDEX idx_users_name ON users (name)" }.execute().run(conn)
        }
    }
    //stop
}
