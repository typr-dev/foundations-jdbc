@file:Suppress("unused")
package dev.typr.foundationskt

object ParamBuilders {
    class ParamBuilder1<P0>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder1<*>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder1<*>) : this(u, List(1) { null })

        fun append(s: String): ParamBuilder1<P0> = ParamBuilder1(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder1<P0> = ParamBuilder1(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder1<P0> = ParamBuilder1(underlying.append(fragment.underlying), transforms)

        fun <P1> param(type: DbType<P1>): ParamBuilder2<P0, P1> =
            ParamBuilder2(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder2<P0, Boolean> =
            ParamBuilder2(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder2<P0, A?> =
            ParamBuilder2(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder2<P0, Pair<A, B>?> =
            ParamBuilder2(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder2<P0, Triple<A, B, C>?> =
            ParamBuilder2(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query1<P0, Out> =
            SqlTemplate.Query1(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update1<P0> =
            SqlTemplate.Update1(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder2<P0, P1>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder2<*, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder2<*, *>) : this(u, List(2) { null })

        fun append(s: String): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.append(fragment.underlying), transforms)

        fun <P2> param(type: DbType<P2>): ParamBuilder3<P0, P1, P2> =
            ParamBuilder3(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder3<P0, P1, Boolean> =
            ParamBuilder3(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder3<P0, P1, A?> =
            ParamBuilder3(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder3<P0, P1, Pair<A, B>?> =
            ParamBuilder3(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder3<P0, P1, Triple<A, B, C>?> =
            ParamBuilder3(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query2<P0, P1, Out> =
            SqlTemplate.Query2(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update2<P0, P1> =
            SqlTemplate.Update2(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder3<P0, P1, P2>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder3<*, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder3<*, *, *>) : this(u, List(3) { null })

        fun append(s: String): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.append(fragment.underlying), transforms)

        fun <P3> param(type: DbType<P3>): ParamBuilder4<P0, P1, P2, P3> =
            ParamBuilder4(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder4<P0, P1, P2, Boolean> =
            ParamBuilder4(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder4<P0, P1, P2, A?> =
            ParamBuilder4(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder4<P0, P1, P2, Pair<A, B>?> =
            ParamBuilder4(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder4<P0, P1, P2, Triple<A, B, C>?> =
            ParamBuilder4(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query3<P0, P1, P2, Out> =
            SqlTemplate.Query3(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update3<P0, P1, P2> =
            SqlTemplate.Update3(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder4<P0, P1, P2, P3>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder4<*, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder4<*, *, *, *>) : this(u, List(4) { null })

        fun append(s: String): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.append(fragment.underlying), transforms)

        fun <P4> param(type: DbType<P4>): ParamBuilder5<P0, P1, P2, P3, P4> =
            ParamBuilder5(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder5<P0, P1, P2, P3, Boolean> =
            ParamBuilder5(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder5<P0, P1, P2, P3, A?> =
            ParamBuilder5(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder5<P0, P1, P2, P3, Pair<A, B>?> =
            ParamBuilder5(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder5<P0, P1, P2, P3, Triple<A, B, C>?> =
            ParamBuilder5(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query4<P0, P1, P2, P3, Out> =
            SqlTemplate.Query4(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update4<P0, P1, P2, P3> =
            SqlTemplate.Update4(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder5<P0, P1, P2, P3, P4>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder5<*, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder5<*, *, *, *, *>) : this(u, List(5) { null })

        fun append(s: String): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.append(fragment.underlying), transforms)

        fun <P5> param(type: DbType<P5>): ParamBuilder6<P0, P1, P2, P3, P4, P5> =
            ParamBuilder6(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder6<P0, P1, P2, P3, P4, Boolean> =
            ParamBuilder6(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder6<P0, P1, P2, P3, P4, A?> =
            ParamBuilder6(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder6<P0, P1, P2, P3, P4, Pair<A, B>?> =
            ParamBuilder6(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder6<P0, P1, P2, P3, P4, Triple<A, B, C>?> =
            ParamBuilder6(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query5<P0, P1, P2, P3, P4, Out> =
            SqlTemplate.Query5(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update5<P0, P1, P2, P3, P4> =
            SqlTemplate.Update5(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder6<P0, P1, P2, P3, P4, P5>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder6<*, *, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder6<*, *, *, *, *, *>) : this(u, List(6) { null })

        fun append(s: String): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.append(fragment.underlying), transforms)

        fun <P6> param(type: DbType<P6>): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> =
            ParamBuilder7(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder7<P0, P1, P2, P3, P4, P5, Boolean> =
            ParamBuilder7(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder7<P0, P1, P2, P3, P4, P5, A?> =
            ParamBuilder7(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder7<P0, P1, P2, P3, P4, P5, Pair<A, B>?> =
            ParamBuilder7(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder7<P0, P1, P2, P3, P4, P5, Triple<A, B, C>?> =
            ParamBuilder7(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query6<P0, P1, P2, P3, P4, P5, Out> =
            SqlTemplate.Query6(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update6<P0, P1, P2, P3, P4, P5> =
            SqlTemplate.Update6(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder7<P0, P1, P2, P3, P4, P5, P6>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder7<*, *, *, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder7<*, *, *, *, *, *, *>) : this(u, List(7) { null })

        fun append(s: String): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.append(fragment.underlying), transforms)

        fun <P7> param(type: DbType<P7>): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> =
            ParamBuilder8(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, Boolean> =
            ParamBuilder8(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, A?> =
            ParamBuilder8(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, Pair<A, B>?> =
            ParamBuilder8(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, Triple<A, B, C>?> =
            ParamBuilder8(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query7<P0, P1, P2, P3, P4, P5, P6, Out> =
            SqlTemplate.Query7(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update7<P0, P1, P2, P3, P4, P5, P6> =
            SqlTemplate.Update7(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder8<*, *, *, *, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder8<*, *, *, *, *, *, *, *>) : this(u, List(8) { null })

        fun append(s: String): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.append(fragment.underlying), transforms)

        fun <P8> param(type: DbType<P8>): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> =
            ParamBuilder9(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, Boolean> =
            ParamBuilder9(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, A?> =
            ParamBuilder9(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, Pair<A, B>?> =
            ParamBuilder9(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, Triple<A, B, C>?> =
            ParamBuilder9(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out> =
            SqlTemplate.Query8(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update8<P0, P1, P2, P3, P4, P5, P6, P7> =
            SqlTemplate.Update8(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder9<*, *, *, *, *, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder9<*, *, *, *, *, *, *, *, *>) : this(u, List(9) { null })

        fun append(s: String): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.append(fragment.underlying), transforms)

        fun <P9> param(type: DbType<P9>): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> =
            ParamBuilder10(underlying.param(type.underlying), transforms + listOf(null))
        fun optionally(inner: Fragment): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, Boolean> =
            ParamBuilder10(underlying.optionally(inner.underlying), transforms + listOf(null))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, A?> =
            ParamBuilder10(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
                transforms + listOf(OptionallyTransforms.nullableToOptional))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, Pair<A, B>?> =
            ParamBuilder10(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
                transforms + listOf(OptionallyTransforms.pairToOptionalTuple2))

        @Suppress("UNCHECKED_CAST")
        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, Triple<A, B, C>?> =
            ParamBuilder10(
                underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
                transforms + listOf(OptionallyTransforms.tripleToOptionalTuple3))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out> =
            SqlTemplate.Query9(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8> =
            SqlTemplate.Update9(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>(
        internal val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder10<*, *, *, *, *, *, *, *, *, *>,
        internal val transforms: List<((Any?) -> Any?)?>
    ) {
        constructor(u: dev.typr.foundations.ParamBuilders.ParamBuilder10<*, *, *, *, *, *, *, *, *, *>) : this(u, List(10) { null })

        fun append(s: String): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.append(s), transforms)

        fun <T> value(type: DbType<T>, value: T): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.value(type.underlying, value), transforms)

        fun append(fragment: Fragment): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.append(fragment.underlying), transforms)

        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out> =
            SqlTemplate.Query10(underlying.query(parser.underlying), transforms)

        fun update(): SqlTemplate.Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> =
            SqlTemplate.Update10(underlying.update(), transforms)

        fun done(): Fragment = Fragment(underlying.done())
    }
}
