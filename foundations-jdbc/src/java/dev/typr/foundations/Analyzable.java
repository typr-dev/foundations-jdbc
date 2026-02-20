package dev.typr.foundations;

/**
 * Marker interface for types that can be analyzed by {@link QueryAnalyzer}.
 *
 * <p>Implemented by {@link Operation}, {@link Template}, and {@link RowTemplate}.
 */
public sealed interface Analyzable permits Operation, Template, RowTemplate {}
