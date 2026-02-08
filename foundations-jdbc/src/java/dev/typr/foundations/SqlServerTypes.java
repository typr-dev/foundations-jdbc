package dev.typr.foundations;

import java.math.BigDecimal;
import java.time.*;
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
          SqlServerText.textShort.contramap(dev.typr.foundations.data.Uint1::value),
          SqlServerJson.int2.bimap(dev.typr.foundations.data.Uint1::new, u -> (short) u.value()),
          SqlServerOutParam.readShort.map(dev.typr.foundations.data.Uint1::new));

  SqlServerType<Short> smallint =
      SqlServerType.of(
          "SMALLINT",
          SqlServerRead.readShort,
          SqlServerWrite.writeShort,
          SqlServerText.textShort,
          SqlServerJson.int2,
          SqlServerOutParam.readShort);

  SqlServerType<Integer> int_ =
      SqlServerType.of(
          "INT",
          SqlServerRead.readInteger,
          SqlServerWrite.writeInteger,
          SqlServerText.textInteger,
          SqlServerJson.int4,
          SqlServerOutParam.readInteger);

  SqlServerType<Long> bigint =
      SqlServerType.of(
          "BIGINT",
          SqlServerRead.readLong,
          SqlServerWrite.writeLong,
          SqlServerText.textLong,
          SqlServerJson.int8,
          SqlServerOutParam.readLong);

  // ==================== Fixed-Point Types ====================

  SqlServerType<BigDecimal> decimal =
      SqlServerType.of(
          "DECIMAL",
          SqlServerRead.readBigDecimal,
          SqlServerWrite.writeBigDecimal,
          SqlServerText.textBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  SqlServerType<BigDecimal> numeric = decimal.renamed("NUMERIC");

  static SqlServerType<BigDecimal> decimal(int precision, int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DECIMAL", precision, scale),
        SqlServerRead.readBigDecimal,
        SqlServerWrite.writeBigDecimal,
        SqlServerText.textBigDecimal,
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
          SqlServerText.textBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  SqlServerType<BigDecimal> smallmoney =
      SqlServerType.of(
          "SMALLMONEY",
          SqlServerRead.readBigDecimal,
          SqlServerWrite.writeBigDecimal,
          SqlServerText.textBigDecimal,
          SqlServerJson.numeric,
          SqlServerOutParam.readBigDecimal);

  // ==================== Floating-Point Types ====================

  SqlServerType<Float> real =
      SqlServerType.of(
          "REAL",
          SqlServerRead.readFloat,
          SqlServerWrite.writeFloat,
          SqlServerText.textFloat,
          SqlServerJson.float4,
          SqlServerOutParam.readFloat);

  SqlServerType<Double> float_ =
      SqlServerType.of(
          "FLOAT",
          SqlServerRead.readDouble,
          SqlServerWrite.writeDouble,
          SqlServerText.textDouble,
          SqlServerJson.float8,
          SqlServerOutParam.readDouble);

  // ==================== Boolean Type ====================

  SqlServerType<Boolean> bit =
      SqlServerType.of(
          "BIT",
          SqlServerRead.readBoolean,
          SqlServerWrite.writeBoolean,
          SqlServerText.textBoolean,
          SqlServerJson.bool,
          SqlServerOutParam.readBoolean);

  // ==================== String Types (Non-Unicode) ====================

  SqlServerType<String> char_ =
      SqlServerType.of(
          "CHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> char_(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("CHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerText.textString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> varchar =
      SqlServerType.of(
          "VARCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> varchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("VARCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerText.textString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> varcharMax = varchar.renamed("VARCHAR(MAX)");

  SqlServerType<String> text =
      SqlServerType.of(
          "TEXT",
          SqlServerRead.readString,
          SqlServerWrite.writeText,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  // ==================== String Types (Unicode) ====================

  SqlServerType<String> nchar =
      SqlServerType.of(
          "NCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> nchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("NCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerText.textString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> nvarchar =
      SqlServerType.of(
          "NVARCHAR",
          SqlServerRead.readString,
          SqlServerWrite.writeString,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  static SqlServerType<String> nvarchar(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("NVARCHAR", length),
        SqlServerRead.readString,
        SqlServerWrite.writeString,
        SqlServerText.textString,
        SqlServerJson.text,
        SqlServerOutParam.readString);
  }

  SqlServerType<String> nvarcharMax = nvarchar.renamed("NVARCHAR(MAX)");

  SqlServerType<String> ntext =
      SqlServerType.of(
          "NTEXT",
          SqlServerRead.readString,
          SqlServerWrite.writeNText,
          SqlServerText.textString,
          SqlServerJson.text,
          SqlServerOutParam.readString);

  // ==================== Binary Types ====================

  SqlServerType<byte[]> binary =
      SqlServerType.of(
          "BINARY",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerText.textByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  static SqlServerType<byte[]> binary(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("BINARY", length),
        SqlServerRead.readByteArray,
        SqlServerWrite.writeByteArray,
        SqlServerText.textByteArray,
        SqlServerJson.bytea,
        SqlServerOutParam.readByteArray);
  }

  SqlServerType<byte[]> varbinary =
      SqlServerType.of(
          "VARBINARY",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerText.textByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  static SqlServerType<byte[]> varbinary(int length) {
    return SqlServerType.of(
        SqlServerTypename.of("VARBINARY", length),
        SqlServerRead.readByteArray,
        SqlServerWrite.writeByteArray,
        SqlServerText.textByteArray,
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
          SqlServerText.instanceToString(),
          SqlServerJson.date,
          SqlServerOutParam.readLocalDate);

  SqlServerType<LocalTime> time =
      SqlServerType.of(
          "TIME",
          SqlServerRead.readTime,
          SqlServerWrite.writeTime,
          SqlServerText.instanceToString(),
          SqlServerJson.time,
          SqlServerOutParam.readLocalTime);

  static SqlServerType<LocalTime> time(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("TIME", scale),
        SqlServerRead.readTime,
        SqlServerWrite.writeTime,
        SqlServerText.instanceToString(),
        SqlServerJson.time,
        SqlServerOutParam.readLocalTime);
  }

  // DATETIME - legacy type with 3.33ms precision
  SqlServerType<LocalDateTime> datetime =
      SqlServerType.of(
          "DATETIME",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerText.instanceToString(),
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  // SMALLDATETIME - minute precision
  SqlServerType<LocalDateTime> smalldatetime =
      SqlServerType.of(
          "SMALLDATETIME",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerText.instanceToString(),
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  // DATETIME2 - modern type with 100ns precision
  SqlServerType<LocalDateTime> datetime2 =
      SqlServerType.of(
          "DATETIME2",
          SqlServerRead.readTimestamp,
          SqlServerWrite.writeTimestamp,
          SqlServerText.instanceToString(),
          SqlServerJson.timestamp,
          SqlServerOutParam.readLocalDateTime);

  static SqlServerType<LocalDateTime> datetime2(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DATETIME2", scale),
        SqlServerRead.readTimestamp,
        SqlServerWrite.writeTimestamp,
        SqlServerText.instanceToString(),
        SqlServerJson.timestamp,
        SqlServerOutParam.readLocalDateTime);
  }

  // DATETIMEOFFSET - datetime with timezone offset (SQL Server specific!)
  SqlServerType<OffsetDateTime> datetimeoffset =
      SqlServerType.of(
          "DATETIMEOFFSET",
          SqlServerRead.readOffsetDateTime,
          SqlServerWrite.writeOffsetDateTime,
          SqlServerText.instanceToString(),
          SqlServerJson.timestamptz,
          SqlServerOutParam.readOffsetDateTime);

  static SqlServerType<OffsetDateTime> datetimeoffset(int scale) {
    return SqlServerType.of(
        SqlServerTypename.of("DATETIMEOFFSET", scale),
        SqlServerRead.readOffsetDateTime,
        SqlServerWrite.writeOffsetDateTime,
        SqlServerText.instanceToString(),
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
          SqlServerText.textUUID,
          SqlServerJson.uuid,
          SqlServerOutParam.readString.map(java.util.UUID::fromString));

  // XML
  SqlServerType<dev.typr.foundations.data.Xml> xml =
      SqlServerType.of(
          "XML",
          SqlServerRead.readXml,
          SqlServerWrite.writeXml,
          SqlServerText.textString.contramap(dev.typr.foundations.data.Xml::value),
          SqlServerJson.text.bimap(
              dev.typr.foundations.data.Xml::new, dev.typr.foundations.data.Xml::value),
          SqlServerOutParam.readXmlAsString.map(dev.typr.foundations.data.Xml::new));

  // JSON - SQL Server 2016+, stored as NVARCHAR
  SqlServerType<dev.typr.foundations.data.Json> json =
      SqlServerType.of(
          "NVARCHAR(MAX)", // JSON is stored as NVARCHAR(MAX)
          SqlServerRead.readJson,
          SqlServerWrite.writeJson,
          SqlServerText.textString.contramap(dev.typr.foundations.data.Json::value),
          SqlServerJson.text.contramap(dev.typr.foundations.data.Json::value),
          SqlServerOutParam.readString.map(dev.typr.foundations.data.Json::new));

  // VECTOR - SQL Server 2025 (stored as binary for now)
  SqlServerType<byte[]> vector =
      SqlServerType.of(
          "VECTOR",
          SqlServerRead.readVector,
          SqlServerWrite.writeVector,
          SqlServerText.textByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  // ==================== System Types ====================

  // ROWVERSION / TIMESTAMP - 8-byte binary version number
  SqlServerType<byte[]> rowversion =
      SqlServerType.of(
          "ROWVERSION",
          SqlServerRead.readByteArray,
          SqlServerWrite.writeByteArray,
          SqlServerText.textByteArray,
          SqlServerJson.bytea,
          SqlServerOutParam.readByteArray);

  SqlServerType<byte[]> timestamp = rowversion.renamed("TIMESTAMP");

  // HIERARCHYID - hierarchical data (tree structures)
  SqlServerType<dev.typr.foundations.data.HierarchyId> hierarchyid =
      SqlServerType.of(
          "HIERARCHYID",
          SqlServerRead.readHierarchyId,
          SqlServerWrite.writeHierarchyId,
          SqlServerText.textString.contramap(dev.typr.foundations.data.HierarchyId::toString),
          SqlServerJson.text.bimap(
              dev.typr.foundations.data.HierarchyId::parse,
              dev.typr.foundations.data.HierarchyId::toString),
          SqlServerOutParam.notSupported("HIERARCHYID"));

  // SQL_VARIANT - can store values of various types
  SqlServerType<Object> sqlVariant =
      SqlServerType.of(
          "SQL_VARIANT",
          SqlServerRead.readObject,
          SqlServerWrite.writeObject,
          SqlServerText.textObject,
          SqlServerJson.unknown,
          SqlServerOutParam.readSqlVariant);

  // ==================== Spatial Types ====================
  // Use JDBC driver's Geography and Geometry classes

  SqlServerType<com.microsoft.sqlserver.jdbc.Geography> geography =
      SqlServerType.of(
          "GEOGRAPHY",
          SqlServerRead.readGeography,
          SqlServerWrite.writeGeography,
          SqlServerText.textGeography,
          SqlServerJson.jsonGeography,
          SqlServerOutParam.notSupported("GEOGRAPHY"));

  SqlServerType<com.microsoft.sqlserver.jdbc.Geometry> geometry =
      SqlServerType.of(
          "GEOMETRY",
          SqlServerRead.readGeometry,
          SqlServerWrite.writeGeometry,
          SqlServerText.textGeometry,
          SqlServerJson.jsonGeometry,
          SqlServerOutParam.notSupported("GEOMETRY"));

  // ==================== Unknown Type ====================
  // For columns whose type typr doesn't know how to handle - cast to/from string
  SqlServerType<dev.typr.foundations.data.Unknown> unknown =
      SqlServerType.of(
              "VARCHAR(MAX)",
              SqlServerRead.readString,
              SqlServerWrite.writeString,
              SqlServerText.textString,
              SqlServerJson.text,
              SqlServerOutParam.readString)
          .bimap(dev.typr.foundations.data.Unknown::new, dev.typr.foundations.data.Unknown::value);
}
