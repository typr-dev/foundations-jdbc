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
   * Extract all parameter types from this fragment. Traverses the fragment tree and collects DbType
   * instances from all Value nodes. The types are returned in the order they appear in the SQL
   * (left to right).
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
   * Collect parameter types into the provided list. Called by parameterTypes() to traverse the
   * fragment tree.
   */
  void collectParameterTypes(List<DbType<?>> types);

  /**
   * Visitor for collecting typed parameter values from a fragment tree. Used by pipelining
   * implementations to serialize parameters without going through PreparedStatement.
   */
  @FunctionalInterface
  interface ParamCollector {
    <A> void accept(A value, DbType<A> type);
  }

  /**
   * Traverse the fragment tree and call the collector for each bound parameter value. This provides
   * typed access to parameter values and their DbType, enabling direct serialization (e.g., to PG
   * wire protocol text format via PgText.wireEncode) without going through the untyped
   * PreparedStatement interface.
   */
  void collectParams(ParamCollector collector);

  @SuppressWarnings("unchecked")
  default Fragment fill(Iterator<Object> values) {
    return switch (this) {
      case Param<?> p -> new Value<>(values.next(), (DbType<Object>) p.type());
      case Append a -> new Append(a.a().fill(values), a.b().fill(values));
      case Concat c -> new Concat(c.frags().stream().map(f -> f.fill(values)).toList());
      case Branch b -> new Branch(b.variants(), b.execution().fill(values));
      case Optionally ignored ->
          throw new UnsupportedOperationException(
              "Optionally nodes must be resolved via OptionallyResolver.resolve(), not fill()");
      default -> this;
    };
  }

  static int countParams(Fragment fragment) {
    return switch (fragment) {
      case Param<?> p -> 1;
      case Append a -> countParams(a.a()) + countParams(a.b());
      case Concat c -> c.frags().stream().mapToInt(Fragment::countParams).sum();
      case Branch b -> countParams(b.execution());
      case Optionally o ->
          throw new IllegalArgumentException("Cannot count params of nested Optionally");
      default -> 0;
    };
  }

  /**
   * Collect the JDBC parameter positions (1-based) of all {@link Param} nodes in this fragment.
   * Used by batch operations to set parameters directly on the PreparedStatement without rebuilding
   * the fragment tree for each row.
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
      case Branch b -> b.execution().collectParamPositions(idx, positions);
      default -> {}
    }
  }

  default Fragment append(Fragment other) {
    return new Append(this, other);
  }

  // ========== Conditional append DSL ==========

  /**
   * Begin a conditional append driven by an {@link java.util.Optional} value.
   *
   * <pre>{@code
   * Fragment.of("SELECT * FROM orders WHERE 1=1")
   *     .optionally(optName).append(" AND name LIKE ", PgTypes.text)
   *     .optionally(optPrice).append(" AND price < ", PgTypes.decimal)
   *     .optionally(onlyActive).append(" AND active = TRUE")
   *     .optionally(ascending).append(" ORDER BY id ASC", " ORDER BY id DESC")
   *     .query(codec.all());
   * }</pre>
   *
   * <p>Each {@code .optionally().append()} creates a branch point that Query Analysis expands —
   * all possible SQL shapes are checked against the database, not just the one that happens
   * to execute at runtime.
   */
  default <T> OptionallyValue<T> optionally(java.util.Optional<T> value) {
    return new OptionallyValue<>(this, value);
  }

  /**
   * Begin a conditional append driven by a boolean flag.
   * The returned {@link OptionallyFlag} lets you include/skip SQL or choose between two alternatives.
   */
  default OptionallyFlag optionally(boolean condition) {
    return new OptionallyFlag(this, condition);
  }

  /**
   * Conditional append with a value to bind. Returned by
   * {@link Fragment#optionally(java.util.Optional)}.
   * The value MUST be rendered into the SQL via {@link #append(String, DbType)}.
   */
  record OptionallyValue<T>(Fragment base, java.util.Optional<T> value) {

    /** Append SQL with the bound value when present, or nothing when absent. */
    public Fragment append(String sql, DbType<T> type) {
      Fragment paramFragment = Fragment.of(sql).param(type).done();
      Fragment execution = value
          .<Fragment>map(v -> Fragment.of(sql).value(type, v))
          .orElse(EMPTY);
      return base.append(new Branch(List.of(paramFragment, EMPTY), execution));
    }

    /** Append SQL with the bound value when present, or the alternative SQL when absent. */
    public Fragment append(String sql, DbType<T> type, String whenAbsent) {
      Fragment paramFragment = Fragment.of(sql).param(type).done();
      Fragment alt = Fragment.of(whenAbsent);
      Fragment execution = value
          .<Fragment>map(v -> Fragment.of(sql).value(type, v))
          .orElse(alt);
      return base.append(new Branch(List.of(paramFragment, alt), execution));
    }
  }

  /**
   * Conditional append driven by a boolean flag. Returned by {@link Fragment#optionally(boolean)}.
   * Include or skip SQL, or choose between two alternatives.
   */
  record OptionallyFlag(Fragment base, boolean condition) {

    /** Append SQL when true, or nothing when false. */
    public Fragment append(String sql) {
      Fragment inner = Fragment.of(sql);
      return base.append(new Branch(
          List.of(inner, EMPTY),
          condition ? inner : EMPTY));
    }

    /** Append fragment when true, or nothing when false. */
    public Fragment append(Fragment fragment) {
      return base.append(new Branch(
          List.of(fragment, EMPTY),
          condition ? fragment : EMPTY));
    }

    /** Choose between two SQL strings. */
    public Fragment append(String whenTrue, String whenFalse) {
      Fragment a = Fragment.of(whenTrue);
      Fragment b = Fragment.of(whenFalse);
      return base.append(new Branch(
          List.of(a, b),
          condition ? a : b));
    }

    /** Choose between two fragments. */
    public Fragment append(Fragment whenTrue, Fragment whenFalse) {
      return base.append(new Branch(
          List.of(whenTrue, whenFalse),
          condition ? whenTrue : whenFalse));
    }
  }

  default <T> OperationRead.Query<T> query(ResultSetParser<T> parser) {
    return new OperationRead.Query<>(this, parser);
  }

  default <T> OperationRead.Query<T> queryExactlyOne(DbType<T> type) {
    return query(RowCodec.of(type).exactlyOne());
  }

  default <T> OperationRead.Query<T> queryExactlyOne(RowCodec<T> codec) {
    return query(codec.exactlyOne());
  }

  default <T> OperationRead.Query<List<T>> queryAll(DbType<T> type) {
    return query(RowCodec.of(type).all());
  }

  default <T> OperationRead.Query<List<T>> queryAll(RowCodec<T> codec) {
    return query(codec.all());
  }

  default <T> OperationRead.Query<java.util.Optional<T>> queryMaxOne(DbType<T> type) {
    return query(RowCodec.of(type).maxOne());
  }

  default <T> OperationRead.Query<java.util.Optional<T>> queryMaxOne(RowCodec<T> codec) {
    return query(codec.maxOne());
  }

  default Operation.Update update() {
    return new Operation.Update(this);
  }

  /**
   * Execute this fragment using {@code stmt.execute()}, which works for all SQL statement types.
   */
  default Operation.Execute execute() {
    return new Operation.Execute(this);
  }

  default <T> Operation.UpdateReturning<T> updateReturning(ResultSetParser<T> parser) {
    return new Operation.UpdateReturning<>(this, parser);
  }

  default <T> Operation.UpdateReturningGeneratedKeys<T> updateReturningGeneratedKeys(
      String[] columnNames, ResultSetParser<T> parser) {
    return new Operation.UpdateReturningGeneratedKeys<>(this, columnNames, parser);
  }

  default <Row> Operation.UpdateMany<Row> updateMany(RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateMany<Row>(this, codec, rows);
  }

  default <Row> Operation.UpdateManyReturning<Row> updateManyReturning(
      RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateManyReturning<>(this, codec, rows);
  }

  default <Row> Operation.UpdateReturningEach<Row> updateReturningEach(
      RowCodec<Row> codec, Iterator<Row> rows) {
    return new Operation.UpdateReturningEach<>(this, codec, rows);
  }

  default <T> OperationRead.Streaming<T> streamingQuery(RowCodec<T> codec, int fetchSize) {
    return new OperationRead.Streaming<>(this, codec, fetchSize);
  }

  default <T> OperationRead.Streaming<T> streamingQuery(DbType<T> type, int fetchSize) {
    return new OperationRead.Streaming<>(this, RowCodec.of(type), fetchSize);
  }

  record Literal(String value) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append(value);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {}

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {}

    @Override
    public void collectParams(ParamCollector collector) {}
  }

  static Literal of(String value) {
    return new Literal(value);
  }

  /**
   * Empty starting point for the fluent builder pattern — equivalent to {@code Fragment.of("")}.
   * Use when your first call is {@code .value(...)} or {@code .append(...)} and there is no leading
   * SQL literal.
   */
  static Literal builder() {
    return new Literal("");
  }

  static <Row> RowParamBuilder<Row> insertInto(
      String table, RowCodecNamed<Row> codec, String... except) {
    return of("INSERT INTO " + table + " (" + columnList(codec, except) + ") VALUES (")
        .paramRow(codec, except)
        .append(")");
  }

  /** PostgreSQL/DuckDB: INSERT ... RETURNING all columns. */
  static <Row> RowParamBuilder<Row> insertIntoReturning(
      String table, RowCodecNamed<Row> codec, String... except) {
    String allCols = String.join(", ", codec.columnNames());
    return of("INSERT INTO " + table + " (" + columnList(codec, except) + ") VALUES (")
        .paramRow(codec, except)
        .append(") RETURNING " + allCols);
  }

  /**
   * INSERT ... RETURNING with a separate read codec. Use {@code .updateReturning(row,
   * readCodec.exactlyOne())} at the call site — the read codec's columns are baked into the
   * RETURNING clause here.
   */
  static <In> RowParamBuilder<In> insertIntoReturning(
      String table, RowCodecNamed<In> writeCodec, RowCodecNamed<?> readCodec) {
    String cols = String.join(", ", writeCodec.columnNames());
    String returnCols = String.join(", ", readCodec.columnNames());
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(writeCodec)
        .append(") RETURNING " + returnCols);
  }

  /**
   * PostgreSQL: INSERT ... ON CONFLICT (conflictColumns) DO UPDATE SET ... Returns an update
   * template that performs an upsert. All columns except the conflict columns are included in the
   * SET clause.
   *
   * <pre>{@code
   * var upsert = Fragment.upsert("users", userCodec, "email");
   * executor.execute(upsert.on(new User("alice", "alice@example.com", 30)));
   * }</pre>
   */
  static <Row> RowParamBuilder<Row> upsert(
      String table, RowCodecNamed<Row> codec, String... conflictColumns) {
    if (conflictColumns.length == 0)
      throw new IllegalArgumentException("At least one conflict column required");
    String cols = String.join(", ", codec.columnNames());
    String conflict = String.join(", ", conflictColumns);
    Set<String> conflictSet = Set.of(conflictColumns);
    List<String> updateCols = new ArrayList<>();
    for (String name : codec.columnNames()) {
      if (!conflictSet.contains(name)) {
        updateCols.add(name + " = EXCLUDED." + name);
      }
    }
    String setClauses = String.join(", ", updateCols);
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(codec)
        .append(") ON CONFLICT (" + conflict + ") DO UPDATE SET " + setClauses);
  }

  /**
   * PostgreSQL: INSERT ... ON CONFLICT (conflictColumns) DO NOTHING. Inserts the row only if no
   * conflict exists. Returns the number of affected rows (0 or 1).
   */
  static <Row> RowParamBuilder<Row> insertIgnore(
      String table, RowCodecNamed<Row> codec, String... conflictColumns) {
    if (conflictColumns.length == 0)
      throw new IllegalArgumentException("At least one conflict column required");
    String cols = String.join(", ", codec.columnNames());
    String conflict = String.join(", ", conflictColumns);
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(codec)
        .append(") ON CONFLICT (" + conflict + ") DO NOTHING");
  }

  /**
   * PostgreSQL: INSERT ... ON CONFLICT ... DO UPDATE SET ... RETURNING. Combines upsert with
   * returning the resulting row.
   */
  static <Row> RowParamBuilder<Row> upsertReturning(
      String table, RowCodecNamed<Row> codec, String... conflictColumns) {
    if (conflictColumns.length == 0)
      throw new IllegalArgumentException("At least one conflict column required");
    String cols = String.join(", ", codec.columnNames());
    String conflict = String.join(", ", conflictColumns);
    Set<String> conflictSet = Set.of(conflictColumns);
    List<String> updateCols = new ArrayList<>();
    for (String name : codec.columnNames()) {
      if (!conflictSet.contains(name)) {
        updateCols.add(name + " = EXCLUDED." + name);
      }
    }
    String setClauses = String.join(", ", updateCols);
    return of("INSERT INTO " + table + " (" + cols + ") VALUES (")
        .paramRow(codec)
        .append(
            ") ON CONFLICT (" + conflict + ") DO UPDATE SET " + setClauses + " RETURNING " + cols);
  }

  private static String columnList(RowCodecNamed<?> codec, String... except) {
    Set<String> excludeSet = except.length > 0 ? Set.of(except) : Set.of();
    List<String> names = codec.columnNames();
    List<String> included = new ArrayList<>();
    for (String name : names) {
      if (!excludeSet.contains(name)) included.add(name);
    }
    return String.join(", ", included);
  }

  static Fragment empty() {
    return EMPTY;
  }

  /**
   * Emit {@code DROP TABLE IF EXISTS <table>}. Works on PostgreSQL, DuckDB, MariaDB, MySQL, SQL
   * Server (2016+), Oracle (23c+) and DB2 (11.5.4+).
   *
   * <p>On DB2 older than 11.5.4 (no native {@code IF EXISTS} support), wrap the plain {@code DROP
   * TABLE <table>} in a try/catch for SQLSTATE 42704 ("undefined name").
   *
   * @param table the unqualified or schema-qualified table name
   */
  static Operation.Execute dropTableIfExists(String table) {
    return new Operation.Execute(of("DROP TABLE IF EXISTS " + table));
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

    @Override
    public void collectParams(ParamCollector collector) {
      a.collectParams(collector);
      b.collectParams(collector);
    }
  }

  record Value<A>(A value, DbType<A> type) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      var inline = type.write().inlineSql(value);
      if (inline.isPresent()) {
        sb.append(inline.get());
      } else {
        sb.append(type.typename().renderPlaceholder());
      }
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      if (type.isRedacted()) {
        sb.append("<redacted>");
        return;
      }
      var inline = type.write().inlineSql(value);
      if (inline.isPresent()) {
        sb.append(inline.get());
        return;
      }
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
      if (type.write().inlineSql(value).isEmpty()) {
        type.write().set(stmt, idx.getAndIncrement(), value);
      }
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      if (type.write().inlineSql(value).isEmpty()) {
        types.add(type);
      }
    }

    @Override
    public void collectParams(ParamCollector collector) {
      if (type.write().inlineSql(value).isEmpty()) {
        collector.accept(value, type);
      }
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

    @Override
    public void collectParams(ParamCollector collector) {
      // Param is an unbound parameter hole — no value to collect
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
      // Optionally nodes do not collect inner params — managed by OptionallyResolver
    }

    @Override
    public void collectParamPositions(AtomicInteger idx, List<Integer> positions) {
      throw new UnsupportedOperationException(
          "Optionally nodes must be resolved via OptionallyResolver before execution");
    }

    @Override
    public void collectParams(ParamCollector collector) {
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

    @Override
    public void collectParams(ParamCollector collector) {
      for (Fragment frag : frags) {
        frag.collectParams(collector);
      }
    }
  }

  /**
   * A conditional branch point in the fragment tree. At execution time, only the {@code execution}
   * fragment is rendered and bound. At analysis time, {@link OptionallyResolver#analysisVariants}
   * expands all {@code variants} to check every possible SQL shape.
   *
   * <p>Created via the {@link #when} DSL. Users don't construct this directly.
   *
   * @param variants  all possible SQL shapes (for QA to check)
   * @param execution the concrete fragment to use at runtime
   */
  record Branch(List<Fragment> variants, Fragment execution) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      execution.render(sb);
    }

    @Override
    public void renderInterpolated(StringBuilder sb) {
      execution.renderInterpolated(sb);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      execution.set(stmt, idx);
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      execution.collectParameterTypes(types);
    }

    @Override
    public void collectParams(ParamCollector collector) {
      execution.collectParams(collector);
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

  /**
   * Returns {@code (?, ?, ...)} with each element bound as a typed parameter. Useful for {@code IN}
   * clauses on dialects without native array types (MariaDB, SQL Server, Oracle, pre-23c; DB2):
   * {@code Fragment.of("WHERE id IN ").append(Fragment.valuesList(MariaTypes.int_, ids))}.
   *
   * <p>On PostgreSQL/DuckDB prefer the array idiom: {@code .value(int4.array(), ids)} with {@code =
   * ANY(?)}.
   *
   * @throws IllegalArgumentException if {@code values} is empty — an empty {@code IN()} is
   *     SQL-invalid, so the caller must branch (e.g. return no rows without issuing the query).
   */
  static <A> Fragment valuesList(DbType<A> type, Iterable<? extends A> values) {
    var parts = new java.util.ArrayList<Fragment>();
    for (A v : values) parts.add(Fragment.value(v, type));
    if (parts.isEmpty()) {
      throw new IllegalArgumentException(
          "Fragment.valuesList requires at least one value — an empty IN() clause is SQL-invalid. "
              + "Branch on the caller side (return empty list without issuing the query).");
    }
    return parentheses(comma(parts));
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
