package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.ParamDef;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.Procedure;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class QueryAnalysisRoutine {
  private final Transactor transactor = null; // placeholder

  // start
  // Verify a stored function matches the database definition
  void checkStoredFunction() {
    var addUser =
        Procedure.buildFunction(
            "add_user",
            List.of(ParamDef.input(PgTypes.text), ParamDef.input(PgTypes.text)),
            PgTypes.int4);

    QueryChecker checker = QueryChecker.create(transactor);
    checker.checkRoutine(addUser);
  }
  // stop
}
