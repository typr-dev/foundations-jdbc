@file:Suppress("unused")
package dev.typr.kotlinfoundations

import dev.typr.foundations.And
import java.sql.Connection
import java.sql.SQLException
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
    fun <B> with(other: Operation<B>): Operation<And<Out, B>> =
        With(dev.typr.foundations.Operation.With(underlying as dev.typr.foundations.Operation<Out>, other.underlying as dev.typr.foundations.Operation<B>), this, other)

    fun <B, R> with(other: Operation<B>, combine: (Out, B) -> R): Operation<R> =
        with(other).map { and -> combine(and.left(), and.right()) }

    fun <B, C, R> with(b: Operation<B>, c: Operation<C>, combine: (Out, B, C) -> R): Operation<R> =
        with(b).with(c).map { and -> combine(and.left().left(), and.left().right(), and.right()) }

    fun <B, C, D, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, combine: (Out, B, C, D) -> R): Operation<R> =
        with(b).with(c).with(d).map { and -> combine(and.left().left().left(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B, C, D, E, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, combine: (Out, B, C, D, E) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).map { and -> combine(and.left().left().left().left(), and.left().left().left().right(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B, C, D, E, F, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, combine: (Out, B, C, D, E, F) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).map { and -> combine(and.left().left().left().left().left(), and.left().left().left().left().right(), and.left().left().left().right(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B, C, D, E, F, G, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, combine: (Out, B, C, D, E, F, G) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).map { and -> combine(and.left().left().left().left().left().left(), and.left().left().left().left().left().right(), and.left().left().left().left().right(), and.left().left().left().right(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B, C, D, E, F, G, H, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, combine: (Out, B, C, D, E, F, G, H) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).with(h).map { and -> combine(and.left().left().left().left().left().left().left(), and.left().left().left().left().left().left().right(), and.left().left().left().left().left().right(), and.left().left().left().left().right(), and.left().left().left().right(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B, C, D, E, F, G, H, I, R> with(b: Operation<B>, c: Operation<C>, d: Operation<D>, e: Operation<E>, f: Operation<F>, g: Operation<G>, h: Operation<H>, i: Operation<I>, combine: (Out, B, C, D, E, F, G, H, I) -> R): Operation<R> =
        with(b).with(c).with(d).with(e).with(f).with(g).with(h).with(i).map { and -> combine(and.left().left().left().left().left().left().left().left(), and.left().left().left().left().left().left().left().right(), and.left().left().left().left().left().left().right(), and.left().left().left().left().left().right(), and.left().left().left().left().right(), and.left().left().left().right(), and.left().left().right(), and.left().right(), and.right()) }

    fun <B> thenIgnore(other: Operation<B>): Operation<Out> =
        with(other).map { and -> and.left() }

    fun <B> then(template: SqlTemplate<Out, B>): Operation<B> {
        val javaTemplate = template.underlying
        @Suppress("UNCHECKED_CAST")
        val javaOp = dev.typr.foundations.Operation.Then(
            underlying as dev.typr.foundations.Operation<Out>,
            java.util.function.Function.identity(),
            javaTemplate as dev.typr.foundations.SqlTemplate<Out, B>
        )
        return Then(javaOp, this, { it }, template)
    }

    fun voided(): Operation<Unit> = map { }

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
    ) : Operation<And<A, B>>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): And<A, B> =
            And(first.runChecked(conn), second.runChecked(conn))
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
        val continuation: SqlTemplate<In, Out>
    ) : Operation<Out>() {
        @Throws(SQLException::class)
        override fun runChecked(conn: Connection): Out {
            val a = source.runChecked(conn)
            val input = extract(a)
            return continuation.on(input).runChecked(conn)
        }
    }

    companion object {
        fun <T> pure(value: T): Operation<T> =
            Pure(dev.typr.foundations.Operation.Pure(value), value)

        fun <T> sequence(operations: List<Operation<T>>): Operation<List<T>> {
            if (operations.isEmpty()) return pure(emptyList())
            var result: Operation<List<T>> = operations.first().map { listOf(it) }
            for (i in 1 until operations.size) {
                result = result.with(operations[i]).map { and ->
                    and.left() + listOf(and.right())
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
