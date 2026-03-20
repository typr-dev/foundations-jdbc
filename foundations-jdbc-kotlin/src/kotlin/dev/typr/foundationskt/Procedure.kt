package dev.typr.foundationskt

import java.sql.Connection

/** Kotlin wrapper for dev.typr.foundations.Procedure with Unit instead of Void. */
class Procedure<Out> internal constructor(
    @JvmField val javaProcedure: dev.typr.foundations.Procedure<*>,
    private val doCall: (Array<out Any?>) -> ProcedureOp<Out>
) {

    fun call(vararg inValues: Any?): ProcedureOp<Out> = doCall(inValues)

    companion object {
        internal fun fromVoid(jp: dev.typr.foundations.Procedure<Void>): Procedure<Unit> =
            Procedure(jp) { args -> ProcedureOp.mapped(jp.call(*args)) { _ -> } }

        internal fun <Out> fromJava(jp: dev.typr.foundations.Procedure<Out>): Procedure<Out> =
            Procedure(jp) { args -> ProcedureOp.direct(jp.call(*args)) }

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

/** Operation returned by [Procedure.call] -- wraps a Java Operation with result conversion. */
class ProcedureOp<Out> internal constructor(
    private val runImpl: (Connection) -> Out,
    private val transactImpl: (dev.typr.foundations.Transactor) -> Out
) {

    fun run(conn: Connection): Out = runImpl(conn)

    fun transact(transactor: Transactor): Out =
        transactImpl(transactor.underlying)

    internal companion object {
        fun <T> direct(javaOp: dev.typr.foundations.Operation<T>): ProcedureOp<T> =
            ProcedureOp({ conn -> javaOp.run(conn) }, { tx -> javaOp.transact(tx) })

        fun <T, R> mapped(javaOp: dev.typr.foundations.Operation<T>, f: (T) -> R): ProcedureOp<R> =
            ProcedureOp({ conn -> f(javaOp.run(conn)) }, { tx -> f(javaOp.transact(tx)) })
    }
}
