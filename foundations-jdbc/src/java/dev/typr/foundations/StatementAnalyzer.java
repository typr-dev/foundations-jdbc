package dev.typr.foundations;

/**
 * Backend-specific statement analysis. Prepares a SQL statement and extracts parameter/column
 * metadata without executing it.
 *
 * <p>JDBC implements this via {@code PreparedStatement} metadata. PgPipe implements this via the
 * Parse+Describe wire-protocol messages.
 *
 * <p>Implementations should throw {@link DatabaseException} on failure (e.g. syntax error in SQL).
 * The caller ({@link AnalysisRunner}) catches these to produce structured {@link
 * QueryAnalysis#prepareFailed} results.
 *
 * @see AnalysisRunner
 * @see StatementMeta
 */
public interface StatementAnalyzer {

  /**
   * Analyze a SQL statement: prepare it and extract parameter/column metadata.
   *
   * @param sql the SQL with {@code ?} placeholders (implementations convert to backend-specific
   *     format if needed)
   * @return metadata describing parameters and columns
   * @throws DatabaseException if the statement cannot be prepared
   */
  StatementMeta analyzeStatement(String sql);
}
