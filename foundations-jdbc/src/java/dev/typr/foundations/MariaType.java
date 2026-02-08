package dev.typr.foundations;

import dev.typr.foundations.analysis.AnalysisOptions;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines MariaDB type name, read, write, and JSON encoding for a type. Similar to PgType but for
 * MariaDB. Note: MariaDB doesn't support text-based streaming inserts (like PostgreSQL's COPY), so
 * there is no MariaText component.
 */
public record MariaType<A>(
    MariaTypename<A> typename,
    MariaRead<A> read,
    MariaWrite<A> write,
    MariaJson<A> mariaJson,
    MariaOutParam<A> mariaOutParam,
    AnalysisOptions analysisOptions)
    implements DbType<A> {


  @Override
  public java.util.Optional<DbOutParam<A>> outParam() {
    return java.util.Optional.of(mariaOutParam);
  }

  @Override
  public boolean isNullable() {
    return typename instanceof MariaTypename.Opt;
  }

  @Override
  public DbJson<A> json() {
    return mariaJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    if (aliases.isEmpty()) return Set.of(typename.sqlType().toLowerCase());
    var all = new java.util.HashSet<>(aliases);
    all.add(typename.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public MariaType<A> unchecked() { return withAnalysis(analysisOptions.withUnchecked()); }
  public MariaType<A> nullableOk() { return withAnalysis(analysisOptions.withNullableOk()); }
  public MariaType<A> withAnalysis(AnalysisOptions opts) {
    return new MariaType<>(typename, read, write, mariaJson, mariaOutParam, opts);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public MariaType<A> withTypename(MariaTypename<A> typename) {
    return new MariaType<>(typename, read, write, mariaJson, mariaOutParam, analysisOptions);
  }

  public MariaType<A> withTypename(String sqlType) {
    return withTypename(MariaTypename.of(sqlType));
  }

  public MariaType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public MariaType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public MariaType<A> withRead(MariaRead<A> read) {
    return new MariaType<>(typename, read, write, mariaJson, mariaOutParam, analysisOptions);
  }

  public MariaType<A> withWrite(MariaWrite<A> write) {
    return new MariaType<>(typename, read, write, mariaJson, mariaOutParam, analysisOptions);
  }

  public MariaType<A> withJson(MariaJson<A> json) {
    return new MariaType<>(typename, read, write, json, mariaOutParam, analysisOptions);
  }

  public MariaType<A> withOutParam(MariaOutParam<A> outParam) {
    return new MariaType<>(typename, read, write, mariaJson, outParam, analysisOptions);
  }

  public MariaType<Optional<A>> opt() {
    return new MariaType<>(
        typename.opt(), read.opt(), write.opt(typename), mariaJson.opt(),
        mariaOutParam.opt(), analysisOptions);
  }

  public <B> MariaType<B> bimap(SqlFunction<A, B> f, Function<B, A> g) {
    return new MariaType<>(
        typename.as(), read.map(f), write.contramap(g), mariaJson.bimap(f, g),
        mariaOutParam.map(f), analysisOptions);
  }

  public <B> MariaType<B> to(Bijection<A, B> bijection) {
    return new MariaType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        mariaJson.bimap(bijection::underlying, bijection::from),
        mariaOutParam.map(bijection::underlying),
        analysisOptions);
  }

  public static <A> MariaType<A> of(
      String tpe, MariaRead<A> r, MariaWrite<A> w, MariaJson<A> j,
      MariaOutParam<A> cr) {
    return new MariaType<>(MariaTypename.of(tpe), r, w, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> MariaType<A> of(
      MariaTypename<A> typename, MariaRead<A> r, MariaWrite<A> w, MariaJson<A> j,
      MariaOutParam<A> cr) {
    return new MariaType<>(typename, r, w, j, cr, AnalysisOptions.EMPTY);
  }
}
