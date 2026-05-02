package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Describes how to write a value to a {@link PreparedStatement} for SQLite. The xerial driver
 * happily binds primitives via the standard JDBC setters; date/time and UUID are written as ISO
 * text to keep storage stable across drivers.
 */
public sealed interface SqliteWrite<A> extends DbWrite<A> permits SqliteWrite.Instance {
  void set(PreparedStatement ps, int idx, A a) throws SQLException;

  SqliteWrite<Optional<A>> opt(SqliteTypename<A> typename);

  <B> SqliteWrite<B> contramap(Function<B, A> f);

  @FunctionalInterface
  interface RawWriter<A> {
    void set(PreparedStatement ps, int index, A a) throws SQLException;
  }

  record Instance<A, U>(RawWriter<U> rawWriter, Function<A, U> f) implements SqliteWrite<A> {
    @Override
    public void set(PreparedStatement ps, int index, A a) throws SQLException {
      rawWriter.set(ps, index, f.apply(a));
    }

    @Override
    public SqliteWrite<Optional<A>> opt(SqliteTypename<A> typename) {
      return new Instance<>(
          (ps, index, u) -> {
            if (u == null) ps.setNull(index, java.sql.Types.NULL);
            else set(ps, index, u);
          },
          a -> a.orElse(null));
    }

    @Override
    public <B> SqliteWrite<B> contramap(Function<B, A> f) {
      return new Instance<>(rawWriter, f.andThen(this.f));
    }
  }

  static <A> SqliteWrite<A> primitive(RawWriter<A> rawWriter) {
    return new Instance<>(rawWriter, Function.identity());
  }

  // ==================== Primitive writers ====================

  SqliteWrite<String> writeString = primitive(PreparedStatement::setString);
  SqliteWrite<Boolean> writeBoolean = primitive(PreparedStatement::setBoolean);
  SqliteWrite<Byte> writeByte = primitive(PreparedStatement::setByte);
  SqliteWrite<Short> writeShort = primitive(PreparedStatement::setShort);
  SqliteWrite<Integer> writeInteger = primitive(PreparedStatement::setInt);
  SqliteWrite<Long> writeLong = primitive(PreparedStatement::setLong);
  SqliteWrite<Float> writeFloat = primitive(PreparedStatement::setFloat);
  SqliteWrite<Double> writeDouble = primitive(PreparedStatement::setDouble);
  // The xerial driver's setBigDecimal is broken (throws "column -1 out of bounds"); writing
  // BigDecimal as a plain-string TEXT preserves precision and round-trips through getBigDecimal.
  SqliteWrite<BigDecimal> writeBigDecimal = writeString.contramap(BigDecimal::toPlainString);
  SqliteWrite<byte[]> writeByteArray = primitive(PreparedStatement::setBytes);

  SqliteWrite<UUID> writeUuid = writeString.contramap(UUID::toString);

  // ==================== Date/Time (text, ISO-8601) ====================

  /** Always pad seconds and milliseconds so the on-disk text sorts and indexes correctly. */
  DateTimeFormatter SQLITE_DATETIME_FMT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.SSS");

  SqliteWrite<LocalDate> writeLocalDate =
      writeString.contramap(d -> d.format(DateTimeFormatter.ISO_LOCAL_DATE));

  SqliteWrite<LocalTime> writeLocalTime = writeString.contramap(LocalTime::toString);

  SqliteWrite<LocalDateTime> writeLocalDateTime =
      writeString.contramap(dt -> dt.format(SQLITE_DATETIME_FMT));

  SqliteWrite<Instant> writeInstant = writeString.contramap(Instant::toString);
}
