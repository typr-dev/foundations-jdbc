package dev.typr.foundations.docs.postgresql

import dev.typr.scalafoundations.*

@SuppressWarnings(Array("unused"))
object StreamingInsert:

  //start:single-column
  // Insert a list of strings using COPY
  def insertNames(names: java.util.List[String], tx: Transactor): Long =
    streamingInsert
      .of("COPY users(name) FROM STDIN", 1000, names.iterator(), PgTypes.text.pgText())
      .transact(tx)
  //stop:single-column

  case class ProductRow(name: String, price: BigDecimal, quantity: Int)

  //start:multi-column
  // Define a RowParser for your row type
  val productParser: dev.typr.foundations.RowParser[ProductRow] =
    dev.typr.foundations.RowParser.builder[ProductRow]()
      .field(PgTypes.text, (p: ProductRow) => p.name)
      .field(PgTypes.numeric, (p: ProductRow) => p.price)
      .field(PgTypes.int4, (p: ProductRow) => Integer.valueOf(p.quantity))
      .build(ProductRow.apply)

  // PgText.from() derives a text encoder from the RowParser
  val productText: PgText[ProductRow] = PgText.from(productParser)

  def insertProducts(products: java.util.List[ProductRow], tx: Transactor): Long =
    streamingInsert
      .of("COPY products(name, price, quantity) FROM STDIN", 1000, products.iterator(), productText)
      .transact(tx)
  //stop:multi-column
