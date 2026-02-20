@file:Suppress("unused")
package dev.typr.foundationskt

sealed class Template<In, Out> {
    abstract val underlying: dev.typr.foundations.Template<*, *>

    abstract fun on(input: In): Operation<Out>

    fun fragment(): Fragment = Fragment(underlying.fragment())

    class Query1<P0, Out>(
        private val _java: dev.typr.foundations.Template.Query1<*, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<P0, Out>() {
        constructor(j: dev.typr.foundations.Template.Query1<*, Out>) : this(j, listOf(null))

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: P0): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(input) ?: input
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t)).underlying }) { t -> on(f0(t)) }
    }

    class Query2<P0, P1, Out>(
        private val _java: dev.typr.foundations.Template.Query2<*, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple2<P0, P1>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query2<*, *, Out>) : this(j, List(2) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple2<P0, P1>): Operation.Query<Out> =
            on(input._1(), input._2())

        fun on(p0: P0, p1: P1): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t)).underlying }) { t -> on(f0(t), f1(t)) }
    }

    class Query3<P0, P1, P2, Out>(
        private val _java: dev.typr.foundations.Template.Query3<*, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple3<P0, P1, P2>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query3<*, *, *, Out>) : this(j, List(3) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple3<P0, P1, P2>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3())

        fun on(p0: P0, p1: P1, p2: P2): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t)).underlying }) { t -> on(f0(t), f1(t), f2(t)) }
    }

    class Query4<P0, P1, P2, P3, Out>(
        private val _java: dev.typr.foundations.Template.Query4<*, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple4<P0, P1, P2, P3>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query4<*, *, *, *, Out>) : this(j, List(4) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple4<P0, P1, P2, P3>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t)) }
    }

    class Query5<P0, P1, P2, P3, P4, Out>(
        private val _java: dev.typr.foundations.Template.Query5<*, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple5<P0, P1, P2, P3, P4>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query5<*, *, *, *, *, Out>) : this(j, List(5) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple5<P0, P1, P2, P3, P4>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t)) }
    }

    class Query6<P0, P1, P2, P3, P4, P5, Out>(
        private val _java: dev.typr.foundations.Template.Query6<*, *, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple6<P0, P1, P2, P3, P4, P5>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query6<*, *, *, *, *, *, Out>) : this(j, List(6) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple6<P0, P1, P2, P3, P4, P5>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)) }
    }

    class Query7<P0, P1, P2, P3, P4, P5, P6, Out>(
        private val _java: dev.typr.foundations.Template.Query7<*, *, *, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query7<*, *, *, *, *, *, *, Out>) : this(j, List(7) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)) }
    }

    class Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out>(
        private val _java: dev.typr.foundations.Template.Query8<*, *, *, *, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query8<*, *, *, *, *, *, *, *, Out>) : this(j, List(8) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)) }
    }

    class Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out>(
        private val _java: dev.typr.foundations.Template.Query9<*, *, *, *, *, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query9<*, *, *, *, *, *, *, *, *, Out>) : this(j, List(9) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8(), input._9())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val v8: Any? = _transforms[8]?.invoke(p8) ?: p8
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7, f8: (T) -> P8): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)) }
    }

    class Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out>(
        private val _java: dev.typr.foundations.Template.Query10<*, *, *, *, *, *, *, *, *, *, Out>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>, Out>() {
        constructor(j: dev.typr.foundations.Template.Query10<*, *, *, *, *, *, *, *, *, *, Out>) : this(j, List(10) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>): Operation.Query<Out> =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8(), input._9(), input._10())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Query<Out> {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val v8: Any? = _transforms[8]?.invoke(p8) ?: p8
            val v9: Any? = _transforms[9]?.invoke(p9) ?: p9
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9).iterator())
            return Operation.Query(dev.typr.foundations.Operation.Query(resolved, _java.parser()))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7, f8: (T) -> P8, f9: (T) -> P9): From<T, Out> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)) }
    }

    class Update1<P0>(
        private val _java: dev.typr.foundations.Template.Update1<*>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<P0, Int>() {
        constructor(j: dev.typr.foundations.Template.Update1<*>) : this(j, listOf(null))

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: P0): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(input) ?: input
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t)).underlying }) { t -> on(f0(t)) }
    }

    class Update2<P0, P1>(
        private val _java: dev.typr.foundations.Template.Update2<*, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple2<P0, P1>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update2<*, *>) : this(j, List(2) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple2<P0, P1>): Operation.Update =
            on(input._1(), input._2())

        fun on(p0: P0, p1: P1): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t)).underlying }) { t -> on(f0(t), f1(t)) }
    }

    class Update3<P0, P1, P2>(
        private val _java: dev.typr.foundations.Template.Update3<*, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple3<P0, P1, P2>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update3<*, *, *>) : this(j, List(3) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple3<P0, P1, P2>): Operation.Update =
            on(input._1(), input._2(), input._3())

        fun on(p0: P0, p1: P1, p2: P2): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t)).underlying }) { t -> on(f0(t), f1(t), f2(t)) }
    }

    class Update4<P0, P1, P2, P3>(
        private val _java: dev.typr.foundations.Template.Update4<*, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple4<P0, P1, P2, P3>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update4<*, *, *, *>) : this(j, List(4) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple4<P0, P1, P2, P3>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t)) }
    }

    class Update5<P0, P1, P2, P3, P4>(
        private val _java: dev.typr.foundations.Template.Update5<*, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple5<P0, P1, P2, P3, P4>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update5<*, *, *, *, *>) : this(j, List(5) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple5<P0, P1, P2, P3, P4>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t)) }
    }

    class Update6<P0, P1, P2, P3, P4, P5>(
        private val _java: dev.typr.foundations.Template.Update6<*, *, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple6<P0, P1, P2, P3, P4, P5>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update6<*, *, *, *, *, *>) : this(j, List(6) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple6<P0, P1, P2, P3, P4, P5>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)) }
    }

    class Update7<P0, P1, P2, P3, P4, P5, P6>(
        private val _java: dev.typr.foundations.Template.Update7<*, *, *, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update7<*, *, *, *, *, *, *>) : this(j, List(7) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)) }
    }

    class Update8<P0, P1, P2, P3, P4, P5, P6, P7>(
        private val _java: dev.typr.foundations.Template.Update8<*, *, *, *, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update8<*, *, *, *, *, *, *, *>) : this(j, List(8) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)) }
    }

    class Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8>(
        private val _java: dev.typr.foundations.Template.Update9<*, *, *, *, *, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update9<*, *, *, *, *, *, *, *, *>) : this(j, List(9) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8(), input._9())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val v8: Any? = _transforms[8]?.invoke(p8) ?: p8
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7, f8: (T) -> P8): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)) }
    }

    class Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>(
        private val _java: dev.typr.foundations.Template.Update10<*, *, *, *, *, *, *, *, *, *>,
        private val _transforms: List<((Any?) -> Any?)?>
    ) : Template<dev.typr.foundations.Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>, Int>() {
        constructor(j: dev.typr.foundations.Template.Update10<*, *, *, *, *, *, *, *, *, *>) : this(j, List(10) { null })

        override val underlying: dev.typr.foundations.Template<*, *> get() = _java

        override fun on(input: dev.typr.foundations.Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>): Operation.Update =
            on(input._1(), input._2(), input._3(), input._4(), input._5(), input._6(), input._7(), input._8(), input._9(), input._10())

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Update {
            val v0: Any? = _transforms[0]?.invoke(p0) ?: p0
            val v1: Any? = _transforms[1]?.invoke(p1) ?: p1
            val v2: Any? = _transforms[2]?.invoke(p2) ?: p2
            val v3: Any? = _transforms[3]?.invoke(p3) ?: p3
            val v4: Any? = _transforms[4]?.invoke(p4) ?: p4
            val v5: Any? = _transforms[5]?.invoke(p5) ?: p5
            val v6: Any? = _transforms[6]?.invoke(p6) ?: p6
            val v7: Any? = _transforms[7]?.invoke(p7) ?: p7
            val v8: Any? = _transforms[8]?.invoke(p8) ?: p8
            val v9: Any? = _transforms[9]?.invoke(p9) ?: p9
            val resolved = dev.typr.foundations.OptionallyResolver.resolve(
                _java.fragment(), listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9).iterator())
            return Operation.Update(dev.typr.foundations.Operation.Update(resolved))
        }

        fun <T> from(f0: (T) -> P0, f1: (T) -> P1, f2: (T) -> P2, f3: (T) -> P3, f4: (T) -> P4, f5: (T) -> P5, f6: (T) -> P6, f7: (T) -> P7, f8: (T) -> P8, f9: (T) -> P9): From<T, Int> =
            From(dev.typr.foundations.Template.From(_java) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)).underlying }) { t -> on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)) }
    }

    class From<T, Out>(
        private val _java: dev.typr.foundations.Template.From<T, *>,
        private val _resolver: (T) -> Operation<Out>
    ) : Template<T, Out>() {
        override val underlying: dev.typr.foundations.Template<*, *> get() = _java
        override fun on(input: T): Operation<Out> = _resolver(input)
    }
}
