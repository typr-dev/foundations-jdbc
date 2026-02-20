@file:Suppress("unused")
package dev.typr.foundationskt

import java.sql.Connection
import java.sql.SQLException
import java.time.Duration
import java.util.Optional

sealed class Operation<Out> {
    abstract val underlying: dev.typr.foundations.Operation<*>

    @Throws(SQLException::class)
    abstract fun runChecked(conn: Connection): Out

    fun run(conn: Connection): Out =
        try { runChecked(conn) } catch (e: SQLException) { throw RuntimeException(e) }

    @Throws(SQLException::class)
    fun transact(transactor: Transactor): Out =
        transactor.execute(this)

    fun <B> map(f: (Out) -> B): Operation<B> =
        Mapped(this.underlying, this, f)

    @Suppress("UNCHECKED_CAST")
    fun <B> with(other: Operation<B>): Operation<Pair<Out, B>> =
        With(dev.typr.foundations.Operation.With(underlying as dev.typr.foundations.Operation<Out>, other.underlying as dev.typr.foundations.Operation<B>), this, other)

    fun <B, R> with(other: Operation<B>, combine: (Out, B) -> R): Operation<R> =
        with(other).map { pair -> combine(pair.first, pair.second) }

    fun <B, C, R> with(b: Operation<B>, c: Operation<C>, combine: (Out, B, C) -> R): Operation<R> =
        with(b).with(c).map { pair -> combine(pair.first.first, pair.first.second, pair.second) }

    fun <B, C, D, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, combine: (Out, B, C, D) -> R): Operation<R> =
        with(b).with(c).with(d).map { pair -> combine(pair.first.first.first, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, combine: (Out, B, C, D, E) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).map { pair -> combine(pair.first.first.first.first, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, combine: (Out, B, C, D, E, F) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).map { pair -> combine(pair.first.first.first.first.first, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, combine: (Out, B, C, D, E, F, G) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).map { pair -> combine(pair.first.first.first.first.first.first, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, H, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, combine: (Out, B, C, D, E, F, G, H) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).with(h).map { pair -> combine(pair.first.first.first.first.first.first.first, pair.first.first.first.first.first.first.second, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, H, I, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, i: Operation<I>, combine: (Out, B, C, D, E, F, G, H, I) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).with(h).with(i).map { pair -> combine(pair.first.first.first.first.first.first.first.first, pair.first.first.first.first.first.first.first.second, pair.first.first.first.first.first.first.second, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B> thenIgnore(other: Operation<B>): Operation<Out> =
        with(other).map { pair -> pair.first }

    fun <B> then(template: Template<Out, B>): Operation<B> {
        val javaTemplate = template.underlying
        @Suppress("UNCHECKED_CAST")
        val javaOp = dev.typr.foundations.Operation.Then(
            underlying as dev.typr.foundations.Operation<Out>,
            java.util.function.Function.identity(),
            javaTemplate as dev.typr.foundations.Template<Out, B>
        )
        return Then(javaOp, this, { it }, template)
    }

    fun voided(): Operation<Unit> = map { }

    fun named(name: String): Operation<Out> =
        Configured(dev.typr.foundations.Operation.Configured(underlying, name, null, null), this, name, null, null)

    fun timeout(timeout: Duration): Operation<Out> =
        Configured(dev.typr.foundations.Operation.Configured(underlying, null, timeout, null), this, null, timeout, null)

    fun withListener(listener: dev.typr.foundations.QueryListener): Operation<Out> =
        Configured(dev.typr.foundations.Operation.Configured(underlying, null, null, listener), this, null, null, listener)

    class Query<Out>(override val underlying: dev.typr.foundations.Operation.Query<Out>) : Operation<Out>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Out = underlying.runChecked(conn)
    }

    class Update(override val underlying: dev.typr.foundations.Operation.Update) : Operation<Int>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Int = underlying.runChecked(conn)
    }

    class UpdateReturning<Out>(override val underlying: dev.typr.foundations.Operation.UpdateReturning<Out>) : Operation<Out>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Out = underlying.runChecked(conn)
    }

    class UpdateMany<Row>(override val underlying: dev.typr.foundations.Operation.UpdateMany<Row>) : Operation<IntArray>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): IntArray = underlying.runChecked(conn)
    }

    class UpdateManyReturning<Row>(override val underlying: dev.typr.foundations.Operation.UpdateManyReturning<Row>) : Operation<List<Row>>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): List<Row> = underlying.runChecked(conn)
    }

    class UpdateReturningEach<Row>(override val underlying: dev.typr.foundations.Operation.UpdateReturningEach<Row>) : Operation<List<Row>>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): List<Row> = underlying.runChecked(conn)
    }

    class UpdateManyTemplate<Row>(override val underlying: dev.typr.foundations.Operation.UpdateManyTemplate<Row>) : Operation<IntArray>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): IntArray = underlying.runChecked(conn)
    }

    class StreamingCopy(override val underlying: dev.typr.foundations.Operation<Long>) : Operation<Long>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Long = underlying.runChecked(conn)
    }

    class Mapped<A, B>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val source: Operation<A>,
        val f: (A) -> B
    ) : Operation<B>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): B = f(source.runChecked(conn))
    }

    class Pure<T>(override val underlying: dev.typr.foundations.Operation.Pure<T>, val value: T) : Operation<T>() {
        override fun runChecked(conn: Connection): T = value
    }

    class With<A, B>(
        override val underlying: dev.typr.foundations.Operation.With<A, B>,
        val first: Operation<A>,
        val second: Operation<B>
    ) : Operation<Pair<A, B>>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Pair<A, B> =
            Pair(first.runChecked(conn), second.runChecked(conn))
    }

    class IfEmpty<T : Any>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val check: Operation<T?>,
        val fallback: Operation<T>
    ) : Operation<T>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): T =
            check.runChecked(conn) ?: fallback.runChecked(conn)
    }

    class Then<A, In, Out>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val source: Operation<A>,
        val extract: (A) -> In,
        val continuation: Template<In, Out>
    ) : Operation<Out>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Out {
            val a = source.runChecked(conn)
            val input = extract(a)
            return continuation.on(input).runChecked(conn)
        }
    }

    class Configured<Out>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val inner: Operation<Out>,
        val name: String?,
        val timeout: Duration?,
        val listener: dev.typr.foundations.QueryListener?
    ) : Operation<Out>() {
        @Suppress("UNCHECKED_CAST")
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Out = underlying.runChecked(conn) as Out
    }

    companion object {
        fun <T> pure(value: T): Operation<T> =
            Pure(dev.typr.foundations.Operation.Pure(value), value)

        fun <T> sequence(operations: List<Operation<T>>): Operation<List<T>> {
            if (operations.isEmpty()) return pure(emptyList())
            var result: Operation<List<T>> = operations.first().map { listOf(it) }
            for (i in 1 until operations.size) {
                result = result.with(operations[i]).map { pair ->
                    pair.first + listOf(pair.second)
                }
            }
            return result
        }

        fun allOf(vararg operations: Operation<*>): Operation<Unit> {
            if (operations.isEmpty()) return pure(Unit)
            var result: Operation<Unit> = operations[0].voided()
            for (i in 1 until operations.size) {
                result = result.thenIgnore(operations[i])
            }
            return result
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> ifEmpty(check: Operation<T?>, fallback: Operation<T>): Operation<T> {
            val javaCheck = check.underlying as dev.typr.foundations.Operation<Optional<T>>
            val javaFallback = fallback.underlying as dev.typr.foundations.Operation<T>
            val javaOp = dev.typr.foundations.Operation.IfEmpty(javaCheck, javaFallback)
            return IfEmpty(javaOp, check, fallback)
        }
    }
}
