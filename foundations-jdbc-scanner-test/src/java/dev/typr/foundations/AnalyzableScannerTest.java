package dev.typr.foundations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyzableScannerTest {

    @Test
    void scanJavaSnippets() {
        var found = AnalyzableScanner.scanDetailed("dev.typr.foundations.docs");
        printResults("Java", found);
        assertTrue(found.size() >= 30, "Expected at least 30 Java analyzables, got " + found.size());
    }

    @Test
    void scanKotlinSnippets() {
        var found = AnalyzableScanner.scanDetailed("dev.typr.foundationskt.docs");
        printResults("Kotlin", found);
        assertTrue(found.size() >= 30, "Expected at least 30 Kotlin analyzables, got " + found.size());
    }

    @Test
    void scanScalaSnippets() {
        var found = AnalyzableScanner.scanDetailed("dev.typr.foundationssc.docs");
        printResults("Scala", found);
        assertTrue(found.size() >= 30, "Expected at least 30 Scala analyzables, got " + found.size());
    }

    private static void printResults(String language, java.util.List<AnalyzableScanner.Result> results) {
        System.out.println(language + ": " + results.size() + " analyzables");
        for (var r : results) {
            System.out.println("  " + r.className() + "." + r.fieldName());
            System.out.println("    " + AnalyzableScanner.describe(r.analyzable()).replace("\n", "\n    "));
        }
    }
}
