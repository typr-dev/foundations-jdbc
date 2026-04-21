---
title: PgPipe Architecture
---

# PgPipe Architecture

This page describes how PgPipe works under the hood. You don't need to know any of this to use PgPipe — but it helps when tuning, debugging, or understanding performance characteristics.

## Wire Protocol

PgPipe implements PostgreSQL's wire protocol (v3) directly over TCP sockets. A single query goes through these protocol steps:

```
Client                                Server
  │                                     │
  │─── Parse (SQL, statement name) ────>│
  │─── Bind (params, portal name) ─────>│
  │─── Describe (portal) ─────────────>│
  │─── Execute (portal, max rows) ────>│
  │─── Sync ───────────────────────────>│
  │                                     │
  │<── ParseComplete ──────────────────│
  │<── BindComplete ───────────────────│
  │<── RowDescription (columns) ───────│
  │<── DataRow ────────────────────────│
  │<── DataRow ────────────────────────│
  │<── ... ────────────────────────────│
  │<── CommandComplete ────────────────│
  │<── ReadyForQuery ──────────────────│
```

**Without pipelining**, the client sends Parse through Sync, then waits for all responses before sending the next query.

**With pipelining**, the client sends multiple Parse/Bind/Execute/Sync sequences back-to-back before reading any responses:

```
Client                                Server
  │                                     │
  │─── Parse  (query A) ──────────────>│
  │─── Bind   (query A) ──────────────>│
  │─── Execute(query A) ──────────────>│
  │─── Sync ───────────────────────────>│
  │─── Parse  (query B) ──────────────>│  ← sent immediately, no waiting
  │─── Bind   (query B) ──────────────>│
  │─── Execute(query B) ──────────────>│
  │─── Sync ───────────────────────────>│
  │                                     │
  │<── ParseComplete (A) ──────────────│  ← responses arrive in order
  │<── BindComplete (A) ───────────────│
  │<── DataRow (A) ────────────────────│
  │<── CommandComplete (A) ────────────│
  │<── ReadyForQuery ──────────────────│
  │<── ParseComplete (B) ──────────────│
  │<── DataRow (B) ────────────────────│
  │<── CommandComplete (B) ────────────│
  │<── ReadyForQuery ──────────────────│
```

The server processes queries in order and streams results back. The client matches responses to requests in FIFO order. Multiple queries pay only one round-trip worth of latency instead of one per query.

### Prepared Statement Optimization

When a statement is already in the cache, PgPipe skips the Parse message entirely — only Bind, Execute, and Sync are sent. This reduces wire overhead for repeated queries.

### Placeholder Conversion

JDBC uses `?` for parameter placeholders. PostgreSQL uses `$1`, `$2`, etc. PgPipe converts these automatically and caches the result globally:

```
SELECT * FROM users WHERE id = ? AND name = ?
→
SELECT * FROM users WHERE id = $1 AND name = $2
```

Placeholders inside quoted strings are left untouched.

## Connection Architecture

Each `PgPipelinedConnection` consists of:

- **A raw TCP socket** (or SSLSocket) to PostgreSQL
- **A sender thread** (virtual) that reads from the send queue and writes protocol messages to the socket
- **A receiver thread** (virtual) that reads responses from the socket and completes pending futures
- **A pending operations deque** that tracks in-flight requests
- **A semaphore** that limits concurrent in-flight operations to `pipeliningLimit`

```
                    ┌──────────────────────────────────────────────┐
                    │           PgPipelinedConnection              │
Caller ─submit()──> │  sendQueue ──> Sender Thread ──> Socket ──> │ ──> PostgreSQL
                    │                                              │
                    │  pendingOps <── Receiver Thread <── Socket <──│ <── PostgreSQL
Caller <─future───  │                                              │
                    └──────────────────────────────────────────────┘
```

### Submit Flow

1. Caller calls `submit(sql, params, resultFormats)` — returns a `CompletableFuture<QueryResult>`
2. A `PipelineOp` is created and enqueued in the send queue
3. The sender thread dequeues the op, writes Parse/Bind/Execute/Sync to the socket
4. The op is added to the pending operations deque
5. The receiver thread reads responses, matches them to the first pending op, and completes its future

### Deferred BEGIN

When `transact()` is called, PgPipe doesn't send BEGIN immediately. Instead, it marks the connection with a pending BEGIN. The first actual query is then sent as a `WithBegin` operation — BEGIN and the query are written to the socket in a single flush, saving a round-trip.

If the callback returns without executing any queries, no transaction is ever started.

## Pool Architecture

`PgPipelinePool` manages an array of `PgPipelinedConnection` instances:

```
                    ┌─────────────────────────────────┐
                    │         PgPipelinePool           │
                    │                                  │
  execute(op) ─────>│  round-robin ──> Connection[0]   │
  query(sql) ──────>│              ──> Connection[1]   │
  transactRead─>│              ──> Connection[2]   │
                    │              ──> ...              │
                    │                                  │
  transact(fn) ────>│  reserve ────> Connection[i]     │ (dedicated for tx)
                    │                                  │
                    │  maintenance thread              │
                    └─────────────────────────────────┘
```

### Connection Selection

**Non-transactional queries** use round-robin selection across all non-reserved, healthy connections. If a connection is unhealthy, it's replaced on the spot.

**Transactions** acquire a semaphore (limited to `maxTransactionConnections`) and then atomically reserve one connection using `compareAndSet` on an `AtomicBoolean` array. The reserved connection is dedicated to the transaction until it completes.

### Backpressure

Multiple layers of backpressure prevent unbounded resource consumption:

1. **Send queue capacity** (default 1024) — callers block when the queue is full
2. **Pipelining semaphore** (default 256 per connection) — limits in-flight operations
3. **Transaction semaphore** (`maxTransactionConnections`) — limits concurrent transactions
4. **Exhaustion strategy** — BLOCK waits for availability, THROW fails immediately

## Error Recovery

### Connection-Level Errors

When a connection fails (IOException, unexpected close), the receiver thread detects the error and:

1. Fails all pending futures with `PgPipelineException`
2. Triggers the fatal error callback
3. A new virtual thread spawns to replace the connection

If replacement fails, exponential backoff retries up to 5 times with delays of 1s, 2s, 4s, 8s, 16s (capped at 30s).

### Query-Level Errors

PostgreSQL error responses (ErrorResponse) are matched to the corresponding pending operation and surfaced as `PgPipelineException`. The connection remains healthy — only the specific query fails.

### Stale Statement Errors

When a schema change invalidates a cached prepared statement, PostgreSQL returns error codes `26000` (invalid prepared statement), `42P05` (duplicate), or `0A000` (feature not supported on cached plan). PgPipe detects these codes, evicts the stale entry from the cache, and the next execution will re-prepare the statement.

### Read-Only Retry

`execute(Operation)` for read-only operations has a transparent retry: if the first attempt fails with a connection error, it retries once on a (possibly different) connection. This covers transient network blips without surfacing errors to the caller.

## Cursor Streaming Internals

Streaming cursors use PostgreSQL's portal mechanism. A named portal is created via Parse/Bind, then Execute is called with a `maxRows` limit. The server returns up to `maxRows` DataRow messages followed by PortalSuspended (more data available) or CommandComplete (done).

### Deep Prefetching

After the initial batch arrives through the normal sender/receiver path, the cursor takes over the socket directly:

1. The cursor requests the receiver thread to yield via `awaitReceiverYield()`
2. The receiver thread parks at a yield flag
3. The cursor thread sends `cursorPrefetchDepth` Execute messages directly to the socket
4. The cursor thread reads responses directly from the socket

This socket handoff eliminates the send queue / pending deque overhead for cursor fetches. After the initial round-trip, batches arrive continuously:

```
Cursor Thread                         Server
  │                                     │
  │─── Execute(portal, 1000) ─────────>│  ← prefetch[1]
  │─── Execute(portal, 1000) ─────────>│  ← prefetch[2]
  │─── Execute(portal, 1000) ─────────>│  ← prefetch[3]
  │─── Flush ─────────────────────────>│
  │                                     │
  │<── DataRow × 1000 ────────────────│  ← batch 1 arrives
  │    (consumer processes rows)        │
  │<── DataRow × 1000 ────────────────│  ← batch 2 already in flight
  │─── Execute(portal, 1000) ─────────>│  ← refill prefetch window
  │    ...                              │
```

## Authentication Flow

PgPipe negotiates authentication during connection startup:

1. Client sends StartupMessage (protocol version, user, database, application_name, client_encoding)
2. Server responds with an authentication request:
   - **AuthenticationOk** — no auth needed (trust mode)
   - **AuthenticationCleartextPassword** — client sends plaintext password
   - **AuthenticationMD5Password** — client sends `md5(md5(password + user) + salt)`
   - **AuthenticationSASL** — SCRAM-SHA-256 negotiation (3 additional round-trips)
3. After auth, server sends ParameterStatus messages (server_version, encoding, etc.), BackendKeyData, and ReadyForQuery

### SCRAM-SHA-256

The SCRAM flow is implemented entirely with JDK crypto (PBKDF2WithHmacSHA256, HmacSHA256):

1. Client generates a 24-byte random nonce and sends ClientFirstMessage
2. Server responds with salt, iteration count, and combined nonce
3. Client derives SaltedPassword via PBKDF2, computes ClientProof
4. Server verifies and sends ServerSignature
5. Client verifies ServerSignature

## SSL/TLS Negotiation

SSL is negotiated before the PostgreSQL protocol starts:

1. Client sends SSLRequest (8 bytes: length=8, code=80877103)
2. Server responds with a single byte: `S` (accept) or `N` (reject)
3. On `S`, the client wraps the socket in an SSLSocket
4. Normal PostgreSQL protocol begins over the encrypted connection

PEM files are parsed with JDK-only APIs (`CertificateFactory`, `KeyFactory`). Both RSA and EC private keys are supported.

## Virtual Threads

PgPipe uses virtual threads (Java 21+) for all background work:

- **Sender threads**: one per connection, named `pg-sender-N`
- **Receiver threads**: one per connection, named `pg-receiver-N`
- **Maintenance thread**: one per pool, named `pg-pipeline-maintenance`
- **Reconnection threads**: spawned on demand, named `pg-reconnect-N`

Virtual threads enable thousands of concurrent connections with minimal memory overhead. Each thread is lightweight — no 1MB stack allocation like platform threads.
