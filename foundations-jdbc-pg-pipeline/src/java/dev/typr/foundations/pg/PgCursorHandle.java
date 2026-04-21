package dev.typr.foundations.pg;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Raw cursor handle over a PostgreSQL portal. Iterates over batches of raw rows fetched via the
 * wire protocol cursor mechanism (Execute with maxRows).
 *
 * <p>After the initial batch (received via the normal sender/receiver path), takes over direct
 * socket I/O from the receiver thread. Uses deep prefetching: sends {@code prefetchDepth} Execute
 * messages ahead, so the server prepares multiple batches while we process the current one. After
 * the initial RTT, batches arrive back-to-back — turning N sequential roundtrips into 1 RTT +
 * continuous flow.
 *
 * <p>Must be used within a transaction — PostgreSQL destroys portals after Sync outside
 * transactions.
 */
final class PgCursorHandle implements Iterator<byte[][]>, AutoCloseable {

  private final PgPipelinedConnection conn;
  private final String portalName;
  private final int fetchSize;
  private final Runnable onClose;

  private List<byte[][]> currentBatch;
  private int posInBatch;
  private boolean exhausted;
  private boolean closed;
  private boolean receiverYielded;
  private int pendingSends;

  PgCursorHandle(
      PgPipelinedConnection conn,
      String portalName,
      int fetchSize,
      int prefetchDepth,
      PgPipelinedConnection.QueryResult firstBatch,
      Runnable onClose) {
    this.conn = conn;
    this.portalName = portalName;
    this.fetchSize = fetchSize;
    this.onClose = onClose;
    this.currentBatch = firstBatch.rawRows;
    this.posInBatch = 0;
    this.exhausted = !firstBatch.portalSuspended;
    this.pendingSends = 0;
    if (!exhausted) {
      conn.awaitReceiverYield();
      receiverYielded = true;
      int depth = Math.max(1, prefetchDepth);
      for (int i = 0; i < depth; i++) {
        conn.directCursorSend(portalName, fetchSize);
        pendingSends++;
      }
    }
  }

  @Override
  public boolean hasNext() {
    if (closed) return false;
    if (posInBatch < currentBatch.size()) return true;
    if (exhausted) {
      close();
      return false;
    }
    fetchNextBatch();
    return posInBatch < currentBatch.size();
  }

  @Override
  public byte[][] next() {
    if (!hasNext()) throw new NoSuchElementException();
    return currentBatch.get(posInBatch++);
  }

  private void fetchNextBatch() {
    try {
      PgPipelinedConnection.QueryResult result = conn.directCursorReceive(fetchSize);
      pendingSends--;
      currentBatch = result.rawRows;
      posInBatch = 0;

      if (!result.portalSuspended) {
        exhausted = true;
        drainPending();
      } else {
        conn.directCursorSend(portalName, fetchSize);
        pendingSends++;
      }
    } catch (PgPipelineException e) {
      exhausted = true;
      drainPending();
      throw e;
    }
  }

  private void drainPending() {
    while (pendingSends > 0) {
      try {
        conn.directCursorReceive(fetchSize);
      } catch (Exception ignored) {
      }
      pendingSends--;
    }
  }

  @Override
  public void close() {
    if (closed) return;
    closed = true;
    try {
      if (!exhausted) {
        drainPending();
        try {
          conn.directCursorClose(portalName);
        } catch (Exception ignored) {
        }
      }
    } finally {
      if (receiverYielded) {
        conn.resumeReceiver();
        receiverYielded = false;
      }
      onClose.run();
    }
  }
}
