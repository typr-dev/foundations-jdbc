package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ScannerDirectives {
  private final Transactor transactor = null; // placeholder

  // A repo with methods the scanner can't auto-invoke
  static class ReportRepo {
    // Runnable can't be constructed — scanner will skip or fail
    public OperationRead<List<String>> generateReport(Runnable onProgress) {
      return Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text);
    }

    public OperationRead<List<String>> allReports() {
      return Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text);
    }
  }

  // start
  void checkWithDirectives() {
    var analyzables =
        AnalyzableScanner.scan(
            "com.myapp.reports",

            // Skip a method entirely — it won't be type-checked
            ScanDirective.skip(ReportRepo.class, "generateReport"));

    QueryChecker checker = QueryChecker.create(transactor);
    checker.checkAll(analyzables);
  }
  // stop
}
