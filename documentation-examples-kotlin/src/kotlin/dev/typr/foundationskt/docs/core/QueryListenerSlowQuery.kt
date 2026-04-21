package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Duration

@Suppress("unused")
class QueryListenerSlowQuery {
    //start
    fun slowQueryDetector(threshold: Duration): QueryListener =
        object : QueryListener {
            override fun beforeQuery(sql: String, name: java.util.Optional<String>) {}
            override fun afterQuery(event: QueryEvent) {
                if (event.elapsed() > threshold) {
                    System.err.println("SLOW QUERY [${event.elapsed().toMillis()}ms]: ${event.interpolatedSql()}")
                }
            }
            override fun failedQuery(event: QueryEvent) {}
        }
    //stop
}
