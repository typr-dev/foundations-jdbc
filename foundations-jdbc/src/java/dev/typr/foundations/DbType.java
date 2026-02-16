package dev.typr.foundations;

import java.util.Optional;
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

  /** Get the text encoder for bulk loading (COPY). Only supported for PostgreSQL. */
  default java.util.Optional<DbText<A>> text() {
    return java.util.Optional.empty();
  }

  /**
   * Get the JSON codec for converting values to/from JSON format that the database can
   * produce/consume.
   */
  DbJson<A> json();

  /**
   * Get the callable read codec for reading OUT/INOUT parameter values from a CallableStatement.
   */
  Optional<DbOutParam<A>> outParam();

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
   * Vendor-specific type names this type recognizes (case-insensitive).
   * Used by query analysis to match against database metadata.
   *
   * <p>The default returns a singleton set of the typename's sqlType() in lowercase.
   * Concrete types override to combine typename().sqlType() with any aliases from analysisOptions().
   *
   * @return set of vendor type names (lowercase)
   */
  default Set<String> vendorTypeNames() {
    return Set.of(typename().sqlType().toLowerCase());
  }

  /**
   * Analysis options controlling type checking behavior.
   *
   * @return analysis options for this type
   */
  AnalysisOptions analysisOptions();

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
