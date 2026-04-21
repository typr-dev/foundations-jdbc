package dev.typr.foundations;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Shared operation tree walker. Handles all structural nodes (Mapped, Combine, Then, IfEmpty,
 * Configured) uniformly, delegating leaf execution to an {@link OperationExecutor} and fan-out
 * strategy to a {@link CombineStrategy}.
 *
 * <p>This is the single place where the operation tree is interpreted. Neither Operation records
 * nor backend implementations need tree-walking code.
 */
public final class OperationRunner {

  private final OperationExecutor executor;
  private final CombineStrategy combineStrategy;
  private final InstrumentationContext baseContext;

  public OperationRunner(OperationExecutor executor, CombineStrategy combineStrategy) {
    this(executor, combineStrategy, InstrumentationContext.EMPTY);
  }

  public OperationRunner(
      OperationExecutor executor,
      CombineStrategy combineStrategy,
      InstrumentationContext baseContext) {
    this.executor = executor;
    this.combineStrategy = combineStrategy;
    this.baseContext = baseContext;
  }

  public <T> T run(Operation<T> op) {
    return run(op, baseContext);
  }

  @SuppressWarnings("unchecked")
  private <T> T run(Operation<T> op, InstrumentationContext ctx) {
    return (T)
        switch (op) {

          // === Read leaves ===
          case OperationRead.Query<?> q ->
              instrumented(ctx, q.query(), () -> executor.executeQuery(q, ctx));
          case OperationRead.Pure<?> p -> p.value();
          case OperationRead.Streaming<?> s ->
              instrumented(ctx, s.query(), () -> executor.executeStreaming(s, ctx));

          // === Write leaves ===
          case Operation.Update u ->
              instrumented(ctx, u.query(), () -> executor.executeUpdate(u, ctx));
          case Operation.Execute e ->
              instrumented(ctx, e.query(), () -> executor.executeStatement(e, ctx));
          case Operation.UpdateReturning<?> ur ->
              instrumented(ctx, ur.query(), () -> executor.executeUpdateReturning(ur, ctx));
          case Operation.UpdateReturningGeneratedKeys<?> urgk ->
              instrumented(
                  ctx, urgk.query(), () -> executor.executeUpdateReturningGeneratedKeys(urgk, ctx));
          case Operation.UpdateMany<?> um ->
              instrumented(ctx, um.query(), () -> executor.executeUpdateMany(um, ctx));
          case Operation.UpdateManyReturning<?> umr ->
              instrumented(ctx, umr.query(), () -> executor.executeUpdateManyReturning(umr, ctx));
          case Operation.UpdateReturningEach<?> ure ->
              instrumented(ctx, ure.query(), () -> executor.executeUpdateReturningEach(ure, ctx));
          case Operation.UpdateManyTemplate<?> umt ->
              instrumented(ctx, umt.fragment(), () -> executor.executeUpdateManyTemplate(umt, ctx));
          case Operation.StreamingCopy<?> sc ->
              instrumented(
                  ctx, Fragment.of(sc.copyCommand()), () -> executor.executeStreamingCopy(sc, ctx));

          // === Procedure leaves ===
          case Procedure.ProcedureCall<?> pc -> executor.executeProcedureCall(pc, ctx);
          case Procedure.FunctionCall<?> fc -> executor.executeFunctionCall(fc, ctx);

          // === Structural nodes: OperationRead variants ===
          case OperationRead.Mapped<?, ?> m -> applyMapped(m, ctx);
          case OperationRead.Combine<?, ?> c ->
              combineStrategy.combine(() -> run(c.first(), ctx), () -> run(c.second(), ctx));
          case OperationRead.Then<?, ?> t -> runThen(t, ctx);
          case OperationRead.IfEmpty<?> ie -> runIfEmpty(ie, ctx);
          case OperationRead.Configured<?> c ->
              run(c.inner(), applyConfigured(ctx, c.name(), c.timeout(), c.listener()));

          // === Structural nodes: Operation (base) variants ===
          case Operation.Mapped<?, ?> m -> applyMapped(m, ctx);
          case Operation.Combine<?, ?> c ->
              combineStrategy.combine(() -> run(c.first(), ctx), () -> run(c.second(), ctx));
          case Operation.Then<?, ?> t -> runThen(t, ctx);
          case Operation.IfEmpty<?> ie -> runIfEmpty(ie, ctx);
          case Operation.Configured<?> c ->
              run(c.inner(), applyConfigured(ctx, c.name(), c.timeout(), c.listener()));
        };
  }

  private <A, B> B applyMapped(OperationRead.Mapped<A, B> m, InstrumentationContext ctx) {
    A source = run(m.source(), ctx);
    try {
      return m.f().apply(source);
    } catch (SQLException e) {
      throw new DatabaseException.Jdbc(e);
    }
  }

  private <A, B> B applyMapped(Operation.Mapped<A, B> m, InstrumentationContext ctx) {
    A source = run(m.source(), ctx);
    try {
      return m.f().apply(source);
    } catch (SQLException e) {
      throw new DatabaseException.Jdbc(e);
    }
  }

  private <A, B> B runThen(OperationRead.Then<A, B> t, InstrumentationContext ctx) {
    A a = run(t.source(), ctx);
    return run(t.continuation().on(a), ctx);
  }

  private <A, B> B runThen(Operation.Then<A, B> t, InstrumentationContext ctx) {
    A a = run(t.source(), ctx);
    return run(t.continuation().on(a), ctx);
  }

  @SuppressWarnings("unchecked")
  private <T> T runIfEmpty(OperationRead.IfEmpty<T> ie, InstrumentationContext ctx) {
    Optional<T> result = run(ie.check(), ctx);
    return result.isPresent() ? result.get() : run(ie.fallback(), ctx);
  }

  @SuppressWarnings("unchecked")
  private <T> T runIfEmpty(Operation.IfEmpty<T> ie, InstrumentationContext ctx) {
    Optional<T> result = run(ie.check(), ctx);
    return result.isPresent() ? result.get() : run(ie.fallback(), ctx);
  }

  private static InstrumentationContext applyConfigured(
      InstrumentationContext ctx,
      Optional<String> name,
      Optional<Duration> timeout,
      Optional<QueryListener> listener) {
    InstrumentationContext result = ctx;
    if (name.isPresent()) result = result.withName(name.get());
    if (timeout.isPresent()) result = result.withTimeout(timeout.get());
    if (listener.isPresent()) result = result.withListener(listener.get());
    return result;
  }

  private <T> T instrumented(InstrumentationContext ctx, Fragment fragment, Supplier<T> action) {
    QueryListener listener = ctx.listener();
    if (listener == QueryListener.NOOP && ctx.name().isEmpty()) {
      return action.get();
    }

    String sql = fragment.render();
    Optional<String> name = ctx.name();
    listener.beforeQuery(sql, name);
    long start = System.nanoTime();
    try {
      T result = action.get();
      listener.afterQuery(
          new QueryEvent(
              name, sql, fragment, Duration.ofNanos(System.nanoTime() - start), Optional.empty()));
      return result;
    } catch (RuntimeException e) {
      listener.failedQuery(
          new QueryEvent(
              name, sql, fragment, Duration.ofNanos(System.nanoTime() - start), Optional.of(e)));
      throw e;
    }
  }
}
