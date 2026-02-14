package dev.typr.scalafoundations

object ParamBuilders:
  class ParamBuilder1[P0] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder1[P0]
  ):
    def append(s: String): ParamBuilder1[P0] = new ParamBuilder1(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder1[P0] = new ParamBuilder1(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder1[P0] = new ParamBuilder1(underlying.append(fragment.underlying))

    def param[P1](tpe: DbType[P1]): ParamBuilder2[P0, P1] =
      new ParamBuilder2(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query1[P0, Out] =
      new SqlTemplate.Query1(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update1[P0] =
      new SqlTemplate.Update1(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder2[P0, P1] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder2[P0, P1]
  ):
    def append(s: String): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.append(fragment.underlying))

    def param[P2](tpe: DbType[P2]): ParamBuilder3[P0, P1, P2] =
      new ParamBuilder3(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query2[P0, P1, Out] =
      new SqlTemplate.Query2(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update2[P0, P1] =
      new SqlTemplate.Update2(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder3[P0, P1, P2] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder3[P0, P1, P2]
  ):
    def append(s: String): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.append(fragment.underlying))

    def param[P3](tpe: DbType[P3]): ParamBuilder4[P0, P1, P2, P3] =
      new ParamBuilder4(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query3[P0, P1, P2, Out] =
      new SqlTemplate.Query3(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update3[P0, P1, P2] =
      new SqlTemplate.Update3(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder4[P0, P1, P2, P3] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder4[P0, P1, P2, P3]
  ):
    def append(s: String): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.append(fragment.underlying))

    def param[P4](tpe: DbType[P4]): ParamBuilder5[P0, P1, P2, P3, P4] =
      new ParamBuilder5(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query4[P0, P1, P2, P3, Out] =
      new SqlTemplate.Query4(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update4[P0, P1, P2, P3] =
      new SqlTemplate.Update4(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder5[P0, P1, P2, P3, P4] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder5[P0, P1, P2, P3, P4]
  ):
    def append(s: String): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.append(fragment.underlying))

    def param[P5](tpe: DbType[P5]): ParamBuilder6[P0, P1, P2, P3, P4, P5] =
      new ParamBuilder6(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query5[P0, P1, P2, P3, P4, Out] =
      new SqlTemplate.Query5(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update5[P0, P1, P2, P3, P4] =
      new SqlTemplate.Update5(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder6[P0, P1, P2, P3, P4, P5] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder6[P0, P1, P2, P3, P4, P5]
  ):
    def append(s: String): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.append(fragment.underlying))

    def param[P6](tpe: DbType[P6]): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] =
      new ParamBuilder7(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query6[P0, P1, P2, P3, P4, P5, Out] =
      new SqlTemplate.Query6(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update6[P0, P1, P2, P3, P4, P5] =
      new SqlTemplate.Update6(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder7[P0, P1, P2, P3, P4, P5, P6]
  ):
    def append(s: String): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.append(fragment.underlying))

    def param[P7](tpe: DbType[P7]): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] =
      new ParamBuilder8(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query7[P0, P1, P2, P3, P4, P5, P6, Out] =
      new SqlTemplate.Query7(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update7[P0, P1, P2, P3, P4, P5, P6] =
      new SqlTemplate.Update7(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7]
  ):
    def append(s: String): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.append(fragment.underlying))

    def param[P8](tpe: DbType[P8]): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] =
      new ParamBuilder9(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query8[P0, P1, P2, P3, P4, P5, P6, P7, Out] =
      new SqlTemplate.Query8(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update8[P0, P1, P2, P3, P4, P5, P6, P7] =
      new SqlTemplate.Update8(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8]
  ):
    def append(s: String): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.append(fragment.underlying))

    def param[P9](tpe: DbType[P9]): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] =
      new ParamBuilder10(underlying.param(tpe.underlying))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query9[P0, P1, P2, P3, P4, P5, P6, P7, P8, Out] =
      new SqlTemplate.Query9(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update9[P0, P1, P2, P3, P4, P5, P6, P7, P8] =
      new SqlTemplate.Update9(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] private[scalafoundations] (
    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9]
  ):
    def append(s: String): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.append(s))

    def value[T](tpe: DbType[T], value: T): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.value(tpe.underlying, value))

    def append(fragment: Fragment): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.append(fragment.underlying))

    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out] =
      new SqlTemplate.Query10(underlying.query(parser.underlying))

    def update(): SqlTemplate.Update10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] =
      new SqlTemplate.Update10(underlying.update())

    def done(): Fragment = new Fragment(underlying.done())
