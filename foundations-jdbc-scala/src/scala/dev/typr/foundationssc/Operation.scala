package dev.typr.foundationssc

import java.sql.{Connection, SQLException}
import java.time.Duration
import _root_.scala.jdk.CollectionConverters.*

sealed trait Operation[Out] {
  def underlying: dev.typr.foundations.Operation[?]

  def runChecked(conn: Connection): Out

  def run(conn: Connection): Out = {
    try {
      runChecked(conn)
    } catch {
      case e: SQLException => throw new RuntimeException(e)
    }
  }

  def transact(transactor: Transactor): Out =
    transactor.execute(this)

  def map[B](f: Out => B): Operation[B] =
    Operation.Mapped(this, f)

  def `with`[B](other: Operation[B]): Operation[(Out, B)] =
    Operation.With(this, other)

  def `with`[B, R](other: Operation[B])(combine: (Out, B) => R): Operation[R] =
    `with`(other).map(t => combine(t._1, t._2))

  def `with`[B, C, R](b: Operation[B], c: Operation[C])(combine: (Out, B, C) => R): Operation[R] =
    `with`(b).`with`(c).map(t => combine(t._1._1, t._1._2, t._2))

  def `with`[B, C, D, R](b: Operation[B], c: Operation[C], d: Operation[D])(combine: (Out, B, C, D) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).map(t => combine(t._1._1._1, t._1._1._2, t._1._2, t._2))

  def `with`[B, C, D, E, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E])(combine: (Out, B, C, D, E) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).`with`(e).map(t => combine(t._1._1._1._1, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def `with`[B, C, D, E, F, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F])(combine: (Out, B, C, D, E, F) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).`with`(e).`with`(f).map(t => combine(t._1._1._1._1._1, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def `with`[B, C, D, E, F, G, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G])(combine: (Out, B, C, D, E, F, G) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).`with`(e).`with`(f).`with`(g).map(t => combine(t._1._1._1._1._1._1, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def `with`[B, C, D, E, F, G, H, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G], h: Operation[H])(combine: (Out, B, C, D, E, F, G, H) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).`with`(e).`with`(f).`with`(g).`with`(h).map(t => combine(t._1._1._1._1._1._1._1, t._1._1._1._1._1._1._2, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def `with`[B, C, D, E, F, G, H, I, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G], h: Operation[H], i: Operation[I])(combine: (Out, B, C, D, E, F, G, H, I) => R): Operation[R] =
    `with`(b).`with`(c).`with`(d).`with`(e).`with`(f).`with`(g).`with`(h).`with`(i).map(t => combine(t._1._1._1._1._1._1._1._1, t._1._1._1._1._1._1._1._2, t._1._1._1._1._1._1._2, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def thenIgnore[B](other: Operation[B]): Operation[Out] =
    `with`(other).map(_._1)

  def andThen[B](template: SqlTemplate[Out, B]): Operation[B] =
    Operation.Then(this, identity[Out], template)

  def voided: Operation[Unit] =
    map(_ => ())

  def named(name: String): Operation[Out] =
    Operation.Configured(this, name, null, null)

  def timeout(timeout: Duration): Operation[Out] =
    Operation.Configured(this, null, timeout, null)

  def withListener(listener: dev.typr.foundations.QueryListener): Operation[Out] =
    Operation.Configured(this, null, null, listener)
}

object Operation {

  def pure[T](value: T): Operation[T] = Pure(value)

  def sequence[T](operations: List[Operation[T]]): Operation[List[T]] = {
    if (operations.isEmpty) return pure(Nil)
    var result: Operation[List[T]] = operations.head.map(t => List(t))
    for (op <- operations.tail) {
      result = result.`with`(op).map(t => t._1 :+ t._2)
    }
    result
  }

  def allOf(operations: Operation[?]*): Operation[Unit] = {
    if (operations.isEmpty) return pure(())
    var result: Operation[Unit] = operations.head.voided
    for (op <- operations.tail) {
      result = result.thenIgnore(op)
    }
    result
  }

  def ifEmpty[T](check: Operation[Option[T]], fallback: Operation[T]): Operation[T] =
    IfEmpty(check, fallback)

  class Query[Out](val underlying: dev.typr.foundations.Operation.Query[Out]) extends Operation[Out] {
    override def runChecked(conn: Connection): Out = underlying.runChecked(conn)
  }

  object Query {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): Query[Out] =
      new Query(new dev.typr.foundations.Operation.Query(query.underlying, parser.underlying))
  }

  class Update(val underlying: dev.typr.foundations.Operation.Update) extends Operation[Int] {
    override def runChecked(conn: Connection): Int = underlying.runChecked(conn)
  }

  object Update {
    def apply(query: Fragment): Update =
      new Update(new dev.typr.foundations.Operation.Update(query.underlying))
  }

  class UpdateReturning[Out](val underlying: dev.typr.foundations.Operation.UpdateReturning[Out]) extends Operation[Out] {
    override def runChecked(conn: Connection): Out = underlying.runChecked(conn)
  }

  object UpdateReturning {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): UpdateReturning[Out] =
      new UpdateReturning(new dev.typr.foundations.Operation.UpdateReturning(query.underlying, parser.underlying))
  }

  class UpdateMany[Row](val underlying: dev.typr.foundations.Operation.UpdateMany[Row]) extends Operation[Array[Int]] {
    override def runChecked(conn: Connection): Array[Int] = underlying.runChecked(conn)
  }

  class UpdateManyReturning[Row](val underlying: dev.typr.foundations.Operation.UpdateManyReturning[Row]) extends Operation[List[Row]] {
    import _root_.scala.jdk.CollectionConverters.*
    override def runChecked(conn: Connection): List[Row] = underlying.runChecked(conn).asScala.toList
  }

  class UpdateReturningEach[Row](val underlying: dev.typr.foundations.Operation.UpdateReturningEach[Row]) extends Operation[List[Row]] {
    import _root_.scala.jdk.CollectionConverters.*
    override def runChecked(conn: Connection): List[Row] = underlying.runChecked(conn).asScala.toList
  }

  class UpdateManyTemplate[Row](val underlying: dev.typr.foundations.Operation.UpdateManyTemplate[Row]) extends Operation[Array[Int]] {
    override def runChecked(conn: Connection): Array[Int] = underlying.runChecked(conn)
  }

  class StreamingCopy(val underlying: dev.typr.foundations.Operation[java.lang.Long]) extends Operation[Long] {
    override def runChecked(conn: Connection): Long = underlying.runChecked(conn)
  }

  class Mapped[A, B](val source: Operation[A], val f: A => B) extends Operation[B] {
    val underlying: dev.typr.foundations.Operation[?] = source.underlying
    override def runChecked(conn: Connection): B = f(source.runChecked(conn))
  }

  class Pure[T](val value: T) extends Operation[T] {
    val underlying: dev.typr.foundations.Operation[T] = new dev.typr.foundations.Operation.Pure(value)
    override def runChecked(conn: Connection): T = value
  }

  class With[A, B](val first: Operation[A], val second: Operation[B]) extends Operation[(A, B)] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.With(
        first.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]],
        second.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]]
      )
    override def runChecked(conn: Connection): (A, B) = (first.runChecked(conn), second.runChecked(conn))
  }

  class IfEmpty[T](val check: Operation[Option[T]], val fallback: Operation[T]) extends Operation[T] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.IfEmpty[Object](
        check.underlying.asInstanceOf[dev.typr.foundations.Operation[java.util.Optional[Object]]],
        fallback.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]]
      )
    override def runChecked(conn: Connection): T = check.runChecked(conn).getOrElse(fallback.runChecked(conn))
  }

  class Configured[Out](val inner: Operation[Out], val name: String, val timeout: Duration, val listener: dev.typr.foundations.QueryListener) extends Operation[Out] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.Configured(
        inner.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]],
        name,
        timeout,
        listener
      )
    override def runChecked(conn: Connection): Out =
      underlying.runChecked(conn).asInstanceOf[Out]
  }

  class Then[A, In, B](val source: Operation[A], val extract: A => In, val continuation: SqlTemplate[In, B]) extends Operation[B] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.Then(
        source.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]],
        java.util.function.Function.identity[Object](),
        continuation.underlying.asInstanceOf[dev.typr.foundations.SqlTemplate[Object, Object]]
      )
    override def runChecked(conn: Connection): B = {
      val a = source.runChecked(conn)
      val in_ = extract(a)
      continuation.on(in_).runChecked(conn)
    }
  }
}
