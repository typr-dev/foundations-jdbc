package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NamedJsonObject:
  //start
  case class OrderLine(product: String, qty: Int, price: BigDecimal)

  val lineParser: RowParserNamed[OrderLine] = RowParser.namedBuilder[OrderLine]()
    .field("product", DuckDbTypes.varchar)(_.product)
    .field("qty", DuckDbTypes.integer)(_.qty)
    .field("price", DuckDbTypes.decimal(10, 2))(_.price)
    .build(OrderLine.apply)

  // JSON array codec — positional: [["Widget", 3, 9.99], ...]
  val arrayCodec: DbJson[List[OrderLine]] =
    lineParser.jsonArray().asList

  // JSON object codec — named: [{"product": "Widget", "qty": 3, "price": 9.99}, ...]
  // Column names come from the parser — no redundant list to maintain
  val objectCodec: DbJson[List[OrderLine]] =
    lineParser.jsonObject().asList
  //stop
