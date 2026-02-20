package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;

/**
 * SQL Server type definitions for the typr-runtime-java library.
 *
 * <p>This interface provides type codecs for all SQL Server data types.
 *
 * <p>Key differences from other databases: - TINYINT is UNSIGNED (0-255), mapped to Short - Unicode
 * types (NCHAR, NVARCHAR, NTEXT) are separate from non-Unicode - DATETIMEOFFSET for timezone-aware
 * timestamps - UNIQUEIDENTIFIER for UUIDs/GUIDs - No native array support (use table-valued
 * parameters instead)
 */
public interface SqlServerTypes {

  // ==================== Integer Types ====================

  // TINYINT - UNSIGNED in SQL Server! (0-255)
  SqlServerType<dev.typr.foundations.data.Uint1> tinyint =
      SqlServerType.of(
          "TINYINT",
          SqlServerRead.readUint1,
          SqlServerWrite.writeUint1,
          SqlServerJson.int2.transform(dev.typr.foundations.data.Uint1::new, u -> (short) u.value()),
          SqlServerOutParam.readShort.map(dev.typr.foundations.data.Uint1::new));

  SqlServerType<Short> smallint =
      SqlServerType.of(
          "SMALLINT",
          SqlServerRead.readShort,
          SqlServerWrite.writeShort,
          SqlServerJson.int2,
          SqlServerOutParam.readShort);

  SqlServerType<Integer> int_ =
      SqlServerType.of(
          "INT",
          SqlServerRead.readInteger,
          SqlServerWrite.writeInteger,
          SqlServerJson.int4,
          SqlServerOutParam.readInteger);

  SqlServerType<Long> bigint =
      SqlServerType.of(
          "BIGINT",
          SqlServerRead.readLong,
          SqlServerWrite.writeLong,
          SqlServerJson.int8,
          SqlServerOutParam.readLong);

  // ==================== Fixed-Point Types ====================

  SqlServerType<BigDecimal> decimal =
      SqlServerType.of(
          "DECIMAL",
          SqlServerRead.readBigDecimal,
          SqlServerWrite.writeBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  SqlServerType<BigDecimal> numeric = decimal.renamed("NUMERIC");

  static SqlServerType<BigDecimal> decimal(int precision, int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DECIMAL", precision, scale),
        SqlServerRead.readBigDecimal,
        SqlServerWrite.writeBigDecimal,
        SqlServerJson.numeric,
        SqlServerOutParam.readBigDecimal);
  }

  static SqlServerType<BigDecimal> numeric(int precision, int scale) {
    return decimal(precision, scale).renamed("NUMERIC");
  }

  SqlServerType<BigDecimal> money =
      SqlServerType.of(
          "MONEY",
          SqlServerRead.readBigDecimal,
          SqlServerWrite.writeBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  SqlServerType<BigDecimal> smallmoney =
      SqlServerType.of(
          "SMALLMONEY",
          SqlServerRead.readBigDecimal,
          SqlServerWrite.writeBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  // ==================== Floating-Point Types ====================

  SqlServerType<Float> real =
      SqlServerType.of(
          "REAL",
          SqlServerRead.readFloat,
          SqlServerWrite.writeFloat,
          SqlServerJson.float4,
          SqlServerOutParam.readFloat);

  SqlServerType<Double> float_ =
      SqlServerType.of(
          "FLOAT",
          SqlServerRead.readDouble,
          SqlServerWrite.writeDouble,
          SqlServerJson.float8,
          SqlServerOutParam.readDouble);

  // ==================== Boolean Type ====================

  SqlServerType<Boolean> bit =
      SqlServerType.of(
          "BIT",
          SqlServerRead.readBoolean,
          SqlServerWrite.writeBoolean,
          SqlServerJson.bool,
          SqlServerOutParam.readBoolean);

  // ==================== String Types (Non-Unicode) ====================

  SqlServerType<String> char_ =
      SqlServerType.of(
          "CHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> char_(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("CHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> varchar =
      SqlServerType.of(
          "VARCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> varchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("VARCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> varcharMax = varchar.renamed("VARCHAR(MAX)");

  SqlServerType<String> text =
      SqlServerType.of(
          "TEXT",
          SqlServerRead.readString,
          SqlServerWrite.writeText,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  // ==================== String Types (Unicode) ====================

  SqlServerType<String> nchar =
      SqlServerType.of(
          "NCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> nchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("NCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> nvarchar =
      SqlServerType.of(
          "NVARCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> nvarchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("NVARCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> nvarcharMax = nvarchar.renamed("NVARCHAR(MAX)");

  SqlServerType<String> ntext =
      SqlServerType.of(
          "NTEXT",
          SqlServerRead.readString,
          SqlServerWrite.writeNText,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  // ==================== Binary Types ====================

  SqlServerType<byte[]> binary =
      SqlServerType.of(
          "BINARY",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  static SqlServerType<byte[]> binary(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("BINARY", length),
        SqlServerRead.readByteArray,
        SqlServerWrite.writeByteArray,
        SqlServerJson.bytea,
        SqlServerOutParam.readByteArray);
  }

  SqlServerType<byte[]> varbinary =
      SqlServerType.of(
          "VARBINARY",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  static SqlServerType<byte[]> varbinary(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("VARBINARY", length),
        SqlServerRead.readByteArray,
        SqlServerWrite.writeByteArray,
        SqlServerJson.bytea,
        SqlServerOutParam.readByteArray);
  }

  SqlServerType<byte[]> varbinaryMax = varbinary.renamed("VARBINARY(MAX)");
  SqlServerType<byte[]> image = varbinary.renamed("IMAGE");

  // ==================== Date/Time Types ====================

  SqlServerType<LocalDate> date =
      SqlServerType.of(
          "DATE",
          SqlServerRead.readDate,
          SqlServerWrite.writeDate,
          SqlServerJson.date,
          SqlServerOutParam.readLocalDate);

  SqlServerType<LocalTime> time =
      SqlServerType.of(
          "TIME",
          SqlServerRead.readTime,
          SqlServerWrite.writeTime,
          SqlServerJson.time,
          SqlServerOutParam.readLocalTime);

  static SqlServerType<LocalTime> time(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("TIME", scale),
        SqlServerRead.readTime,
        SqlServerWrite.writeTime,
        SqlServerJson.time,
        SqlServerOutParam.readLocalTime);
  }

  // DATETIME - legacy type with 3.33ms precision
  SqlServerType<LocalDateTime> datetime =
      SqlServerType.of(
          "DATETIME",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  // SMALLDATETIME - minute precision
  SqlServerType<LocalDateTime> smalldatetime =
      SqlServerType.of(
          "SMALLDATETIME",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  // DATETIME2 - modern type with 100ns precision
  SqlServerType<LocalDateTime> datetime2 =
      SqlServerType.of(
          "DATETIME2",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  static SqlServerType<LocalDateTime> datetime2(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DATETIME2", scale),
        SqlServerRead.readTimestamp,
        SqlServerWrite.writeTimestamp,
        SqlServerJson.timestamp,
        SqlServerOutParam.readLocalDateTime);
  }

  // DATETIMEOFFSET - datetime with timezone offset (SQL Server specific!)
  SqlServerType<OffsetDateTime> datetimeoffset =
      SqlServerType.of(
          "DATETIMEOFFSET",
          SqlServerRead.readOffsetDateTime,
          SqlServerWrite.writeOffsetDateTime,
          SqlServerJson.timestamptz,
          SqlServerOutParam.readOffsetDateTime);

  static SqlServerType<OffsetDateTime> datetimeoffset(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DATETIMEOFFSET", scale),
        SqlServerRead.readOffsetDateTime,
        SqlServerWrite.writeOffsetDateTime,
        SqlServerJson.timestamptz,
        SqlServerOutParam.readOffsetDateTime);
  }

  // ==================== Special Types ====================

  // UNIQUEIDENTIFIER (UUID/GUID)
  SqlServerType<UUID> uniqueidentifier =
      SqlServerType.of(
          "UNIQUEIDENTIFIER",
          SqlServerRead.readUUID,
          SqlServerWrite.writeUUID,
          SqlServerJson.uuid,
          SqlServerOutParam.readString.map(java.util.UUID::fromString));

  // XML
  SqlServerType<dev.typr.foundations.data.Xml> xml =
      SqlServerType.of(
          "XML",
          SqlServerRead.readXml,
          SqlServerWrite.writeXml,
          SqlServerJson.text.transform(
              dev.typr.foundations.data.Xml::new, dev.typr.foundations.data.Xml::value),
          SqlServerOutParam.readXmlAsString.map(dev.typr.foundations.data.Xml::new));

  // JSON - SQL Server 2016+, stored as NVARCHAR
  SqlServerType<dev.typr.foundations.data.Json> json =
      SqlServerType.of(
          "NVARCHAR(MAX)", // JSON is stored as NVARCHAR(MAX)
          SqlServerRead.readJson,
          SqlServerWrite.writeJson,
          SqlServerJson.text.transform(dev.typr.foundations.data.Json::new, dev.typr.foundations.data.Json::value),
          SqlServerOutParam.readString.map(dev.typr.foundations.data.Json::new));

  // VECTOR - SQL Server 2025 (stored as binary for now)
  SqlServerType<byte[]> vector =
      SqlServerType.of(
          "VECTOR",
          SqlServerRead.readVector,
          SqlServerWrite.writeVector,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  // ==================== System Types ====================

  // ROWVERSION / TIMESTAMP - 8-byte binary version number
  SqlServerType<byte[]> rowversion =
      SqlServerType.of(
          "ROWVERSION",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  SqlServerType<byte[]> timestamp = rowversion.renamed("TIMESTAMP");

  // HIERARCHYID - hierarchical data (tree structures)
  SqlServerType<dev.typr.foundations.data.HierarchyId> hierarchyid =
      SqlServerType.of(
          "HIERARCHYID",
          SqlServerRead.readHierarchyId,
          SqlServerWrite.writeHierarchyId,
          SqlServerJson.text.transform(
              dev.typr.foundations.data.HierarchyId::parse,
              dev.typr.foundations.data.HierarchyId::toString),
          SqlServerOutParam.notSupported("HIERARCHYID"));

  // SQL_VARIANT - can store values of various types
  SqlServerType<Object> sqlVariant =
      SqlServerType.of(
          "SQL_VARIANT",
          SqlServerRead.readObject,
          SqlServerWrite.writeObject,
          SqlServerJson.unknown,
          SqlServerOutParam.readSqlVariant);

  // ==================== Spatial Types ====================
  // Use JDBC driver's Geography and Geometry classes

  SqlServerType<com.microsoft.sqlserver.jdbc.Geography> geography =
      SqlServerType.of(
          "GEOGRAPHY",
          SqlServerRead.readGeography,
          SqlServerWrite.writeGeography,
          SqlServerJson.jsonGeography,
          SqlServerOutParam.notSupported("GEOGRAPHY"));

  SqlServerType<com.microsoft.sqlserver.jdbc.Geometry> geometry =
      SqlServerType.of(
          "GEOMETRY",
          SqlServerRead.readGeometry,
          SqlServerWrite.writeGeometry,
          SqlServerJson.jsonGeometry,
          SqlServerOutParam.notSupported("GEOMETRY"));

  // ==================== Unknown Type ====================
  // For columns whose type typr doesn't know how to handle - cast to/from string
  SqlServerType<dev.typr.foundations.data.Unknown> unknown =
      SqlServerType.of(
              "VARCHAR(MAX)",
              SqlServerRead.readString,
              SqlServerWrite.writeString,
              SqlServerJson.text,
              SqlServerOutParam.readString)
          .transform(dev.typr.foundations.data.Unknown::new, dev.typr.foundations.data.Unknown::value);

  // ==================== JSON-Encoded Row Types ====================

  /** A JSON column type that stores a single row as a positional JSON array. */
  static <Row> SqlServerType<Row> jsonArrayEncoded(RowCodec<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonArray(parser);
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Json(codec.toJson(row).encode()));
  }

  /** A JSON column type that stores a list of rows, each as a positional JSON array. */
  static <Row> SqlServerType<List<Row>> jsonArrayEncodedList(RowCodec<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonArray(parser).list();
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Json(codec.toJson(list).encode()));
  }

  /** A JSON column type that stores a single row as a keyed JSON object. */
  static <Row> SqlServerType<Row> jsonObjectEncoded(RowCodecNamed<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonObject(parser);
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Json(codec.toJson(row).encode()));
  }

  /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> SqlServerType<List<Row>> jsonObjectEncodedList(RowCodecNamed<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonObject(parser).list();
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Json(codec.toJson(list).encode()));
  }
}
