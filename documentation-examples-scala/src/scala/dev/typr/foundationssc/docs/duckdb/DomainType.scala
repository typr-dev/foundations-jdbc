package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object DomainType:
  // start
  // Wrapper type
  case class ProductId(value: Long)

  // Create DuckDbType from bigint
  val productIdType: DuckDbType[ProductId] = DuckDbTypes.bigint.transform(ProductId.apply, _.value)
  // stop
