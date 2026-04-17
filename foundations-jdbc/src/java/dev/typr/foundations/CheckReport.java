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
    List<QueryAnalysis> failed = new java.util.ArrayList<>();
    int idx = 0;
    for (var analysis : analyses) {
      idx++;
      if (!analysis.succeeded()) {
        failed.add(analysis);
        errors.append("\n\n--- Query ").append(idx).append(" ---\n");
        errors.append(analysis.report());
      }
    }
    if (!failed.isEmpty()) {
      throw new QueryCheckFailedException(
          failed, failed.size() + " queries failed type checking:" + errors);
    }
  }

  public Str styledSummary() {
    var b = Str.builder();
    long ddlCount = 0;
    for (var a : analyses) {
      boolean isDdl = isDdl(a);
      if (!a.succeeded()) {
        b.red("  ✗ ").plain(displayName(a));
      } else if (isDdl) {
        ddlCount++;
        b.gray("  · ").gray(displayName(a)).gray(" (DDL)");
      } else {
        b.green("  ✓ ").plain(displayName(a));
      }
      b.newline();
    }
    long failed = analyses.stream().filter(a -> !a.succeeded()).count();
    long checked = analyses.size() - ddlCount;
    b.newline();
    if (failed == 0) {
      if (ddlCount == 0) {
        b.boldGreen("All " + analyses.size() + " queries passed.");
      } else {
        b.boldGreen("All " + checked + " queries passed")
            .gray(" (" + ddlCount + " DDL skipped)")
            .boldGreen(".");
      }
    } else {
      b.boldRed(failed + " of " + checked + " queries failed.");
    }
    b.newline();
    return b.build();
  }

  /**
   * A statement with no declared parameters and no declared columns is DDL (CREATE TABLE, DROP,
   * ALTER). The analyzer trivially passes these since there's nothing to align — but they
   * dilute the "N queries passed" signal, so we tag them out of the headline count.
   */
  private static boolean isDdl(QueryAnalysis a) {
    return a.parameterAlignment().isEmpty() && a.columnAlignment().isEmpty();
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
