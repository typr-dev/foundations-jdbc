package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.function.Function;

/**
 * SQLite type definitions.
 *
 * <p>SQLite stores every value in one of five storage classes (NULL, INTEGER, REAL, TEXT, BLOB)
 * regardless of the column's declared type. The "type affinity" of a column comes from substring
 * matches on the declared text — see {@link <a
 * href="https://www.sqlite.org/datatype3.html#determination_of_column_affinity">§3.1</a>} for the
 * exact rules.
 *
 * <p>The catalog below picks one canonical declared name per {@link SqliteType} and registers the
 * common SQL aliases as vendor-type names so the query analyzer accepts equivalent declarations
 * (e.g. {@code BIGINT}, {@code INT2} → {@link #integer}; {@code VARCHAR}, {@code CLOB} → {@link
 * #text}). For a defensive setup, declare tables with {@code STRICT} so the database refuses
 * silent affinity coercions.
 */
public interface SqliteTypes {

  // ==================== INTEGER affinity ====================

  /** All values get INTEGER affinity. Read/written as {@code long} (SQLite stores up to 8 bytes). */
  SqliteType<Long> integer =
      SqliteType.of("INTEGER", SqliteRead.readLong, SqliteWrite.writeLong, SqliteJson.int8)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("integer"),
                  SqliteTypename.of("int"),
                  SqliteTypename.of("int2"),
                  SqliteTypename.of("int4"),
                  SqliteTypename.of("int8"),
                  SqliteTypename.of("bigint"),
                  SqliteTypename.of("smallint"),
                  SqliteTypename.of("tinyint"),
                  SqliteTypename.of("mediumint"),
                  SqliteTypename.of("unsigned big int")));

  /** {@code BIGINT} alias for {@link #integer}. */
  SqliteType<Long> bigint = integer.renamed("BIGINT");

  /** {@code INT} alias for {@link #integer}. */
  SqliteType<Integer> int_ =
      SqliteType.of("INT", SqliteRead.readInteger, SqliteWrite.writeInteger, SqliteJson.int4)
          .withAnalysis(integer.analysisOptions());

  /** {@code SMALLINT} alias backed by Java {@code short}. */
  SqliteType<Short> smallint =
      SqliteType.of(
              "SMALLINT", SqliteRead.readShort, SqliteWrite.writeShort, SqliteJson.int2)
          .withAnalysis(integer.analysisOptions());

  /** {@code TINYINT} alias backed by Java {@code byte}. */
  SqliteType<Byte> tinyint =
      SqliteType.of("TINYINT", SqliteRead.readByte, SqliteWrite.writeByte, SqliteJson.int1)
          .withAnalysis(integer.analysisOptions());

  /**
   * {@code BOOLEAN} stored as INTEGER 0/1. The xerial driver maps {@code setBoolean}/{@code
   * getBoolean} for us. The {@code BOOLEAN} keyword has been a recognised type name since SQLite
   * 3.23.0 (2018) but is still NUMERIC-affinity under the hood.
   */
  SqliteType<Boolean> boolean_ =
      SqliteType.of(
              "BOOLEAN", SqliteRead.readBoolean, SqliteWrite.writeBoolean, SqliteJson.bool)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("boolean"),
                  SqliteTypename.of("bool"),
                  SqliteTypename.of("integer"),
                  SqliteTypename.of("int")));

  /** Alias for {@link #boolean_} avoiding the Java-keyword suffix. */
  SqliteType<Boolean> bool = boolean_;

  // ==================== REAL affinity ====================

  /** REAL — 8-byte IEEE 754. */
  SqliteType<Double> real =
      SqliteType.of("REAL", SqliteRead.readDouble, SqliteWrite.writeDouble, SqliteJson.float8)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("real"),
                  SqliteTypename.of("double"),
                  SqliteTypename.of("double precision"),
                  SqliteTypename.of("float")));

  SqliteType<Double> double_ = real.renamed("DOUBLE");
  SqliteType<Double> doublePrecision = real.renamed("DOUBLE PRECISION");

  SqliteType<Float> float_ =
      SqliteType.of("FLOAT", SqliteRead.readFloat, SqliteWrite.writeFloat, SqliteJson.float4)
          .withAnalysis(real.analysisOptions());

  // ==================== NUMERIC affinity ====================

  /**
   * NUMERIC / DECIMAL backed by {@link BigDecimal}. SQLite does not enforce precision or scale;
   * the declared {@code DECIMAL(p,s)} is a label only. Storage is whichever class fits — TEXT for
   * arbitrary precision, INTEGER/REAL when the value coerces losslessly.
   */
  SqliteType<BigDecimal> numeric =
      SqliteType.of(
              "NUMERIC",
              SqliteRead.readBigDecimal,
              SqliteWrite.writeBigDecimal,
              SqliteJson.numeric)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("numeric"), SqliteTypename.of("decimal")));

  SqliteType<BigDecimal> decimal = numeric.renamed("DECIMAL");

  static SqliteType<BigDecimal> decimalOf(int precision, int scale) {
    return SqliteType.of(
            SqliteTypename.of("DECIMAL", precision, scale),
            SqliteRead.readBigDecimal,
            SqliteWrite.writeBigDecimal,
            SqliteJson.numeric)
        .withAnalysis(numeric.analysisOptions());
  }

  // ==================== TEXT affinity ====================

  SqliteType<String> text =
      SqliteType.of("TEXT", SqliteRead.readString, SqliteWrite.writeString, SqliteJson.text)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("text"),
                  SqliteTypename.of("varchar"),
                  SqliteTypename.of("char"),
                  SqliteTypename.of("character"),
                  SqliteTypename.of("nvarchar"),
                  SqliteTypename.of("nchar"),
                  SqliteTypename.of("clob"),
                  SqliteTypename.of("varying character"),
                  SqliteTypename.of("native character")));

  SqliteType<String> varchar = text.renamed("VARCHAR");
  SqliteType<String> char_ = text.renamed("CHAR");
  SqliteType<String> clob = text.renamed("CLOB");

  static SqliteType<String> varcharOf(int length) {
    return SqliteType.of(
            SqliteTypename.of("VARCHAR", length),
            SqliteRead.readString,
            SqliteWrite.writeString,
            SqliteJson.text)
        .withAnalysis(text.analysisOptions());
  }

  static SqliteType<String> charOf(int length) {
    return SqliteType.of(
            SqliteTypename.of("CHAR", length),
            SqliteRead.readString,
            SqliteWrite.writeString,
            SqliteJson.text)
        .withAnalysis(text.analysisOptions());
  }

  // ==================== BLOB affinity ====================

  SqliteType<byte[]> blob =
      SqliteType.of("BLOB", SqliteRead.readByteArray, SqliteWrite.writeByteArray, SqliteJson.blob)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("blob"),
                  SqliteTypename.of("binary"),
                  SqliteTypename.of("varbinary")));

  SqliteType<byte[]> binary = blob.renamed("BINARY");
  SqliteType<byte[]> varbinary = blob.renamed("VARBINARY");

  // ==================== Date/Time (TEXT, ISO-8601) ====================
  // SQLite has no date/time storage class. We pin storage to TEXT (ISO-8601) — the same choice
  // the xerial driver makes by default — and parse on read. To use INTEGER or REAL date storage,
  // pass a non-default `dateClass(...)` on SqliteConfig and supply your own SqliteType bindings.

  SqliteType<LocalDate> date =
      SqliteType.of(
              "DATE", SqliteRead.readLocalDate, SqliteWrite.writeLocalDate, SqliteJson.date)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("date"), SqliteTypename.of("text")));

  SqliteType<LocalTime> time =
      SqliteType.of("TIME", SqliteRead.readLocalTime, SqliteWrite.writeLocalTime, SqliteJson.time)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("time"), SqliteTypename.of("text")));

  SqliteType<LocalDateTime> datetime =
      SqliteType.of(
              "DATETIME",
              SqliteRead.readLocalDateTime,
              SqliteWrite.writeLocalDateTime,
              SqliteJson.timestamp)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("datetime"),
                  SqliteTypename.of("timestamp"),
                  SqliteTypename.of("text")));

  /** Alias for {@link #datetime} matching the {@code TIMESTAMP} keyword. */
  SqliteType<LocalDateTime> timestamp = datetime.renamed("TIMESTAMP");

  /**
   * UTC instant stored as ISO-8601 text with {@code Z} suffix (the format produced by {@link
   * Instant#toString()}). SQLite has no zone storage class, so this is the honest representation.
   */
  SqliteType<Instant> instant =
      SqliteType.of(
              "TIMESTAMP", SqliteRead.readInstant, SqliteWrite.writeInstant, SqliteJson.instant)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("timestamp"),
                  SqliteTypename.of("datetime"),
                  SqliteTypename.of("text")));

  // ==================== Convenience types backed by TEXT ====================

  /** UUID stored as canonical 36-character TEXT. */
  SqliteType<UUID> uuid =
      SqliteType.of("UUID", SqliteRead.readUuid, SqliteWrite.writeUuid, SqliteJson.uuid)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("uuid"), SqliteTypename.of("text")));

  /**
   * JSON stored as TEXT. SQLite's JSON1 extension is built into the engine since 3.38 — use {@code
   * json()}, {@code json_extract()}, {@code ->}, {@code ->>} on these columns.
   */
  SqliteType<Json> json =
      SqliteType.of(
              "JSON",
              SqliteRead.readString.map(Json::new),
              SqliteWrite.writeString.contramap(Json::value),
              SqliteJson.json)
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  SqliteTypename.of("json"), SqliteTypename.of("text")));

  // ==================== Unknown ====================
  // For columns whose type SQLite doesn't tell us about (computed expressions, empty result set
  // metadata) — falls back to TEXT and a marker type so callers can wrap intentionally.
  SqliteType<dev.typr.foundations.data.Unknown> unknown =
      SqliteType.of("TEXT", SqliteRead.readString, SqliteWrite.writeString, SqliteJson.text)
          .transform(
              dev.typr.foundations.data.Unknown::new,
              dev.typr.foundations.data.Unknown::value);

  // ==================== Enums (text-backed) ====================

  /**
   * Enum column stored as TEXT. SQLite has no native enum type — pair this with a {@code CHECK
   * (col IN ('a', 'b', 'c'))} constraint in DDL for static enforcement.
   */
  static <E extends Enum<E>> SqliteType<E> ofEnum(Function<String, E> fromString) {
    return ofEnumImpl(fromString, Enum::name);
  }

  static <E extends Enum<E>> SqliteType<E> ofEnum(E[] values) {
    return ofEnumImpl(enumFromString(values, Enum::name), Enum::name);
  }

  static <E> SqliteType<E> ofEnum(E[] values, Function<E, String> name) {
    return ofEnumImpl(enumFromString(values, name), name);
  }

  private static <E> SqliteType<E> ofEnumImpl(
      Function<String, E> fromString, Function<E, String> name) {
    return SqliteType.of(
            "TEXT",
            SqliteRead.readString.map(fromString::apply),
            SqliteWrite.writeString.contramap(name::apply),
            new SqliteJson<>() {
              @Override
              public JsonValue toJson(E value) {
                return new JsonValue.JString(name.apply(value));
              }

              @Override
              public E fromJson(JsonValue jsonValue) {
                if (jsonValue instanceof JsonValue.JString(String v)) return fromString.apply(v);
                throw new IllegalArgumentException(
                    "Expected string for enum, got: " + jsonValue.getClass().getSimpleName());
              }
            })
        .withAnalysis(text.analysisOptions());
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
}
