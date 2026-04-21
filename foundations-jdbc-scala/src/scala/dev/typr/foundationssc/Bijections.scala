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

  private[foundationssc] def andToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[A, B], (A, B)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[A, B], (A, B)](
      (t: dev.typr.foundations.Tuple.Tuple2[A, B]) => (t._1(), t._2()),
      (t: (A, B)) => dev.typr.foundations.Tuple.of(t._1, t._2)
    )
  }

  private[foundationssc] def leftJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[A, Optional[B]], (A, Option[B])] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[A, Optional[B]], (A, Option[B])](
      (t: dev.typr.foundations.Tuple.Tuple2[A, Optional[B]]) => (t._1(), t._2().toScala),
      (t: (A, Option[B])) => dev.typr.foundations.Tuple.of(t._1, t._2.toJava)
    )
  }

  private[foundationssc] def rightJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[Optional[A], B], (Option[A], B)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[Optional[A], B], (Option[A], B)](
      (t: dev.typr.foundations.Tuple.Tuple2[Optional[A], B]) => (t._1().toScala, t._2()),
      (t: (Option[A], B)) => dev.typr.foundations.Tuple.of(t._1.toJava, t._2)
    )
  }

  private[foundationssc] def fullJoinToTuple[A, B]: Bijection[dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]], (Option[A], Option[B])] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]], (Option[A], Option[B])](
      (t: dev.typr.foundations.Tuple.Tuple2[Optional[A], Optional[B]]) => (t._1().toScala, t._2().toScala),
      (t: (Option[A], Option[B])) => dev.typr.foundations.Tuple.of(t._1.toJava, t._2.toJava)
    )
  }

  private[foundationssc] def tupleToScala3[T0, T1, T2]: Bijection[dev.typr.foundations.Tuple.Tuple3[T0, T1, T2], (T0, T1, T2)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple3[T0, T1, T2], (T0, T1, T2)](
      (t: dev.typr.foundations.Tuple.Tuple3[T0, T1, T2]) => (t._1(), t._2(), t._3()),
      (t: (T0, T1, T2)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3)
    )
  }

  private[foundationssc] def tupleToScala4[T0, T1, T2, T3]: Bijection[dev.typr.foundations.Tuple.Tuple4[T0, T1, T2, T3], (T0, T1, T2, T3)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple4[T0, T1, T2, T3], (T0, T1, T2, T3)](
      (t: dev.typr.foundations.Tuple.Tuple4[T0, T1, T2, T3]) => (t._1(), t._2(), t._3(), t._4()),
      (t: (T0, T1, T2, T3)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4)
    )
  }

  private[foundationssc] def tupleToScala5[T0, T1, T2, T3, T4]: Bijection[dev.typr.foundations.Tuple.Tuple5[T0, T1, T2, T3, T4], (T0, T1, T2, T3, T4)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple5[T0, T1, T2, T3, T4], (T0, T1, T2, T3, T4)](
      (t: dev.typr.foundations.Tuple.Tuple5[T0, T1, T2, T3, T4]) => (t._1(), t._2(), t._3(), t._4(), t._5()),
      (t: (T0, T1, T2, T3, T4)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5)
    )
  }

  private[foundationssc] def tupleToScala6[T0, T1, T2, T3, T4, T5]
      : Bijection[dev.typr.foundations.Tuple.Tuple6[T0, T1, T2, T3, T4, T5], (T0, T1, T2, T3, T4, T5)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple6[T0, T1, T2, T3, T4, T5], (T0, T1, T2, T3, T4, T5)](
      (t: dev.typr.foundations.Tuple.Tuple6[T0, T1, T2, T3, T4, T5]) => (t._1(), t._2(), t._3(), t._4(), t._5(), t._6()),
      (t: (T0, T1, T2, T3, T4, T5)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6)
    )
  }

  private[foundationssc] def tupleToScala7[T0, T1, T2, T3, T4, T5, T6]
      : Bijection[dev.typr.foundations.Tuple.Tuple7[T0, T1, T2, T3, T4, T5, T6], (T0, T1, T2, T3, T4, T5, T6)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple7[T0, T1, T2, T3, T4, T5, T6], (T0, T1, T2, T3, T4, T5, T6)](
      (t: dev.typr.foundations.Tuple.Tuple7[T0, T1, T2, T3, T4, T5, T6]) => (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7()),
      (t: (T0, T1, T2, T3, T4, T5, T6)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7)
    )
  }

  private[foundationssc] def tupleToScala8[T0, T1, T2, T3, T4, T5, T6, T7]
      : Bijection[dev.typr.foundations.Tuple.Tuple8[T0, T1, T2, T3, T4, T5, T6, T7], (T0, T1, T2, T3, T4, T5, T6, T7)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple8[T0, T1, T2, T3, T4, T5, T6, T7], (T0, T1, T2, T3, T4, T5, T6, T7)](
      (t: dev.typr.foundations.Tuple.Tuple8[T0, T1, T2, T3, T4, T5, T6, T7]) => (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
    )
  }

  private[foundationssc] def tupleToScala9[T0, T1, T2, T3, T4, T5, T6, T7, T8]
      : Bijection[dev.typr.foundations.Tuple.Tuple9[T0, T1, T2, T3, T4, T5, T6, T7, T8], (T0, T1, T2, T3, T4, T5, T6, T7, T8)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple9[T0, T1, T2, T3, T4, T5, T6, T7, T8], (T0, T1, T2, T3, T4, T5, T6, T7, T8)](
      (t: dev.typr.foundations.Tuple.Tuple9[T0, T1, T2, T3, T4, T5, T6, T7, T8]) => (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
    )
  }

  private[foundationssc] def tupleToScala10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9]
      : Bijection[dev.typr.foundations.Tuple.Tuple10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)](
      (t: dev.typr.foundations.Tuple.Tuple10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
    )
  }

  private[foundationssc] def tupleToScala11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
      : Bijection[dev.typr.foundations.Tuple.Tuple11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)](
      (t: dev.typr.foundations.Tuple.Tuple11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)) => dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11)
    )
  }

  private[foundationssc] def tupleToScala12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
      : Bijection[dev.typr.foundations.Tuple.Tuple12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] = {
    Bijection.of[dev.typr.foundations.Tuple.Tuple12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)](
      (t: dev.typr.foundations.Tuple.Tuple12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12)
    )
  }

  private[foundationssc] def tupleToScala13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]: Bijection[
    dev.typr.foundations.Tuple.Tuple13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)
  ] = {
    Bijection
      .of[dev.typr.foundations.Tuple.Tuple13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12], (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)](
        (t: dev.typr.foundations.Tuple.Tuple13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]) =>
          (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12(), t._13()),
        (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)) =>
          dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13)
      )
  }

  private[foundationssc] def tupleToScala14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]: Bijection[
    dev.typr.foundations.Tuple.Tuple14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)
    ](
      (t: dev.typr.foundations.Tuple.Tuple14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12(), t._13(), t._14()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14)
    )
  }

  private[foundationssc] def tupleToScala15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]: Bijection[
    dev.typr.foundations.Tuple.Tuple15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)
    ](
      (t: dev.typr.foundations.Tuple.Tuple15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12(), t._13(), t._14(), t._15()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
    )
  }

  private[foundationssc] def tupleToScala16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]: Bijection[
    dev.typr.foundations.Tuple.Tuple16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)
    ](
      (t: dev.typr.foundations.Tuple.Tuple16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12(), t._13(), t._14(), t._15(), t._16()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16)
    )
  }

  private[foundationssc] def tupleToScala17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]: Bijection[
    dev.typr.foundations.Tuple.Tuple17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)
    ](
      (t: dev.typr.foundations.Tuple.Tuple17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]) =>
        (t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9(), t._10(), t._11(), t._12(), t._13(), t._14(), t._15(), t._16(), t._17()),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17)
    )
  }

  private[foundationssc] def tupleToScala18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]: Bijection[
    dev.typr.foundations.Tuple.Tuple18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)
    ](
      (t: dev.typr.foundations.Tuple.Tuple18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]) =>
        (
          t._1(),
          t._2(),
          t._3(),
          t._4(),
          t._5(),
          t._6(),
          t._7(),
          t._8(),
          t._9(),
          t._10(),
          t._11(),
          t._12(),
          t._13(),
          t._14(),
          t._15(),
          t._16(),
          t._17(),
          t._18()
        ),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)) =>
        dev.typr.foundations.Tuple.of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18)
    )
  }

  private[foundationssc] def tupleToScala19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]: Bijection[
    dev.typr.foundations.Tuple.Tuple19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)
    ](
      (t: dev.typr.foundations.Tuple.Tuple19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]) =>
        (
          t._1(),
          t._2(),
          t._3(),
          t._4(),
          t._5(),
          t._6(),
          t._7(),
          t._8(),
          t._9(),
          t._10(),
          t._11(),
          t._12(),
          t._13(),
          t._14(),
          t._15(),
          t._16(),
          t._17(),
          t._18(),
          t._19()
        ),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)) =>
        dev.typr.foundations.Tuple
          .of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)
    )
  }

  private[foundationssc] def tupleToScala20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]: Bijection[
    dev.typr.foundations.Tuple.Tuple20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)
    ](
      (t: dev.typr.foundations.Tuple.Tuple20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]) =>
        (
          t._1(),
          t._2(),
          t._3(),
          t._4(),
          t._5(),
          t._6(),
          t._7(),
          t._8(),
          t._9(),
          t._10(),
          t._11(),
          t._12(),
          t._13(),
          t._14(),
          t._15(),
          t._16(),
          t._17(),
          t._18(),
          t._19(),
          t._20()
        ),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)) =>
        dev.typr.foundations.Tuple
          .of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20)
    )
  }

  private[foundationssc] def tupleToScala21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]: Bijection[
    dev.typr.foundations.Tuple.Tuple21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)
    ](
      (t: dev.typr.foundations.Tuple.Tuple21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]) =>
        (
          t._1(),
          t._2(),
          t._3(),
          t._4(),
          t._5(),
          t._6(),
          t._7(),
          t._8(),
          t._9(),
          t._10(),
          t._11(),
          t._12(),
          t._13(),
          t._14(),
          t._15(),
          t._16(),
          t._17(),
          t._18(),
          t._19(),
          t._20(),
          t._21()
        ),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)) =>
        dev.typr.foundations.Tuple
          .of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21)
    )
  }

  private[foundationssc] def tupleToScala22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]: Bijection[
    dev.typr.foundations.Tuple.Tuple22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21],
    (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)
  ] = {
    Bijection.of[
      dev.typr.foundations.Tuple.Tuple22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21],
      (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)
    ](
      (t: dev.typr.foundations.Tuple.Tuple22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]) =>
        (
          t._1(),
          t._2(),
          t._3(),
          t._4(),
          t._5(),
          t._6(),
          t._7(),
          t._8(),
          t._9(),
          t._10(),
          t._11(),
          t._12(),
          t._13(),
          t._14(),
          t._15(),
          t._16(),
          t._17(),
          t._18(),
          t._19(),
          t._20(),
          t._21(),
          t._22()
        ),
      (t: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)) =>
        dev.typr.foundations.Tuple
          .of(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21, t._22)
    )
  }

  def identity[T]: Bijection[T, T] = Bijection.identity[T]()
}
