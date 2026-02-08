package dev.typr.foundations;

import dev.typr.foundations.analysis.AnalysisOptions;
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
    AnalysisOptions analysisOptions)
    implements DbType<A> {

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
    if (aliases.isEmpty()) return Set.of(typename.sqlType().toLowerCase());
    var all = new java.util.HashSet<>(aliases);
    all.add(typename.sqlType().toLowerCase());
    return Set.copyOf(all);
  }

  public DuckDbType<A> unchecked() { return withAnalysis(analysisOptions.withUnchecked()); }
  public DuckDbType<A> nullableOk() { return withAnalysis(analysisOptions.withNullableOk()); }
  public DuckDbType<A> withAnalysis(AnalysisOptions opts) {
    return new DuckDbType<>(typename, read, write, stringifier, duckDbJson, mapSupport, opts);
  }

  public Fragment.Value<A> encode(A value) {
    return new Fragment.Value<>(value, this);
  }

  public DuckDbType<A> withTypename(DuckDbTypename<A> typename) {
    return new DuckDbType<>(typename, read, write, stringifier, duckDbJson, mapSupport, analysisOptions);
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
    return new DuckDbType<>(typename, read, write, stringifier, duckDbJson, mapSupport, analysisOptions);
  }

  public DuckDbType<A> withWrite(DuckDbWrite<A> write) {
    return new DuckDbType<>(typename, read, write, stringifier, duckDbJson, mapSupport, analysisOptions);
  }

  public DuckDbType<A> withStringifier(DuckDbStringifier<A> stringifier) {
    return new DuckDbType<>(typename, read, write, stringifier, duckDbJson, mapSupport, analysisOptions);
  }

  public DuckDbType<A> withJson(DuckDbJson<A> json) {
    return new DuckDbType<>(typename, read, write, stringifier, json, mapSupport, analysisOptions);
  }

  @Override
  public DuckDbType<Optional<A>> opt() {
    return new DuckDbType<>(
        typename.opt(), read.opt(), write.opt(typename), stringifier.opt(), duckDbJson.opt(),
        DuckDbMapSupport.cast(),
        analysisOptions);
  }

  @SuppressWarnings("unchecked")
  public DuckDbType<A[]> array() {
    DuckDbTypename<A[]> arrayTypename = typename.array();
    DuckDbRead<A[]> arrayRead =
        DuckDbRead.of(
            (rs, idx) -> {
              java.sql.Array arr = rs.getArray(idx);
              if (arr == null) return null;
              Object[] elements = (Object[]) arr.getArray();
              A[] result = (A[]) new Object[elements.length];
              for (int i = 0; i < elements.length; i++) {
                result[i] = (A) elements[i];
              }
              return result;
            });
    DuckDbWrite<A[]> arrayWrite =
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), stringifier)
            .contramap(arr -> java.util.Arrays.asList(arr));
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
          @SuppressWarnings("unchecked")
          public A[] fromJson(dev.typr.foundations.data.JsonValue json) {
            java.util.List<A> list = listJson.fromJson(json);
            return (A[]) list.toArray();
          }
        };
    return new DuckDbType<>(arrayTypename, arrayRead, arrayWrite, arrayStringifier, arrayJson,
        DuckDbMapSupport.cast(), analysisOptions);
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
        mapTypename, mapRead, mapWrite, mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public DuckDbType<java.util.List<A>> listNative(
      Class<A> elementClass, java.util.function.IntFunction<A[]> toArray) {
    DuckDbTypename<java.util.List<A>> listTypename = typename.list();
    DuckDbRead<java.util.List<A>> listRead = DuckDbRead.readList(elementClass);
    DuckDbWrite<java.util.List<A>> listWrite = DuckDbWrite.writeList(typename.sqlType(), toArray);
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
    return new DuckDbType<>(listTypename, listRead, listWrite, listStringifier, duckDbJson.list(),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public DuckDbType<java.util.List<A>> listViaSqlLiteral(
      Class<A> elementClass, DuckDbStringifier<A> elementStringifier) {
    DuckDbTypename<java.util.List<A>> listTypename = typename.list();
    DuckDbRead<java.util.List<A>> listRead = DuckDbRead.readList(elementClass);
    DuckDbWrite<java.util.List<A>> listWrite =
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), elementStringifier);
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
                elementStringifier.unsafeEncode(elem, sb, true);
              }
              sb.append("]");
            });
    return new DuckDbType<>(listTypename, listRead, listWrite, listStringifier, duckDbJson.list(),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public <W> DuckDbType<java.util.List<A>> listViaSqlLiteral(
      Class<W> wireClass,
      java.util.function.Function<W, A> wireToElement,
      DuckDbStringifier<A> elementStringifier) {
    DuckDbTypename<java.util.List<A>> listTypename = typename.list();
    DuckDbRead<java.util.List<A>> listRead = DuckDbRead.readListConverted(wireToElement);
    DuckDbWrite<java.util.List<A>> listWrite =
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), elementStringifier);
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
                elementStringifier.unsafeEncode(elem, sb, true);
              }
              sb.append("]");
            });
    return new DuckDbType<>(listTypename, listRead, listWrite, listStringifier, duckDbJson.list(),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public DuckDbType<java.util.List<A>> arrayNative(
      int size, Class<A> elementClass, java.util.function.IntFunction<A[]> toArray) {
    DuckDbTypename<java.util.List<A>> arrayTypename = new DuckDbTypename.ArrayOf<>(typename, size);
    DuckDbRead<java.util.List<A>> arrayRead = DuckDbRead.readList(elementClass);
    DuckDbWrite<java.util.List<A>> arrayWrite = DuckDbWrite.writeList(typename.sqlType(), toArray);
    DuckDbStringifier<java.util.List<A>> arrayStringifier =
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
    return new DuckDbType<>(
        arrayTypename, arrayRead, arrayWrite, arrayStringifier, duckDbJson.list(),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public DuckDbType<java.util.List<A>> arrayViaSqlLiteral(
      int size, Class<A> elementClass, DuckDbStringifier<A> elementStringifier) {
    DuckDbTypename<java.util.List<A>> arrayTypename = new DuckDbTypename.ArrayOf<>(typename, size);
    DuckDbRead<java.util.List<A>> arrayRead = DuckDbRead.readList(elementClass);
    DuckDbWrite<java.util.List<A>> arrayWrite =
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), elementStringifier);
    DuckDbStringifier<java.util.List<A>> arrayStringifier =
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
                elementStringifier.unsafeEncode(elem, sb, true);
              }
              sb.append("]");
            });
    return new DuckDbType<>(
        arrayTypename, arrayRead, arrayWrite, arrayStringifier, duckDbJson.list(),
        DuckDbMapSupport.cast(), analysisOptions);
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
        mapTypename, mapRead, mapWrite, mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(), analysisOptions);
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
        mapTypename, mapRead, mapWrite, mapStringifier,
        DuckDbTypes.mapJson(duckDbJson, valueType.duckDbJson),
        DuckDbMapSupport.cast(), analysisOptions);
  }

  public <B> DuckDbType<B> bimap(SqlFunction<A, B> f, Function<B, A> g) {
    return new DuckDbType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        stringifier.contramap(g),
        duckDbJson.bimap(f, g),
        mapSupport.bimap(
            a -> {
              try {
                return f.apply(a);
              } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
              }
            },
            g),
        analysisOptions);
  }

  @Override
  public <B> DuckDbType<B> to(Bijection<A, B> bijection) {
    return new DuckDbType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        stringifier.contramap(bijection::from),
        duckDbJson.bimap(bijection::underlying, bijection::from),
        mapSupport.bimap(bijection::underlying, bijection::from),
        analysisOptions);
  }

  public static <A> DuckDbType<A> of(
      String tpe, DuckDbRead<A> r, DuckDbWrite<A> w, DuckDbStringifier<A> s, DuckDbJson<A> j) {
    return new DuckDbType<>(DuckDbTypename.of(tpe), r, w, s, j,
        DuckDbMapSupport.cast(), AnalysisOptions.EMPTY);
  }

  public static <A> DuckDbType<A> of(
      DuckDbTypename<A> typename,
      DuckDbRead<A> r,
      DuckDbWrite<A> w,
      DuckDbStringifier<A> s,
      DuckDbJson<A> j) {
    return new DuckDbType<>(typename, r, w, s, j,
        DuckDbMapSupport.cast(), AnalysisOptions.EMPTY);
  }
}
