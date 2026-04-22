package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint2;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * DuckDB type definitions for the typr-runtime-java library.
 *
 * <p>DuckDB has a rich type system including: - Standard SQL types (INTEGER, VARCHAR, etc.) -
 * Extended integer types (HUGEINT, UHUGEINT, UTINYINT, etc.) - Nested types (LIST, STRUCT, MAP,
 * UNION) - Temporal types with various precisions
 */
public interface DuckDbTypes {
  // ==================== Integer Types (Signed) ====================

  DuckDbType<Byte> tinyint =
      DuckDbType.of(
              "TINYINT",
              DuckDbRead.readByte,
              DuckDbWrite.writeByte,
              DuckDbStringifier.tinyint,
              DuckDbJson.int1)
          .withListCodec(DuckDbListCodec.nativeCodec(Byte[]::new));

  DuckDbType<Short> smallint =
      DuckDbType.of(
              "SMALLINT",
              DuckDbRead.readShort,
              DuckDbWrite.writeShort,
              DuckDbStringifier.smallint,
              DuckDbJson.int2)
          .withListCodec(DuckDbListCodec.nativeCodec(Short[]::new));

  DuckDbType<Integer> integer =
      DuckDbType.of(
              "INTEGER",
              DuckDbRead.readInteger,
              DuckDbWrite.writeInteger,
              DuckDbStringifier.integer,
              DuckDbJson.int4)
          .withListCodec(DuckDbListCodec.nativeCodec(Integer[]::new));

  DuckDbType<Long> bigint =
      DuckDbType.of(
              "BIGINT",
              DuckDbRead.readLong,
              DuckDbWrite.writeLong,
              DuckDbStringifier.bigint,
              DuckDbJson.int8)
          .withListCodec(DuckDbListCodec.nativeCodec(Long[]::new));

  // HUGEINT: 128-bit signed integer (-170141183460469231731687303715884105728 to
  // 170141183460469231731687303715884105727)
  DuckDbType<BigInteger> hugeint =
      DuckDbType.of(
              "HUGEINT",
              DuckDbRead.readBigInteger,
              DuckDbWrite.writeBigInteger,
              DuckDbStringifier.hugeint,
              DuckDbJson.hugeint)
          .withListCodec(DuckDbListCodec.sqlLiteralCast())
          .withStructAttributeEncoder(DuckDbStringifier.hugeint.asWireEncoder());

  // ==================== Integer Types (Unsigned) ====================

  // UTINYINT: 0-255, wrapped in Uint1
  DuckDbType<Uint1> utinyint =
      DuckDbType.of(
              "UTINYINT",
              DuckDbRead.readShort.map(Uint1::new),
              DuckDbWrite.writeShort.contramap(Uint1::value),
              DuckDbStringifier.smallint.contramap(Uint1::value),
              DuckDbJson.int2.transform(Uint1::new, Uint1::value))
          .withStructAttributeEncoder(
              DuckDbStringifier.smallint.contramap(Uint1::value).asWireEncoder());

  // USMALLINT: 0-65535, wrapped in Uint2
  DuckDbType<Uint2> usmallint =
      DuckDbType.of(
              "USMALLINT",
              DuckDbRead.readInteger.map(Uint2::new),
              DuckDbWrite.writeInteger.contramap(Uint2::value),
              DuckDbStringifier.integer.contramap(Uint2::value),
              DuckDbJson.int4.transform(Uint2::new, Uint2::value))
          .withStructAttributeEncoder(
              DuckDbStringifier.integer.contramap(Uint2::value).asWireEncoder());

  // UINTEGER: 0-4294967295, wrapped in Uint4
  DuckDbType<Uint4> uinteger =
      DuckDbType.of(
              "UINTEGER",
              DuckDbRead.readLong.map(Uint4::new),
              DuckDbWrite.writeLong.contramap(Uint4::value),
              DuckDbStringifier.bigint.contramap(Uint4::value),
              DuckDbJson.int8.transform(Uint4::new, Uint4::value))
          .withStructAttributeEncoder(
              DuckDbStringifier.bigint.contramap(Uint4::value).asWireEncoder());

  // UBIGINT: 0-18446744073709551615, wrapped in Uint8
  DuckDbType<Uint8> ubigint =
      DuckDbType.of(
              "UBIGINT",
              DuckDbRead.readBigInteger.map(Uint8::of),
              DuckDbWrite.writeBigInteger.contramap(Uint8::value),
              DuckDbStringifier.hugeint.contramap(Uint8::value),
              DuckDbJson.hugeint.transform(Uint8::of, Uint8::value))
          .withStructAttributeEncoder(
              DuckDbStringifier.hugeint.contramap(Uint8::value).asWireEncoder());

  // UHUGEINT: 128-bit unsigned integer, needs BigInteger
  DuckDbType<BigInteger> uhugeint =
      DuckDbType.of(
              "UHUGEINT",
              DuckDbRead.readBigInteger,
              DuckDbWrite.writeBigInteger,
              DuckDbStringifier.hugeint,
              DuckDbJson.hugeint)
          .withStructAttributeEncoder(DuckDbStringifier.hugeint.asWireEncoder());

  // ==================== Floating-Point Types ====================

  DuckDbType<Float> float_ =
      DuckDbType.of(
              "FLOAT",
              DuckDbRead.readFloat,
              DuckDbWrite.writeFloat,
              DuckDbStringifier.float4,
              DuckDbJson.float4)
          .withListCodec(DuckDbListCodec.nativeCodec(Float[]::new));

  DuckDbType<Double> double_ =
      DuckDbType.of(
              "DOUBLE",
              DuckDbRead.readDouble,
              DuckDbWrite.writeDouble,
              DuckDbStringifier.float8,
              DuckDbJson.float8)
          .withListCodec(DuckDbListCodec.nativeCodec(Double[]::new));

  // Aliases
  DuckDbType<Float> real = float_.renamed("REAL");
  DuckDbType<Float> float4 = float_.renamed("FLOAT4");
  DuckDbType<Double> float8 = double_.renamed("FLOAT8");

  // ==================== Fixed-Point Types ====================

  DuckDbType<BigDecimal> decimal =
      DuckDbType.of(
              "DECIMAL",
              DuckDbRead.readBigDecimal,
              DuckDbWrite.writeBigDecimal,
              DuckDbStringifier.numeric,
              DuckDbJson.numeric)
          .withListCodec(DuckDbListCodec.sqlLiteralCast());

  DuckDbType<BigDecimal> numeric = decimal.renamed("NUMERIC");

  static DuckDbType<BigDecimal> decimalOf(int precision, int scale) {
    return DuckDbType.of(
        DuckDbTypename.of("DECIMAL", precision, scale),
        DuckDbRead.readBigDecimal,
        DuckDbWrite.writeBigDecimal,
        DuckDbStringifier.numeric,
        DuckDbJson.numeric);
  }

  // ==================== Boolean Type ====================

  DuckDbType<Boolean> boolean_ =
      DuckDbType.of(
              "BOOLEAN",
              DuckDbRead.readBoolean,
              DuckDbWrite.writeBoolean,
              DuckDbStringifier.bool,
              DuckDbJson.bool)
          .withListCodec(DuckDbListCodec.nativeCodec(Boolean[]::new));

  /** Alias for {@link #boolean_} — aesthetic, avoids the Java-keyword {@code _} suffix. */
  DuckDbType<Boolean> bool = boolean_;

  // ==================== String Types ====================

  DuckDbType<String> varchar =
      DuckDbType.of(
              "VARCHAR",
              DuckDbRead.readString,
              DuckDbWrite.writeString,
              DuckDbStringifier.string,
              DuckDbJson.text)
          .withListCodec(DuckDbListCodec.nativeCodec(String[]::new));

  DuckDbType<String> text =
      varchar
          .renamed("TEXT")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("varchar")));
  DuckDbType<String> string =
      varchar
          .renamed("STRING")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("varchar")));
  DuckDbType<String> char_ =
      varchar
          .renamed("CHAR")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("varchar")));

  /** Alias for {@link #char_} — aesthetic, avoids the Java-keyword {@code _} suffix. */
  DuckDbType<String> character = char_;

  DuckDbType<String> bpchar =
      varchar
          .renamed("BPCHAR")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("varchar")));

  static DuckDbType<String> varcharOf(int length) {
    return DuckDbType.of(
        DuckDbTypename.of("VARCHAR", length),
        DuckDbRead.readString,
        DuckDbWrite.writeString,
        DuckDbStringifier.string,
        DuckDbJson.text);
  }

  static DuckDbType<String> char_Of(int length) {
    return DuckDbType.<String>of(
            DuckDbTypename.of("CHAR", length),
            DuckDbRead.readString,
            DuckDbWrite.writeString,
            DuckDbStringifier.string,
            DuckDbJson.text)
        .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("varchar")));
  }

  // ==================== Binary Types ====================

  DuckDbType<byte[]> blob =
      DuckDbType.of(
          "BLOB",
          DuckDbRead.readByteArray,
          DuckDbWrite.writeByteArray,
          DuckDbStringifier.blob,
          DuckDbJson.blob);
  // BLOB[] / BLOB.list() not usefully supported: binary can't be serialized via DuckDBUserArray.

  DuckDbType<byte[]> bytea = blob.renamed("BYTEA");
  DuckDbType<byte[]> binary = blob.renamed("BINARY");
  DuckDbType<byte[]> varbinary = blob.renamed("VARBINARY");

  // ==================== Bit String Type ====================

  // BIT type - stored as string of 0s and 1s
  DuckDbType<String> bit =
      DuckDbType.of(
          "BIT",
          DuckDbRead.readBitString,
          DuckDbWrite.writeString,
          DuckDbStringifier.string,
          DuckDbJson.bit);

  DuckDbType<String> bitstring = bit.renamed("BITSTRING");

  static DuckDbType<String> bitOf(int length) {
    return DuckDbType.of(
        DuckDbTypename.of("BIT", length),
        DuckDbRead.readBitString,
        DuckDbWrite.writeString,
        DuckDbStringifier.string,
        DuckDbJson.bit);
  }

  // ==================== Date/Time Types ====================

  DuckDbType<LocalDate> date =
      DuckDbType.of(
              "DATE",
              DuckDbRead.readLocalDate,
              DuckDbWrite.passObjectToJdbc(),
              DuckDbStringifier.date,
              DuckDbJson.date)
          .withListCodec(DuckDbListCodec.sqlLiteralCast());

  DuckDbType<LocalTime> time =
      DuckDbType.of(
              "TIME",
              DuckDbRead.readLocalTime,
              DuckDbWrite.writeLocalTime,
              DuckDbStringifier.time,
              DuckDbJson.time)
          .withListCodec(DuckDbListCodec.sqlLiteralCast())
          .withStructAttributeEncoder(DuckDbStringifier.time.asWireEncoder());

  DuckDbType<LocalDateTime> timestamp =
      DuckDbType.of(
              "TIMESTAMP",
              DuckDbRead.readLocalDateTime,
              DuckDbWrite.passObjectToJdbc(),
              DuckDbStringifier.timestamp,
              DuckDbJson.timestamp)
          .withListCodec(
              DuckDbListCodec.sqlLiteral(obj -> ((java.sql.Timestamp) obj).toLocalDateTime()));

  DuckDbType<LocalDateTime> datetime = timestamp.renamed("DATETIME");

  // TIMESTAMP WITH TIME ZONE → Instant.
  //
  // DuckDB stores TIMESTAMPTZ as an INT64 count of microseconds since the Unix epoch — no offset
  // or zone is retained (see https://duckdb.org/docs/sql/data_types/timestamp). The `WITH TIME
  // ZONE` name refers only to input/output rendering in the session zone, not to storage.
  // `Instant` is therefore the honest Java representation — an absolute point in time with no
  // associated zone — and matches the `PgTypes.timestamptz` choice (PG has the same UTC-only
  // semantics). The previous `OffsetDateTime` mapping surfaced the JDBC driver's cosmetic
  // "render in session offset" behaviour as if it were data.
  DuckDbType<Instant> timestamptz =
      DuckDbType.<OffsetDateTime>of(
              "TIMESTAMP WITH TIME ZONE",
              DuckDbRead.readOffsetDateTime,
              DuckDbWrite.passObjectToJdbc(),
              DuckDbStringifier.timestamptz,
              DuckDbJson.timestamptz)
          .transform(OffsetDateTime::toInstant, i -> i.atOffset(ZoneOffset.UTC));

  // TIME WITH TIME ZONE → OffsetTime.
  //
  // Unlike TIMESTAMPTZ (which stores UTC microseconds and discards the offset), DuckDB's TIMETZ
  // does preserve the offset — values round-trip through the JDBC driver with their original
  // offset intact (verified empirically against duckdb_jdbc 1.1.x). The CLI renders TIMETZ in
  // session timezone which can make it look lossy, but that's display-only.
  DuckDbType<OffsetTime> timetz =
      DuckDbType.of(
              "TIME WITH TIME ZONE",
              DuckDbRead.readOffsetTime,
              DuckDbWrite.writeOffsetTime,
              DuckDbStringifier.timetz,
              DuckDbJson.timetz)
          .withStructAttributeEncoder(DuckDbStringifier.timetz.asWireEncoder());

  // Timestamp variants with different precisions
  DuckDbType<LocalDateTime> timestamp_s = timestamp.renamed("TIMESTAMP_S");
  DuckDbType<LocalDateTime> timestamp_ms = timestamp.renamed("TIMESTAMP_MS");
  DuckDbType<LocalDateTime> timestamp_ns = timestamp.renamed("TIMESTAMP_NS");

  // ==================== Interval Type ====================

  DuckDbType<Duration> interval =
      DuckDbType.of(
              "INTERVAL",
              DuckDbRead.readDuration,
              DuckDbWrite.writeDuration,
              DuckDbStringifier.interval,
              DuckDbJson.interval)
          .withListCodec(
              DuckDbListCodec.sqlLiteral(obj -> DuckDbRead.parseDuckDbInterval(obj.toString())))
          .withStructAttributeEncoder(DuckDbStringifier.interval.asWireEncoder());

  // ==================== UUID Type ====================

  DuckDbType<UUID> uuid =
      DuckDbType.of(
              "UUID",
              DuckDbRead.readUuid,
              DuckDbWrite.writeUuid,
              DuckDbStringifier.uuid,
              DuckDbJson.uuid)
          .withListCodec(DuckDbListCodec.sqlLiteralCast())
          .withStructAttributeEncoder(DuckDbStringifier.uuid.asWireEncoder());

  // ==================== JSON Type ====================

  DuckDbType<Json> json =
      DuckDbType.of(
              "JSON",
              DuckDbRead.readString.map(Json::new),
              DuckDbWrite.writeString.contramap(Json::value),
              DuckDbStringifier.string.contramap(Json::value),
              DuckDbJson.json)
          .withListCodec(DuckDbListCodec.sqlLiteral(obj -> new Json(obj.toString())))
          .withStructAttributeEncoder(
              DuckDbStringifier.string.contramap(Json::value).asWireEncoder());

  // ==================== Enum Type ====================

  /**
   * Create a DuckDbType for ENUM columns. DuckDB ENUMs are read/written as strings.
   *
   * @param enumTypeName the name of the enum type (e.g., "mood" for CREATE TYPE mood AS
   *     ENUM('happy', 'sad'))
   * @param fromString function to convert string to enum value
   * @param <E> the enum type
   * @return DuckDbType for the enum
   */
  static <E extends Enum<E>> DuckDbType<E> ofEnum(
      String enumTypeName, Function<String, E> fromString) {
    return ofEnumImpl(enumTypeName, fromString, Enum::name);
  }

  /** Create a DuckDbType for ENUM columns from a values array. No reflection. */
  static <E extends Enum<E>> DuckDbType<E> ofEnum(String enumTypeName, E[] values) {
    return ofEnumImpl(enumTypeName, enumFromString(values, Enum::name), Enum::name);
  }

  /** Create a DuckDbType for ENUM columns without the {@code Enum} bound. */
  static <E> DuckDbType<E> ofEnum(String enumTypeName, E[] values, Function<E, String> name) {
    return ofEnumImpl(enumTypeName, enumFromString(values, name), name);
  }

  private static <E> DuckDbType<E> ofEnumImpl(
      String enumTypeName, Function<String, E> fromString, Function<E, String> name) {
    return DuckDbType.<E>of(
            enumTypeName,
            DuckDbRead.readString.map(fromString::apply),
            DuckDbWrite.writeString.contramap(name::apply),
            DuckDbStringifier.string.contramap(name::apply),
            DuckDbJson.text.transform(fromString::apply, name::apply))
        .withStructAttributeEncoder(name::apply)
        .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(DuckDbTypename.of("enum")));
  }

  private static <E> Function<String, E> enumFromString(E[] values, Function<E, String> name) {
    var map = new java.util.HashMap<String, E>();
    for (E v : values) map.put(name.apply(v), v);
    return s -> {
      E result = map.get(s);
      if (result == null) throw new IllegalArgumentException("No enum constant: " + s);
      return result;
    };
  }

  // ==================== Unknown Type ====================
  // For columns whose type typr doesn't know how to handle - cast to/from string
  DuckDbType<dev.typr.foundations.data.Unknown> unknown =
      DuckDbType.of(
              "VARCHAR",
              DuckDbRead.readString,
              DuckDbWrite.writeString,
              DuckDbStringifier.string,
              DuckDbJson.text)
          .transform(
              dev.typr.foundations.data.Unknown::new, dev.typr.foundations.data.Unknown::value);

  /**
   * JSON codec for MAP types with typed keys and values. Uses JSON object format for compatibility
   * with DuckDB's JSON COPY. Keys are encoded to their JSON string representation.
   */
  static <K, V> DuckDbJson<java.util.Map<K, V>> mapJson(
      DuckDbJson<K> keyJson, DuckDbJson<V> valueJson) {
    return new DuckDbJson<>() {
      @Override
      public JsonValue toJson(java.util.Map<K, V> value) {
        // Serialize as JSON object for compatibility with DuckDB JSON COPY
        // Keys are converted to strings using their JSON representation (without quotes)
        java.util.Map<String, JsonValue> jsonMap = new java.util.LinkedHashMap<>();
        for (var entry : value.entrySet()) {
          JsonValue keyJsonValue = keyJson.toJson(entry.getKey());
          // For string keys, use the raw value; for other types, encode to JSON string
          String keyStr =
              keyJsonValue instanceof JsonValue.JString s ? s.value() : keyJsonValue.encode();
          JsonValue valJson = valueJson.toJson(entry.getValue());
          jsonMap.put(keyStr, valJson);
        }
        return new JsonValue.JObject(jsonMap);
      }

      @Override
      public java.util.Map<K, V> fromJson(JsonValue json) {
        if (!(json instanceof JsonValue.JObject(java.util.Map<String, JsonValue> obj))) {
          throw new IllegalArgumentException(
              "Expected JSON object for MAP, got: " + json.getClass().getSimpleName());
        }
        java.util.Map<K, V> result = new java.util.LinkedHashMap<>();
        for (var entry : obj.entrySet()) {
          String keyStr = entry.getKey();
          // Try to parse the key - if it's already a JSON string, use it directly
          // Otherwise parse it as JSON (for complex types encoded as JSON strings)
          JsonValue keyJsonValue;
          try {
            keyJsonValue = JsonValue.parse(keyStr);
          } catch (Exception e) {
            // Not valid JSON, treat as raw string
            keyJsonValue = new JsonValue.JString(keyStr);
          }
          K key = keyJson.fromJson(keyJsonValue);
          V val = valueJson.fromJson(entry.getValue());
          result.put(key, val);
        }
        return result;
      }
    };
  }

  // ==================== Composite (STRUCT) Types ====================

  /**
   * Build a named STRUCT DuckDbType from a {@link RowCodecNamed}. Use for DuckDB STRUCT types.
   * Supports read, write, stringification, JSON encoding, and {@code .array()}/{@code .list()}.
   *
   * <p>Usage:
   *
   * <pre>{@code
   * DuckDbType<Point> pointType = DuckDbTypes.compositeOf("point",
   *     RowCodec.<Point>namedBuilder()
   *         .field("x", DuckDbTypes.double_, Point::x)
   *         .field("y", DuckDbTypes.double_, Point::y)
   *         .build(Point::new));
   * }</pre>
   *
   * @param structName the STRUCT type name (used in typename metadata)
   * @param codec the named row codec defining the fields
   * @param <Row> the Java type representing this STRUCT
   * @return a DuckDbType for the STRUCT
   */
  static <Row> DuckDbType<Row> compositeOf(String structName, RowCodecNamed<Row> codec) {
    var columns = codec.columns();
    var names = codec.columnNames();
    var duckColumns = new java.util.ArrayList<DuckDbType<?>>(columns.size());
    var typenameFields =
        new java.util.ArrayList<DuckDbTypename.StructOf.StructField>(columns.size());
    for (int i = 0; i < columns.size(); i++) {
      var col = columns.get(i);
      if (!(col instanceof DuckDbType<?> duck)) {
        throw new IllegalArgumentException(
            "compositeOf requires all fields to be DuckDbType, got: "
                + col.getClass().getSimpleName()
                + " at field '"
                + names.get(i)
                + "'");
      }
      duckColumns.add(duck);
      typenameFields.add(new DuckDbTypename.StructOf.StructField(names.get(i), duck.typename()));
    }

    DuckDbTypename.StructOf<Row> typename =
        new DuckDbTypename.StructOf<>(structName, typenameFields);
    var decode = codec.decode();
    var encode = codec.encode();

    java.util.function.Function<Object, Row> structConverter =
        obj -> {
          if (obj instanceof java.sql.Struct struct) {
            try {
              Object[] rawAttrs = struct.getAttributes();
              Object[] decodedAttrs = new Object[duckColumns.size()];
              for (int i = 0; i < duckColumns.size(); i++) {
                decodedAttrs[i] = decodeStructAttribute(duckColumns.get(i), rawAttrs[i]);
              }
              return decode.apply(decodedAttrs);
            } catch (java.sql.SQLException e) {
              throw new DatabaseException.Jdbc(e);
            }
          }
          throw new IllegalArgumentException("Expected STRUCT, got: " + obj.getClass());
        };

    DuckDbRead<Row> duckDbRead =
        new DuckDbRead.NonNullable<>(
            (rs, idx) -> {
              Object obj = rs.getObject(idx);
              if (obj == null) return java.util.Optional.empty();
              return java.util.Optional.of(structConverter.apply(obj));
            },
            structConverter);

    DuckDbStringifier<Row> stringifier =
        DuckDbStringifier.instance(
            (value, sb, quoted) -> {
              Object[] fieldValues = encode.apply(value);
              sb.append("{");
              for (int i = 0; i < duckColumns.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append("'").append(names.get(i)).append("': ");
                encodeStructField(duckColumns.get(i), fieldValues[i], sb);
              }
              sb.append("}");
            });

    final String structSqlType = typename.sqlType();
    java.util.function.Function<Row, Object> structAttributeEncoder =
        row -> {
          if (row == null) return null;
          Object[] rawFields = encode.apply(row);
          Object[] bindFields = new Object[duckColumns.size()];
          for (int i = 0; i < duckColumns.size(); i++) {
            bindFields[i] = applyStructAttributeEncoder(duckColumns.get(i), rawFields[i]);
          }
          return new org.duckdb.user.DuckDBUserStruct(structSqlType, bindFields);
        };

    // Bind via DuckDBUserStruct (setObject) rather than a SQL-literal string (setString).
    // setString with a struct literal causes DuckDB to re-parse nested array elements with
    // their quoting — strings inside a STRUCT's VARCHAR[] field end up keeping their quote
    // characters. DuckDBUserStruct bypasses the literal parser entirely.
    DuckDbWrite<Row> duckDbWrite =
        new DuckDbWrite.Instance<>(
            (ps, idx, obj) -> ps.setObject(idx, obj), structAttributeEncoder);

    DuckDbJson<Row> duckDbJson =
        new DuckDbJson<>() {
          @Override
          public dev.typr.foundations.data.JsonValue toJson(Row value) {
            Object[] fieldValues = encode.apply(value);
            var jsonFields =
                new java.util.LinkedHashMap<String, dev.typr.foundations.data.JsonValue>();
            for (int i = 0; i < fieldValues.length; i++) {
              jsonFields.put(names.get(i), structFieldToJson(duckColumns.get(i), fieldValues[i]));
            }
            return new dev.typr.foundations.data.JsonValue.JObject(jsonFields);
          }

          @Override
          public Row fromJson(dev.typr.foundations.data.JsonValue jsonValue) {
            if (jsonValue instanceof dev.typr.foundations.data.JsonValue.JObject obj) {
              Object[] fieldValues = new Object[duckColumns.size()];
              for (int i = 0; i < duckColumns.size(); i++) {
                dev.typr.foundations.data.JsonValue fieldJson = obj.fields().get(names.get(i));
                fieldValues[i] =
                    (fieldJson == null
                            || fieldJson instanceof dev.typr.foundations.data.JsonValue.JNull)
                        ? null
                        : duckColumns.get(i).duckDbJson().fromJson(fieldJson);
              }
              return decode.apply(fieldValues);
            }
            throw new IllegalArgumentException("Expected JSON object");
          }
        };

    return new DuckDbType<>(
        typename.asGeneric(),
        duckDbRead,
        duckDbWrite,
        stringifier,
        duckDbJson,
        AnalysisOptions.EMPTY,
        DuckDbWrite.writeListOfUserArray(structSqlType, structAttributeEncoder),
        structAttributeEncoder);
  }

  @SuppressWarnings("unchecked")
  private static <F> Object applyStructAttributeEncoder(DuckDbType<F> col, Object value) {
    return col.structAttributeEncoder().apply((F) value);
  }

  /**
   * Decode a raw JDBC attribute (as returned by {@link java.sql.Struct#getAttributes()}) into the
   * field type's Java representation. Each {@link DuckDbType}'s {@link DuckDbRead#fromJdbcValue}
   * handles its own unwrapping — scalars pass through, composites run their struct converter,
   * lists/arrays unwrap {@link java.sql.Array}, and {@link java.util.Optional} wraps correctly.
   */
  private static Object decodeStructAttribute(DuckDbType<?> col, Object raw) {
    return col.read().fromJdbcValue(raw);
  }

  @SuppressWarnings("unchecked")
  private static <F> void encodeStructField(DuckDbType<F> column, Object value, StringBuilder sb) {
    if (value == null) {
      sb.append("NULL");
      return;
    }
    column.stringifier().unsafeEncode((F) value, sb, true);
  }

  @SuppressWarnings("unchecked")
  private static <F> dev.typr.foundations.data.JsonValue structFieldToJson(
      DuckDbType<F> column, Object value) {
    if (value == null) return dev.typr.foundations.data.JsonValue.JNull.INSTANCE;
    return column.duckDbJson().toJson((F) value);
  }

  // ==================== JSON-Encoded Row Types ====================
  //
  // These methods create JSON column types from a RowCodec. The row's fields are serialized
  // into JSON when writing and deserialized back when reading — the JSON column stores the
  // complete row structure.
  //
  // "Array encoded" means each row becomes a positional JSON array: [val1, val2, val3]
  // "Object encoded" means each row becomes a keyed JSON object: {"col1": val1, "col2": val2}

  /**
   * A JSON column type that stores a single row as a positional JSON array. Each field is encoded
   * by position: {@code [val1, val2, val3]}.
   */
  static <Row> DuckDbType<Row> jsonArrayEncoded(RowCodec<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonArray(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /**
   * A JSON column type that stores a list of rows, each as a positional JSON array. The column
   * contains: {@code [[val1, val2], [val3, val4], ...]}.
   */
  static <Row> DuckDbType<List<Row>> jsonArrayEncodedList(RowCodec<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonArray(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }

  /**
   * A JSON column type that stores a single row as a keyed JSON object. Each field is encoded with
   * its column name: {@code {"name": val1, "age": val2}}.
   */
  static <Row> DuckDbType<Row> jsonObjectEncoded(RowCodecNamed<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonObject(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /**
   * A JSON column type that stores a list of rows, each as a keyed JSON object. The column
   * contains: {@code [{"name": val1, "age": val2}, ...]}.
   */
  static <Row> DuckDbType<List<Row>> jsonObjectEncodedList(RowCodecNamed<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonObject(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }
}
