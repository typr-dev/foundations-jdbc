package dev.typr.foundations.pg;

import dev.typr.foundations.AnalysisRunner;
import dev.typr.foundations.Analyzable;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.Procedure;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RoutineAnalysis;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.StatementAnalyzer;
import dev.typr.foundations.Transactor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link QueryChecker} implementation that uses the PostgreSQL wire protocol directly
 * (Parse+Describe) instead of JDBC. This enables query analysis without a JDBC connection, using
 * PgPipe's pipelining infrastructure.
 *
 * <p>Works by:
 *
 * <ol>
 *   <li>Loading an OID-to-type-name map from {@code pg_type} once at creation time
 *   <li>For each analyzed query: sending Parse+Describe to get parameter OIDs and column metadata
 *   <li>Resolving OIDs to vendor type names and building {@link QueryAnalysis} results
 * </ol>
 */
public final class PgPipeQueryChecker implements QueryChecker {

  private final PgPipelinePool pool;
  private final StatementAnalyzer statementAnalyzer;

  private PgPipeQueryChecker(PgPipelinePool pool, StatementAnalyzer statementAnalyzer) {
    this.pool = pool;
    this.statementAnalyzer = statementAnalyzer;
  }

  /** Create a PgPipeQueryChecker backed by the given pool. Loads the OID map from the database. */
  public static PgPipeQueryChecker create(TransactorPgPipe transactor) {
    if (!(transactor instanceof PgPipelinePool pgPool)) {
      throw new PgPipelineException("PgPipeQueryChecker requires a PgPipelinePool instance");
    }
    Map<Integer, String> oidMap = loadOidMap(pgPool);
    StatementAnalyzer analyzer = new PgPipeStatementAnalyzer(pgPool, oidMap);
    return new PgPipeQueryChecker(pgPool, analyzer);
  }

  @Override
  public Transactor transactor() {
    return pool;
  }

  @Override
  public List<QueryAnalysis> doAnalyze(Analyzable analyzable) {
    return AnalysisRunner.analyze(analyzable, statementAnalyzer);
  }

  @Override
  public QueryAnalysis doAnalyzeFragmentAndParser(Fragment fragment, ResultSetParser<?> parser) {
    return AnalysisRunner.analyzeFragmentAndParser(
        Optional.empty(), fragment, parser, statementAnalyzer);
  }

  @Override
  public RoutineAnalysis doAnalyzeRoutine(Procedure<?> procedure) {
    if (procedure instanceof Procedure.FunctionProcedure<?> func) {
      return analyzeFunction(func);
    } else if (procedure instanceof Procedure.VoidProcedure vp) {
      return analyzeProcedureParams(vp.name(), vp.params());
    } else if (procedure instanceof Procedure.OutProcedure<?> op) {
      return analyzeProcedureParams(op.name(), op.params());
    }
    throw new IllegalArgumentException("Unknown procedure type: " + procedure.getClass());
  }

  // ========== Routine analysis ==========

  private RoutineAnalysis analyzeFunction(Procedure.FunctionProcedure<?> func) {
    var sb = new StringBuilder("SELECT ");
    sb.append(func.name()).append('(');
    var inParams = func.inParams();
    for (int i = 0; i < inParams.size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append("null::").append(inParams.get(i).type().typename().sqlType());
    }
    sb.append(')');

    String pgSql = sb.toString();
    PgPipelinedConnection.QueryResult meta;
    try {
      meta = pool.analyzeStatement(pgSql);
    } catch (RuntimeException ex) {
      return new RoutineAnalysis(
          func.name(), RoutineAnalysis.RoutineKind.FUNCTION, List.of(), Optional.empty(), false);
    }

    Map<Integer, String> oidMap = loadOidMap(pool);
    String returnedTypeName = "";
    if (meta.columns != null && meta.columns.length > 0) {
      returnedTypeName =
          QueryAnalysis.normalizeVendorTypeName(
              oidMap.getOrDefault(meta.columns[0].typeOid(), "unknown"));
    }

    List<RoutineAnalysis.ParamCheck> checks = new ArrayList<>();
    for (int i = 0; i < inParams.size(); i++) {
      var p = inParams.get(i);
      checks.add(
          new RoutineAnalysis.ParamCheck(
              i + 1,
              p.type().typename().sqlType(),
              p.type().typename().sqlType(),
              "IN",
              "IN",
              true,
              true));
    }

    boolean returnMatch =
        !returnedTypeName.isEmpty()
            && func.returnType().vendorTypeNames().contains(returnedTypeName);

    return new RoutineAnalysis(
        func.name(),
        RoutineAnalysis.RoutineKind.FUNCTION,
        checks,
        Optional.of(
            new RoutineAnalysis.ReturnCheck(
                func.returnType().typename().sqlType(),
                returnedTypeName.isEmpty() ? "(unknown)" : returnedTypeName,
                returnMatch || returnedTypeName.isEmpty())),
        true);
  }

  private RoutineAnalysis analyzeProcedureParams(
      String name, List<dev.typr.foundations.ParamDef> params) {
    var metaColumns = fetchProcedureColumns(name);
    if (metaColumns.isEmpty()) {
      return new RoutineAnalysis(
          name, RoutineAnalysis.RoutineKind.PROCEDURE, List.of(), Optional.empty(), false);
    }

    List<RoutineAnalysis.ParamCheck> checks = new ArrayList<>();
    int count = Math.max(metaColumns.size(), params.size());
    for (int i = 0; i < count; i++) {
      if (i < params.size() && i < metaColumns.size()) {
        var p = params.get(i);
        var mc = metaColumns.get(i);
        String declaredType = p.type().typename().sqlType();
        String expectedType = QueryAnalysis.normalizeVendorTypeName(mc.typeName);
        String declaredMode = p.mode().name();
        String expectedMode = mc.mode;
        boolean typeMatch =
            p.type().vendorTypeNames().contains(expectedType) || expectedType.isEmpty();
        boolean modeMatch = declaredMode.equals(expectedMode);
        checks.add(
            new RoutineAnalysis.ParamCheck(
                i + 1,
                declaredType,
                expectedType.isEmpty() ? "(unknown)" : expectedType,
                declaredMode,
                expectedMode,
                typeMatch,
                modeMatch));
      } else if (i < params.size()) {
        var p = params.get(i);
        checks.add(
            new RoutineAnalysis.ParamCheck(
                i + 1,
                p.type().typename().sqlType(),
                "(missing)",
                p.mode().name(),
                "(missing)",
                false,
                false));
      } else {
        var mc = metaColumns.get(i);
        checks.add(
            new RoutineAnalysis.ParamCheck(
                i + 1,
                "(missing)",
                QueryAnalysis.normalizeVendorTypeName(mc.typeName),
                "(missing)",
                mc.mode,
                false,
                false));
      }
    }

    return new RoutineAnalysis(
        name, RoutineAnalysis.RoutineKind.PROCEDURE, checks, Optional.empty(), true);
  }

  private record ProcColumnInfo(String typeName, String mode) {}

  private List<ProcColumnInfo> fetchProcedureColumns(String name) {
    String sql =
        """
        SELECT unnest(p.proargmodes) AS mode, t.typname
        FROM pg_proc p, unnest(p.proargtypes) WITH ORDINALITY AS u(oid, ord)
        JOIN pg_type t ON t.oid = u.oid
        WHERE p.proname = $1
        ORDER BY u.ord\
        """;

    var codec = RowCodec.of(PgTypes.text, PgTypes.text);
    List<dev.typr.foundations.Tuple.Tuple2<String, String>> rows;
    try {
      rows = pool.execute(Fragment.of(sql).value(PgTypes.text, name).query(codec.all()));
    } catch (RuntimeException e) {
      return List.of();
    }

    List<ProcColumnInfo> result = new ArrayList<>();
    for (var row : rows) {
      String pgMode = row._1();
      String typeName = row._2();
      String mode =
          switch (pgMode) {
            case "i" -> "IN";
            case "o" -> "OUT";
            case "b" -> "INOUT";
            default -> "IN";
          };
      result.add(new ProcColumnInfo(typeName, mode));
    }
    return result;
  }

  // ========== OID map loading ==========

  private static Map<Integer, String> loadOidMap(PgPipelinePool pool) {
    var codec = RowCodec.of(PgTypes.int4, PgTypes.text);
    var rows =
        pool.execute(Fragment.of("SELECT oid::int4, typname FROM pg_type").query(codec.all()));
    Map<Integer, String> map = new HashMap<>();
    for (var row : rows) {
      map.put(row._1(), row._2());
    }
    return map;
  }
}
