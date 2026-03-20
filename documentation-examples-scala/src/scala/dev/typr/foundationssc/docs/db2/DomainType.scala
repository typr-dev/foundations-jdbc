package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object DomainType:
  // start
  // Wrapper type
  case class ProductId(value: Long)

  // Create Db2Type from bigint
  val productIdType: Db2Type[ProductId] = Db2Types.bigint.transform(ProductId.apply, _.value)
  // stop
