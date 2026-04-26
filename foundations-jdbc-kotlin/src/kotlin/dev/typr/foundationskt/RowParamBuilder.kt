@file:Suppress("unused")
package dev.typr.foundationskt

class RowParamBuilder<Row : Any>(internal val underlying: dev.typr.foundations.RowParamBuilder<Row>) {

    fun append(s: String): RowParamBuilder<Row> = RowParamBuilder(underlying.append(s))

    fun append(fragment: Fragment): RowParamBuilder<Row> = RowParamBuilder(underlying.append(fragment.underlying))

    // Row-driven execution

    fun updateOne(row: Row): Operation.Update = Operation.Update(underlying.updateOne(row))

    fun <Out : Any> updateReturning(row: Row, parser: ResultSetParser<Out>): OperationRead.Query<Out> =
        OperationRead.Query(underlying.updateReturning(row, parser.underlying))

    fun updateReturning(row: Row): OperationRead.Query<Row> =
        OperationRead.Query(underlying.updateReturning(row))

    fun updateMany(rows: Iterator<Row>): Operation.BatchUpdate<Row> =
        Operation.BatchUpdate(underlying.updateMany(rows))

    fun <Out> updateOneGenerated(row: Row, generatedColumns: Array<String>, parser: ResultSetParser<Out>): Operation.UpdateReturningGeneratedKeys<Out> =
        Operation.UpdateReturningGeneratedKeys(underlying.updateOneGenerated(row, generatedColumns, parser.underlying))

    fun <Out> queryOne(row: Row, parser: ResultSetParser<Out>): OperationRead.Query<Out> =
        OperationRead.Query(underlying.queryOne(row, parser.underlying))

    fun done(): Fragment = Fragment(underlying.done())
}
