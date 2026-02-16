package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NamedJsonObject:
  //start
  case class OrderLine(
    product: String, qty: Int, price: BigDecimal
  )

  val lineParser: RowParserNamed[OrderLine] =
    RowParser.namedBuilder[OrderLine]()
      .field("product", DuckDbTypes.varchar)(_.product)
      .field("qty", DuckDbTypes.integer)(_.qty)
      .field("price", DuckDbTypes.decimal(10, 2))(_.price)
      .build(OrderLine.apply)

  // JSON array codec — positional
  val arrayCodec: DbJson[List[OrderLine]] =
    lineParser.jsonArray().asList

  // JSON object codec — named keys from the parser
  val objectCodec: DbJson[List[OrderLine]] =
    lineParser.jsonObject().asList
  //stop
