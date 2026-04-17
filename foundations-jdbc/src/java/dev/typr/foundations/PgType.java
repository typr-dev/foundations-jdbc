package dev.typr.foundations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public record PgType<A>(
    PgTypename<A> typename,
    PgRead<A> read,
    PgWrite<A> write,
    PgText<A> pgText,
    PgCompositeText<A> pgCompositeText,
    PgJson<A> pgJson,
    PgOutParam<A> pgOutParam,
    AnalysisOptions analysisOptions,
    Optional<PgElementCodec<A>> pgArrayCodec,
    char arrayDelimiter)
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
  public java.util.Optional<DbText<A>> text() {
    return java.util.Optional.of(pgText);
  }

  @Override
  public DbJson<A> json() {
    return pgJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    var all = new java.util.HashSet<String>();
    all.add(typename.sqlType().toLowerCase());
    for (var alias : aliases) all.add(alias.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public PgType<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public PgType<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public PgType<A> withAnalysis(AnalysisOptions opts) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        opts,
        pgArrayCodec,
        arrayDelimiter);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public PgType<A> withTypename(PgTypename<A> typename) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
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
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withWrite(PgWrite<A> write) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withText(PgText<A> text) {
    return new PgType<>(
        typename,
        read,
        write,
        text,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withCompositeText(PgCompositeText<A> compositeText) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        compositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withJson(PgJson<A> json) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        json,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withOutParam(PgOutParam<A> outParam) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        outParam,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withArrayCodec(PgElementCodec<A> codec) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        Optional.of(codec),
        arrayDelimiter);
  }

  /**
   * Set the array element delimiter used when this type is wrapped in a PG array (typdelim).
   * Default is ','; geometric types (box, circle, line, lseg, path, point, polygon) use ';'.
   */
  public PgType<A> withArrayDelimiter(char delimiter) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgCompositeText,
        pgJson,
        pgOutParam,
        analysisOptions,
        pgArrayCodec,
        delimiter);
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
        analysisOptions,
        Optional.empty(),
        arrayDelimiter);
  }

  /**
   * Variable-length array of this type. Returns {@link PgType} parameterised on {@link List}
   * — PG's {@code T[]} always maps to {@code List<T>}. Call {@code .array().array()} for
   * multi-dimensional arrays like {@code int4[][]} → {@code List<List<Integer>>}.
   */
  public PgType<List<A>> array() {
    PgElementCodec<A> codec =
        pgArrayCodec.orElseThrow(
            () ->
                new IllegalStateException(
                    "Array not supported for "
                        + typename.sqlType()
                        + ". This type does not provide a PgElementCodec."));
    final Function<Object, A> elementConverter =
        switch (codec) {
          case PgElementCodec.OfElement<A> e -> e.converter();
          case PgElementCodec.OfText<A> ignored ->
              // OfText reads whole array as text — no per-element converter. Fall through
              // to the compositeText path handled below.
              null;
        };
    PgRead<List<A>> listRead =
        (codec instanceof PgElementCodec.OfText)
            ? PgRead.readCompositeList(pgCompositeText)
            : PgRead.readElementList(elementConverter);
    // Nested-array support: the resulting PgType<List<A>> needs its own pgArrayCodec so that
    // a further .array() call (producing PgType<List<List<A>>>) can decode sub-arrays.
    PgElementCodec<List<A>> nestedCodec =
        PgElementCodec.of(
            obj -> {
              if (obj == null) return null;
              if (!(obj instanceof java.sql.Array subArr)) {
                throw new IllegalArgumentException(
                    "Expected java.sql.Array for nested PG array element, got: " + obj.getClass());
              }
              try {
                Object[] subElements = (Object[]) subArr.getArray();
                List<A> inner = new java.util.ArrayList<>(subElements.length);
                if (elementConverter != null) {
                  for (Object e : subElements) inner.add(e == null ? null : elementConverter.apply(e));
                } else {
                  // OfText: shouldn't normally combine with nesting, but handle gracefully.
                  for (Object e : subElements) {
                    inner.add(e == null ? null : pgCompositeText.decode(e.toString()));
                  }
                }
                return inner;
              } catch (java.sql.SQLException ex) {
                throw new DatabaseException(ex);
              }
            });
    return new PgType<>(
        typename.array(),
        listRead,
        write.list(typename),
        pgText.list(arrayDelimiter),
        pgCompositeText.list(arrayDelimiter),
        pgJson.list(),
        PgOutParam.parsedList(pgCompositeText::decode),
        analysisOptions.listForms(),
        Optional.of(nestedCodec),
        ',');
  }

  public <B> PgType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    return new PgType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        pgText.contramap(g),
        pgCompositeText.transform(
            a -> {
              try {
                return f.apply(a);
              } catch (java.sql.SQLException e) {
                throw new DatabaseException(e);
              }
            },
            g),
        pgJson.transform(f, g),
        pgOutParam.map(f),
        analysisOptions,
        pgArrayCodec.map(
            codec ->
                codec.map(
                    a -> {
                      try {
                        return f.apply(a);
                      } catch (java.sql.SQLException e) {
                        throw new DatabaseException(e);
                      }
                    })),
        arrayDelimiter);
  }

  public <B> PgType<B> to(Bijection<A, B> bijection) {
    return new PgType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        pgText.contramap(bijection::from),
        pgCompositeText.transform(bijection::underlying, bijection::from),
        pgJson.transform(bijection::underlying, bijection::from),
        pgOutParam.map(bijection::underlying),
        analysisOptions,
        pgArrayCodec.map(codec -> codec.map(bijection::underlying)),
        arrayDelimiter);
  }
}
