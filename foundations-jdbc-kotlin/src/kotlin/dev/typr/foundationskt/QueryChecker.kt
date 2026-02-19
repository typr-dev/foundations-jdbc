@file:Suppress("unused")
package dev.typr.foundationskt

object QueryChecker {
    @JvmStatic
    fun create(transactor: Transactor): dev.typr.foundations.QueryChecker =
        dev.typr.foundations.QueryChecker { transactor.underlying }

    @JvmStatic
    fun checkRoutine(checker: dev.typr.foundations.QueryChecker, procedure: Procedure<*>) {
        checker.checkRoutine(procedure.javaProcedure)
    }
}

fun dev.typr.foundations.QueryChecker.check(op: Operation<*>) = check(op.underlying)
fun dev.typr.foundations.QueryChecker.check(name: String, op: Operation<*>) = check(name, op.underlying)
fun dev.typr.foundations.QueryChecker.check(template: SqlTemplate<*, *>) = check(template.underlying)
fun dev.typr.foundations.QueryChecker.check(name: String, template: SqlTemplate<*, *>) = check(name, template.underlying)
