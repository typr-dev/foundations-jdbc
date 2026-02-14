package dev.typr.scalafoundations

sealed trait SqlTemplate[In, Out]:
  def underlying: dev.typr.foundations.SqlTemplate[?, ?]

  def on(input: In): Operation[Out]

  def fragment: Fragment = new Fragment(underlying.fragment())

object SqlTemplate:

  class Query1[P0, Out](val underlying: dev.typr.foundations.SqlTemplate.Query1[P0, Out]) extends SqlTemplate[P0, Out]:
    override def on(input: P0): Operation.Query[Out] =
      new Operation.Query(underlying.on(input))

  class Query2[P0, P1, Out](val underlying: dev.typr.foundations.SqlTemplate.Query2[P0, P1, Out]) extends SqlTemplate[(P0, P1), Out]:
    override def on(input: (P0, P1)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2))

    def on(p0: P0, p1: P1): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1))

  class Query3[P0, P1, P2, Out](val underlying: dev.typr.foundations.SqlTemplate.Query3[P0, P1, P2, Out]) extends SqlTemplate[(P0, P1, P2), Out]:
    override def on(input: (P0, P1, P2)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3))

    def on(p0: P0, p1: P1, p2: P2): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2))

  class Query4[P0, P1, P2, P3, Out](val underlying: dev.typr.foundations.SqlTemplate.Query4[P0, P1, P2, P3, Out]) extends SqlTemplate[(P0, P1, P2, P3), Out]:
    override def on(input: (P0, P1, P2, P3)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4))

    def on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3))

  class Query5[P0, P1, P2, P3, P4, Out](val underlying: dev.typr.foundations.SqlTemplate.Query5[P0, P1, P2, P3, P4, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4), Out]:
    override def on(input: (P0, P1, P2, P3, P4)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4))

  class Query6[P0, P1, P2, P3, P4, P5, Out](val underlying: dev.typr.foundations.SqlTemplate.Query6[P0, P1, P2, P3, P4, P5, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5), Out]:
    override def on(input: (P0, P1, P2, P3, P4, P5)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5))

  class Query7[P0, P1, P2, P3, P4, P5, P6, Out](val underlying: dev.typr.foundations.SqlTemplate.Query7[P0, P1, P2, P3, P4, P5, P6, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6), Out]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6))

  class Query8[P0, P1, P2, P3, P4, P5, P6, P7, Out](val underlying: dev.typr.foundations.SqlTemplate.Query8[P0, P1, P2, P3, P4, P5, P6, P7, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7), Out]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7))

  class Query9[P0, P1, P2, P3, P4, P5, P6, P7, P8, Out](val underlying: dev.typr.foundations.SqlTemplate.Query9[P0, P1, P2, P3, P4, P5, P6, P7, P8, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7, P8), Out]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8))

  class Query10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out](val underlying: dev.typr.foundations.SqlTemplate.Query10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7, P8, P9), Out]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9)): Operation.Query[Out] =
      new Operation.Query(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9, input._10))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Query[Out] =
      new Operation.Query(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))

  class Update1[P0](val underlying: dev.typr.foundations.SqlTemplate.Update1[P0]) extends SqlTemplate[P0, Int]:
    override def on(input: P0): Operation.Update =
      new Operation.Update(underlying.on(input))

  class Update2[P0, P1](val underlying: dev.typr.foundations.SqlTemplate.Update2[P0, P1]) extends SqlTemplate[(P0, P1), Int]:
    override def on(input: (P0, P1)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2))

    def on(p0: P0, p1: P1): Operation.Update =
      new Operation.Update(underlying.on(p0, p1))

  class Update3[P0, P1, P2](val underlying: dev.typr.foundations.SqlTemplate.Update3[P0, P1, P2]) extends SqlTemplate[(P0, P1, P2), Int]:
    override def on(input: (P0, P1, P2)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3))

    def on(p0: P0, p1: P1, p2: P2): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2))

  class Update4[P0, P1, P2, P3](val underlying: dev.typr.foundations.SqlTemplate.Update4[P0, P1, P2, P3]) extends SqlTemplate[(P0, P1, P2, P3), Int]:
    override def on(input: (P0, P1, P2, P3)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4))

    def on(p0: P0, p1: P1, p2: P2, p3: P3): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3))

  class Update5[P0, P1, P2, P3, P4](val underlying: dev.typr.foundations.SqlTemplate.Update5[P0, P1, P2, P3, P4]) extends SqlTemplate[(P0, P1, P2, P3, P4), Int]:
    override def on(input: (P0, P1, P2, P3, P4)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4))

  class Update6[P0, P1, P2, P3, P4, P5](val underlying: dev.typr.foundations.SqlTemplate.Update6[P0, P1, P2, P3, P4, P5]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5), Int]:
    override def on(input: (P0, P1, P2, P3, P4, P5)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5))

  class Update7[P0, P1, P2, P3, P4, P5, P6](val underlying: dev.typr.foundations.SqlTemplate.Update7[P0, P1, P2, P3, P4, P5, P6]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6), Int]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6))

  class Update8[P0, P1, P2, P3, P4, P5, P6, P7](val underlying: dev.typr.foundations.SqlTemplate.Update8[P0, P1, P2, P3, P4, P5, P6, P7]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7), Int]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7))

  class Update9[P0, P1, P2, P3, P4, P5, P6, P7, P8](val underlying: dev.typr.foundations.SqlTemplate.Update9[P0, P1, P2, P3, P4, P5, P6, P7, P8]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7, P8), Int]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8))

  class Update10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9](val underlying: dev.typr.foundations.SqlTemplate.Update10[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9]) extends SqlTemplate[(P0, P1, P2, P3, P4, P5, P6, P7, P8, P9), Int]:
    override def on(input: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9)): Operation.Update =
      new Operation.Update(underlying.on(input._1, input._2, input._3, input._4, input._5, input._6, input._7, input._8, input._9, input._10))

    def on(p0: P0, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Operation.Update =
      new Operation.Update(underlying.on(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))
