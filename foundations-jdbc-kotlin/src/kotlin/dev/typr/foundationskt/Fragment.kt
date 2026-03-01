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

    fun <T> query(parser: ResultSetParser<T>): Operation.Query<T> =
        Operation.Query(dev.typr.foundations.Operation.Query(underlying, parser.underlying))

    fun <T : Any> queryExactlyOne(type: DbType<T>): Operation.Query<T> =
        query(RowCodec.of(type).exactlyOne())

    fun <T : Any> queryExactlyOne(codec: RowCodec<T>): Operation.Query<T> =
        query(codec.exactlyOne())

    fun <T : Any> queryAll(type: DbType<T>): Operation.Query<List<T>> =
        query(RowCodec.of(type).all())

    fun <T : Any> queryAll(codec: RowCodec<T>): Operation.Query<List<T>> =
        query(codec.all())

    fun <T : Any> queryMaxOne(type: DbType<T>): Operation.Query<T?> =
        query(RowCodec.of(type).maxOne())

    fun <T : Any> queryMaxOne(codec: RowCodec<T>): Operation.Query<T?> =
        query(codec.maxOne())

    fun update(): Operation.Update =
        Operation.Update(dev.typr.foundations.Operation.Update(underlying))

    fun execute(): Operation<Unit> = update().voided()

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

    fun <T : Any> streamingQuery(codec: RowCodec<T>, fetchSize: Int): Operation.Streaming<T> =
        Operation.Streaming(underlying.streamingQuery(codec.underlying, fetchSize))

    fun <T : Any> streamingQuery(type: DbType<T>, fetchSize: Int): Operation.Streaming<T> =
        Operation.Streaming(underlying.streamingQuery(type.underlying, fetchSize))

    fun append(s: String): Fragment = Fragment(underlying.append(s))

    fun <T> value(dbType: DbType<T>, value: T): Fragment = Fragment(underlying.value(dbType.underlying, value))

    fun appendAll(fragments: List<Fragment>, separator: Fragment): Fragment =
        Fragment(underlying.appendAll(fragments.map { it.underlying }, separator.underlying))

    fun <T : Any> valueNullable(dbType: DbType<T>, value: T?): Fragment =
        Fragment(underlying.value(dbType.underlying.opt(), Optional.ofNullable(value)))

    fun <Row : Any> paramRow(parser: RowCodecNamed<Row>, vararg except: String): RowParamBuilder<Row> =
        RowParamBuilder(underlying.paramRow(parser.underlying, *except))

    fun <Row : Any> row(parser: RowCodecNamed<Row>, row: Row, vararg except: String): Fragment =
        Fragment(underlying.row(parser.underlying, row, *except))

    fun <P0> param(dbType: DbType<P0>): ParamBuilders.ParamBuilder1<P0> =
        ParamBuilders.ParamBuilder1(underlying.param(dbType.underlying))

    fun optionally(inner: Fragment): ParamBuilders.ParamBuilder1<Boolean> =
        ParamBuilders.ParamBuilder1(underlying.optionally(inner.underlying))

    @Suppress("UNCHECKED_CAST")
    fun <A : Any> optionally(builder: ParamBuilders.ParamBuilder1<A>): ParamBuilders.ParamBuilder1<A?> =
        ParamBuilders.ParamBuilder1(
            underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder1<A>),
            listOf(OptionallyTransforms.nullableToOptional))

    @Suppress("UNCHECKED_CAST")
    fun <A : Any, B : Any> optionally(builder: ParamBuilders.ParamBuilder2<A, B>): ParamBuilders.ParamBuilder1<Pair<A, B>?> =
        ParamBuilders.ParamBuilder1(
            underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder2<A, B>),
            listOf(OptionallyTransforms.pairToOptionalTuple2))

    @Suppress("UNCHECKED_CAST")
    fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilders.ParamBuilder3<A, B, C>): ParamBuilders.ParamBuilder1<Triple<A, B, C>?> =
        ParamBuilders.ParamBuilder1(
            underlying.optionally(builder.underlying as dev.typr.foundations.ParamBuilders.ParamBuilder3<A, B, C>),
            listOf(OptionallyTransforms.tripleToOptionalTuple3))

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

        @JvmStatic
        fun <Row : Any> insertInto(table: String, codec: RowCodecNamed<Row>, vararg except: String): RowTemplate.Update<Row> =
            RowTemplate.Update(dev.typr.foundations.Fragment.insertInto(table, codec.underlying, *except))

        @JvmStatic
        fun <Row : Any> insertIntoReturning(table: String, codec: RowCodecNamed<Row>, vararg except: String): RowTemplate.Query<Row, Row> =
            RowTemplate.Query(dev.typr.foundations.Fragment.insertIntoReturning(table, codec.underlying, *except))

        @JvmStatic
        fun <In : Any, Out : Any> insertIntoReturning(
            table: String, writeCodec: RowCodecNamed<In>, readCodec: RowCodecNamed<Out>
        ): RowTemplate.Query<In, Out> =
            RowTemplate.Query(dev.typr.foundations.Fragment.insertIntoReturning(
                table, writeCodec.underlying, readCodec.underlying))

        @JvmStatic
        @JvmName("rowStatic")
        fun <Row : Any> row(codec: RowCodecNamed<Row>, row: Row, vararg except: String): Fragment =
            Fragment(dev.typr.foundations.Fragment.EMPTY.row(codec.underlying, row, *except))
    }
}

@Suppress("UNCHECKED_CAST")
internal object OptionallyTransforms {
    val nullableToOptional: (Any?) -> Any? = { java.util.Optional.ofNullable(it) }

    val pairToOptionalTuple2: (Any?) -> Any? = { value ->
        if (value != null) {
            val p = value as Pair<Any?, Any?>
            java.util.Optional.of(dev.typr.foundations.Tuple.of(p.first, p.second))
        } else {
            java.util.Optional.empty<Any>()
        }
    }

    val tripleToOptionalTuple3: (Any?) -> Any? = { value ->
        if (value != null) {
            val t = value as Triple<Any?, Any?, Any?>
            java.util.Optional.of(dev.typr.foundations.Tuple.of(t.first, t.second, t.third))
        } else {
            java.util.Optional.empty<Any>()
        }
    }
}
