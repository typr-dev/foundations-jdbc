@file:Suppress("unused")
package dev.typr.foundationskt

import java.time.Duration

sealed class OperationRead<Out> : Operation<Out>() {

    abstract override val underlying: dev.typr.foundations.OperationRead<Out>

    fun run(conn: dev.typr.foundationskt.ConnectionRead): Out =
        conn.execute(this)

    fun transactRead(transactor: Transactor): Out =
        transactor.execute(this)

    override fun <B> map(f: (Out) -> B): OperationRead<B> =
        Mapped(this, f)

    override fun voided(): OperationRead<Unit> = map { }

    fun <B> combine(other: OperationRead<B>): OperationRead<Pair<Out, B>> =
        CombineRead(this, other)

    fun <B, R> combineWith(other: OperationRead<B>, combine: (Out, B) -> R): OperationRead<R> =
        combine(other).map { (a, b) -> combine(a, b) }

    fun <B, C, R> combineWith(
        b: OperationRead<B>, c: OperationRead<C>,
        combine: (Out, B, C) -> R
    ): OperationRead<R> =
        combine(b).combine(c).map { (ab, c2) -> combine(ab.first, ab.second, c2) }

    fun <B, C, D, R> combineWith(
        b: OperationRead<B>, c: OperationRead<C>, d: OperationRead<D>,
        combine: (Out, B, C, D) -> R
    ): OperationRead<R> =
        combine(b).combine(c).combine(d).map { (abc, d2) ->
            combine(abc.first.first, abc.first.second, abc.second, d2)
        }

    fun <B, C, D, E, R> combineWith(
        b: OperationRead<B>, c: OperationRead<C>, d: OperationRead<D>,
        e: OperationRead<E>,
        combine: (Out, B, C, D, E) -> R
    ): OperationRead<R> =
        combine(b).combine(c).combine(d).combine(e).map { (abcd, e2) ->
            combine(abcd.first.first.first, abcd.first.first.second, abcd.first.second, abcd.second, e2)
        }

    fun <B, C, D, E, F, R> combineWith(
        b: OperationRead<B>, c: OperationRead<C>, d: OperationRead<D>,
        e: OperationRead<E>, f: OperationRead<F>,
        combine: (Out, B, C, D, E, F) -> R
    ): OperationRead<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).map { (abcde, f2) ->
            combine(abcde.first.first.first.first, abcde.first.first.first.second, abcde.first.first.second, abcde.first.second, abcde.second, f2)
        }

    fun <B> productL(other: OperationRead<B>): OperationRead<Out> =
        combine(other).map { (a, _) -> a }

    fun <B> then(template: TemplateRead<Out, B>): OperationRead<B> =
        ThenRead(this, template)

    override fun named(name: String): OperationRead<Out> =
        JavaWrapped(underlying.named(name))

    override fun timeout(timeout: Duration): OperationRead<Out> =
        JavaWrapped(underlying.timeout(timeout))

    override fun withListener(listener: QueryListener): OperationRead<Out> =
        JavaWrapped(underlying.withListener(listener))

    // ========== Read operation types ==========

    class Query<Out>(override val underlying: dev.typr.foundations.OperationRead.Query<Out>) : OperationRead<Out>()

    class Streaming<Row>(private val java: dev.typr.foundations.OperationRead.Streaming<Row>) : OperationRead<Cursor<Row>>() {
        override val underlying: dev.typr.foundations.OperationRead<Cursor<Row>>
            get() = java.map { Cursor(it) }
    }

    class Pure<T>(override val underlying: dev.typr.foundations.OperationRead.Pure<T>) : OperationRead<T>()

    // ========== Internal structural types ==========

    internal class Mapped<A, B>(
        private val source: OperationRead<A>,
        private val f: (A) -> B
    ) : OperationRead<B>() {
        override val underlying: dev.typr.foundations.OperationRead<B>
            get() = source.underlying.map { a -> f(a) }
    }

    internal class CombineRead<A, B>(
        private val first: OperationRead<A>,
        private val second: OperationRead<B>
    ) : OperationRead<Pair<A, B>>() {
        override val underlying: dev.typr.foundations.OperationRead<Pair<A, B>>
            get() = first.underlying.combine(second.underlying).map { t -> Pair(t._1(), t._2()) }
    }

    internal class ThenRead<A, Out>(
        private val source: OperationRead<A>,
        private val continuation: TemplateRead<A, Out>
    ) : OperationRead<Out>() {
        override val underlying: dev.typr.foundations.OperationRead<Out>
            get() = source.underlying.then(continuation.underlying)
    }

    internal class IfEmptyRead<T : Any>(
        private val check: OperationRead<T?>,
        private val fallback: OperationRead<T>
    ) : OperationRead<T>() {
        override val underlying: dev.typr.foundations.OperationRead<T>
            get() = dev.typr.foundations.OperationRead.ifEmpty(
                check.underlying.map { java.util.Optional.ofNullable(it) },
                fallback.underlying
            )
    }

    internal class JavaWrapped<Out>(
        override val underlying: dev.typr.foundations.OperationRead<Out>
    ) : OperationRead<Out>()

    companion object {
        fun <T> pure(value: T): OperationRead<T> =
            Pure(dev.typr.foundations.OperationRead.Pure(value))

        fun <T> sequence(operations: List<OperationRead<T>>): OperationRead<List<T>> {
            if (operations.isEmpty()) return pure(emptyList())
            var result: OperationRead<List<T>> = operations.first().map { listOf(it) }
            for (i in 1 until operations.size) {
                result = result.combine(operations[i]).map { (list, item) -> list + item }
            }
            return result
        }

        fun allOf(vararg operations: OperationRead<*>): OperationRead<Unit> {
            if (operations.isEmpty()) return pure(Unit)
            var result: OperationRead<Unit> = operations[0].voided()
            for (i in 1 until operations.size) {
                result = result.productL(operations[i])
            }
            return result
        }

        fun <T : Any> ifEmpty(check: OperationRead<T?>, fallback: OperationRead<T>): OperationRead<T> =
            IfEmptyRead(check, fallback)
    }
}
