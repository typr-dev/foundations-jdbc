package dev.typr.foundations.docs.analysis
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisAll:
  case class User(id: Int, name: String)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  var conn: Connection = null // placeholder

  val insertUser: SqlTemplate[String, Int] =
    Fragment.of("INSERT INTO users(name) VALUES(")
      .param(PgTypes.text)
      .append(") RETURNING id")
      .query(RowParser.of(PgTypes.int4).exactlyOne())

  val allUsers: Operation[List[User]] =
    sql"SELECT id, name FROM users".query(userParser.all())

  //start
  def analyzeComposedOperation(): Unit =
    // Build a composed operation
    val transaction: Operation[?] =
      insertUser.on("Alice")
        .thenIgnore(allUsers)

    // Analyze every SQL statement in the tree - one call
    val results: List[QueryAnalysis] =
      QueryAnalyzer.analyze(transaction, conn)

    for analysis <- results do
      if !analysis.succeeded() then
        System.err.println(analysis.report())
  //stop
