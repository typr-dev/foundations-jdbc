@file:Suppress("unused")
package dev.typr.foundationskt

object ParamBuilders {
    class ParamBuilder1<P0>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder1<P0>
    ) {
        fun append(s: String): ParamBuilder1<P0> = ParamBuilder1(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder1<P0> = ParamBuilder1(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder1<P0> = ParamBuilder1(underlying.append(fragment.underlying))

        fun <P1> param(type: DbType<P1>): ParamBuilder2<P0, P1> =
            ParamBuilder2(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query1<P0, Out> =
            SqlTemplate.Query1(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update1<P0> =
            SqlTemplate.Update1(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder2<P0, P1>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder2<P0, P1>
    ) {
        fun append(s: String): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder2<P0, P1> = ParamBuilder2(underlying.append(fragment.underlying))

        fun <P2> param(type: DbType<P2>): ParamBuilder3<P0, P1, P2> =
            ParamBuilder3(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query2<P0, P1, Out> =
            SqlTemplate.Query2(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update2<P0, P1> =
            SqlTemplate.Update2(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder3<P0, P1, P2>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder3<P0, P1, P2>
    ) {
        fun append(s: String): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder3<P0, P1, P2> = ParamBuilder3(underlying.append(fragment.underlying))

        fun <P3> param(type: DbType<P3>): ParamBuilder4<P0, P1, P2, P3> =
            ParamBuilder4(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query3<P0, P1, P2, Out> =
            SqlTemplate.Query3(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update3<P0, P1, P2> =
            SqlTemplate.Update3(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder4<P0, P1, P2, P3>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder4<P0, P1, P2, P3>
    ) {
        fun append(s: String): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder4<P0, P1, P2, P3> = ParamBuilder4(underlying.append(fragment.underlying))

        fun <P4> param(type: DbType<P4>): ParamBuilder5<P0, P1, P2, P3, P4> =
            ParamBuilder5(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query4<P0, P1, P2, P3, Out> =
            SqlTemplate.Query4(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update4<P0, P1, P2, P3> =
            SqlTemplate.Update4(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder5<P0, P1, P2, P3, P4>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder5<P0, P1, P2, P3, P4>
    ) {
        fun append(s: String): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder5<P0, P1, P2, P3, P4> = ParamBuilder5(underlying.append(fragment.underlying))

        fun <P5> param(type: DbType<P5>): ParamBuilder6<P0, P1, P2, P3, P4, P5> =
            ParamBuilder6(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query5<P0, P1, P2, P3, P4, Out> =
            SqlTemplate.Query5(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update5<P0, P1, P2, P3, P4> =
            SqlTemplate.Update5(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder6<P0, P1, P2, P3, P4, P5>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder6<P0, P1, P2, P3, P4, P5>
    ) {
        fun append(s: String): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder6<P0, P1, P2, P3, P4, P5> = ParamBuilder6(underlying.append(fragment.underlying))

        fun <P6> param(type: DbType<P6>): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> =
            ParamBuilder7(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query6<P0, P1, P2, P3, P4, P5, Out> =
            SqlTemplate.Query6(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update6<P0, P1, P2, P3, P4, P5> =
            SqlTemplate.Update6(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder7<P0, P1, P2, P3, P4, P5, P6>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder7<P0, P1, P2, P3, P4, P5, P6>
    ) {
        fun append(s: String): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> = ParamBuilder7(underlying.append(fragment.underlying))

        fun <P7> param(type: DbType<P7>): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> =
            ParamBuilder8(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query7<P0, P1, P2, P3, P4, P5, P6, Out> =
            SqlTemplate.Query7(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update7<P0, P1, P2, P3, P4, P5, P6> =
            SqlTemplate.Update7(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7>
    ) {
        fun append(s: String): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> = ParamBuilder8(underlying.append(fragment.underlying))

        fun <P8> param(type: DbType<P8>): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> =
            ParamBuilder9(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out> =
            SqlTemplate.Query8(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update8<P0, P1, P2, P3, P4, P5, P6, P7> =
            SqlTemplate.Update8(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8>
    ) {
        fun append(s: String): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> = ParamBuilder9(underlying.append(fragment.underlying))

        fun <P9> param(type: DbType<P9>): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> =
            ParamBuilder10(underlying.param(type.underlying))
        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out> =
            SqlTemplate.Query9(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8> =
            SqlTemplate.Update9(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }

    class ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>(
        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>
    ) {
        fun append(s: String): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.append(s))

        fun <T> value(type: DbType<T>, value: T): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.value(type.underlying, value))

        fun append(fragment: Fragment): ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> = ParamBuilder10(underlying.append(fragment.underlying))

        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out> =
            SqlTemplate.Query10(underlying.query(parser.underlying))

        fun update(): SqlTemplate.Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> =
            SqlTemplate.Update10(underlying.update())

        fun done(): Fragment = Fragment(underlying.done())
    }
}
