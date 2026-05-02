package dev.typr.foundations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines SQLite type name, read, write, and JSON encoding for a type.
 *
 * <p>SQLite is dynamically typed — every value belongs to one of five storage classes (NULL,
 * INTEGER, REAL, TEXT, BLOB), and a column's declared type only suggests an "affinity" for
 * conversions. This wrapper still tracks the declared type so the wrapper can emit DDL and so the
 * query analyzer can compare against {@code PRAGMA table_info()} or {@code
 * ResultSetMetaData.getColumnTypeName()}.
 */
public record SqliteType<A>(
    SqliteTypename<A> typename,
    SqliteRead<A> read,
    SqliteWrite<A> write,
    SqliteJson<A> sqliteJson,
    AnalysisOptions analysisOptions)
    implements DbType<A> {

  @Override
  public Optional<DbOutParam<A>> outParam() {
    // SQLite has no stored procedures and no callable statements.
    return Optional.empty();
  }

  @Override
  public boolean isNullable() {
    return typename.isNullable();
  }

  @Override
  public DbJson<A> json() {
    return sqliteJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    var all = new HashSet<String>();
    all.add(typename.sqlType().toLowerCase());
    for (var alias : aliases) all.add(alias.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  @Override
  public String toString() {
    return "SQLite(" + typename + ")";
  }

  public SqliteType<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public SqliteType<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public SqliteType<A> withAnalysis(AnalysisOptions opts) {
    return new SqliteType<>(typename, read, write, sqliteJson, opts);
  }

  public SqliteType<A> withTypename(SqliteTypename<A> typename) {
    return new SqliteType<>(typename, read, write, sqliteJson, analysisOptions);
  }

  public SqliteType<A> withTypename(String sqlType) {
    return withTypename(SqliteTypename.of(sqlType));
  }

  public SqliteType<A> renamed(String name) {
    return withTypename(typename.renamed(name));
  }

  public SqliteType<A> renamedDropPrecision(String name) {
    return withTypename(typename.renamedDropPrecision(name));
  }

  public SqliteType<A> withRead(SqliteRead<A> read) {
    return new SqliteType<>(typename, read, write, sqliteJson, analysisOptions);
  }

  public SqliteType<A> withWrite(SqliteWrite<A> write) {
    return new SqliteType<>(typename, read, write, sqliteJson, analysisOptions);
  }

  public SqliteType<A> withJson(SqliteJson<A> sqliteJson) {
    return new SqliteType<>(typename, read, write, sqliteJson, analysisOptions);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  @Override
  public SqliteType<Optional<A>> opt() {
    return new SqliteType<>(
        typename.opt(), read.opt(), write.opt(typename), sqliteJson.opt(), analysisOptions);
  }

  public <B> SqliteType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    return new SqliteType<>(
        typename.as(), read.map(f), write.contramap(g), sqliteJson.transform(f, g), analysisOptions);
  }

  @Override
  public <B> SqliteType<B> to(Bijection<A, B> bijection) {
    return new SqliteType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        sqliteJson.transform(bijection::underlying, bijection::from),
        analysisOptions);
  }

  public static <A> SqliteType<A> of(
      String tpe, SqliteRead<A> r, SqliteWrite<A> w, SqliteJson<A> j) {
    return of(SqliteTypename.of(tpe), r, w, j);
  }

  public static <A> SqliteType<A> of(
      SqliteTypename<A> typename, SqliteRead<A> r, SqliteWrite<A> w, SqliteJson<A> j) {
    return new SqliteType<>(typename, r, w, j, AnalysisOptions.EMPTY);
  }
}
