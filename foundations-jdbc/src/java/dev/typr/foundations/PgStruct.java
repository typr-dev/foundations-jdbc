package dev.typr.foundations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.postgresql.util.PGobject;

/**
 * PostgreSQL composite type (record) support.
 *
 * <p>A composite type is an ordered sequence of named fields with typed values. Example: CREATE
 * TYPE address AS (street VARCHAR, city VARCHAR, zip VARCHAR)
 *
 * <p>In Java, we represent a composite type as a generated record class with typed fields. This
 * class provides the machinery to read/write composite types via JDBC using the PostgreSQL text
 * format.
 *
 * @param <A> the Java type representing this composite (typically a generated record)
 */
public record PgStruct<A>(
    PgTypename.CompositeOf<A> typename,
    List<Field<A, ?>> fields,
    StructReader<A> reader,
    StructWriter<A> writer,
    PgJson<A> json) {

  /**
   * A single field in a composite type.
   *
   * @param <A> the struct type
   * @param <F> the field value type
   */
  public record Field<A, F>(String name, PgType<F> type, Function<A, F> getter) {}

  /** Functional interface for reading a composite from parsed field values. */
  @FunctionalInterface
  public interface StructReader<A> {
    A read(Object[] fieldValues) throws SQLException;
  }

  /** Functional interface for writing a composite to field values. */
  @FunctionalInterface
  public interface StructWriter<A> {
    Object[] write(A value);
  }

  /** Create a PgType for this composite type. */
  public PgType<A> asType() {
    PgRead<A> pgRead =
        PgRead.of(
            (rs, idx) -> {
              Object obj = rs.getObject(idx);
              if (obj == null) return null;
              if (obj instanceof PGobject pgObj) {
                String textValue = pgObj.getValue();
                if (textValue == null) return null;
                return parseFromText(textValue);
              }
              throw new SQLException(
                  "Expected PGobject for composite type, got: " + obj.getClass());
            });

    PgWrite<A> pgWrite =
        new PgWrite.Instance<>(
            (ps, idx, pgObj) -> ps.setObject(idx, pgObj),
            value -> {
              if (value == null) return null;
              PGobject pgObj = new PGobject();
              pgObj.setType(typename.sqlType());
              try {
                pgObj.setValue(encodeToText(value));
              } catch (SQLException e) {
                throw new DatabaseException("Failed to encode composite type", e);
              }
              return pgObj;
            });

    var self = this;
    PgText<A> pgText =
        new PgText<>() {
          @Override
          public void unsafeEncode(A value, StringBuilder sb) {
            sb.append(encodeToText(value));
          }

          @Override
          public void unsafeArrayEncode(A value, StringBuilder sb) {
            // For array encoding, the value needs to be quoted
            unsafeEncode(value, sb);
          }
        };

    PgCompositeText<A> pgCompositeText =
        new PgCompositeText<>() {
          @Override
          public java.util.Optional<String> encode(A value) {
            return java.util.Optional.of(encodeToText(value));
          }

          @Override
          public A decode(String text) {
            try {
              return self.parseFromText(text);
            } catch (SQLException e) {
              throw new DatabaseException("Failed to parse composite type", e);
            }
          }
        };

    PgOutParam<A> pgOutParam = PgOutParam.pgObject(textValue -> parseFromText(textValue));

    return new PgType<>(
        typename.asGeneric(),
        pgRead,
        pgWrite,
        pgText,
        pgCompositeText,
        json,
        pgOutParam,
        AnalysisOptions.EMPTY);
  }

  /** Create an optional version of this composite type. */
  public PgType<Optional<A>> opt() {
    return asType().opt();
  }

  /** Parse a composite value from PostgreSQL text format. */
  private A parseFromText(String text) throws SQLException {
    List<String> parsedFields = PgRecordParser.parse(text);

    if (parsedFields.size() != fields.size()) {
      throw new SQLException(
          "Field count mismatch: expected "
              + fields.size()
              + " but got "
              + parsedFields.size()
              + " in: "
              + text);
    }

    Object[] fieldValues = new Object[fields.size()];
    for (int i = 0; i < fields.size(); i++) {
      Field<A, ?> field = fields.get(i);
      String rawValue = parsedFields.get(i);
      fieldValues[i] = parseFieldValue(field, rawValue);
    }

    return reader.read(fieldValues);
  }

  /** Parse a single field value from text. */
  private <F> F parseFieldValue(Field<A, F> field, String rawValue) {
    if (rawValue == null) {
      return null;
    }
    return field.type().pgCompositeText().decode(rawValue);
  }

  /** Encode a composite value to PostgreSQL text format. */
  private String encodeToText(A value) {
    List<String> encodedFields = new ArrayList<>(fields.size());
    for (Field<A, ?> field : fields) {
      encodedFields.add(encodeFieldValue(field, value));
    }
    return PgRecordParser.encode(encodedFields);
  }

  /** Encode a single field value to text. */
  private <F> String encodeFieldValue(Field<A, F> field, A structValue) {
    F value = field.getter().apply(structValue);
    if (value == null) {
      return null;
    }
    return field.type().pgCompositeText().encode(value).orElse(null);
  }

  // ========================================================================
  // Builder API for creating composite types
  // ========================================================================

  /**
   * Create a type-safe composite type builder.
   *
   * <p>Usage:
   *
   * <pre>{@code
   * PgStruct<Address> struct = PgStruct.<Address>builder("address")
   *     .field("street", PgTypes.text, Address::street)
   *     .field("city", PgTypes.text, Address::city)
   *     .build(Address::new);
   * }</pre>
   *
   * @param <A> the struct type (typically a record)
   * @param typeName the PostgreSQL type name
   * @return a type-safe builder
   */
  public static <A> PgStructBuilders.Builder0<A> builder(String typeName) {
    return PgStructBuilders.builder(typeName);
  }
}
