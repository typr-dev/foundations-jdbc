@file:Suppress("unused")
package dev.typr.foundationskt

class DuckDbStruct<T>(val underlying: dev.typr.foundations.DuckDbStruct<T>) {
    fun asType(): DuckDbType<T> = DuckDbType(underlying.asType())

    companion object {
        fun <A> builder(name: String): Builder0<A> =
            Builder0(dev.typr.foundations.DuckDbStructBuilders.builder(name))
    }

    class Builder0<A> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder0<A>
    ) {
        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder1<A, F> =
            Builder1(underlying.field(name, type.underlying, getter))
    }

    class Builder1<A, T0> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder1<A, T0>
    ) {
        fun build(decode: (T0) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0 -> decode(t0) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder2<A, T0, F> =
            Builder2(underlying.field(name, type.underlying, getter))
    }

    class Builder2<A, T0, T1> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder2<A, T0, T1>
    ) {
        fun build(decode: (T0, T1) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1 -> decode(t0, t1) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder3<A, T0, T1, F> =
            Builder3(underlying.field(name, type.underlying, getter))
    }

    class Builder3<A, T0, T1, T2> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder3<A, T0, T1, T2>
    ) {
        fun build(decode: (T0, T1, T2) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2 -> decode(t0, t1, t2) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder4<A, T0, T1, T2, F> =
            Builder4(underlying.field(name, type.underlying, getter))
    }

    class Builder4<A, T0, T1, T2, T3> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder4<A, T0, T1, T2, T3>
    ) {
        fun build(decode: (T0, T1, T2, T3) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3 -> decode(t0, t1, t2, t3) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder5<A, T0, T1, T2, T3, F> =
            Builder5(underlying.field(name, type.underlying, getter))
    }

    class Builder5<A, T0, T1, T2, T3, T4> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder5<A, T0, T1, T2, T3, T4>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4 -> decode(t0, t1, t2, t3, t4) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder6<A, T0, T1, T2, T3, T4, F> =
            Builder6(underlying.field(name, type.underlying, getter))
    }

    class Builder6<A, T0, T1, T2, T3, T4, T5> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder6<A, T0, T1, T2, T3, T4, T5>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5 -> decode(t0, t1, t2, t3, t4, t5) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder7<A, T0, T1, T2, T3, T4, T5, F> =
            Builder7(underlying.field(name, type.underlying, getter))
    }

    class Builder7<A, T0, T1, T2, T3, T4, T5, T6> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder7<A, T0, T1, T2, T3, T4, T5, T6>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6 -> decode(t0, t1, t2, t3, t4, t5, t6) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> =
            Builder8(underlying.field(name, type.underlying, getter))
    }

    class Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7 -> decode(t0, t1, t2, t3, t4, t5, t6, t7) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> =
            Builder9(underlying.field(name, type.underlying, getter))
    }

    class Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> =
            Builder10(underlying.field(name, type.underlying, getter))
    }

    class Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> =
            Builder11(underlying.field(name, type.underlying, getter))
    }

    class Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> =
            Builder12(underlying.field(name, type.underlying, getter))
    }

    class Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> =
            Builder13(underlying.field(name, type.underlying, getter))
    }

    class Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> =
            Builder14(underlying.field(name, type.underlying, getter))
    }

    class Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> =
            Builder15(underlying.field(name, type.underlying, getter))
    }

    class Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> =
            Builder16(underlying.field(name, type.underlying, getter))
    }

    class Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> =
            Builder17(underlying.field(name, type.underlying, getter))
    }

    class Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> =
            Builder18(underlying.field(name, type.underlying, getter))
    }

    class Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> =
            Builder19(underlying.field(name, type.underlying, getter))
    }

    class Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> =
            Builder20(underlying.field(name, type.underlying, getter))
    }

    class Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> =
            Builder21(underlying.field(name, type.underlying, getter))
    }

    class Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> =
            Builder22(underlying.field(name, type.underlying, getter))
    }

    class Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> =
            Builder23(underlying.field(name, type.underlying, getter))
    }

    class Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> =
            Builder24(underlying.field(name, type.underlying, getter))
    }

    class Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> =
            Builder25(underlying.field(name, type.underlying, getter))
    }

    class Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> =
            Builder26(underlying.field(name, type.underlying, getter))
    }

    class Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> =
            Builder27(underlying.field(name, type.underlying, getter))
    }

    class Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> =
            Builder28(underlying.field(name, type.underlying, getter))
    }

    class Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> =
            Builder29(underlying.field(name, type.underlying, getter))
    }

    class Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28) })

        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> =
            Builder30(underlying.field(name, type.underlying, getter))
    }

    class Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> internal constructor(
        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29>
    ) {
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) -> A): DuckDbStruct<A> =
            DuckDbStruct(underlying.build { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29 -> decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29) })

    }
}
