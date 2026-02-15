package dev.typr.scalafoundations

/** Type-safe stored function definitions with fully typed inputs.
  *
  * Usage:
  * {{{
  * val calcTax: DbFunction.Def2[BigDecimal, String, BigDecimal] = DbFunction.define("calculate_tax", PgTypes.numeric)
  *   .in(PgTypes.numeric)
  *   .in(PgTypes.text)
  *   .build()
  * val tax = calcTax.call(amount, "US").transact(tx)  // Types enforced!
  * }}}
  *
  * @see [[DbProcedure]] for stored procedures (with OUT/INOUT parameters)
  */
object DbFunction {

  /** Start defining a stored function (single return value, uses SELECT). */
  def define[R](name: String, returnType: DbType[R]): Builder_0[R] =
    new Builder_0(dev.typr.foundations.DbFunction.define(name, returnType.underlying))

  // ─────────────────────────────────────────────────────────────────────────────
  // Function definition interfaces (11 total: 0-10 inputs)
  // ─────────────────────────────────────────────────────────────────────────────

  /** Function definition with 0 input(s). */
  trait Def0[R] {
    def call(): ProcedureOp[R]
  }

  /** Function definition with 1 input(s). */
  trait Def1[I0, R] {
    def call(i0: I0): ProcedureOp[R]
  }

  /** Function definition with 2 input(s). */
  trait Def2[I0, I1, R] {
    def call(i0: I0, i1: I1): ProcedureOp[R]
  }

  /** Function definition with 3 input(s). */
  trait Def3[I0, I1, I2, R] {
    def call(i0: I0, i1: I1, i2: I2): ProcedureOp[R]
  }

  /** Function definition with 4 input(s). */
  trait Def4[I0, I1, I2, I3, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[R]
  }

  /** Function definition with 5 input(s). */
  trait Def5[I0, I1, I2, I3, I4, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[R]
  }

  /** Function definition with 6 input(s). */
  trait Def6[I0, I1, I2, I3, I4, I5, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[R]
  }

  /** Function definition with 7 input(s). */
  trait Def7[I0, I1, I2, I3, I4, I5, I6, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[R]
  }

  /** Function definition with 8 input(s). */
  trait Def8[I0, I1, I2, I3, I4, I5, I6, I7, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[R]
  }

  /** Function definition with 9 input(s). */
  trait Def9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[R]
  }

  /** Function definition with 10 input(s). */
  trait Def10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R] {
    def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[R]
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Function builders (11 total: 0-10 inputs)
  // ─────────────────────────────────────────────────────────────────────────────

  class Builder_0[R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_0[R]
  ) {
    def in[I0](tpe: DbType[I0]): Builder_1[I0, R] =
      new Builder_1(underlying.in(tpe.underlying))

    def build(): Def0[R] = {
      val javaFn = underlying.build()
      new Def0[R] {
        def call(): ProcedureOp[R] =
          new ProcedureOp(javaFn.call().asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_1[I0, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_1[I0, R]
  ) {
    def in[I1](tpe: DbType[I1]): Builder_2[I0, I1, R] =
      new Builder_2(underlying.in(tpe.underlying))

    def build(): Def1[I0, R] = {
      val javaFn = underlying.build()
      new Def1[I0, R] {
        def call(i0: I0): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_2[I0, I1, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_2[I0, I1, R]
  ) {
    def in[I2](tpe: DbType[I2]): Builder_3[I0, I1, I2, R] =
      new Builder_3(underlying.in(tpe.underlying))

    def build(): Def2[I0, I1, R] = {
      val javaFn = underlying.build()
      new Def2[I0, I1, R] {
        def call(i0: I0, i1: I1): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_3[I0, I1, I2, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_3[I0, I1, I2, R]
  ) {
    def in[I3](tpe: DbType[I3]): Builder_4[I0, I1, I2, I3, R] =
      new Builder_4(underlying.in(tpe.underlying))

    def build(): Def3[I0, I1, I2, R] = {
      val javaFn = underlying.build()
      new Def3[I0, I1, I2, R] {
        def call(i0: I0, i1: I1, i2: I2): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_4[I0, I1, I2, I3, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_4[I0, I1, I2, I3, R]
  ) {
    def in[I4](tpe: DbType[I4]): Builder_5[I0, I1, I2, I3, I4, R] =
      new Builder_5(underlying.in(tpe.underlying))

    def build(): Def4[I0, I1, I2, I3, R] = {
      val javaFn = underlying.build()
      new Def4[I0, I1, I2, I3, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_5[I0, I1, I2, I3, I4, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_5[I0, I1, I2, I3, I4, R]
  ) {
    def in[I5](tpe: DbType[I5]): Builder_6[I0, I1, I2, I3, I4, I5, R] =
      new Builder_6(underlying.in(tpe.underlying))

    def build(): Def5[I0, I1, I2, I3, I4, R] = {
      val javaFn = underlying.build()
      new Def5[I0, I1, I2, I3, I4, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_6[I0, I1, I2, I3, I4, I5, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_6[I0, I1, I2, I3, I4, I5, R]
  ) {
    def in[I6](tpe: DbType[I6]): Builder_7[I0, I1, I2, I3, I4, I5, I6, R] =
      new Builder_7(underlying.in(tpe.underlying))

    def build(): Def6[I0, I1, I2, I3, I4, I5, R] = {
      val javaFn = underlying.build()
      new Def6[I0, I1, I2, I3, I4, I5, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_7[I0, I1, I2, I3, I4, I5, I6, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_7[I0, I1, I2, I3, I4, I5, I6, R]
  ) {
    def in[I7](tpe: DbType[I7]): Builder_8[I0, I1, I2, I3, I4, I5, I6, I7, R] =
      new Builder_8(underlying.in(tpe.underlying))

    def build(): Def7[I0, I1, I2, I3, I4, I5, I6, R] = {
      val javaFn = underlying.build()
      new Def7[I0, I1, I2, I3, I4, I5, I6, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_8[I0, I1, I2, I3, I4, I5, I6, I7, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_8[I0, I1, I2, I3, I4, I5, I6, I7, R]
  ) {
    def in[I8](tpe: DbType[I8]): Builder_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R] =
      new Builder_9(underlying.in(tpe.underlying))

    def build(): Def8[I0, I1, I2, I3, I4, I5, I6, I7, R] = {
      val javaFn = underlying.build()
      new Def8[I0, I1, I2, I3, I4, I5, I6, I7, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R]
  ) {
    def in[I9](tpe: DbType[I9]): Builder_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R] =
      new Builder_10(underlying.in(tpe.underlying))

    def build(): Def9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R] = {
      val javaFn = underlying.build()
      new Def9[I0, I1, I2, I3, I4, I5, I6, I7, I8, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7, i8).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }

  class Builder_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R] private[scalafoundations] (
    private val underlying: dev.typr.foundations.DbFunction.Builder_10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R]
  ) {

    def build(): Def10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R] = {
      val javaFn = underlying.build()
      new Def10[I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R] {
        def call(i0: I0, i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9): ProcedureOp[R] =
          new ProcedureOp(javaFn.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
      }
    }
  }
}
