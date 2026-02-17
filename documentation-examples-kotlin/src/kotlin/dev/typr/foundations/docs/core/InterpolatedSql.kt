package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class InterpolatedSql {
    //start
    val debugListener = object : QueryListener {
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
