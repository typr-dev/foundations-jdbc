package dev.typr.foundations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Combines DuckDB type name, read, write, stringification, and JSON encoding for a type. Similar to
 * PgType but for DuckDB. Note: DuckDB doesn't support text-based streaming inserts via JDBC (like
 * PostgreSQL's COPY), so there is no DuckDbText component.
 *
 * <p>Every type also carries two pre-built parameters for composition: {@link
 * #structAttributeEncoder} — how a value of this type encodes as a wire attribute when it sits
 * inside a composite ({@code DuckDBUserStruct} attribute, {@code DuckDBUserArray} element, {@code
 * DuckDBMap} entry) — and {@link #listWrite} — the {@link DuckDbWrite} for a {@code List<A>} column
 * when values of this type are the elements. Both are set at construction time so {@link #list} /
 * {@link #array} / {@code compositeOf} / {@code mapTo} never need to introspect the typename.
 */
public record DuckDbType<A>(
    DuckDbTypename<A> typename,
    DuckDbRead<A> read,
    DuckDbWrite<A> write,
    DuckDbStringifier<A> stringifier,
    DuckDbJson<A> duckDbJson,
    AnalysisOptions analysisOptions,
    DuckDbWrite<List<A>> listWrite,
    Function<A, Object> structAttributeEncoder)
    implements DbType<A> {

  public DuckDbType<A> withStructAttributeEncoder(Function<A, Object> encoder) {
    return new DuckDbType<>(
        typename, read, write, stringifier, duckDbJson, analysisOptions, listWrite, encoder);
  }

  public DuckDbType<A> withListWrite(DuckDbWrite<List<A>> listWrite) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        analysisOptions,
        listWrite,
        structAttributeEncoder);
  }

  /**
   * Replace the list-write strategy by describing the element codec. Native codecs use JDBC's
   * {@code createArrayOf} fast path; SQL-literal codecs cast a text array literal on the way in.
   */
  public DuckDbType<A> withListCodec(DuckDbListCodec<A> codec) {
    String sqlType = typename.sqlType();
    DuckDbWrite<List<A>> newListWrite =
        switch (codec) {
          case DuckDbListCodec.Native<A> n -> DuckDbWrite.writeList(sqlType, n.arrayFactory());
          case DuckDbListCodec.SqlLiteral<A> ignored ->
              DuckDbWrite.writeListViaSqlLiteral(sqlType, stringifier);
        };
    return withListWrite(newListWrite);
  }

  @Override
  public Optional<DbOutParam<A>> outParam() {
    return Optional.empty();
  }

  @Override
  public boolean isNullable() {
    return typename.isNullable();
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

  @Override
  public String toString() {
    return "DuckDB(" + typename + ")";
  }

  public DuckDbType<A> unchecked() {
    return withAnalysis(analysisOptions.withUnchecked());
  }

  public DuckDbType<A> nullableOk() {
    return withAnalysis(analysisOptions.withNullableOk());
  }

  public DuckDbType<A> withAnalysis(AnalysisOptions opts) {
    return new DuckDbType<>(
        typename, read, write, stringifier, duckDbJson, opts, listWrite, structAttributeEncoder);
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
        analysisOptions,
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), stringifier),
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
        analysisOptions,
        listWrite,
        structAttributeEncoder);
  }

  public DuckDbType<A> withWrite(DuckDbWrite<A> write) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        analysisOptions,
        listWrite,
        structAttributeEncoder);
  }

  public DuckDbType<A> withStringifier(DuckDbStringifier<A> stringifier) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        duckDbJson,
        analysisOptions,
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), stringifier),
        structAttributeEncoder);
  }

  public DuckDbType<A> withJson(DuckDbJson<A> json) {
    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        json,
        analysisOptions,
        listWrite,
        structAttributeEncoder);
  }

  @Override
  public DuckDbType<Optional<A>> opt() {
    Function<A, Object> innerEncoder = structAttributeEncoder;
    Function<Optional<A>, Object> optEncoder = opt -> opt.map(innerEncoder).orElse(null);
    DuckDbStringifier<Optional<A>> optStringifier = stringifier.opt();
    return new DuckDbType<>(
        typename.opt(),
        read.opt(),
        write.opt(typename),
        optStringifier,
        duckDbJson.opt(),
        analysisOptions,
        DuckDbWrite.writeListViaSqlLiteral(typename.opt().sqlType(), optStringifier),
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
   * Shared machinery behind {@link #list()} and {@link #array(int)}. The element's own {@link
   * #listWrite} drives binding of a single list value; the resulting list type carries a {@link
   * DuckDBUserArray}-based listWrite of its own so a second {@link #list}/{@link #array} call
   * (list-of-list) composes without dispatch.
   */
  private DuckDbType<List<A>> buildCollection(
      DuckDbTypename<List<A>> collectionTypename,
      java.util.function.Function<DuckDbTypename<?>, DuckDbTypename<?>> wrapAlias) {
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
            for (Object elem : elements) result.add(this.read.fromJdbcValue(elem));
            return result;
          } catch (java.sql.SQLException e) {
            throw new DatabaseException.Jdbc(e);
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

    // The element's own listWrite IS the write for this list type. No dispatch here — whichever
    // strategy (SQL literal / Native setArray / DuckDBUserArray / inline-SQL for UNION) the
    // element carries is the correct one by construction.
    DuckDbWrite<List<A>> collWrite = this.listWrite;

    final String elementSqlType = typename.sqlType();
    final Function<A, Object> elementEncoder = this.structAttributeEncoder;

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

    // The list type itself is a composite — when nested further ({@code .list().list()}), the
    // outer layer needs the DuckDBUserArray path keyed on this list's sqlType.
    DuckDbWrite<List<List<A>>> outerListWrite =
        DuckDbWrite.writeListOfUserArray(collectionTypename.sqlType(), collEncoder);

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
        collOpts,
        outerListWrite,
        collEncoder);
  }

  /** Build a {@code MAP(A, V)} DuckDB type. See {@link DuckDbMapSupport} for the impl details. */
  public <V> DuckDbType<Map<A, V>> mapTo(DuckDbType<V> valueType) {
    return DuckDbMapSupport.mapType(this, valueType);
  }

  public <B> DuckDbType<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    DuckDbStringifier<B> newStringifier = stringifier.contramap(g);
    Function<A, Object> innerEncoder = structAttributeEncoder;
    return new DuckDbType<>(
        typename.as(),
        read.map(f),
        write.contramap(g),
        newStringifier,
        duckDbJson.transform(f, g),
        analysisOptions,
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), newStringifier),
        b -> innerEncoder.apply(g.apply(b)));
  }

  @Override
  public <B> DuckDbType<B> to(Bijection<A, B> bijection) {
    DuckDbStringifier<B> newStringifier = stringifier.contramap(bijection::from);
    Function<A, Object> innerEncoder = structAttributeEncoder;
    return new DuckDbType<>(
        typename.as(),
        read.map(bijection::underlying),
        write.contramap(bijection::from),
        newStringifier,
        duckDbJson.transform(bijection::underlying, bijection::from),
        analysisOptions,
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), newStringifier),
        b -> innerEncoder.apply(bijection.from(b)));
  }

  public static <A> DuckDbType<A> of(
      String tpe, DuckDbRead<A> r, DuckDbWrite<A> w, DuckDbStringifier<A> s, DuckDbJson<A> j) {
    return of(DuckDbTypename.of(tpe), r, w, s, j);
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
        AnalysisOptions.EMPTY,
        DuckDbWrite.writeListViaSqlLiteral(typename.sqlType(), s),
        // Scalar default: identity. The Java value is what DuckDB's JNI binds directly in
        // DuckDBUserStruct attribute / DuckDBUserArray element / DuckDBMap entry position.
        // Scalars whose native Java representation the driver doesn't accept (UUID, Duration,
        // LocalTime, Json, BigInteger, …) override explicitly with {@code
        // withStructAttributeEncoder(stringifier.asWireEncoder())}.
        a -> a);
  }
}
