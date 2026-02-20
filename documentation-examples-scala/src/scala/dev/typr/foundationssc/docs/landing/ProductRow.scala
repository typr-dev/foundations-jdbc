package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.OffsetDateTime

@SuppressWarnings(Array("unused"))
object ProductRow:
  case class ProductId(value: Int)
  case class Dimensions(width: Double, height: Double, depth: Double, unit: String)

  //start
  case class Product(
    id: ProductId,
    name: String,
    price: BigDecimal,
    tags: Option[Array[String]],            // text[]
    dimensions: Option[Dimensions],         // composite type
    metadata: Option[Jsonb],                // jsonb
    createdAt: Option[OffsetDateTime]       // timestamptz
  )
  //stop
