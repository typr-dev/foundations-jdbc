package dev.typr.foundations.otel;

import dev.typr.foundations.connect.DatabaseKind;

/**
 * Maps {@link DatabaseKind} to OpenTelemetry {@code db.system.name} values per Semantic Conventions
 * v1.39.0.
 */
public enum DatabaseSystem {
  POSTGRESQL("postgresql"),
  MARIADB("mariadb"),
  DUCKDB("duckdb"),
  ORACLE("oracle"),
  SQLSERVER("mssql"),
  DB2("db2"),
  SQLITE("sqlite");

  private final String otelName;

  DatabaseSystem(String otelName) {
    this.otelName = otelName;
  }

  /** The OpenTelemetry {@code db.system.name} attribute value. */
  public String otelName() {
    return otelName;
  }

  /** Create a DatabaseSystem from a {@link DatabaseKind}. */
  public static DatabaseSystem from(DatabaseKind kind) {
    return switch (kind) {
      case POSTGRESQL -> POSTGRESQL;
      case MARIADB -> MARIADB;
      case DUCKDB -> DUCKDB;
      case ORACLE -> ORACLE;
      case SQLSERVER -> SQLSERVER;
      case DB2 -> DB2;
      case SQLITE -> SQLITE;
    };
  }
}
