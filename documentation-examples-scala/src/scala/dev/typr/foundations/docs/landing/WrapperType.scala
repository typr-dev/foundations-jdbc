package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object WrapperType:
  //start
  case class ProductId(value: Int)

  object ProductId:
    // MariaDB int -> wraps to your domain type
    val mariaType: MariaType[ProductId] =
      MariaTypes.int_.bimap(ProductId.apply, _.value)
  //stop
