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

  def query[T](parser: ResultSetParser[T]): Operation.Query[T] =
    Operation.Query(this, parser)

  def queryExactlyOne[T](tpe: DbType[T]): Operation.Query[T] =
    query(RowCodec.of(tpe).exactlyOne())

  def queryExactlyOne[T](codec: RowCodec[T]): Operation.Query[T] =
    query(codec.exactlyOne())

  def queryAll[T](tpe: DbType[T]): Operation.Query[List[T]] =
    query(RowCodec.of(tpe).all())

  def queryAll[T](codec: RowCodec[T]): Operation.Query[List[T]] =
    query(codec.all())

  def queryMaxOne[T](tpe: DbType[T]): Operation.Query[Option[T]] =
    query(RowCodec.of(tpe).maxOne())

  def queryMaxOne[T](codec: RowCodec[T]): Operation.Query[Option[T]] =
    query(codec.maxOne())

  def update(): Operation.Update =
    Operation.Update(this)

  def execute(): Operation[Unit] = update().voided

  def updateReturning[T](parser: ResultSetParser[T]): Operation.UpdateReturning[T] =
    Operation.UpdateReturning(this, parser)

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
    new ParamBuilders.ParamBuilder1(underlying.optionally(inner.underlying))

  def optionally[A](builder: ParamBuilders.ParamBuilder1[A]): ParamBuilders.ParamBuilder1[Option[A]] =
    new ParamBuilders.ParamBuilder1(
      underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
      List(Some(OptionallyTransforms.optionToOptional)))

  def optionally[A, B](builder: ParamBuilders.ParamBuilder2[A, B]): ParamBuilders.ParamBuilder1[Option[(A, B)]] =
    new ParamBuilders.ParamBuilder1(
      underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
      List(Some(OptionallyTransforms.optionTupleToOptionalTuple2)))

  def optionally[A, B, C](builder: ParamBuilders.ParamBuilder3[A, B, C]): ParamBuilders.ParamBuilder1[Option[(A, B, C)]] =
    new ParamBuilders.ParamBuilder1(
      underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
      List(Some(OptionallyTransforms.optionTupleToOptionalTuple3)))
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

  /** Extension to allow `dbType(value)` syntax for creating Fragment values.
    * Example: `PgTypes.bool(true)` instead of `Fragment.encode(PgTypes.bool, true)`.
    */
  extension [A](dbType: DbType[A])
    def apply(value: A): Fragment = Fragment.encode(dbType, value)

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
        case multiple =>
          val javaList = new java.util.ArrayList[dev.typr.foundations.Fragment](multiple.size)
          multiple.foreach(javaList.add)
          new Fragment(new dev.typr.foundations.Fragment.Concat(javaList))
      }
    }
  }

  def of(fragments: Fragment*): Fragment =
    new Fragment(dev.typr.foundations.Fragment.of(fragments.map(_.underlying)*))
}

private[foundationssc] object OptionallyTransforms:
  val optionToOptional: AnyRef => AnyRef = { v =>
    import _root_.scala.jdk.OptionConverters.*
    v.asInstanceOf[Option[?]].toJava
  }

  val optionTupleToOptionalTuple2: AnyRef => AnyRef = { v =>
    import _root_.scala.jdk.OptionConverters.*
    v.asInstanceOf[Option[(?, ?)]].map { case (a, b) =>
      dev.typr.foundations.Tuple.of(a.asInstanceOf[AnyRef], b.asInstanceOf[AnyRef])
    }.toJava
  }

  val optionTupleToOptionalTuple3: AnyRef => AnyRef = { v =>
    import _root_.scala.jdk.OptionConverters.*
    v.asInstanceOf[Option[(?, ?, ?)]].map { case (a, b, c) =>
      dev.typr.foundations.Tuple.of(a.asInstanceOf[AnyRef], b.asInstanceOf[AnyRef], c.asInstanceOf[AnyRef])
    }.toJava
  }
