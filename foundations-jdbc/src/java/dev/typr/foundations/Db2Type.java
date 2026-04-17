package dev.typr.foundations;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines DB2 type name, read, write, and JSON encoding for a type. Similar to OracleType but for
 * IBM DB2. Note: DB2 doesn't support text-based streaming inserts (like PostgreSQL's COPY), so
 * there is no Db2Text component.
 */
public record Db2Type<A>(
    Db2Typename<A> typename,
    Db2Read<A> read,
    Db2Write<A> write,
    Db2Json<A> db2Json,
    Db2OutParam<A> db2OutParam,
    AnalysisOptions analysisOptions)
    implements DbType<A> {

  @Override
  public java.util.Optional<DbOutParam<A>> outParam() {
    return java.util.Optional.of(db2OutParam);
  }

  @Override
  public boolean isNullable() {
    return typename instanceof Db2Typename.Opt;
  }

  @Override
  public DbJson<A> json() {
    return db2Json;
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
    return "DB2(" + typename + ")";
  }

  public Db2Type<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public Db2Type<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public Db2Type<A> withAnalysis(AnalysisOptions opts) {
    return new Db2Type<>(typename, read, write, db2Json, db2OutParam, opts);
  }

  public Db2Type<A> withTypename(Db2Typename<A> typename) {
    return new Db2Type<>(typename, read, write, db2Json, db2OutParam, analysisOptions);
  }

  public Db2Type<A> withTypename(String sqlType) {
    return withTypename(Db2Typename.of(sqlType));
  }

  public Db2Type<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public Db2Type<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public Db2Type<A> withRead(Db2Read<A> read) {
    return new Db2Type<>(typename, read, write, db2Json, db2OutParam, analysisOptions);
  }

  public Db2Type<A> withWrite(Db2Write<A> write) {
    return new Db2Type<>(typename, read, write, db2Json, db2OutParam, analysisOptions);
  }

  public Db2Type<A> withJson(Db2Json<A> json) {
    return new Db2Type<>(typename, read, write, json, db2OutParam, analysisOptions);
  }

  public Db2Type<A> withOutParam(Db2OutParam<A> outParam) {
    return new Db2Type<>(typename, read, write, db2Json, outParam, analysisOptions);
  }

  public Db2Type<Optional<A>> opt() {
    return new Db2Type<>(
        typename.opt(),
        read.opt(),
        write.opt(typename),
        db2Json.opt(),
        db2OutParam.opt(),
        analysisOptions);
  }

  public <B> Db2Type<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    return new Db2Type<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        db2Json.transform(f, g),
        db2OutParam.map(f),
        analysisOptions);
  }

  public <B> Db2Type<B> to(Bijection<A, B> bijection) {
    return new Db2Type<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        db2Json.transform(bijection::underlying, bijection::from),
        db2OutParam.map(bijection::underlying),
        analysisOptions);
  }

  public static <A> Db2Type<A> of(
      String tpe, Db2Read<A> r, Db2Write<A> w, Db2Json<A> j, Db2OutParam<A> cr) {
    return new Db2Type<>(Db2Typename.of(tpe), r, w, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> Db2Type<A> of(
      Db2Typename<A> typename, Db2Read<A> r, Db2Write<A> w, Db2Json<A> j, Db2OutParam<A> cr) {
    return new Db2Type<>(typename, r, w, j, cr, AnalysisOptions.EMPTY);
  }
}
