package dev.typr.foundations.internal;

import dev.typr.foundations.DatabaseException;
import dev.typr.foundations.JdbcMeta;
import dev.typr.foundations.StatementAnalyzer;
import dev.typr.foundations.StatementMeta;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC implementation of {@link StatementAnalyzer}. Prepares a statement and extracts
 * parameter/column metadata from the JDBC driver.
 */
public final class JdbcStatementAnalyzer implements StatementAnalyzer {

  private final java.sql.Connection conn;

  public JdbcStatementAnalyzer(java.sql.Connection conn) {
    this.conn = conn;
  }

  @Override
  public StatementMeta analyzeStatement(String sql) {
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      try (ps) {
        var params = JdbcMeta.extractParameters(ps);
        var columns = JdbcMeta.extractColumns(ps);
        return new StatementMeta(params, columns);
      }
    } catch (SQLException e) {
      throw new DatabaseException.Jdbc(e);
    }
  }
}
