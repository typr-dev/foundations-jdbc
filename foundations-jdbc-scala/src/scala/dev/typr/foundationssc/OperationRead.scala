package dev.typr.foundationssc

import java.time.Duration

sealed trait OperationRead[Out] extends Operation[Out]:

  override def underlying: dev.typr.foundations.OperationRead[Out]

  def run(using conn: dev.typr.foundations.ConnectionRead): Out =
    conn.execute(underlying)

  def transactRead(transactor: Transactor): Out =
    transactor.execute(this)

  override def map[B](f: Out => B): OperationRead[B] =
    OperationRead.Mapped(this, f)

  override def voided(): OperationRead[Unit] = map(_ => ())

  def combine[B](other: OperationRead[B]): OperationRead[(Out, B)] =
    OperationRead.CombineRead(this, other)

  def combineWith[B, R](other: OperationRead[B])(f: (Out, B) => R): OperationRead[R] =
    combine(other).map { case (a, b) => f(a, b) }

  def combineWith[B, C, R](b: OperationRead[B], c: OperationRead[C])(f: (Out, B, C) => R): OperationRead[R] =
    combine(b).combine(c).map { case ((a, b), c) => f(a, b, c) }

  def combineWith[B, C, D, R](b: OperationRead[B], c: OperationRead[C], d: OperationRead[D])(f: (Out, B, C, D) => R): OperationRead[R] =
    combine(b).combine(c).combine(d).map { case (((a, b), c), d) => f(a, b, c, d) }

  def combineWith[B, C, D, E, R](b: OperationRead[B], c: OperationRead[C], d: OperationRead[D], e: OperationRead[E])(
      f: (Out, B, C, D, E) => R
  ): OperationRead[R] =
    combine(b).combine(c).combine(d).combine(e).map { case ((((a, b), c), d), e) => f(a, b, c, d, e) }

  def combineWith[B, C, D, E, F, R](b: OperationRead[B], c: OperationRead[C], d: OperationRead[D], e: OperationRead[E], f: OperationRead[F])(
      fn: (Out, B, C, D, E, F) => R
  ): OperationRead[R] =
    combine(b).combine(c).combine(d).combine(e).combine(f).map { case (((((a, b), c), d), e), f) => fn(a, b, c, d, e, f) }

  def productL[B](other: OperationRead[B]): OperationRead[Out] =
    combine(other).map(_._1)

  def andThen[B](next: Out => OperationRead[B]): OperationRead[B] =
    OperationRead.ThenRead(this, next)

  override def named(name: String): OperationRead[Out] =
    OperationRead.JavaWrapped(underlying.named(name))

  override def timeout(timeout: Duration): OperationRead[Out] =
    OperationRead.JavaWrapped(underlying.timeout(timeout))

  override def withListener(listener: QueryListener): OperationRead[Out] =
    OperationRead.JavaWrapped(underlying.withListener(listener))

object OperationRead:

  class Query[Out](override val underlying: dev.typr.foundations.OperationRead.Query[Out]) extends OperationRead[Out]

  object Query:
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): Query[Out] =
      new Query(new dev.typr.foundations.OperationRead.Query(query.underlying, parser.underlying))

  class Streaming[Row](private val java: dev.typr.foundations.OperationRead.Streaming[Row]) extends OperationRead[Cursor[Row]]:
    override val underlying: dev.typr.foundations.OperationRead[Cursor[Row]] =
      java.map[Cursor[Row]]((jc: dev.typr.foundations.Cursor[Row]) => new Cursor(jc))

  object Streaming:
    def apply[Row](query: Fragment, codec: RowCodec[Row], fetchSize: Int): Streaming[Row] =
      new Streaming(new dev.typr.foundations.OperationRead.Streaming(query.underlying, codec.underlying, fetchSize))

  class Pure[T](override val underlying: dev.typr.foundations.OperationRead.Pure[T]) extends OperationRead[T]

  private[foundationssc] class Mapped[A, B](source: OperationRead[A], f: A => B) extends OperationRead[B]:
    override val underlying: dev.typr.foundations.OperationRead[B] =
      source.underlying.map[B]((a: A) => f(a))

  private[foundationssc] class CombineRead[A, B](first: OperationRead[A], second: OperationRead[B]) extends OperationRead[(A, B)]:
    override val underlying: dev.typr.foundations.OperationRead[(A, B)] =
      first.underlying
        .combine(second.underlying)
        .map[(A, B)]((t: dev.typr.foundations.Tuple.Tuple2[A, B]) => (t._1(), t._2()))

  private[foundationssc] class ThenRead[A, B](source: OperationRead[A], continuation: A => OperationRead[B]) extends OperationRead[B]:
    override val underlying: dev.typr.foundations.OperationRead[B] =
      source.underlying.thenRead((a: A) => continuation(a).underlying)

  private[foundationssc] class IfEmptyRead[T](check: OperationRead[Option[T]], fallback: OperationRead[T]) extends OperationRead[T]:
    override val underlying: dev.typr.foundations.OperationRead[T] =
      dev.typr.foundations.OperationRead.ifEmpty[T](
        check.underlying.map(opt => if opt.isDefined then java.util.Optional.of(opt.get) else java.util.Optional.empty[T]()),
        fallback.underlying
      )

  private[foundationssc] class ConfiguredRead[Out](override val underlying: dev.typr.foundations.OperationRead[Out]) extends OperationRead[Out]

  private[foundationssc] class JavaWrapped[Out](override val underlying: dev.typr.foundations.OperationRead[Out]) extends OperationRead[Out]

  def pure[T](value: T): OperationRead[T] = Pure(new dev.typr.foundations.OperationRead.Pure(value))

  def sequence[T](operations: List[OperationRead[T]]): OperationRead[List[T]] =
    if operations.isEmpty then return pure(Nil)
    var result: OperationRead[List[T]] = operations.head.map(t => List(t))
    for op <- operations.tail do result = result.combine(op).map(t => t._1 :+ t._2)
    result

  def allOf(operations: OperationRead[?]*): OperationRead[Unit] =
    if operations.isEmpty then return pure(())
    var result: OperationRead[Unit] = operations.head.voided()
    for op <- operations.tail do result = result.productL(op)
    result

  def ifEmpty[T](check: OperationRead[Option[T]], fallback: OperationRead[T]): OperationRead[T] =
    IfEmptyRead(check, fallback)
