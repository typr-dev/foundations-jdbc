package dev.typr.foundations.docs.analysis
import dev.typr.foundations.{ParamDef, PgTypes, Procedure}
import dev.typr.foundations.analysis.QueryChecker

@SuppressWarnings(Array("unused"))
object QueryAnalysisRoutine:
  private val transactor: dev.typr.foundations.Transactor = null // placeholder

  //start
  // Verify a stored function matches the database definition
  def checkStoredFunction(): Unit =
    val addUser = Procedure.buildFunction("add_user",
      java.util.List.of(ParamDef.in(PgTypes.text), ParamDef.in(PgTypes.text)),
      PgTypes.int4)

    val checker: QueryChecker = () => transactor
    checker.checkRoutine(addUser)
  //stop
