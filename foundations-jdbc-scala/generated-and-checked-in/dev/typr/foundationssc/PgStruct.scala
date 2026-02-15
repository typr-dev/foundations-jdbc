package dev.typr.scalafoundations

class PgStruct[A](val underlying: dev.typr.foundations.PgStruct[A]):
  def asType(): PgType[A] = PgType(underlying.asType())

object PgStruct:
  def builder[A](typeName: String): Builder0[A] =
    Builder0(dev.typr.foundations.PgStructBuilders.builder(typeName))

  class Builder0[A] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder0[A]
  ):
    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder1[A, F] =
      Builder1(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder1[A, F] =
      Builder1(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder1[A, T0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder1[A, T0]
  ):
    def build(decode: (T0) => A): PgStruct[A] =
      PgStruct(underlying.build((t0) => decode(t0)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder2[A, T0, F] =
      Builder2(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder2[A, T0, F] =
      Builder2(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder2[A, T0, T1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder2[A, T0, T1]
  ):
    def build(decode: (T0, T1) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1) => decode(t0, t1)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder3[A, T0, T1, F] =
      Builder3(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder3[A, T0, T1, F] =
      Builder3(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder3[A, T0, T1, T2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder3[A, T0, T1, T2]
  ):
    def build(decode: (T0, T1, T2) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2) => decode(t0, t1, t2)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder4[A, T0, T1, T2, F] =
      Builder4(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder4[A, T0, T1, T2, F] =
      Builder4(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder4[A, T0, T1, T2, T3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder4[A, T0, T1, T2, T3]
  ):
    def build(decode: (T0, T1, T2, T3) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3) => decode(t0, t1, t2, t3)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder5[A, T0, T1, T2, T3, F] =
      Builder5(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder5[A, T0, T1, T2, T3, F] =
      Builder5(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder5[A, T0, T1, T2, T3, T4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder5[A, T0, T1, T2, T3, T4]
  ):
    def build(decode: (T0, T1, T2, T3, T4) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4) => decode(t0, t1, t2, t3, t4)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder6[A, T0, T1, T2, T3, T4, F] =
      Builder6(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder6[A, T0, T1, T2, T3, T4, F] =
      Builder6(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder6[A, T0, T1, T2, T3, T4, T5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder6[A, T0, T1, T2, T3, T4, T5]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5) => decode(t0, t1, t2, t3, t4, t5)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder7[A, T0, T1, T2, T3, T4, T5, F] =
      Builder7(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder7[A, T0, T1, T2, T3, T4, T5, F] =
      Builder7(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder7[A, T0, T1, T2, T3, T4, T5, T6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder7[A, T0, T1, T2, T3, T4, T5, T6]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6) => decode(t0, t1, t2, t3, t4, t5, t6)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder8[A, T0, T1, T2, T3, T4, T5, T6, F] =
      Builder8(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder8[A, T0, T1, T2, T3, T4, T5, T6, F] =
      Builder8(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder8[A, T0, T1, T2, T3, T4, T5, T6, T7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder8[A, T0, T1, T2, T3, T4, T5, T6, T7]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7) => decode(t0, t1, t2, t3, t4, t5, t6, t7)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, F] =
      Builder9(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, F] =
      Builder9(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, T8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder9[A, T0, T1, T2, T3, T4, T5, T6, T7, T8]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F] =
      Builder10(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F] =
      Builder10(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder10[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F] =
      Builder11(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F] =
      Builder11(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder11[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F] =
      Builder12(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F] =
      Builder12(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder12[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F] =
      Builder13(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F] =
      Builder13(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder13[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F] =
      Builder14(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F] =
      Builder14(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder14[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F] =
      Builder15(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F] =
      Builder15(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder15[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F] =
      Builder16(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F] =
      Builder16(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder16[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F] =
      Builder17(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F] =
      Builder17(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder17[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F] =
      Builder18(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F] =
      Builder18(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder18[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F] =
      Builder19(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F] =
      Builder19(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder19[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F] =
      Builder20(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F] =
      Builder20(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder20[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F] =
      Builder21(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F] =
      Builder21(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder21[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F] =
      Builder22(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F] =
      Builder22(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder22[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F] =
      Builder23(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F] =
      Builder23(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder23[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F] =
      Builder24(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F] =
      Builder24(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder24[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F] =
      Builder25(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F] =
      Builder25(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder25[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F] =
      Builder26(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F] =
      Builder26(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder26[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F] =
      Builder27(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F] =
      Builder27(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder27[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F] =
      Builder28(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F] =
      Builder28(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder28[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F] =
      Builder29(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F] =
      Builder29(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder29[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28)))

    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F] =
      Builder30(underlying.field(name, tpe.underlying, a => getter(a)))
    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F] =
      Builder30(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))

  class Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29] private[scalafoundations] (
    private val underlying: dev.typr.foundations.PgStructBuilders.Builder30[A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29]
  ):
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) => A): PgStruct[A] =
      PgStruct(underlying.build((t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29) => decode(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29)))

