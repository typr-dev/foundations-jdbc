package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ComposingParsers:
  case class ProductRow(id: Int, name: String)
  case class CategoryRow(id: Int, categoryName: String)

  val productParser: RowParser[ProductRow] =
    RowParser.builder[ProductRow]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .build(ProductRow.apply)

  val categoryParser: RowParser[CategoryRow] =
    RowParser.builder[CategoryRow]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.categoryName)
      .build(CategoryRow.apply)

  //start
  // Inner join — both sides always present
  val innerJoined: RowParser[(ProductRow, CategoryRow)] =
    productParser.joined(categoryParser)

  // Left join — right side is Option
  val leftJoined: RowParser[(ProductRow, Option[CategoryRow])] =
    productParser.leftJoined(categoryParser)
  //stop
