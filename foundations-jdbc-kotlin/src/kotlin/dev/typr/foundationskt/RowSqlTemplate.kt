@file:Suppress("unused")
package dev.typr.foundationskt

sealed class RowSqlTemplate<Row : Any, Out> {
    abstract val underlying: dev.typr.foundations.RowSqlTemplate<*, *>

    abstract fun on(row: Row): Operation<Out>

    fun fragment(): Fragment = Fragment(underlying.fragment())

    class Query<Row : Any, Out>(override val underlying: dev.typr.foundations.RowSqlTemplate.Query<Row, Out>)
        : RowSqlTemplate<Row, Out>() {
        override fun on(row: Row): Operation.Query<Out> = Operation.Query(underlying.on(row))
    }

    class Update<Row : Any>(override val underlying: dev.typr.foundations.RowSqlTemplate.Update<Row>)
        : RowSqlTemplate<Row, Int>() {
        override fun on(row: Row): Operation.Update = Operation.Update(underlying.on(row))

        fun onMany(rows: Iterator<Row>): Operation.UpdateManyTemplate<Row> =
            Operation.UpdateManyTemplate(underlying.onMany(rows))
    }
}
