package dev.typr.foundationssc

object ParamBuilders:
  class ParamBuilder1[P0] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder1[?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder1[?]) = this(u, List.fill(1)(None))

    def append(s: String): ParamBuilder1[P0] = new ParamBuilder1(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder1[P0] = new ParamBuilder1(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder1[P0] = new ParamBuilder1(underlying.append(fragment.underlying), transforms)

    def param[P1](tpe: DbType[P1]): ParamBuilder2[P0, P1] =
      new ParamBuilder2(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder2[P0, Boolean] =
      new ParamBuilder2(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder2[P0, Option[A]] =
      new ParamBuilder2(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder2[P0, Option[(A, B)]] =
      new ParamBuilder2(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder2[P0, Option[(A, B, C)]] =
      new ParamBuilder2(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query1[P0, Out] =
      new SqlTemplate.Query1(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update1[P0] =
      new SqlTemplate.Update1(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder2[P0, P1] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder2[?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder2[?, ?]) = this(u, List.fill(2)(None))

    def append(s: String): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder2[P0, P1] = new ParamBuilder2(underlying.append(fragment.underlying), transforms)

    def param[P2](tpe: DbType[P2]): ParamBuilder3[P0, P1, P2] =
      new ParamBuilder3(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder3[P0, P1, Boolean] =
      new ParamBuilder3(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder3[P0, P1, Option[A]] =
      new ParamBuilder3(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder3[P0, P1, Option[(A, B)]] =
      new ParamBuilder3(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder3[P0, P1, Option[(A, B, C)]] =
      new ParamBuilder3(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query2[P0, P1, Out] =
      new SqlTemplate.Query2(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update2[P0, P1] =
      new SqlTemplate.Update2(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder3[P0, P1, P2] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder3[?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder3[?, ?, ?]) = this(u, List.fill(3)(None))

    def append(s: String): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder3[P0, P1, P2] = new ParamBuilder3(underlying.append(fragment.underlying), transforms)

    def param[P3](tpe: DbType[P3]): ParamBuilder4[P0, P1, P2, P3] =
      new ParamBuilder4(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder4[P0, P1, P2, Boolean] =
      new ParamBuilder4(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder4[P0, P1, P2, Option[A]] =
      new ParamBuilder4(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder4[P0, P1, P2, Option[(A, B)]] =
      new ParamBuilder4(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder4[P0, P1, P2, Option[(A, B, C)]] =
      new ParamBuilder4(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query3[P0, P1, P2, Out] =
      new SqlTemplate.Query3(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update3[P0, P1, P2] =
      new SqlTemplate.Update3(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder4[P0, P1, P2, P3] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder4[?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder4[?, ?, ?, ?]) = this(u, List.fill(4)(None))

    def append(s: String): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder4[P0, P1, P2, P3] = new ParamBuilder4(underlying.append(fragment.underlying), transforms)

    def param[P4](tpe: DbType[P4]): ParamBuilder5[P0, P1, P2, P3, P4] =
      new ParamBuilder5(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder5[P0, P1, P2, P3, Boolean] =
      new ParamBuilder5(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder5[P0, P1, P2, P3, Option[A]] =
      new ParamBuilder5(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder5[P0, P1, P2, P3, Option[(A, B)]] =
      new ParamBuilder5(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder5[P0, P1, P2, P3, Option[(A, B, C)]] =
      new ParamBuilder5(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query4[P0, P1, P2, P3, Out] =
      new SqlTemplate.Query4(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update4[P0, P1, P2, P3] =
      new SqlTemplate.Update4(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder5[P0, P1, P2, P3, P4] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder5[?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder5[?, ?, ?, ?, ?]) = this(u, List.fill(5)(None))

    def append(s: String): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder5[P0, P1, P2, P3, P4] = new ParamBuilder5(underlying.append(fragment.underlying), transforms)

    def param[P5](tpe: DbType[P5]): ParamBuilder6[P0, P1, P2, P3, P4, P5] =
      new ParamBuilder6(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder6[P0, P1, P2, P3, P4, Boolean] =
      new ParamBuilder6(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder6[P0, P1, P2, P3, P4, Option[A]] =
      new ParamBuilder6(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder6[P0, P1, P2, P3, P4, Option[(A, B)]] =
      new ParamBuilder6(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder6[P0, P1, P2, P3, P4, Option[(A, B, C)]] =
      new ParamBuilder6(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query5[P0, P1, P2, P3, P4, Out] =
      new SqlTemplate.Query5(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update5[P0, P1, P2, P3, P4] =
      new SqlTemplate.Update5(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder6[P0, P1, P2, P3, P4, P5] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder6[?, ?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder6[?, ?, ?, ?, ?, ?]) = this(u, List.fill(6)(None))

    def append(s: String): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder6[P0, P1, P2, P3, P4, P5] = new ParamBuilder6(underlying.append(fragment.underlying), transforms)

    def param[P6](tpe: DbType[P6]): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] =
      new ParamBuilder7(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder7[P0, P1, P2, P3, P4, P5, Boolean] =
      new ParamBuilder7(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder7[P0, P1, P2, P3, P4, P5, Option[A]] =
      new ParamBuilder7(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder7[P0, P1, P2, P3, P4, P5, Option[(A, B)]] =
      new ParamBuilder7(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder7[P0, P1, P2, P3, P4, P5, Option[(A, B, C)]] =
      new ParamBuilder7(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query6[P0, P1, P2, P3, P4, P5, Out] =
      new SqlTemplate.Query6(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update6[P0, P1, P2, P3, P4, P5] =
      new SqlTemplate.Update6(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder7[?, ?, ?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder7[?, ?, ?, ?, ?, ?, ?]) = this(u, List.fill(7)(None))

    def append(s: String): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder7[P0, P1, P2, P3, P4, P5, P6] = new ParamBuilder7(underlying.append(fragment.underlying), transforms)

    def param[P7](tpe: DbType[P7]): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] =
      new ParamBuilder8(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, Boolean] =
      new ParamBuilder8(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, Option[A]] =
      new ParamBuilder8(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, Option[(A, B)]] =
      new ParamBuilder8(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, Option[(A, B, C)]] =
      new ParamBuilder8(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query7[P0, P1, P2, P3, P4, P5, P6, Out] =
      new SqlTemplate.Query7(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update7[P0, P1, P2, P3, P4, P5, P6] =
      new SqlTemplate.Update7(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder8[?, ?, ?, ?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder8[?, ?, ?, ?, ?, ?, ?, ?]) = this(u, List.fill(8)(None))

    def append(s: String): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder8[P0, P1, P2, P3, P4, P5, P6, P7] = new ParamBuilder8(underlying.append(fragment.underlying), transforms)

    def param[P8](tpe: DbType[P8]): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] =
      new ParamBuilder9(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, Boolean] =
      new ParamBuilder9(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, Option[A]] =
      new ParamBuilder9(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, Option[(A, B)]] =
      new ParamBuilder9(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, Option[(A, B, C)]] =
      new ParamBuilder9(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query8[P0, P1, P2, P3, P4, P5, P6, P7, Out] =
      new SqlTemplate.Query8(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update8[P0, P1, P2, P3, P4, P5, P6, P7] =
      new SqlTemplate.Update8(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder9[?, ?, ?, ?, ?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder9[?, ?, ?, ?, ?, ?, ?, ?, ?]) = this(u, List.fill(9)(None))

    def append(s: String): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder9[P0, P1, P2, P3, P4, P5, P6, P7, P8] = new ParamBuilder9(underlying.append(fragment.underlying), transforms)

    def param[P9](tpe: DbType[P9]): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] =
      new ParamBuilder10(underlying.param(tpe.underlying), transforms :+ None)
    def optionally(inner: Fragment): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, Boolean] =
      new ParamBuilder10(underlying.optionally(inner.underlying), transforms :+ None)

    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, Option[A]] =
      new ParamBuilder10(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
        transforms :+ Some(OptionallyTransforms.optionToOptional))

    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, Option[(A, B)]] =
      new ParamBuilder10(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))

    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, Option[(A, B, C)]] =
      new ParamBuilder10(
        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))
    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query9[P0, P1, P2, P3, P4, P5, P6, P7, P8, Out] =
      new SqlTemplate.Query9(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update9[P0, P1, P2, P3, P4, P5, P6, P7, P8] =
      new SqlTemplate.Update9(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())

  class ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?],
    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
  ):
    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?]) = this(u, List.fill(10)(None))

    def append(s: String): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.append(s), transforms)

    def value[T](tpe: DbType[T], value: T): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.value(tpe.underlying, value), transforms)

    def append(fragment: Fragment): ParamBuilder10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] = new ParamBuilder10(underlying.append(fragment.underlying), transforms)

    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out] =
      new SqlTemplate.Query10(underlying.query(parser.underlying), transforms)

    def update(): SqlTemplate.Update10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9] =
      new SqlTemplate.Update10(underlying.update(), transforms)

    def done(): Fragment = new Fragment(underlying.done())
