package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;
import java.util.UUID;

/**
 * OUT/INOUT parameter codec for PostgreSQL types. Handles both JDBC type registration and value
 * reading from CallableStatement.
 */
public interface PgOutParam<A> extends DbOutParam<A> {

  @FunctionalInterface
  interface ReadFn<A> {
    A read(CallableStatement stmt, int index) throws SQLException;
  }

  private static <A> PgOutParam<A> of(int jdbcType, ReadFn<A> reader) {
    return new PgOutParam<>() {
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
  PgOutParam<Boolean> readBoolean = of(Types.BOOLEAN, (stmt, i) -> stmt.getBoolean(i));
  PgOutParam<Short> readShort = of(Types.SMALLINT, (stmt, i) -> stmt.getShort(i));
  PgOutParam<Integer> readInteger = of(Types.INTEGER, (stmt, i) -> stmt.getInt(i));
  PgOutParam<Long> readLong = of(Types.BIGINT, (stmt, i) -> stmt.getLong(i));
  PgOutParam<Float> readFloat = of(Types.REAL, (stmt, i) -> stmt.getFloat(i));
  PgOutParam<Double> readDouble = of(Types.DOUBLE, (stmt, i) -> stmt.getDouble(i));
  PgOutParam<BigDecimal> readBigDecimal = of(Types.NUMERIC, (stmt, i) -> stmt.getBigDecimal(i));
  PgOutParam<String> readString = of(Types.VARCHAR, (stmt, i) -> stmt.getString(i));
  PgOutParam<byte[]> readByteArray = of(Types.BINARY, (stmt, i) -> stmt.getBytes(i));

  // Date/time types
  PgOutParam<LocalDate> readLocalDate =
      of(
          Types.DATE,
          (stmt, i) -> {
            var date = stmt.getDate(i);
            return date == null ? null : date.toLocalDate();
          });

  PgOutParam<LocalTime> readLocalTime =
      of(
          Types.TIME,
          (stmt, i) -> {
            var time = stmt.getTime(i);
            return time == null ? null : time.toLocalTime();
          });

  PgOutParam<LocalDateTime> readLocalDateTime =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toLocalDateTime();
          });

  PgOutParam<Instant> readInstant =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toInstant();
          });

  PgOutParam<OffsetTime> readOffsetTime =
      of(2013, (stmt, i) -> stmt.getObject(i, OffsetTime.class));

  // UUID
  PgOutParam<UUID> readUUID =
      of(
          Types.OTHER,
          (stmt, i) -> {
            var obj = stmt.getObject(i);
            if (obj == null) return null;
            if (obj instanceof UUID) return (UUID) obj;
            return UUID.fromString(obj.toString());
          });

  // Object-based (for PGobject types - returns string representation)
  PgOutParam<String> readObjectAsString =
      of(
          Types.OTHER,
          (stmt, i) -> {
            var obj = stmt.getObject(i);
            return obj == null ? null : obj.toString();
          });

  @SuppressWarnings("unchecked")
  PgOutParam<java.util.Map<String, String>> readMapStringString =
      of(Types.OTHER, (stmt, i) -> (java.util.Map<String, String>) stmt.getObject(i));

  /** Create an OUT param codec that gets the object and casts it to the expected type. */
  static <T> PgOutParam<T> castTo(Class<T> clazz) {
    return of(Types.OTHER, (stmt, i) -> stmt.getObject(i, clazz));
  }

  /** Create an OUT param codec that gets the string value from a PGobject-based type. */
  static <T> PgOutParam<T> pgObject(SqlFunction<String, T> constructor) {
    return of(
        Types.OTHER,
        (stmt, i) -> {
          var obj = stmt.getObject(i);
          if (obj == null) return null;
          String value;
          if (obj instanceof org.postgresql.util.PGobject pgObj) {
            value = pgObj.getValue();
          } else {
            value = obj.toString();
          }
          return constructor.apply(value);
        });
  }

  static <T> PgOutParam<T> bitString(SqlFunction<String, T> constructor) {
    return of(
        Types.OTHER,
        (stmt, i) -> {
          var obj = stmt.getObject(i);
          if (obj == null) return null;
          String value;
          if (obj instanceof org.postgresql.util.PGobject pgObj) {
            value = pgObj.getValue();
          } else if (obj instanceof Boolean b) {
            value = b ? "1" : "0";
          } else {
            value = obj.toString();
          }
          return constructor.apply(value);
        });
  }

  /** Create an optional version of this OUT param codec. */
  default PgOutParam<java.util.Optional<A>> opt() {
    var self = this;
    return new PgOutParam<>() {
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

  /** Map the result of this OUT param codec. */
  default <B> PgOutParam<B> map(SqlFunction<A, B> f) {
    var self = this;
    return new PgOutParam<>() {
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

  /** Read a PG array from CallableStatement as {@code List<T>}. Preserves null elements. */
  static <T> PgOutParam<java.util.List<T>> list() {
    return of(
        Types.ARRAY,
        (stmt, i) -> {
          var arr = stmt.getArray(i);
          if (arr == null) return null;
          Object[] objects = (Object[]) arr.getArray();
          java.util.List<T> result = new java.util.ArrayList<>(objects.length);
          for (Object o : objects) {
            @SuppressWarnings("unchecked")
            T element = (T) o;
            result.add(element);
          }
          return result;
        });
  }

  /** Read a PG array from CallableStatement with element text parsing. */
  static <T> PgOutParam<java.util.List<T>> parsedList(SqlFunction<String, T> parseElement) {
    return of(
        Types.ARRAY,
        (stmt, i) -> {
          var arr = stmt.getArray(i);
          if (arr == null) return null;
          Object[] objects = (Object[]) arr.getArray();
          java.util.List<T> result = new java.util.ArrayList<>(objects.length);
          for (Object o : objects) {
            if (o == null) {
              result.add(null);
              continue;
            }
            String text;
            if (o instanceof org.postgresql.util.PGobject pg) {
              text = pg.getValue();
            } else {
              text = o.toString();
            }
            result.add(parseElement.apply(text));
          }
          return result;
        });
  }

  /** An OUT param codec that always throws - for types that don't support OUT parameters. */
  static <T> PgOutParam<T> notSupported(String typeName) {
    return new PgOutParam<>() {
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
