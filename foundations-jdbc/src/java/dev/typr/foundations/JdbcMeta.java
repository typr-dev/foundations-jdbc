package dev.typr.foundations;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC metadata extracted from PreparedStatement. Used to compare what the database expects
 * against what our types claim to provide.
 */
public sealed interface JdbcMeta {

  /**
   * Metadata for a query parameter (from ParameterMetaData).
   *
   * @param position 1-indexed position
   * @param jdbcType java.sql.Types constant
   * @param vendorTypeName database-specific type name (e.g., "int4", "varchar")
   * @param nullable ParameterMetaData.parameterNoNulls, parameterNullable, or parameterNullableUnknown
   */
  record ParameterMeta(
      int position,
      int jdbcType,
      String vendorTypeName,
      int nullable
  ) implements JdbcMeta {

    public boolean isNullable() {
      return nullable != ParameterMetaData.parameterNoNulls;
    }

    public boolean isNullabilityKnown() {
      return nullable != ParameterMetaData.parameterNullableUnknown;
    }
  }

  /**
   * Metadata for a result column (from ResultSetMetaData).
   *
   * @param position 1-indexed position
   * @param jdbcType java.sql.Types constant
   * @param vendorTypeName database-specific type name (e.g., "int4", "varchar")
   * @param nullable ResultSetMetaData.columnNoNulls, columnNullable, or columnNullableUnknown
   * @param columnName column name or alias
   * @param columnLabel column label (alias or name)
   */
  record ColumnMeta(
      int position,
      int jdbcType,
      String vendorTypeName,
      int nullable,
      String columnName,
      String columnLabel
  ) implements JdbcMeta {

    public boolean isNullable() {
      return nullable != ResultSetMetaData.columnNoNulls;
    }

    public boolean isNullabilityKnown() {
      return nullable != ResultSetMetaData.columnNullableUnknown;
    }

    public String displayName() {
      return columnLabel != null && !columnLabel.isEmpty() ? columnLabel : columnName;
    }
  }

  /**
   * Extract parameter metadata from a prepared statement.
   * Note: Some databases (MySQL/MariaDB) return unreliable parameter metadata.
   */
  static List<ParameterMeta> extractParameters(PreparedStatement ps) throws SQLException {
    List<ParameterMeta> result = new ArrayList<>();
    try {
      ParameterMetaData pmd = ps.getParameterMetaData();
      int count = pmd.getParameterCount();
      for (int i = 1; i <= count; i++) {
        result.add(new ParameterMeta(
            i,
            pmd.getParameterType(i),
            pmd.getParameterTypeName(i),
            pmd.isNullable(i)
        ));
      }
    } catch (SQLException e) {
      // Some drivers don't support parameter metadata - return empty list
      // The analyzer will handle this gracefully
    }
    return result;
  }

  /**
   * Extract column metadata from a prepared statement (without executing).
   */
  static List<ColumnMeta> extractColumns(PreparedStatement ps) throws SQLException {
    List<ColumnMeta> result = new ArrayList<>();
    ResultSetMetaData rsmd = ps.getMetaData();
    if (rsmd == null) {
      return result;
    }
    int count = rsmd.getColumnCount();
    for (int i = 1; i <= count; i++) {
      result.add(new ColumnMeta(
          i,
          rsmd.getColumnType(i),
          rsmd.getColumnTypeName(i),
          rsmd.isNullable(i),
          rsmd.getColumnName(i),
          rsmd.getColumnLabel(i)
      ));
    }
    return result;
  }

  /**
   * Convert a java.sql.Types constant to a human-readable name.
   */
  static String jdbcTypeName(int jdbcType) {
    return switch (jdbcType) {
      case Types.BIT -> "BIT";
      case Types.TINYINT -> "TINYINT";
      case Types.SMALLINT -> "SMALLINT";
      case Types.INTEGER -> "INTEGER";
      case Types.BIGINT -> "BIGINT";
      case Types.FLOAT -> "FLOAT";
      case Types.REAL -> "REAL";
      case Types.DOUBLE -> "DOUBLE";
      case Types.NUMERIC -> "NUMERIC";
      case Types.DECIMAL -> "DECIMAL";
      case Types.CHAR -> "CHAR";
      case Types.VARCHAR -> "VARCHAR";
      case Types.LONGVARCHAR -> "LONGVARCHAR";
      case Types.DATE -> "DATE";
      case Types.TIME -> "TIME";
      case Types.TIMESTAMP -> "TIMESTAMP";
      case Types.BINARY -> "BINARY";
      case Types.VARBINARY -> "VARBINARY";
      case Types.LONGVARBINARY -> "LONGVARBINARY";
      case Types.NULL -> "NULL";
      case Types.OTHER -> "OTHER";
      case Types.JAVA_OBJECT -> "JAVA_OBJECT";
      case Types.DISTINCT -> "DISTINCT";
      case Types.STRUCT -> "STRUCT";
      case Types.ARRAY -> "ARRAY";
      case Types.BLOB -> "BLOB";
      case Types.CLOB -> "CLOB";
      case Types.REF -> "REF";
      case Types.DATALINK -> "DATALINK";
      case Types.BOOLEAN -> "BOOLEAN";
      case Types.ROWID -> "ROWID";
      case Types.NCHAR -> "NCHAR";
      case Types.NVARCHAR -> "NVARCHAR";
      case Types.LONGNVARCHAR -> "LONGNVARCHAR";
      case Types.NCLOB -> "NCLOB";
      case Types.SQLXML -> "SQLXML";
      case Types.REF_CURSOR -> "REF_CURSOR";
      case Types.TIME_WITH_TIMEZONE -> "TIME_WITH_TIMEZONE";
      case Types.TIMESTAMP_WITH_TIMEZONE -> "TIMESTAMP_WITH_TIMEZONE";
      default -> "UNKNOWN(" + jdbcType + ")";
    };
  }
}
