package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StrategyOverride {
    lateinit var tx: Transactor
    val logger: QueryListener = QueryListener.NOOP

    //start
    val txWithLogging: Transactor = tx.mergeListener(logger)
    //stop
}
