package dev.typr.foundationssc

sealed trait Template[In, Out] extends Analyzable:
  def underlying: dev.typr.foundations.Template[?, ?]

  override def analyzable: dev.typr.foundations.Analyzable = underlying

  def on(input: In): Operation[Out]

  def fragment: Fragment = new Fragment(underlying.fragment())

object Template:

  class Query1[P0, Out](
    private val _java: dev.typr.foundations.Template.Query1[?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[P0, Out]:
    def this(j: dev.typr.foundations.Template.Query1[?, Out]) = this(j, List(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: P0): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(input.asInstanceOf[AnyRef])).getOrElse(input.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0): From[T, Out] =
      new From(_java, (t: T) => on(f0(t)))

  class Query2[P0, P1, Out](
    private val _java: dev.typr.foundations.Template.Query2[?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1), Out]:
    def this(j: dev.typr.foundations.Template.Query2[?, ?, Out]) = this(j, List.fill(2)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1)): Operation.Query[Out] =
      on(input._1, input._2)
    def on(p0: P0, p1: P1): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t)))

  class Query3[P0, P1, P2, Out](
    private val _java: dev.typr.foundations.Template.Query3[?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2), Out]:
    def this(j: dev.typr.foundations.Template.Query3[?, ?, ?, Out]) = this(j, List.fill(3)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2)): Operation.Query[Out] =
      on(input._1, input._2, input._3)
    def on(p0: P0, p1: P1, p2: P2): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t)))

  class Query4[P0, P1, P2, P3, Out](
    private val _java: dev.typr.foundations.Template.Query4[?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3), Out]:
    def this(j: dev.typr.foundations.Template.Query4[?, ?, ?, ?, Out]) = this(j, List.fill(4)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4)
    def on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t)))

  class Query5[P0, P1, P2, P3, P4, Out](
    private val _java: dev.typr.foundations.Template.Query5[?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4), Out]:
    def this(j: dev.typr.foundations.Template.Query5[?, ?, ?, ?, ?, Out]) = this(j, List.fill(5)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t)))

  class Query6[P0, P1, P2, P3, P4, P5, Out](
    private val _java: dev.typr.foundations.Template.Query6[?, ?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5), Out]:
    def this(j: dev.typr.foundations.Template.Query6[?, ?, ?, ?, ?, ?, Out]) = this(j, List.fill(6)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5, input._6)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)))

  class Query7[P0, P1, P2, P3, P4, P5, P6, Out](
    private val _java: dev.typr.foundations.Template.Query7[?, ?, ?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6), Out]:
    def this(j: dev.typr.foundations.Template.Query7[?, ?, ?, ?, ?, ?, ?, Out]) = this(j, List.fill(7)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)))

  class Query8[P0, P1, P2, P3, P4, P5, P6, P7, Out](
    private val _java: dev.typr.foundations.Template.Query8[?, ?, ?, ?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7), Out]:
    def this(j: dev.typr.foundations.Template.Query8[?, ?, ?, ?, ?, ?, ?, ?, Out]) = this(j, List.fill(8)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)))

  class Query9[P0, P1, P2, P3, P4, P5, P6, P7, P8, Out](
    private val _java: dev.typr.foundations.Template.Query9[?, ?, ?, ?, ?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7, P8), Out]:
    def this(j: dev.typr.foundations.Template.Query9[?, ?, ?, ?, ?, ?, ?, ?, ?, Out]) = this(j, List.fill(9)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val v8: AnyRef = _transforms(8).map(_(p8.asInstanceOf[AnyRef])).getOrElse(p8.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7, f8: T => P8): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)))

  class Query10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out](
    private val _java: dev.typr.foundations.Template.Query10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?, Out],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7, P8, P9), Out]:
    def this(j: dev.typr.foundations.Template.Query10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?, Out]) = this(j, List.fill(10)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9)): Operation.Query[Out] =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9, input._10)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Query[Out] =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val v8: AnyRef = _transforms(8).map(_(p8.asInstanceOf[AnyRef])).getOrElse(p8.asInstanceOf[AnyRef])
      val v9: AnyRef = _transforms(9).map(_(p9.asInstanceOf[AnyRef])).getOrElse(p9.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9).iterator())
      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7, f8: T => P8, f9: T => P9): From[T, Out] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)))

  class Update1[P0](
    private val _java: dev.typr.foundations.Template.Update1[?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[P0, Int]:
    def this(j: dev.typr.foundations.Template.Update1[?]) = this(j, List(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: P0): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(input.asInstanceOf[AnyRef])).getOrElse(input.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0): From[T, Int] =
      new From(_java, (t: T) => on(f0(t)))

  class Update2[P0, P1](
    private val _java: dev.typr.foundations.Template.Update2[?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1), Int]:
    def this(j: dev.typr.foundations.Template.Update2[?, ?]) = this(j, List.fill(2)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1)): Operation.Update =
      on(input._1, input._2)
    def on(p0: P0, p1: P1): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t)))

  class Update3[P0, P1, P2](
    private val _java: dev.typr.foundations.Template.Update3[?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2), Int]:
    def this(j: dev.typr.foundations.Template.Update3[?, ?, ?]) = this(j, List.fill(3)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2)): Operation.Update =
      on(input._1, input._2, input._3)
    def on(p0: P0, p1: P1, p2: P2): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t)))

  class Update4[P0, P1, P2, P3](
    private val _java: dev.typr.foundations.Template.Update4[?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3), Int]:
    def this(j: dev.typr.foundations.Template.Update4[?, ?, ?, ?]) = this(j, List.fill(4)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3)): Operation.Update =
      on(input._1, input._2, input._3, input._4)
    def on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t)))

  class Update5[P0, P1, P2, P3, P4](
    private val _java: dev.typr.foundations.Template.Update5[?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4), Int]:
    def this(j: dev.typr.foundations.Template.Update5[?, ?, ?, ?, ?]) = this(j, List.fill(5)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t)))

  class Update6[P0, P1, P2, P3, P4, P5](
    private val _java: dev.typr.foundations.Template.Update6[?, ?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5), Int]:
    def this(j: dev.typr.foundations.Template.Update6[?, ?, ?, ?, ?, ?]) = this(j, List.fill(6)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5, input._6)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t)))

  class Update7[P0, P1, P2, P3, P4, P5, P6](
    private val _java: dev.typr.foundations.Template.Update7[?, ?, ?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6), Int]:
    def this(j: dev.typr.foundations.Template.Update7[?, ?, ?, ?, ?, ?, ?]) = this(j, List.fill(7)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t)))

  class Update8[P0, P1, P2, P3, P4, P5, P6, P7](
    private val _java: dev.typr.foundations.Template.Update8[?, ?, ?, ?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7), Int]:
    def this(j: dev.typr.foundations.Template.Update8[?, ?, ?, ?, ?, ?, ?, ?]) = this(j, List.fill(8)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t)))

  class Update9[P0, P1, P2, P3, P4, P5, P6, P7, P8](
    private val _java: dev.typr.foundations.Template.Update9[?, ?, ?, ?, ?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7, P8), Int]:
    def this(j: dev.typr.foundations.Template.Update9[?, ?, ?, ?, ?, ?, ?, ?, ?]) = this(j, List.fill(9)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val v8: AnyRef = _transforms(8).map(_(p8.asInstanceOf[AnyRef])).getOrElse(p8.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7, f8: T => P8): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t)))

  class Update10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9](
    private val _java: dev.typr.foundations.Template.Update10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?],
    private val _transforms: List[Option[AnyRef => AnyRef]]
  ) extends Template[(P0, P1, P2, P3, P4, P5, P6, P7, P8, P9), Int]:
    def this(j: dev.typr.foundations.Template.Update10[?, ?, ?, ?, ?, ?, ?, ?, ?, ?]) = this(j, List.fill(10)(None))
    override def underlying: dev.typr.foundations.Template[?, ?] = _java
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9)): Operation.Update =
      on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9, input._10)
    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Update =
      val v0: AnyRef = _transforms(0).map(_(p0.asInstanceOf[AnyRef])).getOrElse(p0.asInstanceOf[AnyRef])
      val v1: AnyRef = _transforms(1).map(_(p1.asInstanceOf[AnyRef])).getOrElse(p1.asInstanceOf[AnyRef])
      val v2: AnyRef = _transforms(2).map(_(p2.asInstanceOf[AnyRef])).getOrElse(p2.asInstanceOf[AnyRef])
      val v3: AnyRef = _transforms(3).map(_(p3.asInstanceOf[AnyRef])).getOrElse(p3.asInstanceOf[AnyRef])
      val v4: AnyRef = _transforms(4).map(_(p4.asInstanceOf[AnyRef])).getOrElse(p4.asInstanceOf[AnyRef])
      val v5: AnyRef = _transforms(5).map(_(p5.asInstanceOf[AnyRef])).getOrElse(p5.asInstanceOf[AnyRef])
      val v6: AnyRef = _transforms(6).map(_(p6.asInstanceOf[AnyRef])).getOrElse(p6.asInstanceOf[AnyRef])
      val v7: AnyRef = _transforms(7).map(_(p7.asInstanceOf[AnyRef])).getOrElse(p7.asInstanceOf[AnyRef])
      val v8: AnyRef = _transforms(8).map(_(p8.asInstanceOf[AnyRef])).getOrElse(p8.asInstanceOf[AnyRef])
      val v9: AnyRef = _transforms(9).map(_(p9.asInstanceOf[AnyRef])).getOrElse(p9.asInstanceOf[AnyRef])
      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
        _java.fragment(), java.util.List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9).iterator())
      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
    def from[T](f0: T => P0, f1: T => P1, f2: T => P2, f3: T => P3, f4: T => P4, f5: T => P5, f6: T => P6, f7: T => P7, f8: T => P8, f9: T => P9): From[T, Int] =
      new From(_java, (t: T) => on(f0(t), f1(t), f2(t), f3(t), f4(t), f5(t), f6(t), f7(t), f8(t), f9(t)))

  class From[T, Out](
    private val _innerUnderlying: dev.typr.foundations.Template[?, ?],
    private val _resolver: T => Operation[Out]
  ) extends Template[T, Out]:
    override def underlying: dev.typr.foundations.Template[?, ?] = _innerUnderlying
    override def on(input: T): Operation[Out] = _resolver(input)

sealed trait RowTemplate[Row, Out] extends Template[Row, Out]:
  override def underlying: dev.typr.foundations.RowTemplate[?, ?]

object RowTemplate:

  class Query[Row, Out](val underlying: dev.typr.foundations.RowTemplate.Query[Row, Out])
      extends RowTemplate[Row, Out]:
    override def on(input: Row): Operation.Query[Out] = new Operation.Query(underlying.on(input))

  class Update[Row](val underlying: dev.typr.foundations.RowTemplate.Update[Row])
      extends RowTemplate[Row, Int]:
    override def on(input: Row): Operation.Update = new Operation.Update(underlying.on(input))

    def onMany(rows: Iterator[Row]): Operation.UpdateManyTemplate[Row] = {
      import _root_.scala.jdk.CollectionConverters.*
      new Operation.UpdateManyTemplate(underlying.onMany(rows.asJava))
    }
