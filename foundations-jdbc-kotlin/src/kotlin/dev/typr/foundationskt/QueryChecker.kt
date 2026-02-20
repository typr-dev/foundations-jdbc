@file:Suppress("unused")
package dev.typr.foundationskt

class QueryChecker(val underlying: dev.typr.foundations.QueryChecker) {

    fun check(analyzable: Analyzable) {
        underlying.check(analyzable.analyzable)
    }

    fun checkAll(analyzables: List<Analyzable>) {
        underlying.checkAll(analyzables.map { it.analyzable })
    }

    fun checkAll(vararg analyzables: Analyzable) {
        checkAll(analyzables.toList())
    }

    fun checkRoutine(procedure: Procedure<*>) {
        underlying.checkRoutine(procedure.javaProcedure)
    }

    companion object {
        @JvmStatic
        fun create(transactor: Transactor): QueryChecker =
            QueryChecker(dev.typr.foundations.QueryChecker { transactor.underlying })
    }
}
