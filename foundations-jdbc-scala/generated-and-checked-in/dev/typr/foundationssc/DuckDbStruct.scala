package dev.typr.foundationssc

class DuckDbStruct[A](val underlying: dev.typr.foundations.DuckDbStruct[A]):
  def asType(): DuckDbType[A] = DuckDbType(underlying.asType())

object DuckDbStruct:
  def builder[A](structName: String): Builder0[A] =
    Builder0(dev.typr.foundations.DuckDbStructBuilders.builder(structName))

  class Builder0[A] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder0[A]
  ):
    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder1[A, F] =
      Builder1(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder1[A, T0] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder1[A, T0]
  ):
    def build(decode: (T0) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0) => decode(t0)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder2[A, T0, F] =
      Builder2(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder2[A, T0, T1] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder2[A, T0, T1]
  ):
    def build(decode: (T0, T1) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1) => decode(t0, t1)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder3[A, T0, T1, F] =
      Builder3(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder3[A, T0, T1, T2] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder3[A, T0, T1, T2]
  ):
    def build(decode: (T0, T1, T2) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2) => decode(t0, t1, t2)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder4[A, T0, T1, T2, F] =
      Builder4(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder4[A, T0, T1, T2, T3] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder4[A, T0, T1, T2, T3]
  ):
    def build(decode: (T0, T1, T2, T3) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3) => decode(t0, t1, t2, t3)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder5[A, T0, T1, T2, T3, F] =
      Builder5(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder5[A, T0, T1, T2, T3, T4] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder5[A, T0, T1, T2, T3, T4]
  ):
    def build(decode: (T0, T1, T2, T3, T4) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4) => decode(t0, t1, t2, t3, t4)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder6[A, T0, T1, T2, T3, T4, F] =
      Builder6(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder6[A, T0, T1, T2, T3, T4, T5] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder6[A, T0, T1, T2, T3, T4, T5]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5) => decode(t0, t1, t2, t3, t4, t5)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder7[A, T0, T1, T2, T3, T4, T5, F] =
      Builder7(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder7[A, T0, T1, T2, T3, T4, T5, T6] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder7[A, T0, T1, T2, T3, T4, T5, T6]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6) => decode(t0, t1, t2, t3, t4, t5, t6)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder8[A, T0, T1, T2, T3, T4, T5, T6, F] =
      Builder8(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder8[A, T0, T1, T2, T3, T4, T5, T6, T7] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder8[A, T0, T1, T2, T3, T4, T5, T6, T7]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7) => decode(t0, t1, t2, t3, t4, t5, t6, t7)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, F] =
      Builder9(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, T8] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, T8]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F] =
      Builder10(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F] =
      Builder11(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F] =
      Builder12(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F] =
      Builder13(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F] =
      Builder14(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F] =
      Builder15(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F] =
      Builder16(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F] =
      Builder17(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F] =
      Builder18(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F] =
      Builder19(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F] =
      Builder20(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F] =
      Builder21(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F] =
      Builder22(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F] =
      Builder23(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F] =
      Builder24(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F] =
      Builder25(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F] =
      Builder26(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F] =
      Builder27(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F] =
      Builder28(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F] =
      Builder29(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28)))

    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F] =
      Builder30(underlying.field(name, tpe.underlying, a => getter(a)))

  class Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29] private[foundationssc] (
    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) => A): DuckDbStruct[A] =
      DuckDbStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29)))

