package dev.typr.foundations;

import dev.typr.foundations.analysis.AnalysisOptions;
import dev.typr.foundations.dsl.Bijection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

public record PgType<A>(
    PgTypename<A> typename,
    PgRead<A> read,
    PgWrite<A> write,
    PgText<A> pgText,
    PgCompositeText<A> pgCompositeText,
    PgJson<A> pgJson,
    PgOutParam<A> pgOutParam,
    AnalysisOptions analysisOptions)
    implements DbType<A> {


  @Override
  public Optional<DbOutParam<A>> outParam() {
    return Optional.of(pgOutParam);
  }

  @Override
  public boolean isNullable() {
    return typename instanceof PgTypename.Opt;
  }

  @Override
  public DbText<A> text() {
    return pgText;
  }

  @Override
  public DbJson<A> json() {
    return pgJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    if (aliases.isEmpty()) return Set.of(typename.sqlType().toLowerCase());
    var all = new java.util.HashSet<>(aliases);
    all.add(typename.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public PgType<A> unchecked() { return withAnalysis(analysisOptions.withUnchecked()); }
  public PgType<A> nullableOk() { return withAnalysis(analysisOptions.withNullableOk()); }
  public PgType<A> withAnalysis(AnalysisOptions opts) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, pgJson, pgOutParam, opts);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public PgType<A> withTypename(PgTypename<A> typename) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, pgJson, pgOutParam, analysisOptions);
  }

  public PgType<A> withTypename(String sqlType) {
    return withTypename(PgTypename.of(sqlType));
  }

  public PgType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public PgType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public PgType<A> withRead(PgRead<A> read) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, pgJson, pgOutParam, analysisOptions);
  }

  public PgType<A> withWrite(PgWrite<A> write) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, pgJson, pgOutParam, analysisOptions);
  }

  public PgType<A> withText(PgText<A> text) {
    return new PgType<>(typename, read, write, text, pgCompositeText, pgJson, pgOutParam, analysisOptions);
  }

  public PgType<A> withCompositeText(PgCompositeText<A> compositeText) {
    return new PgType<>(typename, read, write, pgText, compositeText, pgJson, pgOutParam, analysisOptions);
  }

  public PgType<A> withJson(PgJson<A> json) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, json, pgOutParam, analysisOptions);
  }

  public PgType<A> withOutParam(PgOutParam<A> outParam) {
    return new PgType<>(typename, read, write, pgText, pgCompositeText, pgJson, outParam, analysisOptions);
  }

  @Override
  public PgType<Optional<A>> opt() {
    return new PgType<>(
        typename.opt(),
        read.opt(),
        write.opt(typename),
        pgText.opt(),
        pgCompositeText.opt(),
        pgJson.opt(),
        pgOutParam.opt(),
        analysisOptions);
  }

  public PgType<A[]> array(PgRead<A[]> read, IntFunction<A[]> arrayFactory) {
    return new PgType<>(
        typename.array(),
        read,
        write.array(typename),
        pgText.array(),
        pgCompositeText.array(arrayFactory),
        pgJson.array(arrayFactory),
        PgOutParam.parsedArray(arrayFactory, pgCompositeText::decode),
        analysisOptions);
  }

  public PgType<A[]> array(
      PgRead<A[]> read, IntFunction<A[]> arrayFactory, char compositeTextDelimiter) {
    return new PgType<>(
        typename.array(),
        read,
        write.array(typename),
        pgText.array(),
        pgCompositeText.array(arrayFactory, compositeTextDelimiter),
        pgJson.array(arrayFactory),
        PgOutParam.parsedArray(arrayFactory, pgCompositeText::decode),
        analysisOptions);
  }

  public PgType<A[]> array(PgRead<A[]> read, PgWrite<A[]> write, IntFunction<A[]> arrayFactory) {
    return new PgType<>(
        typename.array(),
        read,
        write,
        pgText.array(),
        pgCompositeText.array(arrayFactory),
        pgJson.array(arrayFactory),
        PgOutParam.parsedArray(arrayFactory, pgCompositeText::decode),
        analysisOptions);
  }

  public <B> PgType<B> bimap(SqlFunction<A, B> f, Function<B, A> g) {
    return new PgType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        pgText.contramap(g),
        pgCompositeText.bimap(
            a -> {
              try {
                return f.apply(a);
              } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
              }
            },
            g),
        pgJson.bimap(f, g),
        pgOutParam.map(f),
        analysisOptions);
  }

  public <B> PgType<B> to(Bijection<A, B> bijection) {
    return new PgType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        pgText.contramap(bijection::from),
        pgCompositeText.bimap(bijection::underlying, bijection::from),
        pgJson.bimap(bijection::underlying, bijection::from),
        pgOutParam.map(bijection::underlying),
        analysisOptions);
  }

  public static <A> PgType<A> of(
      String tpe, PgRead<A> r, PgWrite<A> w, PgText<A> t, PgCompositeText<A> ct, PgJson<A> j,
      PgOutParam<A> cr) {
    return new PgType<>(PgTypename.of(tpe), r, w, t, ct, j, cr, AnalysisOptions.EMPTY);
  }

  public static <A> PgType<A> of(
      PgTypename<A> typename,
      PgRead<A> r,
      PgWrite<A> w,
      PgText<A> t,
      PgCompositeText<A> ct,
      PgJson<A> j,
      PgOutParam<A> cr) {
    return new PgType<>(typename, r, w, t, ct, j, cr, AnalysisOptions.EMPTY);
  }
}
