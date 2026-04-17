package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OUT/INOUT parameter codecs for Oracle types. Each instance handles both registration and reading.
 */
public interface OracleOutParam<A> extends DbOutParam<A> {

  @FunctionalInterface
  interface ReadFn<A> {
    A read(CallableStatement stmt, int index) throws SQLException;
  }

  private static <A> OracleOutParam<A> of(int jdbcType, ReadFn<A> reader) {
    return new OracleOutParam<>() {
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
  OracleOutParam<Boolean> readBoolean =
      of(
          Types.INTEGER,
          (stmt, i) -> {
            int val = stmt.getInt(i);
            return stmt.wasNull() ? null : val != 0;
          });
  OracleOutParam<Short> readShort = of(Types.SMALLINT, CallableStatement::getShort);
  OracleOutParam<Integer> readInteger = of(Types.INTEGER, CallableStatement::getInt);
  OracleOutParam<Long> readLong = of(Types.BIGINT, CallableStatement::getLong);
  OracleOutParam<Float> readFloat = of(Types.FLOAT, CallableStatement::getFloat);
  OracleOutParam<Double> readDouble = of(Types.DOUBLE, CallableStatement::getDouble);
  OracleOutParam<BigDecimal> readBigDecimal = of(Types.NUMERIC, CallableStatement::getBigDecimal);
  OracleOutParam<String> readString = of(Types.VARCHAR, CallableStatement::getString);
  OracleOutParam<byte[]> readByteArray = of(Types.VARBINARY, CallableStatement::getBytes);

  // Date/time types
  OracleOutParam<LocalDate> readLocalDate =
      of(
          Types.DATE,
          (stmt, i) -> {
            var date = stmt.getDate(i);
            return date == null ? null : date.toLocalDate();
          });

  OracleOutParam<LocalDateTime> readLocalDateTime =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toLocalDateTime();
          });

  OracleOutParam<Instant> readInstant =
      of(
          -102,
          (stmt, i) -> {
            OffsetDateTime odt = stmt.getObject(i, OffsetDateTime.class);
            return odt == null ? null : odt.toInstant();
          });

  OracleOutParam<OffsetDateTime> readOffsetDateTime =
      of(2014, (stmt, i) -> stmt.getObject(i, OffsetDateTime.class));

  // Oracle TIMESTAMP WITH TIME ZONE — uses ZonedDateTime to preserve named zone regions
  // (e.g. "America/Los_Angeles"), which OffsetDateTime would discard. Matches
  // OracleRead.readZonedDateTime — see that field for the full rationale.
  OracleOutParam<ZonedDateTime> readZonedDateTime =
      of(2014, (stmt, i) -> stmt.getObject(i, ZonedDateTime.class));

  // Oracle JSON type code = 2016 (oracle.jdbc.OracleTypes.JSON)
  OracleOutParam<String> readJsonAsString = of(2016, CallableStatement::getString);

  /** Create an optional version of this OUT parameter codec. */
  default OracleOutParam<java.util.Optional<A>> opt() {
    var self = this;
    return new OracleOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        self.register(stmt, index);
      }

      @Override
      public java.util.Optional<A> read(CallableStatement stmt, int index) throws SQLException {
        A value = self.read(stmt, index);
        if (stmt.wasNull()) {
          return java.util.Optional.empty();
        }
        return java.util.Optional.ofNullable(value);
      }
    };
  }

  /** Map the result of this OUT parameter codec. */
  default <B> OracleOutParam<B> map(SqlFunction<A, B> f) {
    var self = this;
    return new OracleOutParam<>() {
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

  /** OUT param codec for Oracle named STRUCT (OBJECT) types. */
  static <A> OracleOutParam<A> struct(
      String typeName,
      List<OracleType<?>> oracleColumns,
      java.util.function.Function<Object[], A> decode) {
    return new OracleOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        stmt.registerOutParameter(index, Types.STRUCT, typeName);
      }

      @Override
      public A read(CallableStatement stmt, int index) throws SQLException {
        Object obj = stmt.getObject(index);
        if (obj == null) return null;
        if (!(obj instanceof oracle.sql.STRUCT struct)) {
          throw new SQLException(
              "Expected STRUCT for " + typeName + ", got: " + obj.getClass().getName());
        }
        try {
          Object[] rawAttrs = struct.getAttributes();
          Object[] typedAttrs = new Object[oracleColumns.size()];
          for (int i = 0; i < oracleColumns.size(); i++) {
            typedAttrs[i] = oracleColumns.get(i).read().fromOracleValue(rawAttrs[i]);
          }
          return decode.apply(typedAttrs);
        } catch (SQLException e) {
          throw e;
        } catch (Exception e) {
          throw new SQLException("Failed to read Oracle STRUCT OUT param: " + e.getMessage(), e);
        }
      }
    };
  }

  /** OUT param codec for Oracle named ARRAY (NESTED TABLE / VARRAY) types. */
  static <T> OracleOutParam<List<T>> oracleArray(String typeName, OracleType<T> elementType) {
    return new OracleOutParam<>() {
      @Override
      public void register(CallableStatement stmt, int index) throws SQLException {
        stmt.registerOutParameter(index, Types.ARRAY, typeName);
      }

      @Override
      public List<T> read(CallableStatement stmt, int index) throws SQLException {
        Object obj = stmt.getObject(index);
        if (obj == null) return null;
        if (!(obj instanceof oracle.sql.ARRAY array)) {
          throw new SQLException(
              "Expected ARRAY for " + typeName + ", got: " + obj.getClass().getName());
        }
        try {
          Object[] rawArray = (Object[]) array.getArray();
          List<T> result = new ArrayList<>(rawArray.length);
          for (Object element : rawArray) {
            if (element == null) {
              result.add(null);
            } else {
              @SuppressWarnings("unchecked")
              T converted = (T) elementType.read().fromOracleValue(element);
              result.add(converted);
            }
          }
          return result;
        } catch (SQLException e) {
          throw e;
        } catch (Exception e) {
          throw new SQLException("Failed to read Oracle ARRAY OUT param: " + e.getMessage(), e);
        }
      }
    };
  }

  /** An OUT parameter codec that always throws - for types that don't support OUT parameters. */
  static <T> OracleOutParam<T> notSupported(String typeName) {
    return new OracleOutParam<>() {
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
