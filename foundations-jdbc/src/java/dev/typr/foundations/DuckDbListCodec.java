package dev.typr.foundations;

import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Describes how to read and write elements of a DuckDB LIST column.
 *
 * <p>Two strategies:
 *
 * <ul>
 *   <li>{@link Native} — write via JNI native array path ({@code DuckDBUserArray} with typed
 *       array). For types DuckDB JNI handles directly: Boolean, Byte, Short, Integer, Long, Float,
 *       Double, String.
 *   <li>{@link SqlLiteral} — write via SQL literal stringification ({@code DuckDBUserArray} with
 *       String[]). For types DuckDB JNI doesn't handle natively: UUID, LocalDate, LocalTime,
 *       BigDecimal, etc.
 * </ul>
 *
 * <p>Both strategies use the same read path: {@code Array.getArray()} with per-element conversion
 * via {@code fromElement}.
 */
public sealed interface DuckDbListCodec<A> {

  /** Convert a raw JDBC array element to the typed value. */
  Function<Object, A> fromElement();

  <B> DuckDbListCodec<B> map(Function<A, B> f, Function<B, A> g);

  record Native<A>(Function<Object, A> fromElement, IntFunction<A[]> arrayFactory)
      implements DuckDbListCodec<A> {
    @Override
    public <B> DuckDbListCodec<B> map(Function<A, B> f, Function<B, A> g) {
      return new SqlLiteral<>(obj -> f.apply(fromElement.apply(obj)));
    }
  }

  record SqlLiteral<A>(Function<Object, A> fromElement) implements DuckDbListCodec<A> {
    @Override
    public <B> DuckDbListCodec<B> map(Function<A, B> f, Function<B, A> g) {
      return new SqlLiteral<>(obj -> f.apply(fromElement.apply(obj)));
    }
  }

  @SuppressWarnings("unchecked")
  static <A> DuckDbListCodec<A> nativeCodec(IntFunction<A[]> arrayFactory) {
    return new Native<>(obj -> (A) obj, arrayFactory);
  }

  static <A> DuckDbListCodec<A> sqlLiteral(Function<Object, A> fromElement) {
    return new SqlLiteral<>(fromElement);
  }

  @SuppressWarnings("unchecked")
  static <A> DuckDbListCodec<A> sqlLiteralCast() {
    return new SqlLiteral<>(obj -> (A) obj);
  }
}
