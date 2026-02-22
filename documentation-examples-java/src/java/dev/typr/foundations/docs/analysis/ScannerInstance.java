package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ScannerInstance {
    private final Transactor transactor = null; // placeholder

    // This class lives outside the scanned package
    static class ExternalRepo {
        public final Operation<List<String>> allItems =
            Fragment.of("SELECT name FROM items").queryAll(PgTypes.text);

        public Operation<List<String>> activeItems() {
            return Fragment.of("SELECT name FROM items WHERE active")
                .queryAll(PgTypes.text);
        }

        public Operation<List<String>> generateReport(Runnable callback) {
            return Fragment.of("SELECT name FROM items").queryAll(PgTypes.text);
        }
    }

    //start
    void checkWithExternalObjects() {
        var external = new ExternalRepo();

        var analyzables = AnalyzableScanner.scan(
            "com.myapp.db",

            // Add an object from outside the scanned package
            ScanDirective.instance(external),

            // Add with per-instance overrides
            ScanDirective.instance(external, (inst, cfg) -> {
                cfg.skip(ExternalRepo.class, "generateReport");
            })
        );

        QueryChecker checker = QueryChecker.create(transactor);
        checker.checkAll(analyzables);
    }
    //stop
}
