package dev.typr.scalafoundations

import dev.typr.foundations.DbType
import scala.jdk.CollectionConverters.*

/** Type-safe builders for Scala RowParser.
  *
  * Usage:
  * {{{
  * val parser: RowParser[Product] = RowParser.builder[Product]()
  *   .field(PgTypes.int4)(_.id)
  *   .field(PgTypes.text)(_.name)
  *   .field(PgTypes.numeric)(_.price)
  *   .build(Product.apply)
  * }}}
  */
object RowParserBuilders {
  def builder[Row](): Builder0[Row] = new Builder0()

  class Builder0[Row] private[scalafoundations] () {
    private val types = scala.collection.mutable.ListBuffer[DbType[?]]()
    private val getters = scala.collection.mutable.ListBuffer[Row => Any]()

    def field[F](tpe: DbType[F])(getter: Row => F): Builder1[Row, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder1(types, getters)
    }
  }

  class Builder1[Row, T0] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder2[Row, T0, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder2(types, getters)
    }
  }

  class Builder2[Row, T0, T1] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder3[Row, T0, T1, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder3(types, getters)
    }
  }

  class Builder3[Row, T0, T1, T2] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder4[Row, T0, T1, T2, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder4(types, getters)
    }
  }

  class Builder4[Row, T0, T1, T2, T3] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder5[Row, T0, T1, T2, T3, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder5(types, getters)
    }
  }

  class Builder5[Row, T0, T1, T2, T3, T4] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder6[Row, T0, T1, T2, T3, T4, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder6(types, getters)
    }
  }

  class Builder6[Row, T0, T1, T2, T3, T4, T5] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder7[Row, T0, T1, T2, T3, T4, T5, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder7(types, getters)
    }
  }

  class Builder7[Row, T0, T1, T2, T3, T4, T5, T6] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder8[Row, T0, T1, T2, T3, T4, T5, T6, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder8(types, getters)
    }
  }

  class Builder8[Row, T0, T1, T2, T3, T4, T5, T6, T7] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder9[Row, T0, T1, T2, T3, T4, T5, T6, T7, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder9(types, getters)
    }
  }

  class Builder9[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder10[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder10(types, getters)
    }
  }

  class Builder10[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder11[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder11(types, getters)
    }
  }

  class Builder11[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder12[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder12(types, getters)
    }
  }

  class Builder12[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder13[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder13(types, getters)
    }
  }

  class Builder13[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder14[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder14(types, getters)
    }
  }

  class Builder14[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder15[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder15(types, getters)
    }
  }

  class Builder15[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder16[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder16(types, getters)
    }
  }

  class Builder16[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder17[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder17(types, getters)
    }
  }

  class Builder17[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder18[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder18(types, getters)
    }
  }

  class Builder18[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder19[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder19(types, getters)
    }
  }

  class Builder19[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder20[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder20(types, getters)
    }
  }

  class Builder20[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder21[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder21(types, getters)
    }
  }

  class Builder21[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder22[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder22(types, getters)
    }
  }

  class Builder22[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder23[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder23(types, getters)
    }
  }

  class Builder23[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder24[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder24(types, getters)
    }
  }

  class Builder24[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder25[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder25(types, getters)
    }
  }

  class Builder25[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder26[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder26(types, getters)
    }
  }

  class Builder26[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder27[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder27(types, getters)
    }
  }

  class Builder27[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder28[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder28(types, getters)
    }
  }

  class Builder28[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder29[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder29(types, getters)
    }
  }

  class Builder29[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder30[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder30(types, getters)
    }
  }

  class Builder30[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder31[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder31(types, getters)
    }
  }

  class Builder31[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder32[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder32(types, getters)
    }
  }

  class Builder32[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder33[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder33(types, getters)
    }
  }

  class Builder33[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder34[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder34(types, getters)
    }
  }

  class Builder34[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder35[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder35(types, getters)
    }
  }

  class Builder35[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder36[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder36(types, getters)
    }
  }

  class Builder36[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder37[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder37(types, getters)
    }
  }

  class Builder37[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder38[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder38(types, getters)
    }
  }

  class Builder38[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder39[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder39(types, getters)
    }
  }

  class Builder39[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder40[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder40(types, getters)
    }
  }

  class Builder40[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder41[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder41(types, getters)
    }
  }

  class Builder41[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder42[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder42(types, getters)
    }
  }

  class Builder42[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder43[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder43(types, getters)
    }
  }

  class Builder43[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder44[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder44(types, getters)
    }
  }

  class Builder44[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder45[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder45(types, getters)
    }
  }

  class Builder45[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder46[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder46(types, getters)
    }
  }

  class Builder46[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder47[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder47(types, getters)
    }
  }

  class Builder47[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder48[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder48(types, getters)
    }
  }

  class Builder48[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder49[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder49(types, getters)
    }
  }

  class Builder49[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder50[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder50(types, getters)
    }
  }

  class Builder50[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder51[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder51(types, getters)
    }
  }

  class Builder51[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder52[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder52(types, getters)
    }
  }

  class Builder52[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder53[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder53(types, getters)
    }
  }

  class Builder53[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder54[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder54(types, getters)
    }
  }

  class Builder54[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder55[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder55(types, getters)
    }
  }

  class Builder55[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder56[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder56(types, getters)
    }
  }

  class Builder56[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder57[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder57(types, getters)
    }
  }

  class Builder57[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder58[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder58(types, getters)
    }
  }

  class Builder58[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder59[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder59(types, getters)
    }
  }

  class Builder59[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder60[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder60(types, getters)
    }
  }

  class Builder60[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder61[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder61(types, getters)
    }
  }

  class Builder61[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder62[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder62(types, getters)
    }
  }

  class Builder62[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder63[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder63(types, getters)
    }
  }

  class Builder63[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder64[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder64(types, getters)
    }
  }

  class Builder64[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder65[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder65(types, getters)
    }
  }

  class Builder65[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder66[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder66(types, getters)
    }
  }

  class Builder66[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder67[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder67(types, getters)
    }
  }

  class Builder67[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder68[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder68(types, getters)
    }
  }

  class Builder68[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder69[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder69(types, getters)
    }
  }

  class Builder69[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder70[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder70(types, getters)
    }
  }

  class Builder70[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder71[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder71(types, getters)
    }
  }

  class Builder71[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder72[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder72(types, getters)
    }
  }

  class Builder72[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder73[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder73(types, getters)
    }
  }

  class Builder73[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder74[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder74(types, getters)
    }
  }

  class Builder74[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder75[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder75(types, getters)
    }
  }

  class Builder75[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder76[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder76(types, getters)
    }
  }

  class Builder76[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder77[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder77(types, getters)
    }
  }

  class Builder77[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder78[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder78(types, getters)
    }
  }

  class Builder78[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder79[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder79(types, getters)
    }
  }

  class Builder79[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder80[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder80(types, getters)
    }
  }

  class Builder80[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder81[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder81(types, getters)
    }
  }

  class Builder81[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder82[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder82(types, getters)
    }
  }

  class Builder82[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder83[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder83(types, getters)
    }
  }

  class Builder83[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder84[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder84(types, getters)
    }
  }

  class Builder84[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder85[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder85(types, getters)
    }
  }

  class Builder85[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder86[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder86(types, getters)
    }
  }

  class Builder86[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder87[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder87(types, getters)
    }
  }

  class Builder87[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder88[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder88(types, getters)
    }
  }

  class Builder88[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder89[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder89(types, getters)
    }
  }

  class Builder89[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder90[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder90(types, getters)
    }
  }

  class Builder90[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder91[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder91(types, getters)
    }
  }

  class Builder91[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder92[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder92(types, getters)
    }
  }

  class Builder92[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder93[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder93(types, getters)
    }
  }

  class Builder93[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder94[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder94(types, getters)
    }
  }

  class Builder94[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder95[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder95(types, getters)
    }
  }

  class Builder95[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93], arr(94).asInstanceOf[T94]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder96[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder96(types, getters)
    }
  }

  class Builder96[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93], arr(94).asInstanceOf[T94], arr(95).asInstanceOf[T95]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder97[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder97(types, getters)
    }
  }

  class Builder97[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93], arr(94).asInstanceOf[T94], arr(95).asInstanceOf[T95], arr(96).asInstanceOf[T96]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder98[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder98(types, getters)
    }
  }

  class Builder98[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93], arr(94).asInstanceOf[T94], arr(95).asInstanceOf[T95], arr(96).asInstanceOf[T96], arr(97).asInstanceOf[T97]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
    def field[F](tpe: DbType[F])(getter: Row => F): Builder99[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F] = {
      types += tpe
      getters += getter.asInstanceOf[Row => Any]
      new Builder99(types, getters)
    }
  }

  class Builder99[Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98] private[scalafoundations] (
    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
  ) {
    def build(decode: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98) => Row): RowParser[Row] = {
      val capturedGetters = getters.toList
      val javaParser = new dev.typr.foundations.RowParser[Row](
        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        arr => decode(arr(0).asInstanceOf[T0], arr(1).asInstanceOf[T1], arr(2).asInstanceOf[T2], arr(3).asInstanceOf[T3], arr(4).asInstanceOf[T4], arr(5).asInstanceOf[T5], arr(6).asInstanceOf[T6], arr(7).asInstanceOf[T7], arr(8).asInstanceOf[T8], arr(9).asInstanceOf[T9], arr(10).asInstanceOf[T10], arr(11).asInstanceOf[T11], arr(12).asInstanceOf[T12], arr(13).asInstanceOf[T13], arr(14).asInstanceOf[T14], arr(15).asInstanceOf[T15], arr(16).asInstanceOf[T16], arr(17).asInstanceOf[T17], arr(18).asInstanceOf[T18], arr(19).asInstanceOf[T19], arr(20).asInstanceOf[T20], arr(21).asInstanceOf[T21], arr(22).asInstanceOf[T22], arr(23).asInstanceOf[T23], arr(24).asInstanceOf[T24], arr(25).asInstanceOf[T25], arr(26).asInstanceOf[T26], arr(27).asInstanceOf[T27], arr(28).asInstanceOf[T28], arr(29).asInstanceOf[T29], arr(30).asInstanceOf[T30], arr(31).asInstanceOf[T31], arr(32).asInstanceOf[T32], arr(33).asInstanceOf[T33], arr(34).asInstanceOf[T34], arr(35).asInstanceOf[T35], arr(36).asInstanceOf[T36], arr(37).asInstanceOf[T37], arr(38).asInstanceOf[T38], arr(39).asInstanceOf[T39], arr(40).asInstanceOf[T40], arr(41).asInstanceOf[T41], arr(42).asInstanceOf[T42], arr(43).asInstanceOf[T43], arr(44).asInstanceOf[T44], arr(45).asInstanceOf[T45], arr(46).asInstanceOf[T46], arr(47).asInstanceOf[T47], arr(48).asInstanceOf[T48], arr(49).asInstanceOf[T49], arr(50).asInstanceOf[T50], arr(51).asInstanceOf[T51], arr(52).asInstanceOf[T52], arr(53).asInstanceOf[T53], arr(54).asInstanceOf[T54], arr(55).asInstanceOf[T55], arr(56).asInstanceOf[T56], arr(57).asInstanceOf[T57], arr(58).asInstanceOf[T58], arr(59).asInstanceOf[T59], arr(60).asInstanceOf[T60], arr(61).asInstanceOf[T61], arr(62).asInstanceOf[T62], arr(63).asInstanceOf[T63], arr(64).asInstanceOf[T64], arr(65).asInstanceOf[T65], arr(66).asInstanceOf[T66], arr(67).asInstanceOf[T67], arr(68).asInstanceOf[T68], arr(69).asInstanceOf[T69], arr(70).asInstanceOf[T70], arr(71).asInstanceOf[T71], arr(72).asInstanceOf[T72], arr(73).asInstanceOf[T73], arr(74).asInstanceOf[T74], arr(75).asInstanceOf[T75], arr(76).asInstanceOf[T76], arr(77).asInstanceOf[T77], arr(78).asInstanceOf[T78], arr(79).asInstanceOf[T79], arr(80).asInstanceOf[T80], arr(81).asInstanceOf[T81], arr(82).asInstanceOf[T82], arr(83).asInstanceOf[T83], arr(84).asInstanceOf[T84], arr(85).asInstanceOf[T85], arr(86).asInstanceOf[T86], arr(87).asInstanceOf[T87], arr(88).asInstanceOf[T88], arr(89).asInstanceOf[T89], arr(90).asInstanceOf[T90], arr(91).asInstanceOf[T91], arr(92).asInstanceOf[T92], arr(93).asInstanceOf[T93], arr(94).asInstanceOf[T94], arr(95).asInstanceOf[T95], arr(96).asInstanceOf[T96], arr(97).asInstanceOf[T97], arr(98).asInstanceOf[T98]),
        row => capturedGetters.map(_(row)).toArray
      )
      new RowParser(javaParser)
    }
  }
}
