@file:Suppress("unused")
package dev.typr.foundationskt

import java.util.Optional

class Fragment(val underlying: dev.typr.foundations.Fragment) {

    fun render(): String = underlying.render()

    override fun toString(): String {
        val ctx = Sql.currentContext()
        if (ctx != null) {
            return ctx.register(this)
        }
        return render()
    }

    fun append(other: Fragment): Fragment = Fragment(underlying.append(other.underlying))

    operator fun plus(other: Fragment): Fragment = append(other)

    fun <T> query(parser: ResultSetParser<T>): Operation.Query<T> =
        Operation.Query(dev.typr.foundations.Operation.Query(underlying, parser.underlying))

    fun <T : Any> queryOne(type: DbType<T>): Operation.Query<T> =
        query(RowParser.of(type).exactlyOne())

    fun <T : Any> queryList(type: DbType<T>): Operation.Query<List<T>> =
        query(RowParser.of(type).all())

    fun <T : Any> queryMaybe(type: DbType<T>): Operation.Query<T?> =
        query(RowParser.of(type).maxOne())

    fun update(): Operation.Update =
        Operation.Update(dev.typr.foundations.Operation.Update(underlying))

    fun execute(): Operation<Unit> = update().voided()

    fun <T> updateReturning(parser: ResultSetParser<T>): Operation.UpdateReturning<T> =
        Operation.UpdateReturning(dev.typr.foundations.Operation.UpdateReturning(underlying, parser.underlying))

    fun <Row : Any> updateMany(parser: RowParser<Row>, rows: Iterator<Row>): Operation.UpdateMany<Row> =
        Operation.UpdateMany(underlying.updateMany(parser.underlying, rows))

    fun <Row : Any> updateManyReturning(parser: RowParser<Row>, rows: Iterator<Row>): Operation.UpdateManyReturning<Row> =
        Operation.UpdateManyReturning(underlying.updateManyReturning(parser.underlying, rows))

    fun <Row : Any> updateReturningEach(parser: RowParser<Row>, rows: Iterator<Row>): Operation.UpdateReturningEach<Row> =
        Operation.UpdateReturningEach(underlying.updateReturningEach(parser.underlying, rows))

    fun append(s: String): Fragment = Fragment(underlying.append(s))

    fun <T> value(dbType: DbType<T>, value: T): Fragment = Fragment(underlying.value(dbType.underlying, value))

    fun appendAll(fragments: List<Fragment>, separator: Fragment): Fragment =
        Fragment(underlying.appendAll(fragments.map { it.underlying }, separator.underlying))

    fun <T : Any> valueNullable(dbType: DbType<T>, value: T?): Fragment =
        Fragment(underlying.value(dbType.underlying.opt(), Optional.ofNullable(value)))

    fun <Row : Any> paramRow(parser: RowParserNamed<Row>, vararg except: String): RowParamBuilder<Row> =
        RowParamBuilder(underlying.paramRow(parser.underlying, *except))

    fun <Row : Any> row(parser: RowParserNamed<Row>, row: Row, vararg except: String): Fragment =
        Fragment(underlying.row(parser.underlying, row, *except))

    fun <P0> param(dbType: DbType<P0>): ParamBuilders.ParamBuilder1<P0> =
        ParamBuilders.ParamBuilder1(underlying.param(dbType.underlying))

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
    }
}
