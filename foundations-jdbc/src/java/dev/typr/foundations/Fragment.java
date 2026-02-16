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
      default -> this;
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

  default <Row> Operation.UpdateMany<Row> updateMany(RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateMany<>(this, parser, rows);
  }

  default <Row> Operation.UpdateManyReturning<Row> updateManyReturning(
      RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateManyReturning<>(this, parser, rows);
  }

  default <Row> Operation.UpdateReturningEach<Row> updateReturningEach(
      RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateReturningEach<>(this, parser, rows);
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

  record Concat(List<? extends Fragment> frags) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      for (Fragment frag : frags) {
        frag.render(sb);
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

  default <Row> RowParamBuilder<Row> paramRow(RowParserNamed<Row> parser, String... except) {
    List<DbType<?>> types = parser.columns();
    List<String> names = parser.columnNames();
    Set<String> exceptSet = except.length > 0 ? Set.of(except) : Set.of();
    List<Fragment> fragments = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < types.size(); i++) {
      if (exceptSet.contains(names.get(i))) continue;
      fragments.add(new Param<>(types.get(i)));
      indices.add(i);
    }
    int[] includedIndices = indices.stream().mapToInt(Integer::intValue).toArray();
    return new RowParamBuilder<>(append(Fragment.comma(fragments)), parser, includedIndices);
  }

  @SuppressWarnings("unchecked")
  default <Row> Fragment row(RowParserNamed<Row> parser, Row row, String... except) {
    Object[] values = parser.encode().apply(row);
    List<DbType<?>> types = parser.columns();
    List<String> names = parser.columnNames();
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
