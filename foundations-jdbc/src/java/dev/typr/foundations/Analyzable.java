package dev.typr.foundations;

/**
 * Marker interface for types that can be analyzed by {@link QueryAnalyzer}.
 *
 * <p>Implemented by {@link Operation}, {@link Template}, and {@link RowTemplate}.
 */
public sealed interface Analyzable permits Operation, Template, Analyzable.Named {

  /**
   * Wraps an {@link Analyzable} with a default name. The name is used by
   * {@link QueryAnalyzer} when the inner analyzable doesn't already have an explicit name.
   */
  record Named(String name, Analyzable inner) implements Analyzable {}
}
