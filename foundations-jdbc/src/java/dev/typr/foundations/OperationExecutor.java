package dev.typr.foundations;

/**
 * Backend-specific execution of leaf operations. JDBC and PgPipe each provide their own
 * implementation. Tree-walking (Mapped, Combine, Then, IfEmpty, Configured) is NOT here — it is
 * handled by the shared {@link OperationRunner}.
 *
 * <p>Each method receives the leaf operation record and an {@link InstrumentationContext} carrying
 * the current name, timeout, and listener. The executor is responsible for applying the timeout
 * (e.g. via {@code setQueryTimeout} for JDBC, or a deadline for PgPipe) and prepending the name to
 * SQL if appropriate.
 */
public interface OperationExecutor {

  // === Read leaves ===

  <Out> Out executeQuery(OperationRead.Query<Out> query, InstrumentationContext ctx);

  <Row> Cursor<Row> executeStreaming(
      OperationRead.Streaming<Row> streaming, InstrumentationContext ctx);

  // === Write leaves ===

  int executeUpdate(Operation.Update update, InstrumentationContext ctx);

  Void executeStatement(Operation.Execute execute, InstrumentationContext ctx);

  <Out> Out executeUpdateReturning(Operation.UpdateReturning<Out> op, InstrumentationContext ctx);

  <Out> Out executeUpdateReturningGeneratedKeys(
      Operation.UpdateReturningGeneratedKeys<Out> op, InstrumentationContext ctx);

  <Row> java.util.Optional<int[]> executeUpdateMany(
      Operation.UpdateMany<Row> op, InstrumentationContext ctx);

  <Row> java.util.List<Row> executeUpdateManyReturning(
      Operation.UpdateManyReturning<Row> op, InstrumentationContext ctx);

  <Row> java.util.List<Row> executeUpdateReturningEach(
      Operation.UpdateReturningEach<Row> op, InstrumentationContext ctx);

  <Row> java.util.Optional<int[]> executeUpdateManyTemplate(
      Operation.UpdateManyTemplate<Row> op, InstrumentationContext ctx);

  <Row> long executeStreamingCopy(Operation.StreamingCopy<Row> op, InstrumentationContext ctx);

  // === Procedure leaves ===

  <Out> Out executeProcedureCall(Procedure.ProcedureCall<Out> call, InstrumentationContext ctx);

  <R> R executeFunctionCall(Procedure.FunctionCall<R> call, InstrumentationContext ctx);
}
