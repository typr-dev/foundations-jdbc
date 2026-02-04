package dev.typr.kotlinfoundations

import dev.typr.foundations.DbType
import dev.typr.foundations.dsl.Bijection
import java.util.Optional

/**
 * Generic extension methods for converting between Java Optional and Kotlin nullable types.
 * These work at the VALUE level, not the type parameter level.
 */

// ================================
// DbType<T>.nullable → DbType<T?>
// ================================

/**
 * Extension to add `.nullable` method to any DbType, converting Optional to T?.
 * Usage: val nullableTimestamp: DbType<Instant?> = PgTypes.timestamptz.nullable
 */
val <T : Any> DbType<T>.nullable: DbType<T?>
    get() = this.opt().to(optionalToNullable())

// ================================
// Optional<T> → T?
// ================================

/**
 * Convert Optional<T> to T? (nullable).
 * Usage: val user: User? = optionalUser.orNull()
 */
fun <T : Any> Optional<T>.orNull(): T? = this.orElse(null)

// ================================
// T? → Optional<T>
// ================================

/**
 * Convert T? (nullable) to Optional<T>.
 * Usage: val optional: Optional<User> = nullableUser.toOptional()
 */
fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

// ================================
// Bijection: Optional<T> ↔ T?
// ================================

/**
 * Bijection between Java Optional<T> and Kotlin nullable T?.
 * Used for type-safe phantom type conversion in PgTypename/MariaTypename.
 *
 * Usage:
 *   val typename: PgTypename<String?> = pgType.opt().typename().to(optionalToNullable())
 */
fun <T : Any> optionalToNullable(): Bijection<Optional<T>, T?> {
    return Bijection.of(
        { opt: Optional<T> -> opt.orElse(null) },
        { nullable: T? -> Optional.ofNullable(nullable) }
    )
}

/**
 * Bijection between Kotlin nullable T? and Java Optional<T>.
 * Inverse of optionalToNullable().
 */
fun <T : Any> nullableToOptional(): Bijection<T?, Optional<T>> = optionalToNullable<T>().inverse()

// ================================
// And<A, Optional<B>> → And<A, B?>
// ================================

/**
 * Bijection for left join results, converting And<A, Optional<B>> to And<A, B?>.
 * Usage: rowParser.leftJoined(other).to(leftJoinToNullable())
 */
fun <A, B : Any> leftJoinToNullable(): Bijection<dev.typr.foundations.And<A, Optional<B>>, dev.typr.foundations.And<A, B?>> {
    return Bijection.of(
        { and: dev.typr.foundations.And<A, Optional<B>> ->
            dev.typr.foundations.And(and.left(), and.right().orElse(null))
        },
        { and: dev.typr.foundations.And<A, B?> ->
            dev.typr.foundations.And(and.left(), Optional.ofNullable(and.right()))
        }
    )
}

/**
 * Bijection for right join results, converting And<Optional<A>, B> to And<A?, B>.
 * Usage: rowParser.rightJoined(other).to(rightJoinToNullable())
 */
fun <A : Any, B> rightJoinToNullable(): Bijection<dev.typr.foundations.And<Optional<A>, B>, dev.typr.foundations.And<A?, B>> {
    return Bijection.of(
        { and: dev.typr.foundations.And<Optional<A>, B> ->
            dev.typr.foundations.And(and.left().orElse(null), and.right())
        },
        { and: dev.typr.foundations.And<A?, B> ->
            dev.typr.foundations.And(Optional.ofNullable(and.left()), and.right())
        }
    )
}

/**
 * Bijection for full join results, converting And<Optional<A>, Optional<B>> to And<A?, B?>.
 * Usage: rowParser.fullJoined(other).to(fullJoinToNullable())
 */
fun <A : Any, B : Any> fullJoinToNullable(): Bijection<dev.typr.foundations.And<Optional<A>, Optional<B>>, dev.typr.foundations.And<A?, B?>> {
    return Bijection.of(
        { and: dev.typr.foundations.And<Optional<A>, Optional<B>> ->
            dev.typr.foundations.And(and.left().orElse(null), and.right().orElse(null))
        },
        { and: dev.typr.foundations.And<A?, B?> ->
            dev.typr.foundations.And(Optional.ofNullable(and.left()), Optional.ofNullable(and.right()))
        }
    )
}
