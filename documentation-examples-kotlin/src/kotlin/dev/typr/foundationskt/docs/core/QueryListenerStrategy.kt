package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class QueryListenerStrategy {
    val logger: QueryListener = QueryListener.NOOP

    //start
    val strategy: Strategy =
        Transactor.defaultStrategy()
            .replaceListener(logger)
    //stop
}
