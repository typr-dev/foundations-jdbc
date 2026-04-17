package dev.typr.foundations;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Describes how to read and write elements of a DuckDB array/list.
 *
 * <p>Read: convert a raw JDBC value (from {@code Array.getArray()}) to the typed value. Write:
 * serialize a value to a string for {@code DuckDBUserArray}.
 *
 * <p>The optional {@code arrayFactory} allocates a properly-typed {@code A[]} at read time. When
 * present, {@link DuckDbType#array()} uses it so the resulting array can be assigned to typed
 * fields (e.g. {@code Person[]}) without a {@link ClassCastException}. When absent, the read path
 * falls back to allocating {@code Object[]} and relying on generic erasure — which works for
 * scalars but fails for user-defined composite types. Always supply an {@code arrayFactory} when
 * building a codec for a composite type (see {@link DuckDbTypes#compositeOf}).
 *
 * <p>Every {@link DuckDbType} carries one of these. {@link DuckDbType#array()} uses it to build the
 * array read/write codecs.
 */
public record DuckDbArrayCodec<A>(
    Function<Object, A> fromElement,
    Function<A, String> toElement,
    Optional<IntFunction<A[]>> arrayFactory) {

  /** Default: use DuckDbRead.fromJdbcValue for reading, stringifier for writing. */
  static <A> DuckDbArrayCodec<A> fromReadAndStringifier(
      DuckDbRead<A> read, DuckDbStringifier<A> stringifier) {
    return new DuckDbArrayCodec<>(
        read::fromJdbcValue, a -> stringifier.encode(a, false), Optional.empty());
  }

  public DuckDbArrayCodec<A> withArrayFactory(IntFunction<A[]> factory) {
    return new DuckDbArrayCodec<>(fromElement, toElement, Optional.of(factory));
  }

  /** Transform: compose with a bijection or transform pair. */
  public <B> DuckDbArrayCodec<B> map(Function<A, B> read, Function<B, A> write) {
    // A typed array factory for A cannot be safely transformed to one for B without a new factory.
    return new DuckDbArrayCodec<>(
        obj -> read.apply(fromElement.apply(obj)),
        b -> toElement.apply(write.apply(b)),
        Optional.empty());
  }
}
