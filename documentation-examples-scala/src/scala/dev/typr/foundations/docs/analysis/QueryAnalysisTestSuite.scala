package dev.typr.foundations.docs.analysis
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*


import javax.sql.DataSource
import scala.jdk.CollectionConverters.*
import scala.util.Using

@SuppressWarnings(Array("unused"))
object QueryAnalysisTestSuite:
  case class User(id: Int, name: String, email: String)
  case class Product(id: Int, name: String)

  private val testDataSource: DataSource = null // placeholder

  private val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  private val productParser: RowParser[Product] = RowParser.builder[Product]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(Product.apply)

  //start
  def allQueriesTypeCheck(): Unit =
    Using.resource(testDataSource.getConnection) { conn =>
      val queries: List[Operation.Query[?]] = List(
        sql"""SELECT id, name, email
              FROM users
              WHERE id = ${PgTypes.int4(1)}"""
          .query(userParser.all()),
        sql"""SELECT id, name
              FROM products
              WHERE name LIKE ${PgTypes.text("%widget%")}"""
          .query(productParser.all())
      )

      val failures = queries.flatMap { query =>
        val analysis: QueryAnalysis =
          QueryAnalyzer.analyze(query, conn).head
        if !analysis.succeeded() then
          Some(analysis.report())
        else None
      }

      if failures.nonEmpty then
        throw AssertionError(
          s"Query type check failed:\n\n${failures.mkString("\n\n")}"
        )
    }
  //stop
