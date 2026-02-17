package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StrategyMerge {
    val logger: QueryListener = QueryListener.NOOP

    //start
    val base: Strategy = Transactor.defaultStrategy()
    val withLogging: Strategy = base.merge(Strategy(
        { _ -> }, { _ -> }, { _, _ -> }, { _ -> }, logger))
    //stop
}
