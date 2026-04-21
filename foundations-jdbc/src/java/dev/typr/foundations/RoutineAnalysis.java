package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.util.List;
import java.util.Optional;

public record RoutineAnalysis(
    String routineName,
    RoutineKind kind,
    List<ParamCheck> paramChecks,
    Optional<ReturnCheck> returnCheck,
    boolean routineExists) {

  public enum RoutineKind {
    FUNCTION,
    PROCEDURE
  }

  public record ParamCheck(
      int position,
      String declaredType,
      String expectedType,
      String declaredMode,
      String expectedMode,
      boolean typeMatch,
      boolean modeMatch) {}

  public record ReturnCheck(String declaredType, String returnedType, boolean match) {}

  public boolean succeeded() {
    if (!routineExists) return false;
    for (var pc : paramChecks) {
      if (!pc.typeMatch || !pc.modeMatch) return false;
    }
    if (returnCheck.isPresent() && !returnCheck.get().match()) return false;
    return true;
  }

  public Str styledReport() {
    var b = Str.builder();
    b.newline();
    b.cyan("╔══════════════════════════════════════════════════════════════════════════════╗")
        .newline();
    b.cyan("║")
        .bold("  Routine Analysis Report                                                     ")
        .cyan("║")
        .newline();
    b.cyan("╚══════════════════════════════════════════════════════════════════════════════╝")
        .newline();
    b.newline();

    b.bold("Routine: ")
        .cyan(routineName)
        .plain(" (")
        .plain(kind.name().toLowerCase())
        .plain(")")
        .newline()
        .newline();

    if (!routineExists) {
      b.boldRed("✗ Routine not found: " + routineName).newline();
      return b.build();
    }

    // Parameters
    if (!paramChecks.isEmpty()) {
      b.bold("Parameters:").newline();
      for (var pc : paramChecks) {
        String status = (pc.typeMatch && pc.modeMatch) ? "✓" : "✗";
        var line = Str.builder();
        if (pc.typeMatch && pc.modeMatch) {
          line.green(status);
        } else {
          line.red(status);
        }
        line.plain(" [" + pc.position + "] ")
            .cyan(pc.declaredType)
            .gray(" (" + pc.declaredMode + ")")
            .gray(" → ")
            .plain(pc.expectedType)
            .gray(" (" + pc.expectedMode + ")");
        if (!pc.typeMatch) {
          line.red(" TYPE MISMATCH");
        }
        if (!pc.modeMatch) {
          line.red(" MODE MISMATCH");
        }
        b.plain("  ").add(line.build()).newline();
      }
      b.newline();
    }

    // Return type
    if (returnCheck.isPresent()) {
      var rc = returnCheck.get();
      b.bold("Return type:").newline();
      b.plain("  ");
      if (rc.match()) {
        b.green("✓");
      } else {
        b.red("✗");
      }
      b.plain(" ").cyan(rc.declaredType()).gray(" → ").plain(rc.returnedType());
      if (!rc.match()) {
        b.red(" MISMATCH");
      }
      b.newline().newline();
    }

    if (succeeded()) {
      b.boldGreen("✓ No errors found").newline();
    } else {
      b.boldRed("✗ Routine analysis failed").newline();
    }

    return b.build();
  }

  public String report() {
    return styledReport().plainText();
  }
}
