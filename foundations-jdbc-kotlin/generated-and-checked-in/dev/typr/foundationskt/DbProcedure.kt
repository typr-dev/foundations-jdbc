package dev.typr.foundationskt

/**
 * Type-safe stored procedure definitions with fully typed inputs and outputs.
 *
 * Usage:
 * ```kotlin
 * val getUser: DbProcedure.Def1_2<Int, String, String> = DbProcedure.define("get_user_by_id")
 *     .`in`(PgTypes.int4)
 *     .out(PgTypes.text)
 *     .out(PgTypes.text)
 *     .build()
 * val result = getUser.call(42).transact(tx)  // Int enforced!
 * ```
 *
 * @see DbFunction for stored functions (single return value via SELECT)
 */
object DbProcedure {

    /** Start defining a stored procedure. */
    fun define(name: String): Builder_0_0 =
        Builder_0_0(dev.typr.foundations.DbProcedure.define(name))

    // ─────────────────────────────────────────────────────────────────────────────
    // Procedure definition interfaces (121 total: 11×11 matrix of input×output arities)
    // ─────────────────────────────────────────────────────────────────────────────

    /** Procedure definition with 0 input(s) and 0 output(s). */
    fun interface Def0_0 {
        fun call(): ProcedureOp<Unit>
    }

    /** Procedure definition with 0 input(s) and 1 output(s). */
    fun interface Def0_1<O0> {
        fun call(): ProcedureOp<O0>
    }

    /** Procedure definition with 0 input(s) and 2 output(s). */
    fun interface Def0_2<O0, O1> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 0 input(s) and 3 output(s). */
    fun interface Def0_3<O0, O1, O2> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 0 input(s) and 4 output(s). */
    fun interface Def0_4<O0, O1, O2, O3> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 0 input(s) and 5 output(s). */
    fun interface Def0_5<O0, O1, O2, O3, O4> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 0 input(s) and 6 output(s). */
    fun interface Def0_6<O0, O1, O2, O3, O4, O5> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 0 input(s) and 7 output(s). */
    fun interface Def0_7<O0, O1, O2, O3, O4, O5, O6> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 0 input(s) and 8 output(s). */
    fun interface Def0_8<O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 0 input(s) and 9 output(s). */
    fun interface Def0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 0 input(s) and 10 output(s). */
    fun interface Def0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 1 input(s) and 0 output(s). */
    fun interface Def1_0<I0> {
        fun call(i0: I0): ProcedureOp<Unit>
    }

    /** Procedure definition with 1 input(s) and 1 output(s). */
    fun interface Def1_1<I0, O0> {
        fun call(i0: I0): ProcedureOp<O0>
    }

    /** Procedure definition with 1 input(s) and 2 output(s). */
    fun interface Def1_2<I0, O0, O1> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 1 input(s) and 3 output(s). */
    fun interface Def1_3<I0, O0, O1, O2> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 1 input(s) and 4 output(s). */
    fun interface Def1_4<I0, O0, O1, O2, O3> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 1 input(s) and 5 output(s). */
    fun interface Def1_5<I0, O0, O1, O2, O3, O4> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 1 input(s) and 6 output(s). */
    fun interface Def1_6<I0, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 1 input(s) and 7 output(s). */
    fun interface Def1_7<I0, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 1 input(s) and 8 output(s). */
    fun interface Def1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 1 input(s) and 9 output(s). */
    fun interface Def1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 1 input(s) and 10 output(s). */
    fun interface Def1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 2 input(s) and 0 output(s). */
    fun interface Def2_0<I0, I1> {
        fun call(i0: I0, i1: I1): ProcedureOp<Unit>
    }

    /** Procedure definition with 2 input(s) and 1 output(s). */
    fun interface Def2_1<I0, I1, O0> {
        fun call(i0: I0, i1: I1): ProcedureOp<O0>
    }

    /** Procedure definition with 2 input(s) and 2 output(s). */
    fun interface Def2_2<I0, I1, O0, O1> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 2 input(s) and 3 output(s). */
    fun interface Def2_3<I0, I1, O0, O1, O2> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 2 input(s) and 4 output(s). */
    fun interface Def2_4<I0, I1, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 2 input(s) and 5 output(s). */
    fun interface Def2_5<I0, I1, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 2 input(s) and 6 output(s). */
    fun interface Def2_6<I0, I1, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 2 input(s) and 7 output(s). */
    fun interface Def2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 2 input(s) and 8 output(s). */
    fun interface Def2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 2 input(s) and 9 output(s). */
    fun interface Def2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 2 input(s) and 10 output(s). */
    fun interface Def2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 3 input(s) and 0 output(s). */
    fun interface Def3_0<I0, I1, I2> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<Unit>
    }

    /** Procedure definition with 3 input(s) and 1 output(s). */
    fun interface Def3_1<I0, I1, I2, O0> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<O0>
    }

    /** Procedure definition with 3 input(s) and 2 output(s). */
    fun interface Def3_2<I0, I1, I2, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 3 input(s) and 3 output(s). */
    fun interface Def3_3<I0, I1, I2, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 3 input(s) and 4 output(s). */
    fun interface Def3_4<I0, I1, I2, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 3 input(s) and 5 output(s). */
    fun interface Def3_5<I0, I1, I2, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 3 input(s) and 6 output(s). */
    fun interface Def3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 3 input(s) and 7 output(s). */
    fun interface Def3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 3 input(s) and 8 output(s). */
    fun interface Def3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 3 input(s) and 9 output(s). */
    fun interface Def3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 3 input(s) and 10 output(s). */
    fun interface Def3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 4 input(s) and 0 output(s). */
    fun interface Def4_0<I0, I1, I2, I3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<Unit>
    }

    /** Procedure definition with 4 input(s) and 1 output(s). */
    fun interface Def4_1<I0, I1, I2, I3, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<O0>
    }

    /** Procedure definition with 4 input(s) and 2 output(s). */
    fun interface Def4_2<I0, I1, I2, I3, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 4 input(s) and 3 output(s). */
    fun interface Def4_3<I0, I1, I2, I3, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 4 input(s) and 4 output(s). */
    fun interface Def4_4<I0, I1, I2, I3, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 4 input(s) and 5 output(s). */
    fun interface Def4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 4 input(s) and 6 output(s). */
    fun interface Def4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 4 input(s) and 7 output(s). */
    fun interface Def4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 4 input(s) and 8 output(s). */
    fun interface Def4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 4 input(s) and 9 output(s). */
    fun interface Def4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 4 input(s) and 10 output(s). */
    fun interface Def4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 5 input(s) and 0 output(s). */
    fun interface Def5_0<I0, I1, I2, I3, I4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<Unit>
    }

    /** Procedure definition with 5 input(s) and 1 output(s). */
    fun interface Def5_1<I0, I1, I2, I3, I4, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<O0>
    }

    /** Procedure definition with 5 input(s) and 2 output(s). */
    fun interface Def5_2<I0, I1, I2, I3, I4, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 5 input(s) and 3 output(s). */
    fun interface Def5_3<I0, I1, I2, I3, I4, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 5 input(s) and 4 output(s). */
    fun interface Def5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 5 input(s) and 5 output(s). */
    fun interface Def5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 5 input(s) and 6 output(s). */
    fun interface Def5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 5 input(s) and 7 output(s). */
    fun interface Def5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 5 input(s) and 8 output(s). */
    fun interface Def5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 5 input(s) and 9 output(s). */
    fun interface Def5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 5 input(s) and 10 output(s). */
    fun interface Def5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 6 input(s) and 0 output(s). */
    fun interface Def6_0<I0, I1, I2, I3, I4, I5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<Unit>
    }

    /** Procedure definition with 6 input(s) and 1 output(s). */
    fun interface Def6_1<I0, I1, I2, I3, I4, I5, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<O0>
    }

    /** Procedure definition with 6 input(s) and 2 output(s). */
    fun interface Def6_2<I0, I1, I2, I3, I4, I5, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 6 input(s) and 3 output(s). */
    fun interface Def6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 6 input(s) and 4 output(s). */
    fun interface Def6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 6 input(s) and 5 output(s). */
    fun interface Def6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 6 input(s) and 6 output(s). */
    fun interface Def6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 6 input(s) and 7 output(s). */
    fun interface Def6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 6 input(s) and 8 output(s). */
    fun interface Def6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 6 input(s) and 9 output(s). */
    fun interface Def6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 6 input(s) and 10 output(s). */
    fun interface Def6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 7 input(s) and 0 output(s). */
    fun interface Def7_0<I0, I1, I2, I3, I4, I5, I6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<Unit>
    }

    /** Procedure definition with 7 input(s) and 1 output(s). */
    fun interface Def7_1<I0, I1, I2, I3, I4, I5, I6, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<O0>
    }

    /** Procedure definition with 7 input(s) and 2 output(s). */
    fun interface Def7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 7 input(s) and 3 output(s). */
    fun interface Def7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 7 input(s) and 4 output(s). */
    fun interface Def7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 7 input(s) and 5 output(s). */
    fun interface Def7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 7 input(s) and 6 output(s). */
    fun interface Def7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 7 input(s) and 7 output(s). */
    fun interface Def7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 7 input(s) and 8 output(s). */
    fun interface Def7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 7 input(s) and 9 output(s). */
    fun interface Def7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 7 input(s) and 10 output(s). */
    fun interface Def7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 8 input(s) and 0 output(s). */
    fun interface Def8_0<I0, I1, I2, I3, I4, I5, I6, I7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<Unit>
    }

    /** Procedure definition with 8 input(s) and 1 output(s). */
    fun interface Def8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<O0>
    }

    /** Procedure definition with 8 input(s) and 2 output(s). */
    fun interface Def8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 8 input(s) and 3 output(s). */
    fun interface Def8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 8 input(s) and 4 output(s). */
    fun interface Def8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 8 input(s) and 5 output(s). */
    fun interface Def8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 8 input(s) and 6 output(s). */
    fun interface Def8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 8 input(s) and 7 output(s). */
    fun interface Def8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 8 input(s) and 8 output(s). */
    fun interface Def8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 8 input(s) and 9 output(s). */
    fun interface Def8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 8 input(s) and 10 output(s). */
    fun interface Def8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 9 input(s) and 0 output(s). */
    fun interface Def9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<Unit>
    }

    /** Procedure definition with 9 input(s) and 1 output(s). */
    fun interface Def9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<O0>
    }

    /** Procedure definition with 9 input(s) and 2 output(s). */
    fun interface Def9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 9 input(s) and 3 output(s). */
    fun interface Def9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 9 input(s) and 4 output(s). */
    fun interface Def9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 9 input(s) and 5 output(s). */
    fun interface Def9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 9 input(s) and 6 output(s). */
    fun interface Def9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 9 input(s) and 7 output(s). */
    fun interface Def9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 9 input(s) and 8 output(s). */
    fun interface Def9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 9 input(s) and 9 output(s). */
    fun interface Def9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 9 input(s) and 10 output(s). */
    fun interface Def9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    /** Procedure definition with 10 input(s) and 0 output(s). */
    fun interface Def10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<Unit>
    }

    /** Procedure definition with 10 input(s) and 1 output(s). */
    fun interface Def10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<O0>
    }

    /** Procedure definition with 10 input(s) and 2 output(s). */
    fun interface Def10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple2<O0, O1>>
    }

    /** Procedure definition with 10 input(s) and 3 output(s). */
    fun interface Def10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple3<O0, O1, O2>>
    }

    /** Procedure definition with 10 input(s) and 4 output(s). */
    fun interface Def10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3>>
    }

    /** Procedure definition with 10 input(s) and 5 output(s). */
    fun interface Def10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4>>
    }

    /** Procedure definition with 10 input(s) and 6 output(s). */
    fun interface Def10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5>>
    }

    /** Procedure definition with 10 input(s) and 7 output(s). */
    fun interface Def10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>>
    }

    /** Procedure definition with 10 input(s) and 8 output(s). */
    fun interface Def10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>>
    }

    /** Procedure definition with 10 input(s) and 9 output(s). */
    fun interface Def10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>>
    }

    /** Procedure definition with 10 input(s) and 10 output(s). */
    fun interface Def10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        fun call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp<dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>>
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Procedure builders (121 total: 11×11 matrix)
    // ─────────────────────────────────────────────────────────────────────────────

    class Builder_0_0 internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_0
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_0<I0> =
            Builder_1_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_0_1<O0> =
            Builder_0_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_1<X, X> =
            Builder_1_1(underlying.inout(type.underlying))

        fun build(): Def0_0 {
            val javaProc = underlying.build()
            return Def0_0 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_0_1<O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_1<O0>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_1<I0, O0> =
            Builder_1_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_0_2<O0, O1> =
            Builder_0_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_2<X, O0, X> =
            Builder_1_2(underlying.inout(type.underlying))

        fun build(): Def0_1<O0> {
            val javaProc = underlying.build()
            return Def0_1 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_0_2<O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_2<O0, O1>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_2<I0, O0, O1> =
            Builder_1_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_0_3<O0, O1, O2> =
            Builder_0_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_3<X, O0, O1, X> =
            Builder_1_3(underlying.inout(type.underlying))

        fun build(): Def0_2<O0, O1> {
            val javaProc = underlying.build()
            return Def0_2 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_0_3<O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_3<O0, O1, O2>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_3<I0, O0, O1, O2> =
            Builder_1_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_0_4<O0, O1, O2, O3> =
            Builder_0_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_4<X, O0, O1, O2, X> =
            Builder_1_4(underlying.inout(type.underlying))

        fun build(): Def0_3<O0, O1, O2> {
            val javaProc = underlying.build()
            return Def0_3 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_0_4<O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_4<O0, O1, O2, O3>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_4<I0, O0, O1, O2, O3> =
            Builder_1_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_0_5<O0, O1, O2, O3, O4> =
            Builder_0_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_5<X, O0, O1, O2, O3, X> =
            Builder_1_5(underlying.inout(type.underlying))

        fun build(): Def0_4<O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def0_4 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_0_5<O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_5<O0, O1, O2, O3, O4>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_5<I0, O0, O1, O2, O3, O4> =
            Builder_1_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_0_6<O0, O1, O2, O3, O4, O5> =
            Builder_0_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_6<X, O0, O1, O2, O3, O4, X> =
            Builder_1_6(underlying.inout(type.underlying))

        fun build(): Def0_5<O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def0_5 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_0_6<O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_6<O0, O1, O2, O3, O4, O5>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_6<I0, O0, O1, O2, O3, O4, O5> =
            Builder_1_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_0_7<O0, O1, O2, O3, O4, O5, O6> =
            Builder_0_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_7<X, O0, O1, O2, O3, O4, O5, X> =
            Builder_1_7(underlying.inout(type.underlying))

        fun build(): Def0_6<O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def0_6 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_0_7<O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_7<O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> =
            Builder_1_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_0_8<O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_0_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_8<X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_1_8(underlying.inout(type.underlying))

        fun build(): Def0_7<O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def0_7 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_0_8<O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_8<O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_1_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_0_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_9<X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_1_9(underlying.inout(type.underlying))

        fun build(): Def0_8<O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def0_8 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_1_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_0_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_1_10<X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_1_10(underlying.inout(type.underlying))

        fun build(): Def0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def0_9 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I0> `in`(type: DbType<I0>): Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_1_10(underlying.`in`(type.underlying))

        fun build(): Def0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def0_10 { ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call() as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_1_0<I0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_0<I0>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_0<I0, I1> =
            Builder_2_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_1_1<I0, O0> =
            Builder_1_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_1<I0, X, X> =
            Builder_2_1(underlying.inout(type.underlying))

        fun build(): Def1_0<I0> {
            val javaProc = underlying.build()
            return Def1_0 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_1_1<I0, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_1<I0, O0>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_1<I0, I1, O0> =
            Builder_2_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_1_2<I0, O0, O1> =
            Builder_1_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_2<I0, X, O0, X> =
            Builder_2_2(underlying.inout(type.underlying))

        fun build(): Def1_1<I0, O0> {
            val javaProc = underlying.build()
            return Def1_1 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_1_2<I0, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_2<I0, O0, O1>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_2<I0, I1, O0, O1> =
            Builder_2_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_1_3<I0, O0, O1, O2> =
            Builder_1_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_3<I0, X, O0, O1, X> =
            Builder_2_3(underlying.inout(type.underlying))

        fun build(): Def1_2<I0, O0, O1> {
            val javaProc = underlying.build()
            return Def1_2 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_1_3<I0, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_3<I0, O0, O1, O2>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_3<I0, I1, O0, O1, O2> =
            Builder_2_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_1_4<I0, O0, O1, O2, O3> =
            Builder_1_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_4<I0, X, O0, O1, O2, X> =
            Builder_2_4(underlying.inout(type.underlying))

        fun build(): Def1_3<I0, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def1_3 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_1_4<I0, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_4<I0, O0, O1, O2, O3>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_4<I0, I1, O0, O1, O2, O3> =
            Builder_2_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_1_5<I0, O0, O1, O2, O3, O4> =
            Builder_1_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_5<I0, X, O0, O1, O2, O3, X> =
            Builder_2_5(underlying.inout(type.underlying))

        fun build(): Def1_4<I0, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def1_4 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_1_5<I0, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_5<I0, O0, O1, O2, O3, O4>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_5<I0, I1, O0, O1, O2, O3, O4> =
            Builder_2_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_1_6<I0, O0, O1, O2, O3, O4, O5> =
            Builder_1_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_6<I0, X, O0, O1, O2, O3, O4, X> =
            Builder_2_6(underlying.inout(type.underlying))

        fun build(): Def1_5<I0, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def1_5 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_1_6<I0, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_6<I0, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> =
            Builder_2_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> =
            Builder_1_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_7<I0, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_2_7(underlying.inout(type.underlying))

        fun build(): Def1_6<I0, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def1_6 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> =
            Builder_2_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_1_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_8<I0, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_2_8(underlying.inout(type.underlying))

        fun build(): Def1_7<I0, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def1_7 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_2_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_1_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_9<I0, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_2_9(underlying.inout(type.underlying))

        fun build(): Def1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def1_8 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_2_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_1_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_2_10<I0, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_2_10(underlying.inout(type.underlying))

        fun build(): Def1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def1_9 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I1> `in`(type: DbType<I1>): Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_2_10(underlying.`in`(type.underlying))

        fun build(): Def1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def1_10 { i0: I0 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_2_0<I0, I1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_0<I0, I1>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_0<I0, I1, I2> =
            Builder_3_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_2_1<I0, I1, O0> =
            Builder_2_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_1<I0, I1, X, X> =
            Builder_3_1(underlying.inout(type.underlying))

        fun build(): Def2_0<I0, I1> {
            val javaProc = underlying.build()
            return Def2_0 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_2_1<I0, I1, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_1<I0, I1, O0>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_1<I0, I1, I2, O0> =
            Builder_3_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_2_2<I0, I1, O0, O1> =
            Builder_2_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_2<I0, I1, X, O0, X> =
            Builder_3_2(underlying.inout(type.underlying))

        fun build(): Def2_1<I0, I1, O0> {
            val javaProc = underlying.build()
            return Def2_1 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_2_2<I0, I1, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_2<I0, I1, O0, O1>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_2<I0, I1, I2, O0, O1> =
            Builder_3_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_2_3<I0, I1, O0, O1, O2> =
            Builder_2_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_3<I0, I1, X, O0, O1, X> =
            Builder_3_3(underlying.inout(type.underlying))

        fun build(): Def2_2<I0, I1, O0, O1> {
            val javaProc = underlying.build()
            return Def2_2 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_2_3<I0, I1, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_3<I0, I1, O0, O1, O2>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_3<I0, I1, I2, O0, O1, O2> =
            Builder_3_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_2_4<I0, I1, O0, O1, O2, O3> =
            Builder_2_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_4<I0, I1, X, O0, O1, O2, X> =
            Builder_3_4(underlying.inout(type.underlying))

        fun build(): Def2_3<I0, I1, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def2_3 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_2_4<I0, I1, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_4<I0, I1, O0, O1, O2, O3>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_4<I0, I1, I2, O0, O1, O2, O3> =
            Builder_3_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_2_5<I0, I1, O0, O1, O2, O3, O4> =
            Builder_2_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_5<I0, I1, X, O0, O1, O2, O3, X> =
            Builder_3_5(underlying.inout(type.underlying))

        fun build(): Def2_4<I0, I1, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def2_4 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_2_5<I0, I1, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_5<I0, I1, O0, O1, O2, O3, O4>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> =
            Builder_3_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> =
            Builder_2_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_6<I0, I1, X, O0, O1, O2, O3, O4, X> =
            Builder_3_6(underlying.inout(type.underlying))

        fun build(): Def2_5<I0, I1, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def2_5 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> =
            Builder_3_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> =
            Builder_2_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_7<I0, I1, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_3_7(underlying.inout(type.underlying))

        fun build(): Def2_6<I0, I1, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def2_6 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> =
            Builder_3_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_2_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_8<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_3_8(underlying.inout(type.underlying))

        fun build(): Def2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def2_7 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_3_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_2_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_9<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_3_9(underlying.inout(type.underlying))

        fun build(): Def2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def2_8 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_3_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_2_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_3_10<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_3_10(underlying.inout(type.underlying))

        fun build(): Def2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def2_9 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I2> `in`(type: DbType<I2>): Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_3_10(underlying.`in`(type.underlying))

        fun build(): Def2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def2_10 { i0: I0, i1: I1 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_3_0<I0, I1, I2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_0<I0, I1, I2>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_0<I0, I1, I2, I3> =
            Builder_4_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_3_1<I0, I1, I2, O0> =
            Builder_3_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_1<I0, I1, I2, X, X> =
            Builder_4_1(underlying.inout(type.underlying))

        fun build(): Def3_0<I0, I1, I2> {
            val javaProc = underlying.build()
            return Def3_0 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_3_1<I0, I1, I2, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_1<I0, I1, I2, O0>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_1<I0, I1, I2, I3, O0> =
            Builder_4_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_3_2<I0, I1, I2, O0, O1> =
            Builder_3_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_2<I0, I1, I2, X, O0, X> =
            Builder_4_2(underlying.inout(type.underlying))

        fun build(): Def3_1<I0, I1, I2, O0> {
            val javaProc = underlying.build()
            return Def3_1 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_3_2<I0, I1, I2, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_2<I0, I1, I2, O0, O1>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_2<I0, I1, I2, I3, O0, O1> =
            Builder_4_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_3_3<I0, I1, I2, O0, O1, O2> =
            Builder_3_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_3<I0, I1, I2, X, O0, O1, X> =
            Builder_4_3(underlying.inout(type.underlying))

        fun build(): Def3_2<I0, I1, I2, O0, O1> {
            val javaProc = underlying.build()
            return Def3_2 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_3_3<I0, I1, I2, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_3<I0, I1, I2, O0, O1, O2>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_3<I0, I1, I2, I3, O0, O1, O2> =
            Builder_4_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_3_4<I0, I1, I2, O0, O1, O2, O3> =
            Builder_3_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_4<I0, I1, I2, X, O0, O1, O2, X> =
            Builder_4_4(underlying.inout(type.underlying))

        fun build(): Def3_3<I0, I1, I2, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def3_3 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_3_4<I0, I1, I2, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_4<I0, I1, I2, O0, O1, O2, O3>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> =
            Builder_4_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> =
            Builder_3_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_5<I0, I1, I2, X, O0, O1, O2, O3, X> =
            Builder_4_5(underlying.inout(type.underlying))

        fun build(): Def3_4<I0, I1, I2, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def3_4 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> =
            Builder_4_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> =
            Builder_3_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_6<I0, I1, I2, X, O0, O1, O2, O3, O4, X> =
            Builder_4_6(underlying.inout(type.underlying))

        fun build(): Def3_5<I0, I1, I2, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def3_5 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> =
            Builder_4_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> =
            Builder_3_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_7<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_4_7(underlying.inout(type.underlying))

        fun build(): Def3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def3_6 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> =
            Builder_4_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_3_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_8<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_4_8(underlying.inout(type.underlying))

        fun build(): Def3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def3_7 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_4_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_3_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_9<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_4_9(underlying.inout(type.underlying))

        fun build(): Def3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def3_8 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_4_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_3_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_4_10<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_4_10(underlying.inout(type.underlying))

        fun build(): Def3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def3_9 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I3> `in`(type: DbType<I3>): Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_4_10(underlying.`in`(type.underlying))

        fun build(): Def3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def3_10 { i0: I0, i1: I1, i2: I2 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_4_0<I0, I1, I2, I3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_0<I0, I1, I2, I3>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_0<I0, I1, I2, I3, I4> =
            Builder_5_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_4_1<I0, I1, I2, I3, O0> =
            Builder_4_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_1<I0, I1, I2, I3, X, X> =
            Builder_5_1(underlying.inout(type.underlying))

        fun build(): Def4_0<I0, I1, I2, I3> {
            val javaProc = underlying.build()
            return Def4_0 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_4_1<I0, I1, I2, I3, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_1<I0, I1, I2, I3, O0>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_1<I0, I1, I2, I3, I4, O0> =
            Builder_5_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_4_2<I0, I1, I2, I3, O0, O1> =
            Builder_4_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_2<I0, I1, I2, I3, X, O0, X> =
            Builder_5_2(underlying.inout(type.underlying))

        fun build(): Def4_1<I0, I1, I2, I3, O0> {
            val javaProc = underlying.build()
            return Def4_1 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_4_2<I0, I1, I2, I3, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_2<I0, I1, I2, I3, O0, O1>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_2<I0, I1, I2, I3, I4, O0, O1> =
            Builder_5_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_4_3<I0, I1, I2, I3, O0, O1, O2> =
            Builder_4_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_3<I0, I1, I2, I3, X, O0, O1, X> =
            Builder_5_3(underlying.inout(type.underlying))

        fun build(): Def4_2<I0, I1, I2, I3, O0, O1> {
            val javaProc = underlying.build()
            return Def4_2 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_4_3<I0, I1, I2, I3, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_3<I0, I1, I2, I3, O0, O1, O2>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> =
            Builder_5_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> =
            Builder_4_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_4<I0, I1, I2, I3, X, O0, O1, O2, X> =
            Builder_5_4(underlying.inout(type.underlying))

        fun build(): Def4_3<I0, I1, I2, I3, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def4_3 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> =
            Builder_5_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> =
            Builder_4_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_5<I0, I1, I2, I3, X, O0, O1, O2, O3, X> =
            Builder_5_5(underlying.inout(type.underlying))

        fun build(): Def4_4<I0, I1, I2, I3, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def4_4 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> =
            Builder_5_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> =
            Builder_4_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_6<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, X> =
            Builder_5_6(underlying.inout(type.underlying))

        fun build(): Def4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def4_5 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> =
            Builder_5_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> =
            Builder_4_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_7<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_5_7(underlying.inout(type.underlying))

        fun build(): Def4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def4_6 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> =
            Builder_5_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_4_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_8<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_5_8(underlying.inout(type.underlying))

        fun build(): Def4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def4_7 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_5_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_4_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_9<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_5_9(underlying.inout(type.underlying))

        fun build(): Def4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def4_8 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_5_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_4_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_5_10<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_5_10(underlying.inout(type.underlying))

        fun build(): Def4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def4_9 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I4> `in`(type: DbType<I4>): Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_5_10(underlying.`in`(type.underlying))

        fun build(): Def4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def4_10 { i0: I0, i1: I1, i2: I2, i3: I3 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_5_0<I0, I1, I2, I3, I4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_0<I0, I1, I2, I3, I4>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_0<I0, I1, I2, I3, I4, I5> =
            Builder_6_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_5_1<I0, I1, I2, I3, I4, O0> =
            Builder_5_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_1<I0, I1, I2, I3, I4, X, X> =
            Builder_6_1(underlying.inout(type.underlying))

        fun build(): Def5_0<I0, I1, I2, I3, I4> {
            val javaProc = underlying.build()
            return Def5_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_5_1<I0, I1, I2, I3, I4, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_1<I0, I1, I2, I3, I4, O0>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_1<I0, I1, I2, I3, I4, I5, O0> =
            Builder_6_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_5_2<I0, I1, I2, I3, I4, O0, O1> =
            Builder_5_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_2<I0, I1, I2, I3, I4, X, O0, X> =
            Builder_6_2(underlying.inout(type.underlying))

        fun build(): Def5_1<I0, I1, I2, I3, I4, O0> {
            val javaProc = underlying.build()
            return Def5_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_5_2<I0, I1, I2, I3, I4, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_2<I0, I1, I2, I3, I4, O0, O1>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> =
            Builder_6_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> =
            Builder_5_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_3<I0, I1, I2, I3, I4, X, O0, O1, X> =
            Builder_6_3(underlying.inout(type.underlying))

        fun build(): Def5_2<I0, I1, I2, I3, I4, O0, O1> {
            val javaProc = underlying.build()
            return Def5_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> =
            Builder_6_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> =
            Builder_5_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_4<I0, I1, I2, I3, I4, X, O0, O1, O2, X> =
            Builder_6_4(underlying.inout(type.underlying))

        fun build(): Def5_3<I0, I1, I2, I3, I4, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def5_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> =
            Builder_6_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> =
            Builder_5_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_5<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, X> =
            Builder_6_5(underlying.inout(type.underlying))

        fun build(): Def5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def5_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> =
            Builder_6_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> =
            Builder_5_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_6<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, X> =
            Builder_6_6(underlying.inout(type.underlying))

        fun build(): Def5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def5_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> =
            Builder_6_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> =
            Builder_5_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_7<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_6_7(underlying.inout(type.underlying))

        fun build(): Def5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def5_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> =
            Builder_6_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_5_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_8<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_6_8(underlying.inout(type.underlying))

        fun build(): Def5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def5_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_6_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_5_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_9<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_6_9(underlying.inout(type.underlying))

        fun build(): Def5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def5_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_6_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_5_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_6_10<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_6_10(underlying.inout(type.underlying))

        fun build(): Def5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def5_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I5> `in`(type: DbType<I5>): Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_6_10(underlying.`in`(type.underlying))

        fun build(): Def5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def5_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_6_0<I0, I1, I2, I3, I4, I5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_0<I0, I1, I2, I3, I4, I5>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_0<I0, I1, I2, I3, I4, I5, I6> =
            Builder_7_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_6_1<I0, I1, I2, I3, I4, I5, O0> =
            Builder_6_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_1<I0, I1, I2, I3, I4, I5, X, X> =
            Builder_7_1(underlying.inout(type.underlying))

        fun build(): Def6_0<I0, I1, I2, I3, I4, I5> {
            val javaProc = underlying.build()
            return Def6_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_6_1<I0, I1, I2, I3, I4, I5, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_1<I0, I1, I2, I3, I4, I5, O0>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> =
            Builder_7_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> =
            Builder_6_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_2<I0, I1, I2, I3, I4, I5, X, O0, X> =
            Builder_7_2(underlying.inout(type.underlying))

        fun build(): Def6_1<I0, I1, I2, I3, I4, I5, O0> {
            val javaProc = underlying.build()
            return Def6_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> =
            Builder_7_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> =
            Builder_6_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_3<I0, I1, I2, I3, I4, I5, X, O0, O1, X> =
            Builder_7_3(underlying.inout(type.underlying))

        fun build(): Def6_2<I0, I1, I2, I3, I4, I5, O0, O1> {
            val javaProc = underlying.build()
            return Def6_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> =
            Builder_7_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> =
            Builder_6_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_4<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, X> =
            Builder_7_4(underlying.inout(type.underlying))

        fun build(): Def6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def6_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> =
            Builder_7_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> =
            Builder_6_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_5<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, X> =
            Builder_7_5(underlying.inout(type.underlying))

        fun build(): Def6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def6_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> =
            Builder_7_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> =
            Builder_6_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_6<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, X> =
            Builder_7_6(underlying.inout(type.underlying))

        fun build(): Def6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def6_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> =
            Builder_7_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> =
            Builder_6_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_7<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_7_7(underlying.inout(type.underlying))

        fun build(): Def6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def6_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> =
            Builder_7_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_6_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_8<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_7_8(underlying.inout(type.underlying))

        fun build(): Def6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def6_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_7_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_6_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_9<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_7_9(underlying.inout(type.underlying))

        fun build(): Def6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def6_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_7_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_6_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_7_10<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_7_10(underlying.inout(type.underlying))

        fun build(): Def6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def6_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I6> `in`(type: DbType<I6>): Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_7_10(underlying.`in`(type.underlying))

        fun build(): Def6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def6_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_7_0<I0, I1, I2, I3, I4, I5, I6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_0<I0, I1, I2, I3, I4, I5, I6>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_0<I0, I1, I2, I3, I4, I5, I6, I7> =
            Builder_8_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> =
            Builder_7_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_1<I0, I1, I2, I3, I4, I5, I6, X, X> =
            Builder_8_1(underlying.inout(type.underlying))

        fun build(): Def7_0<I0, I1, I2, I3, I4, I5, I6> {
            val javaProc = underlying.build()
            return Def7_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> =
            Builder_8_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> =
            Builder_7_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_2<I0, I1, I2, I3, I4, I5, I6, X, O0, X> =
            Builder_8_2(underlying.inout(type.underlying))

        fun build(): Def7_1<I0, I1, I2, I3, I4, I5, I6, O0> {
            val javaProc = underlying.build()
            return Def7_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> =
            Builder_8_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> =
            Builder_7_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_3<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, X> =
            Builder_8_3(underlying.inout(type.underlying))

        fun build(): Def7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> {
            val javaProc = underlying.build()
            return Def7_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> =
            Builder_8_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> =
            Builder_7_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_4<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, X> =
            Builder_8_4(underlying.inout(type.underlying))

        fun build(): Def7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def7_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> =
            Builder_8_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> =
            Builder_7_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_5<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, X> =
            Builder_8_5(underlying.inout(type.underlying))

        fun build(): Def7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def7_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> =
            Builder_8_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> =
            Builder_7_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_6<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, X> =
            Builder_8_6(underlying.inout(type.underlying))

        fun build(): Def7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def7_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> =
            Builder_8_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> =
            Builder_7_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_7<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_8_7(underlying.inout(type.underlying))

        fun build(): Def7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def7_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> =
            Builder_8_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_7_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_8<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_8_8(underlying.inout(type.underlying))

        fun build(): Def7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def7_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_8_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_7_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_9<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_8_9(underlying.inout(type.underlying))

        fun build(): Def7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def7_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_8_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_7_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_8_10<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_8_10(underlying.inout(type.underlying))

        fun build(): Def7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def7_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I7> `in`(type: DbType<I7>): Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_8_10(underlying.`in`(type.underlying))

        fun build(): Def7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def7_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_8_0<I0, I1, I2, I3, I4, I5, I6, I7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_0<I0, I1, I2, I3, I4, I5, I6, I7>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> =
            Builder_9_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> =
            Builder_8_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, X, X> =
            Builder_9_1(underlying.inout(type.underlying))

        fun build(): Def8_0<I0, I1, I2, I3, I4, I5, I6, I7> {
            val javaProc = underlying.build()
            return Def8_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> =
            Builder_9_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> =
            Builder_8_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, X> =
            Builder_9_2(underlying.inout(type.underlying))

        fun build(): Def8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> {
            val javaProc = underlying.build()
            return Def8_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> =
            Builder_9_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> =
            Builder_8_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, X> =
            Builder_9_3(underlying.inout(type.underlying))

        fun build(): Def8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> {
            val javaProc = underlying.build()
            return Def8_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> =
            Builder_9_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> =
            Builder_8_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, X> =
            Builder_9_4(underlying.inout(type.underlying))

        fun build(): Def8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def8_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> =
            Builder_9_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> =
            Builder_8_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, X> =
            Builder_9_5(underlying.inout(type.underlying))

        fun build(): Def8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def8_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> =
            Builder_9_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> =
            Builder_8_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, X> =
            Builder_9_6(underlying.inout(type.underlying))

        fun build(): Def8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def8_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> =
            Builder_9_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> =
            Builder_8_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_9_7(underlying.inout(type.underlying))

        fun build(): Def8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def8_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> =
            Builder_9_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_8_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_9_8(underlying.inout(type.underlying))

        fun build(): Def8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def8_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_9_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_8_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_9_9(underlying.inout(type.underlying))

        fun build(): Def8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def8_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_9_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_8_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_9_10(underlying.inout(type.underlying))

        fun build(): Def8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def8_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I8> `in`(type: DbType<I8>): Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_9_10(underlying.`in`(type.underlying))

        fun build(): Def8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def8_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> =
            Builder_10_0(underlying.`in`(type.underlying))
        fun <O0> out(type: DbType<O0>): Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> =
            Builder_9_1(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, X> =
            Builder_10_1(underlying.inout(type.underlying))

        fun build(): Def9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> {
            val javaProc = underlying.build()
            return Def9_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> =
            Builder_10_1(underlying.`in`(type.underlying))
        fun <O1> out(type: DbType<O1>): Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> =
            Builder_9_2(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, X> =
            Builder_10_2(underlying.inout(type.underlying))

        fun build(): Def9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> {
            val javaProc = underlying.build()
            return Def9_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> =
            Builder_10_2(underlying.`in`(type.underlying))
        fun <O2> out(type: DbType<O2>): Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> =
            Builder_9_3(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, X> =
            Builder_10_3(underlying.inout(type.underlying))

        fun build(): Def9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> {
            val javaProc = underlying.build()
            return Def9_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> =
            Builder_10_3(underlying.`in`(type.underlying))
        fun <O3> out(type: DbType<O3>): Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> =
            Builder_9_4(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, X> =
            Builder_10_4(underlying.inout(type.underlying))

        fun build(): Def9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def9_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> =
            Builder_10_4(underlying.`in`(type.underlying))
        fun <O4> out(type: DbType<O4>): Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> =
            Builder_9_5(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, X> =
            Builder_10_5(underlying.inout(type.underlying))

        fun build(): Def9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def9_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> =
            Builder_10_5(underlying.`in`(type.underlying))
        fun <O5> out(type: DbType<O5>): Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> =
            Builder_9_6(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, X> =
            Builder_10_6(underlying.inout(type.underlying))

        fun build(): Def9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def9_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> =
            Builder_10_6(underlying.`in`(type.underlying))
        fun <O6> out(type: DbType<O6>): Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> =
            Builder_9_7(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, X> =
            Builder_10_7(underlying.inout(type.underlying))

        fun build(): Def9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def9_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> =
            Builder_10_7(underlying.`in`(type.underlying))
        fun <O7> out(type: DbType<O7>): Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_9_8(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, X> =
            Builder_10_8(underlying.inout(type.underlying))

        fun build(): Def9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def9_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_10_8(underlying.`in`(type.underlying))
        fun <O8> out(type: DbType<O8>): Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_9_9(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, X> =
            Builder_10_9(underlying.inout(type.underlying))

        fun build(): Def9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def9_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_10_9(underlying.`in`(type.underlying))
        fun <O9> out(type: DbType<O9>): Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_9_10(underlying.out(type.underlying))
        fun <X> inout(type: DbType<X>): Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> =
            Builder_10_10(underlying.inout(type.underlying))

        fun build(): Def9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def9_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {
        fun <I9> `in`(type: DbType<I9>): Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_10_10(underlying.`in`(type.underlying))

        fun build(): Def9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def9_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }

    class Builder_10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9>
    ) {
        fun <O0> out(type: DbType<O0>): Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> =
            Builder_10_1(underlying.out(type.underlying))

        fun build(): Def10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> {
            val javaProc = underlying.build()
            return Def10_0 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { }
            }
        }
    }

    class Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0>
    ) {
        fun <O1> out(type: DbType<O1>): Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> =
            Builder_10_2(underlying.out(type.underlying))

        fun build(): Def10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> {
            val javaProc = underlying.build()
            return Def10_1 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as O0 }
            }
        }
    }

    class Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1>
    ) {
        fun <O2> out(type: DbType<O2>): Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> =
            Builder_10_3(underlying.out(type.underlying))

        fun build(): Def10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> {
            val javaProc = underlying.build()
            return Def10_2 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple2<O0, O1> }
            }
        }
    }

    class Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2>
    ) {
        fun <O3> out(type: DbType<O3>): Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> =
            Builder_10_4(underlying.out(type.underlying))

        fun build(): Def10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> {
            val javaProc = underlying.build()
            return Def10_3 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple3<O0, O1, O2> }
            }
        }
    }

    class Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3>
    ) {
        fun <O4> out(type: DbType<O4>): Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> =
            Builder_10_5(underlying.out(type.underlying))

        fun build(): Def10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> {
            val javaProc = underlying.build()
            return Def10_4 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple4<O0, O1, O2, O3> }
            }
        }
    }

    class Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4>
    ) {
        fun <O5> out(type: DbType<O5>): Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> =
            Builder_10_6(underlying.out(type.underlying))

        fun build(): Def10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> {
            val javaProc = underlying.build()
            return Def10_5 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple5<O0, O1, O2, O3, O4> }
            }
        }
    }

    class Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5>
    ) {
        fun <O6> out(type: DbType<O6>): Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> =
            Builder_10_7(underlying.out(type.underlying))

        fun build(): Def10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> {
            val javaProc = underlying.build()
            return Def10_6 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple6<O0, O1, O2, O3, O4, O5> }
            }
        }
    }

    class Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6>
    ) {
        fun <O7> out(type: DbType<O7>): Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> =
            Builder_10_8(underlying.out(type.underlying))

        fun build(): Def10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> {
            val javaProc = underlying.build()
            return Def10_7 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6> }
            }
        }
    }

    class Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7>
    ) {
        fun <O8> out(type: DbType<O8>): Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> =
            Builder_10_9(underlying.out(type.underlying))

        fun build(): Def10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> {
            val javaProc = underlying.build()
            return Def10_8 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7> }
            }
        }
    }

    class Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8>
    ) {
        fun <O9> out(type: DbType<O9>): Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> =
            Builder_10_10(underlying.out(type.underlying))

        fun build(): Def10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
            val javaProc = underlying.build()
            return Def10_9 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8> }
            }
        }
    }

    class Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> internal constructor(
        private val underlying: dev.typr.foundations.DbProcedure.Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>
    ) {

        fun build(): Def10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
            val javaProc = underlying.build()
            return Def10_10 { i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9 ->
                @Suppress("UNCHECKED_CAST")
                ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) as dev.typr.foundations.Operation<Any?>) { it as dev.typr.foundations.Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> }
            }
        }
    }
}
