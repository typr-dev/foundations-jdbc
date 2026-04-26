package dev.typr.foundationssc

import java.sql.PreparedStatement
import java.util.concurrent.atomic.AtomicInteger
import _root_.scala.jdk.CollectionConverters.*

/** Scala wrapper for dev.typr.foundations.Fragment with Scala-native APIs.
  *
  * This class wraps the Java Fragment interface and provides Scala-friendly methods that use Scala collections and types.
  */
class Fragment(val underlying: dev.typr.foundations.Fragment) extends AnyVal {

  def render(): String = underlying.render()

  def render(sb: java.lang.StringBuilder): Unit = underlying.render(sb)

  def set(stmt: PreparedStatement): Unit = underlying.set(stmt)

  def set(stmt: PreparedStatement, idx: AtomicInteger): Unit = underlying.set(stmt, idx)

  def append(other: Fragment): Fragment = new Fragment(underlying.append(other.underlying))

  def ++(other: Fragment): Fragment = append(other)

  def query[T](parser: ResultSetParser[T]): OperationRead.Query[T] =
    OperationRead.Query(this, parser)

  def queryExactlyOne[T](tpe: DbType[T]): OperationRead.Query[T] =
    query(RowCodec.of(tpe).exactlyOne())

  def queryExactlyOne[T](codec: RowCodec[T]): OperationRead.Query[T] =
    query(codec.exactlyOne())

  def queryAll[T](tpe: DbType[T]): OperationRead.Query[List[T]] =
    query(RowCodec.of(tpe).all())

  def queryAll[T](codec: RowCodec[T]): OperationRead.Query[List[T]] =
    query(codec.all())

  def queryMaxOne[T](tpe: DbType[T]): OperationRead.Query[Option[T]] =
    query(RowCodec.of(tpe).maxOne())

  def queryMaxOne[T](codec: RowCodec[T]): OperationRead.Query[Option[T]] =
    query(codec.maxOne())

  def update(): Operation.Update =
    new Operation.Update(new dev.typr.foundations.Operation.Update(underlying))

  def execute(): Operation.Execute =
    new Operation.Execute(new dev.typr.foundations.Operation.Execute(underlying))

  def updateReturning[T](parser: ResultSetParser[T]): Operation.UpdateReturning[T] =
    new Operation.UpdateReturning(new dev.typr.foundations.Operation.UpdateReturning(underlying, parser.underlying))

  def updateReturningGeneratedKeys[T](columnNames: Array[String], parser: ResultSetParser[T]): Operation.UpdateReturningGeneratedKeys[T] =
    new Operation.UpdateReturningGeneratedKeys(underlying.updateReturningGeneratedKeys(columnNames, parser.underlying))

  def updateMany[Row](parser: RowCodec[Row], rows: Iterator[Row]): Operation.UpdateMany[Row] = {
    import _root_.scala.jdk.CollectionConverters.*
    new Operation.UpdateMany(underlying.updateMany(parser.underlying, rows.asJava))
  }

  def updateManyReturning[Row](parser: RowCodec[Row], rows: Iterator[Row]): Operation.UpdateManyReturning[Row] = {
    import _root_.scala.jdk.CollectionConverters.*
    new Operation.UpdateManyReturning(underlying.updateManyReturning(parser.underlying, rows.asJava))
  }

  def updateReturningEach[Row](parser: RowCodec[Row], rows: Iterator[Row]): Operation.UpdateReturningEach[Row] = {
    import _root_.scala.jdk.CollectionConverters.*
    new Operation.UpdateReturningEach(underlying.updateReturningEach(parser.underlying, rows.asJava))
  }

  def streamingQuery[T](codec: RowCodec[T], fetchSize: Int): OperationRead.Streaming[T] =
    new OperationRead.Streaming(underlying.streamingQuery(codec.underlying, fetchSize))

  def streamingQuery[T](tpe: DbType[T], fetchSize: Int): OperationRead.Streaming[T] =
    new OperationRead.Streaming(underlying.streamingQuery(tpe.underlying, fetchSize))

  def append(s: String): Fragment = new Fragment(underlying.append(s))

  def value[T](dbType: DbType[T], value: T): Fragment = new Fragment(underlying.value(dbType.underlying, value))

  def appendAll(fragments: List[Fragment], separator: Fragment): Fragment =
    new Fragment(underlying.appendAll(fragments.map(_.underlying).asJava, separator.underlying))

  def valueNullable[T](dbType: DbType[T], value: Option[T]): Fragment = {
    import _root_.scala.jdk.OptionConverters.*
    new Fragment(underlying.value(dbType.underlying.opt(), value.toJava))
  }

  def row[Row](parser: RowCodecNamed[Row], row: Row, except: String*): Fragment =
    new Fragment(underlying.row(parser.underlying, row, except*))

  def param[P0](dbType: DbType[P0]): ParamBuilders.ParamBuilder1[P0] =
    new ParamBuilders.ParamBuilder1(underlying.param(dbType.underlying))

  // ── Conditional append DSL ──

  def optionally[T](value: Option[T]): Fragment.OptionallyValue[T] =
    new Fragment.OptionallyValue(this, value)

  def optionally(condition: Boolean): Fragment.OptionallyFlag =
    new Fragment.OptionallyFlag(this, condition)
}

object Fragment {
  class OptionallyValue[T](base: Fragment, value: Option[T]):
    def append(sql: String, tpe: DbType[T]): Fragment =
      import _root_.scala.jdk.OptionConverters.*
      new Fragment(
        base.underlying
          .optionally(value.toJava.asInstanceOf[java.util.Optional[T]])
          .append(sql, tpe.underlying))

    def append(sql: String, tpe: DbType[T], whenAbsent: String): Fragment =
      import _root_.scala.jdk.OptionConverters.*
      new Fragment(
        base.underlying
          .optionally(value.toJava.asInstanceOf[java.util.Optional[T]])
          .append(sql, tpe.underlying, whenAbsent))

  class OptionallyFlag(base: Fragment, condition: Boolean):
    def append(sql: String): Fragment =
      new Fragment(base.underlying.optionally(condition).append(sql))

    def append(fragment: Fragment): Fragment =
      new Fragment(base.underlying.optionally(condition).append(fragment.underlying))

    def append(whenTrue: String, whenFalse: String): Fragment =
      new Fragment(base.underlying.optionally(condition).append(whenTrue, whenFalse))

    def append(whenTrue: Fragment, whenFalse: Fragment): Fragment =
      new Fragment(base.underlying.optionally(condition).append(whenTrue.underlying, whenFalse.underlying))

  val EMPTY: Fragment = new Fragment(dev.typr.foundations.Fragment.EMPTY)

  def of(value: String): Fragment = new Fragment(dev.typr.foundations.Fragment.of(value))

  def empty(): Fragment = EMPTY

  def quotedDouble(value: String): Fragment = new Fragment(dev.typr.foundations.Fragment.quotedDouble(value))

  def quotedSingle(value: String): Fragment = new Fragment(dev.typr.foundations.Fragment.quotedSingle(value))

  def value[A](value: A, dbType: DbType[A]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.value(value, dbType.underlying))

  def encode[A](dbType: DbType[A], value: A): Fragment =
    new Fragment(dev.typr.foundations.Fragment.encode(dbType.underlying, value))

  def and(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.and(fragments.map(_.underlying)*))

  def and(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.and(fragments.map(_.underlying).asJava))

  def or(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.or(fragments.map(_.underlying)*))

  def or(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.or(fragments.map(_.underlying).asJava))

  def whereAnd(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.whereAnd(fragments.map(_.underlying)*))

  def whereAnd(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.whereAnd(fragments.map(_.underlying).asJava))

  def whereOr(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.whereOr(fragments.map(_.underlying)*))

  def whereOr(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.whereOr(fragments.map(_.underlying).asJava))

  def set(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.set(fragments.map(_.underlying)*))

  def set(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.set(fragments.map(_.underlying).asJava))

  def parentheses(fragment: Fragment): Fragment =
    new Fragment(dev.typr.foundations.Fragment.parentheses(fragment.underlying))

  def comma(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.comma(fragments.map(_.underlying)*))

  def comma(fragments: Iterable[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.comma(fragments.map(_.underlying).toList.asJava))

  def orderBy(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.orderBy(fragments.map(_.underlying)*))

  def orderBy(fragments: List[Fragment]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.orderBy(fragments.map(_.underlying).asJava))

  def join(fragments: List[Fragment], separator: Fragment): Fragment =
    new Fragment(dev.typr.foundations.Fragment.join(fragments.map(_.underlying).asJava, separator.underlying))

  def concat(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.concat(fragments.map(_.underlying)*))

  /** Scala string interpolator for creating SQL Fragments. */
  extension (sc: StringContext) {
    def sql(args: Fragment*): Fragment = {
      val parts = sc.parts.iterator
      val frags = new scala.collection.mutable.ListBuffer[dev.typr.foundations.Fragment]()

      if (parts.hasNext) {
        val first = parts.next()
        if (first.nonEmpty) {
          frags += dev.typr.foundations.Fragment.of(first)
        }
      }

      val argsIt = args.iterator
      while (parts.hasNext && argsIt.hasNext) {
        frags += argsIt.next().underlying
        val part = parts.next()
        if (part.nonEmpty) {
          frags += dev.typr.foundations.Fragment.of(part)
        }
      }

      while (argsIt.hasNext) {
        frags += argsIt.next().underlying
      }

      frags.result() match {
        case Nil           => Fragment.empty()
        case single :: Nil => new Fragment(single)
        case multiple      =>
          val javaList = new java.util.ArrayList[dev.typr.foundations.Fragment](multiple.size)
          multiple.foreach(javaList.add)
          new Fragment(new dev.typr.foundations.Fragment.Concat(javaList))
      }
    }
  }

  def of(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.of(fragments.map(_.underlying)*))

  /** Emit `DROP TABLE IF EXISTS <table>`. Works on PostgreSQL, DuckDB, MariaDB, MySQL, SQL Server (2016+) and Oracle (23c+). DB2 does not support the
    * `IF EXISTS` clause — wrap the plain `DROP TABLE <table>` in a try/catch for SQLSTATE 42704.
    */
  def dropTableIfExists(table: String): Operation.Execute =
    new Operation.Execute(dev.typr.foundations.Fragment.dropTableIfExists(table))

  def insertOne[Row](table: String, codec: RowCodecNamed[Row], row: Row, except: String*): Operation.Update =
    new Operation.Update(dev.typr.foundations.Fragment.insertOne(table, codec.underlying, row, except*))

  def insertMany[Row](table: String, codec: RowCodecNamed[Row], rows: Iterator[Row], except: String*): Operation.BatchUpdate[Row] =
    new Operation.BatchUpdate(dev.typr.foundations.Fragment.insertMany(table, codec.underlying, rows.asJava, except*))

  def insertOneReturning[Row](table: String, codec: RowCodecNamed[Row], row: Row, except: String*): OperationRead.Query[Row] =
    new OperationRead.Query(dev.typr.foundations.Fragment.insertOneReturning(table, codec.underlying, row, except*))

  def insertOneReturning[In, Out](table: String, writeCodec: RowCodecNamed[In], row: In, readCodec: RowCodecNamed[Out]): OperationRead.Query[Out] =
    new OperationRead.Query(dev.typr.foundations.Fragment.insertOneReturning(
      table, writeCodec.underlying, row, readCodec.underlying))

  def insertOneGenerated[Row, Out](
      table: String, codec: RowCodecNamed[Row], row: Row,
      generatedColumns: Array[String], parser: ResultSetParser[Out], except: String*
  ): Operation.UpdateReturningGeneratedKeys[Out] =
    new Operation.UpdateReturningGeneratedKeys(dev.typr.foundations.Fragment.insertOneGenerated(
      table, codec.underlying, row, generatedColumns, parser.underlying, except*))

  def upsertOne[Row](table: String, codec: RowCodecNamed[Row], row: Row, conflictColumns: String*): Operation.Update =
    new Operation.Update(dev.typr.foundations.Fragment.upsertOne(table, codec.underlying, row, conflictColumns*))

  def upsertMany[Row](table: String, codec: RowCodecNamed[Row], rows: Iterator[Row], conflictColumns: String*): Operation.BatchUpdate[Row] =
    new Operation.BatchUpdate(dev.typr.foundations.Fragment.upsertMany(table, codec.underlying, rows.asJava, conflictColumns*))

  def upsertOneReturning[Row](table: String, codec: RowCodecNamed[Row], row: Row, conflictColumns: String*): OperationRead.Query[Row] =
    new OperationRead.Query(dev.typr.foundations.Fragment.upsertOneReturning(table, codec.underlying, row, conflictColumns*))

  def insertIgnoreOne[Row](table: String, codec: RowCodecNamed[Row], row: Row, conflictColumns: String*): Operation.Update =
    new Operation.Update(dev.typr.foundations.Fragment.insertIgnoreOne(table, codec.underlying, row, conflictColumns*))

  def insertIgnoreMany[Row](table: String, codec: RowCodecNamed[Row], rows: Iterator[Row], conflictColumns: String*): Operation.BatchUpdate[Row] =
    new Operation.BatchUpdate(dev.typr.foundations.Fragment.insertIgnoreMany(table, codec.underlying, rows.asJava, conflictColumns*))

  def row[Row](codec: RowCodecNamed[Row], row: Row, except: String*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.EMPTY.row(codec.underlying, row, except*))

  def valuesList[A](dbType: DbType[A], values: Iterable[A]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.valuesList(dbType.underlying, values.asJava))
}
