package dev.typr.foundations;

import dev.typr.foundations.dsl.Bijection;
import java.util.Set;

/**
 * Common interface for database type codecs. Implemented by both PgType (PostgreSQL) and MariaType
 * (MariaDB).
 */
public interface DbType<A> {
  /** Get the typename for SQL rendering (e.g., for casts like ?::typename). */
  DbTypename<A> typename();

  /** Get the read codec for reading ResultSet columns. */
  DbRead<A> read();

  /** Get the write codec for setting PreparedStatement parameters. */
  DbWrite<A> write();

  /** Get the text encoder for bulk loading (COPY/LOAD DATA). */
  DbText<A> text();

  /**
   * Get the JSON codec for converting values to/from JSON format that the database can
   * produce/consume.
   */
  DbJson<A> json();

  /** Create an optional version of this type. */
  DbType<java.util.Optional<A>> opt();

  /**
   * Convert this DbType to handle a different type using a bijection. The bijection converts values
   * bidirectionally while preserving the underlying database type semantics.
   *
   * @param bijection The bijection to convert between A and B
   * @param <B> The target type
   * @return A DbType that handles type B by converting to/from type A
   */
  <B> DbType<B> to(Bijection<A, B> bijection);

  // ─────────────────────────────────────────────────────────────────────────────
  // Query Analysis Support (for type checking at test time)
  // ─────────────────────────────────────────────────────────────────────────────

  /**
   * JDBC type codes (java.sql.Types constants) this type can write to when used as a parameter.
   * Used by query analysis to verify parameter type compatibility.
   *
   * <p>The default implementation looks up the JDBC types from a registry based on
   * {@link #typename()}.sqlType(). Override to customize.
   *
   * @return set of java.sql.Types constants this type can write to
   */
  default Set<Integer> jdbcTargets() {
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcTargets(typename().sqlType());
  }

  /**
   * JDBC type codes (java.sql.Types constants) this type can read from when used in a RowParser.
   * Used by query analysis to verify column type compatibility.
   *
   * <p>This is often broader than jdbcTargets() because types can often read from related types
   * (e.g., an int4 type might read from SMALLINT, INTEGER, or BIGINT).
   *
   * <p>The default implementation looks up the JDBC types from a registry based on
   * {@link #typename()}.sqlType(). Override to customize.
   *
   * @return set of java.sql.Types constants this type can read from
   */
  default Set<Integer> jdbcSources() {
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcSources(typename().sqlType());
  }

  /**
   * Vendor-specific type names this type recognizes (case-insensitive).
   * Used by query analysis to provide better error messages.
   *
   * <p>Examples: "int4", "integer", "serial" for PostgreSQL integer types.
   *
   * @return set of vendor type names (lowercase)
   */
  default Set<String> vendorTypeNames() {
    return dev.typr.foundations.analysis.JdbcTypeRegistry.lookup(typename().sqlType()).vendorTypeNames();
  }

  /**
   * Whether this type allows null values. Types created with .opt() return true.
   * Used by query analysis to detect nullability mismatches.
   *
   * @return true if this type handles null values (is Optional)
   */
  default boolean isNullable() {
    return false;
  }
}
