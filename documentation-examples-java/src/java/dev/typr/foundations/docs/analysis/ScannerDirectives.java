package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ScannerDirectives {
    private final Transactor transactor = null; // placeholder

    // A repo with methods the scanner can't auto-invoke
    static class ReportRepo {
        record ReportFilter(String category, int limit) {}

        // Runnable can't be constructed — scanner will skip or fail
        public Operation<List<String>> generateReport(Runnable onProgress) {
            return Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text);
        }

        // ReportFilter is constructible — scanner CAN auto-invoke this
        public Operation<List<String>> filteredReport(ReportFilter filter) {
            return Fragment.of("SELECT name FROM reports WHERE category = ")
                .value(PgTypes.text, filter.category())
                .queryAll(PgTypes.text);
        }

        public Operation<List<String>> allReports() {
            return Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text);
        }
    }

    //start
    void checkWithDirectives() {
        var repo = new ReportRepo();

        var analyzables = AnalyzableScanner.scan(
            "com.myapp.reports",

            // Skip a method entirely — it won't be type-checked
            ScanDirective.skip(ReportRepo.class, "generateReport"),

            // Provide specific arguments for a method
            ScanDirective.manual(
                ReportRepo.class, "filteredReport", "defaults",
                repo.filteredReport(new ReportRepo.ReportFilter("all", 100)))
        );

        QueryChecker checker = QueryChecker.create(transactor);
        checker.checkAll(analyzables);
    }
    //stop
}
