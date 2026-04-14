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
          PgCompositeText.numeric,
          PgJson.numeric,
          PgOutParam.readBigDecimal,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<Boolean> bool =
      new PgType<>(
          PgTypename.of("bool"),
          PgRead.readBoolean,
          PgWrite.writeBoolean,
          PgText.textBoolean,
          PgCompositeText.bool,
          PgJson.bool,
          PgOutParam.readBoolean,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');

  @SuppressWarnings("unchecked")
  PgType<boolean[]> boolArrayUnboxed =
      new PgType<>(
          (PgTypename<boolean[]>) (PgTypename<?>) PgTypename.of("bool").array(),
          PgRead.readBooleanArrayUnboxed,
          PgWrite.writeBooleanArrayUnboxed,
          PgText.boolArrayUnboxed,
          PgCompositeText.boolArrayUnboxed,
          PgJson.boolArrayUnboxed,
          PgOutParam.readBooleanArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  PgType<Bit> bit = bitType("bit");

  static PgType<Bit> bit(int n) {
    return new PgType<>(
        PgTypename.of("bit", n),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.textParsed()),
        ',');
  }

  private static PgType<Bit> bitType(String sqlType) {
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.textParsed()),
        ',');
  }

  PgType<Varbit> varbit = ofPgObject("varbit", Varbit::new, Varbit::value, PgJson.varbit);

  PgType<Double> float8 =
      new PgType<>(
          PgTypename.of("float8"),
          PgRead.readDouble,
          PgWrite.writeDouble,
          PgText.textDouble,
          PgCompositeText.float8,
          PgJson.float8,
          PgOutParam.readDouble,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');

  @SuppressWarnings("unchecked")
  PgType<double[]> float8ArrayUnboxed =
      new PgType<>(
          (PgTypename<double[]>) (PgTypename<?>) PgTypename.of("float8").array(),
          PgRead.readDoubleArrayUnboxed,
          PgWrite.writeDoubleArrayUnboxed,
          PgText.doubleArrayUnboxed,
          PgCompositeText.doubleArrayUnboxed,
          PgJson.doubleArrayUnboxed,
          PgOutParam.readDoubleArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  PgType<Float> float4 =
      new PgType<>(
          PgTypename.of("float4"),
          PgRead.readFloat,
          PgWrite.writeFloat,
          PgText.textFloat,
          PgCompositeText.float4,
          PgJson.float4,
          PgOutParam.readFloat,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');

  @SuppressWarnings("unchecked")
  PgType<float[]> float4ArrayUnboxed =
      new PgType<>(
          (PgTypename<float[]>) (PgTypename<?>) PgTypename.of("float4").array(),
          PgRead.readFloatArrayUnboxed,
          PgWrite.writeFloatArrayUnboxed,
          PgText.floatArrayUnboxed,
          PgCompositeText.floatArrayUnboxed,
          PgJson.floatArrayUnboxed,
          PgOutParam.readFloatArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
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
          PgText.instance(
              (t, sb) -> sb.append(t.atOffset(ZoneOffset.UTC).toString().replace('T', ' '))),
          PgCompositeText.of(
              t -> t.atOffset(ZoneOffset.UTC).toString().replace('T', ' '),
              text -> OffsetDateTime.parse(text.replace(' ', 'T')).toInstant()),
          PgJson.timestamptz,
          PgOutParam.readInstant,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.of(obj -> ((java.sql.Timestamp) obj).toInstant())),
          ',');
  PgType<Int2Vector> int2vector =
      ofPgObject("int2vector", Int2Vector::new, Int2Vector::value, PgJson.int2vector);
  PgType<Integer> int4 =
      new PgType<>(
          PgTypename.of("int4"),
          PgRead.readInteger,
          PgWrite.writeInteger,
          PgText.textInteger,
          PgCompositeText.int4,
          PgJson.int4,
          PgOutParam.readInteger,
          AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("serial")),
          Optional.of(PgArrayCodec.cast()),
          ',');

  @SuppressWarnings("unchecked")
  PgType<int[]> int4ArrayUnboxed =
      new PgType<>(
          (PgTypename<int[]>) (PgTypename<?>) PgTypename.of("int4").array(),
          PgRead.readIntArrayUnboxed,
          PgWrite.writeIntArrayUnboxed,
          PgText.intArrayUnboxed,
          PgCompositeText.intArrayUnboxed,
          PgJson.intArrayUnboxed,
          PgOutParam.readIntArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  PgType<Json> json =
      ofPgObject("json", Json::new, Json::value, PgJson.json)
          .withArrayCodec(PgArrayCodec.fromString(Json::new));
  PgType<Jsonb> jsonb =
      ofPgObject("jsonb", Jsonb::new, Jsonb::value, PgJson.jsonb)
          .withArrayCodec(PgArrayCodec.fromString(Jsonb::new));
  PgType<LocalDate> date =
      new PgType<>(
          PgTypename.of("date"),
          PgRead.readLocalDate,
          PgWrite.passObjectToJdbc(),
          PgText.instance((d, sb) -> sb.append(d.toString())),
          PgCompositeText.of(LocalDate::toString, LocalDate::parse),
          PgJson.date,
          PgOutParam.readLocalDate,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.of(obj -> ((java.sql.Date) obj).toLocalDate())),
          ',');
  PgType<LocalDateTime> timestamp =
      new PgType<>(
          PgTypename.of("timestamp"),
          PgRead.readLocalDateTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString().replace('T', ' '))),
          PgCompositeText.of(
              t -> t.toString().replace('T', ' '),
              text -> LocalDateTime.parse(text.replace(' ', 'T'))),
          PgJson.timestamp,
          PgOutParam.readLocalDateTime,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.of(obj -> ((java.sql.Timestamp) obj).toLocalDateTime())),
          ',');
  PgType<LocalTime> time =
      new PgType<>(
          PgTypename.of("time"),
          PgRead.readLocalTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.of(LocalTime::toString, LocalTime::parse),
          PgJson.time,
          PgOutParam.readLocalTime,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.textParsed()),
          ',');
  PgType<Long> int8 =
      new PgType<>(
          PgTypename.of("int8"),
          PgRead.readLong,
          PgWrite.writeLong,
          PgText.textLong,
          PgCompositeText.int8,
          PgJson.int8,
          PgOutParam.readLong,
          AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("bigserial")),
          Optional.of(PgArrayCodec.cast()),
          ',');

  @SuppressWarnings("unchecked")
  PgType<long[]> int8ArrayUnboxed =
      new PgType<>(
          (PgTypename<long[]>) (PgTypename<?>) PgTypename.of("int8").array(),
          PgRead.readLongArrayUnboxed,
          PgWrite.writeLongArrayUnboxed,
          PgText.longArrayUnboxed,
          PgCompositeText.longArrayUnboxed,
          PgJson.longArrayUnboxed,
          PgOutParam.readLongArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  PgType<Oid> oid =
      new PgType<>(
          PgTypename.of("oid"),
          PgRead.readLong.map(Oid::new),
          PgWrite.writeLong.contramap(Oid::value),
          PgText.instance((o, sb) -> sb.append(o.value())),
          PgCompositeText.int8.transform(Oid::new, Oid::value),
          PgJson.text.transform(s -> new Oid(Long.parseLong(s)), o -> Long.toString(o.value())),
          PgOutParam.readLong.map(Oid::new),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.of(obj -> new Oid(((Number) obj).longValue()))),
          ',');

  PgType<Map<String, String>> hstore =
      new PgType<>(
          PgTypename.of("hstore"),
          PgRead.readMapStringString,
          PgWrite.passObjectToJdbc(),
          PgText.textMapStringString,
          PgCompositeText.hstore,
          PgJson.hstore,
          PgOutParam.readMapStringString,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');
  PgType<Money> money =
      new PgType<>(
          PgTypename.of("money"),
          PgRead.readDouble.map(Money::new),
          PgWrite.pgObject("money").contramap(m -> String.valueOf(m.value())),
          PgText.textDouble.contramap(Money::value),
          PgCompositeText.money,
          PgJson.money,
          PgOutParam.readDouble.map(Money::new),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.textParsed()),
          ',');
  PgType<String> name =
      new PgType<>(
          PgTypename.of("name"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<OffsetTime> timetz =
      new PgType<>(
          PgTypename.of("timetz"),
          PgRead.readOffsetTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.timetz,
          PgJson.timetz,
          PgOutParam.readOffsetTime,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.textParsed()),
          ',');
  PgType<OidVector> oidvector =
      ofPgObject("oidvector", OidVector::new, OidVector::value, PgJson.oidvector);
  PgType<PGInterval> interval =
      new PgType<>(
          PgTypename.of("interval"),
          PgRead.castJdbcObjectTo(PGInterval.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.interval,
          PgJson.interval,
          PgOutParam.castTo(PGInterval.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<PGbox> box =
      new PgType<>(
          PgTypename.of("box"),
          PgRead.castJdbcObjectTo(PGbox.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.box,
          PgJson.box,
          PgOutParam.castTo(PGbox.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGcircle> circle =
      new PgType<>(
          PgTypename.of("circle"),
          PgRead.castJdbcObjectTo(PGcircle.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.circle,
          PgJson.circle,
          PgOutParam.castTo(PGcircle.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGline> line =
      new PgType<>(
          PgTypename.of("line"),
          PgRead.castJdbcObjectTo(PGline.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.line,
          PgJson.line,
          PgOutParam.castTo(PGline.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGlseg> lseg =
      new PgType<>(
          PgTypename.of("lseg"),
          PgRead.castJdbcObjectTo(PGlseg.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.lseg,
          PgJson.lseg,
          PgOutParam.castTo(PGlseg.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGpath> path =
      new PgType<>(
          PgTypename.of("path"),
          PgRead.castJdbcObjectTo(PGpath.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.path,
          PgJson.path,
          PgOutParam.castTo(PGpath.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGpoint> point =
      new PgType<>(
          PgTypename.of("point"),
          PgRead.castJdbcObjectTo(PGpoint.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.point,
          PgJson.point,
          PgOutParam.castTo(PGpoint.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ';');
  PgType<PGpolygon> polygon =
      new PgType<>(
          PgTypename.of("polygon"),
          PgRead.castJdbcObjectTo(PGpolygon.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.polygon,
          PgJson.polygon,
          PgOutParam.castTo(PGpolygon.class),
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
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
          PgCompositeText.int2,
          PgJson.int2,
          PgOutParam.readShort,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<Short> smallint =
      int2.withTypename(PgTypename.of("smallint"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));
  PgType<Short> smallserial =
      int2.withTypename(PgTypename.of("smallserial"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));

  @SuppressWarnings("unchecked")
  PgType<short[]> int2ArrayUnboxed =
      new PgType<>(
          (PgTypename<short[]>) (PgTypename<?>) PgTypename.of("int2").array(),
          PgRead.readShortArrayUnboxed,
          PgWrite.writeShortArrayUnboxed,
          PgText.shortArrayUnboxed,
          PgCompositeText.shortArrayUnboxed,
          PgJson.shortArrayUnboxed,
          PgOutParam.readShortArrayUnboxed,
          AnalysisOptions.EMPTY,
          Optional.empty(),
          ',');

  @SuppressWarnings("unchecked")
  PgType<short[]> smallintArrayUnboxed =
      int2ArrayUnboxed
          .renamed("smallint")
          .withAnalysis(
              AnalysisOptions.EMPTY.withVendorTypeNames(
                  (PgTypename<short[]>) (PgTypename<?>) PgTypename.of("int2").array()));

  PgType<String> bpchar =
      new PgType<>(
          PgTypename.of("bpchar"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<String> text =
      new PgType<>(
          PgTypename.of("text"),
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<UUID> uuid =
      new PgType<>(
          PgTypename.of("uuid"),
          PgRead.readUUID,
          PgWrite.writeUUID,
          PgText.textUuid,
          PgCompositeText.uuid,
          PgJson.uuid,
          PgOutParam.readUUID,
          AnalysisOptions.EMPTY,
          Optional.of(PgArrayCodec.cast()),
          ',');
  PgType<Xid> xid = ofPgObject("xid", Xid::new, Xid::value, PgJson.xid);
  PgType<Xml> xml =
      new PgType<>(
              PgTypename.of("xml"),
              PgRead.readString,
              PgWrite.pgObject("xml"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString,
              AnalysisOptions.EMPTY,
              Optional.of(PgArrayCodec.of(obj -> ((PGobject) obj).getValue())),
              ',')
          .transform(Xml::new, Xml::value);
  PgType<Vector> vector =
      new PgType<>(
              PgTypename.of("vector"),
              PgRead.readString,
              PgWrite.pgObject("vector"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString,
              AnalysisOptions.EMPTY,
              Optional.of(PgArrayCodec.of(obj -> ((PGobject) obj).getValue())),
              ',')
          .transform(Vector::new, Vector::value);
  PgType<Unknown> unknown =
      new PgType<>(
              PgTypename.of("unknown"),
              PgRead.readString,
              PgWrite.pgObject("unknown"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString,
              AnalysisOptions.EMPTY,
              Optional.<PgArrayCodec<String>>empty(),
              ',')
          .transform(Unknown::new, Unknown::value);
  PgType<byte[]> bytea =
      new PgType<>(
          PgTypename.of("bytea"),
          PgRead.readByteArray,
          PgWrite.writeByteArray,
          PgText.textByteArray,
          PgCompositeText.bytea,
          PgJson.bytea,
          PgOutParam.readByteArray,
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
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.readString.map(fromString::apply),
        PgWrite.writeString.contramap(Enum::name),
        PgText.textString.contramap(Enum::name),
        PgCompositeText.text.transform(fromString::apply, Enum::name),
        PgJson.text.transform(fromString::apply, Enum::name),
        PgOutParam.readString.map(fromString::apply),
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.fromString(fromString::apply)),
        ',');
  }

  static <T> PgType<T> ofPgObject(
      String sqlType,
      SqlFunction<String, T> constructor,
      Function<T, String> extractor,
      PgJson<T> json) {
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.pgObject(sqlType).map(constructor),
        PgWrite.pgObject(sqlType).contramap(extractor),
        PgText.textString.contramap(extractor),
        PgCompositeText.text.transform(
            s -> {
              try {
                return constructor.apply(s);
              } catch (java.sql.SQLException e) {
                throw new DatabaseException(e);
              }
            },
            extractor),
        json,
        PgOutParam.pgObject(constructor),
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.pgObject(constructor)),
        ',');
  }

  // Default record type for generic composite/record columns
  PgType<Record> record = ofPgObject("record", Record::new, Record::value, PgJson.record);

  static PgType<Record> record(String sqlType) {
    return ofPgObject(sqlType, Record::new, Record::value, PgJson.record);
  }

  static <T extends PGobject> PgType<T> pgObject(String sqlType, Class<T> clazz, PgJson<T> json) {
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.castJdbcObjectTo(clazz),
        PgWrite.passObjectToJdbc(),
        PgText.textPGobject(),
        PgCompositeText.notSupported(),
        json,
        PgOutParam.castTo(clazz),
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.cast()),
        ',');
  }

  static PgType<String> bpchar(int precision) {
    return new PgType<>(
        PgTypename.of("bpchar", precision),
        PgRead.readString,
        PgWrite.writeString,
        PgText.textString,
        PgCompositeText.text,
        PgJson.text,
        PgOutParam.readString,
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.cast()),
        ',');
  }

  static <T extends Comparable<? super T>> PgType<Range<T>> rangeType(
      String sqlType,
      SqlFunction<String, T> valueParser,
      java.util.function.BiFunction<RangeBound<T>, RangeBound<T>, Range<T>> rangeFactory,
      PgJson<Range<T>> json) {
    return new PgType<>(
        PgTypename.of(sqlType),
        PgRead.pgObject(sqlType).map(str -> RangeParser.parse(str, valueParser, rangeFactory)),
        PgWrite.pgObject(sqlType).contramap(RangeParser::format),
        PgText.textString.contramap(RangeParser::format),
        PgCompositeText.of(
            RangeParser::format,
            str -> {
              try {
                return RangeParser.parse(str, valueParser, rangeFactory);
              } catch (java.sql.SQLException e) {
                throw new DatabaseException(e);
              }
            }),
        json,
        PgOutParam.pgObject(str -> RangeParser.parse(str, valueParser, rangeFactory)),
        AnalysisOptions.EMPTY,
        Optional.of(
            PgArrayCodec.of(
                obj -> {
                  try {
                    return RangeParser.parse(
                        ((PGobject) obj).getValue(), valueParser, rangeFactory);
                  } catch (java.sql.SQLException e) {
                    throw new DatabaseException(e);
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
            throw new DatabaseException(
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
                throw new DatabaseException("Failed to encode composite type", e);
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
        };

    PgCompositeText<Row> pgCompositeText =
        new PgCompositeText<>() {
          @Override
          public Optional<String> encode(Row value) {
            return Optional.of(encodeToText.apply(value));
          }

          @Override
          public Row decode(String text) {
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
        pgCompositeText,
        pgJson,
        pgOutParam,
        AnalysisOptions.EMPTY,
        Optional.of(PgArrayCodec.of(obj -> parseFromText.apply(obj.toString()))),
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
