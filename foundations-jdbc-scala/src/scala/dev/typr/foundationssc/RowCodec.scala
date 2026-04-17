package dev.typr.foundationssc

import java.sql.ResultSet
import _root_.scala.jdk.CollectionConverters.*
import _root_.scala.jdk.OptionConverters.*

/** Scala wrapper for dev.typr.foundations.RowCodec that provides Scala-native methods.
  *
  * This class has the same API surface as the Java RowCodec but returns Scala types (Option[T]) instead of Java types (Optional[T]).
  */
class RowCodec[Row](val underlying: dev.typr.foundations.RowCodec[Row]) {

  def join[Row2](other: RowCodec[Row2]): RowCodec[(Row, Row2)] =
    new RowCodec(underlying.join(other.underlying).to(Bijections.andToTuple[Row, Row2]))

  def leftJoin[Row2](other: RowCodec[Row2]): RowCodec[(Row, Option[Row2])] =
    new RowCodec(underlying.leftJoin(other.underlying).to(Bijections.leftJoinToTuple[Row, Row2]))

  def rightJoin[Row2](other: RowCodec[Row2]): RowCodec[(Option[Row], Row2)] =
    new RowCodec(underlying.rightJoin(other.underlying).to(Bijections.rightJoinToTuple[Row, Row2]))

  def fullJoin[Row2](other: RowCodec[Row2]): RowCodec[(Option[Row], Option[Row2])] =
    new RowCodec(underlying.fullJoin(other.underlying).to(Bijections.fullJoinToTuple[Row, Row2]))

  /** Parse all rows from a ResultSet. Returns Scala List instead of java.util.List.
    */
  def all(): ResultSetParser[List[Row]] = {
    new ResultSetParser(underlying.all().map(jlist => jlist.asScala.toList))
  }

  /** Parse exactly one row from a ResultSet. Returns Row directly (throws if not exactly one row).
    */
  def exactlyOne(): ResultSetParser[Row] = {
    new ResultSetParser(underlying.exactlyOne())
  }

  /** Parse the first row from a ResultSet or None if empty. Returns Option[Row] instead of Optional[Row].
    */
  def first(): ResultSetParser[Option[Row]] = {
    new ResultSetParser(underlying.first().map(opt => opt.toScala))
  }

  /** Parse at most one row from a ResultSet or None. Returns Option[Row] instead of Optional[Row].
    */
  def maxOne(): ResultSetParser[Option[Row]] = {
    new ResultSetParser(underlying.maxOne().map(opt => opt.toScala))
  }

  /** Parse a single row from the current position in ResultSet.
    */
  def parse(rs: ResultSet): Row = underlying.parse(rs)

  /** Create a DbJson codec that encodes rows as JSON arrays.
    */
  def jsonArray(): dev.typr.foundations.DbJson[Row] =
    dev.typr.foundations.DbJsonRow.jsonArray(underlying)

  /** Create a DbJson codec that encodes rows as JSON objects with named fields.
    */
  def jsonObject(columnNames: List[String]): dev.typr.foundations.DbJson[Row] =
    import _root_.scala.jdk.CollectionConverters.*
    dev.typr.foundations.DbJsonRow.jsonObject(underlying, columnNames.asJava)
}

/** Scala wrapper for dev.typr.foundations.RowCodecNamed. Adds columnNames, columnList, and no-argument jsonObject().
  */
class RowCodecNamed[Row](override val underlying: dev.typr.foundations.RowCodecNamed[Row]) extends RowCodec[Row](underlying) {

  def columnNames: List[String] =
    import _root_.scala.jdk.CollectionConverters.*
    underlying.columnNames().asScala.toList

  def columnList: Fragment = new Fragment(underlying.columnList())

  /** Return a copy of this codec where every column name is prefixed with `alias.`. Apply before a join to keep `columnList` unambiguous:
    *
    * {{{
    * empCodec.aliased("e").leftJoin(deptCodec.aliased("d"))
    * }}}
    */
  def aliased(alias: String): RowCodecNamed[Row] =
    new RowCodecNamed(underlying.aliased(alias))

  /** Inner join that preserves column names. Returns a RowCodecNamed. */
  def join[Row2](other: RowCodecNamed[Row2]): RowCodecNamed[(Row, Row2)] =
    new RowCodecNamed(underlying.join(other.underlying).to(Bijections.andToTuple[Row, Row2]))

  /** Left join that preserves column names. Right-side columns are wrapped in .opt(). */
  def leftJoin[Row2](other: RowCodecNamed[Row2]): RowCodecNamed[(Row, Option[Row2])] =
    new RowCodecNamed(underlying.leftJoin(other.underlying).to(Bijections.leftJoinToTuple[Row, Row2]))

  /** Right join that preserves column names. Left-side columns are wrapped in .opt(). */
  def rightJoin[Row2](other: RowCodecNamed[Row2]): RowCodecNamed[(Option[Row], Row2)] =
    new RowCodecNamed(underlying.rightJoin(other.underlying).to(Bijections.rightJoinToTuple[Row, Row2]))

  /** Full outer join that preserves column names. Both sides are wrapped in .opt(). */
  def fullJoin[Row2](other: RowCodecNamed[Row2]): RowCodecNamed[(Option[Row], Option[Row2])] =
    new RowCodecNamed(underlying.fullJoin(other.underlying).to(Bijections.fullJoinToTuple[Row, Row2]))

  def to[Row2](forward: Row => Row2, backward: Row2 => Row): RowCodecNamed[Row2] =
    new RowCodecNamed(underlying.to(dev.typr.foundations.Bijection.of[Row, Row2](r => forward(r), r2 => backward(r2))))

  def jsonObject(): dev.typr.foundations.DbJson[Row] =
    dev.typr.foundations.DbJsonRow.jsonObject(underlying)
}

object RowCodec {

  /** Create a type-safe builder for RowCodec.
    */
  def builder[Row](): RowCodecBuilders.Builder0[Row] = RowCodecBuilders.builder[Row]()

  /** Create a type-safe named builder for RowCodec.
    */
  def namedBuilder[Row](): RowCodecNamedBuilders.Builder0[Row] = RowCodecNamedBuilders.builder[Row]()

  /** Create a single-column row parser.
    */
  def of[T](dbType: DbType[T]): RowCodec[T] =
    new RowCodec(dev.typr.foundations.RowCodec.of(dbType.underlying))

  def of[T0, T1](t0: DbType[T0], t1: DbType[T1]): RowCodec[(T0, T1)] =
    new RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying).to(Bijections.andToTuple[T0, T1]))

  def of[T0, T1, T2](t0: DbType[T0], t1: DbType[T1], t2: DbType[T2]): RowCodec[(T0, T1, T2)] =
    new RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying).to(Bijections.tupleToScala3[T0, T1, T2]))

  def of[T0, T1, T2, T3](t0: DbType[T0], t1: DbType[T1], t2: DbType[T2], t3: DbType[T3]): RowCodec[(T0, T1, T2, T3)] =
    new RowCodec(dev.typr.foundations.RowCodec.of(t0.underlying, t1.underlying, t2.underlying, t3.underlying).to(Bijections.tupleToScala4[T0, T1, T2, T3]))

  def of[T0, T1, T2, T3, T4](t0: DbType[T0], t1: DbType[T1], t2: DbType[T2], t3: DbType[T3], t4: DbType[T4]): RowCodec[(T0, T1, T2, T3, T4)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying)
        .to(Bijections.tupleToScala5[T0, T1, T2, T3, T4])
    )

  def of[T0, T1, T2, T3, T4, T5](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5]
  ): RowCodec[(T0, T1, T2, T3, T4, T5)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying)
        .to(Bijections.tupleToScala6[T0, T1, T2, T3, T4, T5])
    )

  def of[T0, T1, T2, T3, T4, T5, T6](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying)
        .to(Bijections.tupleToScala7[T0, T1, T2, T3, T4, T5, T6])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying)
        .to(Bijections.tupleToScala8[T0, T1, T2, T3, T4, T5, T6, T7])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(t0.underlying, t1.underlying, t2.underlying, t3.underlying, t4.underlying, t5.underlying, t6.underlying, t7.underlying, t8.underlying)
        .to(Bijections.tupleToScala9[T0, T1, T2, T3, T4, T5, T6, T7, T8])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying
        )
        .to(Bijections.tupleToScala10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying
        )
        .to(Bijections.tupleToScala11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying
        )
        .to(Bijections.tupleToScala12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying
        )
        .to(Bijections.tupleToScala13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying
        )
        .to(Bijections.tupleToScala14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying
        )
        .to(Bijections.tupleToScala15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying
        )
        .to(Bijections.tupleToScala16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying
        )
        .to(Bijections.tupleToScala17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16],
      t17: DbType[T17]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying,
          t17.underlying
        )
        .to(Bijections.tupleToScala18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16],
      t17: DbType[T17],
      t18: DbType[T18]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying,
          t17.underlying,
          t18.underlying
        )
        .to(Bijections.tupleToScala19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16],
      t17: DbType[T17],
      t18: DbType[T18],
      t19: DbType[T19]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying,
          t17.underlying,
          t18.underlying,
          t19.underlying
        )
        .to(Bijections.tupleToScala20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16],
      t17: DbType[T17],
      t18: DbType[T18],
      t19: DbType[T19],
      t20: DbType[T20]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying,
          t17.underlying,
          t18.underlying,
          t19.underlying,
          t20.underlying
        )
        .to(Bijections.tupleToScala21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20])
    )

  def of[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
      t0: DbType[T0],
      t1: DbType[T1],
      t2: DbType[T2],
      t3: DbType[T3],
      t4: DbType[T4],
      t5: DbType[T5],
      t6: DbType[T6],
      t7: DbType[T7],
      t8: DbType[T8],
      t9: DbType[T9],
      t10: DbType[T10],
      t11: DbType[T11],
      t12: DbType[T12],
      t13: DbType[T13],
      t14: DbType[T14],
      t15: DbType[T15],
      t16: DbType[T16],
      t17: DbType[T17],
      t18: DbType[T18],
      t19: DbType[T19],
      t20: DbType[T20],
      t21: DbType[T21]
  ): RowCodec[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new RowCodec(
      dev.typr.foundations.RowCodec
        .of(
          t0.underlying,
          t1.underlying,
          t2.underlying,
          t3.underlying,
          t4.underlying,
          t5.underlying,
          t6.underlying,
          t7.underlying,
          t8.underlying,
          t9.underlying,
          t10.underlying,
          t11.underlying,
          t12.underlying,
          t13.underlying,
          t14.underlying,
          t15.underlying,
          t16.underlying,
          t17.underlying,
          t18.underlying,
          t19.underlying,
          t20.underlying,
          t21.underlying
        )
        .to(Bijections.tupleToScala22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21])
    )

  /** Create a single-column named row codec.
    */
  def ofNamed[T](name: String, dbType: DbType[T]): RowCodecNamed[T] =
    new RowCodecNamed(dev.typr.foundations.RowCodec.ofNamed(name, dbType.underlying))
}
