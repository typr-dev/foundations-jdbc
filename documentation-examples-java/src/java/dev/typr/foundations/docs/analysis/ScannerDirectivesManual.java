package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ScannerDirectivesManual {
  private final Transactor transactor = null; // placeholder

  static class ReportRepo {
    record ReportFilter(String category, int limit) {}

    public OperationRead<List<String>> filteredReport(ReportFilter filter) {
      return Fragment.of("SELECT name FROM reports WHERE category = ")
          .value(PgTypes.text, filter.category())
          .queryAll(PgTypes.text);
    }

    public OperationRead<List<String>> allReports() {
      return Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text);
    }
  }

  // start
  void checkWithManualDirective() {
    var repo = new ReportRepo();

    var analyzables =
        AnalyzableScanner.scan(
            "com.myapp.reports",

            // Provide specific arguments for a method
            ScanDirective.manual(
                ReportRepo.class,
                "filteredReport",
                "defaults",
                repo.filteredReport(new ReportRepo.ReportFilter("all", 100))));

    QueryChecker checker = QueryChecker.create(transactor);
    checker.checkAll(analyzables);
  }
  // stop
}
