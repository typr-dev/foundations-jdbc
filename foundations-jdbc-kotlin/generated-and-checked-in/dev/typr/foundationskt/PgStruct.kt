@file:Suppress("unused")
package dev.typr.foundationskt

class PgStruct<T>(val underlying: dev.typr.foundations.PgStruct<T>) {
    fun asType(): PgType<T> = PgType(underlying.asType())

    companion object {
        fun <A> builder(name: String): Builder0<A> =
            Builder0(dev.typr.foundations.PgStructBuilders.builder(name))
    }

    class Builder0<A> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder0<A>
    ) {
        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder1<A, F> =
            Builder1(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder1<A, F> =
            Builder1(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder1<A, Array<F>> =
            Builder1(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder1<A, T0> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder1<A, T0>
    ) {
        fun build(decode: (T0) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0 -> decode(t0) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder2<A, T0, F> =
            Builder2(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder2<A, T0, F> =
            Builder2(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder2<A, T0, Array<F>> =
            Builder2(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder2<A, T0, T1> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder2<A, T0, T1>
    ) {
        fun build(decode: (T0, T1) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1 -> decode(t0, t1) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder3<A, T0, T1, F> =
            Builder3(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder3<A, T0, T1, F> =
            Builder3(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder3<A, T0, T1, Array<F>> =
            Builder3(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder3<A, T0, T1, T2> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder3<A, T0, T1, T2>
    ) {
        fun build(decode: (T0, T1, T2) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2 -> decode(t0, t1, t2) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder4<A, T0, T1, T2, F> =
            Builder4(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder4<A, T0, T1, T2, F> =
            Builder4(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder4<A, T0, T1, T2, Array<F>> =
            Builder4(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder4<A, T0, T1, T2, T3> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder4<A, T0, T1, T2, T3>
    ) {
        fun build(decode: (T0, T1, T2, T3) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3 -> decode(t0, t1, t2, t3) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder5<A, T0, T1, T2, T3, F> =
            Builder5(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder5<A, T0, T1, T2, T3, F> =
            Builder5(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder5<A, T0, T1, T2, T3, Array<F>> =
            Builder5(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder5<A, T0, T1, T2, T3, T4> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder5<A, T0, T1, T2, T3, T4>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4 -> decode(t0, t1, t2, t3, t4) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder6<A, T0, T1, T2, T3, T4, F> =
            Builder6(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder6<A, T0, T1, T2, T3, T4, F> =
            Builder6(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder6<A, T0, T1, T2, T3, T4, Array<F>> =
            Builder6(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder6<A, T0, T1, T2, T3, T4, T5> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder6<A, T0, T1, T2, T3, T4, T5>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5 -> decode(t0, t1, t2, t3, t4, t5) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder7<A, T0, T1, T2, T3, T4, T5, F> =
            Builder7(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder7<A, T0, T1, T2, T3, T4, T5, F> =
            Builder7(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder7<A, T0, T1, T2, T3, T4, T5, Array<F>> =
            Builder7(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder7<A, T0, T1, T2, T3, T4, T5, T6> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder7<A, T0, T1, T2, T3, T4, T5, T6>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6 -> decode(t0, t1, t2, t3, t4, t5, t6) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> =
            Builder8(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> =
            Builder8(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder8<A, T0, T1, T2, T3, T4, T5, T6, Array<F>> =
            Builder8(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7 -> decode(t0, t1, t2, t3, t4, t5, t6, t7) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> =
            Builder9(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> =
            Builder9(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, Array<F>> =
            Builder9(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> =
            Builder10(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> =
            Builder10(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, Array<F>> =
            Builder10(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> =
            Builder11(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> =
            Builder11(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, Array<F>> =
            Builder11(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> =
            Builder12(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> =
            Builder12(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Array<F>> =
            Builder12(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> =
            Builder13(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> =
            Builder13(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Array<F>> =
            Builder13(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> =
            Builder14(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> =
            Builder14(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Array<F>> =
            Builder14(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> =
            Builder15(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> =
            Builder15(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Array<F>> =
            Builder15(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> =
            Builder16(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> =
            Builder16(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Array<F>> =
            Builder16(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> =
            Builder17(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> =
            Builder17(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Array<F>> =
            Builder17(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> =
            Builder18(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> =
            Builder18(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Array<F>> =
            Builder18(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> =
            Builder19(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> =
            Builder19(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Array<F>> =
            Builder19(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> =
            Builder20(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> =
            Builder20(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Array<F>> =
            Builder20(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> =
            Builder21(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> =
            Builder21(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Array<F>> =
            Builder21(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> =
            Builder22(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> =
            Builder22(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Array<F>> =
            Builder22(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> =
            Builder23(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> =
            Builder23(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Array<F>> =
            Builder23(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> =
            Builder24(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> =
            Builder24(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Array<F>> =
            Builder24(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> =
            Builder25(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> =
            Builder25(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Array<F>> =
            Builder25(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> =
            Builder26(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> =
            Builder26(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Array<F>> =
            Builder26(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> =
            Builder27(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> =
            Builder27(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Array<F>> =
            Builder27(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> =
            Builder28(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> =
            Builder28(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Array<F>> =
            Builder28(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> =
            Builder29(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> =
            Builder29(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, Array<F>> =
            Builder29(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28) })

        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> =
            Builder30(underlying.field(name, type.underlying, getter))

        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> =
            Builder30(underlying.nestedField(name, nestedStruct.underlying, getter))

        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, Array<F>> =
            Builder30(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
    }

    class Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> internal constructor(
        private val underlying: dev.typr.foundations.PgStructBuilders.Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) -> A): PgStruct<A> =
            PgStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29) })

    }
}
