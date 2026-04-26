@file:Suppress("unused")
package dev.typr.foundationskt

import java.util.Optional

class Fragment(val underlying: dev.typr.foundations.Fragment) {

    fun render(): String = underlying.render()

    override fun toString(): String {
        val ctx = SqlBuilder.currentContext()
        if (ctx != null) {
            return ctx.register(this)
        }
        return render()
    }

    fun append(other: Fragment): Fragment = Fragment(underlying.append(other.underlying))

    operator fun plus(other: Fragment): Fragment = append(other)

    fun <T> query(parser: ResultSetParser<T>): OperationRead.Query<T> =
        OperationRead.Query(dev.typr.foundations.OperationRead.Query(underlying, parser.underlying))

    fun <T : Any> queryExactlyOne(type: DbType<T>): OperationRead.Query<T> =
        query(RowCodec.of(type).exactlyOne())

    fun <T : Any> queryExactlyOne(codec: RowCodec<T>): OperationRead.Query<T> =
        query(codec.exactlyOne())

    fun <T : Any> queryAll(type: DbType<T>): OperationRead.Query<List<T>> =
        query(RowCodec.of(type).all())

    fun <T : Any> queryAll(codec: RowCodec<T>): OperationRead.Query<List<T>> =
        query(codec.all())

    fun <T : Any> queryMaxOne(type: DbType<T>): OperationRead.Query<T?> =
        query(RowCodec.of(type).maxOne())

    fun <T : Any> queryMaxOne(codec: RowCodec<T>): OperationRead.Query<T?> =
        query(codec.maxOne())

    fun update(): Operation.Update =
        Operation.Update(dev.typr.foundations.Operation.Update(underlying))

    fun execute(): Operation.Execute =
        Operation.Execute(dev.typr.foundations.Operation.Execute(underlying))

    fun <T> updateReturning(parser: ResultSetParser<T>): Operation.UpdateReturning<T> =
        Operation.UpdateReturning(dev.typr.foundations.Operation.UpdateReturning(underlying, parser.underlying))

    fun <T> updateReturningGeneratedKeys(columnNames: Array<String>, parser: ResultSetParser<T>): Operation.UpdateReturningGeneratedKeys<T> =
        Operation.UpdateReturningGeneratedKeys(underlying.updateReturningGeneratedKeys(columnNames, parser.underlying))

    fun <Row : Any> updateMany(parser: RowCodec<Row>, rows: Iterator<Row>): Operation.UpdateMany<Row> =
        Operation.UpdateMany(underlying.updateMany(parser.underlying, rows))

    fun <Row : Any> updateManyReturning(parser: RowCodec<Row>, rows: Iterator<Row>): Operation.UpdateManyReturning<Row> =
        Operation.UpdateManyReturning(underlying.updateManyReturning(parser.underlying, rows))

    fun <Row : Any> updateReturningEach(parser: RowCodec<Row>, rows: Iterator<Row>): Operation.UpdateReturningEach<Row> =
        Operation.UpdateReturningEach(underlying.updateReturningEach(parser.underlying, rows))

    fun <T : Any> streamingQuery(codec: RowCodec<T>, fetchSize: Int): OperationRead.Streaming<T> =
        OperationRead.Streaming(underlying.streamingQuery(codec.underlying, fetchSize))

    fun <T : Any> streamingQuery(type: DbType<T>, fetchSize: Int): OperationRead.Streaming<T> =
        OperationRead.Streaming(underlying.streamingQuery(type.underlying, fetchSize))

    fun append(s: String): Fragment = Fragment(underlying.append(s))

    fun <T> value(dbType: DbType<T>, value: T): Fragment = Fragment(underlying.value(dbType.underlying, value))

    fun appendAll(fragments: List<Fragment>, separator: Fragment): Fragment =
        Fragment(underlying.appendAll(fragments.map { it.underlying }, separator.underlying))

    fun <T : Any> valueNullable(dbType: DbType<T>, value: T?): Fragment =
        Fragment(underlying.value(dbType.underlying.opt(), Optional.ofNullable(value)))

    fun <Row : Any> row(parser: RowCodecNamed<Row>, row: Row, vararg except: String): Fragment =
        Fragment(underlying.row(parser.underlying, row, *except))

    fun <P0> param(dbType: DbType<P0>): ParamBuilders.ParamBuilder1<P0> =
        ParamBuilders.ParamBuilder1(underlying.param(dbType.underlying))

    // ── Conditional append DSL ──

    fun <T : Any> optionally(value: T?): OptionallyValue<T> = OptionallyValue(this, value)
    fun optionally(condition: Boolean): OptionallyFlag = OptionallyFlag(this, condition)

    class OptionallyValue<T : Any>(private val base: Fragment, private val value: T?) {
        fun append(sql: String, type: DbType<T>): Fragment {
            val javaOpt = base.underlying.optionally(java.util.Optional.ofNullable(value))
            return Fragment(javaOpt.append(sql, type.underlying))
        }

        fun append(sql: String, type: DbType<T>, whenAbsent: String): Fragment {
            val javaOpt = base.underlying.optionally(java.util.Optional.ofNullable(value))
            return Fragment(javaOpt.append(sql, type.underlying, whenAbsent))
        }
    }

    class OptionallyFlag(private val base: Fragment, private val condition: Boolean) {
        fun append(sql: String): Fragment =
            Fragment(base.underlying.optionally(condition).append(sql))

        fun append(fragment: Fragment): Fragment =
            Fragment(base.underlying.optionally(condition).append(fragment.underlying))

        fun append(whenTrue: String, whenFalse: String): Fragment =
            Fragment(base.underlying.optionally(condition).append(whenTrue, whenFalse))

        fun append(whenTrue: Fragment, whenFalse: Fragment): Fragment =
            Fragment(base.underlying.optionally(condition).append(whenTrue.underlying, whenFalse.underlying))
    }

    companion object {
        @JvmField
        val EMPTY: Fragment = Fragment(dev.typr.foundations.Fragment.EMPTY)

        @JvmStatic
        fun of(value: String): Fragment = Fragment(dev.typr.foundations.Fragment.of(value))

        @JvmStatic
        fun empty(): Fragment = EMPTY

        @JvmStatic
        fun quotedDouble(value: String): Fragment = Fragment(dev.typr.foundations.Fragment.quotedDouble(value))

        @JvmStatic
        fun quotedSingle(value: String): Fragment = Fragment(dev.typr.foundations.Fragment.quotedSingle(value))

        @JvmStatic
        fun <A> value(value: A, dbType: DbType<A>): Fragment =
            Fragment(dev.typr.foundations.Fragment.value(value, dbType.underlying))

        @JvmStatic
        fun <A> encode(dbType: DbType<A>, value: A): Fragment =
            Fragment(dev.typr.foundations.Fragment.encode(dbType.underlying, value))

        @JvmStatic
        fun and(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.and(fragments.map { it.underlying }))

        @JvmStatic
        fun and(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.and(fragments.map { it.underlying }))

        @JvmStatic
        fun or(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.or(fragments.map { it.underlying }))

        @JvmStatic
        fun or(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.or(fragments.map { it.underlying }))

        @JvmStatic
        fun whereAnd(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.whereAnd(fragments.map { it.underlying }))

        @JvmStatic
        fun whereAnd(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.whereAnd(fragments.map { it.underlying }))

        @JvmStatic
        fun whereOr(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.whereOr(fragments.map { it.underlying }))

        @JvmStatic
        fun whereOr(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.whereOr(fragments.map { it.underlying }))

        @JvmStatic
        fun set(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.set(fragments.map { it.underlying }))

        @JvmStatic
        fun set(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.set(fragments.map { it.underlying }))

        @JvmStatic
        fun parentheses(fragment: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.parentheses(fragment.underlying))

        @JvmStatic
        fun comma(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.comma(fragments.map { it.underlying }))

        @JvmStatic
        fun comma(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.comma(fragments.map { it.underlying }))

        @JvmStatic
        fun orderBy(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.orderBy(fragments.map { it.underlying }))

        @JvmStatic
        fun orderBy(fragments: List<Fragment>): Fragment =
            Fragment(dev.typr.foundations.Fragment.orderBy(fragments.map { it.underlying }))

        @JvmStatic
        fun join(fragments: List<Fragment>, separator: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.join(fragments.map { it.underlying }, separator.underlying))

        @JvmStatic
        fun concat(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.concat(*fragments.map { it.underlying }.toTypedArray()))

        @JvmStatic
        fun of(vararg fragments: Fragment): Fragment =
            Fragment(dev.typr.foundations.Fragment.of(*fragments.map { it.underlying }.toTypedArray()))

        /**
         * Emit `DROP TABLE IF EXISTS <table>`. Works on PostgreSQL, DuckDB, MariaDB, MySQL,
         * SQL Server (2016+) and Oracle (23c+). DB2 does not support the `IF EXISTS` clause —
         * wrap the plain `DROP TABLE <table>` in a try/catch for SQLSTATE 42704.
         */
        @JvmStatic
        fun dropTableIfExists(table: String): Operation.Execute =
            Operation.Execute(dev.typr.foundations.Fragment.dropTableIfExists(table))

        @JvmStatic
        fun <Row : Any> insertOne(
            table: String, codec: RowCodecNamed<Row>, row: Row, vararg except: String
        ): Operation.Update =
            Operation.Update(dev.typr.foundations.Fragment.insertOne(table, codec.underlying, row, *except))

        @JvmStatic
        fun <Row : Any> insertMany(
            table: String, codec: RowCodecNamed<Row>, rows: Iterator<Row>, vararg except: String
        ): Operation.BatchUpdate<Row> =
            Operation.BatchUpdate(dev.typr.foundations.Fragment.insertMany(table, codec.underlying, rows, *except))

        @JvmStatic
        fun <Row : Any> insertOneReturning(
            table: String, codec: RowCodecNamed<Row>, row: Row, vararg except: String
        ): OperationRead.Query<Row> =
            OperationRead.Query(dev.typr.foundations.Fragment.insertOneReturning(table, codec.underlying, row, *except))

        @JvmStatic
        fun <In : Any, Out : Any> insertOneReturning(
            table: String, writeCodec: RowCodecNamed<In>, row: In, readCodec: RowCodecNamed<Out>
        ): OperationRead.Query<Out> =
            OperationRead.Query(dev.typr.foundations.Fragment.insertOneReturning(
                table, writeCodec.underlying, row, readCodec.underlying))

        @JvmStatic
        fun <Row : Any, Out> insertOneGenerated(
            table: String, codec: RowCodecNamed<Row>, row: Row,
            generatedColumns: Array<String>, parser: ResultSetParser<Out>, vararg except: String
        ): Operation.UpdateReturningGeneratedKeys<Out> =
            Operation.UpdateReturningGeneratedKeys(dev.typr.foundations.Fragment.insertOneGenerated(
                table, codec.underlying, row, generatedColumns, parser.underlying, *except))

        @JvmStatic
        fun <Row : Any> upsertOne(
            table: String, codec: RowCodecNamed<Row>, row: Row, vararg conflictColumns: String
        ): Operation.Update =
            Operation.Update(dev.typr.foundations.Fragment.upsertOne(table, codec.underlying, row, *conflictColumns))

        @JvmStatic
        fun <Row : Any> upsertMany(
            table: String, codec: RowCodecNamed<Row>, rows: Iterator<Row>, vararg conflictColumns: String
        ): Operation.BatchUpdate<Row> =
            Operation.BatchUpdate(dev.typr.foundations.Fragment.upsertMany(table, codec.underlying, rows, *conflictColumns))

        @JvmStatic
        fun <Row : Any> upsertOneReturning(
            table: String, codec: RowCodecNamed<Row>, row: Row, vararg conflictColumns: String
        ): OperationRead.Query<Row> =
            OperationRead.Query(dev.typr.foundations.Fragment.upsertOneReturning(table, codec.underlying, row, *conflictColumns))

        @JvmStatic
        fun <Row : Any> insertIgnoreOne(
            table: String, codec: RowCodecNamed<Row>, row: Row, vararg conflictColumns: String
        ): Operation.Update =
            Operation.Update(dev.typr.foundations.Fragment.insertIgnoreOne(table, codec.underlying, row, *conflictColumns))

        @JvmStatic
        fun <Row : Any> insertIgnoreMany(
            table: String, codec: RowCodecNamed<Row>, rows: Iterator<Row>, vararg conflictColumns: String
        ): Operation.BatchUpdate<Row> =
            Operation.BatchUpdate(dev.typr.foundations.Fragment.insertIgnoreMany(table, codec.underlying, rows, *conflictColumns))

        @JvmStatic
        @JvmName("rowStatic")
        fun <Row : Any> row(codec: RowCodecNamed<Row>, row: Row, vararg except: String): Fragment =
            Fragment(dev.typr.foundations.Fragment.EMPTY.row(codec.underlying, row, *except))
    }
}

internal object OptionallyTransforms {
    val nullableToOptional: (Any?) -> Any? = { java.util.Optional.ofNullable(it) }

    val pairToOptionalTuple2: (Any?) -> Any? = { value ->
        if (value is Pair<*, *>) {
            java.util.Optional.of(dev.typr.foundations.Tuple.of(value.first, value.second))
        } else {
            java.util.Optional.empty<Any>()
        }
    }

    val tripleToOptionalTuple3: (Any?) -> Any? = { value ->
        if (value is Triple<*, *, *>) {
            java.util.Optional.of(dev.typr.foundations.Tuple.of(value.first, value.second, value.third))
        } else {
            java.util.Optional.empty<Any>()
        }
    }
}
