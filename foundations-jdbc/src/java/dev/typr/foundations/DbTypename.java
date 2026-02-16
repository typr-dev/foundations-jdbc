package dev.typr.foundations;


/**
 * Common interface for database type names. Implemented by both PgTypename (PostgreSQL) and
 * MariaTypename (MariaDB).
 */
public interface DbTypename<A> {
  /** Get the SQL type string (e.g., "text", "int4", "varchar(255)"). */
  String sqlType();

  /**
   * Render the SQL placeholder for this type. PostgreSQL and DuckDB override to return
   * {@code ?::typename}, others return plain {@code ?}.
   */
  default String renderPlaceholder() {
    return "?";
  }

  /**
   * Type-safe conversion using a bijection as proof of type relationship. Since DbTypename is just
   * type metadata (SQL type string), the type parameter is phantom - no values of type A are ever
   * stored. The bijection proves that A and B are related types, providing compile-time type
   * safety.
   *
   * @param bijection proof that A and B are related types (not used at runtime)
   * @return this typename with type parameter B
   */
  default <B> DbTypename<B> to(Bijection<A, B> bijection) {
    return (DbTypename<B>) this;
  }
}
