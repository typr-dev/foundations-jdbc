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
    PgJson<A> pgJson,
    PgOutParam<A> pgOutParam,
    PgBinary<A> pgBinary,
    AnalysisOptions analysisOptions,
    Optional<PgElementCodec<A>> pgArrayCodec,
    char arrayDelimiter)
    implements DbType<A> {

  public PgCompositeText<A> pgCompositeText() {
    return pgText.asCompositeText();
  }

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

  @Override
  public String toString() {
    return "PostgreSQL(" + typename + ")";
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        pgJson,
        pgOutParam,
        pgBinary,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }

  public PgType<A> withTypename(String sqlType) {
    return withTypename(PgTypename.of(sqlType));
  }

  /**
   * Reinterpret this type as the JDBC view of a PostgreSQL DOMAIN. Renames the typename to {@code
   * domainName} and switches the array codec to text-parsing so domain arrays survive JDBC
   * returning unknown-OID elements as {@link org.postgresql.util.PGobject}.
   *
   * <p>For a scalar column the underlying read/write codecs are unchanged — JDBC's column-level
   * coercion handles {@code dom_xxx} just like the underlying type. For arrays however, JDBC has no
   * per-element decoder for an unknown OID and falls back to {@code PGobject}; the standard {@code
   * (String) obj} / {@code (Boolean) obj} / etc. element converters then either throw or
   * silently return a list typed as {@code List<T>} but holding {@code PGobject} instances. By
   * reading the whole array via {@code getString} and parsing through {@link PgCompositeText} the
   * decode is independent of how the driver chose to package each element.
   */
  public PgType<A> asDomain(String domainName) {
    // Capture the underlying typename as an analysis alias before we rename it. PG JDBC's
    // ResultSetMetaData.getColumnTypeName resolves domains to their base type (TypeInfoCache
    // follows typbasetype), so a column declared as `domainName` reports back as the underlying
    // name. The query analyzer must accept either; recording the underlying as a vendor alias
    // makes the match succeed without further user opt-in. Existing aliases on the underlying
    // type (e.g. PgTypes.smallint already aliases int2) are preserved — PG canonicalizes some
    // names in catalogs and we need the canonical name to match too.
    var aliases = new java.util.HashSet<DbTypename<?>>(analysisOptions.vendorTypeNames());
    aliases.add(typename);
    AnalysisOptions opts =
        analysisOptions.withVendorTypeNames(aliases.toArray(DbTypename<?>[]::new));
    PgType<A> renamed = withTypename(domainName).withAnalysis(opts);
    return pgArrayCodec.isEmpty()
        ? renamed
        : renamed.withArrayCodec(PgElementCodec.textParsed());
  }

  /**
   * Combined {@link #asDomain(String)} + {@link #transform(SqlFunction, Function)} — declares
   * this type as a PG DOMAIN named {@code domainName} and wraps it in a typed value class in one
   * call. Identical to {@code asDomain(domainName).transform(f, g)}.
   *
   * <p>Use this so the wrapping is in place at the scalar level before {@code .array()} or any
   * other combinator runs — the array codec then carries the wrapper end-to-end and you avoid
   * needing a list-level bijection.
   */
  public <B> PgType<B> asDomain(String domainName, SqlFunction<A, B> f, Function<B, A> g) {
    return asDomain(domainName).transform(f, g);
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        json,
        pgOutParam,
        pgBinary,
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
        pgJson,
        outParam,
        pgBinary,
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        pgJson,
        pgOutParam,
        pgBinary,
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
        pgJson.opt(),
        pgOutParam.opt(),
        pgBinary.opt(),
        analysisOptions,
        Optional.empty(),
        arrayDelimiter);
  }

  /**
   * Variable-length array of this type. Returns {@link PgType} parameterised on {@link List} — PG's
   * {@code T[]} always maps to {@code List<T>}. Call {@code .array().array()} for multi-dimensional
   * arrays like {@code int4[][]} → {@code List<List<Integer>>}.
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
            ? PgRead.readCompositeList(pgCompositeText(), arrayDelimiter)
            : PgRead.readElementList(elementConverter);
    // Nested-array support: the resulting PgType<List<A>> needs its own pgArrayCodec so a
    // further .array() call (producing PgType<List<List<A>>>) can decode sub-arrays. If the
    // scalar used OfText (bit, time, timetz, money, ranges) we keep the outer as OfText too
    // — pgCompositeText.list(',') composes naturally at every depth. For OfElement types we
    // recurse via java.sql.Array/Object[] since PG JDBC returns multi-dim arrays as
    // Object[][…] (every level is an Object[], never a java.sql.Array).
    final PgElementCodec<List<A>> nestedCodec;
    if (codec instanceof PgElementCodec.OfText) {
      nestedCodec = PgElementCodec.textParsed();
    } else {
      nestedCodec =
          PgElementCodec.of(
              obj -> {
                if (obj == null) return null;
                Object[] subElements;
                if (obj instanceof java.sql.Array subArr) {
                  try {
                    subElements = (Object[]) subArr.getArray();
                  } catch (java.sql.SQLException ex) {
                    throw new DatabaseException.Jdbc(ex);
                  }
                } else if (obj instanceof Object[] arr) {
                  subElements = arr;
                } else {
                  throw new IllegalArgumentException(
                      "Expected java.sql.Array or Object[] for nested PG array element, got: "
                          + obj.getClass());
                }
                List<A> inner = new java.util.ArrayList<>(subElements.length);
                for (Object e : subElements) {
                  inner.add(e == null ? null : elementConverter.apply(e));
                }
                return inner;
              });
    }
    PgWrite<List<A>> listWrite;
    if (typename instanceof PgTypename.ArrayOf<?>) {
      // Nested case. PG JDBC's createArrayOf only accepts the scalar type name plus a
      // (possibly multi-dim) Object[] payload; chaining `createArrayOf("int4[]", ...)` is not
      // supported. Peel ArrayOf wrappers to the scalar, then build the nested Object[][…]
      // payload via the existing write's pre-bind converter.
      PgTypename<?> scalarTypename = typename;
      while (scalarTypename instanceof PgTypename.ArrayOf<?> arr) {
        scalarTypename = arr.of();
      }
      final String scalarSqlType = scalarTypename.sqlTypeNoPrecision();
      final PgWrite<A> innerWrite = this.write;
      // innerWrite.f is A→Object[] for single-level, or List<A>→Object[] (with nested inner)
      // for deeper nesting; applying it to each outer element produces the next inner Object[].
      final Function<A, Object> innerElementConverter;
      if (innerWrite instanceof PgWrite.Instance<A, ?> i) {
        @SuppressWarnings("unchecked")
        Function<A, Object> f = (Function<A, Object>) i.f();
        innerElementConverter = f;
      } else {
        innerElementConverter = a -> a;
      }
      listWrite =
          PgWrite.primitive(
              (ps, idx, list) -> {
                if (list == null) {
                  ps.setNull(idx, 0, scalarSqlType + "[]");
                } else {
                  Object[] values = new Object[list.size()];
                  for (int i = 0; i < list.size(); i++) {
                    A elem = list.get(i);
                    values[i] = elem == null ? null : innerElementConverter.apply(elem);
                  }
                  ps.setArray(idx, ps.getConnection().createArrayOf(scalarSqlType, values));
                }
              });
    } else {
      listWrite = write.list(typename);
    }
    PgText<List<A>> listText = pgText.list(arrayDelimiter);
    return new PgType<>(
        typename.array(),
        listRead,
        listWrite,
        listText,
        pgJson.list(),
        PgOutParam.parsedList(pgText::compositeDecode),
        PgBinary.textFallback(listText),
        analysisOptions.listForms(),
        Optional.of(nestedCodec),
        arrayDelimiter);
  }

  public <B> PgType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    Function<A, B> uncheckedF =
        a -> {
          try {
            return f.apply(a);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException.Jdbc(e);
          }
        };
    return new PgType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        pgText.bimap(uncheckedF, g),
        pgJson.transform(f, g),
        pgOutParam.map(f),
        pgBinary.bimap(uncheckedF, g),
        analysisOptions,
        pgArrayCodec.map(codec -> codec.map(uncheckedF)),
        arrayDelimiter);
  }

  public <B> PgType<B> to(Bijection<A, B> bijection) {
    return new PgType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        pgText.bimap(bijection::underlying, bijection::from),
        pgJson.transform(bijection::underlying, bijection::from),
        pgOutParam.map(bijection::underlying),
        pgBinary.bimap(bijection::underlying, bijection::from),
        analysisOptions,
        pgArrayCodec.map(codec -> codec.map(bijection::underlying)),
        arrayDelimiter);
  }

  public PgType<A> withBinary(PgBinary<A> binary) {
    return new PgType<>(
        typename,
        read,
        write,
        pgText,
        pgJson,
        pgOutParam,
        binary,
        analysisOptions,
        pgArrayCodec,
        arrayDelimiter);
  }
}
