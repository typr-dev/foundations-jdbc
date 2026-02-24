package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public sealed interface Fragment {
  Fragment EMPTY = of("");

  default String render() {
    StringBuilder sb = new StringBuilder();
    render(sb);
    return sb.toString();
  }

  void render(StringBuilder sb);

  default String renderInterpolated() {
    StringBuilder sb = new StringBuilder();
    renderInterpolated(sb);
    return sb.toString();
  }

  default void renderInterpolated(StringBuilder sb) {
    render(sb);
  }

  default void set(PreparedStatement stmt) throws SQLException {
    set(stmt, new AtomicInteger(1));
  }

  void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException;

  /**
   * Extract all parameter types from this fragment.
   * Traverses the fragment tree and collects DbType instances from all Value nodes.
   * The types are returned in the order they appear in the SQL (left to right).
   *
   * <p>Used by query analysis to compare declared types against JDBC metadata.
   *
   * @return list of DbType instances for all parameters
   */
  default List<DbType<?>> parameterTypes() {
    List<DbType<?>> types = new ArrayList<>();
    collectParameterTypes(types);
    return types;
  }

  /**
   * Collect parameter types into the provided list.
   * Called by parameterTypes() to traverse the fragment tree.
   */
  void collectParameterTypes(List<DbType<?>> types);

  @SuppressWarnings("unchecked")
  default Fragment fill(Iterator<Object> values) {
    return switch (this) {
      case Param<?> p -> new Value<>(values.next(), (DbType<Object>) p.type());
      case Append a -> new Append(a.a().fill(values), a.b().fill(values));
      case Concat c -> new Concat(c.frags().stream().map(f -> f.fill(values)).toList());
      case Optionally ignored -> throw new UnsupportedOperationException(
          "Optionally nodes must be resolved via OptionallyResolver.resolve(), not fill()");
      default -> this;
    };
  }

  static int countParams(Fragment fragment) {
    return switch (fragment) {
      case Param<?> p -> 1;
      case Append a -> countParams(a.a()) + countParams(a.b());
      case Concat c -> c.frags().stream().mapToInt(Fragment::countParams).sum();
      case Optionally o -> throw new IllegalArgumentException("Cannot count params of nested Optionally");
      default -> 0;
    };
  }

  /**
   * Collect the JDBC parameter positions (1-based) of all {@link Param} nodes in this fragment.
   * Used by batch operations to set parameters directly on the PreparedStatement without
   * rebuilding the fragment tree for each row.
   *
   * @return array of 1-based JDBC parameter positions for Param nodes
   */
  default int[] paramPositions() {
    List<Integer> positions = new ArrayList<>();
    collectParamPositions(new AtomicInteger(1), positions);
    return positions.stream().mapToInt(Integer::intValue).toArray();
  }

  default void collectParamPositions(AtomicInteger idx, List<Integer> positions) {
    switch (this) {
      case Value<?> v -> idx.getAndIncrement();
      case Param<?> p -> positions.add(idx.getAndIncrement());
      case Append a -> {
        a.a().collectParamPositions(idx, positions);
        a.b().collectParamPositions(idx, positions);
      }
      case Concat c -> {
        for (Fragment f : c.frags()) f.collectParamPositions(idx, positions);
      }
      default -> {}
    }
  }

  default Fragment append(Fragment other) {
    return new Append(this, other);
  }

  default <T> Operation.Query<T> query(ResultSetParser<T> parser) {
    return new Operation.Query<>(this, parser);
  }

  default <T> Operation.Query<T> queryExactlyOne(DbType<T> type) {
    return query(RowCodec.of(type).exactlyOne());
  }

  default <T> Operation.Query<T> queryExactlyOne(RowCodec<T> codec) {
    return query(codec.exactlyOne());
  }

  default <T> Operation.Query<List<T>> queryAll(DbType<T> type) {
    return query(RowCodec.of(type).all());
  }

  default <T> Operation.Query<List<T>> queryAll(RowCodec<T> codec) {
    return query(codec.all());
  }

  default <T> Operation.Query<java.util.Optional<T>> queryMaxOne(DbType<T> type) {
    return query(RowCodec.of(type).maxOne());
  }

  default <T> Operation.Query<java.util.Optional<T>> queryMaxOne(RowCodec<T> codec) {
    return query(codec.maxOne());
  }

  default Operation.Update update() {
    return new Operation.Update(this);
  }

  /** Same as {@link #update()}, but ignores the number of rows changed. */
  default Operation<Void> execute() {
    return update().voided();
  }

  default <T> Operation.UpdateReturning<T> updateReturning(ResultSetParser<T> parser) {
    return new Operation.UpdateReturning<>(this, parser);
  }

  default <T> Operation.UpdateReturningGeneratedKeys<T> updateReturningGeneratedKeys(
      String[] columnNames, ResultSetParser<T> parser) {
    return new Operation.UpdateReturningGeneratedKeys<>(this, columnNames, parser);
  }

  default <Row> Operation.UpdateMany<Row> updateMany(RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateMany<>(this, codec, rows);
  }

  default <Row> Operation.UpdateManyReturning<Row> updateManyReturning(
      RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateManyReturning<>(this, codec, rows);
  }

  default <Row> Operation.UpdateReturningEach<Row> updateReturningEach(
      RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateReturningEach<>(this, codec, rows);
  }

  record Literal(String value) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append(value);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {}

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      // Literals have no parameters
    }
  }

  static Literal of(String value) {
    return new Literal(value);
  }

  static <Row> RowTemplate.Update<Row> insertInto(String table, RowCodecNamed<Row> codec, String... except) {
    Set<String> excludeSet = except.length > 0 ? Set.of(except) : Set.of();
    List<String> names = codec.columnNames();
    List<String> included = new ArrayList<>();
    for (String name : names) {
      if (!excludeSet.contains(name)) included.add(name);
    }
    String cols = String.join(", ", included);
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(codec, except)
        .append(")")
        .update();
  }

  static <Row> RowTemplate.Query<Row, Row> insertIntoReturning(String table, RowCodecNamed<Row> codec, String... except) {
    Set<String> excludeSet = except.length > 0 ? Set.of(except) : Set.of();
    List<String> names = codec.columnNames();
    List<String> included = new ArrayList<>();
    for (String name : names) {
      if (!excludeSet.contains(name)) included.add(name);
    }
    String cols = String.join(", ", included);
    String allCols = String.join(", ", names);
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(codec, except)
        .append(") RETURNING " + allCols)
        .query(codec.exactlyOne());
  }

  static <In, Out> RowTemplate.Query<In, Out> insertIntoReturning(
      String table, RowCodecNamed<In> writeCodec, RowCodecNamed<Out> readCodec) {
    String cols = String.join(", ", writeCodec.columnNames());
    String returnCols = String.join(", ", readCodec.columnNames());
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(writeCodec)
        .append(") RETURNING " + returnCols)
        .query(readCodec.exactlyOne());
  }

  static Fragment empty() {
    return EMPTY;
  }

  static Literal quotedDouble(String value) {
    return new Literal('"' + value + '"');
  }

  static Literal quotedSingle(String value) {
    return new Literal("'" + value + "'");
  }

  record Append(Fragment a, Fragment b) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      a.render(sb);
      b.render(sb);
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      a.renderInterpolated(sb);
      b.renderInterpolated(sb);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      a.set(stmt, idx);
      b.set(stmt, idx);
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      a.collectParameterTypes(types);
      b.collectParameterTypes(types);
    }
  }

  record Value<A>(A value, DbType<A> type) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append(type.typename().renderPlaceholder());
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      if (value == null) {
        sb.append("NULL");
        return;
      }
      if (value instanceof java.util.Optional<?> opt) {
        if (opt.isEmpty()) {
          sb.append("NULL");
          return;
        }
        renderInterpolatedValue(sb, opt.get());
        return;
      }
      renderInterpolatedValue(sb, value);
    }

    private void renderInterpolatedValue(StringBuilder sb, Object val) {
      String sqlType = type.typename().sqlType().toLowerCase();
      if (isQuotedType(sqlType)) {
        sb.append('\'');
        sb.append(val.toString().replace("'", "''"));
        sb.append('\'');
      } else {
        sb.append(val);
      }
    }

    private static boolean isQuotedType(String sqlType) {
      return sqlType.contains("char")
          || sqlType.contains("text")
          || sqlType.contains("varchar")
          || sqlType.contains("uuid")
          || sqlType.contains("date")
          || sqlType.contains("time")
          || sqlType.contains("json")
          || sqlType.contains("xml")
          || sqlType.contains("bytea")
          || sqlType.contains("blob")
          || sqlType.contains("clob")
          || sqlType.contains("enum")
          || sqlType.contains("interval");
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      type.write().set(stmt, idx.getAndIncrement(), value);
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      types.add(type);
    }
  }

  static <A> Value<A> value(A value, DbType<A> type) {
    return new Value<>(value, type);
  }

  /** Encode a value into a SQL fragment using the provided database type. */
  static <A> Fragment encode(DbType<A> type, A value) {
    return new Value<>(value, type);
  }

  record Param<A>(DbType<A> type) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append(type.typename().renderPlaceholder());
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      idx.getAndIncrement();
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      types.add(type);
    }
  }

  record Optionally(Fragment inner, int innerParamCount) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      inner.render(sb);
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      inner.renderInterpolated(sb);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      throw new UnsupportedOperationException(
          "Optionally nodes must be resolved via OptionallyResolver before execution");
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      // Optionally nodes do not collect inner params — managed by Template layer
    }

    @Override
    public void collectParamPositions(AtomicInteger idx, List<Integer> positions) {
      throw new UnsupportedOperationException(
          "Optionally nodes must be resolved via OptionallyResolver before execution");
    }
  }

  record Concat(List<? extends Fragment> frags) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      for (Fragment frag : frags) {
        frag.render(sb);
      }
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      for (Fragment frag : frags) {
        frag.renderInterpolated(sb);
      }
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      for (Fragment frag : frags) {
        frag.set(stmt, idx);
      }
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      for (Fragment frag : frags) {
        frag.collectParameterTypes(types);
      }
    }
  }

  /** Returns `(f1 AND f2 AND ... fn)`. */
  static Fragment and(Fragment... fs) {
    return and(Arrays.asList(fs));
  }

  /** Returns `(f1 AND f2 AND ... fn)` for a non-empty collection. */
  static Fragment and(List<? extends Fragment> fs) {
    if (fs.isEmpty()) return EMPTY;
    else return join(fs, of(" AND "));
  }

  /** Returns `(f1 OR f2 OR ... fn)`. */
  static Fragment or(Fragment... fs) {
    return or(Arrays.asList(fs));
  }

  /** Returns `(f1 OR f2 OR ... fn)` */
  static Fragment or(List<? extends Fragment> fs) {
    if (fs.isEmpty()) return EMPTY;
    else return join(fs, of(" OR "));
  }

  /** Returns `WHERE f1 AND f2 AND ... fn` */
  static Fragment whereAnd(Fragment... fs) {
    return whereAnd(Arrays.asList(fs));
  }

  /** Returns `WHERE f1 AND f2 AND ... fn` */
  static Fragment whereAnd(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return of("WHERE ").append(and(fs));
    }
  }

  /** Returns `WHERE f1 OR f2 OR ... fn`. */
  static Fragment whereOr(Fragment... fs) {
    return whereOr(Arrays.asList(fs));
  }

  /** Returns `WHERE f1 OR f2 OR ... fn`. */
  static Fragment whereOr(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return of("WHERE ").append(or(fs));
    }
  }

  /** Returns `SET f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment set(Fragment... fs) {
    return set(Arrays.asList(fs));
  }

  /** Returns `SET f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment set(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return of("SET ").append(comma(fs));
    }
  }

  /** Returns `(f)`. */
  static Fragment parentheses(Fragment f) {
    return of("(").append(f).append(of(")"));
  }

  /** Returns `f1, f2, ... fn`. */
  static Fragment comma(Fragment... fs) {
    return comma(Arrays.asList(fs));
  }

  /** Returns `f1, f2, ... fn`. */
  static Fragment comma(List<? extends Fragment> fs) {
    return join(fs, of(", "));
  }

  /** Returns `ORDER BY f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment orderBy(Fragment... fs) {
    return orderBy(Arrays.asList(fs));
  }

  /** Returns `ORDER BY f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment orderBy(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return of("ORDER BY ").append(comma(fs));
    }
  }

  static Concat join(List<? extends Fragment> fs, Fragment sep) {
    var list = new ArrayList<Fragment>();
    var first = true;
    for (Fragment f : fs) {
      if (!first) {
        list.add(sep);
      }
      list.add(f);
      first = false;
    }
    return new Concat(list);
  }

  static Concat concat(Fragment... fs) {
    return new Concat(Arrays.asList(fs));
  }

  default Fragment append(String s) {
    return append(new Literal(s));
  }

  default <T> Fragment value(DbType<T> type, T value) {
    return append(new Value<>(value, type));
  }

  default Fragment appendAll(List<? extends Fragment> fragments, Fragment separator) {
    return append(join(fragments, separator));
  }

  default <P0> ParamBuilders.ParamBuilder1<P0> param(DbType<P0> type) {
    return new ParamBuilders.ParamBuilder1<>(append(new Param<>(type)), type);
  }

  default ParamBuilders.ParamBuilder1<Boolean> optionally(Fragment inner) {
    int n = countParams(inner);
    if (n != 0) throw new IllegalArgumentException(
        "optionally(Fragment) requires 0 inner params, got " + n + ". Use optionally(ParamBuilder) for parameterized fragments.");
    return new ParamBuilders.ParamBuilder1<>(append(new Optionally(inner, 0)), null);
  }

  @SuppressWarnings("unchecked")
  default <A> ParamBuilders.ParamBuilder1<java.util.Optional<A>> optionally(ParamBuilders.ParamBuilder1<A> builder) {
    Fragment inner = builder.done();
    return new ParamBuilders.ParamBuilder1<>(append(new Optionally(inner, countParams(inner))), null);
  }

  @SuppressWarnings("unchecked")
  default <A, B> ParamBuilders.ParamBuilder1<java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilders.ParamBuilder2<A, B> builder) {
    Fragment inner = builder.done();
    return new ParamBuilders.ParamBuilder1<>(append(new Optionally(inner, countParams(inner))), null);
  }

  @SuppressWarnings("unchecked")
  default <A, B, C> ParamBuilders.ParamBuilder1<java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilders.ParamBuilder3<A, B, C> builder) {
    Fragment inner = builder.done();
    return new ParamBuilders.ParamBuilder1<>(append(new Optionally(inner, countParams(inner))), null);
  }

  default <Row> RowParamBuilder<Row> paramRow(RowCodecNamed<Row> codec, String... except) {
    List<DbType<?>> types = codec.columns();
    List<String> names = codec.columnNames();
    Set<String> exceptSet = except.length > 0 ? Set.of(except) : Set.of();
    List<Fragment> fragments = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < types.size(); i++) {
      if (exceptSet.contains(names.get(i))) continue;
      fragments.add(new Param<>(types.get(i)));
      indices.add(i);
    }
    int[] includedIndices = indices.stream().mapToInt(Integer::intValue).toArray();
    return new RowParamBuilder<>(append(Fragment.comma(fragments)), codec, includedIndices);
  }

  @SuppressWarnings("unchecked")
  default <Row> Fragment row(RowCodecNamed<Row> codec, Row row, String... except) {
    Object[] values = codec.encode().apply(row);
    List<DbType<?>> types = codec.columns();
    List<String> names = codec.columnNames();
    Set<String> exceptSet = except.length > 0 ? Set.of(except) : Set.of();
    List<Fragment> fragments = new ArrayList<>();
    for (int i = 0; i < types.size(); i++) {
      if (exceptSet.contains(names.get(i))) continue;
      fragments.add(Fragment.value(values[i], (DbType<Object>) types.get(i)));
    }
    return append(Fragment.comma(fragments));
  }

  static Fragment of(Fragment... fragments) {
    return new Concat(Arrays.asList(fragments));
  }
}
