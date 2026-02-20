package dev.typr.foundations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import org.jetbrains.annotations.Nullable;

public sealed interface Operation<Out> extends Analyzable
    permits Operation.Query,
        Operation.Update,
        Operation.UpdateReturning,
        Operation.UpdateReturningGeneratedKeys,
        Operation.UpdateManyReturning,
        Operation.UpdateMany,
        Operation.UpdateReturningEach,
        Operation.UpdateManyTemplate,
        Operation.StreamingCopy,
        Operation.Mapped,
        Operation.Pure,
        Operation.With,
        Operation.IfEmpty,
        Operation.Then,
        Operation.Configured,
        Procedure.ProcedureCall,
        Procedure.FunctionCall {
  Out runChecked(Connection conn) throws SQLException;

  default Out run(Connection conn) {
    try {
      return runChecked(conn);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Execute this operation using the given transactor.
   *
   * @param transactor the transactor to use
   * @return the operation result
   * @throws SQLException if a database error occurs
   */
  default Out transact(Transactor transactor) throws SQLException {
    return transactor.execute(this);
  }

  default <B> Operation<B> map(SqlFunction<Out, B> f) {
    return new Mapped<>(this, f);
  }

  default <B> Operation<And<Out, B>> with(Operation<B> other) {
    return new With<>(this, other);
  }

  default <B, R> Operation<R> with(Operation<B> other, BiFunction<Out, B, R> combine) {
    return with(other).map(and -> combine.apply(and.left(), and.right()));
  }

  default <B, C, R> Operation<R> with(
      Operation<B> b, Operation<C> c, Functions.Function3<Out, B, C, R> combine) {
    return with(b)
        .with(c)
        .map(
            and ->
                combine.apply(and.left().left(), and.left().right(), and.right()));
  }

  default <B, C, D, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Functions.Function4<Out, B, C, D, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .map(
            and ->
                combine.apply(
                    and.left().left().left(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B, C, D, E, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Functions.Function5<Out, B, C, D, E, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .with(e)
        .map(
            and ->
                combine.apply(
                    and.left().left().left().left(),
                    and.left().left().left().right(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B, C, D, E, F, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Functions.Function6<Out, B, C, D, E, F, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .with(e)
        .with(f)
        .map(
            and ->
                combine.apply(
                    and.left().left().left().left().left(),
                    and.left().left().left().left().right(),
                    and.left().left().left().right(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B, C, D, E, F, G, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Functions.Function7<Out, B, C, D, E, F, G, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .with(e)
        .with(f)
        .with(g)
        .map(
            and ->
                combine.apply(
                    and.left().left().left().left().left().left(),
                    and.left().left().left().left().left().right(),
                    and.left().left().left().left().right(),
                    and.left().left().left().right(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B, C, D, E, F, G, H, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h,
      Functions.Function8<Out, B, C, D, E, F, G, H, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .with(e)
        .with(f)
        .with(g)
        .with(h)
        .map(
            and ->
                combine.apply(
                    and.left().left().left().left().left().left().left(),
                    and.left().left().left().left().left().left().right(),
                    and.left().left().left().left().left().right(),
                    and.left().left().left().left().right(),
                    and.left().left().left().right(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B, C, D, E, F, G, H, I, R> Operation<R> with(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h,
      Operation<I> i,
      Functions.Function9<Out, B, C, D, E, F, G, H, I, R> combine) {
    return with(b)
        .with(c)
        .with(d)
        .with(e)
        .with(f)
        .with(g)
        .with(h)
        .with(i)
        .map(
            and ->
                combine.apply(
                    and.left().left().left().left().left().left().left().left(),
                    and.left().left().left().left().left().left().left().right(),
                    and.left().left().left().left().left().left().right(),
                    and.left().left().left().left().left().right(),
                    and.left().left().left().left().right(),
                    and.left().left().left().right(),
                    and.left().left().right(),
                    and.left().right(),
                    and.right()));
  }

  default <B> Operation<Out> thenIgnore(Operation<B> other) {
    return with(other).map(and -> and.left());
  }

  default <B> Operation<B> then(Template<Out, B> next) {
    return new Then<>(this, java.util.function.Function.identity(), next);
  }

  default Operation<Void> voided() {
    return map(ignored -> null);
  }

  default Operation<Out> named(String name) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), name, c.timeout(), c.listener());
    }
    return new Configured<>(this, name, null, null);
  }

  default Operation<Out> timeout(Duration timeout) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), timeout, c.listener());
    }
    return new Configured<>(this, null, timeout, null);
  }

  default Operation<Out> withListener(QueryListener listener) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), c.timeout(), listener);
    }
    return new Configured<>(this, null, null, listener);
  }

  static <T> Operation<T> pure(T value) {
    return new Pure<>(value);
  }

  static <T> Operation<List<T>> sequence(List<Operation<T>> ops) {
    if (ops.isEmpty()) return pure(List.of());
    Operation<List<T>> result = ops.getFirst().map(List::of);
    for (int i = 1; i < ops.size(); i++) {
      result =
          result
              .with(ops.get(i))
              .map(
                  and -> {
                    var list = new ArrayList<>(and.left());
                    list.add(and.right());
                    return Collections.unmodifiableList(list);
                  });
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  static Operation<Void> allOf(Operation<?>... ops) {
    if (ops.length == 0) return pure(null);
    Operation<Void> result = ((Operation<Object>) ops[0]).voided();
    for (int i = 1; i < ops.length; i++) {
      result = result.thenIgnore((Operation<Object>) ops[i]);
    }
    return result;
  }

  static <T> Operation<T> ifEmpty(
      Operation<Optional<T>> check, Operation<T> fallback) {
    return new IfEmpty<>(check, fallback);
  }

  record Query<Out>(Fragment query, ResultSetParser<Out> parser) implements Operation<Out> {
    @Override
    public Out runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          try (ResultSet rs = stmt.executeQuery()) {
            return parser.apply(rs);
          }
        }
      });
    }
  }

  record Update(Fragment query) implements Operation<Integer> {
    @Override
    public Integer runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          return stmt.executeUpdate();
        }
      });
    }
  }

  record UpdateReturning<Out>(Fragment query, ResultSetParser<Out> parser)
      implements Operation<Out> {
    @Override
    public Out runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          try (ResultSet rs = stmt.executeQuery()) {
            return parser.apply(rs);
          }
        }
      });
    }
  }

  record UpdateReturningGeneratedKeys<Out>(
      Fragment query, String[] columnNames, ResultSetParser<Out> parser) implements Operation<Out> {
    @Override
    public Out runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt = conn.prepareStatement(sql, columnNames)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          stmt.executeUpdate();
          try (ResultSet rs = stmt.getGeneratedKeys()) {
            return parser.apply(rs);
          }
        }
      });
    }
  }

  record UpdateMany<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<int[]> {
    @Override
    public int[] runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          while (rows.hasNext()) {
            Row row = rows.next();
            codec.writeRow(stmt, row);
            stmt.addBatch();
          }
          return stmt.executeBatch();
        }
      });
    }
  }

  record UpdateManyReturning<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<List<Row>> {
    @Override
    public List<Row> runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        try (PreparedStatement stmt =
            conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
          Instrumentation.applyTimeout(stmt, conn);
          query.set(stmt);
          while (rows.hasNext()) {
            Row row = rows.next();
            codec.writeRow(stmt, row);
            stmt.addBatch();
          }
          stmt.executeBatch();
          try (ResultSet rs = stmt.getGeneratedKeys()) {
            return codec.all().apply(rs);
          }
        }
      });
    }
  }

  /**
   * Executes each row individually with RETURNING clause. Used for MariaDB where batch mode with
   * RETURNING doesn't work properly via getGeneratedKeys(). Each INSERT/UPDATE is executed
   * separately and the RETURNING result is read from executeQuery().
   */
  record UpdateReturningEach<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<List<Row>> {
    @Override
    public List<Row> runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(query.render(), conn);
      return Instrumentation.instrumented(conn, query, sql, () -> {
        java.util.ArrayList<Row> results = new java.util.ArrayList<>();
        while (rows.hasNext()) {
          Row row = rows.next();
          try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Instrumentation.applyTimeout(stmt, conn);
            query.set(stmt);
            codec.writeRow(stmt, row);
            try (ResultSet rs = stmt.executeQuery()) {
              results.addAll(codec.all().apply(rs));
            }
          }
        }
        return results;
      });
    }
  }

  /**
   * Batch executes a row-parameterized template. Unlike UpdateMany (which writes all codec fields),
   * this only writes the fields specified by includedIndices, matching the Param holes in the fragment.
   * Created by RowTemplate.Update.onMany().
   *
   * <p>Parameter positions and types are computed once from the fragment tree before the loop.
   * Each row is then written directly to the PreparedStatement without rebuilding the fragment.
   */
  record UpdateManyTemplate<Row>(
      Fragment fragment,
      RowCodecNamed<Row> codec,
      int[] includedIndices,
      Iterator<Row> rows)
      implements Operation<int[]> {
    @SuppressWarnings("unchecked")
    @Override
    public int[] runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(fragment.render(), conn);
      return Instrumentation.instrumented(conn, fragment, sql, () -> {
        int[] paramPositions = fragment.paramPositions();
        List<DbType<?>> allCols = codec.columns();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          Instrumentation.applyTimeout(stmt, conn);
          while (rows.hasNext()) {
            Row row = rows.next();
            fragment.set(stmt);
            Object[] encoded = codec.encode().apply(row);
            for (int i = 0; i < includedIndices.length; i++) {
              DbType<Object> type = (DbType<Object>) allCols.get(includedIndices[i]);
              type.write().set(stmt, paramPositions[i], encoded[includedIndices[i]]);
            }
            stmt.addBatch();
          }
          return stmt.executeBatch();
        }
      });
    }
  }

  record StreamingCopy<Row>(
      String copyCommand, int batchSize, Iterator<Row> rows, PgText<Row> text)
      implements Operation<Long> {
    @Override
    public Long runChecked(Connection conn) throws SQLException {
      String sql = Instrumentation.applyName(copyCommand, conn);
      Fragment syntheticFragment = Fragment.of(copyCommand);
      return Instrumentation.instrumented(conn, syntheticFragment, sql, () ->
          streamingInsert.insert(copyCommand, batchSize, rows, conn, text));
    }
  }

  record Mapped<A, B>(Operation<A> source, SqlFunction<A, B> f) implements Operation<B> {
    @Override
    public B runChecked(Connection conn) throws SQLException {
      return f.apply(source.runChecked(conn));
    }
  }

  record Pure<T>(T value) implements Operation<T> {
    @Override
    public T runChecked(Connection conn) {
      return value;
    }
  }

  record With<A, B>(Operation<A> first, Operation<B> second) implements Operation<And<A, B>> {
    @Override
    public And<A, B> runChecked(Connection conn) throws SQLException {
      return new And<>(first.runChecked(conn), second.runChecked(conn));
    }
  }

  record IfEmpty<T>(Operation<Optional<T>> check, Operation<T> fallback)
      implements Operation<T> {
    @Override
    public T runChecked(Connection conn) throws SQLException {
      Optional<T> result = check.runChecked(conn);
      return result.isPresent() ? result.get() : fallback.runChecked(conn);
    }
  }

  record Then<A, In, B>(
      Operation<A> source,
      java.util.function.Function<A, In> extract,
      Template<In, B> continuation)
      implements Operation<B> {
    @Override
    public B runChecked(Connection conn) throws SQLException {
      A a = source.runChecked(conn);
      In in = extract.apply(a);
      return continuation.on(in).runChecked(conn);
    }
  }

  record Configured<Out>(
      Operation<Out> inner,
      @Nullable String name,
      @Nullable Duration timeout,
      @Nullable QueryListener listener)
      implements Operation<Out> {
    @Override
    public Out runChecked(Connection conn) throws SQLException {
      Connection enriched = enrich(conn);
      return inner.runChecked(enriched);
    }

    private Connection enrich(Connection conn) {
      if (name == null && timeout == null && listener == null) {
        return conn;
      }
      if (conn instanceof InstrumentedConnection ic) {
        InstrumentedConnection merged = new InstrumentedConnection(
            ic.delegate(),
            listener != null ? QueryListener.compose(ic.queryListener(), listener) : ic.queryListener(),
            name != null ? name : ic.queryName(),
            timeout != null ? timeout : ic.queryTimeout());
        return merged;
      }
      QueryListener effectiveListener =
          listener != null ? listener : QueryListener.NOOP;
      return new InstrumentedConnection(conn, effectiveListener, name, timeout);
    }
  }
}
