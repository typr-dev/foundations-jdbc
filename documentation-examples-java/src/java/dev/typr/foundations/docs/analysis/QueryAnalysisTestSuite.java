package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.AnalyzableScanner;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class QueryAnalysisTestSuite {
    private final Transactor transactor = null; // placeholder

    //start
    void allQueriesTypeCheck() {
        var analyzables = AnalyzableScanner.scan("com.myapp.db");
        QueryChecker checker = () -> transactor;
        checker.checkAll(analyzables);
    }
    //stop
}
