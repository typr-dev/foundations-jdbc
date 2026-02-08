package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;

/**
 * OUT/INOUT parameter codecs for DB2 types.
 * Each instance handles both registration and reading.
 */
public interface Db2OutParam<A> extends DbOutParam<A> {

  @FunctionalInterface
  interface ReadFn<A> {
    A read(CallableStatement stmt, int index) throws SQLException;
  }

  private static <A> Db2OutParam<A> of(int jdbcType, ReadFn<A> reader) {
    return new Db2OutParam<>() {
      @Override public void register(CallableStatement stmt, int index) throws SQLException {
        stmt.registerOutParameter(index, jdbcType);
      }
      @Override public A read(CallableStatement stmt, int index) throws SQLException {
        return reader.read(stmt, index);
      }
    };
  }

  // Primitive types
  Db2OutParam<Boolean> readBoolean = of(Types.BOOLEAN, CallableStatement::getBoolean);
  Db2OutParam<Short> readShort = of(Types.SMALLINT, CallableStatement::getShort);
  Db2OutParam<Integer> readInteger = of(Types.INTEGER, CallableStatement::getInt);
  Db2OutParam<Long> readLong = of(Types.BIGINT, CallableStatement::getLong);
  Db2OutParam<Float> readFloat = of(Types.REAL, CallableStatement::getFloat);
  Db2OutParam<Double> readDouble = of(Types.DOUBLE, CallableStatement::getDouble);
  Db2OutParam<BigDecimal> readBigDecimal = of(Types.DECIMAL, CallableStatement::getBigDecimal);
  Db2OutParam<String> readString = of(Types.VARCHAR, CallableStatement::getString);
  Db2OutParam<byte[]> readByteArray = of(Types.VARBINARY, CallableStatement::getBytes);

  // Date/time types
  Db2OutParam<LocalDate> readLocalDate = of(Types.DATE, (stmt, i) -> {
    var date = stmt.getDate(i);
    return date == null ? null : date.toLocalDate();
  });

  Db2OutParam<LocalTime> readLocalTime = of(Types.TIME, (stmt, i) -> {
    var time = stmt.getTime(i);
    return time == null ? null : time.toLocalTime();
  });

  Db2OutParam<LocalDateTime> readLocalDateTime = of(Types.TIMESTAMP, (stmt, i) -> {
    var ts = stmt.getTimestamp(i);
    return ts == null ? null : ts.toLocalDateTime();
  });

  Db2OutParam<Instant> readInstant = of(Types.TIMESTAMP, (stmt, i) -> {
    var ts = stmt.getTimestamp(i);
    return ts == null ? null : ts.toInstant();
  });

  Db2OutParam<String> readXmlAsString = of(Types.SQLXML, (stmt, i) -> {
    java.sql.SQLXML sqlxml = stmt.getSQLXML(i);
    return sqlxml == null ? null : sqlxml.getString();
  });

  Db2OutParam<byte[]> readRowId = of(Types.ROWID, (stmt, i) -> stmt.getBytes(i));

  Db2OutParam<Object> readObject = of(Types.OTHER, (stmt, i) -> stmt.getObject(i));

  /**
   * Create an optional version of this OUT parameter codec.
   */
  default Db2OutParam<java.util.Optional<A>> opt() {
    var self = this;
    return new Db2OutParam<>() {
      @Override public void register(CallableStatement stmt, int index) throws SQLException {
        self.register(stmt, index);
      }
      @Override public java.util.Optional<A> read(CallableStatement stmt, int index) throws SQLException {
        A value = self.read(stmt, index);
        if (stmt.wasNull()) return java.util.Optional.empty();
        return java.util.Optional.ofNullable(value);
      }
    };
  }

  /**
   * Map the result of this OUT parameter codec.
   */
  default <B> Db2OutParam<B> map(SqlFunction<A, B> f) {
    var self = this;
    return new Db2OutParam<>() {
      @Override public void register(CallableStatement stmt, int index) throws SQLException {
        self.register(stmt, index);
      }
      @Override public B read(CallableStatement stmt, int index) throws SQLException {
        A value = self.read(stmt, index);
        return value == null ? null : f.apply(value);
      }
    };
  }

  /**
   * An OUT parameter codec that always throws - for types that don't support OUT parameters.
   */
  static <T> Db2OutParam<T> notSupported(String typeName) {
    return new Db2OutParam<>() {
      @Override public void register(CallableStatement stmt, int index) throws SQLException {
        throw new SQLException("Type " + typeName + " does not support stored procedure OUT parameters");
      }
      @Override public T read(CallableStatement stmt, int index) throws SQLException {
        throw new SQLException("Type " + typeName + " does not support stored procedure OUT parameters");
      }
    };
  }
}
