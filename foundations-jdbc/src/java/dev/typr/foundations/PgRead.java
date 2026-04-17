package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.postgresql.util.PGobject;

/**
 * Describes how to read a column from a {@link ResultSet}
 *
 * <p>Note that the implementation is a bit more complex than you would expect. This is because we
 * need to check {@link ResultSet#wasNull()} "in the middle" of extracting data. <br>
 * <br>
 * Correct use of {@code Column} <b>requires</b> use of either
 *
 * <ul>
 *   <li>- pre-defined instances
 *   <li>- or `Column.instance` with a provided function which does not blow up if the value from
 *       the {@link ResultSet} is {@code null}
 * </ul>
 *
 * Then you create derived instances with {@code map} and/or {@code opt}
 */
public sealed interface PgRead<A> extends DbRead<A>
    permits PgRead.NonNullable, PgRead.Nullable, PgRead.Mapped {
  A read(ResultSet rs, int col) throws SQLException;

  <B> PgRead<B> map(SqlFunction<A, B> f);

  /** Derive a `Column` which allows nullable values */
  PgRead<Optional<A>> opt();

  @FunctionalInterface
  interface RawRead<A> {
    A apply(ResultSet rs, int column) throws SQLException;
  }

  /**
   * Create an instance of {@link PgRead} from a function that reads a value from a result set.
   *
   * @param f Should not blow up if the value returned is `null`
   */
  static <A> NonNullable<A> of(RawRead<A> f) {
    RawRead<Optional<A>> readNullableA =
        (rs, col) -> {
          var a = f.apply(rs, col);
          if (rs.wasNull()) return Optional.empty();
          else return Optional.of(a);
        };
    return new NonNullable<>(readNullableA);
  }

  final class NonNullable<A> implements PgRead<A> {
    final RawRead<Optional<A>> readNullable;

    public NonNullable(RawRead<Optional<A>> readNullable) {
      this.readNullable = readNullable;
    }

    @Override
    public A read(ResultSet rs, int col) throws SQLException {
      return readNullable
          .apply(rs, col)
          .orElseThrow(() -> new SQLException("null value in column " + col));
    }

    @Override
    public <B> NonNullable<B> map(SqlFunction<A, B> f) {
      return new NonNullable<>(
          (rs, col) -> {
            Optional<A> maybeA = readNullable.apply(rs, col);
            // this looks like map, but there is a checked exception
            if (maybeA.isEmpty()) return Optional.empty();
            return Optional.of(f.apply(maybeA.get()));
          });
    }

    @Override
    public PgRead<Optional<A>> opt() {
      return new Nullable<>(readNullable);
    }
  }

  final class Nullable<A> implements PgRead<Optional<A>> {
    final RawRead<Optional<A>> readNullable;

    public Nullable(RawRead<Optional<A>> readNullable) {
      this.readNullable = readNullable;
    }

    @Override
    public Optional<A> read(ResultSet rs, int col) throws SQLException {
      return readNullable.apply(rs, col);
    }

    @Override
    public <B> PgRead<B> map(SqlFunction<Optional<A>, B> f) {
      return new Mapped<>(this, f);
    }

    @Override
    public Nullable<Optional<A>> opt() {
      return new Nullable<>(
          (rs, col) -> {
            Optional<A> maybeA = readNullable.apply(rs, col);
            if (maybeA.isEmpty()) return Optional.empty();
            return Optional.of(maybeA);
          });
    }
  }

  /**
   * A read that came from mapping another read. Just returns whatever the mapping function
   * produces, null or not. No throwing on null, no Optional wrapping.
   */
  record Mapped<A, B>(PgRead<A> underlying, SqlFunction<A, B> f) implements PgRead<B> {
    @Override
    public B read(ResultSet rs, int col) throws SQLException {
      return f.apply(underlying.read(rs, col));
    }

    @Override
    public <C> PgRead<C> map(SqlFunction<B, C> g) {
      return new Mapped<>(this, g);
    }

    @Override
    public PgRead<Optional<B>> opt() {
      PgRead<Optional<A>> underlyingOpt = underlying.opt();
      return new Nullable<>(
          (rs, col) -> {
            Optional<A> maybeA = underlyingOpt.read(rs, col);
            if (maybeA.isEmpty()) return Optional.empty();
            return Optional.of(f.apply(maybeA.get()));
          });
    }
  }

  static <A> NonNullable<A> castJdbcObjectTo(Class<A> cls) {
    return of((rs, i) -> cls.cast(rs.getObject(i)));
  }

  /**
   * Read a PG array column as a {@link List}, converting each element via the supplied converter.
   * Null array → {@code null}; null elements inside the array are preserved as {@code null}.
   * Returned list is the internal {@link ArrayList} — callers should treat it as read-only by
   * convention (we deliberately don't wrap because {@code List.copyOf} rejects null elements).
   */
  static <A> PgRead<List<A>> readElementList(Function<Object, A> converter) {
    return of(
        (rs, idx) -> {
          java.sql.Array arr = rs.getArray(idx);
          if (arr == null) return null;
          Object[] elements = (Object[]) arr.getArray();
          List<A> result = new ArrayList<>(elements.length);
          for (Object elem : elements) result.add(elem == null ? null : converter.apply(elem));
          return result;
        });
  }

  /**
   * Read a PG array column of a composite type as a {@link List}, parsing each element text via
   * the supplied {@link PgCompositeText} decoder. Used when JDBC's {@code Array.getArray()} path
   * loses precision or fails (bit, time, money, composite records).
   */
  static <A> PgRead<List<A>> readCompositeList(PgCompositeText<A> decoder) {
    return readString.map(
        arrayText -> {
          if (arrayText == null) return null;
          List<String> elements = PgRecordParser.parseArray(arrayText);
          List<A> result = new ArrayList<>(elements.size());
          for (String elementText : elements) {
            result.add(elementText == null ? null : decoder.decode(elementText));
          }
          return result;
        });
  }

  // PostgreSQL JDBC driver returns Boolean for bit(1) and PGobject for bit(n>1).
  // This reader handles both cases by using getString() which works for all bit types.
  PgRead<String> bitString = of(ResultSet::getString);

  /**
   * Parse a PG array literal (as returned as text by the driver for types where the binary
   * conversion loses precision, e.g. bit strings) into a {@link List} using the supplied
   * per-element function. Null array → {@code null}; null elements preserved.
   */
  static <A> PgRead<List<A>> readBitStringList(Function<String, A> fromString) {
    return readString.map(
        arrayText -> {
          if (arrayText == null) return null;
          List<String> elements = PgRecordParser.parseArray(arrayText);
          List<A> result = new ArrayList<>(elements.size());
          for (String elem : elements) result.add(elem == null ? null : fromString.apply(elem));
          return result;
        });
  }

  static PgRead<String> pgObject(String sqlType) {
    return PgRead.of(
        (rs, i) -> {
          PGobject object = (PGobject) rs.getObject(i);
          if (object == null) return null;
          if (!object.getType().equals(sqlType)) {
            throw new SQLException("Expected " + sqlType + " but got " + object.getType());
          }
          return object.getValue();
        });
  }

  PgRead<OffsetDateTime> readOffsetDateTime =
      of((rs, idx) -> rs.getObject(idx, OffsetDateTime.class));
  PgRead<String> readString = of(ResultSet::getString);
  PgRead<BigDecimal> readBigDecimal = of(ResultSet::getBigDecimal);
  PgRead<Boolean> readBoolean = of(ResultSet::getBoolean);
  PgRead<Byte> readByte = of(ResultSet::getByte);
  PgRead<byte[]> readByteArray = castJdbcObjectTo(byte[].class);
  PgRead<Double> readDouble = of(ResultSet::getDouble);
  PgRead<Float> readFloat = of(ResultSet::getFloat);
  PgRead<Instant> readInstant = readOffsetDateTime.map(OffsetDateTime::toInstant);
  PgRead<Integer> readInteger = of(ResultSet::getInt);
  PgRead<LocalDate> readLocalDate = of((rs, idx) -> rs.getObject(idx, LocalDate.class));
  PgRead<LocalDateTime> readLocalDateTime = of((rs, idx) -> rs.getObject(idx, LocalDateTime.class));
  PgRead<LocalTime> readLocalTime = of((rs, idx) -> rs.getObject(idx, LocalTime.class));
  PgRead<Long> readLong = of(ResultSet::getLong);
  PgRead<OffsetTime> readOffsetTime = of((rs, idx) -> rs.getObject(idx, OffsetTime.class));
  PgRead<Short> readShort = of(ResultSet::getShort);

  PgRead<UUID> readUUID = readString.map(UUID::fromString);
  PgRead<Map<String, String>> readMapStringString =
      PgRead.of(
          (rs, i) -> {
            var obj = rs.getObject(i);
            if (obj == null) return null;
            return (Map<String, String>) obj;
          });
}
