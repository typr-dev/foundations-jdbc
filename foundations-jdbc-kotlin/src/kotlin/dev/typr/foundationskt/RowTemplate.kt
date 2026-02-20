@file:Suppress("unused")
package dev.typr.foundationskt

sealed class RowTemplate<Row : Any, Out> : Analyzable {
    abstract val underlying: dev.typr.foundations.RowTemplate<*, *>

    override val analyzable: dev.typr.foundations.Analyzable get() = underlying

    abstract fun on(row: Row): Operation<Out>

    fun fragment(): Fragment = Fragment(underlying.fragment())

    class Query<Row : Any, Out>(override val underlying: dev.typr.foundations.RowTemplate.Query<Row, Out>)
        : RowTemplate<Row, Out>() {
        override fun on(row: Row): Operation.Query<Out> = Operation.Query(underlying.on(row))
    }

    class Update<Row : Any>(override val underlying: dev.typr.foundations.RowTemplate.Update<Row>)
        : RowTemplate<Row, Int>() {
        override fun on(row: Row): Operation.Update = Operation.Update(underlying.on(row))

        fun onMany(rows: Iterator<Row>): Operation.UpdateManyTemplate<Row> =
            Operation.UpdateManyTemplate(underlying.onMany(rows))
    }
}
