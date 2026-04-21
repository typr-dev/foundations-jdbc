package dev.typr.foundations.pg;

import dev.typr.foundations.Cursor;
import dev.typr.foundations.DbType;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.InstrumentationContext;
import dev.typr.foundations.Operation;
import dev.typr.foundations.OperationExecutor;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.ParamDef;
import dev.typr.foundations.PgText;
import dev.typr.foundations.PgType;
import dev.typr.foundations.Procedure;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RowCodec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * {@link OperationExecutor} backed by PgPipe's wire-protocol pipelining. Delegates leaf operations
 * to {@link PgPipelinedConnection} via the pool.
 *
 * <p>Created per-execution scope: pooled (connection per leaf) or transactional (fixed connection).
 */
final class PgPipeOperationExecutor implements OperationExecutor {

  private static final byte[][] NO_PARAMS = new byte[0][];

  private final PgPipelinePool pool;
  private final Supplier<PgPipelinedConnection> connectionSupplier;
  private final boolean inTransaction;
  private final ConcurrentHashMap<String, String> sqlCache;
  private final PgPipelineConfig config;

  PgPipeOperationExecutor(
      PgPipelinePool pool,
      Supplier<PgPipelinedConnection> connectionSupplier,
      boolean inTransaction,
      ConcurrentHashMap<String, String> sqlCache,
      PgPipelineConfig config) {
    this.pool = pool;
    this.connectionSupplier = connectionSupplier;
    this.inTransaction = inTransaction;
    this.sqlCache = sqlCache;
    this.config = config;
  }

  // === Read leaves ===

  @Override
  public <Out> Out executeQuery(OperationRead.Query<Out> query, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    short[] resultFormats = computeResultFormats(query.parser());
    PgPipelinedConnection.QueryResult result = executeRaw(query.query(), conn, resultFormats);
    return applyParser(result.rawRows, query.parser());
  }

  @Override
  public <Row> Cursor<Row> executeStreaming(
      OperationRead.Streaming<Row> streaming, InstrumentationContext ctx) {
    Fragment fragment = streaming.query();
    RowCodec<Row> codec = streaming.codec();
    int fetchSize = streaming.fetchSize() > 0 ? streaming.fetchSize() : config.defaultFetchSize();

    String rawSql = fragment.render();
    String pgSql = sqlCache.computeIfAbsent(rawSql, PgProtocol::convertPlaceholders);
    byte[][] paramValues = collectParams(fragment);

    boolean ownTransaction = !inTransaction;
    int connIdx;
    PgPipelinedConnection conn;

    if (ownTransaction) {
      connIdx = pool.reserveConnectionIdx();
      conn = pool.getConnection(connIdx);
      try {
        conn.submit("BEGIN", NO_PARAMS, null).join();
      } catch (CompletionException e) {
        pool.unreserveConnection(connIdx);
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException re) throw re;
        throw new PgPipelineException(cause);
      }
    } else {
      connIdx = -1;
      conn = connectionSupplier.get();
    }

    PgPipelinedConnection.QueryResult firstBatch;
    try {
      firstBatch =
          conn.submitCursor(pgSql, paramValues, fetchSize, computeResultFormats(codec)).join();
    } catch (CompletionException e) {
      if (ownTransaction) {
        try {
          conn.submit("ROLLBACK", NO_PARAMS, null).join();
        } catch (Exception ignored) {
        }
        pool.unreserveConnection(connIdx);
      }
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }

    String portalName = conn.lastPortalName();

    Runnable onClose;
    if (ownTransaction) {
      final int idx = connIdx;
      onClose =
          () -> {
            try {
              conn.submit("COMMIT", NO_PARAMS, null).join();
            } catch (Exception ignored) {
            }
            pool.unreserveConnection(idx);
          };
    } else {
      onClose = () -> {};
    }

    PgCursorHandle rawHandle =
        new PgCursorHandle(
            conn, portalName, fetchSize, config.cursorPrefetchDepth(), firstBatch, onClose);

    Iterator<Row> decodedIterator =
        new Iterator<>() {
          @Override
          public boolean hasNext() {
            return rawHandle.hasNext();
          }

          @Override
          public Row next() {
            if (!hasNext()) throw new NoSuchElementException();
            byte[][] rawRow = rawHandle.next();
            return decodeRow(rawRow, codec);
          }
        };

    return Cursor.fromIterator(decodedIterator, rawHandle::close);
  }

  // === Write leaves ===

  @Override
  public int executeUpdate(Operation.Update update, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    PgPipelinedConnection.QueryResult result = executeRaw(update.query(), conn, null);
    return result.affectedRows;
  }

  @Override
  public Void executeStatement(Operation.Execute execute, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    executeRaw(execute.query(), conn, null);
    return null;
  }

  @Override
  public <Out> Out executeUpdateReturning(
      Operation.UpdateReturning<Out> op, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    short[] resultFormats = computeResultFormats(op.parser());
    PgPipelinedConnection.QueryResult result = executeRaw(op.query(), conn, resultFormats);
    return applyParser(result.rawRows, op.parser());
  }

  @Override
  public <Out> Out executeUpdateReturningGeneratedKeys(
      Operation.UpdateReturningGeneratedKeys<Out> op, InstrumentationContext ctx) {
    throw new UnsupportedOperationException(
        "PgPipe does not support UpdateReturningGeneratedKeys — use UpdateReturning with RETURNING"
            + " clause instead");
  }

  @Override
  public <Row> Optional<int[]> executeUpdateMany(
      Operation.UpdateMany<Row> op, InstrumentationContext ctx) {
    throw new UnsupportedOperationException(
        "PgPipe does not support UpdateMany — use UpdateManyTemplate instead");
  }

  @Override
  public <Row> List<Row> executeUpdateManyReturning(
      Operation.UpdateManyReturning<Row> op, InstrumentationContext ctx) {
    throw new UnsupportedOperationException("PgPipe does not support UpdateManyReturning");
  }

  @Override
  public <Row> List<Row> executeUpdateReturningEach(
      Operation.UpdateReturningEach<Row> op, InstrumentationContext ctx) {
    throw new UnsupportedOperationException("PgPipe does not support UpdateReturningEach");
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Row> Optional<int[]> executeUpdateManyTemplate(
      Operation.UpdateManyTemplate<Row> op, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    boolean ownTransaction = !inTransaction;

    Fragment fragment = op.fragment();
    String rawSql = fragment.render();
    String pgSql = sqlCache.computeIfAbsent(rawSql, PgProtocol::convertPlaceholders);

    var codec = op.codec();
    int[] includedIndices = op.includedIndices();
    Iterator<Row> rows = op.rows();

    List<DbType<?>> allColumns = codec.columns();
    PgText<Object>[] encoders = new PgText[includedIndices.length];
    for (int i = 0; i < includedIndices.length; i++) {
      encoders[i] = ((PgType<Object>) allColumns.get(includedIndices[i])).pgText();
    }

    StringBuilder sb = new StringBuilder();
    List<byte[][]> allParamValues = new ArrayList<>();
    while (rows.hasNext()) {
      Row row = rows.next();
      Object[] encoded = codec.encode().apply(row);
      byte[][] paramValues = new byte[includedIndices.length][];
      for (int i = 0; i < includedIndices.length; i++) {
        Object value = encoded[includedIndices[i]];
        if (value == null || (value instanceof Optional<?> opt && opt.isEmpty())) {
          paramValues[i] = null;
        } else {
          sb.setLength(0);
          encoders[i].wireEncode(value, sb);
          paramValues[i] = sb.toString().getBytes(StandardCharsets.UTF_8);
        }
      }
      allParamValues.add(paramValues);
    }

    if (allParamValues.isEmpty()) return Optional.of(new int[0]);

    var beginFuture = ownTransaction ? conn.submit("BEGIN", NO_PARAMS, null) : null;

    try {
      PgPipelinedConnection.QueryResult result =
          conn.submitBatch(pgSql, allParamValues, null).join();
      if (beginFuture != null) beginFuture.join();
      if (ownTransaction) conn.submit("COMMIT", NO_PARAMS, null).join();
      return Optional.of(result.batchResults);
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (ownTransaction) {
        try {
          conn.submit("ROLLBACK", NO_PARAMS, null).join();
        } catch (Exception ignored) {
        }
      }
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Row> long executeStreamingCopy(
      Operation.StreamingCopy<Row> op, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    PgText<Row> text = (PgText<Row>) op.text();
    Iterator<Row> rows = op.rows();
    int batchSize = op.batchSize();

    Iterator<byte[]> rowBytes =
        new Iterator<>() {
          @Override
          public boolean hasNext() {
            return rows.hasNext();
          }

          @Override
          public byte[] next() {
            var sb = new StringBuilder();
            for (int i = 0; i < batchSize && rows.hasNext(); i++) {
              text.unsafeEncode(rows.next(), sb);
              sb.append('\n');
            }
            return sb.toString().getBytes(StandardCharsets.UTF_8);
          }
        };

    try {
      PgPipelinedConnection.QueryResult result = conn.submitCopy(op.copyCommand(), rowBytes).join();
      return result.affectedRows;
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  // === Procedure leaves ===

  @Override
  @SuppressWarnings("unchecked")
  public <R> R executeFunctionCall(Procedure.FunctionCall<R> fc, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    StringBuilder sb = new StringBuilder("SELECT ");
    sb.append(fc.name());
    sb.append('(');
    for (int i = 0; i < fc.inParams().size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append('?');
    }
    sb.append(')');

    String rawSql = sb.toString();
    String pgSql = sqlCache.computeIfAbsent(rawSql, PgProtocol::convertPlaceholders);
    byte[][] paramValues = encodeParamValues(fc.inParams(), fc.inValues());

    try {
      PgPipelinedConnection.QueryResult result = conn.submit(pgSql, paramValues, null).join();
      if (result.rawRows.isEmpty()) {
        throw new PgPipelineException("Function " + fc.name() + " returned no rows");
      }
      byte[][] row = result.rawRows.getFirst();
      if (row[0] == null) {
        return null;
      }
      PgType<R> pgType = (PgType<R>) fc.returnType();
      String text = new String(row[0], StandardCharsets.UTF_8);
      return pgType.pgText().wireDecode(text);
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Out> Out executeProcedureCall(
      Procedure.ProcedureCall<Out> pc, InstrumentationContext ctx) {
    PgPipelinedConnection conn = connectionSupplier.get();
    StringBuilder sb = new StringBuilder("CALL ");
    sb.append(pc.name());
    sb.append('(');
    for (int i = 0; i < pc.params().size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append('?');
    }
    sb.append(')');

    String rawSql = sb.toString();
    String pgSql = sqlCache.computeIfAbsent(rawSql, PgProtocol::convertPlaceholders);

    List<ParamDef> params = pc.params();
    Object[] inValues = pc.inValues();
    byte[][] paramValues = new byte[params.size()][];
    int valueIndex = 0;
    for (int i = 0; i < params.size(); i++) {
      ParamDef p = params.get(i);
      if (p.isInput()) {
        Object value = inValues[valueIndex++];
        if (value == null) {
          paramValues[i] = null;
        } else {
          PgType<Object> pgType = (PgType<Object>) p.type();
          StringBuilder enc = new StringBuilder();
          pgType.pgText().wireEncode(value, enc);
          paramValues[i] = enc.toString().getBytes(StandardCharsets.UTF_8);
        }
      } else {
        paramValues[i] = null;
      }
    }

    try {
      PgPipelinedConnection.QueryResult result = conn.submit(pgSql, paramValues, null).join();

      if (pc.assembler() != null && !result.rawRows.isEmpty()) {
        byte[][] row = result.rawRows.getFirst();
        int outCount = 0;
        for (ParamDef p : params) {
          if (p.isOutput()) outCount++;
        }
        Object[] values = new Object[outCount];
        int outIdx = 0;
        int colIdx = 0;
        for (ParamDef p : params) {
          if (p.isOutput()) {
            byte[] raw = row[colIdx++];
            if (raw == null) {
              values[outIdx++] = null;
            } else {
              PgType<Object> pgType = (PgType<Object>) p.type();
              String text = new String(raw, StandardCharsets.UTF_8);
              values[outIdx++] = pgType.pgText().wireDecode(text);
            }
          }
        }
        return pc.assembler().apply(values);
      }

      return null;
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  // ========== Internal helpers ==========

  private PgPipelinedConnection.QueryResult executeRaw(
      Fragment fragment, PgPipelinedConnection conn, short[] resultFormats) {
    String rawSql = fragment.render();
    String pgSql = sqlCache.computeIfAbsent(rawSql, PgProtocol::convertPlaceholders);
    byte[][] paramValues = collectParams(fragment);

    try {
      return conn.submit(pgSql, paramValues, resultFormats).join();
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof java.util.concurrent.TimeoutException) {
        throw new PgPipelineException(
            "Query timed out after " + config.queryTimeout().toMillis() + "ms");
      }
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T> T applyParser(List<byte[][]> rows, ResultSetParser<T> parser) {
    return (T)
        switch (parser) {
          case ResultSetParser.All<?> all -> {
            var codec = all.rowCodec();
            var list = new ArrayList<>(rows.size());
            for (byte[][] row : rows) list.add(decodeRow(row, codec));
            yield list;
          }
          case ResultSetParser.First<?> first -> {
            if (rows.isEmpty()) yield Optional.empty();
            yield Optional.of(decodeRow(rows.getFirst(), first.rowCodec()));
          }
          case ResultSetParser.MaxOne<?> maxOne -> {
            if (rows.isEmpty()) yield Optional.empty();
            if (rows.size() > 1)
              throw new PgPipelineException("Expected single row, but found " + rows.size());
            yield Optional.of(decodeRow(rows.getFirst(), maxOne.rowCodec()));
          }
          case ResultSetParser.ExactlyOne<?> exactlyOne -> {
            if (rows.isEmpty())
              throw new PgPipelineException("No rows when expecting a single one");
            if (rows.size() > 1)
              throw new PgPipelineException("Expected single row, but found " + rows.size());
            yield decodeRow(rows.getFirst(), exactlyOne.rowCodec());
          }
          case ResultSetParser.Foreach<?> foreach -> {
            var codec = foreach.rowCodec();
            var consumer = (java.util.function.Consumer) foreach.consumer();
            for (byte[][] row : rows) consumer.accept(decodeRow(row, codec));
            yield null;
          }
          case ResultSetParser.Mapped<?, ?> mapped -> {
            Object inner = applyParser(rows, mapped.inner());
            yield ((java.util.function.Function) mapped.f()).apply(inner);
          }
        };
  }

  @SuppressWarnings("unchecked")
  static <T> T decodeRow(byte[][] rawColumns, RowCodec<T> codec) {
    List<DbType<?>> columns = codec.columns();
    Object[] values = new Object[columns.size()];
    for (int i = 0; i < values.length; i++) {
      byte[] raw = rawColumns[i];
      DbType<?> dbType = columns.get(i);
      if (raw == null) {
        if (dbType.isNullable()) {
          values[i] = Optional.empty();
        } else {
          throw new PgPipelineException("Null value in non-nullable column " + (i + 1));
        }
      } else {
        PgType<?> pgType = (PgType<?>) dbType;
        values[i] = pgType.pgBinary().decode(raw, 0, raw.length);
      }
    }
    return codec.decode().apply(values);
  }

  @SuppressWarnings("unchecked")
  static byte[][] collectParams(Fragment fragment) {
    List<byte[]> params = new ArrayList<>();
    fragment.collectParams(
        new Fragment.ParamCollector() {
          @Override
          public <A> void accept(A value, DbType<A> type) {
            if (value == null) {
              params.add(null);
              return;
            }
            if (value instanceof Optional<?> opt) {
              if (opt.isEmpty()) {
                params.add(null);
                return;
              }
            }
            if (type instanceof PgType<A> pgType) {
              PgText<A> pgText = pgType.pgText();
              StringBuilder sb = new StringBuilder();
              pgText.wireEncode(value, sb);
              params.add(sb.toString().getBytes(StandardCharsets.UTF_8));
            } else {
              params.add(value.toString().getBytes(StandardCharsets.UTF_8));
            }
          }
        });
    return params.toArray(new byte[0][]);
  }

  private static byte[][] encodeParamValues(List<ParamDef> inParams, Object[] inValues) {
    byte[][] paramValues = new byte[inParams.size()][];
    for (int i = 0; i < inParams.size(); i++) {
      Object value = inValues[i];
      if (value == null) {
        paramValues[i] = null;
      } else {
        @SuppressWarnings("unchecked")
        PgType<Object> pgType = (PgType<Object>) inParams.get(i).type();
        StringBuilder sb = new StringBuilder();
        pgType.pgText().wireEncode(value, sb);
        paramValues[i] = sb.toString().getBytes(StandardCharsets.UTF_8);
      }
    }
    return paramValues;
  }

  static short[] computeResultFormats(ResultSetParser<?> parser) {
    RowCodec<?> codec = extractRowCodec(parser);
    if (codec == null) return null;
    return computeResultFormats(codec);
  }

  private static RowCodec<?> extractRowCodec(ResultSetParser<?> parser) {
    return switch (parser) {
      case ResultSetParser.All<?> a -> a.rowCodec();
      case ResultSetParser.First<?> f -> f.rowCodec();
      case ResultSetParser.MaxOne<?> m -> m.rowCodec();
      case ResultSetParser.ExactlyOne<?> e -> e.rowCodec();
      case ResultSetParser.Foreach<?> f -> f.rowCodec();
      case ResultSetParser.Mapped<?, ?> m -> extractRowCodec(m.inner());
    };
  }

  static short[] computeResultFormats(RowCodec<?> codec) {
    List<DbType<?>> columns = codec.columns();
    short[] formats = new short[columns.size()];
    for (int i = 0; i < columns.size(); i++) {
      if (columns.get(i) instanceof PgType<?> pgType && pgType.pgBinary().prefersBinaryFormat()) {
        formats[i] = 1;
      }
    }
    return formats;
  }
}
