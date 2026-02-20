package dev.typr.foundations;

import java.sql.*;
import java.util.List;
import java.util.function.Function;

/**
 * A reusable definition of a stored procedure or function.
 *
 * <p>Use {@link DbProcedure} for type-safe procedure definitions with typed inputs and outputs, or
 * {@link DbFunction} for type-safe function definitions with typed inputs.
 *
 * @param <Out> the Java type of the procedure result
 */
public sealed interface Procedure<Out>
    permits Procedure.VoidProcedure, Procedure.OutProcedure, Procedure.FunctionProcedure {

  /**
   * Create an {@link Operation} that calls this procedure with the given IN/INOUT parameter values.
   * Values must be provided in the same order as the {@code .in()} and {@code .inout()} calls
   * during definition.
   *
   * @param inValues the IN and INOUT parameter values, in declaration order
   * @return an Operation that can be run or transacted
   */
  Operation<Out> call(Object... inValues);

  /** Build a void procedure (no OUT/INOUT params). */
  static Procedure<Void> buildVoid(String name, List<ParamDef> params) {
    return new VoidProcedure(name, params);
  }

  /** Build a procedure with a single OUT/INOUT param. */
  static <O> Procedure<O> buildSingleOut(String name, List<ParamDef> params) {
    return new OutProcedure<>(name, params, values -> {
      @SuppressWarnings("unchecked")
      O result = (O) values[0];
      return result;
    });
  }

  /** Build a procedure with multiple OUT/INOUT params. */
  static <O> Procedure<O> buildMultiOut(
      String name, List<ParamDef> params, Function<Object[], O> assembler) {
    return new OutProcedure<>(name, params, assembler);
  }

  /** Build a function (uses SELECT, reads result via DbRead). */
  static <R> Procedure<R> buildFunction(
      String name, List<ParamDef> inParams, DbType<R> returnType) {
    return new FunctionProcedure<>(name, inParams, returnType);
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Implementations
  // ─────────────────────────────────────────────────────────────────────────────

  /** A void procedure call: {@code {call proc_name(?, ?, ...)}}. */
  record VoidProcedure(String name, List<ParamDef> params) implements Procedure<Void> {
    @Override
    public Operation<Void> call(Object... inValues) {
      return new ProcedureCall<>(name, params, inValues, stmt -> null);
    }
  }

  /** A procedure call with OUT/INOUT params, read via DbOutParam. */
  record OutProcedure<Out>(
      String name, List<ParamDef> params, Function<Object[], Out> assembler)
      implements Procedure<Out> {
    @Override
    public Operation<Out> call(Object... inValues) {
      return new ProcedureCall<>(name, params, inValues, stmt -> {
        int outCount = 0;
        for (ParamDef p : params) {
          if (p.isOutput()) outCount++;
        }
        Object[] values = new Object[outCount];
        int outIndex = 0;
        for (int i = 0; i < params.size(); i++) {
          ParamDef p = params.get(i);
          if (p.isOutput()) {
            values[outIndex++] = p.outParam().read(stmt, i + 1);
          }
        }
        return assembler.apply(values);
      });
    }
  }

  /** A function call via SELECT: {@code SELECT func_name(?, ?, ...)}. */
  record FunctionProcedure<R>(String name, List<ParamDef> inParams, DbType<R> returnType)
      implements Procedure<R> {
    @Override
    public Operation<R> call(Object... inValues) {
      return new FunctionCall<>(name, inParams, returnType, inValues);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Operation implementations
  // ─────────────────────────────────────────────────────────────────────────────

  /**
   * Operation for procedure calls using CallableStatement. Handles IN, OUT, and INOUT parameters.
   * Used for both void procedures and procedures with OUT/INOUT params.
   */
  record ProcedureCall<Out>(
      String name,
      List<ParamDef> params,
      Object[] inValues,
      SqlFunction<CallableStatement, Out> reader)
      implements Operation<Out> {

    @Override
    public Out run(Connection conn) {
      try {
        StringBuilder sb = new StringBuilder("{call ");
        sb.append(name);
        sb.append('(');
        for (int i = 0; i < params.size(); i++) {
          if (i > 0) sb.append(", ");
          sb.append('?');
        }
        sb.append(")}");

        String sql = Instrumentation.applyName(sb.toString(), conn);
        Fragment syntheticFragment = Fragment.of(sb.toString());
        return Instrumentation.instrumented(conn, syntheticFragment, sql, () -> {
          try (CallableStatement stmt = conn.prepareCall(sql)) {
            Instrumentation.applyTimeout(stmt, conn);
            int valueIndex = 0;
            for (int i = 0; i < params.size(); i++) {
              ParamDef p = params.get(i);
              int pos = i + 1;
              if (p.isInput()) {
                @SuppressWarnings("unchecked")
                DbType<Object> type = (DbType<Object>) p.type();
                type.write().set(stmt, pos, inValues[valueIndex++]);
              }
              if (p.isOutput()) {
                p.outParam().register(stmt, pos);
              }
            }
            stmt.execute();
            return reader.apply(stmt);
          }
        });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  /**
   * Operation for function calls using SELECT and PreparedStatement.
   * SQL: {@code SELECT func_name(?, ?, ...)}
   */
  record FunctionCall<R>(
      String name, List<ParamDef> inParams, DbType<R> returnType, Object[] inValues)
      implements Operation<R> {

    @Override
    public R run(Connection conn) {
      try {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(name);
        sb.append('(');
        for (int i = 0; i < inParams.size(); i++) {
          if (i > 0) sb.append(", ");
          sb.append('?');
        }
        sb.append(')');

        String sql = Instrumentation.applyName(sb.toString(), conn);
        Fragment syntheticFragment = Fragment.of(sb.toString());
        return Instrumentation.instrumented(conn, syntheticFragment, sql, () -> {
          try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Instrumentation.applyTimeout(stmt, conn);
            for (int i = 0; i < inParams.size(); i++) {
              @SuppressWarnings("unchecked")
              DbType<Object> type = (DbType<Object>) inParams.get(i).type();
              type.write().set(stmt, i + 1, inValues[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
              if (!rs.next()) {
                throw new SQLException("Function " + name + " returned no rows");
              }
              return returnType.read().read(rs, 1);
            }
          }
        });
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }
}
