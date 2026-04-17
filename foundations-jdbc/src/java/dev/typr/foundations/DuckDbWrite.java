package dev.typr.foundations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Describes how to write a value to a {@link PreparedStatement} for DuckDB. DuckDB's JDBC driver
 * handles most types through setObject, but some types need special handling: - UUID: use setString
 * to avoid byte ordering bug in setObject - TIME: use setString to avoid timezone issues with
 * java.sql.Time - INTERVAL: use setString with duration format
 */
public sealed interface DuckDbWrite<A> extends DbWrite<A>
    permits DuckDbWrite.Instance, DuckDbWrite.InlineInstance {
  void set(PreparedStatement ps, int idx, A a) throws SQLException;

  DuckDbWrite<Optional<A>> opt(DuckDbTypename<A> typename);

  <B> DuckDbWrite<B> contramap(Function<B, A> f);

  @FunctionalInterface
  interface RawWriter<A> {
    void set(PreparedStatement ps, int index, A a) throws SQLException;
  }

  record Instance<A, U>(RawWriter<U> rawWriter, Function<A, U> f) implements DuckDbWrite<A> {
    @Override
    public void set(PreparedStatement ps, int index, A a) throws SQLException {
      rawWriter.set(ps, index, f.apply(a));
    }

    @Override
    public DuckDbWrite<Optional<A>> opt(DuckDbTypename<A> typename) {
      return new Instance<>(
          (ps, index, u) -> {
            if (u == null) ps.setNull(index, java.sql.Types.NULL);
            else set(ps, index, u);
          },
          a -> a.orElse(null));
    }

    @Override
    public <B> DuckDbWrite<B> contramap(Function<B, A> f) {
      return new Instance<>(rawWriter, f.andThen(this.f));
    }
  }

  static <A> DuckDbWrite<A> primitive(RawWriter<A> rawWriter) {
    return new Instance<>(rawWriter, Function.identity());
  }

  static <A> DuckDbWrite<A> passObjectToJdbc() {
    return primitive(PreparedStatement::setObject);
  }

  // Basic type writers
  DuckDbWrite<String> writeString = primitive(PreparedStatement::setString);
  DuckDbWrite<Boolean> writeBoolean = primitive(PreparedStatement::setBoolean);
  DuckDbWrite<Byte> writeByte = primitive(PreparedStatement::setByte);
  DuckDbWrite<Short> writeShort = primitive(PreparedStatement::setShort);
  DuckDbWrite<Integer> writeInteger = primitive(PreparedStatement::setInt);
  DuckDbWrite<Long> writeLong = primitive(PreparedStatement::setLong);
  DuckDbWrite<Float> writeFloat = primitive(PreparedStatement::setFloat);
  DuckDbWrite<Double> writeDouble = primitive(PreparedStatement::setDouble);
  DuckDbWrite<BigDecimal> writeBigDecimal = primitive(PreparedStatement::setBigDecimal);
  // Use setString for BigInteger to handle the full 128-bit HUGEINT/UHUGEINT range
  // setBigDecimal(new BigDecimal(hugeint)) fails for values at the 128-bit boundary
  DuckDbWrite<BigInteger> writeBigInteger = writeString.contramap(BigInteger::toString);
  DuckDbWrite<byte[]> writeByteArray = primitive(PreparedStatement::setBytes);

  // UUID - use setString to avoid DuckDB's byte ordering bug with setObject(UUID)
  DuckDbWrite<UUID> writeUuid = writeString.contramap(UUID::toString);

  // TIME - use setString to avoid timezone issues with java.sql.Time
  DuckDbWrite<LocalTime> writeLocalTime = writeString.contramap(LocalTime::toString);

  // TIMETZ / TIME WITH TIME ZONE — OffsetTime.toString() emits "HH:mm" when seconds are zero,
  // but DuckDB's parser requires "HH:mm:ss". Format with a pattern that always includes
  // seconds and the offset.
  java.time.format.DateTimeFormatter DUCKDB_TIMETZ_FMT =
      java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss[.SSSSSS]xxx");
  DuckDbWrite<OffsetTime> writeOffsetTime =
      writeString.contramap(ot -> ot.format(DUCKDB_TIMETZ_FMT));

  // INTERVAL/Duration - use setString with duration format (HH:MM:SS)
  DuckDbWrite<Duration> writeDuration =
      writeString.contramap(
          d -> {
            long hours = d.toHours();
            long minutes = d.toMinutesPart();
            long seconds = d.toSecondsPart();
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
          });

  // ==================== Nested Types ====================
  // DuckDB JDBC supports setObject with DuckDBUserArray for arrays

  /**
   * Write a LIST/Array by converting to DuckDBUserArray. DuckDB JDBC natively supports
   * DuckDBUserArray via setObject().
   *
   * @param typeName the DuckDB type name for the elements (e.g., "INTEGER", "VARCHAR")
   * @param toArray function to convert List to Object array
   * @param <E> element type
   * @return writer for List of elements
   */
  static <E> DuckDbWrite<java.util.List<E>> writeList(
      String typeName, java.util.function.IntFunction<E[]> toArray) {
    return primitive(
        (ps, idx, list) -> {
          if (list == null) {
            ps.setNull(idx, java.sql.Types.ARRAY);
          } else {
            E[] array = list.toArray(toArray);
            org.duckdb.user.DuckDBUserArray userArray =
                new org.duckdb.user.DuckDBUserArray(typeName, array);
            ps.setObject(idx, userArray);
          }
        });
  }

  /**
   * Write a LIST whose elements are encoded to wire objects via a supplied encoder. The wire
   * objects (DuckDBUserStruct, DuckDBUserArray, DuckDBMap, or stringified scalars) are wrapped
   * in a DuckDBUserArray bound via setObject. Used by every composite type's list-binding —
   * the element's own {@code structAttributeEncoder} is the encoder.
   */
  static <E> DuckDbWrite<java.util.List<E>> writeListOfUserArray(
      String elementSqlType, Function<E, Object> encoder) {
    return primitive(
        (ps, idx, list) -> {
          if (list == null) {
            ps.setNull(idx, java.sql.Types.ARRAY);
          } else {
            Object[] encoded = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) encoded[i] = encoder.apply(list.get(i));
            ps.setObject(idx, new org.duckdb.user.DuckDBUserArray(elementSqlType, encoded));
          }
        });
  }

  // ==================== SQL Literal-Based List Writers ====================
  // These types require string conversion because DuckDB JNI doesn't handle them
  // directly or has bugs (e.g., UUID byte-ordering). ~33% overhead at 100k rows.

  /**
   * Write a LIST/Array by formatting elements using DuckDbStringifier. Use this for types that
   * DuckDB JNI doesn't handle natively. Uses unquoted format (quoted=false) suitable for
   * DuckDBUserArray.
   *
   * @param typeName the DuckDB type name for the elements (e.g., "TIME", "DATE")
   * @param stringifier how to format elements
   * @param <E> element type
   * @return writer for List of elements
   */
  static <E> DuckDbWrite<java.util.List<E>> writeListViaSqlLiteral(
      String typeName, DuckDbStringifier<E> stringifier) {
    return primitive(
        (ps, idx, list) -> {
          if (list == null) {
            ps.setNull(idx, java.sql.Types.ARRAY);
          } else {
            String[] array =
                list.stream().map(e -> stringifier.encode(e, false)).toArray(String[]::new);
            org.duckdb.user.DuckDBUserArray userArray =
                new org.duckdb.user.DuckDBUserArray(typeName, array);
            ps.setObject(idx, userArray);
          }
        });
  }

  /**
   * A write implementation that inlines the value as a SQL expression rather than binding a JDBC
   * parameter. Used for types like UNION lists where the value cannot be expressed as a JDBC
   * parameter.
   */
  record InlineInstance<A>(Function<A, String> toInlineSql) implements DuckDbWrite<A> {
    @Override
    public void set(PreparedStatement ps, int idx, A a) throws SQLException {
      throw new UnsupportedOperationException(
          "InlineInstance values are rendered directly in SQL, not bound as parameters");
    }

    @Override
    public java.util.Optional<String> inlineSql(A value) {
      if (value == null) return java.util.Optional.of("NULL");
      return java.util.Optional.of(toInlineSql.apply(value));
    }

    @Override
    public DuckDbWrite<Optional<A>> opt(DuckDbTypename<A> typename) {
      return new InlineInstance<>(opt -> opt.map(toInlineSql).orElse("NULL"));
    }

    @Override
    public <B> DuckDbWrite<B> contramap(Function<B, A> f) {
      return new InlineInstance<>(f.andThen(toInlineSql));
    }
  }

  /**
   * Write a LIST by inlining the value as a SQL expression. Each element is formatted using the
   * stringifier and cast to the element type. The entire list is rendered as a SQL array literal.
   *
   * <p>Used for element types (e.g., UNION) where DuckDBUserArray can't handle the value format.
   *
   * @param elementSqlType the SQL type for each element (e.g., "UNION(num INTEGER, str VARCHAR)")
   * @param elementStringifier how to format each element as a SQL expression
   * @param <E> element type
   * @return writer that inlines the list value in SQL
   */
  static <E> DuckDbWrite<java.util.List<E>> writeListInline(
      String elementSqlType, DuckDbStringifier<E> elementStringifier) {
    return new InlineInstance<>(
        list -> {
          StringBuilder sb = new StringBuilder("[");
          for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            elementStringifier.unsafeEncode(list.get(i), sb, false);
            sb.append("::").append(elementSqlType);
          }
          sb.append("]::").append(elementSqlType).append("[]");
          return sb.toString();
        });
  }
}
