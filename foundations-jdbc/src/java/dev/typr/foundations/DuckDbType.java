package dev.typr.foundations;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines DuckDB type name, read, write, stringification, and JSON encoding for a type. Similar to
 * PgType but for DuckDB. Note: DuckDB doesn't support text-based streaming inserts via JDBC (like
 * PostgreSQL's COPY), so there is no DuckDbText component.
 */
public record DuckDbType<A>(
    DuckDbTypename<A> typename,
    DuckDbRead<A> read,
    DuckDbWrite<A> write,
    DuckDbStringifier<A> stringifier,
    DuckDbJson<A> duckDbJson,
    DuckDbMapSupport<A> mapSupport,
    AnalysisOptions analysisOptions,
    java.util.Optional<DuckDbArrayCodec<A>> arrayCodec,
    java.util.Optional<DuckDbListCodec<A>> listCodec,
    Function<A, Object> structAttributeEncoder)
    implements DbType<A> {

  /**
   * Convenience constructor that defaults the struct-attribute encoder to identity. Safe for
   * scalar types whose Java representation is directly bindable as a {@link
   * org.duckdb.user.DuckDBUserStruct} attribute.
   */
  public DuckDbType(
      DuckDbTypename<A> typename,
      DuckDbRead<A> read,
      DuckDbWrite<A> write,
      DuckDbStringifier<A> stringifier,
      DuckDbJson<A> duckDbJson,
      DuckDbMapSupport<A> mapSupport,
      AnalysisOptions analysisOptions,
      java.util.Optional<DuckDbArrayCodec<A>> arrayCodec,
      java.util.Optional<DuckDbListCodec<A>> listCodec) {
    this(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        arrayCodec,
        listCodec,
        a -> a);
  }

  public DuckDbType<A> withStructAttributeEncoder(Function<A, Object> encoder) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        arrayCodec,
        listCodec,
        encoder);
  }

  @Override
  public Optional<DbOutParam<A>> outParam() {
    return Optional.empty();
  }

  @Override
  public boolean isNullable() {
    return typename instanceof DuckDbTypename.Opt;
  }

  @Override
  public DbJson<A> json() {
    return duckDbJson;
  }

  @Override
  public Set<String> vendorTypeNames() {
    var aliases = analysisOptions.vendorTypeNames();
    var all = new java.util.HashSet<String>();
    all.add(typename.sqlType().toLowerCase());
    for (var alias : aliases) all.add(alias.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public DuckDbType<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public DuckDbType<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public DuckDbType<A> withAnalysis(AnalysisOptions opts) {
    return new DuckDbType<>(
        typename, read, write, stringifier, duckDbJson, mapSupport, opts, arrayCodec, listCodec);
  }

  /** Remove array support from this type. */
  public DuckDbType<A> noArraySupport() {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        java.util.Optional.empty(),
        listCodec);
  }

  public DuckDbType<A> withListCodec(DuckDbListCodec<A> codec) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        arrayCodec,
        java.util.Optional.of(codec));
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public DuckDbType<A> withTypename(DuckDbTypename<A> typename) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        defaultArrayCodec(read, stringifier),
        listCodec);
  }

  public DuckDbType<A> withTypename(String sqlType) {
    return withTypename(DuckDbTypename.of(sqlType));
  }

  public DuckDbType<A> renamed(String value) {
    return withTypename(typename.renamed(value));
  }

  public DuckDbType<A> renamedDropPrecision(String value) {
    return withTypename(typename.renamedDropPrecision(value));
  }

  public DuckDbType<A> withRead(DuckDbRead<A> read) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        defaultArrayCodec(read, stringifier),
        listCodec);
  }

  public DuckDbType<A> withWrite(DuckDbWrite<A> write) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        defaultArrayCodec(read, stringifier),
        listCodec);
  }

  public DuckDbType<A> withStringifier(DuckDbStringifier<A> stringifier) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
        defaultArrayCodec(read, stringifier),
        listCodec);
  }

  public DuckDbType<A> withJson(DuckDbJson<A> json) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        json,
        mapSupport,
        analysisOptions,
        defaultArrayCodec(read, stringifier),
        listCodec);
  }

  @Override
  public DuckDbType<Optional<A>> opt() {
    Function<A, Object> innerEncoder = structAttributeEncoder;
    Function<Optional<A>, Object> optEncoder =
        opt -> opt.isPresent() ? innerEncoder.apply(opt.get()) : null;
    return new DuckDbType<>(
        typename.opt(),
        read.opt(),
        write.opt(typename),
        stringifier.opt(),
        duckDbJson.opt(),
        DuckDbMapSupport.cast(),
        analysisOptions,
        java.util.Optional.empty(),
        java.util.Optional.empty(),
        optEncoder);
  }

  public DuckDbType<java.util.List<A>> list() {
    DuckDbTypename<java.util.List<A>> listTypename = typename.list();
    java.util.function.Function<Object, A> fromElem =
        listCodec
            .<java.util.function.Function<Object, A>>map(DuckDbListCodec::fromElement)
            .orElse(mapSupport::fromMap);
    java.util.function.Function<Object, java.util.List<A>> fromArray =
        raw -> {
          if (raw == null) return null;
          if (!(raw instanceof java.sql.Array arr)) {
            throw new IllegalArgumentException(
                "Expected java.sql.Array for list, got: " + raw.getClass());
          }
          try {
            Object[] elements = (Object[]) arr.getArray();
            java.util.List<A> result = new java.util.ArrayList<>(elements.length);
            for (Object elem : elements) result.add(fromElem.apply(elem));
            return result;
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        };
    DuckDbRead<java.util.List<A>> listRead =
        DuckDbRead.of(
            (rs, idx) -> {
              java.sql.Array arr = rs.getArray(idx);
              if (arr == null) return null;
              return fromArray.apply(arr);
            },
            fromArray);
    final boolean compositeListElement = typename instanceof DuckDbTypename.StructOf;
    final String listElementSqlType = typename.sqlType();
    final Function<A, Object> listElementEncoder = this.structAttributeEncoder;
    DuckDbWrite<java.util.List<A>> listWrite;
    if (compositeListElement) {
      // Composite element types need to bypass DuckDB's SQL-literal parser — same rationale as
      // compositeOf's struct write. Each element becomes a DuckDBUserStruct carried inside a
      // DuckDBUserArray, avoiding quote-mangling of nested VARCHAR[] inside struct fields.
      listWrite =
          DuckDbWrite.primitive(
              (ps, idx, list) -> {
                if (list == null) {
                  ps.setNull(idx, java.sql.Types.ARRAY);
                } else {
                  Object[] encoded = new Object[list.size()];
                  for (int i = 0; i < list.size(); i++) {
                    encoded[i] = listElementEncoder.apply(list.get(i));
                  }
                  ps.setObject(
                      idx, new org.duckdb.user.DuckDBUserArray(listElementSqlType, encoded));
                }
              });
    } else {
      listWrite =
          listCodec
              .<DuckDbWrite<java.util.List<A>>>map(
                  codec ->
                      switch (codec) {
                        case DuckDbListCodec.Native<A> n ->
                            DuckDbWrite.writeList(typename.sqlType(), n.arrayFactory());
                        case DuckDbListCodec.SqlLiteral<A> ignored ->
                            DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), stringifier);
                      })
              .orElse(
                  typename instanceof DuckDbTypename.UnionOf
                      ? DuckDbWrite.writeListInline(typename.sqlType(), stringifier)
                      : DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), stringifier));
    }
    DuckDbStringifier<java.util.List<A>> listStringifier =
        DuckDbStringifier.instance(
            (list, sb, quoted) -> {
              if (list.isEmpty()) {
                sb.append("[]");
                return;
              }
              sb.append("[");
              boolean first = true;
              for (A elem : list) {
                if (!first) sb.append(", ");
                first = false;
                stringifier.unsafeEncode(elem, sb, true);
              }
              sb.append("]");
            });
    final String elementSqlType = typename.sqlType();
    final Function<A, Object> elementEncoder = structAttributeEncoder;
    Function<java.util.List<A>, Object> listEncoder =
        list -> {
          if (list == null) return null;
          Object[] encoded = new Object[list.size()];
          for (int i = 0; i < list.size(); i++) encoded[i] = elementEncoder.apply(list.get(i));
          return new org.duckdb.user.DuckDBUserArray(elementSqlType, encoded);
        };
    return new DuckDbType<>(
        listTypename.as(),
        listRead,
        listWrite,
        listStringifier,
        duckDbJson.list(),
        DuckDbMapSupport.cast(),
        analysisOptions,
        java.util.Optional.empty(),
        java.util.Optional.empty(),
        listEncoder);
  }

  @SuppressWarnings("unchecked")
  public DuckDbType<A[]> array() {
    if (typename instanceof DuckDbTypename.ListOf || typename instanceof DuckDbTypename.ArrayOf) {
      throw new IllegalStateException(
          "Nested arrays are not supported. Cannot call .array() on " + typename.sqlType());
    }
    DuckDbArrayCodec<A> baseCodec =
        arrayCodec.orElseThrow(
            () ->
                new IllegalStateException(
                    "Array not supported for "
                        + typename.sqlType()
                        + ". This type does not provide a DuckDbArrayCodec."));
    // If the array codec didn't ship with a factory but the list codec's Native variant has one,
    // reuse it — scalar types with native list support get a typed A[] factory for free.
    DuckDbArrayCodec<A> codec = baseCodec;
    if (codec.arrayFactory().isEmpty()
        && listCodec.isPresent()
        && listCodec.get() instanceof DuckDbListCodec.Native<A> native_) {
      codec = codec.withArrayFactory(native_.arrayFactory());
    }
    final DuckDbArrayCodec<A> finalCodec = codec;
    DuckDbTypename<A[]> arrayTypename = typename.array();
    java.util.function.Function<Object, A[]> fromArrayRaw =
        raw -> {
          if (raw == null) return null;
          if (!(raw instanceof java.sql.Array arr)) {
            throw new IllegalArgumentException(
                "Expected java.sql.Array for array, got: " + raw.getClass());
          }
          try {
            Object[] elements = (Object[]) arr.getArray();
            A[] result = allocArray(finalCodec, elements.length);
            for (int i = 0; i < elements.length; i++) {
              result[i] = finalCodec.fromElement().apply(elements[i]);
            }
            return result;
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        };
    DuckDbRead<A[]> arrayRead =
        DuckDbRead.of(
            (rs, idx) -> {
              java.sql.Array arr = rs.getArray(idx);
              if (arr == null) return null;
              return fromArrayRaw.apply(arr);
            },
            fromArrayRaw);
    final Function<A, Object> elementEncoderForArray = this.structAttributeEncoder;
    final boolean compositeElement = typename instanceof DuckDbTypename.StructOf;
    DuckDbWrite<A[]> arrayWrite =
        DuckDbWrite.primitive(
            (ps, idx, arr) -> {
              if (arr == null) {
                ps.setNull(idx, java.sql.Types.ARRAY);
              } else if (compositeElement) {
                Object[] encoded = new Object[arr.length];
                for (int i = 0; i < arr.length; i++) {
                  encoded[i] = elementEncoderForArray.apply(arr[i]);
                }
                ps.setObject(idx, new org.duckdb.user.DuckDBUserArray(typename.sqlType(), encoded));
              } else {
                String[] strings = new String[arr.length];
                for (int i = 0; i < arr.length; i++) {
                  strings[i] = finalCodec.toElement().apply(arr[i]);
                }
                ps.setObject(idx, new org.duckdb.user.DuckDBUserArray(typename.sqlType(), strings));
              }
            });
    DuckDbStringifier<A[]> arrayStringifier =
        DuckDbStringifier.instance(
            (arr, sb, quoted) -> {
              if (arr.length == 0) {
                sb.append("[]");
                return;
              }
              sb.append("[");
              boolean first = true;
              for (A elem : arr) {
                if (!first) sb.append(", ");
                first = false;
                stringifier.unsafeEncode(elem, sb, true);
              }
              sb.append("]");
            });
    DuckDbJson<A[]> arrayJson =
        new DuckDbJson<>() {
          private final DuckDbJson<java.util.List<A>> listJson = duckDbJson.list();

          @Override
          public dev.typr.foundations.data.JsonValue toJson(A[] value) {
            return listJson.toJson(java.util.Arrays.asList(value));
          }

          @Override
          public A[] fromJson(dev.typr.foundations.data.JsonValue json) {
            java.util.List<A> list = listJson.fromJson(json);
            A[] result = allocArray(finalCodec, list.size());
            for (int i = 0; i < list.size(); i++) result[i] = list.get(i);
            return result;
          }
        };
    final String arraySqlType = typename.sqlType();
    final Function<A, Object> elemEncoder = structAttributeEncoder;
    Function<A[], Object> arrayEncoder =
        arr -> {
          if (arr == null) return null;
          Object[] encoded = new Object[arr.length];
          for (int i = 0; i < arr.length; i++) encoded[i] = elemEncoder.apply(arr[i]);
          return new org.duckdb.user.DuckDBUserArray(arraySqlType, encoded);
        };
    return new DuckDbType<>(
        arrayTypename,
        arrayRead,
        arrayWrite,
        arrayStringifier,
        arrayJson,
        DuckDbMapSupport.cast(),
        analysisOptions.arrayForms(),
        java.util.Optional.empty(),
        java.util.Optional.empty(),
        arrayEncoder);
  }

  public <V> DuckDbType<java.util.Map<A, V>> mapTo(DuckDbType<V> valueType) {
    DuckDbTypename<java.util.Map<A, V>> mapTypename = typename.mapTo(valueType.typename);
    String sqlType = mapTypename.sqlType();
    DuckDbRead<java.util.Map<A, V>> mapRead =
        DuckDbRead.readMapWithSupport(mapSupport, valueType.mapSupport);
    DuckDbWrite<java.util.Map<A, V>> mapWrite =
        DuckDbWrite.writeMapViaSqlLiteral(sqlType, stringifier, valueType.stringifier);
    DuckDbStringifier<java.util.Map<A, V>> mapStringifier =
        DuckDbStringifier.instance(
            (map, sb, quoted) -> {
              if (map.isEmpty()) {
                sb.append("{}");
                return;
              }
              sb.append("{");
              boolean first = true;
              for (var entry : map.entrySet()) {
                if (!first) sb.append(", ");
                first = false;
                stringifier.unsafeEncode(entry.getKey(), sb, true);
                sb.append(": ");
                valueType.stringifier.unsafeEncode(entry.getValue(), sb, true);
              }
              sb.append("}");
            });
    return new DuckDbType<>(
        mapTypename,
        mapRead,
        mapWrite,
        mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(),
        analysisOptions,
        java.util.Optional.empty(),
        java.util.Optional.empty()); // MAP[] not supported by DuckDB JDBC
  }

  public <V> DuckDbType<java.util.Map<A, V>> mapToNative(
      DuckDbType<V> valueType, Class<A> keyClass, Class<V> valueClass) {
    DuckDbTypename<java.util.Map<A, V>> mapTypename = typename.mapTo(valueType.typename);
    String sqlType = mapTypename.sqlType();
    DuckDbRead<java.util.Map<A, V>> mapRead =
        DuckDbRead.readMap(read, keyClass, valueType.read, valueClass);
    DuckDbWrite<java.util.Map<A, V>> mapWrite = DuckDbWrite.writeMap(sqlType);
    DuckDbStringifier<java.util.Map<A, V>> mapStringifier =
        DuckDbStringifier.instance(
            (map, sb, quoted) -> {
              if (map.isEmpty()) {
                sb.append("{}");
                return;
              }
              sb.append("{");
              boolean first = true;
              for (var entry : map.entrySet()) {
                if (!first) sb.append(", ");
                first = false;
                stringifier.unsafeEncode(entry.getKey(), sb, true);
                sb.append(": ");
                valueType.stringifier.unsafeEncode(entry.getValue(), sb, true);
              }
              sb.append("}");
            });
    return new DuckDbType<>(
        mapTypename,
        mapRead,
        mapWrite,
        mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(),
        analysisOptions,
        java.util.Optional.empty(),
        java.util.Optional.empty()); // MAP[] not supported by DuckDB JDBC
  }

  public <V> DuckDbType<java.util.Map<A, V>> mapToViaSqlLiteral(
      DuckDbType<V> valueType,
      Class<A> keyClass,
      Class<V> valueClass,
      DuckDbStringifier<A> keyStringifier,
      DuckDbStringifier<V> valueStringifier) {
    DuckDbTypename<java.util.Map<A, V>> mapTypename = typename.mapTo(valueType.typename);
    String sqlType = mapTypename.sqlType();
    DuckDbRead<java.util.Map<A, V>> mapRead =
        DuckDbRead.readMap(read, keyClass, valueType.read, valueClass);
    DuckDbWrite<java.util.Map<A, V>> mapWrite =
        DuckDbWrite.writeMapViaSqlLiteral(sqlType, keyStringifier, valueStringifier);
    DuckDbStringifier<java.util.Map<A, V>> mapStringifier =
        DuckDbStringifier.instance(
            (map, sb, quoted) -> {
              if (map.isEmpty()) {
                sb.append("{}");
                return;
              }
              sb.append("{");
              boolean first = true;
              for (var entry : map.entrySet()) {
                if (!first) sb.append(", ");
                first = false;
                keyStringifier.unsafeEncode(entry.getKey(), sb, true);
                sb.append(": ");
                valueStringifier.unsafeEncode(entry.getValue(), sb, true);
              }
              sb.append("}");
            });
    return new DuckDbType<>(
        mapTypename,
        mapRead,
        mapWrite,
        mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(),
        analysisOptions,
        java.util.Optional.empty(),
        java.util.Optional.empty()); // MAP[] not supported by DuckDB JDBC
  }

  public <B> DuckDbType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    Function<A, B> fUnchecked =
        a -> {
          try {
            return f.apply(a);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        };
    return new DuckDbType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        stringifier.contramap(g),
        duckDbJson.transform(f, g),
        mapSupport.transform(fUnchecked, g),
        analysisOptions,
        arrayCodec.map(c -> c.map(fUnchecked, g)),
        listCodec.map(c -> c.map(fUnchecked, g)));
  }

  @Override
  public <B> DuckDbType<B> to(Bijection<A, B> bijection) {
    return new DuckDbType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        stringifier.contramap(bijection::from),
        duckDbJson.transform(bijection::underlying, bijection::from),
        mapSupport.transform(bijection::underlying, bijection::from),
        analysisOptions,
        arrayCodec.map(c -> c.map(bijection::underlying, bijection::from)),
        listCodec.map(c -> c.map(bijection::underlying, bijection::from)));
  }

  /** Default arrayCodec derived from read + stringifier. */
  static <A> java.util.Optional<DuckDbArrayCodec<A>> defaultArrayCodec(
      DuckDbRead<A> r, DuckDbStringifier<A> s) {
    return java.util.Optional.of(DuckDbArrayCodec.fromReadAndStringifier(r, s));
  }

  /**
   * Allocate an A[] for {@link #array()} reads. Uses the codec's typed {@code arrayFactory} when
   * present; otherwise falls back to a raw {@code Object[]} cast that only works for scalars
   * handled via generic erasure. Composite types must supply a factory via
   * {@link DuckDbArrayCodec#withArrayFactory}.
   */
  @SuppressWarnings("unchecked")
  private static <A> A[] allocArray(DuckDbArrayCodec<A> codec, int length) {
    if (codec.arrayFactory().isPresent()) {
      return codec.arrayFactory().get().apply(length);
    }
    return (A[]) new Object[length];
  }

  public static <A> DuckDbType<A> of(
      String tpe, DuckDbRead<A> r, DuckDbWrite<A> w, DuckDbStringifier<A> s, DuckDbJson<A> j) {
    return new DuckDbType<>(
        DuckDbTypename.of(tpe),
        r,
        w,
        s,
        j,
        DuckDbMapSupport.cast(),
        AnalysisOptions.EMPTY,
        defaultArrayCodec(r, s),
        java.util.Optional.empty());
  }

  public static <A> DuckDbType<A> of(
      DuckDbTypename<A> typename,
      DuckDbRead<A> r,
      DuckDbWrite<A> w,
      DuckDbStringifier<A> s,
      DuckDbJson<A> j) {
    return new DuckDbType<>(
        typename,
        r,
        w,
        s,
        j,
        DuckDbMapSupport.cast(),
        AnalysisOptions.EMPTY,
        defaultArrayCodec(r, s),
        java.util.Optional.empty());
  }
}
