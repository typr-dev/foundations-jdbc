package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class QueryListenerMetrics {
    //start
    fun metricsListener(/* registry: MeterRegistry */): QueryListener =
        object : QueryListener {
            override fun beforeQuery(sql: String, name: java.util.Optional<String>) {}
            override fun afterQuery(event: QueryEvent) {
                // registry.timer("db.query",
                //     "name", event.name(),
                //     "outcome", "success"
                // ).record(event.elapsed())
            }
            override fun failedQuery(event: QueryEvent) {
                // registry.timer("db.query",
                //     "name", event.name(),
                //     "outcome", "failure"
                // ).record(event.elapsed())
            }
        }
    //stop
}
