package dev.typr.foundations;

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
    Optional<PgArrayCodec<A>> pgArrayCodec,
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

  public PgType<A> withArrayCodec(PgArrayCodec<A> codec) {
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

  @SuppressWarnings("unchecked")
  public PgType<A[]> array() {
    PgArrayCodec<A> codec =
        pgArrayCodec.orElseThrow(
            () ->
                new IllegalStateException(
                    "Array not supported for "
                        + typename.sqlType()
                        + ". This type does not provide a PgArrayCodec."));
    java.util.function.IntFunction<A[]> arrayFactory = size -> (A[]) new Object[size];
    PgRead<A[]> arrayRead =
        switch (codec) {
          case PgArrayCodec.OfElement<A> e ->
              PgRead.of(
                  (rs, idx) -> {
                    java.sql.Array arr = rs.getArray(idx);
                    if (arr == null) return null;
                    Object[] elements = (Object[]) arr.getArray();
                    // Decode elements first, then build a properly-typed array via reflection
                    // based on the first non-null element's class. Avoids ClassCastException when
                    // the result flows into a concrete typed field (e.g. LineItem[] in a record).
                    Class<?> elementClass = null;
                    Object[] decoded = new Object[elements.length];
                    for (int i = 0; i < elements.length; i++) {
                      decoded[i] = e.converter().apply(elements[i]);
                      if (elementClass == null && decoded[i] != null)
                        elementClass = decoded[i].getClass();
                    }
                    A[] result;
                    if (elementClass != null) {
                      result =
                          (A[]) java.lang.reflect.Array.newInstance(elementClass, decoded.length);
                    } else {
                      result = arrayFactory.apply(decoded.length);
                    }
                    for (int i = 0; i < decoded.length; i++) result[i] = (A) decoded[i];
                    return result;
                  });
          case PgArrayCodec.OfText<A> ignored ->
              PgRead.readCompositeArray(pgCompositeText, arrayFactory);
        };
    return new PgType<>(
        typename.array(),
        arrayRead,
        write.array(typename),
        pgText.array(arrayDelimiter),
        pgCompositeText.array(arrayFactory, arrayDelimiter),
        pgJson.array(arrayFactory),
        PgOutParam.parsedArray(arrayFactory, pgCompositeText::decode),
        analysisOptions.arrayForms(),
        Optional.empty(),
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
