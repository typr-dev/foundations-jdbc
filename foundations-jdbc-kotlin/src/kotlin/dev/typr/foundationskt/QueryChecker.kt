@file:Suppress("unused")
package dev.typr.kotlinfoundations

object QueryChecker {
    @JvmStatic
    fun create(transactor: Transactor): dev.typr.foundations.QueryChecker =
        dev.typr.foundations.QueryChecker { transactor.underlying }

    @JvmStatic
    fun checkRoutine(checker: dev.typr.foundations.QueryChecker, procedure: Procedure<*>) {
        checker.checkRoutine(procedure.javaProcedure)
    }
}
