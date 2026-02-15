package dev.typr.kotlinfoundations

import dev.typr.foundations.And
import java.util.Optional

class Bijection<A, B>(val underlying: dev.typr.foundations.Bijection<A, B>) {
    fun to(value: A): B = underlying.underlying(value)
    fun from(value: B): A = underlying.from(value)
    fun inverse(): Bijection<B, A> = Bijection(underlying.inverse())

    companion object {
        internal fun <T : Any> optionalToNullable(): dev.typr.foundations.Bijection<Optional<T>, T?> =
            dev.typr.foundations.Bijection.of(
                { opt: Optional<T> -> opt.orElse(null) },
                { nullable: T? -> Optional.ofNullable(nullable) }
            )

        internal fun <T : Any> nullableToOptional(): dev.typr.foundations.Bijection<T?, Optional<T>> =
            optionalToNullable<T>().inverse()

        internal fun <A, B : Any> leftJoinToNullable(): dev.typr.foundations.Bijection<And<A, Optional<B>>, And<A, B?>> =
            dev.typr.foundations.Bijection.of(
                { and: And<A, Optional<B>> -> And(and.left(), and.right().orElse(null)) },
                { and: And<A, B?> -> And(and.left(), Optional.ofNullable(and.right())) }
            )

        internal fun <A : Any, B> rightJoinToNullable(): dev.typr.foundations.Bijection<And<Optional<A>, B>, And<A?, B>> =
            dev.typr.foundations.Bijection.of(
                { and: And<Optional<A>, B> -> And(and.left().orElse(null), and.right()) },
                { and: And<A?, B> -> And(Optional.ofNullable(and.left()), and.right()) }
            )

        internal fun <A : Any, B : Any> fullJoinToNullable(): dev.typr.foundations.Bijection<And<Optional<A>, Optional<B>>, And<A?, B?>> =
            dev.typr.foundations.Bijection.of(
                { and: And<Optional<A>, Optional<B>> -> And(and.left().orElse(null), and.right().orElse(null)) },
                { and: And<A?, B?> -> And(Optional.ofNullable(and.left()), Optional.ofNullable(and.right())) }
            )

        @Suppress("UNCHECKED_CAST")
        internal fun <T> optionalToNullableUnchecked(): dev.typr.foundations.Bijection<Optional<T>, T?> =
            optionalToNullable<Any>() as dev.typr.foundations.Bijection<Optional<T>, T?>
    }
}
