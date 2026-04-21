package dev.typr.foundationssc.docs.analysis

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisRoutine:
  private val transactor: Transactor = null // placeholder

  // start
  // Verify a stored function matches the database
  def checkStoredFunction(): Unit =
    val addUser = Procedure.buildFunction(
      "add_user",
      java.util.List.of(
        ParamDef.input(PgTypes.text),
        ParamDef.input(PgTypes.text)
      ),
      PgTypes.int4
    )

    val checker: QueryChecker =
      QueryChecker.create(transactor)
    checker.checkRoutine(addUser)
  // stop
