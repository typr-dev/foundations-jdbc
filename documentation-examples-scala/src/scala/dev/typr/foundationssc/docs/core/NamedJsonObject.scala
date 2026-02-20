package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object NamedJsonObject:
  //start
  case class OrderLine(
    product: String, qty: Int, price: BigDecimal
  )

  val lineCodec: RowCodecNamed[OrderLine] =
    RowCodec.namedBuilder[OrderLine]()
      .field("product", DuckDbTypes.varchar)(_.product)
      .field("qty", DuckDbTypes.integer)(_.qty)
      .field("price", DuckDbTypes.decimal(10, 2))(_.price)
      .build(OrderLine.apply)

  // Stores rows as positional JSON arrays
  val arrayType: DuckDbType[List[OrderLine]] =
    DuckDbTypes.jsonArrayEncodedList(lineCodec)

  // Stores rows as named JSON objects — keys from the codec
  val objectType: DuckDbType[List[OrderLine]] =
    DuckDbTypes.jsonObjectEncodedList(lineCodec)
  //stop
