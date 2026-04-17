package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Builds {@link DuckDbType} instances for DuckDB MAP columns.
 *
 * <p>Reads delegate per-element decoding to each side's {@link DuckDbRead#fromJdbcValue} so
 * transformed/composite element types compose without per-map plumbing.
 *
 * <p>Writes use a per-side strategy:
 *
 * <ul>
 *   <li>STRUCT / LIST / ARRAY entries are bound natively via each type's {@code
 *       structAttributeEncoder}, which produces {@link org.duckdb.user.DuckDBUserStruct} or {@link
 *       org.duckdb.user.DuckDBUserArray}. This is the same path top-level struct/list parameters
 *       use, so nested {@code VARCHAR[]} fields don't get re-quoted by DuckDB's struct-literal
 *       parser.
 *   <li>Scalar entries are stringified via the type's {@link DuckDbStringifier} and shipped as
 *       {@code String} → DuckDB casts them to the column type. This avoids JNI binding gaps for
 *       scalars whose Java representation isn't natively recognised in {@code DuckDBMap} (UUID,
 *       INTERVAL, JSON, HUGEINT, TIME).
 * </ul>
 */
final class DuckDbMapSupport {
  private DuckDbMapSupport() {}

  static <K, V> DuckDbType<Map<K, V>> mapType(DuckDbType<K> keyType, DuckDbType<V> valueType) {
    DuckDbTypename<Map<K, V>> typename = keyType.typename().mapTo(valueType.typename());
    String sqlType = typename.sqlType();

    Function<K, Object> keyEncoder = keyType.structAttributeEncoder();
    Function<V, Object> valueEncoder = valueType.structAttributeEncoder();

    DuckDbRead<Map<K, V>> read =
        DuckDbRead.of((rs, idx) -> readJdbcMap(rs, idx, keyType.read(), valueType.read()));

    DuckDbWrite<Map<K, V>> write =
        DuckDbWrite.primitive(
            (ps, idx, map) -> writeJdbcMap(ps, idx, sqlType, map, keyEncoder, valueEncoder));

    DuckDbStringifier<Map<K, V>> stringifier =
        DuckDbStringifier.instance(
            (map, sb, quoted) ->
                stringify(map, sb, keyType.stringifier(), valueType.stringifier()));

    // Composite-as-attribute encoder: when this MAP type is itself the value of an outer MAP,
    // LIST, ARRAY or STRUCT, it gets bound as a DuckDBMap object via the same native path
    // composites use. Mirrors the structAttributeEncoder produced by `compositeOf` for STRUCT
    // and `buildCollection` for LIST/ARRAY.
    Function<Map<K, V>, Object> attributeEncoder =
        map -> {
          if (map == null) return null;
          var wireMap = new LinkedHashMap<Object, Object>();
          for (var entry : map.entrySet()) {
            wireMap.put(keyEncoder.apply(entry.getKey()), valueEncoder.apply(entry.getValue()));
          }
          return new org.duckdb.user.DuckDBMap<>(sqlType, wireMap);
        };

    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        DuckDbTypes.mapJson(keyType.duckDbJson(), valueType.duckDbJson()),
        AnalysisOptions.EMPTY,
        DuckDbWrite.writeListOfUserArray(sqlType, attributeEncoder),
        attributeEncoder);
  }

  private static <K, V> Map<K, V> readJdbcMap(
      ResultSet rs, int idx, DuckDbRead<K> keyRead, DuckDbRead<V> valueRead) throws SQLException {
    Object obj = rs.getObject(idx);
    if (obj == null) return null;
    if (!(obj instanceof Map<?, ?> rawMap)) {
      throw new SQLException("Cannot convert " + obj.getClass() + " to Map");
    }
    Map<K, V> result = new LinkedHashMap<>();
    for (var entry : rawMap.entrySet()) {
      result.put(keyRead.fromJdbcValue(entry.getKey()), valueRead.fromJdbcValue(entry.getValue()));
    }
    return result;
  }

  private static <K, V> void writeJdbcMap(
      PreparedStatement ps,
      int idx,
      String sqlType,
      Map<K, V> map,
      Function<K, Object> keyEncoder,
      Function<V, Object> valueEncoder)
      throws SQLException {
    if (map == null) {
      ps.setNull(idx, java.sql.Types.OTHER);
      return;
    }
    var wireMap = new LinkedHashMap<Object, Object>();
    for (var entry : map.entrySet()) {
      wireMap.put(keyEncoder.apply(entry.getKey()), valueEncoder.apply(entry.getValue()));
    }
    ps.setObject(idx, new org.duckdb.user.DuckDBMap<>(sqlType, wireMap));
  }

  private static <K, V> void stringify(
      Map<K, V> map, StringBuilder sb, DuckDbStringifier<K> keyStr, DuckDbStringifier<V> valueStr) {
    if (map.isEmpty()) {
      sb.append("{}");
      return;
    }
    sb.append("{");
    boolean first = true;
    for (var entry : map.entrySet()) {
      if (!first) sb.append(", ");
      first = false;
      keyStr.unsafeEncode(entry.getKey(), sb, true);
      sb.append(": ");
      valueStr.unsafeEncode(entry.getValue(), sb, true);
    }
    sb.append("}");
  }
}
