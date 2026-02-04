package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class ProductId(value: Long)

  // Create DuckDbType from bigint
  val productIdType: DuckDbType[ProductId] = DuckDbTypes.bigint.bimap(ProductId.apply, _.value)
  //stop
