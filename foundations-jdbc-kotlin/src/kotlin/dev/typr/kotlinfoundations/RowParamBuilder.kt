@file:Suppress("unused")
package dev.typr.kotlinfoundations

class RowParamBuilder<Row : Any>(private val underlying: dev.typr.foundations.RowParamBuilder<Row>) {

    fun append(s: String): RowParamBuilder<Row> = RowParamBuilder(underlying.append(s))

    fun append(fragment: Fragment): RowParamBuilder<Row> = RowParamBuilder(underlying.append(fragment.underlying))

    fun <Out> query(parser: ResultSetParser<Out>): RowSqlTemplate.Query<Row, Out> =
        RowSqlTemplate.Query(underlying.query(parser.underlying))

    fun update(): RowSqlTemplate.Update<Row> = RowSqlTemplate.Update(underlying.update())

    fun done(): Fragment = Fragment(underlying.done())
}
