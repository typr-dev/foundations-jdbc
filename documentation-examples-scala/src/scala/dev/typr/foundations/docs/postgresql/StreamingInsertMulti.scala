package dev.typr.foundations.docs.postgresql

import dev.typr.scalafoundations.*

@SuppressWarnings(Array("unused"))
object StreamingInsertMulti:

  case class ProductRow(name: String, price: BigDecimal, quantity: Int)

  //start
  // Define a RowParser for your row type
  val productParser: RowParser[ProductRow] =
    RowParser.builder[ProductRow]()
      .field(PgTypes.text)(_.name)
      .field(PgTypes.numeric)(_.price)
      .field(PgTypes.int4)(_.quantity)
      .build(ProductRow.apply)

  // PgText.from() derives a text encoder from the RowParser
  val productText: PgText[ProductRow] = PgText.from(productParser)

  def insertProducts(products: Iterator[ProductRow], tx: Transactor): Long =
    streamingInsert
      .of("COPY products(name, price, quantity) FROM STDIN", 1000, products, productText)
      .transact(tx)
  //stop
