package dev.typr.foundations;

import dev.typr.foundations.analysis.AnalysisOptions;
import dev.typr.foundations.dsl.Bijection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines SQL Server type name, read, write, text encoding, and JSON encoding for a type. Similar
 * to MariaType but for SQL Server.
 */
public record SqlServerType<A>(
    SqlServerTypename<A> typename,
    SqlServerRead<A> read,
    SqlServerWrite<A> write,
    SqlServerText<A> sqlServerText,
    SqlServerJson<A> sqlServerJson,
    SqlServerOutParam<A> sqlServerOutParam,
    AnalysisOptions analysisOptions)
    implements DbType<A> {


  @Override
  public java.util.Optional<DbOutParam<A>> outParam() {
    return java.util.Optional.of(sqlServerOutParam);
  }

  @Override
  public boolean isNullable() {
    return typename instanceof SqlServerTypename.Opt;
  }

  @Override
  public DbText<A> text() {
    return sqlServerText;
  }

  @Override
  public DbJson<A> json() {
    return sqlServerJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    if (aliases.isEmpty()) return Set.of(typename.sqlType().toLowerCase());
    var all = new java.util.HashSet<>(aliases);
    all.add(typename.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public SqlServerType<A> unchecked() { return withAnalysis(analysisOptions.withUnchecked()); }
  public SqlServerType<A> nullableOk() { return withAnalysis(analysisOptions.withNullableOk()); }
  public SqlServerType<A> withAnalysis(AnalysisOptions opts) {
    return new SqlServerType<>(typename, read, write, sqlServerText, sqlServerJson, sqlServerOutParam, opts);
  }

  public SqlServerType<A> withTypename(SqlServerTypename<A> typename) {
    return new SqlServerType<>(typename, read, write, sqlServerText, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withTypename(String sqlType) {
    return withTypename(SqlServerTypename.of(sqlType));
  }

  public SqlServerType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public SqlServerType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public SqlServerType<A> withRead(SqlServerRead<A> read) {
    return new SqlServerType<>(typename, read, write, sqlServerText, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withWrite(SqlServerWrite<A> write) {
    return new SqlServerType<>(typename, read, write, sqlServerText, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withText(SqlServerText<A> text) {
    return new SqlServerType<>(typename, read, write, text, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withJson(SqlServerJson<A> json) {
    return new SqlServerType<>(typename, read, write, sqlServerText, json, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withOutParam(SqlServerOutParam<A> outParam) {
    return new SqlServerType<>(typename, read, write, sqlServerText, sqlServerJson, outParam, analysisOptions);
  }

  public SqlServerType<Optional<A>> opt() {
    return new SqlServerType<>(
        typename.opt(), read.opt(), write.opt(typename), sqlServerText.opt(), sqlServerJson.opt(),
        sqlServerOutParam.opt(), analysisOptions);
  }

  public <B> SqlServerType<B> bimap(SqlFunction<A, B> f, Function<B, A> g) {
    return new SqlServerType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        sqlServerText.contramap(g),
        sqlServerJson.bimap(f, g),
        sqlServerOutParam.map(f),
        analysisOptions);
  }

  public <B> SqlServerType<B> to(Bijection<A, B> bijection) {
    return new SqlServerType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        sqlServerText.contramap(bijection::from),
        sqlServerJson.bimap(bijection::underlying, bijection::from),
        sqlServerOutParam.map(bijection::underlying),
        analysisOptions);
  }

  public static <A> SqlServerType<A> of(
      String tpe, SqlServerRead<A> r, SqlServerWrite<A> w, SqlServerText<A> t, SqlServerJson<A> j,
      SqlServerOutParam<A> cr) {
    return new SqlServerType<>(SqlServerTypename.of(tpe), r, w, t, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> SqlServerType<A> of(
      SqlServerTypename<A> typename,
      SqlServerRead<A> r,
      SqlServerWrite<A> w,
      SqlServerText<A> t,
      SqlServerJson<A> j,
      SqlServerOutParam<A> cr) {
    return new SqlServerType<>(typename, r, w, t, j, cr, AnalysisOptions.EMPTY);
  }
}
