@file:Suppress("unused")
package dev.typr.foundationskt

class RowParamBuilder<Row : Any>(private val underlying: dev.typr.foundations.RowParamBuilder<Row>) {

    fun append(s: String): RowParamBuilder<Row> = RowParamBuilder(underlying.append(s))

    fun append(fragment: Fragment): RowParamBuilder<Row> = RowParamBuilder(underlying.append(fragment.underlying))

    fun <Out> query(parser: ResultSetParser<Out>): RowTemplate.Query<Row, Out> =
        RowTemplate.Query(underlying.query(parser.underlying))

    fun update(): RowTemplate.Update<Row> = RowTemplate.Update(underlying.update())

    fun done(): Fragment = Fragment(underlying.done())
}
