package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;

/**
 * OUT/INOUT parameter codecs for SQL Server types. Each instance handles both registration and
 * reading for its JDBC type.
 */
public interface SqlServerOutParam<A> extends DbOutParam<A> {

  @FunctionalInterface
  interface ReadFn<A> {
    A read(CallableStatement stmt, int index) throws SQLException;
  }

  private static <A> SqlServerOutParam<A> of(int jdbcType, ReadFn<A> reader) {
    return new SqlServerOutParam<>() {
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

  SqlServerOutParam<Boolean> readBoolean = of(Types.BIT, CallableStatement::getBoolean);
  SqlServerOutParam<Byte> readByte = of(Types.TINYINT, CallableStatement::getByte);
  SqlServerOutParam<Short> readShort = of(Types.SMALLINT, CallableStatement::getShort);
  SqlServerOutParam<Integer> readInteger = of(Types.INTEGER, CallableStatement::getInt);
  SqlServerOutParam<Long> readLong = of(Types.BIGINT, CallableStatement::getLong);
  SqlServerOutParam<Float> readFloat = of(Types.REAL, CallableStatement::getFloat);
  SqlServerOutParam<Double> readDouble = of(Types.DOUBLE, CallableStatement::getDouble);
  SqlServerOutParam<BigDecimal> readBigDecimal =
      of(Types.DECIMAL, CallableStatement::getBigDecimal);
  SqlServerOutParam<String> readString = of(Types.VARCHAR, CallableStatement::getString);
  SqlServerOutParam<byte[]> readByteArray = of(Types.VARBINARY, CallableStatement::getBytes);

  SqlServerOutParam<LocalDate> readLocalDate =
      of(
          Types.DATE,
          (stmt, i) -> {
            var date = stmt.getDate(i);
            return date == null ? null : date.toLocalDate();
          });

  SqlServerOutParam<LocalTime> readLocalTime =
      of(
          Types.TIME,
          (stmt, i) -> {
            var time = stmt.getTime(i);
            return time == null ? null : time.toLocalTime();
          });

  SqlServerOutParam<LocalDateTime> readLocalDateTime =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toLocalDateTime();
          });

  SqlServerOutParam<Instant> readInstant =
      of(
          Types.TIMESTAMP,
          (stmt, i) -> {
            var ts = stmt.getTimestamp(i);
            return ts == null ? null : ts.toInstant();
          });

  SqlServerOutParam<OffsetDateTime> readOffsetDateTime =
      new SqlServerOutParam<>() {
        @Override
        public void register(CallableStatement stmt, int index) throws SQLException {
          stmt.registerOutParameter(index, microsoft.sql.Types.DATETIMEOFFSET);
        }

        @Override
        public OffsetDateTime read(CallableStatement stmt, int index) throws SQLException {
          return stmt.getObject(index, OffsetDateTime.class);
        }
      };

  SqlServerOutParam<String> readXmlAsString =
      of(
          Types.SQLXML,
          (stmt, i) -> {
            java.sql.SQLXML sqlxml = stmt.getSQLXML(i);
            return sqlxml == null ? null : sqlxml.getString();
          });

  SqlServerOutParam<Object> readSqlVariant =
      new SqlServerOutParam<>() {
        @Override
        public void register(CallableStatement stmt, int index) throws SQLException {
          stmt.registerOutParameter(index, microsoft.sql.Types.SQL_VARIANT);
        }

        @Override
        public Object read(CallableStatement stmt, int index) throws SQLException {
          return stmt.getObject(index);
        }
      };

  // Note: SQL Server JDBC driver doesn't support spatial types as OUT parameters.
  // registerOutParameter fails with AssertionError for vendor type codes,
  // and Types.OTHER triggers "Implicit conversion from geography/geometry to varbinary is not
  // allowed".

  // Note: no readHierarchyId — SQL Server JDBC driver doesn't support implicit conversion
  // from hierarchyid to any JDBC type for OUT parameters

  /** Create an optional version of this OUT parameter codec. */
  default SqlServerOutParam<java.util.Optional<A>> opt() {
    var self = this;
    return new SqlServerOutParam<>() {
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
  default <B> SqlServerOutParam<B> map(SqlFunction<A, B> f) {
    var self = this;
    return new SqlServerOutParam<>() {
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
  static <T> SqlServerOutParam<T> notSupported(String typeName) {
    return new SqlServerOutParam<>() {
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
