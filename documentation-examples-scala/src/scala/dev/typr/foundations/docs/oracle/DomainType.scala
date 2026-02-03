package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}

@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class EmployeeId(value: Long)

  // Create OracleType from NUMBER
  val empIdType: OracleType[EmployeeId] =
    OracleTypes.numberLong.bimap(EmployeeId.apply, _.value)
  //stop
