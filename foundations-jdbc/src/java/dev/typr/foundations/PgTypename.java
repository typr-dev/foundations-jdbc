package dev.typr.foundations;

import java.util.List;
import java.util.Optional;

public sealed interface PgTypename<A> extends DbTypename<A> {
  String sqlType();

  String sqlTypeNoPrecision();

  PgTypename<List<A>> array();

  PgTypename<A> renamed(String value);

  PgTypename<A> renamedDropPrecision(String value);

  @Override
  default String renderPlaceholder() {
    String sqlType = sqlType();
    if (sqlType != null && !sqlType.isEmpty()) {
      return "?::" + sqlType;
    }
    return "?";
  }

  default PgTypename<Optional<A>> opt() {
    return new Opt<>(this);
  }

  default <B> PgTypename<B> as() {
    return (PgTypename<B>) this;
  }

  /**
   * Type-safe conversion using a bijection as proof of type relationship. Overrides DbTypename.to()
   * to return PgTypename for better type refinement.
   */
  @Override
  default <B> PgTypename<B> to(Bijection<A, B> bijection) {
    return (PgTypename<B>) this;
  }

  record Base<A>(String sqlType) implements PgTypename<A> {
    @Override
    public String sqlTypeNoPrecision() {
      return sqlType;
    }

    @Override
    public PgTypename<List<A>> array() {
      return new ArrayOf<>(this);
    }

    @Override
    public Base<A> renamed(String value) {
      return new Base<>(value);
    }

    @Override
    public Base<A> renamedDropPrecision(String value) {
      return new Base<>(value);
    }
  }

  record ArrayOf<A>(PgTypename<A> of) implements PgTypename<List<A>> {
    @Override
    public String sqlType() {
      return of.sqlType() + "[]";
    }

    @Override
    public String sqlTypeNoPrecision() {
      return of.sqlTypeNoPrecision() + "[]";
    }

    @Override
    public PgTypename<List<List<A>>> array() {
      return new ArrayOf<>(this);
    }

    @Override
    public PgTypename<List<A>> renamed(String value) {
      return new ArrayOf<>(of.renamed(value));
    }

    @Override
    public PgTypename<List<A>> renamedDropPrecision(String value) {
      return new ArrayOf<>(of.renamedDropPrecision(value));
    }
  }

  record WithPrec<A>(Base<A> of, int precision) implements PgTypename<A> {
    public String sqlType() {
      return of.sqlType + "(" + precision + ")";
    }

    @Override
    public String sqlTypeNoPrecision() {
      return of.sqlTypeNoPrecision();
    }

    @Override
    public PgTypename<List<A>> array() {
      // drops precision
      return new ArrayOf<>(this);
    }

    @Override
    public PgTypename<A> renamed(String value) {
      return new WithPrec<>(of.renamed(value), precision);
    }

    @Override
    public PgTypename<A> renamedDropPrecision(String value) {
      return of.renamed(value);
    }
  }

  record Opt<A>(PgTypename<A> of) implements PgTypename<Optional<A>> {
    @Override
    public String sqlType() {
      return of.sqlType();
    }

    @Override
    public String sqlTypeNoPrecision() {
      return of.sqlTypeNoPrecision();
    }

    @Override
    public PgTypename<List<Optional<A>>> array() {
      return new ArrayOf<>(this);
    }

    @Override
    public PgTypename<Optional<A>> renamed(String value) {
      return new Opt<>(of.renamed(value));
    }

    @Override
    public PgTypename<Optional<A>> renamedDropPrecision(String value) {
      return new Opt<>(of.renamedDropPrecision(value));
    }
  }

  static <T> PgTypename<T> of(String sqlType) {
    return new Base<>(sqlType);
  }

  static <T> PgTypename<T> of(String sqlType, int precision) {
    return new WithPrec<>(new Base<>(sqlType), precision);
  }

  /**
   * A composite type (record) typename with field information.
   *
   * @param <A> the Java type representing this composite
   */
  record CompositeOf<A>(String name, java.util.List<CompositeField> fields)
      implements PgTypename<A> {
    public record CompositeField(String name, PgTypename<?> type) {}

    @Override
    public String sqlType() {
      return name;
    }

    @Override
    public String sqlTypeNoPrecision() {
      return name;
    }

    @Override
    public PgTypename<List<A>> array() {
      return new ArrayOf<>(this);
    }

    @Override
    public CompositeOf<A> renamed(String value) {
      return new CompositeOf<>(value, fields);
    }

    @Override
    public CompositeOf<A> renamedDropPrecision(String value) {
      return new CompositeOf<>(value, fields);
    }

    /** Convert to generic PgTypename for use in PgType. */
    @SuppressWarnings("unchecked")
    public PgTypename<A> asGeneric() {
      return (PgTypename<A>) this;
    }
  }
}
