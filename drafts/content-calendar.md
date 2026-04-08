# Content Calendar — Foundations JDBC + Typr

## Week 1 (Apr 12-14) — Foundations JDBC Launch
- HN Show HN: Sunday Apr 12, 8-10 AM ET
- Reddit (r/java, r/programming, r/PostgreSQL, r/kotlin, r/scala, r/duckdb, r/jvm, r/database, r/graalvm) + X + Bluesky: Tuesday Apr 14
- Drafts: drafts/launch-posts.md

## Week 2 (Apr 19-21) — PgPipe Launch
- HN: Sunday Apr 19
- Reddit + X + Bluesky: Tuesday Apr 21
- Drafts: drafts/pgpipe-launch-posts.md

## Week 3 (Apr 26-27) — "Stop Flattening Your Data: Structs, Arrays, and Nested Types"
- The rectangular JOIN grid of duplicated values and nulls is a hack
- Array parameters: WHERE id = ANY($1) — one param, not N placeholders
- Array columns: tags, permissions, multi-value fields
- Composite types: rows inside rows (PG composites, DuckDB STRUCT, Oracle OBJECT)
- Arrays of composites: order with line items as a single value
- Batch with UNNEST: pass array of structs, insert in one statement
- Cover PostgreSQL first (richest), then DuckDB, then Oracle
- Status: NEXT UP

## Week 4 (May 3-4) — "Dynamic SQL Done Right: Auto-Verifying All 2^N Query Shapes"
- Templates with .optionally() generating all combinations
- Query Analysis verifies every combination with one test
- Fundamentally changes how you use SQL from Java
- Status: TODO

## Week 5 (May 10-11) — "Zero N+1: Native JSON Aggregation"
- json_agg() / JSON_ARRAYAGG() with RowCodecs
- Parent-child hierarchies in one query
- Pairs with Week 3 (structured types in, JSON aggregation out)
- Status: TODO

## Week 6 (May 17-18) — Typr Launch
- Your database schema → production code in seconds
- Every table → row class, ID type, unsaved type, full repository (CRUD + batch + streaming)
- SQL DSL for the 80% case
- SQL files → typed methods with inferred parameters and nullability
- RowCodecs generated automatically — never write one by hand
- Null safety from column constraints flowing through to code
- Foreign key relationships → type flow (column lineage)
- Unified types: define CustomerId once, flows across PostgreSQL, MariaDB, OpenAPI, Kafka, gRPC
- OpenAPI → server interfaces + typed clients
- Avro/Kafka → typed producers and consumers
- gRPC/Protobuf → services with effect types
- 6 databases, 3 JVM languages
- Status: TODO

## Week 7 (May 24-25) — "Bulk Loading: PostgreSQL COPY at 300K rows/sec"
- Benchmark: COPY vs executeBatch vs Hibernate batch
- StreamingInsert with RowCodecs
- Batch size tuning
- Status: TODO

## Week 8 (May 31-Jun 1) — "Query Analysis Deep Dive"
- How AnalyzableScanner discovers queries
- What it checks: column types, nullability, parameter types, column counts
- How it handles Templates with 2^N shapes
- How it walks composed operations recursively
- Stored procedures as one example (parameter counts, types, modes against catalog)
- Database metadata quirks (DuckDB vs PostgreSQL vs MariaDB reporting)
- Error reports and how to read them
- Status: TODO

## Week 9+ (biweekly rotation)
- Observability without bloat (query listeners, named operations)
- Streaming large result sets with lazy cursors
- Benchmarking: type safety at zero cost
- Kotlin SQL interpolation internals
- Typr deep dives: unified types, SQL DSL, type flow from foreign keys
