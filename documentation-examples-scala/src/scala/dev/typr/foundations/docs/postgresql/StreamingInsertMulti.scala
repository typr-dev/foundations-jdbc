package dev.typr.foundations.docs.postgresql

import dev.typr.scalafoundations.*

@SuppressWarnings(Array("unused"))
object StreamingInsertMulti:

  case class ProductRow(name: String, price: BigDecimal, quantity: Int)

  //start
  // Define a RowParser for your row type
  val productParser: dev.typr.foundations.RowParser[ProductRow] =
    dev.typr.foundations.RowParser.builder[ProductRow]()
      .field(PgTypes.text, (p: ProductRow) => p.name)
      .field(PgTypes.numeric, (p: ProductRow) => p.price)
      .field(PgTypes.int4, (p: ProductRow) => Integer.valueOf(p.quantity))
      .build(ProductRow.apply)

  // PgText.from() derives a text encoder from the RowParser
  val productText: PgText[ProductRow] = PgText.from(productParser)

  def insertProducts(products: List[ProductRow], tx: Transactor): Long =
    streamingInsert
      .of("COPY products(name, price, quantity) FROM STDIN", 1000, products.iterator, productText)
      .transact(tx)
  //stop
