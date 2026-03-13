package dev.typr.foundationskt

import dev.typr.foundations.Tuple
import java.util.Optional

class Bijection<A, B>(val underlying: dev.typr.foundations.Bijection<A, B>) {
    fun to(value: A): B = underlying.underlying(value)
    fun from(value: B): A = underlying.from(value)
    fun inverse(): Bijection<B, A> = Bijection(underlying.inverse())

    companion object {
        @Suppress("UNCHECKED_CAST")
        internal fun <T> optionalToNullable(): dev.typr.foundations.Bijection<Optional<T>, T?> =
            dev.typr.foundations.Bijection.of(
                { opt: Optional<T> -> opt.orElse(null) },
                { nullable: T? -> Optional.ofNullable(nullable) as Optional<T> }
            )

        internal fun <A, B> andToPair(): dev.typr.foundations.Bijection<Tuple.Tuple2<A, B>, Pair<A, B>> =
            dev.typr.foundations.Bijection.of(
                { t: Tuple.Tuple2<A, B> -> Pair(t._1(), t._2()) },
                { pair: Pair<A, B> -> Tuple.of(pair.first, pair.second) }
            )

        internal fun <A, B : Any> leftJoinToNullable(): dev.typr.foundations.Bijection<Tuple.Tuple2<A, Optional<B>>, Pair<A, B?>> =
            dev.typr.foundations.Bijection.of(
                { t: Tuple.Tuple2<A, Optional<B>> -> Pair(t._1(), t._2().orElse(null)) },
                { pair: Pair<A, B?> -> Tuple.of(pair.first, Optional.ofNullable(pair.second)) }
            )

        internal fun <A : Any, B> rightJoinToNullable(): dev.typr.foundations.Bijection<Tuple.Tuple2<Optional<A>, B>, Pair<A?, B>> =
            dev.typr.foundations.Bijection.of(
                { t: Tuple.Tuple2<Optional<A>, B> -> Pair(t._1().orElse(null), t._2()) },
                { pair: Pair<A?, B> -> Tuple.of(Optional.ofNullable(pair.first), pair.second) }
            )

        internal fun <A : Any, B : Any> fullJoinToNullable(): dev.typr.foundations.Bijection<Tuple.Tuple2<Optional<A>, Optional<B>>, Pair<A?, B?>> =
            dev.typr.foundations.Bijection.of(
                { t: Tuple.Tuple2<Optional<A>, Optional<B>> -> Pair(t._1().orElse(null), t._2().orElse(null)) },
                { pair: Pair<A?, B?> -> Tuple.of(Optional.ofNullable(pair.first), Optional.ofNullable(pair.second)) }
            )
    }
}
