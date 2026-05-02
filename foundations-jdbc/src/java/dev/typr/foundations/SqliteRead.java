package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;
import java.util.UUID;

/**
 * Describes how to read a column from a {@link ResultSet} for SQLite. The xerial sqlite-jdbc
 * driver returns simple JDBC primitives and {@link String} for date/time when {@code
 * date_class=text} (the default and only mode {@code SqliteTypes} supports).
 */
public sealed interface SqliteRead<A> extends DbRead<A>
    permits SqliteRead.NonNullable, SqliteRead.Nullable, SqliteRead.Mapped {
  A read(ResultSet rs, int col) throws SQLException;

  <B> SqliteRead<B> map(SqlFunction<A, B> f);

  SqliteRead<Optional<A>> opt();

  @FunctionalInterface
  interface RawRead<A> {
    A apply(ResultSet rs, int column) throws SQLException;
  }

  static <A> NonNullable<A> of(RawRead<A> f) {
    RawRead<Optional<A>> readNullableA =
        (rs, col) -> {
          var a = f.apply(rs, col);
          if (rs.wasNull()) return Optional.empty();
          return Optional.of(a);
        };
    return new NonNullable<>(readNullableA);
  }

  final class NonNullable<A> implements SqliteRead<A> {
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
            if (maybeA.isEmpty()) return Optional.empty();
            return Optional.of(f.apply(maybeA.get()));
          });
    }

    @Override
    public SqliteRead<Optional<A>> opt() {
      return new Nullable<>(readNullable);
    }
  }

  final class Nullable<A> implements SqliteRead<Optional<A>> {
    final RawRead<Optional<A>> readNullable;

    public Nullable(RawRead<Optional<A>> readNullable) {
      this.readNullable = readNullable;
    }

    @Override
    public Optional<A> read(ResultSet rs, int col) throws SQLException {
      return readNullable.apply(rs, col);
    }

    @Override
    public <B> SqliteRead<B> map(SqlFunction<Optional<A>, B> f) {
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

  record Mapped<A, B>(SqliteRead<A> underlying, SqlFunction<A, B> f) implements SqliteRead<B> {
    @Override
    public B read(ResultSet rs, int col) throws SQLException {
      return f.apply(underlying.read(rs, col));
    }

    @Override
    public <C> SqliteRead<C> map(SqlFunction<B, C> g) {
      return new Mapped<>(this, g);
    }

    @Override
    public SqliteRead<Optional<B>> opt() {
      SqliteRead<Optional<A>> underlyingOpt = underlying.opt();
      return new Nullable<>(
          (rs, col) -> {
            Optional<A> maybeA = underlyingOpt.read(rs, col);
            if (maybeA.isEmpty()) return Optional.empty();
            return Optional.of(f.apply(maybeA.get()));
          });
    }
  }

  // ==================== Primitive readers ====================

  SqliteRead<String> readString = of(ResultSet::getString);
  SqliteRead<Boolean> readBoolean = of(ResultSet::getBoolean);
  SqliteRead<Byte> readByte = of(ResultSet::getByte);
  SqliteRead<Short> readShort = of(ResultSet::getShort);
  SqliteRead<Integer> readInteger = of(ResultSet::getInt);
  SqliteRead<Long> readLong = of(ResultSet::getLong);
  SqliteRead<Float> readFloat = of(ResultSet::getFloat);
  SqliteRead<Double> readDouble = of(ResultSet::getDouble);
  // The xerial driver's getBigDecimal throws "column -1 out of bounds" — read as text and parse.
  SqliteRead<BigDecimal> readBigDecimal =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : new BigDecimal(s);
          });
  SqliteRead<byte[]> readByteArray = of(ResultSet::getBytes);

  SqliteRead<UUID> readUuid =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : UUID.fromString(s);
          });

  // ==================== Date/Time (text storage, ISO-8601) ====================

  /**
   * The driver default for {@code date_class=text} is {@code yyyy-MM-dd HH:mm:ss.SSS}. We accept
   * either the space or the {@code T} separator, with optional millisecond fraction, so values
   * written by other tooling round-trip without configuration tweaks.
   */
  DateTimeFormatter SQLITE_DATETIME_PARSER =
      new DateTimeFormatterBuilder()
          .append(DateTimeFormatter.ISO_LOCAL_DATE)
          .appendPattern("[' ']['T']")
          .appendValue(ChronoField.HOUR_OF_DAY, 2)
          .appendLiteral(':')
          .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
          .optionalStart()
          .appendLiteral(':')
          .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
          .optionalStart()
          .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
          .optionalEnd()
          .optionalEnd()
          .toFormatter();

  SqliteRead<LocalDate> readLocalDate =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
          });

  SqliteRead<LocalTime> readLocalTime =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : LocalTime.parse(s);
          });

  SqliteRead<LocalDateTime> readLocalDateTime =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : LocalDateTime.parse(s, SQLITE_DATETIME_PARSER);
          });

  /**
   * TIMESTAMP-with-zone equivalent. SQLite has no zone storage, so values are written as UTC
   * ISO-8601 with a {@code Z} suffix and read back as {@link Instant}.
   */
  SqliteRead<Instant> readInstant =
      of(
          (rs, idx) -> {
            String s = rs.getString(idx);
            return s == null ? null : Instant.parse(s);
          });
}
