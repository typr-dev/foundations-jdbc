package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class EmployeeId(value: Long)

  // Create OracleType from NUMBER
  val empIdType: OracleType[EmployeeId] =
    OracleTypes.numberLong.bimap(EmployeeId.apply, _.value)
  //stop
