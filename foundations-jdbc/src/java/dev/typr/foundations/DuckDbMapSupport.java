package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Builds {@link DuckDbType} instances for DuckDB MAP columns.
 *
 * <p>Reads delegate per-element decoding to each side's {@link DuckDbRead#fromJdbcValue} so
 * transformed/composite element types compose without per-map plumbing. Writes go through
 * DuckDB's literal parser via the keys'/values' stringifiers wrapped in a
 * {@link org.duckdb.user.DuckDBMap} of {@code String}/{@code String} — that path avoids the
 * type-specific JNI binding gaps the driver has for non-string MAP entries.
 */
final class DuckDbMapSupport {
  private DuckDbMapSupport() {}

  static <K, V> DuckDbType<Map<K, V>> mapType(DuckDbType<K> keyType, DuckDbType<V> valueType) {
    DuckDbTypename<Map<K, V>> typename = keyType.typename().mapTo(valueType.typename());
    String sqlType = typename.sqlType();

    DuckDbRead<Map<K, V>> read =
        DuckDbRead.of((rs, idx) -> readJdbcMap(rs, idx, keyType.read(), valueType.read()));

    DuckDbWrite<Map<K, V>> write =
        DuckDbWrite.primitive(
            (ps, idx, map) ->
                writeJdbcMap(ps, idx, sqlType, map, keyType.stringifier(), valueType.stringifier()));

    DuckDbStringifier<Map<K, V>> stringifier =
        DuckDbStringifier.instance(
            (map, sb, quoted) ->
                stringify(map, sb, keyType.stringifier(), valueType.stringifier()));

    return new DuckDbType<>(
        typename,
        read,
        write,
        stringifier,
        DuckDbTypes.mapJson(keyType.duckDbJson(), valueType.duckDbJson()),
        AnalysisOptions.EMPTY,
        Optional.empty());
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
      result.put(
          keyRead.fromJdbcValue(entry.getKey()), valueRead.fromJdbcValue(entry.getValue()));
    }
    return result;
  }

  private static <K, V> void writeJdbcMap(
      PreparedStatement ps,
      int idx,
      String sqlType,
      Map<K, V> map,
      DuckDbStringifier<K> keyStr,
      DuckDbStringifier<V> valueStr)
      throws SQLException {
    if (map == null) {
      ps.setNull(idx, java.sql.Types.OTHER);
      return;
    }
    var wireMap = new LinkedHashMap<String, String>();
    for (var entry : map.entrySet()) {
      wireMap.put(keyStr.encode(entry.getKey(), false), valueStr.encode(entry.getValue(), false));
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
