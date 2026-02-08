package dev.typr.foundations;

import java.math.BigDecimal;
import java.time.*;

/**
 * DB2 type definitions for the typr-runtime-java library.
 *
 * <p>This interface provides type codecs for all IBM DB2 data types.
 *
 * <p>Key differences from other databases:
 *
 * <ul>
 *   <li>DECFLOAT - DB2-specific decimal floating point (16 or 34 digits)
 *   <li>GRAPHIC/VARGRAPHIC/DBCLOB - Double-byte character set types
 *   <li>ROWID - DB2 row identifier
 *   <li>XML - Native XML support
 *   <li>BOOLEAN - Native since DB2 11.1
 * </ul>
 */
public interface Db2Types {

  // ==================== Integer Types ====================

  Db2Type<Short> smallint =
      Db2Type.of(
          "SMALLINT",
          Db2Read.readShort,
          Db2Write.writeShort,
          Db2Json.int2,
          Db2OutParam.readShort);

  Db2Type<Integer> integer =
      Db2Type.of(
          "INTEGER",
          Db2Read.readInteger,
          Db2Write.writeInteger,
          Db2Json.int4,
          Db2OutParam.readInteger);

  Db2Type<Integer> int_ = integer.renamed("INT");

  Db2Type<Long> bigint =
      Db2Type.of(
          "BIGINT",
          Db2Read.readLong,
          Db2Write.writeLong,
          Db2Json.int8,
          Db2OutParam.readLong);

  // ==================== Fixed-Point Types ====================

  Db2Type<BigDecimal> decimal =
      Db2Type.of(
          "DECIMAL",
          Db2Read.readBigDecimal,
          Db2Write.writeBigDecimal,
          Db2Json.numeric,
          Db2OutParam.readBigDecimal);

  Db2Type<BigDecimal> numeric = decimal.renamed("NUMERIC");

  Db2Type<BigDecimal> dec = decimal.renamed("DEC");

  static Db2Type<BigDecimal> decimal(int precision, int scale) {
    return Db2Type.of(
        Db2Typename.of("DECIMAL", precision, scale),
        Db2Read.readBigDecimal,
        Db2Write.writeBigDecimal,
        Db2Json.numeric,
        Db2OutParam.readBigDecimal);
  }

  static Db2Type<BigDecimal> numeric(int precision, int scale) {
    return decimal(precision, scale).renamed("NUMERIC");
  }

  // DECFLOAT - DB2-specific decimal floating point
  Db2Type<BigDecimal> decfloat =
      Db2Type.of(
          "DECFLOAT",
          Db2Read.readDecFloat,
          Db2Write.writeDecFloat,
          Db2Json.numeric,
          Db2OutParam.readBigDecimal);

  static Db2Type<BigDecimal> decfloat(int precision) {
    return Db2Type.of(
        Db2Typename.of("DECFLOAT", precision),
        Db2Read.readDecFloat,
        Db2Write.writeDecFloat,
        Db2Json.numeric,
        Db2OutParam.readBigDecimal);
  }

  // ==================== Floating-Point Types ====================

  Db2Type<Float> real =
      Db2Type.of(
          "REAL",
          Db2Read.readFloat,
          Db2Write.writeFloat,
          Db2Json.float4,
          Db2OutParam.readFloat);

  Db2Type<Double> double_ =
      Db2Type.of(
          "DOUBLE",
          Db2Read.readDouble,
          Db2Write.writeDouble,
          Db2Json.float8,
          Db2OutParam.readDouble);

  Db2Type<Double> float_ = double_.renamed("FLOAT");

  // ==================== Boolean Type ====================

  // Native BOOLEAN support since DB2 11.1
  Db2Type<Boolean> boolean_ =
      Db2Type.of(
          "BOOLEAN",
          Db2Read.readBoolean,
          Db2Write.writeBoolean,
          Db2Json.bool,
          Db2OutParam.readBoolean);

  // ==================== String Types (SBCS - Single-Byte) ====================

  Db2Type<String> char_ =
      Db2Type.of(
          "CHAR",
          Db2Read.readString,
          Db2Write.writeString,
          Db2Json.text,
          Db2OutParam.readString);

  Db2Type<String> character = char_.renamed("CHARACTER");

  static Db2Type<String> char_(int length) {
    return Db2Type.of(
        Db2Typename.of("CHAR", length),
        Db2Read.readString,
        Db2Write.writeString,
        Db2Json.text,
        Db2OutParam.readString);
  }

  Db2Type<String> varchar =
      Db2Type.of(
          "VARCHAR",
          Db2Read.readString,
          Db2Write.writeString,
          Db2Json.text,
          Db2OutParam.readString);

  static Db2Type<String> varchar(int length) {
    return Db2Type.of(
        Db2Typename.of("VARCHAR", length),
        Db2Read.readString,
        Db2Write.writeString,
        Db2Json.text,
        Db2OutParam.readString);
  }

  // CLOB - Character Large Object
  Db2Type<String> clob =
      Db2Type.of(
          "CLOB",
          Db2Read.readClob,
          Db2Write.writeClob,
          Db2Json.text,
          Db2OutParam.readString);

  static Db2Type<String> clob(int length) {
    return Db2Type.of(
        Db2Typename.of("CLOB", length),
        Db2Read.readClob,
        Db2Write.writeClob,
        Db2Json.text,
        Db2OutParam.readString);
  }

  // ==================== String Types (DBCS - Double-Byte) ====================
  // Note: DB2's JSON_OBJECT does not support GRAPHIC/VARGRAPHIC/DBCLOB types (SQLCODE=-171)

  // GRAPHIC - Fixed-length double-byte character string
  Db2Type<String> graphic =
      Db2Type.of(
          "GRAPHIC",
          Db2Read.readGraphic,
          Db2Write.writeGraphic,
          Db2Json.unsupported("GRAPHIC"),
          Db2OutParam.readString);

  static Db2Type<String> graphic(int length) {
    return Db2Type.of(
        Db2Typename.of("GRAPHIC", length),
        Db2Read.readGraphic,
        Db2Write.writeGraphic,
        Db2Json.unsupported("GRAPHIC"),
        Db2OutParam.readString);
  }

  // VARGRAPHIC - Variable-length double-byte character string
  Db2Type<String> vargraphic =
      Db2Type.of(
          "VARGRAPHIC",
          Db2Read.readVarGraphic,
          Db2Write.writeVarGraphic,
          Db2Json.unsupported("VARGRAPHIC"),
          Db2OutParam.readString);

  static Db2Type<String> vargraphic(int length) {
    return Db2Type.of(
        Db2Typename.of("VARGRAPHIC", length),
        Db2Read.readVarGraphic,
        Db2Write.writeVarGraphic,
        Db2Json.unsupported("VARGRAPHIC"),
        Db2OutParam.readString);
  }

  // DBCLOB - Double-byte Character Large Object
  Db2Type<String> dbclob =
      Db2Type.of(
          "DBCLOB",
          Db2Read.readDbClob,
          Db2Write.writeDbClob,
          Db2Json.unsupported("DBCLOB"),
          Db2OutParam.readString);

  static Db2Type<String> dbclob(int length) {
    return Db2Type.of(
        Db2Typename.of("DBCLOB", length),
        Db2Read.readDbClob,
        Db2Write.writeDbClob,
        Db2Json.unsupported("DBCLOB"),
        Db2OutParam.readString);
  }

  // ==================== Binary Types ====================
  // Note: DB2's JSON_OBJECT does not support BINARY/VARBINARY/BLOB types (SQLCODE=-171 or -16402)

  Db2Type<byte[]> binary =
      Db2Type.of(
          "BINARY",
          Db2Read.readByteArray,
          Db2Write.writeByteArray,
          Db2Json.unsupported("BINARY"),
          Db2OutParam.readByteArray);

  static Db2Type<byte[]> binary(int length) {
    return Db2Type.of(
        Db2Typename.of("BINARY", length),
        Db2Read.readByteArray,
        Db2Write.writeByteArray,
        Db2Json.unsupported("BINARY"),
        Db2OutParam.readByteArray);
  }

  Db2Type<byte[]> varbinary =
      Db2Type.of(
          "VARBINARY",
          Db2Read.readByteArray,
          Db2Write.writeByteArray,
          Db2Json.unsupported("VARBINARY"),
          Db2OutParam.readByteArray);

  static Db2Type<byte[]> varbinary(int length) {
    return Db2Type.of(
        Db2Typename.of("VARBINARY", length),
        Db2Read.readByteArray,
        Db2Write.writeByteArray,
        Db2Json.unsupported("VARBINARY"),
        Db2OutParam.readByteArray);
  }

  // BLOB - Binary Large Object
  Db2Type<byte[]> blob =
      Db2Type.of(
          "BLOB",
          Db2Read.readBlob,
          Db2Write.writeBlob,
          Db2Json.unsupported("BLOB"),
          Db2OutParam.readByteArray);

  static Db2Type<byte[]> blob(int length) {
    return Db2Type.of(
        Db2Typename.of("BLOB", length),
        Db2Read.readBlob,
        Db2Write.writeBlob,
        Db2Json.unsupported("BLOB"),
        Db2OutParam.readByteArray);
  }

  // ==================== Date/Time Types ====================

  Db2Type<LocalDate> date =
      Db2Type.of(
          "DATE",
          Db2Read.readDate,
          Db2Write.writeDate,
          Db2Json.date,
          Db2OutParam.readLocalDate);

  Db2Type<LocalTime> time =
      Db2Type.of(
          "TIME",
          Db2Read.readTime,
          Db2Write.writeTime,
          Db2Json.time,
          Db2OutParam.readLocalTime);

  // TIMESTAMP without time zone
  Db2Type<LocalDateTime> timestamp =
      Db2Type.of(
          "TIMESTAMP",
          Db2Read.readTimestamp,
          Db2Write.writeTimestamp,
          Db2Json.timestamp,
          Db2OutParam.readLocalDateTime);

  static Db2Type<LocalDateTime> timestamp(int scale) {
    return Db2Type.of(
        Db2Typename.of("TIMESTAMP", scale),
        Db2Read.readTimestamp,
        Db2Write.writeTimestamp,
        Db2Json.timestamp,
        Db2OutParam.readLocalDateTime);
  }

  // ==================== Special Types ====================
  // Note: DB2's JSON_OBJECT does not support XML type (SQLCODE=-171)

  // XML - Native XML support
  Db2Type<dev.typr.foundations.data.Xml> xml =
      Db2Type.of(
          "XML",
          Db2Read.readXml,
          Db2Write.writeXml,
          Db2Json.unsupported("XML"),
          Db2OutParam.readXmlAsString.map(dev.typr.foundations.data.Xml::new));

  // ROWID - DB2 row identifier
  Db2Type<byte[]> rowid =
      Db2Type.of(
          "ROWID",
          Db2Read.readRowId,
          Db2Write.writeRowId,
          Db2Json.bytea,
          Db2OutParam.readRowId);

  // Generic object for unknown types
  Db2Type<Object> object =
      Db2Type.of(
          "OBJECT",
          Db2Read.readObject,
          Db2Write.writeObject,
          Db2Json.unknown,
          Db2OutParam.readObject);

  // ==================== Unknown Type ====================
  // For columns whose type typr doesn't know how to handle - cast to/from string
  Db2Type<dev.typr.foundations.data.Unknown> unknown =
      Db2Type.of(
              "VARCHAR(32672)",
              Db2Read.readString,
              Db2Write.writeString,
              Db2Json.text,
              Db2OutParam.readString)
          .bimap(dev.typr.foundations.data.Unknown::new, dev.typr.foundations.data.Unknown::value);
}
