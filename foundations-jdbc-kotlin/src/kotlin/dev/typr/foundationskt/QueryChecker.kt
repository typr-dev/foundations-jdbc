@file:Suppress("unused")
package dev.typr.foundationskt

class QueryChecker(val underlying: dev.typr.foundations.QueryChecker) {

    fun check(analyzable: Analyzable) {
        underlying.check(analyzable.analyzable)
    }

    fun analyzeAll(analyzables: List<Analyzable>): CheckReport =
        underlying.analyzeAll(analyzables.map { it.analyzable })

    fun analyzeAll(vararg analyzables: Analyzable): CheckReport =
        analyzeAll(analyzables.toList())

    fun checkAll(analyzables: List<Analyzable>): CheckReport =
        underlying.checkAll(analyzables.map { it.analyzable })

    fun checkAll(vararg analyzables: Analyzable): CheckReport =
        checkAll(analyzables.toList())

    fun checkRoutine(def: RoutineDef) {
        underlying.checkRoutine(def)
    }

    companion object {
        @JvmStatic
        fun create(transactor: Transactor): QueryChecker =
            create(transactor, 1)

        @JvmStatic
        fun create(transactor: Transactor, threads: Int): QueryChecker =
            QueryChecker(
                dev.typr.foundations.QueryChecker.create(transactor.underlying, threads)
            )
    }
}
