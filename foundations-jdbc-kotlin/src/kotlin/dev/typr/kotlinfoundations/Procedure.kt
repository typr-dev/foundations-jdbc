package dev.typr.kotlinfoundations

import java.sql.Connection
import java.sql.SQLException

/** Kotlin wrapper for dev.typr.foundations.Procedure with Unit instead of Void. */
class Procedure<Out> internal constructor(
    internal val javaProcedure: dev.typr.foundations.Procedure<*>,
    private val mapResult: (Any?) -> Out
) {

    fun call(vararg inValues: Any?): ProcedureOp<Out> {
        @Suppress("UNCHECKED_CAST")
        val javaOp = (javaProcedure as dev.typr.foundations.Procedure<Any?>)
            .call(*inValues)
        return ProcedureOp(javaOp, mapResult)
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        internal fun fromVoid(jp: dev.typr.foundations.Procedure<Void>): Procedure<Unit> =
            Procedure(jp) { }

        @Suppress("UNCHECKED_CAST")
        internal fun <Out> fromJava(jp: dev.typr.foundations.Procedure<Out>): Procedure<Out> =
            Procedure(jp) { it as Out }
    }
}

/** Operation returned by [Procedure.call] — wraps a Java Operation with result conversion. */
class ProcedureOp<Out> internal constructor(
    private val javaOp: dev.typr.foundations.Operation<Any?>,
    private val mapResult: (Any?) -> Out
) {

    @Throws(SQLException::class)
    fun run(conn: Connection): Out = mapResult(javaOp.run(conn))

    fun runUnchecked(conn: Connection): Out =
        try { run(conn) } catch (e: SQLException) { throw RuntimeException(e) }

    @Throws(SQLException::class)
    fun transact(transactor: dev.typr.foundations.Transactor): Out =
        mapResult(javaOp.transact(transactor))
}
