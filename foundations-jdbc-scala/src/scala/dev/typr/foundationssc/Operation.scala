package dev.typr.foundationssc

import java.time.Duration
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*

trait Operation[Out] extends Analyzable:
  def underlying: dev.typr.foundations.Operation[Out]

  override def analyzable: dev.typr.foundations.Analyzable = underlying

  def run(using conn: dev.typr.foundations.Connection): Out =
    conn.execute(underlying)

  def transact(transactor: Transactor): Out =
    transactor.execute(this)

  def map[B](f: Out => B): Operation[B] =
    Operation.Mapped(this, f)

  def voided(): Operation[Unit] = map(_ => ())

  def combine[B](other: Operation[B]): Operation[(Out, B)] =
    Operation.CombinePair(underlying.combine(other.underlying))

  def combineWith[B, R](other: Operation[B])(f: (Out, B) => R): Operation[R] =
    combine(other).map { case (a, b) => f(a, b) }

  def combineWith[B, C, R](b: Operation[B], c: Operation[C])(f: (Out, B, C) => R): Operation[R] =
    combine(b).combine(c).map { case ((a, b), c) => f(a, b, c) }

  def combineWith[B, C, D, R](b: Operation[B], c: Operation[C], d: Operation[D])(f: (Out, B, C, D) => R): Operation[R] =
    combine(b).combine(c).combine(d).map { case (((a, b), c), d) => f(a, b, c, d) }

  def combineWith[B, C, D, E, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E])(f: (Out, B, C, D, E) => R): Operation[R] =
    combine(b).combine(c).combine(d).combine(e).map { case ((((a, b), c), d), e) => f(a, b, c, d, e) }

  def combineWith[B, C, D, E, F, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F])(
      fn: (Out, B, C, D, E, F) => R
  ): Operation[R] =
    combine(b).combine(c).combine(d).combine(e).combine(f).map { case (((((a, b), c), d), e), f) => fn(a, b, c, d, e, f) }

  def productL[B](other: Operation[B]): Operation[Out] =
    combine(other).map(_._1)

  def andThen[B](template: Template[Out, B]): Operation[B] =
    Operation.ThenOp(this, template)

  def named(name: String): Operation[Out] =
    Operation.JavaWrapped(underlying.named(name))

  def timeout(timeout: Duration): Operation[Out] =
    Operation.JavaWrapped(underlying.timeout(timeout))

  def withListener(listener: QueryListener): Operation[Out] =
    Operation.JavaWrapped(underlying.withListener(listener))

object Operation:
  class Update(private val javaOp: dev.typr.foundations.Operation.Update) extends Operation[Int]:
    def underlying: dev.typr.foundations.Operation[Int] = javaOp.map(x => x)

  class Execute(private val javaOp: dev.typr.foundations.Operation.Execute) extends Operation[Unit]:
    def underlying: dev.typr.foundations.Operation[Unit] = javaOp.map(_ => ())

  class UpdateReturning[Out](private val javaOp: dev.typr.foundations.Operation.UpdateReturning[Out]) extends Operation[Out]:
    def underlying: dev.typr.foundations.Operation[Out] = javaOp

  class UpdateReturningGeneratedKeys[Out](private val javaOp: dev.typr.foundations.Operation.UpdateReturningGeneratedKeys[Out]) extends Operation[Out]:
    def underlying: dev.typr.foundations.Operation[Out] = javaOp

  class UpdateMany[Row](private val javaOp: dev.typr.foundations.Operation.UpdateMany[Row]) extends Operation[Option[Array[Int]]]:
    def underlying: dev.typr.foundations.Operation[Option[Array[Int]]] = javaOp.map(x => x.map(a => a).toScala)

  class UpdateManyReturning[Row](private val javaOp: dev.typr.foundations.Operation.UpdateManyReturning[Row]) extends Operation[List[Row]]:
    def underlying: dev.typr.foundations.Operation[List[Row]] = javaOp.map(jl => jl.asScala.toList)

  class UpdateReturningEach[Row](private val javaOp: dev.typr.foundations.Operation.UpdateReturningEach[Row]) extends Operation[List[Row]]:
    def underlying: dev.typr.foundations.Operation[List[Row]] = javaOp.map(jl => jl.asScala.toList)

  class UpdateManyTemplate[Row](private val javaOp: dev.typr.foundations.Operation.UpdateManyTemplate[Row]) extends Operation[Option[Array[Int]]]:
    def underlying: dev.typr.foundations.Operation[Option[Array[Int]]] = javaOp.map(x => x.map(a => a).toScala)

  class StreamingCopy[Row](private val javaOp: dev.typr.foundations.Operation.StreamingCopy[Row]) extends Operation[Long]:
    def underlying: dev.typr.foundations.Operation[Long] = javaOp.map(x => x)

  private[foundationssc] class Mapped[A, Out](source: Operation[A], f: A => Out) extends Operation[Out]:
    def underlying: dev.typr.foundations.Operation[Out] =
      source.underlying.map(a => f(a))

  private[foundationssc] class CombinePair[A, B](
      javaOp: dev.typr.foundations.Operation[dev.typr.foundations.Tuple.Tuple2[A, B]]
  ) extends Operation[(A, B)]:
    def underlying: dev.typr.foundations.Operation[(A, B)] =
      javaOp.map(t => (t._1(), t._2()))

  private[foundationssc] class ThenOp[A, B](
      source: Operation[A],
      continuation: Template[A, B]
  ) extends Operation[B]:
    def underlying: dev.typr.foundations.Operation[B] =
      new dev.typr.foundations.Operation.Then(source.underlying, continuation.underlying)

  private[foundationssc] class JavaWrapped[Out](val underlying: dev.typr.foundations.Operation[Out]) extends Operation[Out]

  def sequence[T](operations: List[Operation[T]]): Operation[List[T]] =
    if operations.isEmpty then return OperationRead.pure(Nil)
    var result: Operation[List[T]] = operations.head.map(t => List(t))
    for op <- operations.tail do result = result.combine(op).map(t => t._1 :+ t._2)
    result

  def allOf(operations: Operation[?]*): Operation[Unit] =
    if operations.isEmpty then return OperationRead.pure(())
    var result: Operation[Unit] = operations.head.voided()
    for op <- operations.tail do result = result.productL(op)
    result

  def ifEmpty[T](check: Operation[Option[T]], fallback: Operation[T]): Operation[T] =
    JavaWrapped(
      dev.typr.foundations.Operation.ifEmpty(
        check.underlying.map(opt => if opt.isDefined then java.util.Optional.of(opt.get) else java.util.Optional.empty[T]()),
        fallback.underlying
      )
    )
