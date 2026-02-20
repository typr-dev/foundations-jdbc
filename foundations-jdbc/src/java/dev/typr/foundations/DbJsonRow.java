package dev.typr.foundations;

import dev.typr.foundations.data.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating {@link DbJson} codecs from a {@link RowCodec}.
 *
 * <p>Supports two encoding modes:
 *
 * <ul>
 *   <li><b>Array encoding</b> - More compact, values are JSON arrays: {@code [1,
 *       "foo@example.com"]}
 *   <li><b>Object encoding</b> - More readable, values are JSON objects: {@code {"id": 1, "email":
 *       "foo@example.com"}}
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * RowCodec<Email> emailParser = RowCodecs.of(
 *     PgTypes.int4,
 *     PgTypes.text,
 *     Email::new,
 *     email -> new Object[]{email.id(), email.email()}
 * );
 *
 * // Array encoding (compact)
 * DbJson<Email> arrayCodec = DbJsonRow.jsonArray(emailParser);
 *
 * // Object encoding (with field names)
 * DbJson<Email> objectCodec = DbJsonRow.jsonObject(emailParser, List.of("id", "email"));
 *
 * // Compose with list() for JSON arrays of rows
 * DbJson<List<Email>> listCodec = arrayCodec.list();
 *
 * // Compose with opt() for nullable
 * DbJson<Optional<Email>> optCodec = arrayCodec.opt();
 * }</pre>
 */
public final class DbJsonRow {

  private DbJsonRow() {} // utility class

  /**
   * Create a DbJson codec that encodes rows as JSON arrays.
   *
   * <p>This is the most compact encoding. Each row becomes a JSON array where the elements
   * correspond to the columns in order.
   *
   * @param rowCodec the parser that defines the row structure and types
   * @return a DbJson codec for the row type
   */
  public static <Row> DbJson<Row> jsonArray(RowCodec<Row> rowCodec) {
    return new ArrayCodec<>(rowCodec);
  }

  /**
   * Create a DbJson codec that encodes rows as JSON objects, using column names from the parser.
   *
   * @param rowCodec a named parser that carries column names
   * @return a DbJson codec for the row type
   */
  public static <Row> DbJson<Row> jsonObject(RowCodecNamed<Row> rowCodec) {
    return new ObjectCodec<>(rowCodec, rowCodec.columnNames());
  }

  /**
   * Create a DbJson codec that encodes rows as JSON objects with named fields.
   *
   * <p>Each row becomes a JSON object where keys are the column names provided.
   *
   * @param rowCodec the parser that defines the row structure and types
   * @param columnNames the JSON object keys corresponding to each column (in order)
   * @return a DbJson codec for the row type
   * @throws IllegalArgumentException if columnNames size doesn't match rowCodec columns
   */
  public static <Row> DbJson<Row> jsonObject(RowCodec<Row> rowCodec, List<String> columnNames) {
    if (rowCodec.columns().size() != columnNames.size()) {
      throw new IllegalArgumentException(
          "Column count mismatch: RowCodec has "
              + rowCodec.columns().size()
              + " columns, but "
              + columnNames.size()
              + " column names provided");
    }
    return new ObjectCodec<>(rowCodec, List.copyOf(columnNames));
  }

  /** Array encoding: rows become JSON arrays like [val1, val2, ...] */
  private record ArrayCodec<Row>(RowCodec<Row> rowCodec) implements DbJson<Row> {

    @Override
    @SuppressWarnings("unchecked")
    public JsonValue toJson(Row value) {
      Object[] values = rowCodec.encode().apply(value);
      List<JsonValue> elements = new ArrayList<>(values.length);
      for (int i = 0; i < rowCodec.columns().size(); i++) {
        DbJson<Object> jsonCodec = (DbJson<Object>) rowCodec.columns().get(i).json();
        elements.add(jsonCodec.toJson(values[i]));
      }
      return new JsonValue.JArray(elements);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Row fromJson(JsonValue json) {
      if (!(json instanceof JsonValue.JArray(List<JsonValue> arrayValues))) {
        throw new IllegalArgumentException(
            "Expected JSON array for row, got: " + json.getClass().getSimpleName());
      }
      if (arrayValues.size() != rowCodec.columns().size()) {
        throw new IllegalArgumentException(
            "JSON array size "
                + arrayValues.size()
                + " doesn't match column count "
                + rowCodec.columns().size());
      }
      Object[] values = new Object[rowCodec.columns().size()];
      for (int i = 0; i < rowCodec.columns().size(); i++) {
        DbJson<Object> jsonCodec = (DbJson<Object>) rowCodec.columns().get(i).json();
        values[i] = jsonCodec.fromJson(arrayValues.get(i));
      }
      return rowCodec.decode().apply(values);
    }
  }

  /** Object encoding: rows become JSON objects like {"col1": val1, "col2": val2, ...} */
  private record ObjectCodec<Row>(RowCodec<Row> rowCodec, List<String> columnNames)
      implements DbJson<Row> {

    @Override
    @SuppressWarnings("unchecked")
    public JsonValue toJson(Row value) {
      Object[] values = rowCodec.encode().apply(value);
      java.util.LinkedHashMap<String, JsonValue> fields = new java.util.LinkedHashMap<>();
      for (int i = 0; i < rowCodec.columns().size(); i++) {
        DbJson<Object> jsonCodec = (DbJson<Object>) rowCodec.columns().get(i).json();
        fields.put(columnNames.get(i), jsonCodec.toJson(values[i]));
      }
      return new JsonValue.JObject(fields);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Row fromJson(JsonValue json) {
      if (!(json instanceof JsonValue.JObject(Map<String, JsonValue> fields))) {
        throw new IllegalArgumentException(
            "Expected JSON object for row, got: " + json.getClass().getSimpleName());
      }
      Object[] values = new Object[rowCodec.columns().size()];
      for (int i = 0; i < rowCodec.columns().size(); i++) {
        String colName = columnNames.get(i);
        JsonValue colValue = fields.get(colName);
        if (colValue == null || colValue instanceof JsonValue.JNull) {
          values[i] = null;
        } else {
          DbJson<Object> jsonCodec = (DbJson<Object>) rowCodec.columns().get(i).json();
          values[i] = jsonCodec.fromJson(colValue);
        }
      }
      return rowCodec.decode().apply(values);
    }
  }
}
