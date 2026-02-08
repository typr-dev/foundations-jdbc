package dev.typr.scalafoundations

import dev.typr.foundations.Bijection

import java.util.Optional
import _root_.scala.jdk.OptionConverters.*

object Bijections {

  // ================================
  // Optional<T> ↔ Option[T]
  // ================================

  /** Bijection between Java Optional[T] and Scala Option[T]. Used for type-safe phantom type conversion in PgTypename/MariaTypename.
    *
    * Usage: val typename: PgTypename[Option[String]] = pgType.opt().typename().to(optionalToOption[String])
    */
  def optionalToOption[T]: Bijection[Optional[T], Option[T]] = {
    Bijection.of[Optional[T], Option[T]](
      (opt: Optional[T]) => opt.toScala,
      (option: Option[T]) => option.toJava
    )
  }

  /** Bijection between Scala Option[T] and Java Optional[T]. Inverse of optionalToOption.
    */
  def optionToOptional[T]: Bijection[Option[T], Optional[T]] = optionalToOption[T].inverse()

  // ================================
  // And<A, Optional<B>> ↔ And<A, Option[B]]
  // ================================

  /** Bijection for left join results, converting And[A, Optional[B]] to And[A, Option[B]].
    * Usage: rowParser.leftJoined(other).to(leftJoinToOption)
    */
  def leftJoinToOption[A, B]: Bijection[dev.typr.foundations.And[A, Optional[B]], dev.typr.foundations.And[A, Option[B]]] = {
    Bijection.of[dev.typr.foundations.And[A, Optional[B]], dev.typr.foundations.And[A, Option[B]]](
      (and: dev.typr.foundations.And[A, Optional[B]]) => dev.typr.foundations.And(and.left(), and.right().toScala),
      (and: dev.typr.foundations.And[A, Option[B]]) => dev.typr.foundations.And(and.left(), and.right().toJava)
    )
  }

  /** Bijection for right join results, converting And[Optional[A], B] to And[Option[A], B].
    * Usage: rowParser.rightJoined(other).to(rightJoinToOption)
    */
  def rightJoinToOption[A, B]: Bijection[dev.typr.foundations.And[Optional[A], B], dev.typr.foundations.And[Option[A], B]] = {
    Bijection.of[dev.typr.foundations.And[Optional[A], B], dev.typr.foundations.And[Option[A], B]](
      (and: dev.typr.foundations.And[Optional[A], B]) => dev.typr.foundations.And(and.left().toScala, and.right()),
      (and: dev.typr.foundations.And[Option[A], B]) => dev.typr.foundations.And(and.left().toJava, and.right())
    )
  }

  /** Bijection for full join results, converting And[Optional[A], Optional[B]] to And[Option[A], Option[B]].
    * Usage: rowParser.fullJoined(other).to(fullJoinToOption)
    */
  def fullJoinToOption[A, B]: Bijection[dev.typr.foundations.And[Optional[A], Optional[B]], dev.typr.foundations.And[Option[A], Option[B]]] = {
    Bijection.of[dev.typr.foundations.And[Optional[A], Optional[B]], dev.typr.foundations.And[Option[A], Option[B]]](
      (and: dev.typr.foundations.And[Optional[A], Optional[B]]) => dev.typr.foundations.And(and.left().toScala, and.right().toScala),
      (and: dev.typr.foundations.And[Option[A], Option[B]]) => dev.typr.foundations.And(and.left().toJava, and.right().toJava)
    )
  }

  // Identity bijections for types that don't need conversion
  def identity[T]: Bijection[T, T] = Bijection.identity[T]()
}
