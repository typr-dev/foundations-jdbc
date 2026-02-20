package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StrategyOverride {
    val tx: Transactor = null!! // placeholder
    val logger: QueryListener = QueryListener.NOOP

    //start
    val txWithLogging: Transactor =
        tx.withStrategy(Strategy.empty().withListener(logger))
    //stop
}
