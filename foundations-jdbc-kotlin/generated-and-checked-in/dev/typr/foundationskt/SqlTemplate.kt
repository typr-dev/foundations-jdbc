@file:Suppress("unused")
package dev.typr.foundationskt

sealed class SqlTemplate<In, Out> {
    abstract val underlying: dev.typr.foundations.SqlTemplate<*, *>

    abstract fun on(input: In): Operation<Out>

    fun fragment(): Fragment = Fragment(underlying.fragment())

    class Query1<P0, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query1<P0, Out>) : SqlTemplate<P0, Out>() {
        override fun on(input: P0): Operation.Query<Out> =
            Operation.Query(underlying.on(input))
    }

    class Query2<P0, P1, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query2<P0, P1, Out>) : SqlTemplate<dev.typr.foundations.And<P0, P1>, Out>() {
        override fun on(input: dev.typr.foundations.And<P0, P1>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1))
    }

    class Query3<P0, P1, P2, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query3<P0, P1, P2, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2))
    }

    class Query4<P0, P1, P2, P3, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query4<P0, P1, P2, P3, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3))
    }

    class Query5<P0, P1, P2, P3, P4, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query5<P0, P1, P2, P3, P4, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4))
    }

    class Query6<P0, P1, P2, P3, P4, P5, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query6<P0, P1, P2, P3, P4, P5, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5))
    }

    class Query7<P0, P1, P2, P3, P4, P5, P6, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query7<P0, P1, P2, P3, P4, P5, P6, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6))
    }

    class Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7))
    }

    class Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8))
    }

    class Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>, Out>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>): Operation.Query<Out> =
            Operation.Query(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Query<Out> =
            Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))
    }

    class Update1<P0>(override val underlying: dev.typr.foundations.SqlTemplate.Update1<P0>) : SqlTemplate<P0, Int>() {
        override fun on(input: P0): Operation.Update =
            Operation.Update(underlying.on(input))
    }

    class Update2<P0, P1>(override val underlying: dev.typr.foundations.SqlTemplate.Update2<P0, P1>) : SqlTemplate<dev.typr.foundations.And<P0, P1>, Int>() {
        override fun on(input: dev.typr.foundations.And<P0, P1>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1): Operation.Update =
            Operation.Update(underlying.on(p0, p1))
    }

    class Update3<P0, P1, P2>(override val underlying: dev.typr.foundations.SqlTemplate.Update3<P0, P1, P2>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2))
    }

    class Update4<P0, P1, P2, P3>(override val underlying: dev.typr.foundations.SqlTemplate.Update4<P0, P1, P2, P3>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3))
    }

    class Update5<P0, P1, P2, P3, P4>(override val underlying: dev.typr.foundations.SqlTemplate.Update5<P0, P1, P2, P3, P4>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4))
    }

    class Update6<P0, P1, P2, P3, P4, P5>(override val underlying: dev.typr.foundations.SqlTemplate.Update6<P0, P1, P2, P3, P4, P5>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5))
    }

    class Update7<P0, P1, P2, P3, P4, P5, P6>(override val underlying: dev.typr.foundations.SqlTemplate.Update7<P0, P1, P2, P3, P4, P5, P6>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6))
    }

    class Update8<P0, P1, P2, P3, P4, P5, P6, P7>(override val underlying: dev.typr.foundations.SqlTemplate.Update8<P0, P1, P2, P3, P4, P5, P6, P7>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7))
    }

    class Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8>(override val underlying: dev.typr.foundations.SqlTemplate.Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8))
    }

    class Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>(override val underlying: dev.typr.foundations.SqlTemplate.Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>) : SqlTemplate<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>, Int>() {
        override fun on(input: dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<dev.typr.foundations.And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>): Operation.Update =
            Operation.Update(underlying.on(input))

        fun on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Update =
            Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))
    }
}
