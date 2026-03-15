package dev.typr.foundations;
import dev.typr.foundations.connect.DatabaseKind;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RoutineAnalyzer {

  private RoutineAnalyzer() {}

  public static RoutineAnalysis analyzeFunction(Procedure.FunctionProcedure<?> func, Connection conn)
      throws SQLException {
    String name = func.name();
    List<ParamDef> inParams = func.inParams();
    DbType<?> returnType = func.returnType();

    DatabaseKind dbKind = DatabaseKind.detect(conn);
    boolean useDoubleColon = dbKind == DatabaseKind.POSTGRESQL || dbKind == DatabaseKind.DUCKDB;

    // Build SELECT func_name(null::type1, ...) or SELECT func_name(CAST(NULL AS type1), ...)
    StringBuilder sb = new StringBuilder("SELECT ");
    sb.append(name).append('(');
    for (int i = 0; i < inParams.size(); i++) {
      if (i > 0) sb.append(", ");
      String sqlType = inParams.get(i).type().typename().sqlType();
      if (useDoubleColon) {
        sb.append("null::").append(sqlType);
      } else {
        sb.append("CAST(NULL AS ").append(sqlType).append(')');
      }
    }
    sb.append(')');

    // Oracle and DB2 require FROM DUAL for SELECT without a table
    if (dbKind == DatabaseKind.ORACLE || dbKind == DatabaseKind.DB2) {
      sb.append(" FROM DUAL");
    }

    try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
      var rsmd = ps.getMetaData();
      String returnedTypeName = rsmd != null && rsmd.getColumnCount() > 0
          ? QueryAnalysis.normalizeVendorTypeName(rsmd.getColumnTypeName(1))
          : "";

      List<RoutineAnalysis.ParamCheck> checks = new ArrayList<>();
      for (int i = 0; i < inParams.size(); i++) {
        ParamDef p = inParams.get(i);
        checks.add(new RoutineAnalysis.ParamCheck(
            i + 1,
            p.type().typename().sqlType(),
            p.type().typename().sqlType(),
            "IN",
            "IN",
            true,
            true
        ));
      }

      boolean returnMatch = !returnedTypeName.isEmpty()
          && returnType.vendorTypeNames().contains(returnedTypeName);

      return new RoutineAnalysis(
          name,
          RoutineAnalysis.RoutineKind.FUNCTION,
          checks,
          Optional.of(new RoutineAnalysis.ReturnCheck(
              returnType.typename().sqlType(),
              returnedTypeName.isEmpty() ? "(unknown)" : returnedTypeName,
              returnMatch || returnedTypeName.isEmpty()
          )),
          true
      );
    } catch (SQLException e) {
      if (e.getMessage() != null && (
          e.getMessage().contains("does not exist") ||
          e.getMessage().contains("not found") ||
          e.getMessage().contains("Unknown function"))) {
        return new RoutineAnalysis(
            name,
            RoutineAnalysis.RoutineKind.FUNCTION,
            List.of(),
            Optional.empty(),
            false
        );
      }
      throw e;
    }
  }

  public static RoutineAnalysis analyzeProcedure(Procedure<?> proc, Connection conn)
      throws SQLException {
    String name;
    List<ParamDef> params;

    if (proc instanceof Procedure.VoidProcedure vp) {
      name = vp.name();
      params = vp.params();
    } else if (proc instanceof Procedure.OutProcedure<?> op) {
      name = op.name();
      params = op.params();
    } else if (proc instanceof Procedure.FunctionProcedure<?> fp) {
      return analyzeFunction(fp, conn);
    } else {
      throw new IllegalArgumentException("Unknown procedure type: " + proc.getClass());
    }

    DatabaseMetaData dbmd = conn.getMetaData();
    DatabaseKind dbKind = DatabaseKind.detect(conn);
    List<ProcedureColumnInfo> metaColumns = fetchProcedureColumns(dbmd, name);

    // Oracle stores names uppercase — retry with uppercase if no results found
    if (metaColumns.isEmpty() && dbKind == DatabaseKind.ORACLE && !name.equals(name.toUpperCase())) {
      metaColumns = fetchProcedureColumns(dbmd, name.toUpperCase());
    }

    if (metaColumns.isEmpty()) {
      return new RoutineAnalysis(
          name,
          RoutineAnalysis.RoutineKind.PROCEDURE,
          List.of(),
          Optional.empty(),
          false
      );
    }

    List<RoutineAnalysis.ParamCheck> checks = new ArrayList<>();
    int metaSize = metaColumns.size();
    int paramSize = params.size();
    int count = Math.max(metaSize, paramSize);

    for (int i = 0; i < count; i++) {
      if (i < paramSize && i < metaSize) {
        ParamDef p = params.get(i);
        ProcedureColumnInfo mc = metaColumns.get(i);

        String declaredType = p.type().typename().sqlType();
        String expectedType = QueryAnalysis.normalizeVendorTypeName(mc.typeName());
        String declaredMode = p.mode().name();
        String expectedMode = columnTypeToMode(mc.columnType());

        boolean typeMatch = p.type().vendorTypeNames().contains(expectedType) || expectedType.isEmpty();
        boolean modeMatch = declaredMode.equals(expectedMode);

        checks.add(new RoutineAnalysis.ParamCheck(
            i + 1, declaredType, expectedType.isEmpty() ? "(unknown)" : expectedType,
            declaredMode, expectedMode, typeMatch, modeMatch
        ));
      } else if (i < paramSize) {
        ParamDef p = params.get(i);
        checks.add(new RoutineAnalysis.ParamCheck(
            i + 1, p.type().typename().sqlType(), "(missing)",
            p.mode().name(), "(missing)", false, false
        ));
      } else {
        ProcedureColumnInfo mc = metaColumns.get(i);
        checks.add(new RoutineAnalysis.ParamCheck(
            i + 1, "(missing)", QueryAnalysis.normalizeVendorTypeName(mc.typeName()),
            "(missing)", columnTypeToMode(mc.columnType()), false, false
        ));
      }
    }

    return new RoutineAnalysis(
        name,
        RoutineAnalysis.RoutineKind.PROCEDURE,
        checks,
        Optional.empty(),
        true
    );
  }

  private static List<ProcedureColumnInfo> fetchProcedureColumns(DatabaseMetaData dbmd, String name) throws SQLException {
    List<ProcedureColumnInfo> metaColumns = new ArrayList<>();
    try (ResultSet rs = dbmd.getProcedureColumns(null, null, name, null)) {
      while (rs.next()) {
        int columnType = rs.getInt("COLUMN_TYPE");
        if (columnType == DatabaseMetaData.procedureColumnReturn
            || columnType == DatabaseMetaData.procedureColumnResult) {
          continue;
        }
        String typeName = rs.getString("TYPE_NAME");
        metaColumns.add(new ProcedureColumnInfo(
            columnType,
            typeName != null ? typeName : ""
        ));
      }
    }
    return metaColumns;
  }

  private record ProcedureColumnInfo(int columnType, String typeName) {}

  private static String columnTypeToMode(int columnType) {
    return switch (columnType) {
      case DatabaseMetaData.procedureColumnIn -> "IN";
      case DatabaseMetaData.procedureColumnOut -> "OUT";
      case DatabaseMetaData.procedureColumnInOut -> "INOUT";
      case DatabaseMetaData.procedureColumnReturn -> "RETURN";
      default -> "UNKNOWN";
    };
  }
}
