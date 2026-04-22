package dev.typr.foundations;

import dev.typr.foundations.data.*;
import dev.typr.foundations.data.Record;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.postgresql.geometric.*;
import org.postgresql.util.PGInterval;
import org.postgresql.util.PGobject;

public interface PgTypes {
  PgType<AclItem> aclitem = ofPgObject("aclitem", AclItem::new, AclItem::value, PgJson.aclitem);
  PgType<AnyArray> anyarray =
      ofPgObject(
          "anyarray",
          AnyArray::new,
          AnyArray::value,
          PgJson.text.transform(AnyArray::new, AnyArray::value));
  PgType<BigDecimal> numeric =
      new PgType<>(
          PgTypename.of("numeric"),
          PgRead.readBigDecimal,
          PgWrite.writeBigDecimal,
          PgText.textBigDecimal,
          PgJson.numeric,
          PgOutParam.readBigDecimal,
          PgBinary.textFallback(PgText.textBigDecimal),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<Boolean> bool =
      new PgType<>(
          PgTypename.of("bool"),
          PgRead.readBoolean,
          PgWrite.writeBoolean,
          PgText.textBoolean,
          PgJson.bool,
          PgOutParam.readBoolean,
          PgBinary.textFallback(PgText.textBoolean),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');

  PgType<Bit> bit = bitType("bit");

  static PgType<Bit> bitOf(int n) {
    PgText<Bit> pgText = PgText.textString.bimap(Bit::new, Bit::value);
    return new PgType<>(
        PgTypename.of("bit", n),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        pgText,
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.textParsed()),
        ',');
  }

  private static PgType<Bit> bitType(String sqlType) {
    PgText<Bit> pgText = PgText.textString.bimap(Bit::new, Bit::value);
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        pgText,
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.textParsed()),
        ',');
  }

  PgType<Varbit> varbit = ofPgObject("varbit", Varbit::new, Varbit::value, PgJson.varbit);

  PgType<Double> float8 =
      new PgType<>(
          PgTypename.of("float8"),
          PgRead.readDouble,
          PgWrite.writeDouble,
          PgText.textDouble,
          PgJson.float8,
          PgOutParam.readDouble,
          PgBinary.textFallback(PgText.textDouble),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');

  PgType<Float> float4 =
      new PgType<>(
          PgTypename.of("float4"),
          PgRead.readFloat,
          PgWrite.writeFloat,
          PgText.textFloat,
          PgJson.float4,
          PgOutParam.readFloat,
          PgBinary.textFallback(PgText.textFloat),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');

  PgType<Inet> inet = ofPgObject("inet", Inet::new, Inet::value, PgJson.inet);
  PgType<Cidr> cidr = ofPgObject("cidr", Cidr::new, Cidr::value, PgJson.cidr);
  PgType<MacAddr> macaddr = ofPgObject("macaddr", MacAddr::new, MacAddr::value, PgJson.macaddr);
  PgType<MacAddr8> macaddr8 =
      ofPgObject("macaddr8", MacAddr8::new, MacAddr8::value, PgJson.macaddr8);
  PgType<Instant> timestamptz =
      new PgType<>(
          PgTypename.of("timestamptz"),
          PgRead.readInstant,
          PgWrite.primitive((ps, i, v) -> ps.setObject(i, v.atOffset(ZoneOffset.UTC))),
          PgText.codec(
              (t, sb) -> sb.append(t.atOffset(ZoneOffset.UTC).toString().replace('T', ' ')),
              text -> OffsetDateTime.parse(text.replace(' ', 'T')).toInstant()),
          PgJson.timestamptz,
          PgOutParam.readInstant,
          PgBinary.textFallback(
              PgText.codec(
                  (t, sb) -> sb.append(t.atOffset(ZoneOffset.UTC).toString().replace('T', ' ')),
                  text -> OffsetDateTime.parse(text.replace(' ', 'T')).toInstant())),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.of(obj -> ((java.sql.Timestamp) obj).toInstant())),
          ',');
  PgType<Int2Vector> int2vector =
      ofPgObject("int2vector", Int2Vector::new, Int2Vector::value, PgJson.int2vector);
  PgType<Integer> int4 =
      new PgType<>(
          PgTypename.of("int4"),
          PgRead.readInteger,
          PgWrite.writeInteger,
          PgText.textInteger,
          PgJson.int4,
          PgOutParam.readInteger,
          PgBinary.textFallback(PgText.textInteger),
          AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("serial")),
          Optional.of(PgElementCodec.cast()),
          ',');

  PgType<Json> json =
      ofPgObject("json", Json::new, Json::value, PgJson.json)
          .withArrayCodec(PgElementCodec.fromString(Json::new));
  PgType<Jsonb> jsonb =
      ofPgObject("jsonb", Jsonb::new, Jsonb::value, PgJson.jsonb)
          .withArrayCodec(PgElementCodec.fromString(Jsonb::new));
  PgType<LocalDate> date =
      new PgType<>(
          PgTypename.of("date"),
          PgRead.readLocalDate,
          PgWrite.passObjectToJdbc(),
          PgText.codec((d, sb) -> sb.append(d.toString()), LocalDate::parse),
          PgJson.date,
          PgOutParam.readLocalDate,
          PgBinary.textFallback(PgText.codec((d, sb) -> sb.append(d.toString()), LocalDate::parse)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.of(obj -> ((java.sql.Date) obj).toLocalDate())),
          ',');
  PgType<LocalDateTime> timestamp =
      new PgType<>(
          PgTypename.of("timestamp"),
          PgRead.readLocalDateTime,
          PgWrite.passObjectToJdbc(),
          PgText.codec(
              (t, sb) -> sb.append(t.toString().replace('T', ' ')),
              text -> LocalDateTime.parse(text.replace(' ', 'T'))),
          PgJson.timestamp,
          PgOutParam.readLocalDateTime,
          PgBinary.textFallback(
              PgText.codec(
                  (t, sb) -> sb.append(t.toString().replace('T', ' ')),
                  text -> LocalDateTime.parse(text.replace(' ', 'T')))),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.of(obj -> ((java.sql.Timestamp) obj).toLocalDateTime())),
          ',');
  PgType<LocalTime> time =
      new PgType<>(
          PgTypename.of("time"),
          PgRead.readLocalTime,
          PgWrite.passObjectToJdbc(),
          PgText.codec((t, sb) -> sb.append(t.toString()), LocalTime::parse),
          PgJson.time,
          PgOutParam.readLocalTime,
          PgBinary.textFallback(PgText.codec((t, sb) -> sb.append(t.toString()), LocalTime::parse)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.textParsed()),
          ',');
  PgType<Long> int8 =
      new PgType<>(
          PgTypename.of("int8"),
          PgRead.readLong,
          PgWrite.writeLong,
          PgText.textLong,
          PgJson.int8,
          PgOutParam.readLong,
          PgBinary.textFallback(PgText.textLong),
          AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("bigserial")),
          Optional.of(PgElementCodec.cast()),
          ',');

  PgType<Oid> oid =
      new PgType<>(
          PgTypename.of("oid"),
          PgRead.readLong.map(Oid::new),
          PgWrite.writeLong.contramap(Oid::value),
          PgText.textLong.bimap(Oid::new, Oid::value),
          PgJson.text.transform(s -> new Oid(Long.parseLong(s)), o -> Long.toString(o.value())),
          PgOutParam.readLong.map(Oid::new),
          PgBinary.textFallback(PgText.textLong.bimap(Oid::new, Oid::value)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.of(obj -> new Oid(((Number) obj).longValue()))),
          ',');

  PgType<Map<String, String>> hstore =
      new PgType<>(
          PgTypename.of("hstore"),
          PgRead.readMapStringString,
          PgWrite.passObjectToJdbc(),
          PgText.textMapStringString,
          PgJson.hstore,
          PgOutParam.readMapStringString,
          PgBinary.textFallback(PgText.textMapStringString),
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');
  PgType<Money> money =
      new PgType<>(
          PgTypename.of("money"),
          PgRead.readDouble.map(Money::new),
          PgWrite.pgObject("money").contramap(m -> String.valueOf(m.value())),
          PgText.codec((m, sb) -> sb.append(m.value()), Money::new),
          PgJson.money,
          PgOutParam.readDouble.map(Money::new),
          PgBinary.textFallback(PgText.codec((m, sb) -> sb.append(m.value()), Money::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.textParsed()),
          ',');
  PgType<String> name =
      new PgType<>(
          PgTypename.of("name"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgJson.text,
          PgOutParam.readString,
          PgBinary.textFallback(PgText.textString),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<OffsetTime> timetz =
      new PgType<>(
          PgTypename.of("timetz"),
          PgRead.readOffsetTime,
          PgWrite.passObjectToJdbc(),
          PgText.codec((t, sb) -> sb.append(t.toString()), PgTypes::parseTimetz),
          PgJson.timetz,
          PgOutParam.readOffsetTime,
          PgBinary.textFallback(
              PgText.codec((t, sb) -> sb.append(t.toString()), PgTypes::parseTimetz)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.textParsed()),
          ',');
  PgType<OidVector> oidvector =
      ofPgObject("oidvector", OidVector::new, OidVector::value, PgJson.oidvector);
  PgType<PGInterval> interval =
      new PgType<>(
          PgTypename.of("interval"),
          PgRead.castJdbcObjectTo(PGInterval.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGInterval::new),
          PgJson.interval,
          PgOutParam.castTo(PGInterval.class),
          PgBinary.textFallback(PgText.textPGobject(PGInterval::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<PGbox> box =
      new PgType<>(
          PgTypename.of("box"),
          PgRead.castJdbcObjectTo(PGbox.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGbox::new),
          PgJson.box,
          PgOutParam.castTo(PGbox.class),
          PgBinary.textFallback(PgText.textPGobject(PGbox::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGcircle> circle =
      new PgType<>(
          PgTypename.of("circle"),
          PgRead.castJdbcObjectTo(PGcircle.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGcircle::new),
          PgJson.circle,
          PgOutParam.castTo(PGcircle.class),
          PgBinary.textFallback(PgText.textPGobject(PGcircle::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGline> line =
      new PgType<>(
          PgTypename.of("line"),
          PgRead.castJdbcObjectTo(PGline.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGline::new),
          PgJson.line,
          PgOutParam.castTo(PGline.class),
          PgBinary.textFallback(PgText.textPGobject(PGline::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGlseg> lseg =
      new PgType<>(
          PgTypename.of("lseg"),
          PgRead.castJdbcObjectTo(PGlseg.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGlseg::new),
          PgJson.lseg,
          PgOutParam.castTo(PGlseg.class),
          PgBinary.textFallback(PgText.textPGobject(PGlseg::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGpath> path =
      new PgType<>(
          PgTypename.of("path"),
          PgRead.castJdbcObjectTo(PGpath.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGpath::new),
          PgJson.path,
          PgOutParam.castTo(PGpath.class),
          PgBinary.textFallback(PgText.textPGobject(PGpath::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGpoint> point =
      new PgType<>(
          PgTypename.of("point"),
          PgRead.castJdbcObjectTo(PGpoint.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGpoint::new),
          PgJson.point,
          PgOutParam.castTo(PGpoint.class),
          PgBinary.textFallback(PgText.textPGobject(PGpoint::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PGpolygon> polygon =
      new PgType<>(
          PgTypename.of("polygon"),
          PgRead.castJdbcObjectTo(PGpolygon.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(PGpolygon::new),
          PgJson.polygon,
          PgOutParam.castTo(PGpolygon.class),
          PgBinary.textFallback(PgText.textPGobject(PGpolygon::new)),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ';');
  PgType<PgNodeTree> pgNodeTree =
      ofPgObject(
          "pg_node_tree",
          PgNodeTree::new,
          PgNodeTree::value,
          PgJson.text.transform(PgNodeTree::new, PgNodeTree::value));
  PgType<Regclass> regclass =
      ofPgObject("regclass", Regclass::new, Regclass::value, PgJson.regclass);
  PgType<Regconfig> regconfig =
      ofPgObject("regconfig", Regconfig::new, Regconfig::value, PgJson.regconfig);
  PgType<Regdictionary> regdictionary =
      ofPgObject("regdictionary", Regdictionary::new, Regdictionary::value, PgJson.regdictionary);
  PgType<Regnamespace> regnamespace =
      ofPgObject("regnamespace", Regnamespace::new, Regnamespace::value, PgJson.regnamespace);
  PgType<Regoper> regoper = ofPgObject("regoper", Regoper::new, Regoper::value, PgJson.regoper);
  PgType<Regoperator> regoperator =
      ofPgObject("regoperator", Regoperator::new, Regoperator::value, PgJson.regoperator);
  PgType<Regproc> regproc = ofPgObject("regproc", Regproc::new, Regproc::value, PgJson.regproc);
  PgType<Regprocedure> regprocedure =
      ofPgObject("regprocedure", Regprocedure::new, Regprocedure::value, PgJson.regprocedure);
  PgType<Regrole> regrole = ofPgObject("regrole", Regrole::new, Regrole::value, PgJson.regrole);
  PgType<Regtype> regtype = ofPgObject("regtype", Regtype::new, Regtype::value, PgJson.regtype);
  PgType<Short> int2 =
      new PgType<>(
          PgTypename.of("int2"),
          PgRead.readShort,
          PgWrite.writeShort,
          PgText.textShort,
          PgJson.int2,
          PgOutParam.readShort,
          PgBinary.textFallback(PgText.textShort),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<Short> smallint =
      int2.withTypename(PgTypename.of("smallint"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));
  PgType<Short> smallserial =
      int2.withTypename(PgTypename.of("smallserial"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));

  PgType<String> bpchar =
      new PgType<>(
          PgTypename.of("bpchar"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgJson.text,
          PgOutParam.readString,
          PgBinary.textFallback(PgText.textString),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<String> text =
      new PgType<>(
          PgTypename.of("text"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgJson.text,
          PgOutParam.readString,
          PgBinary.textFallback(PgText.textString),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<UUID> uuid =
      new PgType<>(
          PgTypename.of("uuid"),
          PgRead.readUUID,
          PgWrite.writeUUID,
          PgText.textUuid,
          PgJson.uuid,
          PgOutParam.readUUID,
          PgBinary.textFallback(PgText.textUuid),
          AnalysisOptions.EMPTY,
          Optional.of(PgElementCodec.cast()),
          ',');
  PgType<Xid> xid = ofPgObject("xid", Xid::new, Xid::value, PgJson.xid);
  PgType<Xml> xml =
      new PgType<>(
              PgTypename.of("xml"),
              PgRead.readString,
              PgWrite.pgObject("xml"),
              PgText.textString,
              PgJson.text,
              PgOutParam.readString,
              PgBinary.textFallback(PgText.textString),
              AnalysisOptions.EMPTY,
              Optional.of(PgElementCodec.of(obj -> ((PGobject) obj).getValue())),
              ',')
          .transform(Xml::new, Xml::value);
  PgType<Vector> vector =
      new PgType<>(
              PgTypename.of("vector"),
              PgRead.readString,
              PgWrite.pgObject("vector"),
              PgText.textString,
              PgJson.text,
              PgOutParam.readString,
              PgBinary.textFallback(PgText.textString),
              AnalysisOptions.EMPTY,
              Optional.of(PgElementCodec.of(obj -> ((PGobject) obj).getValue())),
              ',')
          .transform(Vector::new, Vector::value);
  PgType<Unknown> unknown =
      new PgType<>(
              PgTypename.of("unknown"),
              PgRead.readString,
              PgWrite.pgObject("unknown"),
              PgText.textString,
              PgJson.text,
              PgOutParam.readString,
              PgBinary.textFallback(PgText.textString),
              AnalysisOptions.EMPTY,
              Optional.<PgElementCodec<String>>empty(),
              ',')
          .transform(Unknown::new, Unknown::value);
  PgType<byte[]> bytea =
      new PgType<>(
          PgTypename.of("bytea"),
          PgRead.readByteArray,
          PgWrite.writeByteArray,
          PgText.textByteArray,
          PgJson.bytea,
          PgOutParam.readByteArray,
          PgBinary.textFallback(PgText.textByteArray),
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  // Range types
  PgType<Range<Integer>> int4range =
      rangeType("int4range", RangeParser.INT4_PARSER, Range.INT4, PgJson.int4range);
  PgType<Range<Long>> int8range =
      rangeType("int8range", RangeParser.INT8_PARSER, Range.INT8, PgJson.int8range);
  PgType<Range<BigDecimal>> numrange =
      rangeType("numrange", RangeParser.NUMERIC_PARSER, Range.NUMERIC, PgJson.numrange);
  PgType<Range<LocalDate>> daterange =
      rangeType("daterange", RangeParser.DATE_PARSER, Range.DATE, PgJson.daterange);
  PgType<Range<LocalDateTime>> tsrange =
      rangeType("tsrange", RangeParser.TIMESTAMP_PARSER, Range.TIMESTAMP, PgJson.tsrange);
  PgType<Range<Instant>> tstzrange =
      rangeType("tstzrange", RangeParser.TIMESTAMPTZ_PARSER, Range.TIMESTAMPTZ, PgJson.tstzrange);

  static <E extends Enum<E>> PgType<E> ofEnum(String sqlType, Function<String, E> fromString) {
    return ofEnumImpl(sqlType, fromString, Enum::name);
  }

  /**
   * Create a PgType for ENUM columns from a values array. No reflection.
   *
   * @param sqlType the PostgreSQL type name (e.g., "status")
   * @param values all enum constants (e.g., {@code Status.values()})
   */
  static <E extends Enum<E>> PgType<E> ofEnum(String sqlType, E[] values) {
    return ofEnumImpl(sqlType, enumFromString(values, Enum::name), Enum::name);
  }

  /**
   * Create a PgType for ENUM columns from a values array and a name function. No {@code Enum} bound
   * required — works with Scala 3 enums and other constant sets.
   */
  static <E> PgType<E> ofEnum(String sqlType, E[] values, Function<E, String> name) {
    return ofEnumImpl(sqlType, enumFromString(values, name), name);
  }

  private static <E> PgType<E> ofEnumImpl(
      String sqlType, Function<String, E> fromString, Function<E, String> name) {
    PgText<E> pgText = PgText.textString.bimap(fromString::apply, name::apply);
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.readString.map(fromString::apply),
        PgWrite.writeString.contramap(name::apply),
        pgText,
        PgJson.text.transform(fromString::apply, name::apply),
        PgOutParam.readString.map(fromString::apply),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.fromString(fromString::apply)),
        ',');
  }

  private static OffsetTime parseTimetz(String text) {
    // PG sends timetz as e.g. "10:30:45.123456+00" — Java's OffsetTime.parse requires
    // full offset format "+00:00". Normalize PG's short offset format.
    int plusIdx = text.lastIndexOf('+');
    int minusIdx = text.lastIndexOf('-');
    int offsetStart = Math.max(plusIdx, minusIdx);
    if (offsetStart > 0) {
      String offset = text.substring(offsetStart);
      if (!offset.contains(":")) {
        text = text.substring(0, offsetStart) + offset + ":00";
      }
    }
    return OffsetTime.parse(text);
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

  static <T> PgType<T> ofPgObject(
      String sqlType,
      SqlFunction<String, T> constructor,
      Function<T, String> extractor,
      PgJson<T> json) {
    Function<String, T> uncheckedConstructor =
        s -> {
          try {
            return constructor.apply(s);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException.Jdbc(e);
          }
        };
    PgText<T> pgText = PgText.textString.bimap(uncheckedConstructor, extractor);
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.pgObject(sqlType).map(constructor),
        PgWrite.pgObject(sqlType).contramap(extractor),
        pgText,
        json,
        PgOutParam.pgObject(constructor),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.pgObject(constructor)),
        ',');
  }

  // Default record type for generic composite/record columns
  PgType<Record> record = ofPgObject("record", Record::new, Record::value, PgJson.record);

  static PgType<Record> recordOf(String sqlType) {
    return ofPgObject(sqlType, Record::new, Record::value, PgJson.record);
  }

  static <T extends PGobject> PgType<T> pgObject(String sqlType, Class<T> clazz, PgJson<T> json) {
    SqlFunction<String, T> constructor =
        value -> {
          try {
            T obj = clazz.getDeclaredConstructor(String.class).newInstance(value);
            return obj;
          } catch (ReflectiveOperationException e) {
            throw new java.sql.SQLException("Failed to construct " + clazz.getSimpleName(), e);
          }
        };
    PgText<T> pgText = PgText.textPGobject(constructor);
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.castJdbcObjectTo(clazz),
        PgWrite.passObjectToJdbc(),
        pgText,
        json,
        PgOutParam.castTo(clazz),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.cast()),
        ',');
  }

  static PgType<String> bpcharOf(int precision) {
    return new PgType<>(
        PgTypename.of("bpchar", precision),
        PgRead.readString,
        PgWrite.writeString,
        PgText.textString,
        PgJson.text,
        PgOutParam.readString,
        PgBinary.textFallback(PgText.textString),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.cast()),
        ',');
  }

  static <T extends Comparable<? super T>> PgType<Range<T>> rangeType(
      String sqlType,
      SqlFunction<String, T> valueParser,
      java.util.function.BiFunction<RangeBound<T>, RangeBound<T>, Range<T>> rangeFactory,
      PgJson<Range<T>> json) {
    Function<String, Range<T>> parseRange =
        str -> {
          try {
            return RangeParser.parse(str, valueParser, rangeFactory);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException.Jdbc(e);
          }
        };
    PgText<Range<T>> pgText = PgText.textString.bimap(parseRange, RangeParser::format);
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.pgObject(sqlType).map(str -> RangeParser.parse(str, valueParser, rangeFactory)),
        PgWrite.pgObject(sqlType).contramap(RangeParser::format),
        pgText,
        json,
        PgOutParam.pgObject(str -> RangeParser.parse(str, valueParser, rangeFactory)),
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(
            PgElementCodec.of(
                obj -> {
                  try {
                    return RangeParser.parse(
                        ((PGobject) obj).getValue(), valueParser, rangeFactory);
                  } catch (java.sql.SQLException e) {
                    throw new DatabaseException.Jdbc(e);
                  }
                })),
        ',');
  }

  // ==================== Composite Types ====================

  /**
   * Build an ad-hoc composite PgType from a {@link RowCodecNamed}. Read-only — use for row
   * constructors like {@code (a, b, c)} in SQL that don't have a {@code CREATE TYPE} declaration.
   * Attempting to write a value produces an error.
   */
  static <Row> PgType<Row> compositeOf(RowCodecNamed<Row> codec) {
    return compositeOfImpl("record", codec, false);
  }

  /**
   * Build a named composite PgType from a {@link RowCodecNamed}. Use for PostgreSQL composite types
   * declared with {@code CREATE TYPE}. Supports both read and write.
   */
  static <Row> PgType<Row> compositeOf(String sqlType, RowCodecNamed<Row> codec) {
    return compositeOfImpl(sqlType, codec, true);
  }

  private static <Row> PgType<Row> compositeOfImpl(
      String sqlType, RowCodecNamed<Row> codec, boolean writable) {
    // Validate all columns are PgType and extract them
    var columns = codec.columns();
    var names = codec.columnNames();
    var pgColumns = new java.util.ArrayList<PgType<?>>(columns.size());
    var typenameFields =
        new java.util.ArrayList<PgTypename.CompositeOf.CompositeField>(columns.size());
    for (int i = 0; i < columns.size(); i++) {
      var col = columns.get(i);
      if (!(col instanceof PgType<?> pg)) {
        throw new IllegalArgumentException(
            "compositeOf requires all fields to be PgType, got: "
                + col.getClass().getSimpleName()
                + " at field '"
                + names.get(i)
                + "'");
      }
      pgColumns.add(pg);
      typenameFields.add(new PgTypename.CompositeOf.CompositeField(names.get(i), pg.typename()));
    }

    PgTypename<Row> typename = new PgTypename.CompositeOf<Row>(sqlType, typenameFields).asGeneric();
    var decode = codec.decode();
    var encode = codec.encode();

    java.util.function.Function<String, Row> parseFromText =
        text -> {
          List<String> parsedFields = PgRecordParser.parse(text);
          if (parsedFields.size() != pgColumns.size()) {
            throw new DatabaseException.Jdbc(
                new java.sql.SQLException(
                    "Field count mismatch: expected "
                        + pgColumns.size()
                        + " but got "
                        + parsedFields.size()
                        + " in: "
                        + text));
          }
          Object[] fieldValues = new Object[pgColumns.size()];
          for (int i = 0; i < pgColumns.size(); i++) {
            String raw = parsedFields.get(i);
            fieldValues[i] = raw == null ? null : pgColumns.get(i).pgCompositeText().decode(raw);
          }
          return decode.apply(fieldValues);
        };

    java.util.function.Function<Row, String> encodeToText =
        value -> {
          if (!writable) {
            throw new UnsupportedOperationException(
                "Cannot encode ad-hoc composite type (no sqlType). Use compositeOf(sqlType, codec)"
                    + " for writable composites.");
          }
          Object[] fieldValues = encode.apply(value);
          var encodedFields = new java.util.ArrayList<String>(fieldValues.length);
          for (int i = 0; i < fieldValues.length; i++) {
            encodedFields.add(encodeCompositeField(pgColumns.get(i), fieldValues[i]));
          }
          return PgRecordParser.encode(encodedFields);
        };

    PgRead<Row> pgRead =
        PgRead.of(
            (rs, idx) -> {
              Object obj = rs.getObject(idx);
              if (obj == null) return null;
              if (obj instanceof PGobject pgObj) {
                String textValue = pgObj.getValue();
                if (textValue == null) return null;
                return parseFromText.apply(textValue);
              }
              throw new java.sql.SQLException(
                  "Expected PGobject for composite type, got: " + obj.getClass());
            });

    PgWrite<Row> pgWrite =
        new PgWrite.Instance<>(
            (ps, idx, pgObj) -> ps.setObject(idx, pgObj),
            value -> {
              if (value == null) return null;
              PGobject pgObj = new PGobject();
              pgObj.setType(sqlType);
              try {
                pgObj.setValue(encodeToText.apply(value));
              } catch (java.sql.SQLException e) {
                throw new DatabaseException.Jdbc("Failed to encode composite type", e);
              }
              return pgObj;
            });

    PgText<Row> pgText =
        new PgText<>() {
          @Override
          public void unsafeEncode(Row value, StringBuilder sb) {
            sb.append(encodeToText.apply(value));
          }

          @Override
          public void unsafeArrayEncode(Row value, StringBuilder sb) {
            unsafeEncode(value, sb);
          }

          @Override
          public void wireEncode(Row value, StringBuilder sb) {
            sb.append(encodeToText.apply(value));
          }

          @Override
          public Row wireDecode(String text) {
            return parseFromText.apply(text);
          }
        };

    PgJson<Row> pgJson =
        new PgJson<>() {
          @Override
          public dev.typr.foundations.data.JsonValue toJson(Row value) {
            Object[] fieldValues = encode.apply(value);
            var jsonFields =
                new java.util.LinkedHashMap<String, dev.typr.foundations.data.JsonValue>();
            for (int i = 0; i < fieldValues.length; i++) {
              jsonFields.put(names.get(i), compositeFieldToJson(pgColumns.get(i), fieldValues[i]));
            }
            return new dev.typr.foundations.data.JsonValue.JObject(jsonFields);
          }

          @Override
          public Row fromJson(dev.typr.foundations.data.JsonValue jsonValue) {
            if (jsonValue instanceof dev.typr.foundations.data.JsonValue.JObject obj) {
              Object[] fieldValues = new Object[pgColumns.size()];
              for (int i = 0; i < pgColumns.size(); i++) {
                var fieldJson = obj.fields().get(names.get(i));
                fieldValues[i] =
                    (fieldJson == null
                            || fieldJson instanceof dev.typr.foundations.data.JsonValue.JNull)
                        ? null
                        : pgColumns.get(i).pgJson().fromJson(fieldJson);
              }
              return decode.apply(fieldValues);
            }
            throw new IllegalArgumentException("Expected JSON object");
          }
        };

    PgOutParam<Row> pgOutParam = PgOutParam.pgObject(parseFromText::apply);

    return new PgType<>(
        typename,
        pgRead,
        pgWrite,
        pgText,
        pgJson,
        pgOutParam,
        PgBinary.textFallback(pgText),
        AnalysisOptions.EMPTY,
        Optional.of(PgElementCodec.of(obj -> parseFromText.apply(obj.toString()))),
        ',');
  }

  @SuppressWarnings("unchecked")
  private static <F> String encodeCompositeField(PgType<F> column, Object value) {
    if (value == null) return null;
    // Bridge from Object[] (erased by RowCodec.encode) to typed PgCompositeText.encode.
    // Safe because RowCodec guarantees position-matched types from its typed builder.
    return column.pgCompositeText().encode((F) value).orElse(null);
  }

  @SuppressWarnings("unchecked")
  private static <F> dev.typr.foundations.data.JsonValue compositeFieldToJson(
      PgType<F> column, Object value) {
    if (value == null) return dev.typr.foundations.data.JsonValue.JNull.INSTANCE;
    return column.pgJson().toJson((F) value);
  }

  // ==================== JSON-Encoded Row Types ====================

  // ── json ──

  /** A {@code json} column type that stores a single row as a positional JSON array. */
  static <Row> PgType<Row> jsonArrayEncoded(RowCodec<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonArray(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /** A {@code json} column type that stores a list of rows, each as a positional JSON array. */
  static <Row> PgType<List<Row>> jsonArrayEncodedList(RowCodec<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonArray(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }

  /** A {@code json} column type that stores a single row as a keyed JSON object. */
  static <Row> PgType<Row> jsonObjectEncoded(RowCodecNamed<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonObject(codec);
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Json(rowJson.toJson(row).encode()));
  }

  /** A {@code json} column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> PgType<List<Row>> jsonObjectEncodedList(RowCodecNamed<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonObject(codec).list();
    return json.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Json(rowJson.toJson(list).encode()));
  }

  // ── jsonb ──

  /** A {@code jsonb} column type that stores a single row as a positional JSON array. */
  static <Row> PgType<Row> jsonbArrayEncoded(RowCodec<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonArray(codec);
    return jsonb.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Jsonb(rowJson.toJson(row).encode()));
  }

  /** A {@code jsonb} column type that stores a list of rows, each as a positional JSON array. */
  static <Row> PgType<List<Row>> jsonbArrayEncodedList(RowCodec<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonArray(codec).list();
    return jsonb.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Jsonb(rowJson.toJson(list).encode()));
  }

  /** A {@code jsonb} column type that stores a single row as a keyed JSON object. */
  static <Row> PgType<Row> jsonbObjectEncoded(RowCodecNamed<Row> codec) {
    DbJson<Row> rowJson = DbJsonRow.jsonObject(codec);
    return jsonb.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        row -> new Jsonb(rowJson.toJson(row).encode()));
  }

  /** A {@code jsonb} column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> PgType<List<Row>> jsonbObjectEncodedList(RowCodecNamed<Row> codec) {
    DbJson<List<Row>> rowJson = DbJsonRow.jsonObject(codec).list();
    return jsonb.transform(
        j -> rowJson.fromJson(JsonValue.parse(j.value())),
        list -> new Jsonb(rowJson.toJson(list).encode()));
  }
}
