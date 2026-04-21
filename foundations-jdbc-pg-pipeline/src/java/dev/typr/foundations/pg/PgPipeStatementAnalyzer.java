package dev.typr.foundations.pg;

import dev.typr.foundations.JdbcMeta;
import dev.typr.foundations.StatementAnalyzer;
import dev.typr.foundations.StatementMeta;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PgPipe implementation of {@link StatementAnalyzer}. Uses the PostgreSQL wire protocol
 * (Parse+Describe) to extract parameter OIDs and column metadata, then resolves OIDs to type names
 * via a preloaded map.
 */
final class PgPipeStatementAnalyzer implements StatementAnalyzer {

  private final PgPipelinePool pool;
  private final Map<Integer, String> oidToTypeName;

  PgPipeStatementAnalyzer(PgPipelinePool pool, Map<Integer, String> oidToTypeName) {
    this.pool = pool;
    this.oidToTypeName = oidToTypeName;
  }

  @Override
  public StatementMeta analyzeStatement(String sql) {
    String pgSql = PgProtocol.convertPlaceholders(sql);
    PgPipelinedConnection.QueryResult meta = pool.analyzeStatement(pgSql);
    var params = buildParameterMeta(meta.paramOids);
    var columns = buildColumnMeta(meta.columns);
    return new StatementMeta(params, columns);
  }

  private List<JdbcMeta.ParameterMeta> buildParameterMeta(int[] paramOids) {
    if (paramOids == null) return List.of();
    List<JdbcMeta.ParameterMeta> result = new ArrayList<>(paramOids.length);
    for (int i = 0; i < paramOids.length; i++) {
      String typeName = oidToTypeName.getOrDefault(paramOids[i], "unknown");
      result.add(
          new JdbcMeta.ParameterMeta(
              i + 1, Types.OTHER, typeName, ParameterMetaData.parameterNullableUnknown));
    }
    return result;
  }

  private List<JdbcMeta.ColumnMeta> buildColumnMeta(PgProtocol.ColumnDesc[] columns) {
    if (columns == null) return List.of();
    List<JdbcMeta.ColumnMeta> result = new ArrayList<>(columns.length);
    for (int i = 0; i < columns.length; i++) {
      PgProtocol.ColumnDesc col = columns[i];
      String typeName = oidToTypeName.getOrDefault(col.typeOid(), "unknown");
      result.add(
          new JdbcMeta.ColumnMeta(
              i + 1,
              Types.OTHER,
              typeName,
              ResultSetMetaData.columnNullableUnknown,
              col.name(),
              col.name()));
    }
    return result;
  }
}
