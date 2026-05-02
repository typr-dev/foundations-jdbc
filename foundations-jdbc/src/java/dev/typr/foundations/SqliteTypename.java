package dev.typr.foundations;

import java.util.Optional;

/**
 * SQLite type name. SQLite has only five storage classes (NULL, INTEGER, REAL, TEXT, BLOB) plus a
 * collection of "type affinity" hints derived from the declared text. There are no arrays,
 * structs, or other nested types — this typename hierarchy is therefore much smaller than the
 * PostgreSQL or DuckDB equivalents.
 *
 * @see <a href="https://www.sqlite.org/datatype3.html">SQLite Datatypes</a>
 */
public sealed interface SqliteTypename<A> extends DbTypename<A> {
  @Override
  String sqlType();

  SqliteTypename<A> renamed(String newName);

  /** Drop optional precision/length when renaming. Used by aliases like {@code TEXT}. */
  SqliteTypename<A> renamedDropPrecision(String newName);

  default SqliteTypename<Optional<A>> opt() {
    return new Opt<>(this);
  }

  @SuppressWarnings("unchecked")
  default <B> SqliteTypename<B> as() {
    return (SqliteTypename<B>) this;
  }

  /** Whether this typename represents a nullable SQL value. Only {@link Opt} is nullable. */
  default boolean isNullable() {
    return false;
  }

  /**
   * Base type with optional precision and scale. Examples: {@code INTEGER}, {@code TEXT}, {@code
   * VARCHAR(255)}, {@code DECIMAL(10, 2)}. Precision/scale are accepted by SQLite for
   * compatibility but not enforced (except in STRICT tables).
   */
  record Base<A>(String baseType, Optional<Integer> precision, Optional<Integer> scale)
      implements SqliteTypename<A> {
    public Base(String baseType) {
      this(baseType, Optional.empty(), Optional.empty());
    }

    @Override
    public String sqlType() {
      if (precision.isPresent() && scale.isPresent()) {
        return baseType + "(" + precision.get() + ", " + scale.get() + ")";
      } else if (precision.isPresent()) {
        return baseType + "(" + precision.get() + ")";
      }
      return baseType;
    }

    @Override
    public String toString() {
      return sqlType();
    }

    @Override
    public SqliteTypename<A> renamed(String newName) {
      return new Base<>(newName, precision, scale);
    }

    @Override
    public SqliteTypename<A> renamedDropPrecision(String newName) {
      return new Base<>(newName);
    }
  }

  /** Optional wrapper. The SQL type is the same as the underlying type. */
  record Opt<A>(SqliteTypename<A> of) implements SqliteTypename<Optional<A>> {
    @Override
    public boolean isNullable() {
      return true;
    }

    @Override
    public String sqlType() {
      return of.sqlType();
    }

    @Override
    public String toString() {
      return of.toString();
    }

    @Override
    public SqliteTypename<Optional<A>> renamed(String newName) {
      return new Opt<>(of.renamed(newName));
    }

    @Override
    public SqliteTypename<Optional<A>> renamedDropPrecision(String newName) {
      return new Opt<>(of.renamedDropPrecision(newName));
    }
  }

  static <T> SqliteTypename<T> of(String sqlType) {
    return new Base<>(sqlType);
  }

  static <T> SqliteTypename<T> of(String sqlType, int precision) {
    return new Base<>(sqlType, Optional.of(precision), Optional.empty());
  }

  static <T> SqliteTypename<T> of(String sqlType, int precision, int scale) {
    return new Base<>(sqlType, Optional.of(precision), Optional.of(scale));
  }
}
