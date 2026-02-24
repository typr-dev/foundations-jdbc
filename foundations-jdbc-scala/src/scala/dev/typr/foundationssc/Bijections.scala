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

  def andToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[A, B], (A, B)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[A, B], (A, B)](
      (t: dev.typr.foundations.Tuple.Tuple2[A, B]) => (t._1(), t._2()),
      (t: (A, B)) => dev.typr.foundations.Tuple.of(t._1, t._2)
    )
  }

  def leftJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[A, Optional[B]], (A, Option[B])] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[A, Optional[B]], (A, Option[B])](
      (t: dev.typr.foundations.Tuple.Tuple2[A, Optional[B]]) => (t._1(), t._2().toScala),
      (t: (A, Option[B])) => dev.typr.foundations.Tuple.of(t._1, t._2.toJava)
    )
  }

  def rightJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[Optional[A], B], (Option[A], B)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[Optional[A], B], (Option[A], B)](
      (t: dev.typr.foundations.Tuple.Tuple2[Optional[A], B]) => (t._1().toScala, t._2()),
      (t: (Option[A], B)) => dev.typr.foundations.Tuple.of(t._1.toJava, t._2)
    )
  }

  def fullJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]], (Option[A], Option[B])] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]], (Option[A], Option[B])](
      (t: dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]]) => (t._1().toScala, t._2().toScala),
      (t: (Option[A], Option[B])) => dev.typr.foundations.Tuple.of(t._1.toJava, t._2.toJava)
    )
  }

  def identity[T]: Bijection[T, T] = Bijection.identity[T]()
}
