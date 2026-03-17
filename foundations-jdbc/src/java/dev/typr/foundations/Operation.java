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
        Operation.Execute,
        Operation.UpdateReturning,
        Operation.UpdateReturningGeneratedKeys,
        Operation.UpdateManyReturning,
        Operation.UpdateMany,
        Operation.UpdateReturningEach,
        Operation.UpdateManyTemplate,
        Operation.StreamingCopy,
        Operation.Streaming,
        Operation.Mapped,
        Operation.Pure,
        Operation.Combine,
        Operation.IfEmpty,
        Operation.Then,
        Operation.Configured,
        Procedure.ProcedureCall,
        Procedure.FunctionCall {
  Out run(Connection conn);

  /**
   * Execute this operation using the given transactor.
   *
   * @param transactor the transactor to use
   * @return the operation result
   * @throws DatabaseException if a database error occurs
   */
  default Out transact(Transactor transactor) {
    return transactor.execute(this);
  }

  default <B> Operation<B> map(SqlFunction<Out, B> f) {
    return new Mapped<>(this, f);
  }

  default <B> Operation<Tuple.Tuple2<Out, B>> combine(Operation<B> other) {
    return new Combine<>(this, other);
  }

  default <B, C> Operation<Tuple.Tuple3<Out, B, C>> combine(Operation<B> b, Operation<C> c) {
    return combine(b)
        .combine(c)
        .map(t -> new Tuple.Tuple3.Impl<>(t._1()._1(), t._1()._2(), t._2()));
  }

  default <B, C, D> Operation<Tuple.Tuple4<Out, B, C, D>> combine(
      Operation<B> b, Operation<C> c, Operation<D> d) {
    return combine(b, c)
        .combine(d)
        .map(t -> new Tuple.Tuple4.Impl<>(t._1()._1(), t._1()._2(), t._1()._3(), t._2()));
  }

  default <B, C, D, E> Operation<Tuple.Tuple5<Out, B, C, D, E>> combine(
      Operation<B> b, Operation<C> c, Operation<D> d, Operation<E> e) {
    return combine(b, c, d)
        .combine(e)
        .map(
            t ->
                new Tuple.Tuple5.Impl<>(
                    t._1()._1(), t._1()._2(), t._1()._3(), t._1()._4(), t._2()));
  }

  default <B, C, D, E, F> Operation<Tuple.Tuple6<Out, B, C, D, E, F>> combine(
      Operation<B> b, Operation<C> c, Operation<D> d, Operation<E> e, Operation<F> f) {
    return combine(b, c, d, e)
        .combine(f)
        .map(
            t ->
                new Tuple.Tuple6.Impl<>(
                    t._1()._1(), t._1()._2(), t._1()._3(), t._1()._4(), t._1()._5(), t._2()));
  }

  default <B, C, D, E, F, G> Operation<Tuple.Tuple7<Out, B, C, D, E, F, G>> combine(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g) {
    return combine(b, c, d, e, f)
        .combine(g)
        .map(
            t ->
                new Tuple.Tuple7.Impl<>(
                    t._1()._1(),
                    t._1()._2(),
                    t._1()._3(),
                    t._1()._4(),
                    t._1()._5(),
                    t._1()._6(),
                    t._2()));
  }

  default <B, C, D, E, F, G, H> Operation<Tuple.Tuple8<Out, B, C, D, E, F, G, H>> combine(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h) {
    return combine(b, c, d, e, f, g)
        .combine(h)
        .map(
            t ->
                new Tuple.Tuple8.Impl<>(
                    t._1()._1(),
                    t._1()._2(),
                    t._1()._3(),
                    t._1()._4(),
                    t._1()._5(),
                    t._1()._6(),
                    t._1()._7(),
                    t._2()));
  }

  default <B, C, D, E, F, G, H, I> Operation<Tuple.Tuple9<Out, B, C, D, E, F, G, H, I>> combine(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h,
      Operation<I> i) {
    return combine(b, c, d, e, f, g, h)
        .combine(i)
        .map(
            t ->
                new Tuple.Tuple9.Impl<>(
                    t._1()._1(),
                    t._1()._2(),
                    t._1()._3(),
                    t._1()._4(),
                    t._1()._5(),
                    t._1()._6(),
                    t._1()._7(),
                    t._1()._8(),
                    t._2()));
  }

  default <B, R> Operation<R> combineWith(Operation<B> other, BiFunction<Out, B, R> combine) {
    return combine(other).map(t -> combine.apply(t._1(), t._2()));
  }

  default <B, C, R> Operation<R> combineWith(
      Operation<B> b, Operation<C> c, Functions.Function3<Out, B, C, R> combine) {
    return combine(b, c).map(t -> combine.apply(t._1(), t._2(), t._3()));
  }

  default <B, C, D, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Functions.Function4<Out, B, C, D, R> combine) {
    return combine(b, c, d).map(t -> combine.apply(t._1(), t._2(), t._3(), t._4()));
  }

  default <B, C, D, E, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Functions.Function5<Out, B, C, D, E, R> combine) {
    return combine(b, c, d, e).map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5()));
  }

  default <B, C, D, E, F, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Functions.Function6<Out, B, C, D, E, F, R> combine) {
    return combine(b, c, d, e, f)
        .map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6()));
  }

  default <B, C, D, E, F, G, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Functions.Function7<Out, B, C, D, E, F, G, R> combine) {
    return combine(b, c, d, e, f, g)
        .map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7()));
  }

  default <B, C, D, E, F, G, H, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h,
      Functions.Function8<Out, B, C, D, E, F, G, H, R> combine) {
    return combine(b, c, d, e, f, g, h)
        .map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8()));
  }

  default <B, C, D, E, F, G, H, I, R> Operation<R> combineWith(
      Operation<B> b,
      Operation<C> c,
      Operation<D> d,
      Operation<E> e,
      Operation<F> f,
      Operation<G> g,
      Operation<H> h,
      Operation<I> i,
      Functions.Function9<Out, B, C, D, E, F, G, H, I, R> combine) {
    return combine(b, c, d, e, f, g, h, i)
        .map(
            t ->
                combine.apply(
                    t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8(), t._9()));
  }

  default <B> Operation<Out> productL(Operation<B> other) {
    return combine(other).map(t -> t._1());
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
              .combine(ops.get(i))
              .map(
                  t -> {
                    var list = new ArrayList<>(t._1());
                    list.add(t._2());
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
      result = result.productL((Operation<Object>) ops[i]);
    }
    return result;
  }

  static <T> Operation<T> ifEmpty(Operation<Optional<T>> check, Operation<T> fallback) {
    return new IfEmpty<>(check, fallback);
  }

  record Query<Out>(Fragment query, ResultSetParser<Out> parser) implements Operation<Out> {
    @Override
    public Out run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                Instrumentation.applyTimeout(stmt, conn);
                query.set(stmt);
                try (ResultSet rs = stmt.executeQuery()) {
                  return parser.apply(rs);
                }
              }
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record Update(Fragment query) implements Operation<Integer> {
    @Override
    public Integer run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                Instrumentation.applyTimeout(stmt, conn);
                query.set(stmt);
                return stmt.executeUpdate();
              }
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record Execute(Fragment query) implements Operation<Void> {
    @Override
    public Void run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                Instrumentation.applyTimeout(stmt, conn);
                query.set(stmt);
                stmt.execute();
                return null;
              }
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record UpdateReturning<Out>(Fragment query, ResultSetParser<Out> parser)
      implements Operation<Out> {
    @Override
    public Out run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                Instrumentation.applyTimeout(stmt, conn);
                query.set(stmt);
                try (ResultSet rs = stmt.executeQuery()) {
                  return parser.apply(rs);
                }
              }
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record UpdateReturningGeneratedKeys<Out>(
      Fragment query, String[] columnNames, ResultSetParser<Out> parser) implements Operation<Out> {
    @Override
    public Out run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              try (PreparedStatement stmt = conn.prepareStatement(sql, columnNames)) {
                Instrumentation.applyTimeout(stmt, conn);
                query.set(stmt);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                  return parser.apply(rs);
                }
              }
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record UpdateMany<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<int[]> {
    @Override
    public int[] run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
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
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record UpdateManyReturning<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<List<Row>> {
    @Override
    public List<Row> run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
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
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
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
    public List<Row> run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
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
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  /**
   * Batch executes a row-parameterized template. Unlike UpdateMany (which writes all codec fields),
   * this only writes the fields specified by includedIndices, matching the Param holes in the
   * fragment. Created by RowTemplate.Update.onMany().
   *
   * <p>Parameter positions and types are computed once from the fragment tree before the loop. Each
   * row is then written directly to the PreparedStatement without rebuilding the fragment.
   */
  record UpdateManyTemplate<Row>(
      Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices, Iterator<Row> rows)
      implements Operation<int[]> {
    @SuppressWarnings("unchecked")
    @Override
    public int[] run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(fragment.render(), conn);
        return Instrumentation.instrumented(
            conn,
            fragment,
            sql,
            () -> {
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
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record StreamingCopy<Row>(String copyCommand, int batchSize, Iterator<Row> rows, PgText<Row> text)
      implements Operation<Long> {
    @Override
    public Long run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(copyCommand, conn);
        Fragment syntheticFragment = Fragment.of(copyCommand);
        return Instrumentation.instrumented(
            conn,
            syntheticFragment,
            sql,
            () -> StreamingInsert.insert(copyCommand, batchSize, rows, conn, text));
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record Streaming<Row>(Fragment query, RowCodec<Row> codec, int fetchSize)
      implements Operation<Cursor<Row>> {
    @Override
    public Cursor<Row> run(Connection conn) {
      try {
        String sql = Instrumentation.applyName(query.render(), conn);
        return Instrumentation.instrumented(
            conn,
            query,
            sql,
            () -> {
              PreparedStatement stmt =
                  conn.prepareStatement(
                      sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
              stmt.setFetchSize(fetchSize);
              Instrumentation.applyTimeout(stmt, conn);
              query.set(stmt);
              ResultSet rs = stmt.executeQuery();
              return new Cursor<>(stmt, rs, codec);
            });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record Mapped<A, B>(Operation<A> source, SqlFunction<A, B> f) implements Operation<B> {
    @Override
    public B run(Connection conn) {
      try {
        return f.apply(source.run(conn));
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  record Pure<T>(T value) implements Operation<T> {
    @Override
    public T run(Connection conn) {
      return value;
    }
  }

  record Combine<A, B>(Operation<A> first, Operation<B> second)
      implements Operation<Tuple.Tuple2<A, B>> {
    @Override
    public Tuple.Tuple2<A, B> run(Connection conn) {
      return Tuple.of(first.run(conn), second.run(conn));
    }
  }

  record IfEmpty<T>(Operation<Optional<T>> check, Operation<T> fallback) implements Operation<T> {
    @Override
    public T run(Connection conn) {
      Optional<T> result = check.run(conn);
      return result.isPresent() ? result.get() : fallback.run(conn);
    }
  }

  record Then<A, In, B>(
      Operation<A> source, java.util.function.Function<A, In> extract, Template<In, B> continuation)
      implements Operation<B> {
    @Override
    public B run(Connection conn) {
      A a = source.run(conn);
      In in = extract.apply(a);
      return continuation.on(in).run(conn);
    }
  }

  record Configured<Out>(
      Operation<Out> inner,
      @Nullable String name,
      @Nullable Duration timeout,
      @Nullable QueryListener listener)
      implements Operation<Out> {
    @Override
    public Out run(Connection conn) {
      Connection enriched = enrich(conn);
      return inner.run(enriched);
    }

    private Connection enrich(Connection conn) {
      if (name == null && timeout == null && listener == null) {
        return conn;
      }
      if (conn instanceof InstrumentedConnection ic) {
        InstrumentedConnection merged =
            new InstrumentedConnection(
                ic.delegate(),
                listener != null
                    ? QueryListener.compose(ic.queryListener(), listener)
                    : ic.queryListener(),
                name != null ? name : ic.queryName(),
                timeout != null ? timeout : ic.queryTimeout());
        return merged;
      }
      QueryListener effectiveListener = listener != null ? listener : QueryListener.NOOP;
      return new InstrumentedConnection(conn, effectiveListener, name, timeout);
    }
  }
}
