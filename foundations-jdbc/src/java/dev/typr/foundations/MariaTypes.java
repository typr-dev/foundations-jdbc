package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint2;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;
import dev.typr.foundations.data.Vector;
import dev.typr.foundations.data.maria.Inet4;
import dev.typr.foundations.data.maria.Inet6;
import dev.typr.foundations.data.maria.MariaSet;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.mariadb.jdbc.type.Geometry;
import org.mariadb.jdbc.type.GeometryCollection;
import org.mariadb.jdbc.type.LineString;
import org.mariadb.jdbc.type.MultiLineString;
import org.mariadb.jdbc.type.MultiPoint;
import org.mariadb.jdbc.type.MultiPolygon;
import org.mariadb.jdbc.type.Point;
import org.mariadb.jdbc.type.Polygon;

/**
 * MariaDB type definitions for the typr-runtime-java library.
 *
 * <p>This interface provides type codecs for all MariaDB data types, similar to PgTypes for
 * PostgreSQL.
 */
public interface MariaTypes {
  // ==================== Integer Types (Signed) ====================

  MariaType<Byte> tinyint =
      MariaType.of(
          "TINYINT",
          MariaRead.readByte,
          MariaWrite.writeByte,
          MariaJson.int4.transform(Integer::byteValue, Byte::intValue),
          MariaOutParam.readByte);

  MariaType<Short> smallint =
      MariaType.of(
          "SMALLINT",
          MariaRead.readShort,
          MariaWrite.writeShort,
          MariaJson.int2,
          MariaOutParam.readShort);

  MariaType<Integer> mediumint =
      MariaType.of(
          "MEDIUMINT",
          MariaRead.readInteger,
          MariaWrite.writeInteger,
          MariaJson.int4,
          MariaOutParam.readInteger);

  MariaType<Integer> int_ =
      MariaType.of(
              "INT",
              MariaRead.readInteger,
              MariaWrite.writeInteger,
              MariaJson.int4,
              MariaOutParam.readInteger)
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("integer")));

  /** Alias for {@link #int_} — aesthetic, avoids the Java-keyword {@code _} suffix. */
  MariaType<Integer> integer = int_;

  MariaType<Long> bigint =
      MariaType.of(
          "BIGINT",
          MariaRead.readLong,
          MariaWrite.writeLong,
          MariaJson.int8,
          MariaOutParam.readLong);

  // ==================== Integer Types (Unsigned) ====================

  // TINYINT UNSIGNED: 0-255, wrapped in Uint1
  MariaType<Uint1> tinyintUnsigned =
      MariaType.of(
          "TINYINT UNSIGNED",
          MariaRead.readShort.map(Uint1::new),
          MariaWrite.writeShort.contramap(Uint1::value),
          MariaJson.int2.transform(Uint1::new, Uint1::value),
          MariaOutParam.readShort.map(Uint1::new));

  // SMALLINT UNSIGNED: 0-65535, wrapped in Uint2
  MariaType<Uint2> smallintUnsigned =
      MariaType.of(
          "SMALLINT UNSIGNED",
          MariaRead.readInteger.map(Uint2::new),
          MariaWrite.writeInteger.contramap(Uint2::value),
          MariaJson.int4.transform(Uint2::new, Uint2::value),
          MariaOutParam.readInteger.map(Uint2::new));

  // MEDIUMINT UNSIGNED: 0-16777215, wrapped in Uint4
  MariaType<Uint4> mediumintUnsigned =
      MariaType.of(
          "MEDIUMINT UNSIGNED",
          MariaRead.readLong.map(Uint4::new),
          MariaWrite.writeLong.contramap(Uint4::value),
          MariaJson.int8.transform(Uint4::new, Uint4::value),
          MariaOutParam.readLong.map(Uint4::new));

  // INT UNSIGNED: 0-4294967295, wrapped in Uint4
  MariaType<Uint4> intUnsigned =
      MariaType.of(
              "INT UNSIGNED",
              MariaRead.readLong.map(Uint4::new),
              MariaWrite.writeLong.contramap(Uint4::value),
              MariaJson.int8.transform(Uint4::new, Uint4::value),
              MariaOutParam.readLong.map(Uint4::new))
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("integer unsigned")));

  // BIGINT UNSIGNED: 0-18446744073709551615, wrapped in Uint8
  MariaType<Uint8> bigintUnsigned =
      MariaType.of(
          "BIGINT UNSIGNED",
          MariaRead.readBigInteger.map(Uint8::new),
          MariaWrite.writeBigInteger.contramap(Uint8::value),
          MariaJson.numeric.transform(
              v -> new Uint8(v.toBigInteger()), v -> new BigDecimal(v.value())),
          MariaOutParam.readBigDecimal.map(v -> new Uint8(v.toBigInteger())));

  // ==================== Fixed-Point Types ====================

  MariaType<BigDecimal> decimal =
      MariaType.of(
          "DECIMAL",
          MariaRead.readBigDecimal,
          MariaWrite.writeBigDecimal,
          MariaJson.numeric,
          MariaOutParam.readBigDecimal);

  MariaType<BigDecimal> numeric =
      decimal
          .renamed("NUMERIC")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("decimal")));

  static MariaType<BigDecimal> decimalOf(int precision, int scale) {
    return MariaType.of(
        MariaTypename.of("DECIMAL", precision, scale),
        MariaRead.readBigDecimal,
        MariaWrite.writeBigDecimal,
        MariaJson.numeric,
        MariaOutParam.readBigDecimal);
  }

  // ==================== Floating-Point Types ====================

  MariaType<Float> float_ =
      MariaType.of(
          "FLOAT",
          MariaRead.readFloat,
          MariaWrite.writeFloat,
          MariaJson.float4,
          MariaOutParam.readFloat);

  /** Alias for {@link #float_} — aesthetic, avoids the Java-keyword {@code _} suffix. 4B IEEE 754. */
  MariaType<Float> float4 = float_;

  MariaType<Double> double_ =
      MariaType.of(
          "DOUBLE",
          MariaRead.readDouble,
          MariaWrite.writeDouble,
          MariaJson.float8,
          MariaOutParam.readDouble);

  /** Alias for {@link #double_} — aesthetic, avoids the Java-keyword {@code _} suffix. 8B IEEE 754. */
  MariaType<Double> float8 = double_;

  // ==================== Boolean Type ====================

  MariaType<Boolean> bool =
      MariaType.of(
          "BOOLEAN",
          MariaRead.readBoolean,
          MariaWrite.writeBoolean,
          MariaJson.bool,
          MariaOutParam.readBoolean);

  // ==================== Bit Types ====================

  // BIT(1) as Boolean
  MariaType<Boolean> bit1 =
      MariaType.of(
          "BIT",
          MariaRead.readBitAsBoolean,
          MariaWrite.writeBoolean,
          MariaJson.bool,
          MariaOutParam.readBoolean);

  // BIT(n) as byte[]
  MariaType<byte[]> bit =
      MariaType.of(
          "BIT",
          MariaRead.readBit,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  // ==================== String Types ====================

  MariaType<String> char_ =
      MariaType.of(
          "CHAR",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  /** Alias for {@link #char_} — aesthetic, avoids the Java-keyword {@code _} suffix. */
  MariaType<String> character = char_;

  MariaType<String> varchar =
      MariaType.of(
          "VARCHAR",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  MariaType<String> tinytext =
      MariaType.of(
          "TINYTEXT",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  MariaType<String> text =
      MariaType.of(
          "TEXT",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  MariaType<String> mediumtext =
      MariaType.of(
          "MEDIUMTEXT",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  MariaType<String> longtext =
      MariaType.of(
          "LONGTEXT",
          MariaRead.readString,
          MariaWrite.writeString,
          MariaJson.text,
          MariaOutParam.readString);

  static MariaType<String> char_Of(int length) {
    return MariaType.of(
        MariaTypename.of("CHAR", length),
        MariaRead.readString,
        MariaWrite.writeString,
        MariaJson.text,
        MariaOutParam.readString);
  }

  static MariaType<String> varcharOf(int length) {
    return MariaType.of(
        MariaTypename.of("VARCHAR", length),
        MariaRead.readString,
        MariaWrite.writeString,
        MariaJson.text,
        MariaOutParam.readString);
  }

  // ==================== Binary Types ====================

  MariaType<byte[]> binary =
      MariaType.of(
          "BINARY",
          MariaRead.readByteArray,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  MariaType<byte[]> varbinary =
      MariaType.of(
          "VARBINARY",
          MariaRead.readByteArray,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  MariaType<byte[]> tinyblob =
      MariaType.of(
          "TINYBLOB",
          MariaRead.readBlob,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  MariaType<byte[]> blob =
      MariaType.of(
          "BLOB",
          MariaRead.readBlob,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  MariaType<byte[]> mediumblob =
      MariaType.of(
          "MEDIUMBLOB",
          MariaRead.readBlob,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  MariaType<byte[]> longblob =
      MariaType.of(
          "LONGBLOB",
          MariaRead.readBlob,
          MariaWrite.writeByteArray,
          MariaJson.bytea,
          MariaOutParam.readByteArray);

  static MariaType<byte[]> binaryOf(int length) {
    return MariaType.of(
        MariaTypename.of("BINARY", length),
        MariaRead.readByteArray,
        MariaWrite.writeByteArray,
        MariaJson.bytea,
        MariaOutParam.readByteArray);
  }

  static MariaType<byte[]> varbinaryOf(int length) {
    return MariaType.of(
        MariaTypename.of("VARBINARY", length),
        MariaRead.readByteArray,
        MariaWrite.writeByteArray,
        MariaJson.bytea,
        MariaOutParam.readByteArray);
  }

  // ==================== Date/Time Types ====================

  MariaType<LocalDate> date =
      MariaType.of(
          "DATE",
          MariaRead.readLocalDate,
          MariaWrite.passObjectToJdbc(),
          MariaJson.date,
          MariaOutParam.readLocalDate);

  MariaType<LocalTime> time =
      MariaType.of(
          "TIME",
          MariaRead.readLocalTime,
          MariaWrite.passObjectToJdbc(),
          MariaJson.time,
          MariaOutParam.readLocalTime);

  MariaType<LocalDateTime> datetime =
      MariaType.of(
          "DATETIME",
          MariaRead.readLocalDateTime,
          MariaWrite.passObjectToJdbc(),
          MariaJson.timestamp,
          MariaOutParam.readLocalDateTime);

  MariaType<LocalDateTime> timestamp =
      MariaType.of(
          "TIMESTAMP",
          MariaRead.readLocalDateTime,
          MariaWrite.passObjectToJdbc(),
          MariaJson.timestamp,
          MariaOutParam.readLocalDateTime);

  MariaType<Year> year =
      MariaType.of(
          "YEAR",
          MariaRead.readYear,
          MariaWrite.writeShort.contramap(y -> (short) y.getValue()),
          MariaJson.int4.transform(Year::of, Year::getValue),
          MariaOutParam.readYear);

  static MariaType<LocalTime> timeOf(int fsp) {
    return MariaType.of(
        MariaTypename.of("TIME", fsp),
        MariaRead.readLocalTime,
        MariaWrite.passObjectToJdbc(),
        MariaJson.time,
        MariaOutParam.readLocalTime);
  }

  static MariaType<LocalDateTime> datetimeOf(int fsp) {
    return MariaType.of(
        MariaTypename.of("DATETIME", fsp),
        MariaRead.readLocalDateTime,
        MariaWrite.passObjectToJdbc(),
        MariaJson.timestamp,
        MariaOutParam.readLocalDateTime);
  }

  static MariaType<LocalDateTime> timestampOf(int fsp) {
    return MariaType.of(
        MariaTypename.of("TIMESTAMP", fsp),
        MariaRead.readLocalDateTime,
        MariaWrite.passObjectToJdbc(),
        MariaJson.timestamp,
        MariaOutParam.readLocalDateTime);
  }

  // ==================== ENUM Type ====================

  /**
   * Create a MariaType for ENUM columns. MariaDB ENUMs are read/written as strings.
   *
   * @param fromString function to convert string to enum value
   * @param <E> the enum type
   * @return MariaType for the enum
   */
  static <E extends Enum<E>> MariaType<E> ofEnum(String sqlType, Function<String, E> fromString) {
    return MariaType.<E>of(
            sqlType,
            MariaRead.readString.map(fromString::apply),
            MariaWrite.writeString.contramap(Enum::name),
            MariaJson.text.transform(fromString::apply, Enum::name),
            MariaOutParam.readString.map(fromString::apply))
        .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("char")));
  }

  // ==================== SET Type ====================

  /** MariaSet wrapper for SET columns. */
  MariaType<MariaSet> set =
      MariaType.of(
              "SET",
              MariaRead.readString.map(MariaSet::fromString),
              MariaWrite.writeString.contramap(MariaSet::toCommaSeparated),
              MariaJson.text.transform(MariaSet::fromString, MariaSet::toCommaSeparated),
              MariaOutParam.readString.map(MariaSet::fromString))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("char")));

  // ==================== JSON Type ====================

  /** JSON type - reuses dev.typr.foundations.data.Json from the common types. */
  MariaType<Json> json =
      MariaType.of(
          "JSON",
          MariaRead.readString.map(Json::new),
          MariaWrite.writeString.contramap(Json::value),
          MariaJson.json,
          MariaOutParam.readString.map(Json::new));

  // ==================== Network Types ====================

  MariaType<Inet4> inet4 =
      MariaType.of(
              "INET4",
              MariaRead.readString.map(Inet4::parse),
              MariaWrite.writeString.contramap(Inet4::value),
              MariaJson.text.transform(Inet4::parse, Inet4::value),
              MariaOutParam.readString.map(Inet4::parse))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("char")));

  MariaType<Inet6> inet6 =
      MariaType.of(
              "INET6",
              MariaRead.readString.map(Inet6::parse),
              MariaWrite.writeString.contramap(Inet6::value),
              MariaJson.text.transform(Inet6::parse, Inet6::value),
              MariaOutParam.readString.map(Inet6::parse))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("char")));

  // ==================== UUID Type ====================

  MariaType<UUID> uuid =
      MariaType.of(
          "UUID",
          MariaRead.readString.map(UUID::fromString),
          MariaWrite.writeString.contramap(UUID::toString),
          MariaJson.uuid,
          MariaOutParam.readString.map(UUID::fromString));

  // ==================== Spatial Types ====================
  // Using MariaDB Connector/J types directly.
  // We use readGeometry() which handles both normal SELECT (returns typed Geometry objects)
  // and RETURNING clauses (returns WKB bytes). For base Geometry type, it parses the WKB
  // header to determine the actual geometry type and uses the appropriate codec.
  // Note: Spatial types use text representation for JSON (WKT format would be better but is
  // complex)

  MariaType<Geometry> geometry =
      MariaType.of(
          "GEOMETRY",
          MariaRead.readGeometry(Geometry.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("Geometry JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(Geometry.class));

  MariaType<Point> point =
      MariaType.of(
          "POINT",
          MariaRead.readGeometry(Point.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("Point JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(Point.class));

  MariaType<LineString> linestring =
      MariaType.of(
          "LINESTRING",
          MariaRead.readGeometry(LineString.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("LineString JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(LineString.class));

  MariaType<Polygon> polygon =
      MariaType.of(
          "POLYGON",
          MariaRead.readGeometry(Polygon.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("Polygon JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(Polygon.class));

  MariaType<MultiPoint> multipoint =
      MariaType.of(
          "MULTIPOINT",
          MariaRead.readGeometry(MultiPoint.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("MultiPoint JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(MultiPoint.class));

  MariaType<MultiLineString> multilinestring =
      MariaType.of(
          "MULTILINESTRING",
          MariaRead.readGeometry(MultiLineString.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("MultiLineString JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(MultiLineString.class));

  MariaType<MultiPolygon> multipolygon =
      MariaType.of(
          "MULTIPOLYGON",
          MariaRead.readGeometry(MultiPolygon.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("MultiPolygon JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(MultiPolygon.class));

  MariaType<GeometryCollection> geometrycollection =
      MariaType.of(
          "GEOMETRYCOLLECTION",
          MariaRead.readGeometry(GeometryCollection.class),
          MariaWrite.passObjectToJdbc(),
          MariaJson.text.transform(
              s -> {
                throw new UnsupportedOperationException("GeometryCollection JSON not supported");
              },
              Object::toString),
          MariaOutParam.readGeometry(GeometryCollection.class));

  // ==================== Vector Type ====================
  // MariaDB 11.7+ VECTOR type. The connector's FloatArrayCodec handles float[] natively.

  static MariaType<Vector> vector(int dimension) {
    return MariaType.of(
            MariaTypename.of("VECTOR", dimension),
            MariaRead.readVector,
            MariaWrite.writeVector,
            MariaJson.vector,
            MariaOutParam.readVector)
        .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(MariaTypename.of("varbinary")));
  }

  // ==================== Unknown Type ====================
  // For columns whose type typr doesn't know how to handle - cast to/from string
  MariaType<dev.typr.foundations.data.Unknown> unknown =
      MariaType.of(
              "TEXT",
              MariaRead.readString,
              MariaWrite.writeString,
              MariaJson.text,
              MariaOutParam.readString)
          .transform(
              dev.typr.foundations.data.Unknown::new, dev.typr.foundations.data.Unknown::value);

  // ==================== JSON-Encoded Row Types ====================

  /** A JSON column type that stores a single row as a positional JSON array. */
  static <Row> MariaType<Row> jsonArrayEncoded(RowCodec<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonArray(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /** A JSON column type that stores a list of rows, each as a positional JSON array. */
  static <Row> MariaType<List<Row>> jsonArrayEncodedList(RowCodec<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonArray(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }

  /** A JSON column type that stores a single row as a keyed JSON object. */
  static <Row> MariaType<Row> jsonObjectEncoded(RowCodecNamed<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonObject(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> MariaType<List<Row>> jsonObjectEncodedList(RowCodecNamed<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonObject(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }
}
