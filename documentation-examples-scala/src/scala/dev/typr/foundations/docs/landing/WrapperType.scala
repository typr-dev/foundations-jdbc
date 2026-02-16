package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object WrapperType:
  //start
  case class ProductId(value: Int)

  object ProductId:
    // MariaDB int -> wraps to your domain type
    val mariaType: MariaType[ProductId] =
      MariaTypes.int_.transform(ProductId.apply, _.value)
  //stop
