package dev.typr.foundations.analysis;

import dev.typr.foundations.JdbcTypeInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry mapping SQL type names to JDBC type information.
 * Used by query analysis to look up JDBC compatibility for types.
 *
 * <p>This registry-based approach allows query analysis without modifying
 * the existing DbType record structures.
 */
public final class JdbcTypeRegistry {

  private static final Map<String, JdbcTypeInfo> REGISTRY = new HashMap<>();

  static {
    // ─────────────────────────────────────────────────────────────────────────
    // PostgreSQL types
    // ─────────────────────────────────────────────────────────────────────────

    // Boolean
    register("bool", JdbcTypeInfo.BOOLEAN);
    register("boolean", JdbcTypeInfo.BOOLEAN);

    // Integer types
    register("int2", JdbcTypeInfo.SMALLINT);
    register("smallint", JdbcTypeInfo.SMALLINT);
    register("smallserial", JdbcTypeInfo.SMALLINT);

    register("int4", JdbcTypeInfo.INTEGER);
    register("integer", JdbcTypeInfo.INTEGER);
    register("serial", JdbcTypeInfo.INTEGER);
    register("int", JdbcTypeInfo.INTEGER);

    register("int8", JdbcTypeInfo.BIGINT);
    register("bigint", JdbcTypeInfo.BIGINT);
    register("bigserial", JdbcTypeInfo.BIGINT);

    register("oid", JdbcTypeInfo.OID);

    // Floating point
    register("float4", JdbcTypeInfo.REAL);
    register("real", JdbcTypeInfo.REAL);

    register("float8", JdbcTypeInfo.DOUBLE);
    register("double precision", JdbcTypeInfo.DOUBLE);

    // Numeric
    register("numeric", JdbcTypeInfo.NUMERIC);
    register("decimal", JdbcTypeInfo.NUMERIC);
    register("money", JdbcTypeInfo.MONEY);

    // Text types
    register("text", JdbcTypeInfo.VARCHAR);
    register("varchar", JdbcTypeInfo.VARCHAR);
    register("character varying", JdbcTypeInfo.VARCHAR);
    register("name", JdbcTypeInfo.VARCHAR);

    register("bpchar", JdbcTypeInfo.CHAR);
    register("char", JdbcTypeInfo.CHAR);
    register("character", JdbcTypeInfo.CHAR);

    // Binary
    register("bytea", JdbcTypeInfo.BYTEA);

    // Date/Time
    register("date", JdbcTypeInfo.DATE);
    register("time", JdbcTypeInfo.TIME);
    register("time without time zone", JdbcTypeInfo.TIME);
    register("timetz", JdbcTypeInfo.TIME_WITH_TIMEZONE);
    register("time with time zone", JdbcTypeInfo.TIME_WITH_TIMEZONE);
    register("timestamp", JdbcTypeInfo.TIMESTAMP);
    register("timestamp without time zone", JdbcTypeInfo.TIMESTAMP);
    register("timestamptz", JdbcTypeInfo.TIMESTAMP_WITH_TIMEZONE);
    register("timestamp with time zone", JdbcTypeInfo.TIMESTAMP_WITH_TIMEZONE);
    register("interval", JdbcTypeInfo.INTERVAL);

    // UUID
    register("uuid", JdbcTypeInfo.UUID);

    // JSON
    register("json", JdbcTypeInfo.JSON);
    register("jsonb", JdbcTypeInfo.JSONB);

    // XML
    register("xml", JdbcTypeInfo.XML);

    // Network types
    register("inet", JdbcTypeInfo.INET);
    register("cidr", JdbcTypeInfo.CIDR);
    register("macaddr", JdbcTypeInfo.MACADDR);
    register("macaddr8", JdbcTypeInfo.MACADDR8);

    // Geometric types
    register("point", JdbcTypeInfo.POINT);
    register("line", JdbcTypeInfo.LINE);
    register("lseg", JdbcTypeInfo.LSEG);
    register("box", JdbcTypeInfo.BOX);
    register("path", JdbcTypeInfo.PATH);
    register("polygon", JdbcTypeInfo.POLYGON);
    register("circle", JdbcTypeInfo.CIRCLE);

    // Other PostgreSQL types
    register("hstore", JdbcTypeInfo.HSTORE);

    // Range types
    register("int4range", JdbcTypeInfo.RANGE);
    register("int8range", JdbcTypeInfo.RANGE);
    register("numrange", JdbcTypeInfo.RANGE);
    register("daterange", JdbcTypeInfo.RANGE);
    register("tsrange", JdbcTypeInfo.RANGE);
    register("tstzrange", JdbcTypeInfo.RANGE);

    // ─────────────────────────────────────────────────────────────────────────
    // DuckDB types
    // Note: DuckDB JDBC driver uses JAVA_OBJECT (2000) for many types that
    // don't have standard JDBC mappings (HUGEINT, UBIGINT, UUID, INTERVAL, JSON)
    // ─────────────────────────────────────────────────────────────────────────
    register("BOOLEAN", JdbcTypeInfo.BOOLEAN);
    register("TINYINT", JdbcTypeInfo.of(java.sql.Types.TINYINT, "tinyint"));
    register("UTINYINT", JdbcTypeInfo.of(java.sql.Types.TINYINT, "utinyint"));
    register("USMALLINT", JdbcTypeInfo.of(java.sql.Types.SMALLINT, "usmallint"));
    register("UINTEGER", JdbcTypeInfo.of(java.sql.Types.INTEGER, "uinteger"));
    // DuckDB returns JAVA_OBJECT for large integers
    register("UBIGINT", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "ubigint"));
    register("HUGEINT", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "hugeint"));
    register("UHUGEINT", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "uhugeint"));
    register("FLOAT", JdbcTypeInfo.REAL);
    register("DOUBLE", JdbcTypeInfo.DOUBLE);
    register("DECIMAL", JdbcTypeInfo.NUMERIC);
    register("TEXT", JdbcTypeInfo.VARCHAR);
    // DuckDB BLOB uses JDBC type BLOB (2004)
    register("BLOB", new JdbcTypeInfo(
        Set.of(java.sql.Types.BLOB),
        Set.of(java.sql.Types.BLOB, java.sql.Types.BINARY, java.sql.Types.VARBINARY),
        Set.of("blob")
    ));
    // DuckDB INTERVAL uses JAVA_OBJECT
    register("INTERVAL", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "interval"));
    // DuckDB UUID uses JAVA_OBJECT
    register("UUID", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "uuid"));
    // DuckDB JSON uses JAVA_OBJECT
    register("JSON", JdbcTypeInfo.of(java.sql.Types.JAVA_OBJECT, "json"));
    // DuckDB TIMESTAMP WITH TIME ZONE
    register("TIMESTAMP WITH TIME ZONE", JdbcTypeInfo.TIMESTAMP_WITH_TIMEZONE);

    // System types
    register("aclitem", JdbcTypeInfo.of(java.sql.Types.OTHER, "aclitem"));
    register("int2vector", JdbcTypeInfo.of(java.sql.Types.OTHER, "int2vector"));
    register("oidvector", JdbcTypeInfo.of(java.sql.Types.OTHER, "oidvector"));
    register("pg_node_tree", JdbcTypeInfo.of(java.sql.Types.OTHER, "pg_node_tree"));
    register("regclass", JdbcTypeInfo.of(java.sql.Types.OTHER, "regclass"));
    register("regconfig", JdbcTypeInfo.of(java.sql.Types.OTHER, "regconfig"));
    register("regdictionary", JdbcTypeInfo.of(java.sql.Types.OTHER, "regdictionary"));
    register("regnamespace", JdbcTypeInfo.of(java.sql.Types.OTHER, "regnamespace"));
    register("regoper", JdbcTypeInfo.of(java.sql.Types.OTHER, "regoper"));
    register("regoperator", JdbcTypeInfo.of(java.sql.Types.OTHER, "regoperator"));
    register("regproc", JdbcTypeInfo.of(java.sql.Types.OTHER, "regproc"));
    register("regprocedure", JdbcTypeInfo.of(java.sql.Types.OTHER, "regprocedure"));
    register("regrole", JdbcTypeInfo.of(java.sql.Types.OTHER, "regrole"));
    register("regtype", JdbcTypeInfo.of(java.sql.Types.OTHER, "regtype"));
    register("xid", JdbcTypeInfo.of(java.sql.Types.OTHER, "xid"));
    register("record", JdbcTypeInfo.of(java.sql.Types.OTHER, "record"));
    register("anyarray", JdbcTypeInfo.of(java.sql.Types.ARRAY, "anyarray"));
    register("unknown", JdbcTypeInfo.of(java.sql.Types.OTHER, "unknown"));
    register("vector", JdbcTypeInfo.of(java.sql.Types.OTHER, "vector"));
  }

  private JdbcTypeRegistry() {}

  private static void register(String sqlType, JdbcTypeInfo info) {
    REGISTRY.put(sqlType.toLowerCase(), info);
  }

  /**
   * Look up JDBC type info for a SQL type name.
   *
   * @param sqlType the SQL type name (e.g., "int4", "text", "timestamptz")
   * @return the JDBC type info, or {@link JdbcTypeInfo#NONE} if not found
   */
  public static JdbcTypeInfo lookup(String sqlType) {
    if (sqlType == null || sqlType.isEmpty()) {
      return JdbcTypeInfo.NONE;
    }

    // Normalize: lowercase, strip precision/array suffix for lookup
    String normalized = normalizeSqlType(sqlType);
    JdbcTypeInfo info = REGISTRY.get(normalized);

    if (info != null) {
      return info;
    }

    // Check if it's an array type (ends with [])
    if (sqlType.endsWith("[]")) {
      return JdbcTypeInfo.ARRAY;
    }

    return JdbcTypeInfo.NONE;
  }

  /**
   * Normalize a SQL type name for registry lookup.
   * Strips precision specifiers and converts to lowercase.
   */
  private static String normalizeSqlType(String sqlType) {
    String lower = sqlType.toLowerCase().trim();

    // Strip array suffix
    if (lower.endsWith("[]")) {
      return lower.substring(0, lower.length() - 2);
    }

    // Strip precision specifier like "varchar(255)" -> "varchar"
    int parenIdx = lower.indexOf('(');
    if (parenIdx > 0) {
      lower = lower.substring(0, parenIdx);
    }

    return lower;
  }

  /**
   * Get the set of JDBC types that a SQL type can write to.
   */
  public static Set<Integer> jdbcTargets(String sqlType) {
    return lookup(sqlType).jdbcTargets();
  }

  /**
   * Get the set of JDBC types that a SQL type can read from.
   */
  public static Set<Integer> jdbcSources(String sqlType) {
    return lookup(sqlType).jdbcSources();
  }
}
