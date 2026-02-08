package dev.typr.foundations;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Codec for stored procedure OUT/INOUT parameters. Handles both registration and reading.
 *
 * <p>Each database-specific implementation (e.g. {@code PgOutParam}, {@code SqlServerOutParam})
 * provides the correct JDBC type registration and reading logic for its types.
 *
 * @param <A> the Java type this codec reads
 * @see DbType#outParam()
 */
public interface DbOutParam<A> {
  /**
   * Register the OUT parameter at the given index with the correct JDBC type.
   *
   * @param stmt the CallableStatement to register on
   * @param index the 1-based parameter index
   * @throws SQLException if a database access error occurs
   */
  void register(CallableStatement stmt, int index) throws SQLException;

  /**
   * Read an OUT/INOUT parameter value from the CallableStatement.
   *
   * @param stmt the CallableStatement to read from
   * @param index the 1-based parameter index
   * @return the value read from the parameter
   * @throws SQLException if a database access error occurs
   */
  A read(CallableStatement stmt, int index) throws SQLException;
}
