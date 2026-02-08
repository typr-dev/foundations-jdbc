package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object ComposingParsers:
  //start
  case class ProductRow(id: Int, name: String)
  case class CategoryRow(id: Int, categoryName: String)

  val productRowParser: RowParser[ProductRow] = RowParser.builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(ProductRow.apply)

  val categoryRowParser: RowParser[CategoryRow] = RowParser.builder[CategoryRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.categoryName)
    .build(CategoryRow.apply)

  // leftJoined() returns Option right side automatically
  val joined: RowParser[And[ProductRow, Option[CategoryRow]]] =
    productRowParser.leftJoined(categoryRowParser)
  //stop
