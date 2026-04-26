package dev.typr.foundations;

import java.sql.Statement;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;

public sealed interface Operation<Out> extends Analyzable
    permits OperationRead,
        Operation.Update,
        Operation.Execute,
        Operation.UpdateReturning,
        Operation.UpdateReturningGeneratedKeys,
        Operation.UpdateMany,
        Operation.UpdateManyReturning,
        Operation.UpdateReturningEach,
        Operation.BatchUpdate,
        Operation.StreamingCopy,
        Operation.Mapped,
        Operation.Then,
        Operation.IfEmpty,
        Operation.Combine,
        Operation.Configured,
        Procedure.ProcedureCall,
        Procedure.FunctionCall {
  /** Run this operation on the given connection. */
  default Out run(Connection conn) {
    return conn.execute(this);
  }

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

  default <B> Operation<Out> productL(Operation<B> other) {
    return combine(other).map(t -> t._1());
  }

  default <B> Operation<B> then(java.util.function.Function<Out, Operation<B>> next) {
    return new Then<>(this, next);
  }

  default Operation<Void> voided() {
    return map(ignored -> null);
  }

  default Operation<Out> named(String name) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), Optional.of(name), c.timeout(), c.listener());
    }
    return new Configured<>(this, Optional.of(name), Optional.empty(), Optional.empty());
  }

  default Operation<Out> timeout(Duration timeout) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), Optional.of(timeout), c.listener());
    }
    return new Configured<>(this, Optional.empty(), Optional.of(timeout), Optional.empty());
  }

  default Operation<Out> withListener(QueryListener listener) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), c.timeout(), Optional.of(listener));
    }
    return new Configured<>(this, Optional.empty(), Optional.empty(), Optional.of(listener));
  }

  // ========== Static combinators ==========

  static <T> Operation<List<T>> sequence(List<? extends Operation<T>> ops) {
    if (ops.isEmpty()) return OperationRead.pure(List.of());
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

  static Operation<Void> allOf(Operation<?>... ops) {
    if (ops.length == 0) return OperationRead.pure(null);
    Operation<Void> result = ops[0].voided();
    for (int i = 1; i < ops.length; i++) {
      result = result.productL(ops[i]);
    }
    return result;
  }

  static <T> Operation<T> ifEmpty(Operation<Optional<T>> check, Operation<T> fallback) {
    return new IfEmpty<>(check, fallback);
  }

  static <A, B> Operation<B> createThen(
      Operation<A> source, java.util.function.Function<A, Operation<B>> continuation) {
    return new Then<>(source, continuation);
  }

  // ========== Inner types ==========

  record Update(Fragment query) implements Operation<Integer> {
    @Override
    public String description(boolean verbose) {
      return "Update: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Execute(Fragment query) implements Operation<Void> {
    @Override
    public String description(boolean verbose) {
      return "Execute: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record UpdateReturning<Out>(Fragment query, ResultSetParser<Out> parser)
      implements Operation<Out> {
    @Override
    public String description(boolean verbose) {
      return "UpdateReturning: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record UpdateReturningGeneratedKeys<Out>(
      Fragment query, String[] columnNames, ResultSetParser<Out> parser) implements Operation<Out> {
    @Override
    public String description(boolean verbose) {
      return "UpdateReturningGeneratedKeys["
          + String.join(",", columnNames)
          + "]: "
          + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record UpdateMany<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<Optional<int[]>> {
    @Override
    public String description(boolean verbose) {
      return "UpdateMany: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record UpdateManyReturning<Row>(Fragment query, RowCodec<Row> codec, Iterator<Row> rows)
      implements Operation<List<Row>> {
    @Override
    public String description(boolean verbose) {
      return "UpdateManyReturning: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
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
    public String description(boolean verbose) {
      return "UpdateReturningEach: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  /**
   * Batch-executes a row-parameterized fragment. Unlike UpdateMany (which writes all codec fields),
   * this only writes the fields specified by includedIndices, matching the Param holes in the
   * fragment. Created by {@link RowParamBuilder#updateMany}.
   *
   * <p>Parameter positions and types are computed once from the fragment tree before the loop. Each
   * row is then written directly to the PreparedStatement without rebuilding the fragment.
   */
  record BatchUpdate<Row>(
      Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices, Iterator<Row> rows)
      implements Operation<Optional<int[]>> {
    @Override
    public String description(boolean verbose) {
      return "BatchUpdate: " + fragment.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record StreamingCopy<Row>(String copyCommand, int batchSize, Iterator<Row> rows, PgText<Row> text)
      implements Operation<Long> {
    @Override
    public String description(boolean verbose) {
      return "StreamingCopy: " + copyCommand;
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Mapped<A, B>(Operation<A> source, SqlFunction<A, B> f) implements Operation<B> {
    @Override
    public String description(boolean verbose) {
      return "Mapped(" + source.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Then<A, B>(
      Operation<A> source, java.util.function.Function<A, Operation<B>> continuation)
      implements Operation<B> {
    @Override
    public String description(boolean verbose) {
      return "Then(" + source.description(verbose) + " -> ?)";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record IfEmpty<T>(Operation<Optional<T>> check, Operation<T> fallback) implements Operation<T> {
    @Override
    public String description(boolean verbose) {
      return "IfEmpty(" + check.description(verbose) + ", " + fallback.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Combine<A, B>(Operation<A> first, Operation<B> second)
      implements Operation<Tuple.Tuple2<A, B>> {
    @Override
    public String description(boolean verbose) {
      return "Combine(" + first.description(verbose) + ", " + second.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Configured<Out>(
      Operation<Out> inner,
      Optional<String> name,
      Optional<Duration> timeout,
      Optional<QueryListener> listener)
      implements Operation<Out> {
    @Override
    public String description(boolean verbose) {
      if (name.isPresent()) {
        return verbose ? name.get() + "\n" + inner.description(false) : name.get();
      }
      return inner.description(verbose);
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  /**
   * Convert JDBC {@code executeBatch()} results to {@code Optional<int[]>}. Returns the array when
   * per-row counts are available, or empty when the driver reports {@link
   * Statement#SUCCESS_NO_INFO} (e.g. PostgreSQL with {@code reWriteBatchedInserts=true}).
   */
  static Optional<int[]> toBatchResult(int[] raw) {
    for (int r : raw) {
      if (r == Statement.SUCCESS_NO_INFO) return Optional.empty();
    }
    return Optional.of(raw);
  }
}
