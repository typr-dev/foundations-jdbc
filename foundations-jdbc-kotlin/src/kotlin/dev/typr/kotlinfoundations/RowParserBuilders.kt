package dev.typr.kotlinfoundations

import dev.typr.foundations.DbType

/**
 * Type-safe builders for Kotlin RowParser.
 *
 * Usage:
 * ```kotlin
 * val parser: RowParser<Product> = RowParser.builder<Product>()
 *     .field(PgTypes.int4, Product::id)
 *     .field(PgTypes.text, Product::name)
 *     .field(PgTypes.numeric, Product::price)
 *     .build(::Product)
 * ```
 */
object RowParserBuilders {
    fun <Row : Any> builder(): Builder0<Row> = Builder0()

    class Builder0<Row : Any> internal constructor() {
        private val types = mutableListOf<DbType<*>>()
        private val getters = mutableListOf<(Row) -> Any?>()

        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder1<Row, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder1(types, getters)
        }
    }

    class Builder1<Row : Any, T0> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder2<Row, T0, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder2(types, getters)
        }
    }

    class Builder2<Row : Any, T0, T1> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder3<Row, T0, T1, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder3(types, getters)
        }
    }

    class Builder3<Row : Any, T0, T1, T2> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder4<Row, T0, T1, T2, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder4(types, getters)
        }
    }

    class Builder4<Row : Any, T0, T1, T2, T3> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder5<Row, T0, T1, T2, T3, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder5(types, getters)
        }
    }

    class Builder5<Row : Any, T0, T1, T2, T3, T4> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder6<Row, T0, T1, T2, T3, T4, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder6(types, getters)
        }
    }

    class Builder6<Row : Any, T0, T1, T2, T3, T4, T5> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder7<Row, T0, T1, T2, T3, T4, T5, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder7(types, getters)
        }
    }

    class Builder7<Row : Any, T0, T1, T2, T3, T4, T5, T6> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder8<Row, T0, T1, T2, T3, T4, T5, T6, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder8(types, getters)
        }
    }

    class Builder8<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder9<Row, T0, T1, T2, T3, T4, T5, T6, T7, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder9(types, getters)
        }
    }

    class Builder9<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder10<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder10(types, getters)
        }
    }

    class Builder10<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder11<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder11(types, getters)
        }
    }

    class Builder11<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder12<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder12(types, getters)
        }
    }

    class Builder12<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder13<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder13(types, getters)
        }
    }

    class Builder13<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder14<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder14(types, getters)
        }
    }

    class Builder14<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder15<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder15(types, getters)
        }
    }

    class Builder15<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder16<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder16(types, getters)
        }
    }

    class Builder16<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder17<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder17(types, getters)
        }
    }

    class Builder17<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder18<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder18(types, getters)
        }
    }

    class Builder18<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder19<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder19(types, getters)
        }
    }

    class Builder19<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder20<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder20(types, getters)
        }
    }

    class Builder20<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder21<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder21(types, getters)
        }
    }

    class Builder21<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder22<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder22(types, getters)
        }
    }

    class Builder22<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder23<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder23(types, getters)
        }
    }

    class Builder23<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder24<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder24(types, getters)
        }
    }

    class Builder24<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder25<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder25(types, getters)
        }
    }

    class Builder25<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder26<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder26(types, getters)
        }
    }

    class Builder26<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder27<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder27(types, getters)
        }
    }

    class Builder27<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder28<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder28(types, getters)
        }
    }

    class Builder28<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder29<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder29(types, getters)
        }
    }

    class Builder29<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder30<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder30(types, getters)
        }
    }

    class Builder30<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder31<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder31(types, getters)
        }
    }

    class Builder31<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder32<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder32(types, getters)
        }
    }

    class Builder32<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder33<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder33(types, getters)
        }
    }

    class Builder33<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder34<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder34(types, getters)
        }
    }

    class Builder34<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder35<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder35(types, getters)
        }
    }

    class Builder35<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder36<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder36(types, getters)
        }
    }

    class Builder36<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder37<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder37(types, getters)
        }
    }

    class Builder37<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder38<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder38(types, getters)
        }
    }

    class Builder38<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder39<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder39(types, getters)
        }
    }

    class Builder39<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder40<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder40(types, getters)
        }
    }

    class Builder40<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder41<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder41(types, getters)
        }
    }

    class Builder41<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder42<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder42(types, getters)
        }
    }

    class Builder42<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder43<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder43(types, getters)
        }
    }

    class Builder43<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder44<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder44(types, getters)
        }
    }

    class Builder44<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder45<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder45(types, getters)
        }
    }

    class Builder45<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder46<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder46(types, getters)
        }
    }

    class Builder46<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder47<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder47(types, getters)
        }
    }

    class Builder47<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder48<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder48(types, getters)
        }
    }

    class Builder48<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder49<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder49(types, getters)
        }
    }

    class Builder49<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder50<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder50(types, getters)
        }
    }

    class Builder50<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder51<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder51(types, getters)
        }
    }

    class Builder51<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder52<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder52(types, getters)
        }
    }

    class Builder52<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder53<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder53(types, getters)
        }
    }

    class Builder53<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder54<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder54(types, getters)
        }
    }

    class Builder54<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder55<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder55(types, getters)
        }
    }

    class Builder55<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder56<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder56(types, getters)
        }
    }

    class Builder56<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder57<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder57(types, getters)
        }
    }

    class Builder57<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder58<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder58(types, getters)
        }
    }

    class Builder58<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder59<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder59(types, getters)
        }
    }

    class Builder59<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder60<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder60(types, getters)
        }
    }

    class Builder60<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder61<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder61(types, getters)
        }
    }

    class Builder61<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder62<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder62(types, getters)
        }
    }

    class Builder62<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder63<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder63(types, getters)
        }
    }

    class Builder63<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder64<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder64(types, getters)
        }
    }

    class Builder64<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder65<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder65(types, getters)
        }
    }

    class Builder65<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder66<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder66(types, getters)
        }
    }

    class Builder66<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder67<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder67(types, getters)
        }
    }

    class Builder67<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder68<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder68(types, getters)
        }
    }

    class Builder68<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder69<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder69(types, getters)
        }
    }

    class Builder69<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder70<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder70(types, getters)
        }
    }

    class Builder70<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder71<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder71(types, getters)
        }
    }

    class Builder71<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder72<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder72(types, getters)
        }
    }

    class Builder72<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder73<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder73(types, getters)
        }
    }

    class Builder73<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder74<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder74(types, getters)
        }
    }

    class Builder74<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder75<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder75(types, getters)
        }
    }

    class Builder75<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder76<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder76(types, getters)
        }
    }

    class Builder76<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder77<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder77(types, getters)
        }
    }

    class Builder77<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder78<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder78(types, getters)
        }
    }

    class Builder78<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder79<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder79(types, getters)
        }
    }

    class Builder79<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder80<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder80(types, getters)
        }
    }

    class Builder80<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder81<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder81(types, getters)
        }
    }

    class Builder81<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder82<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder82(types, getters)
        }
    }

    class Builder82<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder83<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder83(types, getters)
        }
    }

    class Builder83<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder84<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder84(types, getters)
        }
    }

    class Builder84<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder85<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder85(types, getters)
        }
    }

    class Builder85<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder86<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder86(types, getters)
        }
    }

    class Builder86<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder87<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder87(types, getters)
        }
    }

    class Builder87<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder88<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder88(types, getters)
        }
    }

    class Builder88<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder89<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder89(types, getters)
        }
    }

    class Builder89<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder90<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder90(types, getters)
        }
    }

    class Builder90<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder91<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder91(types, getters)
        }
    }

    class Builder91<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder92<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder92(types, getters)
        }
    }

    class Builder92<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder93<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder93(types, getters)
        }
    }

    class Builder93<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder94<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder94(types, getters)
        }
    }

    class Builder94<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder95<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder95(types, getters)
        }
    }

    class Builder95<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93, arr[94] as T94) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder96<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder96(types, getters)
        }
    }

    class Builder96<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93, arr[94] as T94, arr[95] as T95) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder97<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder97(types, getters)
        }
    }

    class Builder97<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93, arr[94] as T94, arr[95] as T95, arr[96] as T96) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder98<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder98(types, getters)
        }
    }

    class Builder98<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93, arr[94] as T94, arr[95] as T95, arr[96] as T96, arr[97] as T97) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder99<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F> {
            types.add(type)
            @Suppress("UNCHECKED_CAST")
            getters.add(getter as (Row) -> Any?)
            return Builder99(types, getters)
        }
    }

    class Builder99<Row : Any, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> internal constructor(
        private val types: MutableList<DbType<*>>,
        private val getters: MutableList<(Row) -> Any?>
    ) {
        @Suppress("UNCHECKED_CAST")
        fun build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98) -> Row): RowParser<Row> {
            val capturedGetters = getters.toList()
            val javaParser = dev.typr.foundations.RowParser<Row>(
                types.toList(),
                { arr -> decode(arr[0] as T0, arr[1] as T1, arr[2] as T2, arr[3] as T3, arr[4] as T4, arr[5] as T5, arr[6] as T6, arr[7] as T7, arr[8] as T8, arr[9] as T9, arr[10] as T10, arr[11] as T11, arr[12] as T12, arr[13] as T13, arr[14] as T14, arr[15] as T15, arr[16] as T16, arr[17] as T17, arr[18] as T18, arr[19] as T19, arr[20] as T20, arr[21] as T21, arr[22] as T22, arr[23] as T23, arr[24] as T24, arr[25] as T25, arr[26] as T26, arr[27] as T27, arr[28] as T28, arr[29] as T29, arr[30] as T30, arr[31] as T31, arr[32] as T32, arr[33] as T33, arr[34] as T34, arr[35] as T35, arr[36] as T36, arr[37] as T37, arr[38] as T38, arr[39] as T39, arr[40] as T40, arr[41] as T41, arr[42] as T42, arr[43] as T43, arr[44] as T44, arr[45] as T45, arr[46] as T46, arr[47] as T47, arr[48] as T48, arr[49] as T49, arr[50] as T50, arr[51] as T51, arr[52] as T52, arr[53] as T53, arr[54] as T54, arr[55] as T55, arr[56] as T56, arr[57] as T57, arr[58] as T58, arr[59] as T59, arr[60] as T60, arr[61] as T61, arr[62] as T62, arr[63] as T63, arr[64] as T64, arr[65] as T65, arr[66] as T66, arr[67] as T67, arr[68] as T68, arr[69] as T69, arr[70] as T70, arr[71] as T71, arr[72] as T72, arr[73] as T73, arr[74] as T74, arr[75] as T75, arr[76] as T76, arr[77] as T77, arr[78] as T78, arr[79] as T79, arr[80] as T80, arr[81] as T81, arr[82] as T82, arr[83] as T83, arr[84] as T84, arr[85] as T85, arr[86] as T86, arr[87] as T87, arr[88] as T88, arr[89] as T89, arr[90] as T90, arr[91] as T91, arr[92] as T92, arr[93] as T93, arr[94] as T94, arr[95] as T95, arr[96] as T96, arr[97] as T97, arr[98] as T98) },
                { row -> capturedGetters.map { it(row) }.toTypedArray() }
            )
            return RowParser(javaParser)
        }
    }
}
