package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class Interpolatedsql {
    //start
    object debugListener : QueryListener {
        override fun beforeQuery(sql: String, name: String?) {}
        override fun afterQuery(event: QueryEvent) {
            println(event.interpolatedSql())
        }
        override fun failedQuery(event: QueryEvent) {
            System.err.println("Failed: ${event.interpolatedSql()}")
        }
    }
    //stop
}
