package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;

/**
 * OUT/INOUT parameter codecs for MariaDB types. Each instance handles both registration and
 * reading.
 */
public interface MariaOutParam<A> extends DbOutParam<A> {

  @FunctionalInterface
  interface ReadFn<A> {
    A read(CallableStatement stmt, int index) throws SQLException;
  }

  private static <A> MariaOutParam<A> of(int jdbcType, ReadFn<A> reader) {
    return new MariaOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        stmt.registerOutParameter(index, jdbcType);
      }

      @Override
      public A read(CallableStatement stmt, int index) throws SQLException {
        return reader.read(stmt, index);
      }
    };
  }

  // Primitive types
  MariaOutParam<Boolean> readBoolean = of(Types.BOOLEAN, CallableStatement::getBoolean);
  MariaOutParam<Byte> readByte = of(Types.TINYINT, CallableStatement::getByte);
  MariaOutParam<Short> readShort = of(Types.SMALLINT, CallableStatement::getShort);
  MariaOutParam<Integer> readInteger = of(Types.INTEGER, CallableStatement::getInt);
  MariaOutParam<Long> readLong = of(Types.BIGINT, CallableStatement::getLong);
  MariaOutParam<Float> readFloat = of(Types.REAL, CallableStatement::getFloat);
  MariaOutParam<Double> readDouble = of(Types.DOUBLE, CallableStatement::getDouble);
  MariaOutParam<BigDecimal> readBigDecimal = of(Types.DECIMAL, CallableStatement::getBigDecimal);
  MariaOutParam<String> readString = of(Types.VARCHAR, CallableStatement::getString);
  MariaOutParam<byte[]> readByteArray = of(Types.VARBINARY, CallableStatement::getBytes);

  // Date/time types
  MariaOutParam<LocalDate> readLocalDate =
      of(
          Types.DATE,
          (stmt, i) -> {
            var date = stmt.getDate(i);
            return date == null ? null : date.toLocalDate();
          });

  MariaOutParam<LocalTime> readLocalTime =
      of(Types.TIME, (stmt, i) -> stmt.getObject(i, LocalTime.class));

  MariaOutParam<LocalDateTime> readLocalDateTime =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toLocalDateTime();
          });

  MariaOutParam<Instant> readInstant =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toInstant();
          });

  MariaOutParam<Year> readYear =
      of(
          Types.DATE,
          (stmt, i) -> {
            var date = stmt.getDate(i);
            return date == null ? null : Year.of(date.toLocalDate().getYear());
          });

  MariaOutParam<Duration> readDuration =
      of(
          Types.TIME,
          (stmt, i) -> {
            var time = stmt.getObject(i, LocalTime.class);
            return time == null ? null : Duration.ofNanos(time.toNanoOfDay());
          });

  @SuppressWarnings("unchecked")
  static <A> MariaOutParam<A> readGeometry(Class<A> cls) {
    return of(
        Types.OTHER,
        (stmt, i) -> {
          var obj = stmt.getObject(i);
          if (obj == null) return null;
          if (cls.isInstance(obj)) return cls.cast(obj);
          return (A) obj;
        });
  }

  /** Create an optional version of this OUT parameter codec. */
  default MariaOutParam<java.util.Optional<A>> opt() {
    var self = this;
    return new MariaOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        self.register(stmt, index);
      }

      @Override
      public java.util.Optional<A> read(CallableStatement stmt, int index) throws SQLException {
        A value = self.read(stmt, index);
        if (stmt.wasNull()) return java.util.Optional.empty();
        return java.util.Optional.ofNullable(value);
      }
    };
  }

  /** Map the result of this OUT parameter codec. */
  default <B> MariaOutParam<B> map(SqlFunction<A, B> f) {
    var self = this;
    return new MariaOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        self.register(stmt, index);
      }

      @Override
      public B read(CallableStatement stmt, int index) throws SQLException {
        A value = self.read(stmt, index);
        return value == null ? null : f.apply(value);
      }
    };
  }

  /** An OUT parameter codec that always throws - for types that don't support OUT parameters. */
  static <T> MariaOutParam<T> notSupported(String typeName) {
    return new MariaOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        throw new SQLException(
            "Type " + typeName + " does not support stored procedure OUT parameters");
      }

      @Override
      public T read(CallableStatement stmt, int index) throws SQLException {
        throw new SQLException(
            "Type " + typeName + " does not support stored procedure OUT parameters");
      }
    };
  }
}
