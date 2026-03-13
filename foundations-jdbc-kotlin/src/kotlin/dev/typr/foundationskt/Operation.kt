@file:Suppress("unused")
package dev.typr.foundationskt

import java.sql.Connection
import java.time.Duration
import java.util.Optional

sealed class Operation<Out> : Analyzable {
    abstract val underlying: dev.typr.foundations.Operation<*>

    override val analyzable: dev.typr.foundations.Analyzable get() = underlying

    abstract fun run(conn: Connection): Out

    fun transact(transactor: Transactor): Out =
        transactor.execute(this)

    fun <B> map(f: (Out) -> B): Operation<B> =
        Mapped(this.underlying, this, f)

    @Suppress("UNCHECKED_CAST")
    fun <B> combine(other: Operation<B>): Operation<Pair<Out, B>> =
        Combine(dev.typr.foundations.Operation.Combine(underlying as dev.typr.foundations.Operation<Out>, other.underlying as dev.typr.foundations.Operation<B>), this, other)

    fun <B, R> combineWith(other: Operation<B>, combine: (Out, B) -> R): Operation<R> =
        combine(other).map { pair -> combine(pair.first, pair.second) }

    fun <B, C, R> combineWith(b: Operation<B>, c: Operation<C>, combine: (Out, B, C) -> R): Operation<R> =
        combine(b).combine(c).map { pair -> combine(pair.first.first, pair.first.second, pair.second) }

    fun <B, C, D, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, combine: (Out, B, C, D) -> R): Operation<R> =
        combine(b).combine(c).combine(d).map { pair -> combine(pair.first.first.first, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, combine: (Out, B, C, D, E) -> R): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).map { pair -> combine(pair.first.first.first.first, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, combine: (Out, B, C, D, E, F) -> R): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).map { pair -> combine(pair.first.first.first.first.first, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, combine: (Out, B, C, D, E, F, G) -> R): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).map { pair -> combine(pair.first.first.first.first.first.first, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, H, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, combine: (Out, B, C, D, E, F, G, H) -> R): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).combine(h).map { pair -> combine(pair.first.first.first.first.first.first.first, pair.first.first.first.first.first.first.second, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B, C, D, E, F, G, H, I, R> combineWith(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, i: Operation<I>, combine: (Out, B, C, D, E, F, G, H, I) -> R): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).combine(h).combine(i).map { pair -> combine(pair.first.first.first.first.first.first.first.first, pair.first.first.first.first.first.first.first.second, pair.first.first.first.first.first.first.second, pair.first.first.first.first.first.second, pair.first.first.first.first.second, pair.first.first.first.second, pair.first.first.second, pair.first.second, pair.second) }

    fun <B> thenIgnore(other: Operation<B>): Operation<Out> =
        combine(other).map { pair -> pair.first }

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
        override fun run(conn: Connection): Out = underlying.run(conn)
    }

    class Update(override val underlying: dev.typr.foundations.Operation.Update) : Operation<Int>() {
        override fun run(conn: Connection): Int = underlying.run(conn)
    }

    class UpdateReturning<Out>(override val underlying: dev.typr.foundations.Operation.UpdateReturning<Out>) : Operation<Out>() {
        override fun run(conn: Connection): Out = underlying.run(conn)
    }

    class UpdateReturningGeneratedKeys<Out>(override val underlying: dev.typr.foundations.Operation.UpdateReturningGeneratedKeys<Out>) : Operation<Out>() {
        override fun run(conn: Connection): Out = underlying.run(conn)
    }

    class UpdateMany<Row>(override val underlying: dev.typr.foundations.Operation.UpdateMany<Row>) : Operation<IntArray>() {
        override fun run(conn: Connection): IntArray = underlying.run(conn)
    }

    class UpdateManyReturning<Row>(override val underlying: dev.typr.foundations.Operation.UpdateManyReturning<Row>) : Operation<List<Row>>() {
        override fun run(conn: Connection): List<Row> = underlying.run(conn)
    }

    class UpdateReturningEach<Row>(override val underlying: dev.typr.foundations.Operation.UpdateReturningEach<Row>) : Operation<List<Row>>() {
        override fun run(conn: Connection): List<Row> = underlying.run(conn)
    }

    class UpdateManyTemplate<Row>(override val underlying: dev.typr.foundations.Operation.UpdateManyTemplate<Row>) : Operation<IntArray>() {
        override fun run(conn: Connection): IntArray = underlying.run(conn)
    }

    class StreamingCopy(override val underlying: dev.typr.foundations.Operation<Long>) : Operation<Long>() {
        override fun run(conn: Connection): Long = underlying.run(conn)
    }

    class Streaming<Row>(override val underlying: dev.typr.foundations.Operation.Streaming<Row>) : Operation<Cursor<Row>>() {
        override fun run(conn: Connection): Cursor<Row> = Cursor(underlying.run(conn))
    }

    class Mapped<A, B>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val source: Operation<A>,
        val f: (A) -> B
    ) : Operation<B>() {
        override fun run(conn: Connection): B = f(source.run(conn))
    }

    class Pure<T>(override val underlying: dev.typr.foundations.Operation.Pure<T>, val value: T) : Operation<T>() {
        override fun run(conn: Connection): T = value
    }

    class Combine<A, B>(
        override val underlying: dev.typr.foundations.Operation.Combine<A, B>,
        val first: Operation<A>,
        val second: Operation<B>
    ) : Operation<Pair<A, B>>() {
        override fun run(conn: Connection): Pair<A, B> =
            Pair(first.run(conn), second.run(conn))
    }

    class IfEmpty<T : Any>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val check: Operation<T?>,
        val fallback: Operation<T>
    ) : Operation<T>() {
        override fun run(conn: Connection): T =
            check.run(conn) ?: fallback.run(conn)
    }

    class Then<A, In, Out>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val source: Operation<A>,
        val extract: (A) -> In,
        val continuation: Template<In, Out>
    ) : Operation<Out>() {
        override fun run(conn: Connection): Out {
            val a = source.run(conn)
            val input = extract(a)
            return continuation.on(input).run(conn)
        }
    }

    class Configured<Out>(
        override val underlying: dev.typr.foundations.Operation<*>,
        val inner: Operation<Out>,
        val name: String?,
        val timeout: Duration?,
        val listener: dev.typr.foundations.QueryListener?
    ) : Operation<Out>() {
        override fun run(conn: Connection): Out = inner.run(conn)
    }

    companion object {
        fun <T> pure(value: T): Operation<T> =
            Pure(dev.typr.foundations.Operation.Pure(value), value)

        fun <T> sequence(operations: List<Operation<T>>): Operation<List<T>> {
            if (operations.isEmpty()) return pure(emptyList())
            var result: Operation<List<T>> = operations.first().map { listOf(it) }
            for (i in 1 until operations.size) {
                result = result.combine(operations[i]).map { pair ->
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
