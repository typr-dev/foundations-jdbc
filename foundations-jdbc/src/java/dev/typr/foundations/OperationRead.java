package dev.typr.foundations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public sealed interface OperationRead<Out> extends Operation<Out>
    permits OperationRead.Query,
        OperationRead.Streaming,
        OperationRead.Pure,
        OperationRead.Mapped,
        OperationRead.Combine,
        OperationRead.IfEmpty,
        OperationRead.Then,
        OperationRead.Configured {

  /** Run this read operation on a read-only connection. */
  default Out run(ConnectionRead conn) {
    return conn.execute(this);
  }

  /**
   * Re-declares the inherited {@link Operation#run(Connection)} on this trait so Scala 3's
   * overload resolver sees both {@code run(Connection)} and {@code run(ConnectionRead)} declared
   * at the same level — then specificity ({@code Connection <: ConnectionRead}) picks the more
   * specific overload for {@code c: Connection} call sites without ambiguity. Java and Kotlin
   * always picked the most-specific overload regardless; this redeclaration is purely for the
   * Scala 3 overload resolver. Pure delegation; same body as the inherited default.
   */
  @Override
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
  default Out transactRead(Transactor transactor) {
    return transactor.execute(this);
  }

  @Override
  default <B> OperationRead<B> map(SqlFunction<Out, B> f) {
    return new Mapped<>(this, f);
  }

  default <B> OperationRead<Tuple.Tuple2<Out, B>> combine(OperationRead<B> other) {
    return new Combine<>(this, other);
  }

  default <B, C> OperationRead<Tuple.Tuple3<Out, B, C>> combine(
      OperationRead<B> b, OperationRead<C> c) {
    return combine(b)
        .combine(c)
        .map(t -> new Tuple.Tuple3.Impl<>(t._1()._1(), t._1()._2(), t._2()));
  }

  default <B, C, D> OperationRead<Tuple.Tuple4<Out, B, C, D>> combine(
      OperationRead<B> b, OperationRead<C> c, OperationRead<D> d) {
    return combine(b, c)
        .combine(d)
        .map(t -> new Tuple.Tuple4.Impl<>(t._1()._1(), t._1()._2(), t._1()._3(), t._2()));
  }

  default <B, C, D, E> OperationRead<Tuple.Tuple5<Out, B, C, D, E>> combine(
      OperationRead<B> b, OperationRead<C> c, OperationRead<D> d, OperationRead<E> e) {
    return combine(b, c, d)
        .combine(e)
        .map(
            t ->
                new Tuple.Tuple5.Impl<>(
                    t._1()._1(), t._1()._2(), t._1()._3(), t._1()._4(), t._2()));
  }

  default <B, C, D, E, F> OperationRead<Tuple.Tuple6<Out, B, C, D, E, F>> combine(
      OperationRead<B> b,
      OperationRead<C> c,
      OperationRead<D> d,
      OperationRead<E> e,
      OperationRead<F> f) {
    return combine(b, c, d, e)
        .combine(f)
        .map(
            t ->
                new Tuple.Tuple6.Impl<>(
                    t._1()._1(), t._1()._2(), t._1()._3(), t._1()._4(), t._1()._5(), t._2()));
  }

  default <B, R> OperationRead<R> combineWith(
      OperationRead<B> other, BiFunction<Out, B, R> combine) {
    return combine(other).map(t -> combine.apply(t._1(), t._2()));
  }

  default <B, C, R> OperationRead<R> combineWith(
      OperationRead<B> b, OperationRead<C> c, Functions.Function3<Out, B, C, R> combine) {
    return combine(b, c).map(t -> combine.apply(t._1(), t._2(), t._3()));
  }

  default <B, C, D, R> OperationRead<R> combineWith(
      OperationRead<B> b,
      OperationRead<C> c,
      OperationRead<D> d,
      Functions.Function4<Out, B, C, D, R> combine) {
    return combine(b, c, d).map(t -> combine.apply(t._1(), t._2(), t._3(), t._4()));
  }

  default <B, C, D, E, R> OperationRead<R> combineWith(
      OperationRead<B> b,
      OperationRead<C> c,
      OperationRead<D> d,
      OperationRead<E> e,
      Functions.Function5<Out, B, C, D, E, R> combine) {
    return combine(b, c, d, e).map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5()));
  }

  default <B, C, D, E, F, R> OperationRead<R> combineWith(
      OperationRead<B> b,
      OperationRead<C> c,
      OperationRead<D> d,
      OperationRead<E> e,
      OperationRead<F> f,
      Functions.Function6<Out, B, C, D, E, F, R> combine) {
    return combine(b, c, d, e, f)
        .map(t -> combine.apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6()));
  }

  default <B> OperationRead<Out> productL(OperationRead<B> other) {
    return combine(other).map(t -> t._1());
  }

  default <B> OperationRead<B> thenRead(java.util.function.Function<Out, OperationRead<B>> next) {
    return new Then<>(this, next);
  }

  @Override
  default OperationRead<Void> voided() {
    return map(ignored -> null);
  }

  @Override
  default OperationRead<Out> named(String name) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), Optional.of(name), c.timeout(), c.listener());
    }
    return new Configured<>(this, Optional.of(name), Optional.empty(), Optional.empty());
  }

  @Override
  default OperationRead<Out> timeout(Duration timeout) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), Optional.of(timeout), c.listener());
    }
    return new Configured<>(this, Optional.empty(), Optional.of(timeout), Optional.empty());
  }

  @Override
  default OperationRead<Out> withListener(QueryListener listener) {
    if (this instanceof Configured<Out> c) {
      return new Configured<>(c.inner(), c.name(), c.timeout(), Optional.of(listener));
    }
    return new Configured<>(this, Optional.empty(), Optional.empty(), Optional.of(listener));
  }

  static <T> OperationRead<T> pure(T value) {
    return new Pure<>(value);
  }

  static <T> OperationRead<List<T>> sequence(List<OperationRead<T>> ops) {
    if (ops.isEmpty()) return pure(List.of());
    OperationRead<List<T>> result = ops.getFirst().map(List::of);
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

  static OperationRead<Void> allOf(OperationRead<?>... ops) {
    if (ops.length == 0) return pure(null);
    OperationRead<Void> result = ops[0].voided();
    for (int i = 1; i < ops.length; i++) {
      result = result.productL(ops[i]);
    }
    return result;
  }

  static <T> OperationRead<T> ifEmpty(OperationRead<Optional<T>> check, OperationRead<T> fallback) {
    return new IfEmpty<>(check, fallback);
  }

  record Query<Out>(Fragment query, ResultSetParser<Out> parser) implements OperationRead<Out> {
    @Override
    public String description(boolean verbose) {
      return "Query: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Streaming<Row>(Fragment query, RowCodec<Row> codec, int fetchSize)
      implements OperationRead<Cursor<Row>> {
    @Override
    public String description(boolean verbose) {
      return "Streaming: " + query.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Mapped<A, B>(OperationRead<A> source, SqlFunction<A, B> f) implements OperationRead<B> {
    @Override
    public String description(boolean verbose) {
      return "Mapped(" + source.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Pure<T>(T value) implements OperationRead<T> {
    @Override
    public String description(boolean verbose) {
      return "Pure(" + value + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Combine<A, B>(OperationRead<A> first, OperationRead<B> second)
      implements OperationRead<Tuple.Tuple2<A, B>> {
    @Override
    public String description(boolean verbose) {
      return "Combine(" + first.description(verbose) + ", " + second.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record IfEmpty<T>(OperationRead<Optional<T>> check, OperationRead<T> fallback)
      implements OperationRead<T> {
    @Override
    public String description(boolean verbose) {
      return "IfEmpty(" + check.description(verbose) + ", " + fallback.description(verbose) + ")";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Then<A, B>(
      OperationRead<A> source, java.util.function.Function<A, OperationRead<B>> continuation)
      implements OperationRead<B> {
    @Override
    public String description(boolean verbose) {
      return "Then(" + source.description(verbose) + " -> ?)";
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Configured<Out>(
      OperationRead<Out> inner,
      Optional<String> name,
      Optional<Duration> timeout,
      Optional<QueryListener> listener)
      implements OperationRead<Out> {
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
}
