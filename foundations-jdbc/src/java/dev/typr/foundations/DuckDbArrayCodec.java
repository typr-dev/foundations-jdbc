package dev.typr.foundations;

import java.util.function.Function;

/**
 * Describes how to read and write elements of a DuckDB array/list.
 *
 * <p>Read: convert a raw JDBC value (from {@code Array.getArray()}) to the typed value.
 * Write: serialize a value to a string for {@code DuckDBUserArray}.
 *
 * <p>Every {@link DuckDbType} carries one of these. {@link DuckDbType#array()} uses it
 * to build the array read/write codecs.
 */
public record DuckDbArrayCodec<A>(
    Function<Object, A> fromElement,
    Function<A, String> toElement
) {

  /** Default: use DuckDbRead.fromJdbcValue for reading, stringifier for writing. */
  static <A> DuckDbArrayCodec<A> fromReadAndStringifier(DuckDbRead<A> read, DuckDbStringifier<A> stringifier) {
    return new DuckDbArrayCodec<>(
        read::fromJdbcValue,
        a -> stringifier.encode(a, false)
    );
  }

  /** Transform: compose with a bijection or transform pair. */
  public <B> DuckDbArrayCodec<B> map(Function<A, B> read, Function<B, A> write) {
    return new DuckDbArrayCodec<>(
        obj -> read.apply(fromElement.apply(obj)),
        b -> toElement.apply(write.apply(b))
    );
  }
}
