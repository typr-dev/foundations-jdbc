package dev.typr.foundations;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines SQL Server type name, read, write, and JSON encoding for a type. Similar to OracleType
 * but for SQL Server. Note: SQL Server doesn't support text-based streaming inserts (like
 * PostgreSQL's COPY), so there is no SqlServerText component.
 */
public record SqlServerType<A>(
    SqlServerTypename<A> typename,
    SqlServerRead<A> read,
    SqlServerWrite<A> write,
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
  public DbJson<A> json() {
    return sqlServerJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    var all = new java.util.HashSet<String>();
    all.add(typename.sqlType().toLowerCase());
    for (var alias : aliases) all.add(alias.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  @Override
  public String toString() {
    return "SQLServer(" + typename + ")";
  }

  public SqlServerType<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public SqlServerType<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public SqlServerType<A> withAnalysis(AnalysisOptions opts) {
    return new SqlServerType<>(typename, read, write, sqlServerJson, sqlServerOutParam, opts);
  }

  public SqlServerType<A> withTypename(SqlServerTypename<A> typename) {
    return new SqlServerType<>(
        typename, read, write, sqlServerJson, sqlServerOutParam, analysisOptions);
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
    return new SqlServerType<>(
        typename, read, write, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withWrite(SqlServerWrite<A> write) {
    return new SqlServerType<>(
        typename, read, write, sqlServerJson, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withJson(SqlServerJson<A> json) {
    return new SqlServerType<>(typename, read, write, json, sqlServerOutParam, analysisOptions);
  }

  public SqlServerType<A> withOutParam(SqlServerOutParam<A> outParam) {
    return new SqlServerType<>(typename, read, write, sqlServerJson, outParam, analysisOptions);
  }

  public SqlServerType<Optional<A>> opt() {
    return new SqlServerType<>(
        typename.opt(),
        read.opt(),
        write.opt(typename),
        sqlServerJson.opt(),
        sqlServerOutParam.opt(),
        analysisOptions);
  }

  public <B> SqlServerType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    return new SqlServerType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        sqlServerJson.transform(f, g),
        sqlServerOutParam.map(f),
        analysisOptions);
  }

  public <B> SqlServerType<B> to(Bijection<A, B> bijection) {
    return new SqlServerType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        sqlServerJson.transform(bijection::underlying, bijection::from),
        sqlServerOutParam.map(bijection::underlying),
        analysisOptions);
  }

  public static <A> SqlServerType<A> of(
      String tpe,
      SqlServerRead<A> r,
      SqlServerWrite<A> w,
      SqlServerJson<A> j,
      SqlServerOutParam<A> cr) {
    return new SqlServerType<>(SqlServerTypename.of(tpe), r, w, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> SqlServerType<A> of(
      SqlServerTypename<A> typename,
      SqlServerRead<A> r,
      SqlServerWrite<A> w,
      SqlServerJson<A> j,
      SqlServerOutParam<A> cr) {
    return new SqlServerType<>(typename, r, w, j, cr, AnalysisOptions.EMPTY);
  }
}
