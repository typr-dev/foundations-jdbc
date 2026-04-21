package dev.typr.foundations.pg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

/**
 * Low-level PostgreSQL wire protocol v3 encoding/decoding. Zero external dependencies — uses only
 * java.net.Socket and java.io streams.
 *
 * <p>Thread safety: write methods are NOT thread-safe. Read methods are NOT thread-safe. Callers
 * must ensure single-writer and single-reader.
 */
final class PgProtocol {

  private static final byte[] EMPTY = new byte[0];

  private final DataInputStream in;
  private final OutputStream out;

  PgProtocol(InputStream inputStream, OutputStream outputStream) {
    this.in = new DataInputStream(new BufferedInputStream(inputStream, 65536));
    this.out = new BufferedOutputStream(outputStream, 65536);
  }

  // ========== SSL negotiation (called on raw socket streams before wrapping) ==========

  static void writeSSLRequest(OutputStream rawOut) throws IOException {
    // SSLRequest: 8 bytes total — int32 length (8), int32 SSL code (80877103)
    rawOut.write(new byte[] {0, 0, 0, 8, 4, (byte) 210, 22, 47});
    rawOut.flush();
  }

  static boolean readSSLResponse(InputStream rawIn) throws IOException {
    int response = rawIn.read();
    if (response == -1) throw new IOException("Connection closed during SSL negotiation");
    return response == 'S';
  }

  // ========== Frontend (client → server) message writing ==========

  void writeStartup(String user, String database, String applicationName) throws IOException {
    var buf = new ByteArrayOutputStream(128);
    writeInt4(buf, 0); // placeholder for length
    writeInt4(buf, 196608); // protocol version 3.0
    writeCString(buf, "user");
    writeCString(buf, user);
    writeCString(buf, "database");
    writeCString(buf, database);
    writeCString(buf, "client_encoding");
    writeCString(buf, "UTF8");
    if (applicationName != null && !applicationName.isEmpty()) {
      writeCString(buf, "application_name");
      writeCString(buf, applicationName);
    }
    buf.write(0); // terminator

    byte[] data = buf.toByteArray();
    putInt4(data, 0, data.length);
    out.write(data);
  }

  void writePassword(String password) throws IOException {
    byte[] pwBytes = password.getBytes(StandardCharsets.UTF_8);
    out.write('p');
    writeInt4(out, 4 + pwBytes.length + 1);
    out.write(pwBytes);
    out.write(0);
  }

  void writeSASLInitialResponse(String mechanism, byte[] clientFirstMessage) throws IOException {
    byte[] mechBytes = mechanism.getBytes(StandardCharsets.UTF_8);
    out.write('p');
    writeInt4(out, 4 + mechBytes.length + 1 + 4 + clientFirstMessage.length);
    out.write(mechBytes);
    out.write(0);
    writeInt4(out, clientFirstMessage.length);
    out.write(clientFirstMessage);
  }

  void writeSASLResponse(byte[] clientFinalMessage) throws IOException {
    out.write('p');
    writeInt4(out, 4 + clientFinalMessage.length);
    out.write(clientFinalMessage);
  }

  void writeParse(String stmtName, String sql) throws IOException {
    byte[] nameBytes = stmtName.getBytes(StandardCharsets.UTF_8);
    byte[] sqlBytes = sql.getBytes(StandardCharsets.UTF_8);
    out.write('P');
    writeInt4(out, 4 + nameBytes.length + 1 + sqlBytes.length + 1 + 2);
    out.write(nameBytes);
    out.write(0);
    out.write(sqlBytes);
    out.write(0);
    writeInt2(out, 0); // no parameter OIDs — let server infer
  }

  void writeBind(String portal, String stmtName, byte[][] paramValues, short[] resultFormats)
      throws IOException {
    byte[] portalBytes = portal.getBytes(StandardCharsets.UTF_8);
    byte[] stmtBytes = stmtName.getBytes(StandardCharsets.UTF_8);

    int paramCount = paramValues != null ? paramValues.length : 0;
    int resFmtCount = resultFormats != null ? resultFormats.length : 0;

    // Calculate total length
    int len = 4;
    len += portalBytes.length + 1; // portal name
    len += stmtBytes.length + 1; // statement name
    len += 2; // format code count
    len += 2; // parameter count
    for (int i = 0; i < paramCount; i++) {
      len += 4; // length (or -1)
      if (paramValues[i] != null) {
        len += paramValues[i].length;
      }
    }
    len += 2 + resFmtCount * 2; // result format code count + codes

    out.write('B');
    writeInt4(out, len);
    out.write(portalBytes);
    out.write(0);
    out.write(stmtBytes);
    out.write(0);
    writeInt2(out, 0); // all text format for params
    writeInt2(out, paramCount);
    for (int i = 0; i < paramCount; i++) {
      if (paramValues[i] == null) {
        writeInt4(out, -1); // NULL
      } else {
        writeInt4(out, paramValues[i].length);
        out.write(paramValues[i]);
      }
    }
    writeInt2(out, resFmtCount);
    for (int i = 0; i < resFmtCount; i++) {
      writeInt2(out, resultFormats[i]);
    }
  }

  void writeDescribe(char type, String name) throws IOException {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    out.write('D');
    writeInt4(out, 4 + 1 + nameBytes.length + 1);
    out.write(type);
    out.write(nameBytes);
    out.write(0);
  }

  void writeExecute(String portal, int maxRows) throws IOException {
    byte[] portalBytes = portal.getBytes(StandardCharsets.UTF_8);
    out.write('E');
    writeInt4(out, 4 + portalBytes.length + 1 + 4);
    out.write(portalBytes);
    out.write(0);
    writeInt4(out, maxRows);
  }

  // Execute("", 0) is always the same 10 bytes — pre-compute for batch writes
  private static final byte[] EXECUTE_UNNAMED = {'E', 0, 0, 0, 9, 0, 0, 0, 0, 0};

  void writeBatchBindExecute(
      byte[] stmtNameBytes, List<byte[][]> allParamValues, short[] resultFormats)
      throws IOException {
    int resFmtCount = resultFormats != null ? resultFormats.length : 0;
    int bindFixedLen = 4 + 1 + stmtNameBytes.length + 1 + 2 + 2 + 2 + resFmtCount * 2;

    for (byte[][] paramValues : allParamValues) {
      int paramCount = paramValues != null ? paramValues.length : 0;
      int bindLen = bindFixedLen;
      for (int i = 0; i < paramCount; i++) {
        bindLen += 4;
        if (paramValues[i] != null) bindLen += paramValues[i].length;
      }

      out.write('B');
      writeInt4(out, bindLen);
      out.write(0); // empty portal name
      out.write(stmtNameBytes);
      out.write(0);
      writeInt2(out, 0); // text format for params
      writeInt2(out, paramCount);
      for (int i = 0; i < paramCount; i++) {
        if (paramValues[i] == null) {
          writeInt4(out, -1);
        } else {
          writeInt4(out, paramValues[i].length);
          out.write(paramValues[i]);
        }
      }
      writeInt2(out, resFmtCount);
      for (int i = 0; i < resFmtCount; i++) {
        writeInt2(out, resultFormats[i]);
      }

      out.write(EXECUTE_UNNAMED);
    }
  }

  void writeSync() throws IOException {
    out.write('S');
    writeInt4(out, 4);
  }

  void writeFlush() throws IOException {
    out.write('H');
    writeInt4(out, 4);
  }

  void writeSimpleQuery(String sql) throws IOException {
    byte[] sqlBytes = sql.getBytes(StandardCharsets.UTF_8);
    out.write('Q');
    writeInt4(out, 4 + sqlBytes.length + 1);
    out.write(sqlBytes);
    out.write(0);
  }

  void writeCopyData(byte[] data) throws IOException {
    out.write('d');
    writeInt4(out, 4 + data.length);
    out.write(data);
  }

  void writeCopyDone() throws IOException {
    out.write('c');
    writeInt4(out, 4);
  }

  void writeClose(char type, String name) throws IOException {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    out.write('C');
    writeInt4(out, 4 + 1 + nameBytes.length + 1);
    out.write(type);
    out.write(nameBytes);
    out.write(0);
  }

  void writeTerminate() throws IOException {
    out.write('X');
    writeInt4(out, 4);
  }

  void flush() throws IOException {
    out.flush();
  }

  // ========== Backend (server → client) message reading ==========

  Message readMessage() throws IOException {
    int type = in.read();
    if (type == -1) throw new IOException("Unexpected end of stream");
    int length = in.readInt();
    int bodyLen = length - 4;

    // DataRow: read columns directly from stream, skip intermediate body allocation
    if (type == 'D') return parseDataRowDirect();

    // Bodyless messages — avoid allocating empty byte[]
    if (bodyLen == 0) {
      return switch (type) {
        case '1' -> Message.PARSE_COMPLETE;
        case '2' -> Message.BIND_COMPLETE;
        case 'n' -> Message.NO_DATA;
        case 'I' -> Message.EMPTY_QUERY;
        case 's' -> Message.PORTAL_SUSPENDED;
        case '3' -> Message.CLOSE_COMPLETE;
        default -> new Message.Unknown((char) type);
      };
    }

    byte[] body = new byte[bodyLen];
    in.readFully(body);

    return switch (type) {
      case 'R' -> parseAuth(body);
      case 'S' -> parseParameterStatus(body);
      case 'K' -> parseBackendKeyData(body);
      case 'Z' -> new Message.ReadyForQuery((char) body[0]);
      case '1' -> Message.PARSE_COMPLETE;
      case '2' -> Message.BIND_COMPLETE;
      case 'T' -> parseRowDescription(body);
      case 'C' -> parseCommandComplete(body);
      case 'E' -> parseErrorResponse(body);
      case 'n' -> Message.NO_DATA;
      case 't' -> parseParameterDescription(body);
      case 'N' -> Message.NOTICE;
      case 'I' -> Message.EMPTY_QUERY;
      case 'G' -> parseCopyInResponse(body);
      case 'H' -> parseCopyOutResponse(body);
      case 'W' -> parseCopyBothResponse(body);
      case 'A' -> parseNotification(body);
      case 'v' -> parseNegotiateProtocolVersion(body);
      case 's' -> Message.PORTAL_SUSPENDED;
      case '3' -> Message.CLOSE_COMPLETE;
      default -> new Message.Unknown((char) type);
    };
  }

  private Message.DataRow parseDataRowDirect() throws IOException {
    int columnCount = in.readUnsignedShort();
    byte[][] values = new byte[columnCount][];
    for (int i = 0; i < columnCount; i++) {
      int len = in.readInt();
      if (len == -1) {
        values[i] = null;
      } else {
        values[i] = new byte[len];
        in.readFully(values[i]);
      }
    }
    return new Message.DataRow(values);
  }

  private Message.ParameterDescription parseParameterDescription(byte[] body) {
    int paramCount = readInt2(body, 0);
    int[] paramOids = new int[paramCount];
    int pos = 2;
    for (int i = 0; i < paramCount; i++) {
      paramOids[i] = readInt4(body, pos);
      pos += 4;
    }
    return new Message.ParameterDescription(paramOids);
  }

  // ========== Message types ==========

  sealed interface Message {
    ParseComplete PARSE_COMPLETE = new ParseComplete();
    BindComplete BIND_COMPLETE = new BindComplete();
    NoData NO_DATA = new NoData();
    ParameterDescription PARAMETER_DESCRIPTION_EMPTY = new ParameterDescription(new int[0]);
    Notice NOTICE = new Notice();
    EmptyQuery EMPTY_QUERY = new EmptyQuery();
    PortalSuspended PORTAL_SUSPENDED = new PortalSuspended();
    CloseComplete CLOSE_COMPLETE = new CloseComplete();

    record AuthenticationOk() implements Message {}

    record AuthenticationCleartextPassword() implements Message {}

    record AuthenticationMD5(byte[] salt) implements Message {}

    record AuthenticationSASL(List<String> mechanisms) implements Message {}

    record AuthenticationSASLContinue(byte[] data) implements Message {}

    record AuthenticationSASLFinal(byte[] data) implements Message {}

    record ParameterStatus(String name, String value) implements Message {}

    record BackendKeyData(int pid, int secretKey) implements Message {}

    record ReadyForQuery(char status) implements Message {}

    record ParseComplete() implements Message {}

    record BindComplete() implements Message {}

    record RowDescription(ColumnDesc[] columns) implements Message {}

    record DataRow(byte[][] values) implements Message {}

    record CommandComplete(String tag) implements Message {}

    record ErrorResponse(dev.typr.foundations.PgError pgError) implements Message {}

    record NoData() implements Message {}

    record ParameterDescription(int[] paramOids) implements Message {}

    record Notice() implements Message {}

    record EmptyQuery() implements Message {}

    record CopyInResponse(byte format, int columnCount) implements Message {}

    record PortalSuspended() implements Message {}

    record CloseComplete() implements Message {}

    record Notification(int pid, String channel, String payload) implements Message {}

    record NegotiateProtocolVersion(int minorVersion, int unsupportedOptions) implements Message {}

    record CopyOutResponse(byte format, int columnCount) implements Message {}

    record CopyBothResponse(byte format, int columnCount) implements Message {}

    record Unknown(char type) implements Message {}
  }

  record ColumnDesc(String name, int typeOid) {}

  // ========== Text format decoding (PG text + OID → Java object) ==========

  static Object decodeTextValue(byte[] raw, int oid) {
    if (raw == null) return null;
    String text = new String(raw, StandardCharsets.UTF_8);
    return switch (oid) {
      case OID_BOOL -> text.equals("t") || text.equals("true") || text.equals("1");
      case OID_INT2 -> Short.parseShort(text);
      case OID_INT4, OID_OID -> Integer.parseInt(text);
      case OID_INT8 -> Long.parseLong(text);
      case OID_FLOAT4 -> Float.parseFloat(text);
      case OID_FLOAT8 -> Double.parseDouble(text);
      case OID_NUMERIC -> new BigDecimal(text);
      case OID_TEXT, OID_VARCHAR, OID_BPCHAR, OID_NAME, OID_UNKNOWN -> text;
      case OID_BYTEA -> decodeByteaHex(text);
      case OID_DATE -> LocalDate.parse(text);
      case OID_TIME -> LocalTime.parse(text, PG_TIME_FMT);
      case OID_TIMESTAMP -> parseTimestamp(text);
      case OID_TIMESTAMPTZ -> parseTimestamptz(text);
      case OID_UUID -> text; // PgRead uses getString().map(UUID::fromString)
      case OID_JSON, OID_JSONB -> text;
      default -> text;
    };
  }

  private static LocalDateTime parseTimestamp(String text) {
    return LocalDateTime.parse(text.replace(' ', 'T'), PG_TIMESTAMP_READ_FMT);
  }

  private static OffsetDateTime parseTimestamptz(String text) {
    return OffsetDateTime.parse(text.replace(' ', 'T'), PG_TIMESTAMPTZ_READ_FMT);
  }

  private static byte[] decodeByteaHex(String text) {
    if (text.startsWith("\\x")) {
      String hex = text.substring(2);
      byte[] bytes = new byte[hex.length() / 2];
      for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
      }
      return bytes;
    }
    return text.getBytes(StandardCharsets.UTF_8);
  }

  // ========== PostgreSQL OIDs ==========

  static final int OID_BOOL = 16;
  static final int OID_BYTEA = 17;
  static final int OID_NAME = 19;
  static final int OID_INT8 = 20;
  static final int OID_INT2 = 21;
  static final int OID_INT4 = 23;
  static final int OID_OID = 26;
  static final int OID_TEXT = 25;
  static final int OID_JSON = 114;
  static final int OID_FLOAT4 = 700;
  static final int OID_FLOAT8 = 701;
  static final int OID_UNKNOWN = 705;
  static final int OID_VARCHAR = 1043;
  static final int OID_BPCHAR = 1042;
  static final int OID_DATE = 1082;
  static final int OID_TIME = 1083;
  static final int OID_TIMESTAMP = 1114;
  static final int OID_TIMESTAMPTZ = 1184;
  static final int OID_NUMERIC = 1700;
  static final int OID_UUID = 2950;
  static final int OID_JSONB = 3802;

  // ========== Formatters ==========

  private static final DateTimeFormatter PG_TIMESTAMP_READ_FMT =
      new DateTimeFormatterBuilder()
          .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
          .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
          .toFormatter();

  private static final DateTimeFormatter PG_TIMESTAMPTZ_READ_FMT =
      new DateTimeFormatterBuilder()
          .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
          .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
          .appendOffset("+HH:mm:ss", "+00")
          .toFormatter();

  private static final DateTimeFormatter PG_TIME_FMT =
      new DateTimeFormatterBuilder()
          .appendPattern("HH:mm:ss")
          .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
          .toFormatter();

  // ========== Parsing helpers ==========

  private Message parseAuth(byte[] body) {
    int authType = readInt4(body, 0);
    return switch (authType) {
      case 0 -> new Message.AuthenticationOk();
      case 3 -> new Message.AuthenticationCleartextPassword();
      case 5 -> {
        byte[] salt = new byte[4];
        System.arraycopy(body, 4, salt, 0, 4);
        yield new Message.AuthenticationMD5(salt);
      }
      case 10 -> {
        List<String> mechanisms = new ArrayList<>();
        int pos = 4;
        while (pos < body.length) {
          String mech = readCString(body, pos);
          if (mech.isEmpty()) break;
          mechanisms.add(mech);
          pos += mech.length() + 1;
        }
        yield new Message.AuthenticationSASL(mechanisms);
      }
      case 11 -> {
        byte[] data = new byte[body.length - 4];
        System.arraycopy(body, 4, data, 0, data.length);
        yield new Message.AuthenticationSASLContinue(data);
      }
      case 12 -> {
        byte[] data = new byte[body.length - 4];
        System.arraycopy(body, 4, data, 0, data.length);
        yield new Message.AuthenticationSASLFinal(data);
      }
      default -> throw new PgPipelineException("Unsupported authentication type: " + authType);
    };
  }

  private Message.ParameterStatus parseParameterStatus(byte[] body) {
    int sep = indexOf(body, (byte) 0, 0);
    String name = new String(body, 0, sep, StandardCharsets.UTF_8);
    String value = new String(body, sep + 1, body.length - sep - 2, StandardCharsets.UTF_8);
    return new Message.ParameterStatus(name, value);
  }

  private Message.BackendKeyData parseBackendKeyData(byte[] body) {
    return new Message.BackendKeyData(readInt4(body, 0), readInt4(body, 4));
  }

  private Message.RowDescription parseRowDescription(byte[] body) {
    int fieldCount = readInt2(body, 0);
    ColumnDesc[] columns = new ColumnDesc[fieldCount];
    int pos = 2;
    for (int i = 0; i < fieldCount; i++) {
      String name = readCString(body, pos);
      pos += name.length() + 1;
      // int tableOid = readInt4(body, pos);
      pos += 4;
      // short columnAttr = readInt2(body, pos);
      pos += 2;
      int typeOid = readInt4(body, pos);
      pos += 4;
      // short typeSize = readInt2(body, pos);
      pos += 2;
      // int typeMod = readInt4(body, pos);
      pos += 4;
      // short formatCode = readInt2(body, pos);
      pos += 2;
      columns[i] = new ColumnDesc(name, typeOid);
    }
    return new Message.RowDescription(columns);
  }

  private Message.DataRow parseDataRow(byte[] body) {
    int columnCount = readInt2(body, 0);
    byte[][] values = new byte[columnCount][];
    int pos = 2;
    for (int i = 0; i < columnCount; i++) {
      int len = readInt4(body, pos);
      pos += 4;
      if (len == -1) {
        values[i] = null;
      } else {
        values[i] = new byte[len];
        System.arraycopy(body, pos, values[i], 0, len);
        pos += len;
      }
    }
    return new Message.DataRow(values);
  }

  private Message.CommandComplete parseCommandComplete(byte[] body) {
    String tag = readCString(body, 0);
    return new Message.CommandComplete(tag);
  }

  private Message.CopyInResponse parseCopyInResponse(byte[] body) {
    byte format = body[0];
    int columnCount = readInt2(body, 1);
    return new Message.CopyInResponse(format, columnCount);
  }

  private Message.CopyOutResponse parseCopyOutResponse(byte[] body) {
    byte format = body[0];
    int columnCount = readInt2(body, 1);
    return new Message.CopyOutResponse(format, columnCount);
  }

  private Message.CopyBothResponse parseCopyBothResponse(byte[] body) {
    byte format = body[0];
    int columnCount = readInt2(body, 1);
    return new Message.CopyBothResponse(format, columnCount);
  }

  private Message.Notification parseNotification(byte[] body) {
    int pid = readInt4(body, 0);
    String channel = readCString(body, 4);
    int payloadOffset = 4 + channel.length() + 1;
    String payload = readCString(body, payloadOffset);
    return new Message.Notification(pid, channel, payload);
  }

  private Message.NegotiateProtocolVersion parseNegotiateProtocolVersion(byte[] body) {
    int minorVersion = readInt4(body, 0);
    int unsupportedOptions = readInt4(body, 4);
    return new Message.NegotiateProtocolVersion(minorVersion, unsupportedOptions);
  }

  private Message.ErrorResponse parseErrorResponse(byte[] body) {
    String severity = "";
    String message = "";
    String sqlState = "";
    String detail = null;
    String hint = null;
    Integer position = null;
    String where = null;
    Integer internalPosition = null;
    String internalQuery = null;
    String schemaName = null;
    String tableName = null;
    String columnName = null;
    String dataTypeName = null;
    String constraintName = null;
    String file = null;
    Integer line = null;
    String routine = null;
    int pos = 0;
    while (pos < body.length && body[pos] != 0) {
      char fieldType = (char) body[pos++];
      String value = readCString(body, pos);
      pos += value.length() + 1;
      switch (fieldType) {
        case 'S' -> severity = value;
        case 'M' -> message = value;
        case 'C' -> sqlState = value;
        case 'D' -> detail = value;
        case 'H' -> hint = value;
        case 'P' -> position = parseIntOrNull(value);
        case 'W' -> where = value;
        case 'p' -> internalPosition = parseIntOrNull(value);
        case 'q' -> internalQuery = value;
        case 's' -> schemaName = value;
        case 't' -> tableName = value;
        case 'c' -> columnName = value;
        case 'd' -> dataTypeName = value;
        case 'n' -> constraintName = value;
        case 'F' -> file = value;
        case 'L' -> line = parseIntOrNull(value);
        case 'R' -> routine = value;
        default -> {}
      }
    }
    return new Message.ErrorResponse(
        new dev.typr.foundations.PgError(
            severity,
            message,
            sqlState,
            detail,
            hint,
            position,
            where,
            internalPosition,
            internalQuery,
            schemaName,
            tableName,
            columnName,
            dataTypeName,
            constraintName,
            file,
            line,
            routine));
  }

  private static Integer parseIntOrNull(String value) {
    try {
      int v = Integer.parseInt(value);
      return v == 0 ? null : v;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  // ========== Low-level I/O helpers ==========

  private static void writeInt4(OutputStream out, int value) throws IOException {
    out.write((value >> 24) & 0xFF);
    out.write((value >> 16) & 0xFF);
    out.write((value >> 8) & 0xFF);
    out.write(value & 0xFF);
  }

  private static void writeInt2(OutputStream out, int value) throws IOException {
    out.write((value >> 8) & 0xFF);
    out.write(value & 0xFF);
  }

  private static void writeCString(OutputStream out, String value) throws IOException {
    out.write(value.getBytes(StandardCharsets.UTF_8));
    out.write(0);
  }

  private static void putInt4(byte[] data, int offset, int value) {
    data[offset] = (byte) ((value >> 24) & 0xFF);
    data[offset + 1] = (byte) ((value >> 16) & 0xFF);
    data[offset + 2] = (byte) ((value >> 8) & 0xFF);
    data[offset + 3] = (byte) (value & 0xFF);
  }

  private static int readInt4(byte[] data, int offset) {
    return ((data[offset] & 0xFF) << 24)
        | ((data[offset + 1] & 0xFF) << 16)
        | ((data[offset + 2] & 0xFF) << 8)
        | (data[offset + 3] & 0xFF);
  }

  private static int readInt2(byte[] data, int offset) {
    return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
  }

  private static String readCString(byte[] data, int offset) {
    int end = indexOf(data, (byte) 0, offset);
    if (end < 0) end = data.length;
    return new String(data, offset, end - offset, StandardCharsets.UTF_8);
  }

  private static int indexOf(byte[] data, byte value, int from) {
    for (int i = from; i < data.length; i++) {
      if (data[i] == value) return i;
    }
    return -1;
  }

  // ========== Placeholder conversion ==========

  static String convertPlaceholders(String sql) {
    var sb = new StringBuilder(sql.length() + 16);
    int paramIndex = 0;
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;

    for (int i = 0; i < sql.length(); i++) {
      char c = sql.charAt(i);

      if (c == '\'' && !inDoubleQuote) {
        inSingleQuote = !inSingleQuote;
        sb.append(c);
      } else if (c == '"' && !inSingleQuote) {
        inDoubleQuote = !inDoubleQuote;
        sb.append(c);
      } else if (c == '?' && !inSingleQuote && !inDoubleQuote) {
        paramIndex++;
        sb.append('$').append(paramIndex);
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  // ========== Command tag parsing ==========

  static int parseAffectedRows(String tag) {
    // "INSERT 0 5" → 5, "UPDATE 3" → 3, "DELETE 1" → 1, "SELECT 10" → 10
    int lastSpace = tag.lastIndexOf(' ');
    if (lastSpace < 0) return 0;
    try {
      return Integer.parseInt(tag.substring(lastSpace + 1));
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
