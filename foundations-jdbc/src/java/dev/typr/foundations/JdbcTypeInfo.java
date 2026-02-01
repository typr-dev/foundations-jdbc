package dev.typr.foundations;

import java.sql.Types;
import java.util.Set;

/**
 * JDBC type information for query analysis. This record captures what JDBC types
 * a database type can read from and write to.
 *
 * @param jdbcTargets JDBC type codes this type can write to (for parameters)
 * @param jdbcSources JDBC type codes this type can read from (for columns)
 * @param vendorTypeNames vendor-specific type names (case-insensitive)
 */
public record JdbcTypeInfo(
    Set<Integer> jdbcTargets,
    Set<Integer> jdbcSources,
    Set<String> vendorTypeNames
) {

  /**
   * Create JDBC type info where targets and sources are the same.
   */
  public static JdbcTypeInfo symmetric(Set<Integer> jdbcTypes, Set<String> vendorNames) {
    return new JdbcTypeInfo(jdbcTypes, jdbcTypes, vendorNames);
  }

  /**
   * Create JDBC type info with a single JDBC type.
   */
  public static JdbcTypeInfo of(int jdbcType, String... vendorNames) {
    return new JdbcTypeInfo(
        Set.of(jdbcType),
        Set.of(jdbcType),
        Set.of(vendorNames)
    );
  }

  /**
   * Create JDBC type info with single target but multiple sources (for reading wider range).
   */
  public static JdbcTypeInfo readWider(int target, Set<Integer> sources, String... vendorNames) {
    return new JdbcTypeInfo(Set.of(target), sources, Set.of(vendorNames));
  }

  /**
   * Empty type info - disables JDBC type checking.
   */
  public static final JdbcTypeInfo NONE = new JdbcTypeInfo(Set.of(), Set.of(), Set.of());

  // ─────────────────────────────────────────────────────────────────────────────
  // Common PostgreSQL JDBC type info constants
  // ─────────────────────────────────────────────────────────────────────────────

  public static final JdbcTypeInfo BOOLEAN = of(Types.BOOLEAN, "bool", "boolean");
  public static final JdbcTypeInfo BIT = of(Types.BIT, "bit");

  public static final JdbcTypeInfo SMALLINT = readWider(
      Types.SMALLINT,
      Set.of(Types.SMALLINT, Types.TINYINT),
      "int2", "smallint", "smallserial"
  );

  public static final JdbcTypeInfo INTEGER = readWider(
      Types.INTEGER,
      Set.of(Types.INTEGER, Types.SMALLINT, Types.TINYINT),
      "int4", "integer", "serial", "int"
  );

  public static final JdbcTypeInfo BIGINT = readWider(
      Types.BIGINT,
      Set.of(Types.BIGINT, Types.INTEGER, Types.SMALLINT, Types.TINYINT),
      "int8", "bigint", "bigserial"
  );

  public static final JdbcTypeInfo REAL = readWider(
      Types.REAL,
      Set.of(Types.REAL, Types.FLOAT),
      "float4", "real"
  );

  public static final JdbcTypeInfo DOUBLE = readWider(
      Types.DOUBLE,
      Set.of(Types.DOUBLE, Types.REAL, Types.FLOAT),
      "float8", "double precision", "double"
  );

  public static final JdbcTypeInfo NUMERIC = new JdbcTypeInfo(
      Set.of(Types.NUMERIC, Types.DECIMAL),
      Set.of(Types.NUMERIC, Types.DECIMAL, Types.BIGINT, Types.INTEGER, Types.SMALLINT,
          Types.DOUBLE, Types.REAL),
      Set.of("numeric", "decimal")
  );

  public static final JdbcTypeInfo VARCHAR = new JdbcTypeInfo(
      Set.of(Types.VARCHAR),
      Set.of(Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.NVARCHAR, Types.NCHAR),
      Set.of("text", "varchar", "character varying", "name", "bpchar", "char", "character")
  );

  public static final JdbcTypeInfo CHAR = new JdbcTypeInfo(
      Set.of(Types.CHAR),
      Set.of(Types.CHAR, Types.VARCHAR, Types.NCHAR),
      Set.of("bpchar", "char", "character")
  );

  public static final JdbcTypeInfo BYTEA = new JdbcTypeInfo(
      Set.of(Types.BINARY, Types.VARBINARY),
      Set.of(Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB),
      Set.of("bytea")
  );

  public static final JdbcTypeInfo DATE = of(Types.DATE, "date");

  public static final JdbcTypeInfo TIME = of(Types.TIME, "time", "time without time zone");

  public static final JdbcTypeInfo TIME_WITH_TIMEZONE = new JdbcTypeInfo(
      Set.of(Types.TIME_WITH_TIMEZONE),
      Set.of(Types.TIME_WITH_TIMEZONE, Types.TIME),
      Set.of("timetz", "time with time zone")
  );

  public static final JdbcTypeInfo TIMESTAMP = new JdbcTypeInfo(
      Set.of(Types.TIMESTAMP),
      Set.of(Types.TIMESTAMP),
      Set.of("timestamp", "timestamp without time zone")
  );

  public static final JdbcTypeInfo TIMESTAMP_WITH_TIMEZONE = new JdbcTypeInfo(
      Set.of(Types.TIMESTAMP_WITH_TIMEZONE),
      Set.of(Types.TIMESTAMP_WITH_TIMEZONE, Types.TIMESTAMP),
      Set.of("timestamptz", "timestamp with time zone")
  );

  public static final JdbcTypeInfo UUID = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR),
      Set.of("uuid")
  );

  public static final JdbcTypeInfo JSON = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR, Types.LONGVARCHAR),
      Set.of("json")
  );

  public static final JdbcTypeInfo JSONB = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR, Types.LONGVARCHAR),
      Set.of("jsonb")
  );

  public static final JdbcTypeInfo ARRAY = new JdbcTypeInfo(
      Set.of(Types.ARRAY),
      Set.of(Types.ARRAY),
      Set.of() // vendor names are element-type specific
  );

  public static final JdbcTypeInfo XML = new JdbcTypeInfo(
      Set.of(Types.SQLXML),
      Set.of(Types.SQLXML, Types.VARCHAR, Types.LONGVARCHAR),
      Set.of("xml")
  );

  public static final JdbcTypeInfo INTERVAL = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("interval")
  );

  public static final JdbcTypeInfo MONEY = new JdbcTypeInfo(
      Set.of(Types.OTHER, Types.DOUBLE),
      Set.of(Types.DOUBLE, Types.OTHER),
      Set.of("money")
  );

  public static final JdbcTypeInfo INET = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR),
      Set.of("inet")
  );

  public static final JdbcTypeInfo CIDR = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR),
      Set.of("cidr")
  );

  public static final JdbcTypeInfo MACADDR = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR),
      Set.of("macaddr")
  );

  public static final JdbcTypeInfo MACADDR8 = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER, Types.VARCHAR),
      Set.of("macaddr8")
  );

  public static final JdbcTypeInfo POINT = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("point")
  );

  public static final JdbcTypeInfo LINE = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("line")
  );

  public static final JdbcTypeInfo LSEG = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("lseg")
  );

  public static final JdbcTypeInfo BOX = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("box")
  );

  public static final JdbcTypeInfo PATH = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("path")
  );

  public static final JdbcTypeInfo POLYGON = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("polygon")
  );

  public static final JdbcTypeInfo CIRCLE = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("circle")
  );

  public static final JdbcTypeInfo HSTORE = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of("hstore")
  );

  public static final JdbcTypeInfo RANGE = new JdbcTypeInfo(
      Set.of(Types.OTHER),
      Set.of(Types.OTHER),
      Set.of() // vendor names are element-type specific
  );

  public static final JdbcTypeInfo OID = new JdbcTypeInfo(
      Set.of(Types.BIGINT),
      Set.of(Types.BIGINT, Types.INTEGER),
      Set.of("oid")
  );
}
