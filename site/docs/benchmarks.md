---
sidebar_position: 9
---

# Benchmarks

## Methodology

All benchmarks run against an **in-memory DuckDB** database on a single shared connection.
Each library executes a SELECT returning **15 columns** of varied types (VARCHAR, INTEGER, DECIMAL, DOUBLE, BOOLEAN, TIMESTAMP, DATE) and maps every row into a Kotlin data class.

- **5** warmup runs (discarded)
- **11** measured runs, **median** reported
- Row counts verified on every run
- All libraries share the same `javax.sql.DataSource` (single connection, no pooling overhead)
- VARCHAR columns contain realistic-length strings (50-200 chars)

## Results

| Rows | Raw JDBC | Foundations JDBC | Hibernate (entity) | Hibernate (native) | JDBI | JdbcTemplate |
| ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| 1 | 288.5 us | 383.8 us | 775.4 us | 544.5 us | 415.2 us | 297.4 us |
| 10 | 391.3 us | 403.0 us | 1.05 ms | 487.6 us | 434.5 us | 294.0 us |
| 100 | 934.4 us | 1.00 ms | 2.20 ms | 1.07 ms | 916.0 us | 875.4 us |
| 1,000 | 6.18 ms | 5.79 ms | 9.03 ms | 7.36 ms | 6.65 ms | 6.41 ms |
| 10,000 | 54.49 ms | 55.15 ms | 64.75 ms | 58.60 ms | 55.33 ms | 55.69 ms |
| 100,000 | 569.79 ms | 581.46 ms | 687.91 ms | 595.33 ms | 582.34 ms | 585.63 ms |

## Libraries

| Library | Approach |
| :--- | :--- |
| **Raw JDBC** | `PreparedStatement` + positional `getInt`/`getString` — theoretical minimum |
| **Foundations JDBC** | `RowCodec` + `Fragment.query()` + `Transactor` |
| **Hibernate (entity)** | `@Entity` + `SessionFactory` + native SQL mapped to managed entity — full ORM machinery |
| **Hibernate (native)** | `SessionFactory` + native SQL + manual `Object[]` tuple mapping — lightest Hibernate usage |
| **JDBI** | `Jdbi.create(ds)` + `RowMapper` + `withHandle` |
| **JdbcTemplate** | `JdbcTemplate(ds)` + `RowMapper` |

## Notes

- This benchmark measures **mapping overhead**, not database query execution time. All libraries run the same SQL against the same in-memory DuckDB instance.
- **Hibernate (entity)** uses `@Entity` annotation with full ORM machinery: bytecode-enhanced entity hydration, persistence context management, and dirty-checking state snapshots. This is how Hibernate is typically used in production.
- **Hibernate (native)** uses `createNativeQuery` with `Object[]` tuple result and manual column mapping — the absolute lightest-weight Hibernate usage, bypassing the entire entity lifecycle.
- DuckDB is an embedded analytical database — there is no network latency. Differences between libraries are pure framework overhead.
