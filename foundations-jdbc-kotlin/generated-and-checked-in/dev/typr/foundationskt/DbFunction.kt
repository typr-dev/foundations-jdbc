package dev.typr.foundationskt

/**
 * Type-safe stored function definitions with fully typed inputs.
 *
 * Usage:
 * ```kotlin
 * val calcTax: DbFunction.Def2<BigDecimal, String, BigDecimal> = DbFunction.define("calculate_tax", PgTypes.numeric)
 *     .`in`(PgTypes.numeric)
 *     .`in`(PgTypes.text)
 *     .build()
 * val tax = calcTax.call(amount, "US").transact(tx)  // Types enforced!
 * ```
 *
 * @see DbProcedure for stored procedures (with OUT/INOUT parameters)
 */
object DbFunction {

    /** Start defining a stored function (single return value, uses SELECT). */
    fun <R> define(name: String, returnType: DbType<R>): Builder_0<R> =
        Builder_0(dev.typr.foundations.DbFunction.define(name, returnType.underlying))

    // ─────────────────────────────────────────────────────────────────────────────
    // Function definition interfaces (11 total: 0-10 inputs)
    // ─────────────────────────────────────────────────────────────────────────────

    /** Function definition with 0 input(s). */
    fun interface Def0<R> {
        fun call(): ProcedureOp<R>
    }

    /** Function definition with 1 input(s). */
    fun interface Def1<I0, R> {
        fun call(i0: I0): ProcedureOp<R>
    }

    /** Function definition with 2 input(s). */
    fun interface Def2<I0, I1, R> {
        fun call(i0: I0, i1: I1): ProcedureOp<R>
    }

    /** Function definition with 3 input(s). */
    fun interface Def3<I0, I1, I2, R> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<R>
    }

    /** Function definition with 4 input(s). */
    fun interface Def4<I0, I1, I2, I3, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<R>
    }

    /** Function definition with 5 input(s). */
    fun interface Def5<I0, I1, I2, I3, I4, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<R>
    }

    /** Function definition with 6 input(s). */
    fun interface Def6<I0, I1, I2, I3, I4, I5, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<R>
    }

    /** Function definition with 7 input(s). */
    fun interface Def7<I0, I1, I2, I3, I4, I5, I6, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<R>
    }

    /** Function definition with 8 input(s). */
    fun interface Def8<I0, I1, I2, I3, I4, I5, I6, I7, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<R>
    }

    /** Function definition with 9 input(s). */
    fun interface Def9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<R>
    }

    /** Function definition with 10 input(s). */
    fun interface Def10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<R>
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Function builders (11 total: 0-10 inputs)
    // ─────────────────────────────────────────────────────────────────────────────

    class Builder_0<R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_0<R>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1<I0, R> =
            Builder_1(underlying.`in`(type.underlying))

        fun build(): Def0<R> {
            val javaFn = underlying.build()
            return Def0 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call() as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_1<I0, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_1<I0, R>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2<I0, I1, R> =
            Builder_2(underlying.`in`(type.underlying))

        fun build(): Def1<I0, R> {
            val javaFn = underlying.build()
            return Def1 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_2<I0, I1, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_2<I0, I1, R>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3<I0, I1, I2, R> =
            Builder_3(underlying.`in`(type.underlying))

        fun build(): Def2<I0, I1, R> {
            val javaFn = underlying.build()
            return Def2 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_3<I0, I1, I2, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_3<I0, I1, I2, R>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4<I0, I1, I2, I3, R> =
            Builder_4(underlying.`in`(type.underlying))

        fun build(): Def3<I0, I1, I2, R> {
            val javaFn = underlying.build()
            return Def3 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_4<I0, I1, I2, I3, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_4<I0, I1, I2, I3, R>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5<I0, I1, I2, I3, I4, R> =
            Builder_5(underlying.`in`(type.underlying))

        fun build(): Def4<I0, I1, I2, I3, R> {
            val javaFn = underlying.build()
            return Def4 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_5<I0, I1, I2, I3, I4, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_5<I0, I1, I2, I3, I4, R>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6<I0, I1, I2, I3, I4, I5, R> =
            Builder_6(underlying.`in`(type.underlying))

        fun build(): Def5<I0, I1, I2, I3, I4, R> {
            val javaFn = underlying.build()
            return Def5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_6<I0, I1, I2, I3, I4, I5, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_6<I0, I1, I2, I3, I4, I5, R>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7<I0, I1, I2, I3, I4, I5, I6, R> =
            Builder_7(underlying.`in`(type.underlying))

        fun build(): Def6<I0, I1, I2, I3, I4, I5, R> {
            val javaFn = underlying.build()
            return Def6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_7<I0, I1, I2, I3, I4, I5, I6, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_7<I0, I1, I2, I3, I4, I5, I6, R>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8<I0, I1, I2, I3, I4, I5, I6, I7, R> =
            Builder_8(underlying.`in`(type.underlying))

        fun build(): Def7<I0, I1, I2, I3, I4, I5, I6, R> {
            val javaFn = underlying.build()
            return Def7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_8<I0, I1, I2, I3, I4, I5, I6, I7, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_8<I0, I1, I2, I3, I4, I5, I6, I7, R>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> =
            Builder_9(underlying.`in`(type.underlying))

        fun build(): Def8<I0, I1, I2, I3, I4, I5, I6, I7, R> {
            val javaFn = underlying.build()
            return Def8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> =
            Builder_10(underlying.`in`(type.underlying))

        fun build(): Def9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> {
            val javaFn = underlying.build()
            return Def9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }

    class Builder_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> internal constructor(
        private val underlying: dev.typr.foundations.DbFunction.Builder_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R>
    ) {

        fun build(): Def10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> {
            val javaFn = underlying.build()
            return Def10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as R }
            }
        }
    }
}
