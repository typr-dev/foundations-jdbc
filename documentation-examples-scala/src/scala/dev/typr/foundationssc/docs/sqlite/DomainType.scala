package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object DomainType:
  // start
  case class ProductId(value: Long)

  val productIdType: SqliteType[ProductId] =
    SqliteTypes.integer.transform(ProductId.apply, _.value)
  // stop
