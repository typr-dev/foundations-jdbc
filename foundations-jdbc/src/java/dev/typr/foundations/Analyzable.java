package dev.typr.foundations;

/**
 * Marker interface for types that can be analyzed by {@link QueryAnalyzer}.
 *
 * <p>Implemented by {@link Operation}, {@link Template}, and {@link Analyzable.Named}.
 */
public sealed interface Analyzable permits Operation, Template, Analyzable.Named {

  /**
   * Human-readable description of this analyzable. When the analyzable has a name (via {@link
   * Named} or {@link OperationRead#named}), non-verbose returns just the name; verbose appends the
   * underlying detailed rendering (rendered fragments, operation type, etc.). Unnamed analyzables
   * ignore {@code verbose} and always return the detailed rendering.
   */
  default String description(boolean verbose) {
    return toString();
  }

  /** Shorthand for {@code description(false)}. */
  default String description() {
    return description(false);
  }

  /**
   * Wraps an {@link Analyzable} with a default name. The name is used by {@link QueryAnalyzer} when
   * the inner analyzable doesn't already have an explicit name.
   */
  record Named(String name, Analyzable inner) implements Analyzable {
    @Override
    public String description(boolean verbose) {
      if (verbose) return name + "\n" + inner.description(false);
      return name;
    }

    @Override
    public String toString() {
      return description(false);
    }
  }
}
