package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ComposingCodecs:
  case class ProductRow(id: Int, name: String)
  case class CategoryRow(id: Int, categoryName: String)

  val productCodec: RowCodec[ProductRow] =
    RowCodec.builder[ProductRow]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .build(ProductRow.apply)

  val categoryCodec: RowCodec[CategoryRow] =
    RowCodec.builder[CategoryRow]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.categoryName)
      .build(CategoryRow.apply)

  //start
  // Inner join — both sides always present
  val innerJoined: RowCodec[(ProductRow, CategoryRow)] =
    productCodec.joined(categoryCodec)

  // Left join — right side is Option
  val leftJoined: RowCodec[(ProductRow, Option[CategoryRow])] =
    productCodec.leftJoined(categoryCodec)
  //stop
