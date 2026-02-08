package dev.typr.foundations;

import dev.typr.foundations.analysis.AnalysisOptions;
import dev.typr.foundations.dsl.Bijection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines Oracle type name, read, write, and JSON encoding for a type. Similar to PgType/MariaType
 * but for Oracle. Note: Oracle doesn't use text-based streaming inserts (like PostgreSQL's COPY),
 * so there is no OracleText component.
 */
public record OracleType<A>(
    OracleTypename<A> typename,
    OracleRead<A> read,
    OracleWrite<A> write,
    OracleJson<A> oracleJson,
    OracleOutParam<A> oracleOutParam,
    AnalysisOptions analysisOptions)
    implements DbType<A> {


  @Override
  public java.util.Optional<DbOutParam<A>> outParam() {
    return java.util.Optional.of(oracleOutParam);
  }

  @Override
  public boolean isNullable() {
    return typename instanceof OracleTypename.Opt;
  }

  @Override
  public DbJson<A> json() {
    return oracleJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    if (aliases.isEmpty()) return Set.of(typename.sqlType().toLowerCase());
    var all = new java.util.HashSet<>(aliases);
    all.add(typename.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public OracleType<A> unchecked() { return withAnalysis(analysisOptions.withUnchecked()); }
  public OracleType<A> nullableOk() { return withAnalysis(analysisOptions.withNullableOk()); }
  public OracleType<A> withAnalysis(AnalysisOptions opts) {
    return new OracleType<>(typename, read, write, oracleJson, oracleOutParam, opts);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public OracleType<A> withTypename(OracleTypename<A> typename) {
    return new OracleType<>(typename, read, write, oracleJson, oracleOutParam, analysisOptions);
  }

  public OracleType<A> withTypename(String sqlType) {
    return withTypename(OracleTypename.of(sqlType));
  }

  public OracleType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public OracleType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public OracleType<A> withRead(OracleRead<A> read) {
    return new OracleType<>(typename, read, write, oracleJson, oracleOutParam, analysisOptions);
  }

  public OracleType<A> withWrite(OracleWrite<A> write) {
    return new OracleType<>(typename, read, write, oracleJson, oracleOutParam, analysisOptions);
  }

  public OracleType<A> withJson(OracleJson<A> json) {
    return new OracleType<>(typename, read, write, json, oracleOutParam, analysisOptions);
  }

  public OracleType<A> withOutParam(OracleOutParam<A> outParam) {
    return new OracleType<>(typename, read, write, oracleJson, outParam, analysisOptions);
  }

  public OracleType<Optional<A>> opt() {
    return new OracleType<>(
        typename.opt(), read.opt(), write.opt(typename), oracleJson.opt(),
        oracleOutParam.opt(), analysisOptions);
  }

  @Override
  public <B> OracleType<B> to(Bijection<A, B> bijection) {
    return new OracleType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        oracleJson.bimap(bijection::underlying, bijection::from),
        oracleOutParam.map(bijection::underlying),
        analysisOptions);
  }

  public <B> OracleType<B> bimap(SqlFunction<A, B> f, Function<B, A> g) {
    return new OracleType<>(
        typename.as(), read.map(f), write.contramap(g), oracleJson.bimap(f, g),
        oracleOutParam.map(f), analysisOptions);
  }

  public static <A> OracleType<A> of(
      String tpe, OracleRead<A> r, OracleWrite<A> w, OracleJson<A> j, OracleOutParam<A> cr) {
    return new OracleType<>(OracleTypename.of(tpe), r, w, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> OracleType<A> of(
      OracleTypename<A> typename, OracleRead<A> r, OracleWrite<A> w, OracleJson<A> j,
      OracleOutParam<A> cr) {
    return new OracleType<>(typename, r, w, j, cr, AnalysisOptions.EMPTY);
  }
}
