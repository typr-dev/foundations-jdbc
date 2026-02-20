package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class QueryListenerBasic {
    //start
    object logger : QueryListener {
        override fun beforeQuery(sql: String, name: String?) {
            println("Executing: $sql")
        }
        override fun afterQuery(event: QueryEvent) {
            println("${event.name()} completed in ${event.elapsed().toMillis()}ms")
        }
        override fun failedQuery(event: QueryEvent) {
            System.err.println("${event.name()} failed after ${event.elapsed().toMillis()}ms: ${event.error()?.message}")
        }
    }
    //stop
}
