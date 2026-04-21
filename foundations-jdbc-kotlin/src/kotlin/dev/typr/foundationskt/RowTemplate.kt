@file:Suppress("unused")
package dev.typr.foundationskt

sealed interface RowTemplate<Row : Any, Out> : Template<Row, Out> {

    class Query<Row : Any, Out>(
        override val underlying: dev.typr.foundations.RowTemplate.Query<Row, Out>
    ) : RowTemplate<Row, Out>, TemplateRead<Row, Out> {
        override fun on(input: Row): OperationRead.Query<Out> = OperationRead.Query(underlying.on(input))
    }

    class Update<Row : Any>(
        override val underlying: dev.typr.foundations.RowTemplate.Update<Row>
    ) : RowTemplate<Row, Int> {
        override fun on(input: Row): Operation<Int> = Operation.JavaWrapped(underlying.on(input))

        fun onMany(rows: Iterator<Row>): Operation.UpdateManyTemplate<Row> =
            Operation.UpdateManyTemplate(underlying.onMany(rows))
    }

    class GeneratedKeys<Row : Any, Out>(
        override val underlying: dev.typr.foundations.RowTemplate.GeneratedKeys<Row, Out>
    ) : RowTemplate<Row, Out> {
        override fun on(input: Row): Operation<Out> = Operation.JavaWrapped(underlying.on(input))
    }
}
