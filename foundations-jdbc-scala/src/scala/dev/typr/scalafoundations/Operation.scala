package dev.typr.scalafoundations

import java.sql.{Connection, SQLException}
import _root_.scala.jdk.CollectionConverters.*

/** Scala wrapper for dev.typr.foundations.Operation with Scala-native return types.
  *
  * This sealed trait wraps the Java Operation interface and provides Scala-friendly methods.
  */
sealed trait Operation[Out] {
  def underlying: dev.typr.foundations.Operation[?]

  def run(conn: Connection): Out

  def runUnchecked(conn: Connection): Out = {
    try {
      run(conn)
    } catch {
      case e: SQLException => throw new RuntimeException(e)
    }
  }

  def transact(transactor: dev.typr.foundations.Transactor): Out
}

object Operation {

  /** Query operation that returns a parsed result */
  class Query[Out](val underlying: dev.typr.foundations.Operation.Query[Out]) extends Operation[Out] {
    override def run(conn: Connection): Out = underlying.run(conn)

    override def transact(transactor: dev.typr.foundations.Transactor): Out =
      underlying.transact(transactor)
  }

  object Query {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): Query[Out] =
      new Query(new dev.typr.foundations.Operation.Query(query.underlying, parser.underlying))
  }

  /** Update operation that returns the number of affected rows */
  class Update(val underlying: dev.typr.foundations.Operation.Update) extends Operation[Int] {
    override def run(conn: Connection): Int = underlying.run(conn)

    override def transact(transactor: dev.typr.foundations.Transactor): Int =
      underlying.transact(transactor)
  }

  object Update {
    def apply(query: Fragment): Update =
      new Update(new dev.typr.foundations.Operation.Update(query.underlying))
  }

  /** Update operation with RETURNING clause */
  class UpdateReturning[Out](val underlying: dev.typr.foundations.Operation.UpdateReturning[Out]) extends Operation[Out] {
    override def run(conn: Connection): Out = underlying.run(conn)

    override def transact(transactor: dev.typr.foundations.Transactor): Out =
      underlying.transact(transactor)
  }

  object UpdateReturning {
    def apply[Out](query: Fragment, parser: ResultSetParser[Out]): UpdateReturning[Out] =
      new UpdateReturning(new dev.typr.foundations.Operation.UpdateReturning(query.underlying, parser.underlying))
  }
}
