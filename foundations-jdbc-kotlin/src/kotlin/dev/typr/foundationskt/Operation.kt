@file:Suppress("unused")
package dev.typr.foundationskt

import java.time.Duration

sealed class Operation<Out> : Analyzable {
    abstract val underlying: dev.typr.foundations.Operation<Out>

    override val analyzable: dev.typr.foundations.Analyzable get() = underlying

    fun run(conn: dev.typr.foundationskt.Connection): Out =
        conn.execute(this)

    fun transact(transactor: Transactor): Out =
        transactor.execute(this)

    open fun <B> map(f: (Out) -> B): Operation<B> =
        Mapped(this, f)

    open fun voided(): Operation<Unit> = map { }

    fun <B> combine(other: Operation<B>): Operation<Pair<Out, B>> =
        CombinePair(underlying.combine(other.underlying))

    fun <B, R> combineWith(other: Operation<B>, combine: (Out, B) -> R): Operation<R> =
        combine(other).map { (a, b) -> combine(a, b) }

    fun <B, C, R> combineWith(
        b: Operation<B>, c: Operation<C>,
        combine: (Out, B, C) -> R
    ): Operation<R> =
        combine(b).combine(c).map { (ab, c2) -> combine(ab.first, ab.second, c2) }

    fun <B, C, D, R> combineWith(
        b: Operation<B>, c: Operation<C>, d: Operation<D>,
        combine: (Out, B, C, D) -> R
    ): Operation<R> =
        combine(b).combine(c).combine(d).map { (abc, d2) ->
            combine(abc.first.first, abc.first.second, abc.second, d2)
        }

    fun <B, C, D, E, R> combineWith(
        b: Operation<B>, c: Operation<C>, d: Operation<D>,
        e: Operation<E>,
        combine: (Out, B, C, D, E) -> R
    ): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).map { (abcd, e2) ->
            combine(abcd.first.first.first, abcd.first.first.second, abcd.first.second, abcd.second, e2)
        }

    fun <B, C, D, E, F, R> combineWith(
        b: Operation<B>, c: Operation<C>, d: Operation<D>,
        e: Operation<E>, f: Operation<F>,
        combine: (Out, B, C, D, E, F) -> R
    ): Operation<R> =
        combine(b).combine(c).combine(d).combine(e).combine(f).map { (abcde, f2) ->
            combine(abcde.first.first.first.first, abcde.first.first.first.second, abcde.first.first.second, abcde.first.second, abcde.second, f2)
        }

    fun <B> productL(other: Operation<B>): Operation<Out> =
        combine(other).map { (a, _) -> a }

    open fun <B> then(template: Template<Out, B>): Operation<B> =
        JavaWrapped(underlying.then(template.underlying))

    open fun named(name: String): Operation<Out> =
        JavaWrapped(underlying.named(name))

    open fun timeout(timeout: Duration): Operation<Out> =
        JavaWrapped(underlying.timeout(timeout))

    open fun withListener(listener: QueryListener): Operation<Out> =
        JavaWrapped(underlying.withListener(listener))

    // ========== Write operation types ==========

    class Update(private val java: dev.typr.foundations.Operation.Update) : Operation<Int>() {
        override val underlying: dev.typr.foundations.Operation<Int> get() = java
    }

    class Execute(private val java: dev.typr.foundations.Operation.Execute) : Operation<Unit>() {
        override val underlying: dev.typr.foundations.Operation<Unit>
            get() = java.map<Unit> { }
    }

    class UpdateReturning<Out>(private val java: dev.typr.foundations.Operation.UpdateReturning<Out>) : Operation<Out>() {
        override val underlying: dev.typr.foundations.Operation<Out> get() = java
    }

    class UpdateReturningGeneratedKeys<Out>(private val java: dev.typr.foundations.Operation.UpdateReturningGeneratedKeys<Out>) : Operation<Out>() {
        override val underlying: dev.typr.foundations.Operation<Out> get() = java
    }

    class UpdateMany<Row>(private val java: dev.typr.foundations.Operation.UpdateMany<Row>) : Operation<IntArray?>() {
        override val underlying: dev.typr.foundations.Operation<IntArray?>
            get() = java.map { it.orElse(null) }
    }

    class UpdateManyReturning<Row>(private val java: dev.typr.foundations.Operation.UpdateManyReturning<Row>) : Operation<List<Row>>() {
        override val underlying: dev.typr.foundations.Operation<List<Row>>
            get() = java.map { it.toList() }
    }

    class UpdateReturningEach<Row>(private val java: dev.typr.foundations.Operation.UpdateReturningEach<Row>) : Operation<List<Row>>() {
        override val underlying: dev.typr.foundations.Operation<List<Row>>
            get() = java.map { it.toList() }
    }

    class UpdateManyTemplate<Row>(private val java: dev.typr.foundations.Operation.UpdateManyTemplate<Row>) : Operation<IntArray?>() {
        override val underlying: dev.typr.foundations.Operation<IntArray?>
            get() = java.map { it.orElse(null) }
    }

    class StreamingCopy<Row>(private val java: dev.typr.foundations.Operation.StreamingCopy<Row>) : Operation<Long>() {
        override val underlying: dev.typr.foundations.Operation<Long>
            get() = java.map { it }
    }

    // ========== Internal structural types ==========

    internal class Mapped<A, Out>(
        private val source: Operation<A>,
        private val f: (A) -> Out
    ) : Operation<Out>() {
        override val underlying: dev.typr.foundations.Operation<Out>
            get() = source.underlying.map { a -> f(a) }
    }

    internal class CombinePair<A, B>(
        private val java: dev.typr.foundations.Operation<dev.typr.foundations.Tuple.Tuple2<A, B>>
    ) : Operation<Pair<A, B>>() {
        override val underlying: dev.typr.foundations.Operation<Pair<A, B>>
            get() = java.map { t -> Pair(t._1(), t._2()) }
    }

    internal class ThenOp<A, Out>(
        private val source: Operation<A>,
        private val continuation: Template<A, Out>
    ) : Operation<Out>() {
        override val underlying: dev.typr.foundations.Operation<Out>
            get() = dev.typr.foundations.Operation.createThen(source.underlying, continuation.underlying)
    }

    internal class JavaWrapped<Out>(
        override val underlying: dev.typr.foundations.Operation<Out>
    ) : Operation<Out>()

    companion object {
        fun <T> sequence(operations: List<Operation<T>>): Operation<List<T>> {
            if (operations.isEmpty()) return OperationRead.pure(emptyList())
            var result: Operation<List<T>> = operations.first().map { listOf(it) }
            for (i in 1 until operations.size) {
                result = result.combine(operations[i]).map { (list, item) -> list + item }
            }
            return result
        }

        fun allOf(vararg operations: Operation<*>): Operation<Unit> {
            if (operations.isEmpty()) return OperationRead.pure(Unit)
            var result: Operation<Unit> = operations[0].voided()
            for (i in 1 until operations.size) {
                result = result.productL(operations[i])
            }
            return result
        }

        fun <T : Any> ifEmpty(check: Operation<T?>, fallback: Operation<T>): Operation<T> {
            val javaCheck = check.underlying.map { java.util.Optional.ofNullable(it) }
            val javaFallback = fallback.underlying
            return JavaWrapped(dev.typr.foundations.Operation.ifEmpty(javaCheck, javaFallback))
        }
    }
}
