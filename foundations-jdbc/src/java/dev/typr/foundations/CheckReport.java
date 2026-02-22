package dev.typr.foundations;

import java.util.List;

public record CheckReport(List<QueryAnalysis> analyses) {

  public boolean allSucceeded() {
    for (var a : analyses) {
      if (!a.succeeded()) return false;
    }
    return true;
  }

  public void assertAllSucceeded() {
    StringBuilder errors = new StringBuilder();
    int errorCount = 0;
    int idx = 0;
    for (var analysis : analyses) {
      idx++;
      if (!analysis.succeeded()) {
        errorCount++;
        errors.append("\n\n--- Query ").append(idx).append(" ---\n");
        errors.append(analysis.report());
      }
    }
    if (errorCount > 0) {
      throw new AssertionError(errorCount + " queries failed type checking:" + errors);
    }
  }

  public Str styledSummary() {
    var b = Str.builder();
    for (var a : analyses) {
      if (a.succeeded()) {
        b.green("  ✓ ");
      } else {
        b.red("  ✗ ");
      }
      b.plain(displayName(a)).newline();
    }
    long failed = analyses.stream().filter(a -> !a.succeeded()).count();
    b.newline();
    if (failed == 0) {
      b.boldGreen("All " + analyses.size() + " queries passed.");
    } else {
      b.boldRed(failed + " of " + analyses.size() + " queries failed.");
    }
    b.newline();
    return b.build();
  }

  public String summary() {
    return styledSummary().plainText();
  }

  public String summaryColored() {
    return styledSummary().render();
  }

  @Override
  public String toString() {
    return summary();
  }

  private static String displayName(QueryAnalysis a) {
    if (a.queryName() != null) return a.queryName();
    String sql = a.sql().replace('\n', ' ').replaceAll("\\s+", " ").trim();
    return sql.length() <= 60 ? sql : sql.substring(0, 57) + "...";
  }
}
