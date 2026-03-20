package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;

/**
 * A styled string that can render with or without ANSI colors. Inspired by Li Haoyi's fansi
 * library.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * Str message = Str.plain("Column ")
 *     .add(Str.yellow("3"))
 *     .add(Str.plain(" '"))
 *     .add(Str.cyan("name"))
 *     .add(Str.plain("'"));
 *
 * String colored = message.render();      // With ANSI codes
 * String plain = message.plainText();     // Without ANSI codes
 * }</pre>
 */
public final class Str {
  private final List<Segment> segments;

  private Str(List<Segment> segments) {
    this.segments = segments;
  }

  // ==================== Factory Methods ====================

  public static Str empty() {
    return new Str(List.of());
  }

  public static Str plain(String text) {
    return new Str(List.of(new Segment(text, Style.NONE)));
  }

  public static Str styled(String text, Style style) {
    return new Str(List.of(new Segment(text, style)));
  }

  // Color shortcuts
  public static Str red(String text) {
    return styled(text, Style.RED);
  }

  public static Str green(String text) {
    return styled(text, Style.GREEN);
  }

  public static Str yellow(String text) {
    return styled(text, Style.YELLOW);
  }

  public static Str blue(String text) {
    return styled(text, Style.BLUE);
  }

  public static Str cyan(String text) {
    return styled(text, Style.CYAN);
  }

  public static Str gray(String text) {
    return styled(text, Style.GRAY);
  }

  public static Str bold(String text) {
    return styled(text, Style.BOLD);
  }

  public static Str boldRed(String text) {
    return styled(text, Style.BOLD_RED);
  }

  public static Str boldGreen(String text) {
    return styled(text, Style.BOLD_GREEN);
  }

  // ==================== Concatenation ====================

  public Str add(Str other) {
    var combined = new ArrayList<>(this.segments);
    combined.addAll(other.segments);
    return new Str(combined);
  }

  public Str add(String plainText) {
    return add(plain(plainText));
  }

  // ==================== Rendering ====================

  /** Render with ANSI color codes */
  public String render() {
    var sb = new StringBuilder();
    for (var seg : segments) {
      if (seg.style != Style.NONE) {
        sb.append(seg.style.ansiCode);
      }
      sb.append(seg.text);
      if (seg.style != Style.NONE) {
        sb.append(Style.RESET.ansiCode);
      }
    }
    return sb.toString();
  }

  /** Render as plain text without ANSI codes */
  public String plainText() {
    var sb = new StringBuilder();
    for (var seg : segments) {
      sb.append(seg.text);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return plainText();
  }

  // ==================== Style Enum ====================

  public enum Style {
    NONE(""),
    RESET("\u001b[0m"),

    // Colors
    RED("\u001b[31m"),
    GREEN("\u001b[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    MAGENTA("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m"),
    GRAY("\u001b[90m"),

    // Bold
    BOLD("\u001b[1m"),
    BOLD_RED("\u001b[1;31m"),
    BOLD_GREEN("\u001b[1;32m"),
    BOLD_YELLOW("\u001b[1;33m"),
    BOLD_BLUE("\u001b[1;34m"),
    BOLD_CYAN("\u001b[1;36m");

    final String ansiCode;

    Style(String ansiCode) {
      this.ansiCode = ansiCode;
    }
  }

  // ==================== Segment ====================

  private record Segment(String text, Style style) {}

  // ==================== Builder for complex messages ====================

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final List<Segment> segments = new ArrayList<>();

    public Builder plain(String text) {
      segments.add(new Segment(text, Style.NONE));
      return this;
    }

    public Builder styled(String text, Style style) {
      segments.add(new Segment(text, style));
      return this;
    }

    public Builder red(String text) {
      return styled(text, Style.RED);
    }

    public Builder green(String text) {
      return styled(text, Style.GREEN);
    }

    public Builder yellow(String text) {
      return styled(text, Style.YELLOW);
    }

    public Builder blue(String text) {
      return styled(text, Style.BLUE);
    }

    public Builder cyan(String text) {
      return styled(text, Style.CYAN);
    }

    public Builder gray(String text) {
      return styled(text, Style.GRAY);
    }

    public Builder bold(String text) {
      return styled(text, Style.BOLD);
    }

    public Builder boldRed(String text) {
      return styled(text, Style.BOLD_RED);
    }

    public Builder boldGreen(String text) {
      return styled(text, Style.BOLD_GREEN);
    }

    public Builder newline() {
      segments.add(new Segment("\n", Style.NONE));
      return this;
    }

    public Builder add(Str str) {
      segments.addAll(str.segments);
      return this;
    }

    public Str build() {
      return new Str(new ArrayList<>(segments));
    }
  }
}
