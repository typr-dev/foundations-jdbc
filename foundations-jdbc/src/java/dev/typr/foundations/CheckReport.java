package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
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
      throw new QueryChecker.QueryCheckFailedException(
          failed, failed.size() + " queries failed type checking:" + errors);
    }
  }

  public String summary(boolean colored) {
    var b = Str.builder();

    for (QueryAnalysis a : analyses) {
      if (!a.succeeded()) {
        b.add(a.styledReport());
      }
    }

    long ddlGroups = 0;
    long failedGroups = 0;
    long totalGroups = 0;
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
        b.red("  ✗ ").plain(head.displayName());
      } else if (groupIsDdl) {
        ddlGroups++;
        b.gray("  · ").gray(head.displayName()).gray(" (DDL)");
      } else {
        b.green("  ✓ ").plain(head.displayName());
      }
      if (group.size() > 1) {
        b.gray(" (" + group.size() + " variants)");
      }
      b.newline();
      i = end;
    }
    long checked = totalGroups - ddlGroups;
    b.newline();
    if (failedGroups == 0) {
      if (ddlGroups == 0) {
        b.boldGreen("All " + totalGroups + " queries passed.");
      } else {
        b.boldGreen("All " + checked + " queries passed")
            .gray(" (" + ddlGroups + " DDL skipped)")
            .boldGreen(".");
      }
    } else {
      b.boldRed(failedGroups + " of " + checked + " queries failed.");
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
