package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object DomainType:
  // start
  // Wrapper type
  case class UserId(value: Long)

  // Create MariaType from bigint
  val userIdType: MariaType[UserId] = MariaTypes.bigint.transform(UserId.apply, _.value)
  // stop
