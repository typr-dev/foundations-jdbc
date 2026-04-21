package dev.typr.foundations;

import java.util.Optional;
import java.util.Set;

/**
 * Wraps a DbType to redact its values in logs and traces. Encode/decode behavior is identical to
 * the inner type. Only {@link Fragment#renderInterpolated()} is affected — values show as {@code
 * <redacted>}.
 */
final class RedactedDbType<A> implements DbType<A> {
  private final DbType<A> inner;

  RedactedDbType(DbType<A> inner) {
    this.inner = inner;
  }

  @Override
  public DbTypename<A> typename() {
    return inner.typename();
  }

  @Override
  public DbRead<A> read() {
    return inner.read();
  }

  @Override
  public DbWrite<A> write() {
    return inner.write();
  }

  @Override
  public Optional<DbText<A>> text() {
    return inner.text();
  }

  @Override
  public DbJson<A> json() {
    return inner.json();
  }

  @Override
  public Optional<DbOutParam<A>> outParam() {
    return inner.outParam();
  }

  @Override
  public DbType<Optional<A>> opt() {
    return inner.opt().redacted();
  }

  @Override
  public <B> DbType<B> to(Bijection<A, B> bijection) {
    return inner.to(bijection).redacted();
  }

  @Override
  public Set<String> vendorTypeNames() {
    return inner.vendorTypeNames();
  }

  @Override
  public AnalysisOptions analysisOptions() {
    return inner.analysisOptions();
  }

  @Override
  public boolean isNullable() {
    return inner.isNullable();
  }

  @Override
  public boolean isRedacted() {
    return true;
  }

  @Override
  public DbType<A> redacted() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof RedactedDbType<?> other)) return false;
    return inner.equals(other.inner);
  }

  @Override
  public int hashCode() {
    return inner.hashCode();
  }
}
