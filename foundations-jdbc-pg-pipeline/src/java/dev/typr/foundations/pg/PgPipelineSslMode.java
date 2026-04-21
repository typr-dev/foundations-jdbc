package dev.typr.foundations.pg;

/** SSL mode for PgPipeline connections, matching PostgreSQL's sslmode parameter semantics. */
public enum PgPipelineSslMode {
  /** No SSL. Connection is unencrypted. */
  DISABLE,
  /** SSL required but no certificate verification. Protects against passive eavesdropping. */
  REQUIRE,
  /** SSL required, server certificate verified against a trusted CA. */
  VERIFY_CA,
  /** SSL required, server certificate verified against a trusted CA and hostname must match. */
  VERIFY_FULL
}
