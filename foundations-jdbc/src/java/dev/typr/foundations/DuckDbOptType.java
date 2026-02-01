package dev.typr.foundations;

import dev.typr.foundations.dsl.Bijection;
import java.util.Optional;
import java.util.Set;

/**
 * A DuckDbType wrapper for Optional (nullable) types.
 * This separate record exists to override isNullable() to return true,
 * enabling query analysis to detect nullability mismatches.
 *
 * <p>Created by calling {@link DuckDbType#opt()} on any DuckDbType.</p>
 */
public record DuckDbOptType<A>(
    DuckDbTypename<Optional<A>> typename,
    DuckDbRead<Optional<A>> read,
    DuckDbWrite<Optional<A>> write,
    DuckDbStringifier<Optional<A>> stringifier,
    DuckDbJson<Optional<A>> duckDbJson,
    DuckDbText<Optional<A>> duckDbText,
    DuckDbMapSupport<Optional<A>> mapSupport)
    implements DbType<Optional<A>> {

  public DuckDbOptType(
      DuckDbTypename<Optional<A>> typename,
      DuckDbRead<Optional<A>> read,
      DuckDbWrite<Optional<A>> write,
      DuckDbStringifier<Optional<A>> stringifier,
      DuckDbJson<Optional<A>> duckDbJson) {
    this(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        DuckDbText.instance((a, sb) -> stringifier.unsafeEncode(a, sb, false)),
        DuckDbMapSupport.cast());
  }

  @Override
  public DbText<Optional<A>> text() {
    return duckDbText;
  }

  @Override
  public DbJson<Optional<A>> json() {
    return duckDbJson;
  }

  @Override
  public boolean isNullable() {
    return true;
  }

  @Override
  public Set<Integer> jdbcTargets() {
    if (typename instanceof DuckDbTypename.Opt<A> opt) {
      return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcTargets(opt.of().sqlType());
    }
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcTargets(typename.sqlType());
  }

  @Override
  public Set<Integer> jdbcSources() {
    if (typename instanceof DuckDbTypename.Opt<A> opt) {
      return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcSources(opt.of().sqlType());
    }
    return dev.typr.foundations.analysis.JdbcTypeRegistry.jdbcSources(typename.sqlType());
  }

  public Fragment.Value<Optional<A>> encode(Optional<A> value) {
    return new Fragment.Value<>(value, this);
  }

  public DuckDbOptType<A> withTypename(DuckDbTypename<Optional<A>> typename) {
    return new DuckDbOptType<>(typename, read, write, stringifier, duckDbJson, duckDbText, mapSupport);
  }

  public DuckDbOptType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  @Override
  @SuppressWarnings("unchecked")
  public DbType<Optional<Optional<A>>> opt() {
    return (DbType<Optional<Optional<A>>>) (DbType<?>) this;
  }

  @Override
  public <B> DbType<B> to(Bijection<Optional<A>, B> bijection) {
    return new DuckDbType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        stringifier.contramap(bijection::from),
        duckDbJson.bimap(bijection::underlying, bijection::from),
        mapSupport.bimap(bijection::underlying, bijection::from));
  }
}
