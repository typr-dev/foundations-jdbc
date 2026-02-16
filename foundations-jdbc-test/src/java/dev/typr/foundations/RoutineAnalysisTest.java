package dev.typr.foundations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoutineAnalysisTest {

  static <T> T withConnection(SqlFunction<Connection, T> f) {
    try {
      return Containers.postgresTransactor().execute(f);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Function analysis
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testFunctionAnalysis_success() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_add(a int4, b int4) RETURNS int4 AS $$
          BEGIN RETURN a + b; END;
          $$ LANGUAGE plpgsql
          """);

      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction("ra_add",
          List.of(ParamDef.in(PgTypes.int4), ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      System.out.println(analysis.report());

      assertTrue("Function analysis should succeed", analysis.succeeded());
      assertTrue("Function should exist", analysis.routineExists());
      assertEquals(RoutineAnalysis.RoutineKind.FUNCTION, analysis.kind());

      return null;
    });
  }

  @Test
  public void testFunctionAnalysis_wrongReturnType() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_concat(a text, b text) RETURNS text AS $$
          BEGIN RETURN a || b; END;
          $$ LANGUAGE plpgsql
          """);

      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction("ra_concat",
          List.of(ParamDef.in(PgTypes.text), ParamDef.in(PgTypes.text)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      System.out.println(analysis.report());

      assertFalse("Should fail with wrong return type", analysis.succeeded());
      assertTrue("Return check should be present", analysis.returnCheck().isPresent());
      assertFalse("Return type should not match", analysis.returnCheck().get().match());

      return null;
    });
  }

  @Test
  public void testFunctionAnalysis_noParams() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_now() RETURNS timestamptz AS $$
          BEGIN RETURN now(); END;
          $$ LANGUAGE plpgsql
          """);

      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction("ra_now",
          List.of(), PgTypes.timestamptz);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      System.out.println(analysis.report());

      assertTrue("No-param function should succeed", analysis.succeeded());
      assertEquals(0, analysis.paramChecks().size());

      return null;
    });
  }

  @Test
  public void testFunctionAnalysis_notFound() {
    withConnection(conn -> {
      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction(
          "ra_nonexistent_func_xyz", List.of(ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      System.out.println(analysis.report());

      assertFalse("Missing function should fail", analysis.succeeded());
      assertFalse("routineExists should be false", analysis.routineExists());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Procedure analysis (via DatabaseMetaData)
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testProcedureAnalysis_voidProcedure() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE PROCEDURE ra_void_proc(a int4, b text) AS $$
          BEGIN END;
          $$ LANGUAGE plpgsql
          """);

      var proc = Procedure.buildVoid("ra_void_proc",
          List.of(ParamDef.in(PgTypes.int4), ParamDef.in(PgTypes.text)));

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeProcedure(proc, conn);

      System.out.println(analysis.report());

      assertTrue("Void procedure analysis should succeed", analysis.succeeded());
      assertTrue("Procedure should exist", analysis.routineExists());
      assertEquals(RoutineAnalysis.RoutineKind.PROCEDURE, analysis.kind());

      return null;
    });
  }

  @Test
  public void testProcedureAnalysis_notFound() {
    withConnection(conn -> {
      var proc = Procedure.buildVoid("ra_nonexistent_proc_xyz",
          List.of(ParamDef.in(PgTypes.int4)));

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeProcedure(proc, conn);

      System.out.println(analysis.report());

      assertFalse("Missing procedure should fail", analysis.succeeded());
      assertFalse("routineExists should be false", analysis.routineExists());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // RoutineAnalyzer.analyzeProcedure dispatches FunctionProcedure to analyzeFunction
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testAnalyzeProcedure_dispatchesFunction() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_double(x int4) RETURNS int4 AS $$
          BEGIN RETURN x * 2; END;
          $$ LANGUAGE plpgsql
          """);

      var func = Procedure.buildFunction("ra_double",
          List.of(ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeProcedure(func, conn);

      System.out.println(analysis.report());

      assertTrue("Function via analyzeProcedure should succeed", analysis.succeeded());
      assertEquals(RoutineAnalysis.RoutineKind.FUNCTION, analysis.kind());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // QueryChecker integration
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testQueryChecker_checkRoutine_success() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_square(x int4) RETURNS int4 AS $$
          BEGIN RETURN x * x; END;
          $$ LANGUAGE plpgsql
          """);

      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction("ra_square",
          List.of(ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      assertTrue("checkRoutine should succeed for valid function", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testQueryChecker_checkRoutine_failsOnMissing() {
    withConnection(conn -> {
      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction(
          "ra_does_not_exist_xyz", List.of(ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      assertFalse("Missing function should fail", analysis.succeeded());
      assertFalse("routineExists should be false", analysis.routineExists());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Report formatting
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testReportFormatting() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE OR REPLACE FUNCTION ra_fmt(x int4) RETURNS text AS $$
          BEGIN RETURN x::text; END;
          $$ LANGUAGE plpgsql
          """);

      var func = (Procedure.FunctionProcedure<?>) Procedure.buildFunction("ra_fmt",
          List.of(ParamDef.in(PgTypes.int4)), PgTypes.int4);

      RoutineAnalysis analysis = RoutineAnalyzer.analyzeFunction(func, conn);

      String plain = analysis.report();
      String colored = analysis.styledReport().render();

      System.out.println("=== Plain ===");
      System.out.println(plain);
      System.out.println("=== Colored ===");
      System.out.println(colored);

      assertTrue("Plain report should contain routine name", plain.contains("ra_fmt"));
      assertTrue("Colored report should contain ANSI codes", colored.contains("\u001b["));

      assertFalse("Wrong return type should fail", analysis.succeeded());

      return null;
    });
  }
}
