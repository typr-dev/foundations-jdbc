package dev.typr.kotlinfoundations

import java.sql.Connection
import java.sql.SQLException

/** Kotlin wrapper for dev.typr.foundations.Procedure with Unit instead of Void. */
class Procedure<Out> internal constructor(
    @JvmField val javaProcedure: dev.typr.foundations.Procedure<*>,
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

        fun buildVoid(name: String, params: List<dev.typr.foundations.ParamDef>): Procedure<Unit> =
            fromVoid(dev.typr.foundations.Procedure.buildVoid(name, params))

        fun <R> buildFunction(name: String, inParams: List<dev.typr.foundations.ParamDef>, returnType: DbType<R>): Procedure<R> =
            fromJava(dev.typr.foundations.Procedure.buildFunction(name, inParams, returnType.underlying))

        fun <O> buildSingleOut(name: String, params: List<dev.typr.foundations.ParamDef>): Procedure<O> =
            fromJava(dev.typr.foundations.Procedure.buildSingleOut(name, params))

        fun <O> buildMultiOut(name: String, params: List<dev.typr.foundations.ParamDef>, assembler: java.util.function.Function<Array<Any?>, O>): Procedure<O> =
            fromJava(dev.typr.foundations.Procedure.buildMultiOut(name, params, assembler))
    }
}

/** Operation returned by [Procedure.call] — wraps a Java Operation with result conversion. */
class ProcedureOp<Out> internal constructor(
    private val javaOp: dev.typr.foundations.Operation<Any?>,
    private val mapResult: (Any?) -> Out
) {

    @Throws(SQLException::class)
    fun runChecked(conn: Connection): Out = mapResult(javaOp.runChecked(conn))

    fun run(conn: Connection): Out =
        try { runChecked(conn) } catch (e: SQLException) { throw RuntimeException(e) }

    @Throws(SQLException::class)
    fun transact(transactor: Transactor): Out =
        mapResult(javaOp.transact(transactor.underlying))
}
