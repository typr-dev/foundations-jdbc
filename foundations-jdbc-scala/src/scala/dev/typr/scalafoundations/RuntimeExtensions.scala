package dev.typr.scalafoundations

import dev.typr.foundations.*

import _root_.scala.jdk.CollectionConverters.*
import _root_.scala.jdk.OptionConverters.*

/** Extension to add `.nullable` method to any DbType, converting Optional to Option. */
implicit class DbTypeOps[A](private val dbType: DbType[A]) extends AnyVal {
  def nullable: DbType[Option[A]] =
    dbType.opt.to(Bijections.optionalToOption[A])
}

implicit class EitherOps[L, R](private val either: Either[L, R]) extends AnyVal {
  def rightOrNone: Option[R] = {
    either.asOptional().toScala
  }

  def leftOrNone: Option[L] = {
    either match {
      case left: dev.typr.foundations.Either.Left[L, R] => Some(left.value())
      case _                                            => None
    }
  }
}

implicit class ArrOps[A](private val arr: dev.typr.foundations.data.Arr[A]) extends AnyVal {
  def reshapeOrNone(newDims: Int*): Option[dev.typr.foundations.data.Arr[A]] = {
    arr.reshape(newDims*).toScala
  }

  def getOrNone(indices: Int*): Option[A] = {
    arr.get(indices*).toScala
  }
}

implicit class RangeOps[T <: Comparable[T]](private val range: dev.typr.foundations.data.Range[T]) extends AnyVal {
  def finiteOrNone: Option[dev.typr.foundations.data.RangeFinite[T]] = {
    range.finite().toScala
  }
}

implicit class FragmentBuilderOps(private val builder: Fragment.Builder) extends AnyVal {
  def paramNullable[T](dbType: DbType[T], value: Option[T]): Fragment.Builder = {
    builder.param(dbType.opt(), value.toJava)
  }
}
