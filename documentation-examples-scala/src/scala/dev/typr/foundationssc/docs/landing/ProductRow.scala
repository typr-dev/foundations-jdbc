package dev.typr.foundationssc.docs.landing

import java.time.Instant

@SuppressWarnings(Array("unused"))
object ProductRow:
  //start
  case class Product(
    id: Int,
    name: String,
    price: BigDecimal,
    createdAt: Option[Instant]
  )
  //stop
