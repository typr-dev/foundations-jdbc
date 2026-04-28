package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.util.List;

public record CheckReport(List<QueryAnalysis> analyses, long elapsedMs) {

  public CheckReport(List<QueryAnalysis> analyses) {
    this(analyses, 0);
  }

  public boolean allSucceeded() {
    for (var a : analyses) {
      if (!a.succeeded()) return false;
    }
    return true;
  }

  public void assertAllSucceeded() {
    if (allSucceeded()) return;
    List<QueryAnalysis> failed = analyses.stream().filter(a -> !a.succeeded()).toList();
    throw new QueryChecker.QueryCheckFailedException(failed, summary(false));
  }

  public String summary(boolean colored) {
    var b = Str.builder();

    // Group analyses by query name (variants share a name)
    long ddlGroups = 0;
    long failedGroups = 0;
    long totalGroups = 0;
    var failedReports = Str.builder();
    var successLines = Str.builder();
    int i = 0;
    while (i < analyses.size()) {
      QueryAnalysis head = analyses.get(i);
      int groupSize = Math.max(1, head.variantCount());
      int end = Math.min(analyses.size(), i + groupSize);
      List<QueryAnalysis> group = analyses.subList(i, end);
      boolean groupSucceeded = group.stream().allMatch(QueryAnalysis::succeeded);
      boolean groupIsDdl = group.stream().allMatch(CheckReport::isDdl);
      totalGroups++;

      if (!groupSucceeded) {
        failedGroups++;
        for (QueryAnalysis a : group) {
          if (!a.succeeded()) {
            failedReports.add(a.styledReportBody());
            failedReports.newline();
          }
        }
      } else if (groupIsDdl) {
        ddlGroups++;
      } else {
        successLines.green("  ✓ ").plain(head.displayName());
        if (group.size() > 1) {
          successLines.gray(" (" + group.size() + " variants)");
        }
        successLines.newline();
      }
      i = end;
    }

    // 1. Header
    b.newline();
    b.cyan("╔══════════════════════════════════════════════════════════════════════════════╗").newline();
    b.cyan("║").bold("  Query Analysis Report                                                       ").cyan("║").newline();
    b.cyan("╚══════════════════════════════════════════════════════════════════════════════╝").newline();
    b.newline();

    // 2. Failed queries: count + full reports
    if (failedGroups > 0) {
      b.boldRed(failedGroups + " failed:").newline().newline();
      b.add(failedReports.build());
    }

    // 3. Successful queries as one-liners
    b.add(successLines.build());

    // 4. Summary one-liner with timing
    long checked = totalGroups - ddlGroups;
    b.newline();
    if (failedGroups == 0) {
      b.boldGreen(checked + " queries passed");
    } else {
      b.plain(failedGroups + " failed, " + (checked - failedGroups) + " passed");
    }
    if (ddlGroups > 0) {
      b.gray(" (" + ddlGroups + " DDL skipped)");
    }
    if (elapsedMs > 0) {
      b.gray(" (" + elapsedMs + "ms)");
    }
    b.newline();

    Str str = b.build();
    return colored ? str.render() : str.plainText();
  }

  /**
   * A statement with no declared parameters and no declared columns is DDL (CREATE TABLE, DROP,
   * ALTER). The analyzer trivially passes these since there's nothing to align — but they dilute
   * the "N queries passed" signal, so we tag them out of the headline count.
   */
  private static boolean isDdl(QueryAnalysis a) {
    return a.parameterAlignment().isEmpty() && a.columnAlignment().isEmpty();
  }

  @Override
  public String toString() {
    return summary(false);
  }
}
