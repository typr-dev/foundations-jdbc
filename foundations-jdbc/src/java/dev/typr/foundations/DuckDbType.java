package dev.typr.foundations;

import java.util.List;
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
    Optional<DuckDbListCodec<A>> listCodec,
    Function<A, Object> structAttributeEncoder)
    implements DbType<A> {

  /**
   * Convenience constructor that defaults the struct-attribute encoder to identity. Safe for scalar
   * types whose Java representation is directly bindable as a {@link
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
      Optional<DuckDbListCodec<A>> listCodec) {
    this(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        analysisOptions,
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
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        mapSupport,
        opts,
        listCodec,
        structAttributeEncoder);
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
        Optional.of(codec),
        structAttributeEncoder);
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
        listCodec,
        structAttributeEncoder);
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
        listCodec,
        structAttributeEncoder);
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
        listCodec,
        structAttributeEncoder);
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
        listCodec,
        structAttributeEncoder);
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
        listCodec,
        structAttributeEncoder);
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
        Optional.empty(),
        optEncoder);
  }

  /**
   * Variable-length LIST of this element type. DuckDB renders as {@code T[]}. Every row can have a
   * different number of elements.
   */
  public DuckDbType<List<A>> list() {
    return buildCollection(typename.list(), DuckDbTypename::list);
  }

  /**
   * Fixed-size ARRAY of this element type with the given cardinality. DuckDB renders as {@code
   * T[size]} and enforces that every row has exactly {@code size} elements. Use this for embedding
   * vectors and similar dense, fixed-shape tensors.
   */
  public DuckDbType<List<A>> array(int size) {
    return buildCollection(typename.array(size), t -> t.array(size));
  }

  /**
   * Shared machinery behind {@link #list()} and {@link #array(int)}. Both emit a {@link
   * org.duckdb.user.DuckDBUserArray} with per-element-encoded values; DuckDB's column type decides
   * whether it's a variable-length LIST or a fixed-length ARRAY.
   */
  private DuckDbType<List<A>> buildCollection(
      DuckDbTypename<List<A>> collectionTypename,
      java.util.function.Function<DuckDbTypename<?>, DuckDbTypename<?>> wrapAlias) {
    final DuckDbRead<A> elemRead = this.read;
    java.util.function.Function<Object, A> fromElem = elemRead::fromJdbcValue;
    java.util.function.Function<Object, List<A>> fromArray =
        raw -> {
          if (raw == null) return null;
          if (!(raw instanceof java.sql.Array arr)) {
            throw new IllegalArgumentException(
                "Expected java.sql.Array for list/array, got: " + raw.getClass());
          }
          try {
            Object[] elements = (Object[]) arr.getArray();
            List<A> result = new java.util.ArrayList<>(elements.length);
            for (Object elem : elements) result.add(fromElem.apply(elem));
            return result;
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        };
    DuckDbRead<List<A>> collRead =
        DuckDbRead.of(
            (rs, idx) -> {
              java.sql.Array arr = rs.getArray(idx);
              if (arr == null) return null;
              return fromArray.apply(arr);
            },
            fromArray);

    final boolean nestedCollection =
        typename instanceof DuckDbTypename.StructOf
            || typename instanceof DuckDbTypename.ListOf
            || typename instanceof DuckDbTypename.ArrayOf;
    final String elementSqlType = typename.sqlType();
    final Function<A, Object> elementEncoder = this.structAttributeEncoder;
    DuckDbWrite<List<A>> collWrite;
    if (nestedCollection) {
      // Composite / nested-collection element types bypass DuckDB's SQL-literal parser — each
      // element is bound via its structAttributeEncoder and carried inside a DuckDBUserArray.
      // Required to avoid quote-mangling of nested VARCHAR[] inside struct fields and to support
      // arbitrary nesting of LIST/ARRAY.
      collWrite =
          DuckDbWrite.primitive(
              (ps, idx, list) -> {
                if (list == null) {
                  ps.setNull(idx, java.sql.Types.ARRAY);
                } else {
                  Object[] encoded = new Object[list.size()];
                  for (int i = 0; i < list.size(); i++) {
                    encoded[i] = elementEncoder.apply(list.get(i));
                  }
                  ps.setObject(idx, new org.duckdb.user.DuckDBUserArray(elementSqlType, encoded));
                }
              });
    } else {
      collWrite =
          listCodec
              .<DuckDbWrite<List<A>>>map(
                  codec ->
                      switch (codec) {
                        case DuckDbListCodec.Native<A> n ->
                            DuckDbWrite.writeList(elementSqlType, n.arrayFactory());
                        case DuckDbListCodec.SqlLiteral<A> ignored ->
                            DuckDbWrite.writeListViaSqlLiteral(elementSqlType, stringifier);
                      })
              .orElse(
                  typename instanceof DuckDbTypename.UnionOf
                      ? DuckDbWrite.writeListInline(elementSqlType, stringifier)
                      : DuckDbWrite.writeListViaSqlLiteral(elementSqlType, stringifier));
    }

    DuckDbStringifier<List<A>> collStringifier =
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

    Function<List<A>, Object> collEncoder =
        list -> {
          if (list == null) return null;
          Object[] encoded = new Object[list.size()];
          for (int i = 0; i < list.size(); i++) encoded[i] = elementEncoder.apply(list.get(i));
          return new org.duckdb.user.DuckDBUserArray(elementSqlType, encoded);
        };

    AnalysisOptions collOpts;
    if (analysisOptions.vendorTypeNames().isEmpty()) {
      collOpts = analysisOptions;
    } else {
      var wrapped = new java.util.HashSet<DbTypename<?>>();
      for (var alias : analysisOptions.vendorTypeNames()) {
        if (alias instanceof DuckDbTypename<?> duck) {
          wrapped.add(wrapAlias.apply(duck));
        } else {
          wrapped.add(alias);
        }
      }
      collOpts =
          new AnalysisOptions(
              Set.copyOf(wrapped), analysisOptions.nullableOk(), analysisOptions.unchecked());
    }

    return new DuckDbType<>(
        collectionTypename,
        collRead,
        collWrite,
        collStringifier,
        duckDbJson.list(),
        DuckDbMapSupport.cast(),
        collOpts,
        Optional.empty(),
        collEncoder);
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
        Optional.empty());
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
        Optional.empty());
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
        Optional.empty());
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
        listCodec.map(c -> c.map(bijection::underlying, bijection::from)));
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
        Optional.empty());
  }

  public static <A> DuckDbType<A> of(
      DuckDbTypename<A> typename,
      DuckDbRead<A> r,
      DuckDbWrite<A> w,
      DuckDbStringifier<A> s,
      DuckDbJson<A> j) {
    return new DuckDbType<>(
        typename, r, w, s, j, DuckDbMapSupport.cast(), AnalysisOptions.EMPTY, Optional.empty());
  }
}
