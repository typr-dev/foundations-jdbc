package dev.typr.foundationssc.docs.analysis
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisAll:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  val insertUser: Template[String, Int] =
    sql"INSERT INTO users(name) VALUES("
      .param(PgTypes.text)
      .append(") RETURNING id")
      .query(RowCodec.of(PgTypes.int4).exactlyOne())

  val allUsers: OperationRead[List[User]] =
    sql"SELECT id, name FROM users"
      .query(userCodec.all())

  // start
  def analyzeComposedOperation(conn: Connection): Unit =
    // Build a composed operation
    val transaction: Operation[?] =
      insertUser.on("Alice").productL(allUsers)

    // Analyze every statement in the tree
    val results: List[QueryAnalysis] =
      QueryAnalyzer.analyze(transaction, conn)

    for analysis <- results do if !analysis.succeeded() then System.err.println(analysis.report())
  // stop
