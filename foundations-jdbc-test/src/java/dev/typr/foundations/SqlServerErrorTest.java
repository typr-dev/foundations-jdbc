package dev.typr.foundations;

import static org.junit.Assert.*;

import java.sql.SQLException;
import org.junit.Test;

public class SqlServerErrorTest {

  @Test
  public void formattedIncludesAllFields() {
    var err =
        new SqlServerError("Invalid column name 'foo'", 207, 16, 1, "MY_SERVER", "my_proc", 5);

    String formatted = err.formatted();
    assertTrue(formatted.contains("ERROR (severity 16)"));
    assertTrue(formatted.contains("Invalid column name 'foo'"));
    assertTrue(formatted.contains("[207]"));
    assertTrue(formatted.contains("Procedure: my_proc, line 5"));
    assertTrue(formatted.contains("Server: MY_SERVER"));
  }

  @Test
  public void formattedOmitsNullFields() {
    var err = new SqlServerError("Login failed", 18456, 14, 1, null, null, 0);

    String formatted = err.formatted();
    assertTrue(formatted.contains("ERROR (severity 14): Login failed [18456]"));
    assertFalse(formatted.contains("Procedure:"));
    assertFalse(formatted.contains("Server:"));
  }

  @Test
  public void formattedWithProcedure() {
    var err = new SqlServerError("Divide by zero", 8134, 16, 1, null, "usp_calculate", 12);

    String formatted = err.formatted();
    assertTrue(formatted.contains("Procedure: usp_calculate, line 12"));
  }

  @Test
  public void formattedWithoutProcedure() {
    var err = new SqlServerError("Syntax error", 102, 15, 1, "SRV01", null, 3);

    String formatted = err.formatted();
    assertFalse(formatted.contains("Procedure:"));
    assertTrue(formatted.contains("Server: SRV01"));
  }

  @Test
  public void zeroLineNumber() {
    var err = new SqlServerError("Some error", 50000, 16, 1, null, "my_proc", 0);

    String formatted = err.formatted();
    assertTrue(formatted.contains("Procedure: my_proc"));
    assertFalse(formatted.contains("line 0"));
  }

  @Test
  public void severityInOutput() {
    var err = new SqlServerError("Warning", 100, 10, 0, null, null, 0);

    String formatted = err.formatted();
    assertTrue(formatted.contains("severity 10"));
  }

  @Test
  public void sqlStateFromError() {
    var cause = new SQLException("test", "S0001", 207);
    var err = new SqlServerError("Invalid column", 207, 16, 1, null, null, 0);
    var ex = new DatabaseException.SqlServer(err, null, cause);

    assertEquals("S0001", ex.sqlState());
  }

  @Test
  public void sqlStateWithoutCause() {
    var err = new SqlServerError("Invalid column", 207, 16, 1, null, null, 0);
    var ex = new DatabaseException.SqlServer(err, null);

    assertNull(ex.sqlState());
  }

  @Test
  public void sqlAccessorPresent() {
    var err = new SqlServerError("Error", 207, 16, 1, null, null, 0);
    var ex = new DatabaseException.SqlServer(err, "SELECT foo FROM bar");

    assertTrue(ex.sql().isPresent());
    assertEquals("SELECT foo FROM bar", ex.sql().get());
  }

  @Test
  public void sqlAccessorEmpty() {
    var err = new SqlServerError("Error", 207, 16, 1, null, null, 0);
    var ex = new DatabaseException.SqlServer(err, null);

    assertTrue(ex.sql().isEmpty());
  }

  @Test
  public void databaseExceptionSealedHierarchy() {
    var pgError =
        new PgError(
            "ERROR", "msg", "23505", null, null, null, null, null, null, null, null, null, null,
            null, null, null, null);
    DatabaseException pg = new DatabaseException.Postgres(pgError, null);

    var ssError = new SqlServerError("msg", 207, 16, 1, null, null, 0);
    DatabaseException ss = new DatabaseException.SqlServer(ssError, null);

    DatabaseException jdbc = new DatabaseException.Jdbc(new SQLException("test"));

    assertTrue(pg instanceof DatabaseException.Postgres);
    assertFalse(pg instanceof DatabaseException.SqlServer);
    assertFalse(pg instanceof DatabaseException.Jdbc);

    assertTrue(ss instanceof DatabaseException.SqlServer);
    assertFalse(ss instanceof DatabaseException.Postgres);
    assertFalse(ss instanceof DatabaseException.Jdbc);

    assertTrue(jdbc instanceof DatabaseException.Jdbc);
    assertFalse(jdbc instanceof DatabaseException.Postgres);
    assertFalse(jdbc instanceof DatabaseException.SqlServer);
  }

  @Test
  public void jdbcFallbackForUnknownDriver() {
    var cause = new SQLException("Unknown driver error", "HY000", 9999);
    DatabaseException ex = new DatabaseException.Jdbc(cause);

    assertTrue(ex instanceof DatabaseException.Jdbc);
    assertEquals("HY000", ex.sqlState());
    assertEquals(9999, ((DatabaseException.Jdbc) ex).vendorCode());
    assertSame(cause, ((DatabaseException.Jdbc) ex).sqlException());
  }

  @Test
  public void exceptionMessageContainsFormattedError() {
    var err =
        new SqlServerError("Invalid column name 'foo'", 207, 16, 1, "MY_SERVER", "my_proc", 5);
    var ex = new DatabaseException.SqlServer(err, "SELECT foo FROM bar");

    assertTrue(ex.getMessage().contains("ERROR (severity 16)"));
    assertTrue(ex.getMessage().contains("Invalid column name 'foo'"));
    assertTrue(ex.getMessage().contains("[207]"));
  }
}
