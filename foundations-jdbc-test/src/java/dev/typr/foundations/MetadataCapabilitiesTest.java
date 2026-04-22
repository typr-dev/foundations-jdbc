package dev.typr.foundations;

import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests what JDBC metadata each database actually provides. Used to verify the Database Support
 * table in the query analysis docs.
 *
 * <p>Each test creates a table with nullable and non-nullable columns, then checks what
 * ParameterMetaData and ResultSetMetaData report.
 *
 * <p>Summary of findings:
 *
 * <pre>
 * Database      | Param Types | Param Nullability | Col Types | Col Nullability
 * --------------|-------------|-------------------|-----------|----------------
 * PostgreSQL    | Yes         | Unknown           | Yes       | Reliable
 * DuckDB        | None        | Unknown           | Yes       | All nullable
 * Oracle        | Yes         | Yes               | Yes       | Reliable
 * SQL Server    | Yes         | Unknown           | Yes       | Reliable
 * MariaDB/MySQL | None*       | None*             | Yes       | Reliable
 * DB2           | Yes         | All nullable**    | Yes       | Reliable
 * </pre>
 *
 * * MariaDB throws exception on getParameterMetaData() ** DB2 reports all parameters as nullable
 * regardless of column constraints
 */
public class MetadataCapabilitiesTest {

  record MetaReport(
      String database, boolean paramMetaFailed, List<ParamInfo> params, List<ColInfo> columns) {
    void print() {
      System.out.println("\n=== " + database + " ===");
      if (paramMetaFailed) {
        System.out.println("Parameters: UNSUPPORTED (exception thrown)");
      } else {
        System.out.println("Parameters:");
        for (ParamInfo p : params) {
          System.out.println(
              "  param["
                  + p.position
                  + "]: type="
                  + p.typeName
                  + " jdbcType="
                  + JdbcMeta.jdbcTypeName(p.jdbcType)
                  + " nullable="
                  + nullableStr(p.nullable, true));
        }
      }
      System.out.println("Columns:");
      for (ColInfo c : columns) {
        System.out.println(
            "  col["
                + c.position
                + "] "
                + c.name
                + ": type="
                + c.typeName
                + " jdbcType="
                + JdbcMeta.jdbcTypeName(c.jdbcType)
                + " nullable="
                + nullableStr(c.nullable, false));
      }
    }

    private String nullableStr(int nullable, boolean isParam) {
      if (isParam) {
        return switch (nullable) {
          case ParameterMetaData.parameterNoNulls -> "NO_NULLS";
          case ParameterMetaData.parameterNullable -> "NULLABLE";
          case ParameterMetaData.parameterNullableUnknown -> "UNKNOWN";
          default -> "?" + nullable;
        };
      } else {
        return switch (nullable) {
          case ResultSetMetaData.columnNoNulls -> "NO_NULLS";
          case ResultSetMetaData.columnNullable -> "NULLABLE";
          case ResultSetMetaData.columnNullableUnknown -> "UNKNOWN";
          default -> "?" + nullable;
        };
      }
    }
  }

  record ParamInfo(int position, String typeName, int jdbcType, int nullable) {}

  record ColInfo(int position, String name, String typeName, int jdbcType, int nullable) {}

  private static MetaReport probe(String database, java.sql.Connection conn) throws SQLException {
    String createTable;
    if (database.equals("SQL Server")) {
      createTable =
          """
          CREATE TABLE meta_test (
              id INT NOT NULL,
              name NVARCHAR(100) NOT NULL,
              email NVARCHAR(200),
              age INT
          )
          """;
    } else if (database.equals("Oracle")) {
      createTable =
          """
          CREATE TABLE meta_test (
              id NUMBER(10) NOT NULL,
              name VARCHAR2(100) NOT NULL,
              email VARCHAR2(200),
              age NUMBER(10)
          )
          """;
    } else {
      createTable =
          """
          CREATE TABLE meta_test (
              id INTEGER NOT NULL,
              name VARCHAR(100) NOT NULL,
              email VARCHAR(200),
              age INTEGER
          )
          """;
    }

    String selectSql = "SELECT id, name, email, age FROM meta_test WHERE id = ? AND name = ?";

    conn.createStatement().execute(createTable);

    boolean paramMetaFailed = false;
    List<ParamInfo> params = new ArrayList<>();
    List<ColInfo> columns = new ArrayList<>();

    try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
      try {
        ParameterMetaData pmd = ps.getParameterMetaData();
        int paramCount = pmd.getParameterCount();
        for (int i = 1; i <= paramCount; i++) {
          params.add(
              new ParamInfo(
                  i, pmd.getParameterTypeName(i), pmd.getParameterType(i), pmd.isNullable(i)));
        }
      } catch (SQLException e) {
        paramMetaFailed = true;
      }

      ResultSetMetaData rsmd = ps.getMetaData();
      if (rsmd != null) {
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
          columns.add(
              new ColInfo(
                  i,
                  rsmd.getColumnName(i),
                  rsmd.getColumnTypeName(i),
                  rsmd.getColumnType(i),
                  rsmd.isNullable(i)));
        }
      }
    }

    var report = new MetaReport(database, paramMetaFailed, params, columns);
    report.print();

    conn.createStatement().execute("DROP TABLE meta_test");
    return report;
  }

  private static void assertColumnNullabilityReliable(MetaReport report) {
    assertEquals("Should have 4 columns", 4, report.columns.size());
    ColInfo idCol = report.columns.get(0);
    ColInfo nameCol = report.columns.get(1);
    ColInfo emailCol = report.columns.get(2);
    ColInfo ageCol = report.columns.get(3);

    assertEquals(
        "id (NOT NULL) should be columnNoNulls", ResultSetMetaData.columnNoNulls, idCol.nullable);
    assertEquals(
        "name (NOT NULL) should be columnNoNulls",
        ResultSetMetaData.columnNoNulls,
        nameCol.nullable);
    assertEquals(
        "email (nullable) should be columnNullable",
        ResultSetMetaData.columnNullable,
        emailCol.nullable);
    assertEquals(
        "age (nullable) should be columnNullable",
        ResultSetMetaData.columnNullable,
        ageCol.nullable);
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // DuckDB (embedded, no Docker)
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void duckdbMetadata() throws SQLException {
    try (var conn = DriverManager.getConnection("jdbc:duckdb:")) {
      MetaReport report = probe("DuckDB", conn);

      // DuckDB parameter metadata: count is reported but type info is null
      assertFalse("DuckDB should not throw on getParameterMetaData", report.paramMetaFailed);
      assertEquals("DuckDB reports parameter count", 2, report.params.size());
      for (ParamInfo p : report.params) {
        assertNull("DuckDB param type name is null", p.typeName);
        assertEquals(
            "DuckDB param nullability is unknown",
            ParameterMetaData.parameterNullableUnknown,
            p.nullable);
      }

      // DuckDB column metadata: types are available
      assertEquals("Should have 4 columns", 4, report.columns.size());
      for (ColInfo c : report.columns) {
        assertNotNull("DuckDB col type name should not be null", c.typeName);
      }

      // DuckDB nullability: all columns reported as nullable (even NOT NULL ones)
      for (ColInfo c : report.columns) {
        assertEquals(
            "DuckDB reports all columns as nullable: " + c.name,
            ResultSetMetaData.columnNullable,
            c.nullable);
      }
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // PostgreSQL
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void postgresMetadata() throws SQLException {
    Containers.postgresTransactor()
        .transact(
            mc -> {
              var conn = mc.unwrap();
              MetaReport report = probe("PostgreSQL", conn);

              // PostgreSQL parameter metadata: type names available, nullability unknown
              assertFalse("PG should not throw on getParameterMetaData", report.paramMetaFailed);
              assertEquals("PG should report 2 parameters", 2, report.params.size());
              for (ParamInfo p : report.params) {
                assertNotNull("PG param type name should not be null", p.typeName);
                assertEquals(
                    "PG param nullability is always unknown",
                    ParameterMetaData.parameterNullableUnknown,
                    p.nullable);
              }

              // PostgreSQL column metadata: types and nullability are reliable
              assertColumnNullabilityReliable(report);
              return null;
            });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // MariaDB
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void mariadbMetadata() throws SQLException {
    Containers.mariadbTransactor()
        .transact(
            mc -> {
              var conn = mc.unwrap();
              MetaReport report = probe("MariaDB", conn);

              // MariaDB parameter metadata: throws exception (not supported)
              assertTrue("MariaDB should throw on getParameterMetaData", report.paramMetaFailed);

              // MariaDB column metadata: types and nullability are reliable
              assertColumnNullabilityReliable(report);
              return null;
            });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // SQL Server
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void sqlserverMetadata() throws SQLException {
    Containers.sqlserverTransactor()
        .transact(
            mc -> {
              var conn = mc.unwrap();
              MetaReport report = probe("SQL Server", conn);

              // SQL Server parameter metadata: type names available, nullability unknown
              assertFalse("MSSQL should not throw on getParameterMetaData", report.paramMetaFailed);
              assertEquals("MSSQL should report 2 parameters", 2, report.params.size());
              for (ParamInfo p : report.params) {
                assertNotNull("MSSQL param type name should not be null", p.typeName);
                assertEquals(
                    "MSSQL param nullability is unknown",
                    ParameterMetaData.parameterNullableUnknown,
                    p.nullable);
              }

              // SQL Server column metadata: types and nullability are reliable
              assertColumnNullabilityReliable(report);
              return null;
            });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Oracle
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void oracleMetadata() throws SQLException {
    Containers.oracleTransactor()
        .transact(
            mc -> {
              var conn = mc.unwrap();
              MetaReport report = probe("Oracle", conn);

              // Oracle parameter metadata: type names AND nullability available
              assertFalse(
                  "Oracle should not throw on getParameterMetaData", report.paramMetaFailed);
              assertEquals("Oracle should report 2 parameters", 2, report.params.size());
              for (ParamInfo p : report.params) {
                assertNotNull("Oracle param type name should not be null", p.typeName);
                assertNotEquals(
                    "Oracle param nullability is known",
                    ParameterMetaData.parameterNullableUnknown,
                    p.nullable);
              }

              // Oracle column metadata: types and nullability are reliable
              assertColumnNullabilityReliable(report);
              return null;
            });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // DB2
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void db2Metadata() throws SQLException {
    Containers.db2Transactor()
        .transact(
            mc -> {
              var conn = mc.unwrap();
              MetaReport report = probe("DB2", conn);

              // DB2 parameter metadata: type names available, but nullability unreliable
              // (reports all as NULLABLE regardless of column constraints)
              assertFalse("DB2 should not throw on getParameterMetaData", report.paramMetaFailed);
              assertEquals("DB2 should report 2 parameters", 2, report.params.size());
              for (ParamInfo p : report.params) {
                assertNotNull("DB2 param type name should not be null", p.typeName);
                assertEquals(
                    "DB2 param nullability is always NULLABLE (unreliable)",
                    ParameterMetaData.parameterNullable,
                    p.nullable);
              }

              // DB2 column metadata: types and nullability are reliable
              assertColumnNullabilityReliable(report);
              return null;
            });
  }
}
