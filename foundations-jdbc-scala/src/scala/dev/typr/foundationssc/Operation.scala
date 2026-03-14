package dev.typr.foundationssc

import java.sql.Connection
import java.time.Duration
import _root_.scala.jdk.CollectionConverters.*

sealed trait Operation[Out] extends Analyzable {
  def underlying: dev.typr.foundations.Operation[?]

  override def analyzable: dev.typr.foundations.Analyzable = underlying

  def run(conn: Connection): Out

  def transact(transactor: Transactor): Out =
    transactor.execute(this)

  def map[B](f: Out => B): Operation[B] =
    Operation.Mapped(this, f)

  def combine[B](other: Operation[B]): Operation[(Out, B)] =
    Operation.Combine(this, other)

  def combineWith[B, R](other: Operation[B])(combine: (Out, B) => R): Operation[R] =
    this.combine(other).map(t => combine(t._1, t._2))

  def combineWith[B, C, R](b: Operation[B], c: Operation[C])(combine: (Out, B, C) => R): Operation[R] =
    this.combine(b).combine(c).map(t => combine(t._1._1, t._1._2, t._2))

  def combineWith[B, C, D, R](b: Operation[B], c: Operation[C], d: Operation[D])(combine: (Out, B, C, D) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).map(t => combine(t._1._1._1, t._1._1._2, t._1._2, t._2))

  def combineWith[B, C, D, E, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E])(combine: (Out, B, C, D, E) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).combine(e).map(t => combine(t._1._1._1._1, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def combineWith[B, C, D, E, F, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F])(combine: (Out, B, C, D, E, F) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).combine(e).combine(f).map(t => combine(t._1._1._1._1._1, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def combineWith[B, C, D, E, F, G, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G])(combine: (Out, B, C, D, E, F, G) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).map(t => combine(t._1._1._1._1._1._1, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def combineWith[B, C, D, E, F, G, H, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G], h: Operation[H])(combine: (Out, B, C, D, E, F, G, H) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).combine(h).map(t => combine(t._1._1._1._1._1._1._1, t._1._1._1._1._1._1._2, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def combineWith[B, C, D, E, F, G, H, I, R](b: Operation[B], c: Operation[C], d: Operation[D], e: Operation[E], f: Operation[F], g: Operation[G], h: Operation[H], i: Operation[I])(combine: (Out, B, C, D, E, F, G, H, I) => R): Operation[R] =
    this.combine(b).combine(c).combine(d).combine(e).combine(f).combine(g).combine(h).combine(i).map(t => combine(t._1._1._1._1._1._1._1._1, t._1._1._1._1._1._1._1._2, t._1._1._1._1._1._1._2, t._1._1._1._1._1._2, t._1._1._1._1._2, t._1._1._1._2, t._1._1._2, t._1._2, t._2))

  def productL[B](other: Operation[B]): Operation[Out] =
    combine(other).map(_._1)

  def andThen[B](template: Template[Out, B]): Operation[B] =
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
      result = result.combine(op).map(t => t._1 :+ t._2)
    }
    result
  }

  def allOf(operations: Operation[?]*): Operation[Unit] = {
    if (operations.isEmpty) return pure(())
    var result: Operation[Unit] = operations.head.voided
    for (op <- operations.tail) {
      result = result.productL(op)
    }
    result
  }

  def ifEmpty[T](check: Operation[Option[T]], fallback: Operation[T]): Operation[T] =
    IfEmpty(check, fallback)

  class Query[Out](val underlying: dev.typr.foundations.Operation.Query[Out]) extends Operation[Out] {
    override def run(conn: Connection): Out = underlying.run(conn)
  }

  object Query {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): Query[Out] =
      new Query(new dev.typr.foundations.Operation.Query(query.underlying, parser.underlying))
  }

  class Update(val underlying: dev.typr.foundations.Operation.Update) extends Operation[Int] {
    override def run(conn: Connection): Int = underlying.run(conn)
  }

  class Execute(val underlying: dev.typr.foundations.Operation.Execute) extends Operation[Unit] {
    override def run(conn: Connection): Unit = { underlying.run(conn); () }
  }

  object Update {
    def apply(query: Fragment): Update =
      new Update(new dev.typr.foundations.Operation.Update(query.underlying))
  }

  class UpdateReturning[Out](val underlying: dev.typr.foundations.Operation.UpdateReturning[Out]) extends Operation[Out] {
    override def run(conn: Connection): Out = underlying.run(conn)
  }

  object UpdateReturning {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): UpdateReturning[Out] =
      new UpdateReturning(new dev.typr.foundations.Operation.UpdateReturning(query.underlying, parser.underlying))
  }

  class UpdateReturningGeneratedKeys[Out](val underlying: dev.typr.foundations.Operation.UpdateReturningGeneratedKeys[Out]) extends Operation[Out] {
    override def run(conn: Connection): Out = underlying.run(conn)
  }

  class UpdateMany[Row](val underlying: dev.typr.foundations.Operation.UpdateMany[Row]) extends Operation[Array[Int]] {
    override def run(conn: Connection): Array[Int] = underlying.run(conn)
  }

  class UpdateManyReturning[Row](val underlying: dev.typr.foundations.Operation.UpdateManyReturning[Row]) extends Operation[List[Row]] {
    import _root_.scala.jdk.CollectionConverters.*
    override def run(conn: Connection): List[Row] = underlying.run(conn).asScala.toList
  }

  class UpdateReturningEach[Row](val underlying: dev.typr.foundations.Operation.UpdateReturningEach[Row]) extends Operation[List[Row]] {
    import _root_.scala.jdk.CollectionConverters.*
    override def run(conn: Connection): List[Row] = underlying.run(conn).asScala.toList
  }

  class UpdateManyTemplate[Row](val underlying: dev.typr.foundations.Operation.UpdateManyTemplate[Row]) extends Operation[Array[Int]] {
    override def run(conn: Connection): Array[Int] = underlying.run(conn)
  }

  class StreamingCopy(val underlying: dev.typr.foundations.Operation[java.lang.Long]) extends Operation[Long] {
    override def run(conn: Connection): Long = underlying.run(conn)
  }

  class Streaming[Row](val underlying: dev.typr.foundations.Operation.Streaming[Row]) extends Operation[Cursor[Row]] {
    override def run(conn: Connection): Cursor[Row] = new Cursor(underlying.run(conn))
  }

  object Streaming {
    def apply[Row](query: Fragment, codec: RowCodec[Row], fetchSize: Int): Streaming[Row] =
      new Streaming(new dev.typr.foundations.Operation.Streaming(query.underlying, codec.underlying, fetchSize))
  }

  class Mapped[A, B](val source: Operation[A], val f: A => B) extends Operation[B] {
    val underlying: dev.typr.foundations.Operation[?] = source.underlying
    override def run(conn: Connection): B = f(source.run(conn))
  }

  class Pure[T](val value: T) extends Operation[T] {
    val underlying: dev.typr.foundations.Operation[T] = new dev.typr.foundations.Operation.Pure(value)
    override def run(conn: Connection): T = value
  }

  class Combine[A, B](val first: Operation[A], val second: Operation[B]) extends Operation[(A, B)] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.Combine(
        first.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]],
        second.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]]
      )
    override def run(conn: Connection): (A, B) = (first.run(conn), second.run(conn))
  }

  class IfEmpty[T](val check: Operation[Option[T]], val fallback: Operation[T]) extends Operation[T] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.IfEmpty[Object](
        check.underlying.asInstanceOf[dev.typr.foundations.Operation[java.util.Optional[Object]]],
        fallback.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]]
      )
    override def run(conn: Connection): T = check.run(conn).getOrElse(fallback.run(conn))
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
    override def run(conn: Connection): Out =
      underlying.run(conn).asInstanceOf[Out]
  }

  class Then[A, In, B](val source: Operation[A], val extract: A => In, val continuation: Template[In, B]) extends Operation[B] {
    @SuppressWarnings(Array("unchecked"))
    val underlying: dev.typr.foundations.Operation[?] =
      new dev.typr.foundations.Operation.Then(
        source.underlying.asInstanceOf[dev.typr.foundations.Operation[Object]],
        java.util.function.Function.identity[Object](),
        continuation.underlying.asInstanceOf[dev.typr.foundations.Template[Object, Object]]
      )
    override def run(conn: Connection): B = {
      val a = source.run(conn)
      val in_ = extract(a)
      continuation.on(in_).run(conn)
    }
  }
}
