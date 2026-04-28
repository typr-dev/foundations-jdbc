package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisExample:
  private val transactor: Transactor = null // placeholder

  val productCodec: RowCodec[Product] = RowCodec
    .builder[Product]()
    .field(DuckDbTypes.integer)(_.id)
    .field(DuckDbTypes.integer)(_.name)
    .field(DuckDbTypes.double_)(_.price)
    .build(Product.apply)

  // start
  // name is VARCHAR in the database, but declared as Int here
  case class Product(id: Int, name: Int, price: Double)

  val listProductsBad: OperationRead.Query[List[Product]] =
    Fragment.of("SELECT id, name, price FROM products")
      .query(productCodec.all())

  def check(): Unit =
    val checker: QueryChecker = QueryChecker.create(transactor)
    checker.check(listProductsBad)
  // stop
