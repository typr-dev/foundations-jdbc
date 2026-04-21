package dev.typr.foundations.internal;

import dev.typr.foundations.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of {@link OperationExecutor}. Executes leaf operations via
 * PreparedStatement/CallableStatement on a raw JDBC connection.
 */
public final class JdbcOperationExecutor implements OperationExecutor {

  private final java.sql.Connection conn;
  private final java.util.function.Function<SQLException, DatabaseException> exceptionMapper;

  public JdbcOperationExecutor(
      java.sql.Connection conn,
      java.util.function.Function<SQLException, DatabaseException> exceptionMapper) {
    this.conn = conn;
    this.exceptionMapper = exceptionMapper;
  }

  private String applySql(String rawSql, InstrumentationContext ctx) {
    return ctx.name().map(n -> "/* " + n + " */ " + rawSql).orElse(rawSql);
  }

  private void applyTimeout(PreparedStatement stmt, InstrumentationContext ctx)
      throws SQLException {
    if (ctx.timeout().isPresent()) {
      stmt.setQueryTimeout(Math.max(1, (int) ctx.timeout().get().toSeconds()));
    }
  }

  // === Read leaves ===

  @Override
  public <Out> Out executeQuery(OperationRead.Query<Out> query, InstrumentationContext ctx) {
    try {
      String sql = applySql(query.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        query.query().set(stmt);
        try (ResultSet rs = stmt.executeQuery()) {
          return query.parser().apply(rs);
        }
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Row> Cursor<Row> executeStreaming(
      OperationRead.Streaming<Row> streaming, InstrumentationContext ctx) {
    try {
      String sql = applySql(streaming.query().render(), ctx);
      PreparedStatement stmt =
          conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      stmt.setFetchSize(streaming.fetchSize());
      applyTimeout(stmt, ctx);
      streaming.query().set(stmt);
      ResultSet rs = stmt.executeQuery();
      return new Cursor<Row>(stmt, rs, streaming.codec());
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  // === Write leaves ===

  @Override
  public int executeUpdate(Operation.Update update, InstrumentationContext ctx) {
    try {
      String sql = applySql(update.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        update.query().set(stmt);
        return stmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public Void executeStatement(Operation.Execute execute, InstrumentationContext ctx) {
    try {
      String sql = applySql(execute.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        execute.query().set(stmt);
        stmt.execute();
        return null;
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Out> Out executeUpdateReturning(
      Operation.UpdateReturning<Out> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        op.query().set(stmt);
        try (ResultSet rs = stmt.executeQuery()) {
          return op.parser().apply(rs);
        }
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Out> Out executeUpdateReturningGeneratedKeys(
      Operation.UpdateReturningGeneratedKeys<Out> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql, op.columnNames())) {
        applyTimeout(stmt, ctx);
        op.query().set(stmt);
        stmt.executeUpdate();
        try (ResultSet rs = stmt.getGeneratedKeys()) {
          return op.parser().apply(rs);
        }
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Row> Optional<int[]> executeUpdateMany(
      Operation.UpdateMany<Row> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.query().render(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        op.query().set(stmt);
        while (op.rows().hasNext()) {
          Row row = op.rows().next();
          op.codec().writeRow(stmt, row);
          stmt.addBatch();
        }
        return Operation.toBatchResult(stmt.executeBatch());
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Row> List<Row> executeUpdateManyReturning(
      Operation.UpdateManyReturning<Row> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.query().render(), ctx);
      try (PreparedStatement stmt =
          conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
        applyTimeout(stmt, ctx);
        op.query().set(stmt);
        while (op.rows().hasNext()) {
          Row row = op.rows().next();
          op.codec().writeRow(stmt, row);
          stmt.addBatch();
        }
        stmt.executeBatch();
        try (ResultSet rs = stmt.getGeneratedKeys()) {
          return op.codec().all().apply(rs);
        }
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Row> List<Row> executeUpdateReturningEach(
      Operation.UpdateReturningEach<Row> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.query().render(), ctx);
      ArrayList<Row> results = new ArrayList<>();
      while (op.rows().hasNext()) {
        Row row = op.rows().next();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          applyTimeout(stmt, ctx);
          op.query().set(stmt);
          op.codec().writeRow(stmt, row);
          try (ResultSet rs = stmt.executeQuery()) {
            results.addAll(op.codec().all().apply(rs));
          }
        }
      }
      return results;
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Row> Optional<int[]> executeUpdateManyTemplate(
      Operation.UpdateManyTemplate<Row> op, InstrumentationContext ctx) {
    try {
      String sql = applySql(op.fragment().render(), ctx);
      int[] paramPositions = op.fragment().paramPositions();
      List<DbType<?>> allCols = op.codec().columns();
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        while (op.rows().hasNext()) {
          Row row = op.rows().next();
          op.fragment().set(stmt);
          Object[] encoded = op.codec().encode().apply(row);
          for (int i = 0; i < op.includedIndices().length; i++) {
            DbType<Object> type = (DbType<Object>) allCols.get(op.includedIndices()[i]);
            type.write().set(stmt, paramPositions[i], encoded[op.includedIndices()[i]]);
          }
          stmt.addBatch();
        }
        return Operation.toBatchResult(stmt.executeBatch());
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <Row> long executeStreamingCopy(
      Operation.StreamingCopy<Row> op, InstrumentationContext ctx) {
    try {
      return StreamingInsert.insert(op.copyCommand(), op.batchSize(), op.rows(), conn, op.text());
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  // === Procedure leaves ===

  @Override
  @SuppressWarnings("unchecked")
  public <Out> Out executeProcedureCall(
      Procedure.ProcedureCall<Out> call, InstrumentationContext ctx) {
    try {
      boolean isPostgres = conn.getMetaData().getDatabaseProductName().startsWith("PostgreSQL");
      if (isPostgres) {
        return executeProcedurePostgres(call, ctx);
      }
      return executeProcedureCallable(call, ctx);
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @SuppressWarnings("unchecked")
  private <Out> Out executeProcedurePostgres(
      Procedure.ProcedureCall<Out> call, InstrumentationContext ctx) throws SQLException {
    StringBuilder sb = new StringBuilder("CALL ");
    sb.append(call.name());
    sb.append('(');
    for (int i = 0; i < call.params().size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append('?');
    }
    sb.append(')');

    String sql = applySql(sb.toString(), ctx);
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      applyTimeout(stmt, ctx);
      int valueIndex = 0;
      for (int i = 0; i < call.params().size(); i++) {
        ParamDef p = call.params().get(i);
        int pos = i + 1;
        if (p.isInput()) {
          DbType<Object> type = (DbType<Object>) p.type();
          type.write().set(stmt, pos, call.inValues()[valueIndex++]);
        } else if (p.isOutput()) {
          stmt.setNull(pos, java.sql.Types.NULL);
        }
      }
      boolean hasResultSet = stmt.execute();
      if (call.assembler() != null && hasResultSet) {
        try (ResultSet rs = stmt.getResultSet()) {
          if (rs.next()) {
            int outCount = 0;
            for (ParamDef p : call.params()) {
              if (p.isOutput()) outCount++;
            }
            Object[] values = new Object[outCount];
            int outIdx = 0;
            int rsCol = 1;
            for (ParamDef p : call.params()) {
              if (p.isOutput()) {
                values[outIdx++] = p.type().read().read(rs, rsCol++);
              }
            }
            return call.assembler().apply(values);
          }
        }
      }
      return (Out) (Object) null;
    }
  }

  private <Out> Out executeProcedureCallable(
      Procedure.ProcedureCall<Out> call, InstrumentationContext ctx) throws SQLException {
    StringBuilder sb = new StringBuilder("{call ");
    sb.append(call.name());
    sb.append('(');
    for (int i = 0; i < call.params().size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append('?');
    }
    sb.append(")}");

    String sql = applySql(sb.toString(), ctx);
    try (CallableStatement stmt = conn.prepareCall(sql)) {
      applyTimeout(stmt, ctx);
      int valueIndex = 0;
      for (int i = 0; i < call.params().size(); i++) {
        ParamDef p = call.params().get(i);
        int pos = i + 1;
        if (p.isInput()) {
          @SuppressWarnings("unchecked")
          DbType<Object> type = (DbType<Object>) p.type();
          type.write().set(stmt, pos, call.inValues()[valueIndex++]);
        }
        if (p.isOutput()) {
          p.outParam().register(stmt, pos);
        }
      }
      stmt.execute();
      return call.reader().apply(stmt);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R> R executeFunctionCall(Procedure.FunctionCall<R> call, InstrumentationContext ctx) {
    try {
      StringBuilder sb = new StringBuilder("SELECT ");
      sb.append(call.name());
      sb.append('(');
      for (int i = 0; i < call.inParams().size(); i++) {
        if (i > 0) sb.append(", ");
        sb.append('?');
      }
      sb.append(')');

      String sql = applySql(sb.toString(), ctx);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        applyTimeout(stmt, ctx);
        for (int i = 0; i < call.inParams().size(); i++) {
          DbType<Object> type = (DbType<Object>) call.inParams().get(i).type();
          type.write().set(stmt, i + 1, call.inValues()[i]);
        }
        try (ResultSet rs = stmt.executeQuery()) {
          if (!rs.next()) {
            throw new SQLException("Function " + call.name() + " returned no rows");
          }
          return call.returnType().read().read(rs, 1);
        }
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }
}
