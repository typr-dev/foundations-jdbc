package dev.typr.foundations;

import java.util.function.Function;

/**
 * Describes how to read elements of a PostgreSQL array.
 *
 * <p>Two variants:
 *
 * <ul>
 *   <li>{@link OfElement} — read via JDBC's {@code Array.getArray()}, convert each element.
 *   <li>{@link OfText} — read array as string, parse via {@link PgCompositeText}. For types where
 *       JDBC's {@code Array.getArray()} fails or loses precision (bit, time, money).
 * </ul>
 */
public sealed interface PgArrayCodec<A> {

  <B> PgArrayCodec<B> map(Function<A, B> f);

  record OfElement<A>(Function<Object, A> converter) implements PgArrayCodec<A> {
    @Override
    public <B> PgArrayCodec<B> map(Function<A, B> f) {
      return new OfElement<>(obj -> f.apply(converter.apply(obj)));
    }
  }

  record OfText<A>() implements PgArrayCodec<A> {
    @Override
    public <B> PgArrayCodec<B> map(Function<A, B> f) {
      return new OfText<>();
    }
  }

  @SuppressWarnings("unchecked")
  static <A> PgArrayCodec<A> cast() {
    return new OfElement<>(obj -> (A) obj);
  }

  static <A> PgArrayCodec<A> of(Function<Object, A> converter) {
    return new OfElement<>(converter);
  }

  static <A> PgArrayCodec<A> pgObject(SqlFunction<String, A> constructor) {
    return new OfElement<>(
        obj -> {
          try {
            return constructor.apply(((org.postgresql.util.PGobject) obj).getValue());
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        });
  }

  static <A> PgArrayCodec<A> fromString(Function<String, A> constructor) {
    return new OfElement<>(obj -> constructor.apply((String) obj));
  }

  static <A> PgArrayCodec<A> textParsed() {
    return new OfText<>();
  }
}
