package dev.typr.scalafoundations

/** Type-safe stored procedure definitions with fully typed inputs and outputs.
  *
  * Usage:
  * {{{
  * val getUser: DbProcedure.Def1_2[Int, String, String] = DbProcedure.define("get_user_by_id")
  *   .in(PgTypes.int4)
  *   .out(PgTypes.text)
  *   .out(PgTypes.text)
  *   .build()
  * val result = getUser.call(42).transact(tx)  // Int enforced!
  * }}}
  *
  * @see [[DbFunction]] for stored functions (single return value via SELECT)
  */
object DbProcedure {

  /** Start defining a stored procedure. */
  def define(name: String): Builder_0_0 =
    new Builder_0_0(dev.typr.foundations.DbProcedure.define(name))

  // ─────────────────────────────────────────────────────────────────────────────
  // Procedure definition interfaces (121 total: 11×11 matrix of input×output arities)
  // ─────────────────────────────────────────────────────────────────────────────

  /** Procedure definition with 0 input(s) and 0 output(s). */
  trait Def0_0 {
    def call(): ProcedureOp[Unit]
  }

  /** Procedure definition with 0 input(s) and 1 output(s). */
  trait Def0_1[O0] {
    def call(): ProcedureOp[O0]
  }

  /** Procedure definition with 0 input(s) and 2 output(s). */
  trait Def0_2[O0, O1] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 0 input(s) and 3 output(s). */
  trait Def0_3[O0, O1, O2] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 0 input(s) and 4 output(s). */
  trait Def0_4[O0, O1, O2, O3] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 0 input(s) and 5 output(s). */
  trait Def0_5[O0, O1, O2, O3, O4] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 0 input(s) and 6 output(s). */
  trait Def0_6[O0, O1, O2, O3, O4, O5] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 0 input(s) and 7 output(s). */
  trait Def0_7[O0, O1, O2, O3, O4, O5, O6] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 0 input(s) and 8 output(s). */
  trait Def0_8[O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 0 input(s) and 9 output(s). */
  trait Def0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 0 input(s) and 10 output(s). */
  trait Def0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 1 input(s) and 0 output(s). */
  trait Def1_0[I0] {
    def call(i0: I0): ProcedureOp[Unit]
  }

  /** Procedure definition with 1 input(s) and 1 output(s). */
  trait Def1_1[I0, O0] {
    def call(i0: I0): ProcedureOp[O0]
  }

  /** Procedure definition with 1 input(s) and 2 output(s). */
  trait Def1_2[I0, O0, O1] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 1 input(s) and 3 output(s). */
  trait Def1_3[I0, O0, O1, O2] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 1 input(s) and 4 output(s). */
  trait Def1_4[I0, O0, O1, O2, O3] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 1 input(s) and 5 output(s). */
  trait Def1_5[I0, O0, O1, O2, O3, O4] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 1 input(s) and 6 output(s). */
  trait Def1_6[I0, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 1 input(s) and 7 output(s). */
  trait Def1_7[I0, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 1 input(s) and 8 output(s). */
  trait Def1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 1 input(s) and 9 output(s). */
  trait Def1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 1 input(s) and 10 output(s). */
  trait Def1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 2 input(s) and 0 output(s). */
  trait Def2_0[I0, I1] {
    def call(i0: I0, i1: I1): ProcedureOp[Unit]
  }

  /** Procedure definition with 2 input(s) and 1 output(s). */
  trait Def2_1[I0, I1, O0] {
    def call(i0: I0, i1: I1): ProcedureOp[O0]
  }

  /** Procedure definition with 2 input(s) and 2 output(s). */
  trait Def2_2[I0, I1, O0, O1] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 2 input(s) and 3 output(s). */
  trait Def2_3[I0, I1, O0, O1, O2] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 2 input(s) and 4 output(s). */
  trait Def2_4[I0, I1, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 2 input(s) and 5 output(s). */
  trait Def2_5[I0, I1, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 2 input(s) and 6 output(s). */
  trait Def2_6[I0, I1, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 2 input(s) and 7 output(s). */
  trait Def2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 2 input(s) and 8 output(s). */
  trait Def2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 2 input(s) and 9 output(s). */
  trait Def2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 2 input(s) and 10 output(s). */
  trait Def2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 3 input(s) and 0 output(s). */
  trait Def3_0[I0, I1, I2] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[Unit]
  }

  /** Procedure definition with 3 input(s) and 1 output(s). */
  trait Def3_1[I0, I1, I2, O0] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[O0]
  }

  /** Procedure definition with 3 input(s) and 2 output(s). */
  trait Def3_2[I0, I1, I2, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 3 input(s) and 3 output(s). */
  trait Def3_3[I0, I1, I2, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 3 input(s) and 4 output(s). */
  trait Def3_4[I0, I1, I2, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 3 input(s) and 5 output(s). */
  trait Def3_5[I0, I1, I2, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 3 input(s) and 6 output(s). */
  trait Def3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 3 input(s) and 7 output(s). */
  trait Def3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 3 input(s) and 8 output(s). */
  trait Def3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 3 input(s) and 9 output(s). */
  trait Def3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 3 input(s) and 10 output(s). */
  trait Def3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 4 input(s) and 0 output(s). */
  trait Def4_0[I0, I1, I2, I3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[Unit]
  }

  /** Procedure definition with 4 input(s) and 1 output(s). */
  trait Def4_1[I0, I1, I2, I3, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[O0]
  }

  /** Procedure definition with 4 input(s) and 2 output(s). */
  trait Def4_2[I0, I1, I2, I3, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 4 input(s) and 3 output(s). */
  trait Def4_3[I0, I1, I2, I3, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 4 input(s) and 4 output(s). */
  trait Def4_4[I0, I1, I2, I3, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 4 input(s) and 5 output(s). */
  trait Def4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 4 input(s) and 6 output(s). */
  trait Def4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 4 input(s) and 7 output(s). */
  trait Def4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 4 input(s) and 8 output(s). */
  trait Def4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 4 input(s) and 9 output(s). */
  trait Def4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 4 input(s) and 10 output(s). */
  trait Def4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 5 input(s) and 0 output(s). */
  trait Def5_0[I0, I1, I2, I3, I4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[Unit]
  }

  /** Procedure definition with 5 input(s) and 1 output(s). */
  trait Def5_1[I0, I1, I2, I3, I4, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[O0]
  }

  /** Procedure definition with 5 input(s) and 2 output(s). */
  trait Def5_2[I0, I1, I2, I3, I4, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 5 input(s) and 3 output(s). */
  trait Def5_3[I0, I1, I2, I3, I4, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 5 input(s) and 4 output(s). */
  trait Def5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 5 input(s) and 5 output(s). */
  trait Def5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 5 input(s) and 6 output(s). */
  trait Def5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 5 input(s) and 7 output(s). */
  trait Def5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 5 input(s) and 8 output(s). */
  trait Def5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 5 input(s) and 9 output(s). */
  trait Def5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 5 input(s) and 10 output(s). */
  trait Def5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 6 input(s) and 0 output(s). */
  trait Def6_0[I0, I1, I2, I3, I4, I5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[Unit]
  }

  /** Procedure definition with 6 input(s) and 1 output(s). */
  trait Def6_1[I0, I1, I2, I3, I4, I5, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[O0]
  }

  /** Procedure definition with 6 input(s) and 2 output(s). */
  trait Def6_2[I0, I1, I2, I3, I4, I5, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 6 input(s) and 3 output(s). */
  trait Def6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 6 input(s) and 4 output(s). */
  trait Def6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 6 input(s) and 5 output(s). */
  trait Def6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 6 input(s) and 6 output(s). */
  trait Def6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 6 input(s) and 7 output(s). */
  trait Def6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 6 input(s) and 8 output(s). */
  trait Def6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 6 input(s) and 9 output(s). */
  trait Def6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 6 input(s) and 10 output(s). */
  trait Def6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 7 input(s) and 0 output(s). */
  trait Def7_0[I0, I1, I2, I3, I4, I5, I6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[Unit]
  }

  /** Procedure definition with 7 input(s) and 1 output(s). */
  trait Def7_1[I0, I1, I2, I3, I4, I5, I6, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[O0]
  }

  /** Procedure definition with 7 input(s) and 2 output(s). */
  trait Def7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 7 input(s) and 3 output(s). */
  trait Def7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 7 input(s) and 4 output(s). */
  trait Def7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 7 input(s) and 5 output(s). */
  trait Def7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 7 input(s) and 6 output(s). */
  trait Def7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 7 input(s) and 7 output(s). */
  trait Def7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 7 input(s) and 8 output(s). */
  trait Def7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 7 input(s) and 9 output(s). */
  trait Def7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 7 input(s) and 10 output(s). */
  trait Def7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 8 input(s) and 0 output(s). */
  trait Def8_0[I0, I1, I2, I3, I4, I5, I6, I7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[Unit]
  }

  /** Procedure definition with 8 input(s) and 1 output(s). */
  trait Def8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[O0]
  }

  /** Procedure definition with 8 input(s) and 2 output(s). */
  trait Def8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 8 input(s) and 3 output(s). */
  trait Def8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 8 input(s) and 4 output(s). */
  trait Def8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 8 input(s) and 5 output(s). */
  trait Def8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 8 input(s) and 6 output(s). */
  trait Def8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 8 input(s) and 7 output(s). */
  trait Def8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 8 input(s) and 8 output(s). */
  trait Def8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 8 input(s) and 9 output(s). */
  trait Def8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 8 input(s) and 10 output(s). */
  trait Def8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 9 input(s) and 0 output(s). */
  trait Def9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[Unit]
  }

  /** Procedure definition with 9 input(s) and 1 output(s). */
  trait Def9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[O0]
  }

  /** Procedure definition with 9 input(s) and 2 output(s). */
  trait Def9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 9 input(s) and 3 output(s). */
  trait Def9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 9 input(s) and 4 output(s). */
  trait Def9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 9 input(s) and 5 output(s). */
  trait Def9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 9 input(s) and 6 output(s). */
  trait Def9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 9 input(s) and 7 output(s). */
  trait Def9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 9 input(s) and 8 output(s). */
  trait Def9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 9 input(s) and 9 output(s). */
  trait Def9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 9 input(s) and 10 output(s). */
  trait Def9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  /** Procedure definition with 10 input(s) and 0 output(s). */
  trait Def10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[Unit]
  }

  /** Procedure definition with 10 input(s) and 1 output(s). */
  trait Def10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[O0]
  }

  /** Procedure definition with 10 input(s) and 2 output(s). */
  trait Def10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]]
  }

  /** Procedure definition with 10 input(s) and 3 output(s). */
  trait Def10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]]
  }

  /** Procedure definition with 10 input(s) and 4 output(s). */
  trait Def10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]]
  }

  /** Procedure definition with 10 input(s) and 5 output(s). */
  trait Def10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]]
  }

  /** Procedure definition with 10 input(s) and 6 output(s). */
  trait Def10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]]
  }

  /** Procedure definition with 10 input(s) and 7 output(s). */
  trait Def10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]]
  }

  /** Procedure definition with 10 input(s) and 8 output(s). */
  trait Def10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]]
  }

  /** Procedure definition with 10 input(s) and 9 output(s). */
  trait Def10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]]
  }

  /** Procedure definition with 10 input(s) and 10 output(s). */
  trait Def10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]]
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Procedure builders (121 total: 11×11 matrix)
  // ─────────────────────────────────────────────────────────────────────────────

  class Builder_0_0 private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_0
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_0[I0] =
      new Builder_1_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_0_1[O0] =
      new Builder_0_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_1[X, X] =
      new Builder_1_1(underlying.inout(tpe.underlying))

    def build(): Def0_0 = {
      val javaProc = underlying.build()
      new Def0_0 {
        def call(): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_0_1[O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_1[O0]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_1[I0, O0] =
      new Builder_1_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_0_2[O0, O1] =
      new Builder_0_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_2[X, O0, X] =
      new Builder_1_2(underlying.inout(tpe.underlying))

    def build(): Def0_1[O0] = {
      val javaProc = underlying.build()
      new Def0_1[O0] {
        def call(): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_0_2[O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_2[O0, O1]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_2[I0, O0, O1] =
      new Builder_1_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_0_3[O0, O1, O2] =
      new Builder_0_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_3[X, O0, O1, X] =
      new Builder_1_3(underlying.inout(tpe.underlying))

    def build(): Def0_2[O0, O1] = {
      val javaProc = underlying.build()
      new Def0_2[O0, O1] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_0_3[O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_3[O0, O1, O2]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_3[I0, O0, O1, O2] =
      new Builder_1_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_0_4[O0, O1, O2, O3] =
      new Builder_0_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_4[X, O0, O1, O2, X] =
      new Builder_1_4(underlying.inout(tpe.underlying))

    def build(): Def0_3[O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def0_3[O0, O1, O2] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_0_4[O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_4[O0, O1, O2, O3]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_4[I0, O0, O1, O2, O3] =
      new Builder_1_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_0_5[O0, O1, O2, O3, O4] =
      new Builder_0_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_5[X, O0, O1, O2, O3, X] =
      new Builder_1_5(underlying.inout(tpe.underlying))

    def build(): Def0_4[O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def0_4[O0, O1, O2, O3] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_0_5[O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_5[O0, O1, O2, O3, O4]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_5[I0, O0, O1, O2, O3, O4] =
      new Builder_1_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_0_6[O0, O1, O2, O3, O4, O5] =
      new Builder_0_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_6[X, O0, O1, O2, O3, O4, X] =
      new Builder_1_6(underlying.inout(tpe.underlying))

    def build(): Def0_5[O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def0_5[O0, O1, O2, O3, O4] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_0_6[O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_6[O0, O1, O2, O3, O4, O5]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_6[I0, O0, O1, O2, O3, O4, O5] =
      new Builder_1_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_0_7[O0, O1, O2, O3, O4, O5, O6] =
      new Builder_0_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_7[X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_1_7(underlying.inout(tpe.underlying))

    def build(): Def0_6[O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def0_6[O0, O1, O2, O3, O4, O5] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_0_7[O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_7[O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_7[I0, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_1_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_0_8[O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_0_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_8[X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_1_8(underlying.inout(tpe.underlying))

    def build(): Def0_7[O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def0_7[O0, O1, O2, O3, O4, O5, O6] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_0_8[O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_8[O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_1_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_0_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_9[X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_1_9(underlying.inout(tpe.underlying))

    def build(): Def0_8[O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def0_8[O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_1_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_0_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_1_10[X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_1_10(underlying.inout(tpe.underlying))

    def build(): Def0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def0_9[O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_1_10(underlying.in(tpe.underlying))

    def build(): Def0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def0_10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_1_0[I0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_0[I0]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_0[I0, I1] =
      new Builder_2_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_1_1[I0, O0] =
      new Builder_1_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_1[I0, X, X] =
      new Builder_2_1(underlying.inout(tpe.underlying))

    def build(): Def1_0[I0] = {
      val javaProc = underlying.build()
      new Def1_0[I0] {
        def call(i0: I0): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_1_1[I0, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_1[I0, O0]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_1[I0, I1, O0] =
      new Builder_2_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_1_2[I0, O0, O1] =
      new Builder_1_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_2[I0, X, O0, X] =
      new Builder_2_2(underlying.inout(tpe.underlying))

    def build(): Def1_1[I0, O0] = {
      val javaProc = underlying.build()
      new Def1_1[I0, O0] {
        def call(i0: I0): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_1_2[I0, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_2[I0, O0, O1]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_2[I0, I1, O0, O1] =
      new Builder_2_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_1_3[I0, O0, O1, O2] =
      new Builder_1_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_3[I0, X, O0, O1, X] =
      new Builder_2_3(underlying.inout(tpe.underlying))

    def build(): Def1_2[I0, O0, O1] = {
      val javaProc = underlying.build()
      new Def1_2[I0, O0, O1] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_1_3[I0, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_3[I0, O0, O1, O2]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_3[I0, I1, O0, O1, O2] =
      new Builder_2_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_1_4[I0, O0, O1, O2, O3] =
      new Builder_1_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_4[I0, X, O0, O1, O2, X] =
      new Builder_2_4(underlying.inout(tpe.underlying))

    def build(): Def1_3[I0, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def1_3[I0, O0, O1, O2] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_1_4[I0, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_4[I0, O0, O1, O2, O3]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_4[I0, I1, O0, O1, O2, O3] =
      new Builder_2_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_1_5[I0, O0, O1, O2, O3, O4] =
      new Builder_1_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_5[I0, X, O0, O1, O2, O3, X] =
      new Builder_2_5(underlying.inout(tpe.underlying))

    def build(): Def1_4[I0, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def1_4[I0, O0, O1, O2, O3] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_1_5[I0, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_5[I0, O0, O1, O2, O3, O4]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_5[I0, I1, O0, O1, O2, O3, O4] =
      new Builder_2_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_1_6[I0, O0, O1, O2, O3, O4, O5] =
      new Builder_1_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_6[I0, X, O0, O1, O2, O3, O4, X] =
      new Builder_2_6(underlying.inout(tpe.underlying))

    def build(): Def1_5[I0, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def1_5[I0, O0, O1, O2, O3, O4] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_1_6[I0, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_6[I0, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_6[I0, I1, O0, O1, O2, O3, O4, O5] =
      new Builder_2_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_1_7[I0, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_1_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_7[I0, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_2_7(underlying.inout(tpe.underlying))

    def build(): Def1_6[I0, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def1_6[I0, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_1_7[I0, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_7[I0, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_2_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_1_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_8[I0, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_2_8(underlying.inout(tpe.underlying))

    def build(): Def1_7[I0, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def1_7[I0, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_2_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_1_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_9[I0, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_2_9(underlying.inout(tpe.underlying))

    def build(): Def1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def1_8[I0, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_2_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_1_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_2_10[I0, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_2_10(underlying.inout(tpe.underlying))

    def build(): Def1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def1_9[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_2_10(underlying.in(tpe.underlying))

    def build(): Def1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def1_10[I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_2_0[I0, I1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_0[I0, I1]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_0[I0, I1, I2] =
      new Builder_3_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_2_1[I0, I1, O0] =
      new Builder_2_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_1[I0, I1, X, X] =
      new Builder_3_1(underlying.inout(tpe.underlying))

    def build(): Def2_0[I0, I1] = {
      val javaProc = underlying.build()
      new Def2_0[I0, I1] {
        def call(i0: I0, i1: I1): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_2_1[I0, I1, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_1[I0, I1, O0]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_1[I0, I1, I2, O0] =
      new Builder_3_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_2_2[I0, I1, O0, O1] =
      new Builder_2_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_2[I0, I1, X, O0, X] =
      new Builder_3_2(underlying.inout(tpe.underlying))

    def build(): Def2_1[I0, I1, O0] = {
      val javaProc = underlying.build()
      new Def2_1[I0, I1, O0] {
        def call(i0: I0, i1: I1): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_2_2[I0, I1, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_2[I0, I1, O0, O1]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_2[I0, I1, I2, O0, O1] =
      new Builder_3_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_2_3[I0, I1, O0, O1, O2] =
      new Builder_2_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_3[I0, I1, X, O0, O1, X] =
      new Builder_3_3(underlying.inout(tpe.underlying))

    def build(): Def2_2[I0, I1, O0, O1] = {
      val javaProc = underlying.build()
      new Def2_2[I0, I1, O0, O1] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_2_3[I0, I1, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_3[I0, I1, O0, O1, O2]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_3[I0, I1, I2, O0, O1, O2] =
      new Builder_3_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_2_4[I0, I1, O0, O1, O2, O3] =
      new Builder_2_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_4[I0, I1, X, O0, O1, O2, X] =
      new Builder_3_4(underlying.inout(tpe.underlying))

    def build(): Def2_3[I0, I1, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def2_3[I0, I1, O0, O1, O2] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_2_4[I0, I1, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_4[I0, I1, O0, O1, O2, O3]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_4[I0, I1, I2, O0, O1, O2, O3] =
      new Builder_3_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_2_5[I0, I1, O0, O1, O2, O3, O4] =
      new Builder_2_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_5[I0, I1, X, O0, O1, O2, O3, X] =
      new Builder_3_5(underlying.inout(tpe.underlying))

    def build(): Def2_4[I0, I1, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def2_4[I0, I1, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_2_5[I0, I1, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_5[I0, I1, O0, O1, O2, O3, O4]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_5[I0, I1, I2, O0, O1, O2, O3, O4] =
      new Builder_3_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_2_6[I0, I1, O0, O1, O2, O3, O4, O5] =
      new Builder_2_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_6[I0, I1, X, O0, O1, O2, O3, O4, X] =
      new Builder_3_6(underlying.inout(tpe.underlying))

    def build(): Def2_5[I0, I1, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def2_5[I0, I1, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_2_6[I0, I1, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_6[I0, I1, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] =
      new Builder_3_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_2_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_7[I0, I1, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_3_7(underlying.inout(tpe.underlying))

    def build(): Def2_6[I0, I1, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def2_6[I0, I1, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_3_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_2_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_8[I0, I1, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_3_8(underlying.inout(tpe.underlying))

    def build(): Def2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def2_7[I0, I1, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_3_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_2_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_9[I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_3_9(underlying.inout(tpe.underlying))

    def build(): Def2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def2_8[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_3_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_2_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_3_10[I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_3_10(underlying.inout(tpe.underlying))

    def build(): Def2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def2_9[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_3_10(underlying.in(tpe.underlying))

    def build(): Def2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def2_10[I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_3_0[I0, I1, I2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_0[I0, I1, I2]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_0[I0, I1, I2, I3] =
      new Builder_4_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_3_1[I0, I1, I2, O0] =
      new Builder_3_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_1[I0, I1, I2, X, X] =
      new Builder_4_1(underlying.inout(tpe.underlying))

    def build(): Def3_0[I0, I1, I2] = {
      val javaProc = underlying.build()
      new Def3_0[I0, I1, I2] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_3_1[I0, I1, I2, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_1[I0, I1, I2, O0]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_1[I0, I1, I2, I3, O0] =
      new Builder_4_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_3_2[I0, I1, I2, O0, O1] =
      new Builder_3_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_2[I0, I1, I2, X, O0, X] =
      new Builder_4_2(underlying.inout(tpe.underlying))

    def build(): Def3_1[I0, I1, I2, O0] = {
      val javaProc = underlying.build()
      new Def3_1[I0, I1, I2, O0] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_3_2[I0, I1, I2, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_2[I0, I1, I2, O0, O1]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_2[I0, I1, I2, I3, O0, O1] =
      new Builder_4_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_3_3[I0, I1, I2, O0, O1, O2] =
      new Builder_3_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_3[I0, I1, I2, X, O0, O1, X] =
      new Builder_4_3(underlying.inout(tpe.underlying))

    def build(): Def3_2[I0, I1, I2, O0, O1] = {
      val javaProc = underlying.build()
      new Def3_2[I0, I1, I2, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_3_3[I0, I1, I2, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_3[I0, I1, I2, O0, O1, O2]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_3[I0, I1, I2, I3, O0, O1, O2] =
      new Builder_4_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_3_4[I0, I1, I2, O0, O1, O2, O3] =
      new Builder_3_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_4[I0, I1, I2, X, O0, O1, O2, X] =
      new Builder_4_4(underlying.inout(tpe.underlying))

    def build(): Def3_3[I0, I1, I2, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def3_3[I0, I1, I2, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_3_4[I0, I1, I2, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_4[I0, I1, I2, O0, O1, O2, O3]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_4[I0, I1, I2, I3, O0, O1, O2, O3] =
      new Builder_4_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_3_5[I0, I1, I2, O0, O1, O2, O3, O4] =
      new Builder_3_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_5[I0, I1, I2, X, O0, O1, O2, O3, X] =
      new Builder_4_5(underlying.inout(tpe.underlying))

    def build(): Def3_4[I0, I1, I2, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def3_4[I0, I1, I2, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_3_5[I0, I1, I2, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_5[I0, I1, I2, O0, O1, O2, O3, O4]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] =
      new Builder_4_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] =
      new Builder_3_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_6[I0, I1, I2, X, O0, O1, O2, O3, O4, X] =
      new Builder_4_6(underlying.inout(tpe.underlying))

    def build(): Def3_5[I0, I1, I2, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def3_5[I0, I1, I2, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] =
      new Builder_4_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_3_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_7[I0, I1, I2, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_4_7(underlying.inout(tpe.underlying))

    def build(): Def3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def3_6[I0, I1, I2, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_4_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_3_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_8[I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_4_8(underlying.inout(tpe.underlying))

    def build(): Def3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def3_7[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_4_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_3_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_9[I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_4_9(underlying.inout(tpe.underlying))

    def build(): Def3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def3_8[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_4_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_3_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_4_10[I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_4_10(underlying.inout(tpe.underlying))

    def build(): Def3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def3_9[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_4_10(underlying.in(tpe.underlying))

    def build(): Def3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def3_10[I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_4_0[I0, I1, I2, I3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_0[I0, I1, I2, I3]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_0[I0, I1, I2, I3, I4] =
      new Builder_5_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_4_1[I0, I1, I2, I3, O0] =
      new Builder_4_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_1[I0, I1, I2, I3, X, X] =
      new Builder_5_1(underlying.inout(tpe.underlying))

    def build(): Def4_0[I0, I1, I2, I3] = {
      val javaProc = underlying.build()
      new Def4_0[I0, I1, I2, I3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_4_1[I0, I1, I2, I3, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_1[I0, I1, I2, I3, O0]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_1[I0, I1, I2, I3, I4, O0] =
      new Builder_5_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_4_2[I0, I1, I2, I3, O0, O1] =
      new Builder_4_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_2[I0, I1, I2, I3, X, O0, X] =
      new Builder_5_2(underlying.inout(tpe.underlying))

    def build(): Def4_1[I0, I1, I2, I3, O0] = {
      val javaProc = underlying.build()
      new Def4_1[I0, I1, I2, I3, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_4_2[I0, I1, I2, I3, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_2[I0, I1, I2, I3, O0, O1]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_2[I0, I1, I2, I3, I4, O0, O1] =
      new Builder_5_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_4_3[I0, I1, I2, I3, O0, O1, O2] =
      new Builder_4_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_3[I0, I1, I2, I3, X, O0, O1, X] =
      new Builder_5_3(underlying.inout(tpe.underlying))

    def build(): Def4_2[I0, I1, I2, I3, O0, O1] = {
      val javaProc = underlying.build()
      new Def4_2[I0, I1, I2, I3, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_4_3[I0, I1, I2, I3, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_3[I0, I1, I2, I3, O0, O1, O2]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_3[I0, I1, I2, I3, I4, O0, O1, O2] =
      new Builder_5_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_4_4[I0, I1, I2, I3, O0, O1, O2, O3] =
      new Builder_4_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_4[I0, I1, I2, I3, X, O0, O1, O2, X] =
      new Builder_5_4(underlying.inout(tpe.underlying))

    def build(): Def4_3[I0, I1, I2, I3, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def4_3[I0, I1, I2, I3, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_4_4[I0, I1, I2, I3, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_4[I0, I1, I2, I3, O0, O1, O2, O3]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] =
      new Builder_5_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] =
      new Builder_4_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_5[I0, I1, I2, I3, X, O0, O1, O2, O3, X] =
      new Builder_5_5(underlying.inout(tpe.underlying))

    def build(): Def4_4[I0, I1, I2, I3, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def4_4[I0, I1, I2, I3, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] =
      new Builder_5_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] =
      new Builder_4_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_6[I0, I1, I2, I3, X, O0, O1, O2, O3, O4, X] =
      new Builder_5_6(underlying.inout(tpe.underlying))

    def build(): Def4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def4_5[I0, I1, I2, I3, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] =
      new Builder_5_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_4_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_7[I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_5_7(underlying.inout(tpe.underlying))

    def build(): Def4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def4_6[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_5_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_4_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_8[I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_5_8(underlying.inout(tpe.underlying))

    def build(): Def4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def4_7[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_5_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_4_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_9[I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_5_9(underlying.inout(tpe.underlying))

    def build(): Def4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def4_8[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_5_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_4_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_5_10[I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_5_10(underlying.inout(tpe.underlying))

    def build(): Def4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def4_9[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_5_10(underlying.in(tpe.underlying))

    def build(): Def4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def4_10[I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_5_0[I0, I1, I2, I3, I4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_0[I0, I1, I2, I3, I4]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_0[I0, I1, I2, I3, I4, I5] =
      new Builder_6_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_5_1[I0, I1, I2, I3, I4, O0] =
      new Builder_5_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_1[I0, I1, I2, I3, I4, X, X] =
      new Builder_6_1(underlying.inout(tpe.underlying))

    def build(): Def5_0[I0, I1, I2, I3, I4] = {
      val javaProc = underlying.build()
      new Def5_0[I0, I1, I2, I3, I4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_5_1[I0, I1, I2, I3, I4, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_1[I0, I1, I2, I3, I4, O0]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_1[I0, I1, I2, I3, I4, I5, O0] =
      new Builder_6_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_5_2[I0, I1, I2, I3, I4, O0, O1] =
      new Builder_5_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_2[I0, I1, I2, I3, I4, X, O0, X] =
      new Builder_6_2(underlying.inout(tpe.underlying))

    def build(): Def5_1[I0, I1, I2, I3, I4, O0] = {
      val javaProc = underlying.build()
      new Def5_1[I0, I1, I2, I3, I4, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_5_2[I0, I1, I2, I3, I4, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_2[I0, I1, I2, I3, I4, O0, O1]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_2[I0, I1, I2, I3, I4, I5, O0, O1] =
      new Builder_6_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_5_3[I0, I1, I2, I3, I4, O0, O1, O2] =
      new Builder_5_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_3[I0, I1, I2, I3, I4, X, O0, O1, X] =
      new Builder_6_3(underlying.inout(tpe.underlying))

    def build(): Def5_2[I0, I1, I2, I3, I4, O0, O1] = {
      val javaProc = underlying.build()
      new Def5_2[I0, I1, I2, I3, I4, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_5_3[I0, I1, I2, I3, I4, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_3[I0, I1, I2, I3, I4, O0, O1, O2]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] =
      new Builder_6_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] =
      new Builder_5_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_4[I0, I1, I2, I3, I4, X, O0, O1, O2, X] =
      new Builder_6_4(underlying.inout(tpe.underlying))

    def build(): Def5_3[I0, I1, I2, I3, I4, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def5_3[I0, I1, I2, I3, I4, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] =
      new Builder_6_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] =
      new Builder_5_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_5[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, X] =
      new Builder_6_5(underlying.inout(tpe.underlying))

    def build(): Def5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def5_4[I0, I1, I2, I3, I4, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] =
      new Builder_6_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] =
      new Builder_5_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_6[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, X] =
      new Builder_6_6(underlying.inout(tpe.underlying))

    def build(): Def5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def5_5[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] =
      new Builder_6_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_5_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_7[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_6_7(underlying.inout(tpe.underlying))

    def build(): Def5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def5_6[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_6_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_5_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_8[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_6_8(underlying.inout(tpe.underlying))

    def build(): Def5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def5_7[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_6_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_5_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_9[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_6_9(underlying.inout(tpe.underlying))

    def build(): Def5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def5_8[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_6_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_5_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_6_10[I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_6_10(underlying.inout(tpe.underlying))

    def build(): Def5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def5_9[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_6_10(underlying.in(tpe.underlying))

    def build(): Def5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def5_10[I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_6_0[I0, I1, I2, I3, I4, I5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_0[I0, I1, I2, I3, I4, I5]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_0[I0, I1, I2, I3, I4, I5, I6] =
      new Builder_7_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_6_1[I0, I1, I2, I3, I4, I5, O0] =
      new Builder_6_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_1[I0, I1, I2, I3, I4, I5, X, X] =
      new Builder_7_1(underlying.inout(tpe.underlying))

    def build(): Def6_0[I0, I1, I2, I3, I4, I5] = {
      val javaProc = underlying.build()
      new Def6_0[I0, I1, I2, I3, I4, I5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_6_1[I0, I1, I2, I3, I4, I5, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_1[I0, I1, I2, I3, I4, I5, O0]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_1[I0, I1, I2, I3, I4, I5, I6, O0] =
      new Builder_7_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_6_2[I0, I1, I2, I3, I4, I5, O0, O1] =
      new Builder_6_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_2[I0, I1, I2, I3, I4, I5, X, O0, X] =
      new Builder_7_2(underlying.inout(tpe.underlying))

    def build(): Def6_1[I0, I1, I2, I3, I4, I5, O0] = {
      val javaProc = underlying.build()
      new Def6_1[I0, I1, I2, I3, I4, I5, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_6_2[I0, I1, I2, I3, I4, I5, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_2[I0, I1, I2, I3, I4, I5, O0, O1]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] =
      new Builder_7_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] =
      new Builder_6_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_3[I0, I1, I2, I3, I4, I5, X, O0, O1, X] =
      new Builder_7_3(underlying.inout(tpe.underlying))

    def build(): Def6_2[I0, I1, I2, I3, I4, I5, O0, O1] = {
      val javaProc = underlying.build()
      new Def6_2[I0, I1, I2, I3, I4, I5, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] =
      new Builder_7_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] =
      new Builder_6_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_4[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, X] =
      new Builder_7_4(underlying.inout(tpe.underlying))

    def build(): Def6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def6_3[I0, I1, I2, I3, I4, I5, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] =
      new Builder_7_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] =
      new Builder_6_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_5[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, X] =
      new Builder_7_5(underlying.inout(tpe.underlying))

    def build(): Def6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def6_4[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] =
      new Builder_7_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] =
      new Builder_6_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_6[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, X] =
      new Builder_7_6(underlying.inout(tpe.underlying))

    def build(): Def6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def6_5[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] =
      new Builder_7_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_6_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_7[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_7_7(underlying.inout(tpe.underlying))

    def build(): Def6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def6_6[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_7_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_6_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_8[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_7_8(underlying.inout(tpe.underlying))

    def build(): Def6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def6_7[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_7_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_6_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_9[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_7_9(underlying.inout(tpe.underlying))

    def build(): Def6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def6_8[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_7_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_6_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_7_10[I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_7_10(underlying.inout(tpe.underlying))

    def build(): Def6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def6_9[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_7_10(underlying.in(tpe.underlying))

    def build(): Def6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def6_10[I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_7_0[I0, I1, I2, I3, I4, I5, I6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_0[I0, I1, I2, I3, I4, I5, I6]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_0[I0, I1, I2, I3, I4, I5, I6, I7] =
      new Builder_8_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_7_1[I0, I1, I2, I3, I4, I5, I6, O0] =
      new Builder_7_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_1[I0, I1, I2, I3, I4, I5, I6, X, X] =
      new Builder_8_1(underlying.inout(tpe.underlying))

    def build(): Def7_0[I0, I1, I2, I3, I4, I5, I6] = {
      val javaProc = underlying.build()
      new Def7_0[I0, I1, I2, I3, I4, I5, I6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_7_1[I0, I1, I2, I3, I4, I5, I6, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_1[I0, I1, I2, I3, I4, I5, I6, O0]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] =
      new Builder_8_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] =
      new Builder_7_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_2[I0, I1, I2, I3, I4, I5, I6, X, O0, X] =
      new Builder_8_2(underlying.inout(tpe.underlying))

    def build(): Def7_1[I0, I1, I2, I3, I4, I5, I6, O0] = {
      val javaProc = underlying.build()
      new Def7_1[I0, I1, I2, I3, I4, I5, I6, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] =
      new Builder_8_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] =
      new Builder_7_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_3[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, X] =
      new Builder_8_3(underlying.inout(tpe.underlying))

    def build(): Def7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] = {
      val javaProc = underlying.build()
      new Def7_2[I0, I1, I2, I3, I4, I5, I6, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] =
      new Builder_8_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] =
      new Builder_7_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_4[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, X] =
      new Builder_8_4(underlying.inout(tpe.underlying))

    def build(): Def7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def7_3[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] =
      new Builder_8_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] =
      new Builder_7_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_5[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, X] =
      new Builder_8_5(underlying.inout(tpe.underlying))

    def build(): Def7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def7_4[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] =
      new Builder_8_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] =
      new Builder_7_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_6[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, X] =
      new Builder_8_6(underlying.inout(tpe.underlying))

    def build(): Def7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def7_5[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] =
      new Builder_8_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_7_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_7[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_8_7(underlying.inout(tpe.underlying))

    def build(): Def7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def7_6[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_8_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_7_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_8[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_8_8(underlying.inout(tpe.underlying))

    def build(): Def7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def7_7[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_8_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_7_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_9[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_8_9(underlying.inout(tpe.underlying))

    def build(): Def7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def7_8[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_8_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_7_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_8_10[I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_8_10(underlying.inout(tpe.underlying))

    def build(): Def7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def7_9[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_8_10(underlying.in(tpe.underlying))

    def build(): Def7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def7_10[I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_8_0[I0, I1, I2, I3, I4, I5, I6, I7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_0[I0, I1, I2, I3, I4, I5, I6, I7]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8] =
      new Builder_9_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] =
      new Builder_8_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_1[I0, I1, I2, I3, I4, I5, I6, I7, X, X] =
      new Builder_9_1(underlying.inout(tpe.underlying))

    def build(): Def8_0[I0, I1, I2, I3, I4, I5, I6, I7] = {
      val javaProc = underlying.build()
      new Def8_0[I0, I1, I2, I3, I4, I5, I6, I7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] =
      new Builder_9_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] =
      new Builder_8_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_2[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, X] =
      new Builder_9_2(underlying.inout(tpe.underlying))

    def build(): Def8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] = {
      val javaProc = underlying.build()
      new Def8_1[I0, I1, I2, I3, I4, I5, I6, I7, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] =
      new Builder_9_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] =
      new Builder_8_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_3[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, X] =
      new Builder_9_3(underlying.inout(tpe.underlying))

    def build(): Def8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] = {
      val javaProc = underlying.build()
      new Def8_2[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] =
      new Builder_9_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] =
      new Builder_8_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_4[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, X] =
      new Builder_9_4(underlying.inout(tpe.underlying))

    def build(): Def8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def8_3[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] =
      new Builder_9_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] =
      new Builder_8_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_5[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, X] =
      new Builder_9_5(underlying.inout(tpe.underlying))

    def build(): Def8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def8_4[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] =
      new Builder_9_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] =
      new Builder_8_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_6[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, X] =
      new Builder_9_6(underlying.inout(tpe.underlying))

    def build(): Def8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def8_5[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] =
      new Builder_9_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_8_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_7[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_9_7(underlying.inout(tpe.underlying))

    def build(): Def8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def8_6[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_9_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_8_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_8[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_9_8(underlying.inout(tpe.underlying))

    def build(): Def8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def8_7[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_9_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_8_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_9[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_9_9(underlying.inout(tpe.underlying))

    def build(): Def8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def8_8[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_9_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_8_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_9_10[I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_9_10(underlying.inout(tpe.underlying))

    def build(): Def8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def8_9[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_9_10(underlying.in(tpe.underlying))

    def build(): Def8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def8_10[I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9] =
      new Builder_10_0(underlying.in(tpe.underlying))
    def out[O0](tpe: DbType[O0]): Builder_9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] =
      new Builder_9_1(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, X] =
      new Builder_10_1(underlying.inout(tpe.underlying))

    def build(): Def9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8] = {
      val javaProc = underlying.build()
      new Def9_0[I0, I1, I2, I3, I4, I5, I6, I7, I8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] =
      new Builder_10_1(underlying.in(tpe.underlying))
    def out[O1](tpe: DbType[O1]): Builder_9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] =
      new Builder_9_2(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, X] =
      new Builder_10_2(underlying.inout(tpe.underlying))

    def build(): Def9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] = {
      val javaProc = underlying.build()
      new Def9_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] =
      new Builder_10_2(underlying.in(tpe.underlying))
    def out[O2](tpe: DbType[O2]): Builder_9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] =
      new Builder_9_3(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, X] =
      new Builder_10_3(underlying.inout(tpe.underlying))

    def build(): Def9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] = {
      val javaProc = underlying.build()
      new Def9_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] =
      new Builder_10_3(underlying.in(tpe.underlying))
    def out[O3](tpe: DbType[O3]): Builder_9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] =
      new Builder_9_4(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, X] =
      new Builder_10_4(underlying.inout(tpe.underlying))

    def build(): Def9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def9_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] =
      new Builder_10_4(underlying.in(tpe.underlying))
    def out[O4](tpe: DbType[O4]): Builder_9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] =
      new Builder_9_5(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, X] =
      new Builder_10_5(underlying.inout(tpe.underlying))

    def build(): Def9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def9_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] =
      new Builder_10_5(underlying.in(tpe.underlying))
    def out[O5](tpe: DbType[O5]): Builder_9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] =
      new Builder_9_6(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, X] =
      new Builder_10_6(underlying.inout(tpe.underlying))

    def build(): Def9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def9_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] =
      new Builder_10_6(underlying.in(tpe.underlying))
    def out[O6](tpe: DbType[O6]): Builder_9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_9_7(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, X] =
      new Builder_10_7(underlying.inout(tpe.underlying))

    def build(): Def9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def9_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_10_7(underlying.in(tpe.underlying))
    def out[O7](tpe: DbType[O7]): Builder_9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_9_8(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, X] =
      new Builder_10_8(underlying.inout(tpe.underlying))

    def build(): Def9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def9_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_10_8(underlying.in(tpe.underlying))
    def out[O8](tpe: DbType[O8]): Builder_9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_9_9(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, X] =
      new Builder_10_9(underlying.inout(tpe.underlying))

    def build(): Def9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def9_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_10_9(underlying.in(tpe.underlying))
    def out[O9](tpe: DbType[O9]): Builder_9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_9_10(underlying.out(tpe.underlying))
    def inout[X](tpe: DbType[X]): Builder_10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X] =
      new Builder_10_10(underlying.inout(tpe.underlying))

    def build(): Def9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def9_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_10_10(underlying.in(tpe.underlying))

    def build(): Def9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def9_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }

  class Builder_10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9]
  ) {
    def out[O0](tpe: DbType[O0]): Builder_10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] =
      new Builder_10_1(underlying.out(tpe.underlying))

    def build(): Def10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9] = {
      val javaProc = underlying.build()
      new Def10_0[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[Unit] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _ => ())
      }
    }
  }

  class Builder_10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0]
  ) {
    def out[O1](tpe: DbType[O1]): Builder_10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] =
      new Builder_10_2(underlying.out(tpe.underlying))

    def build(): Def10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] = {
      val javaProc = underlying.build()
      new Def10_1[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[O0] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[O0])
      }
    }
  }

  class Builder_10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1]
  ) {
    def out[O2](tpe: DbType[O2]): Builder_10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] =
      new Builder_10_3(underlying.out(tpe.underlying))

    def build(): Def10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] = {
      val javaProc = underlying.build()
      new Def10_2[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple2[O0, O1]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple2[O0, O1]])
      }
    }
  }

  class Builder_10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2]
  ) {
    def out[O3](tpe: DbType[O3]): Builder_10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] =
      new Builder_10_4(underlying.out(tpe.underlying))

    def build(): Def10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] = {
      val javaProc = underlying.build()
      new Def10_3[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple3[O0, O1, O2]])
      }
    }
  }

  class Builder_10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3]
  ) {
    def out[O4](tpe: DbType[O4]): Builder_10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] =
      new Builder_10_5(underlying.out(tpe.underlying))

    def build(): Def10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] = {
      val javaProc = underlying.build()
      new Def10_4[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple4[O0, O1, O2, O3]])
      }
    }
  }

  class Builder_10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4]
  ) {
    def out[O5](tpe: DbType[O5]): Builder_10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] =
      new Builder_10_6(underlying.out(tpe.underlying))

    def build(): Def10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] = {
      val javaProc = underlying.build()
      new Def10_5[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple5[O0, O1, O2, O3, O4]])
      }
    }
  }

  class Builder_10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5]
  ) {
    def out[O6](tpe: DbType[O6]): Builder_10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] =
      new Builder_10_7(underlying.out(tpe.underlying))

    def build(): Def10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] = {
      val javaProc = underlying.build()
      new Def10_6[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple6[O0, O1, O2, O3, O4, O5]])
      }
    }
  }

  class Builder_10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6]
  ) {
    def out[O7](tpe: DbType[O7]): Builder_10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] =
      new Builder_10_8(underlying.out(tpe.underlying))

    def build(): Def10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] = {
      val javaProc = underlying.build()
      new Def10_7[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple7[O0, O1, O2, O3, O4, O5, O6]])
      }
    }
  }

  class Builder_10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7]
  ) {
    def out[O8](tpe: DbType[O8]): Builder_10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] =
      new Builder_10_9(underlying.out(tpe.underlying))

    def build(): Def10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] = {
      val javaProc = underlying.build()
      new Def10_8[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple8[O0, O1, O2, O3, O4, O5, O6, O7]])
      }
    }
  }

  class Builder_10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8]
  ) {
    def out[O9](tpe: DbType[O9]): Builder_10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] =
      new Builder_10_10(underlying.out(tpe.underlying))

    def build(): Def10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] = {
      val javaProc = underlying.build()
      new Def10_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple9[O0, O1, O2, O3, O4, O5, O6, O7, O8]])
      }
    }
  }

  class Builder_10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbProcedure.Builder_10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]
  ) {

    def build(): Def10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] = {
      val javaProc = underlying.build()
      new Def10_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]] =
          new ProcedureOp(javaProc.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[dev.typr.foundations.Tuple.Tuple10[O0, O1, O2, O3, O4, O5, O6, O7, O8, O9]])
      }
    }
  }
}
