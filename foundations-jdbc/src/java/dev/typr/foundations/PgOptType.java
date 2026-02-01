package dev.typr.foundations;

import dev.typr.foundations.dsl.Bijection;
import java.util.Optional;
import java.util.Set;

/**
 * A PgType wrapper for Optional (nullable) types.
 * This separate record exists to override isNullable() to return true,
 * enabling query analysis to detect nullability mismatches.
 *
 * <p>Created by calling {@link PgType#opt()} on any PgType.</p>
 */
public record PgOptType<A>(
    PgTypename<Optional<A>> typename,
    PgRead<Optional<A>> read,
    PgWrite<Optional<A>> write,
    PgText<Optional<A>> pgText,
    PgCompositeText<Optional<A>> pgCompositeText,
    PgJson<Optional<A>> pgJson)
    implements DbType<Optional<A>> {

  @Override
  public DbText<Optional<A>> text() {
    return pgText;
  }

  @Override
  public DbJson<Optional<A>> json() {
    return pgJson;
  }

  @Override
  public boolean isNullable() {
    return true;
  }

  @Override
  public Set<Integer> jdbcTargets() {
    // Get targets from the underlying typename (stripping the Opt wrapper)
    if (typename instanceof PgTypename.Opt<A> opt) {
      return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcTargets(opt.of().sqlType());
    }
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcTargets(typename.sqlType());
  }

  @Override
  public Set<Integer> jdbcSources() {
    // Get sources from the underlying typename (stripping the Opt wrapper)
    if (typename instanceof PgTypename.Opt<A> opt) {
      return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcSources(opt.of().sqlType());
    }
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcSources(typename.sqlType());
  }

  public Fragment.Value<Optional<A>> encode(Optional<A> value) {
    return new Fragment.Value<>(value, this);
  }

  public PgOptType<A> withTypename(PgTypename<Optional<A>> typename) {
    return new PgOptType<>(typename, read, write, pgText, pgCompositeText, pgJson);
  }

  public PgOptType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public PgOptType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public PgOptType<A> withRead(PgRead<Optional<A>> read) {
    return new PgOptType<>(typename, read, write, pgText, pgCompositeText, pgJson);
  }

  public PgOptType<A> withWrite(PgWrite<Optional<A>> write) {
    return new PgOptType<>(typename, read, write, pgText, pgCompositeText, pgJson);
  }

  public PgOptType<A> withText(PgText<Optional<A>> text) {
    return new PgOptType<>(typename, read, write, text, pgCompositeText, pgJson);
  }

  public PgOptType<A> withJson(PgJson<Optional<A>> json) {
    return new PgOptType<>(typename, read, write, pgText, pgCompositeText, json);
  }

  /**
   * Calling opt() on an already-optional type returns a wrapped version.
   * This maintains the DbType contract but Optional<Optional<A>> is rarely useful.
   */
  @Override
  @SuppressWarnings("unchecked")
  public DbType<java.util.Optional<java.util.Optional<A>>> opt() {
    // Return this cast to the expected type - semantically this is equivalent
    // since Optional<Optional<A>> is isomorphic to Optional<A> for null handling
    return (DbType<java.util.Optional<java.util.Optional<A>>>) (DbType<?>) this;
  }

  @Override
  public <B> DbType<B> to(Bijection<Optional<A>, B> bijection) {
    return new PgType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        pgText.contramap(bijection::from),
        pgCompositeText.bimap(bijection::underlying, bijection::from),
        pgJson.bimap(bijection::underlying, bijection::from));
  }
}
