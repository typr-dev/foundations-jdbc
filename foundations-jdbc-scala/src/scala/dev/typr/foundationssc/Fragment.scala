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

  def paramRow[Row](parser: RowCodecNamed[Row], except: String*): RowParamBuilder[Row] =
    new RowParamBuilder(underlying.paramRow(parser.underlying, except*))

  def row[Row](parser: RowCodecNamed[Row], row: Row, except: String*): Fragment =
    new Fragment(underlying.row(parser.underlying, row, except*))

  def param[P0](dbType: DbType[P0]): ParamBuilders.ParamBuilder1[P0] =
    new ParamBuilders.ParamBuilder1(underlying.param(dbType.underlying))

  def optionally(inner: Fragment): ParamBuilders.ParamBuilder1[Boolean] =
    new ParamBuilders.ParamBuilder1(
      ParamBuilders.createOps1(
        underlying.optionally(inner.underlying),
        dev.typr.foundations.Bijection.of[java.lang.Boolean, Boolean]((jb: java.lang.Boolean) => jb: Boolean, (sb: Boolean) => sb: java.lang.Boolean)
      )
    )

  def optionally[A](builder: ParamBuilders.ParamBuilder1[A]): ParamBuilders.ParamBuilder1[Option[A]] =
    val innerFrag = builder.ops.buildDone()
    val newFrag = underlying.append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
    val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder1[java.util.Optional[A]](newFrag, null)
    new ParamBuilders.ParamBuilder1(ParamBuilders.createOps1(newJava, Bijections.optionalToOption[A]))

  def optionally[A, B](builder: ParamBuilders.ParamBuilder2[A, B]): ParamBuilders.ParamBuilder1[Option[(A, B)]] =
    val innerFrag = builder.ops.buildDone()
    val newFrag = underlying.append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
    val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder1[java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]]](newFrag, null)
    val bij = dev.typr.foundations.Bijection.of[java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]], Option[(A, B)]](
      (opt: java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]]) => if opt.isPresent then Some((opt.get._1(), opt.get._2())) else None,
      (v: Option[(A, B)]) => { import _root_.scala.jdk.OptionConverters.*; v.map(t => dev.typr.foundations.Tuple.of(t._1, t._2)).toJava }
    )
    new ParamBuilders.ParamBuilder1(ParamBuilders.createOps1(newJava, bij))

  def optionally[A, B, C](builder: ParamBuilders.ParamBuilder3[A, B, C]): ParamBuilders.ParamBuilder1[Option[(A, B, C)]] =
    val innerFrag = builder.ops.buildDone()
    val newFrag = underlying.append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
    val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder1[java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]]](newFrag, null)
    val bij = dev.typr.foundations.Bijection.of[java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]], Option[(A, B, C)]](
      (opt: java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]]) => if opt.isPresent then Some((opt.get._1(), opt.get._2(), opt.get._3())) else None,
      (v: Option[(A, B, C)]) => { import _root_.scala.jdk.OptionConverters.*; v.map(t => dev.typr.foundations.Tuple.of(t._1, t._2, t._3)).toJava }
    )
    new ParamBuilders.ParamBuilder1(ParamBuilders.createOps1(newJava, bij))
}

object Fragment {
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

  def insertInto[Row](table: String, codec: RowCodecNamed[Row], except: String*): RowTemplate.Update[Row] =
    new RowTemplate.Update(dev.typr.foundations.Fragment.insertInto(table, codec.underlying, except*))

  /** Emit `DROP TABLE IF EXISTS <table>`. Works on PostgreSQL, DuckDB, MariaDB, MySQL, SQL Server (2016+) and Oracle (23c+). DB2 does not support the
    * `IF EXISTS` clause — wrap the plain `DROP TABLE <table>` in a try/catch for SQLSTATE 42704.
    */
  def dropTableIfExists(table: String): Operation.Execute =
    new Operation.Execute(dev.typr.foundations.Fragment.dropTableIfExists(table))

  def insertIntoReturning[Row](table: String, codec: RowCodecNamed[Row], except: String*): RowTemplate.Query[Row, Row] =
    new RowTemplate.Query(dev.typr.foundations.Fragment.insertIntoReturning(table, codec.underlying, except*))

  def insertIntoReturning[In, Out](table: String, writeCodec: RowCodecNamed[In], readCodec: RowCodecNamed[Out]): RowTemplate.Query[In, Out] =
    new RowTemplate.Query(dev.typr.foundations.Fragment.insertIntoReturning(table, writeCodec.underlying, readCodec.underlying))

  def insertIntoGeneratedKeys[Row, Out](
      table: String,
      codec: RowCodecNamed[Row],
      generatedColumns: Seq[String],
      parser: ResultSetParser[Out],
      except: String*
  ): RowTemplate.GeneratedKeys[Row, Out] =
    new RowTemplate.GeneratedKeys(
      dev.typr.foundations.Fragment.insertIntoGeneratedKeys(table, codec.underlying, generatedColumns.toArray, parser.underlying, except*)
    )

  /** Convenience for the common single-generated-column insert (AUTO_INCREMENT / IDENTITY / GENERATED BY DEFAULT AS IDENTITY). Excludes the generated column
    * from the INSERT's VALUES list.
    */
  def insertIntoGeneratedKey[Row, Out](
      table: String,
      codec: RowCodecNamed[Row],
      generatedColumn: String,
      parser: ResultSetParser[Out]
  ): RowTemplate.GeneratedKeys[Row, Out] =
    new RowTemplate.GeneratedKeys(
      dev.typr.foundations.Fragment.insertIntoGeneratedKeys(table, codec.underlying, Array(generatedColumn), parser.underlying, generatedColumn)
    )

  def upsert[Row](table: String, codec: RowCodecNamed[Row], conflictColumns: String*): RowTemplate.Update[Row] =
    new RowTemplate.Update(dev.typr.foundations.Fragment.upsert(table, codec.underlying, conflictColumns*))

  def upsertReturning[Row](table: String, codec: RowCodecNamed[Row], conflictColumns: String*): RowTemplate.Query[Row, Row] =
    new RowTemplate.Query(dev.typr.foundations.Fragment.upsertReturning(table, codec.underlying, conflictColumns*))

  def insertIgnore[Row](table: String, codec: RowCodecNamed[Row], conflictColumns: String*): RowTemplate.Update[Row] =
    new RowTemplate.Update(dev.typr.foundations.Fragment.insertIgnore(table, codec.underlying, conflictColumns*))

  def row[Row](codec: RowCodecNamed[Row], row: Row, except: String*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.EMPTY.row(codec.underlying, row, except*))

  def valuesList[A](dbType: DbType[A], values: Iterable[A]): Fragment =
    new Fragment(dev.typr.foundations.Fragment.valuesList(dbType.underlying, values.asJava))
}
