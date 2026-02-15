package dev.typr.foundationssc

import dev.typr.foundations.Bijection

import java.util.Optional
import _root_.scala.jdk.OptionConverters.*

object Bijections {

  def optionalToOption[T]: Bijection[Optional[T], Option[T]] = {
    Bijection.of[Optional[T], Option[T]](
      (opt: Optional[T]) => opt.toScala,
      (option: Option[T]) => option.toJava
    )
  }

  def optionToOptional[T]: Bijection[Option[T], Optional[T]] = optionalToOption[T].inverse()

  def andToTuple[A, B]: Bijection[dev.typr.foundations.And[A, B], (A, B)] = {
    Bijection.of[dev.typr.foundations.And[A, B], (A, B)](
      (and: dev.typr.foundations.And[A, B]) => (and.left(), and.right()),
      (t: (A, B)) => dev.typr.foundations.And(t._1, t._2)
    )
  }

  def leftJoinToTuple[A, B]: Bijection[dev.typr.foundations.And[A, Optional[B]], (A, Option[B])] = {
    Bijection.of[dev.typr.foundations.And[A, Optional[B]], (A, Option[B])](
      (and: dev.typr.foundations.And[A, Optional[B]]) => (and.left(), and.right().toScala),
      (t: (A, Option[B])) => dev.typr.foundations.And(t._1, t._2.toJava)
    )
  }

  def rightJoinToTuple[A, B]: Bijection[dev.typr.foundations.And[Optional[A], B], (Option[A], B)] = {
    Bijection.of[dev.typr.foundations.And[Optional[A], B], (Option[A], B)](
      (and: dev.typr.foundations.And[Optional[A], B]) => (and.left().toScala, and.right()),
      (t: (Option[A], B)) => dev.typr.foundations.And(t._1.toJava, t._2)
    )
  }

  def fullJoinToTuple[A, B]: Bijection[dev.typr.foundations.And[Optional[A], Optional[B]], (Option[A], Option[B])] = {
    Bijection.of[dev.typr.foundations.And[Optional[A], Optional[B]], (Option[A], Option[B])](
      (and: dev.typr.foundations.And[Optional[A], Optional[B]]) => (and.left().toScala, and.right().toScala),
      (t: (Option[A], Option[B])) => dev.typr.foundations.And(t._1.toJava, t._2.toJava)
    )
  }

  def identity[T]: Bijection[T, T] = Bijection.identity[T]()
}
