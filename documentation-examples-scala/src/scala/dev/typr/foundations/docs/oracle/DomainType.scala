package dev.typr.foundations.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class EmployeeId(value: Long)

  // Create OracleType from NUMBER
  val empIdType: OracleType[EmployeeId] =
    OracleTypes.numberLong.transform(EmployeeId.apply, _.value)
  //stop
