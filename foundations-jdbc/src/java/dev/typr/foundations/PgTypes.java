package dev.typr.foundations;

import dev.typr.foundations.data.*;
import dev.typr.foundations.data.Record;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
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
      PgType.of(
          "numeric",
          PgRead.readBigDecimal,
          PgWrite.writeBigDecimal,
          PgText.textBigDecimal,
          PgCompositeText.numeric,
          PgJson.numeric,
          PgOutParam.readBigDecimal,
          PgArrayCodec.cast());
  PgType<Boolean> bool =
      PgType.of(
          "bool",
          PgRead.readBoolean,
          PgWrite.writeBoolean,
          PgText.textBoolean,
          PgCompositeText.bool,
          PgJson.bool,
          PgOutParam.readBoolean,
          PgArrayCodec.cast());

  @SuppressWarnings("unchecked")
  PgType<boolean[]> boolArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<boolean[]>) (PgTypename<?>) PgTypename.of("bool").array(),
          PgRead.readBooleanArrayUnboxed,
          PgWrite.writeBooleanArrayUnboxed,
          PgText.boolArrayUnboxed,
          PgCompositeText.boolArrayUnboxed,
          PgJson.boolArrayUnboxed,
          PgOutParam.readBooleanArrayUnboxed);

  PgType<Bit> bit = bitType("bit");

  static PgType<Bit> bit(int n) {
    return PgType.of(
        PgTypename.of("bit", n),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        PgArrayCodec.textParsed());
  }

  private static PgType<Bit> bitType(String sqlType) {
    return PgType.of(
        sqlType,
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new),
        PgArrayCodec.textParsed());
  }

  PgType<Varbit> varbit = ofPgObject("varbit", Varbit::new, Varbit::value, PgJson.varbit);

  PgType<Double> float8 =
      PgType.of(
          "float8",
          PgRead.readDouble,
          PgWrite.writeDouble,
          PgText.textDouble,
          PgCompositeText.float8,
          PgJson.float8,
          PgOutParam.readDouble,
          PgArrayCodec.cast());

  @SuppressWarnings("unchecked")
  PgType<double[]> float8ArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<double[]>) (PgTypename<?>) PgTypename.of("float8").array(),
          PgRead.readDoubleArrayUnboxed,
          PgWrite.writeDoubleArrayUnboxed,
          PgText.doubleArrayUnboxed,
          PgCompositeText.doubleArrayUnboxed,
          PgJson.doubleArrayUnboxed,
          PgOutParam.readDoubleArrayUnboxed);

  PgType<Float> float4 =
      PgType.of(
          "float4",
          PgRead.readFloat,
          PgWrite.writeFloat,
          PgText.textFloat,
          PgCompositeText.float4,
          PgJson.float4,
          PgOutParam.readFloat,
          PgArrayCodec.cast());

  @SuppressWarnings("unchecked")
  PgType<float[]> float4ArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<float[]>) (PgTypename<?>) PgTypename.of("float4").array(),
          PgRead.readFloatArrayUnboxed,
          PgWrite.writeFloatArrayUnboxed,
          PgText.floatArrayUnboxed,
          PgCompositeText.floatArrayUnboxed,
          PgJson.floatArrayUnboxed,
          PgOutParam.readFloatArrayUnboxed);

  PgType<Inet> inet = ofPgObject("inet", Inet::new, Inet::value, PgJson.inet);
  PgType<Cidr> cidr = ofPgObject("cidr", Cidr::new, Cidr::value, PgJson.cidr);
  PgType<MacAddr> macaddr = ofPgObject("macaddr", MacAddr::new, MacAddr::value, PgJson.macaddr);
  PgType<MacAddr8> macaddr8 =
      ofPgObject("macaddr8", MacAddr8::new, MacAddr8::value, PgJson.macaddr8);
  PgType<Instant> timestamptz =
      PgType.of(
          "timestamptz",
          PgRead.readInstant,
          PgWrite.primitive((ps, i, v) -> ps.setObject(i, v.atOffset(ZoneOffset.UTC))),
          PgText.instance(
              (t, sb) -> sb.append(t.atOffset(ZoneOffset.UTC).toString().replace('T', ' '))),
          PgCompositeText.of(
              t -> t.atOffset(ZoneOffset.UTC).toString().replace('T', ' '),
              text -> OffsetDateTime.parse(text.replace(' ', 'T')).toInstant()),
          PgJson.timestamptz,
          PgOutParam.readInstant,
          PgArrayCodec.of(obj -> ((java.sql.Timestamp) obj).toInstant()));
  PgType<Int2Vector> int2vector =
      ofPgObject("int2vector", Int2Vector::new, Int2Vector::value, PgJson.int2vector);
  PgType<Integer> int4 =
      PgType.of(
              "int4",
              PgRead.readInteger,
              PgWrite.writeInteger,
              PgText.textInteger,
              PgCompositeText.int4,
              PgJson.int4,
              PgOutParam.readInteger,
              PgArrayCodec.cast())
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("serial")));

  @SuppressWarnings("unchecked")
  PgType<int[]> int4ArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<int[]>) (PgTypename<?>) PgTypename.of("int4").array(),
          PgRead.readIntArrayUnboxed,
          PgWrite.writeIntArrayUnboxed,
          PgText.intArrayUnboxed,
          PgCompositeText.intArrayUnboxed,
          PgJson.intArrayUnboxed,
          PgOutParam.readIntArrayUnboxed);

  PgType<Json> json =
      ofPgObject("json", Json::new, Json::value, PgJson.json)
          .withArrayCodec(PgArrayCodec.fromString(Json::new));
  PgType<Jsonb> jsonb =
      ofPgObject("jsonb", Jsonb::new, Jsonb::value, PgJson.jsonb)
          .withArrayCodec(PgArrayCodec.fromString(Jsonb::new));
  PgType<LocalDate> date =
      PgType.of(
          "date",
          PgRead.readLocalDate,
          PgWrite.passObjectToJdbc(),
          PgText.instance((d, sb) -> sb.append(d.toString())),
          PgCompositeText.of(LocalDate::toString, LocalDate::parse),
          PgJson.date,
          PgOutParam.readLocalDate,
          PgArrayCodec.of(obj -> ((java.sql.Date) obj).toLocalDate()));
  PgType<LocalDateTime> timestamp =
      PgType.of(
          "timestamp",
          PgRead.readLocalDateTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString().replace('T', ' '))),
          PgCompositeText.of(
              t -> t.toString().replace('T', ' '),
              text -> LocalDateTime.parse(text.replace(' ', 'T'))),
          PgJson.timestamp,
          PgOutParam.readLocalDateTime,
          PgArrayCodec.of(obj -> ((java.sql.Timestamp) obj).toLocalDateTime()));
  PgType<LocalTime> time =
      PgType.of(
          "time",
          PgRead.readLocalTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.of(LocalTime::toString, LocalTime::parse),
          PgJson.time,
          PgOutParam.readLocalTime,
          PgArrayCodec.textParsed());
  PgType<Long> int8 =
      PgType.of(
              "int8",
              PgRead.readLong,
              PgWrite.writeLong,
              PgText.textLong,
              PgCompositeText.int8,
              PgJson.int8,
              PgOutParam.readLong,
              PgArrayCodec.cast())
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("bigserial")));

  @SuppressWarnings("unchecked")
  PgType<long[]> int8ArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<long[]>) (PgTypename<?>) PgTypename.of("int8").array(),
          PgRead.readLongArrayUnboxed,
          PgWrite.writeLongArrayUnboxed,
          PgText.longArrayUnboxed,
          PgCompositeText.longArrayUnboxed,
          PgJson.longArrayUnboxed,
          PgOutParam.readLongArrayUnboxed);

  PgType<Oid> oid =
      PgType.of(
          "oid",
          PgRead.readLong.map(Oid::new),
          PgWrite.writeLong.contramap(Oid::value),
          PgText.instance((o, sb) -> sb.append(o.value())),
          PgCompositeText.int8.transform(Oid::new, Oid::value),
          PgJson.text.transform(s -> new Oid(Long.parseLong(s)), o -> Long.toString(o.value())),
          PgOutParam.readLong.map(Oid::new),
          PgArrayCodec.of(obj -> new Oid(((Number) obj).longValue())));

  PgType<Map<String, String>> hstore =
      PgType.noArraySupport(
          "hstore",
          PgRead.readMapStringString,
          PgWrite.passObjectToJdbc(),
          PgText.textMapStringString,
          PgCompositeText.hstore,
          PgJson.hstore,
          PgOutParam.readMapStringString);
  PgType<Money> money =
      PgType.of(
          "money",
          PgRead.readDouble.map(Money::new),
          PgWrite.pgObject("money").contramap(m -> String.valueOf(m.value())),
          PgText.textDouble.contramap(Money::value),
          PgCompositeText.money,
          PgJson.money,
          PgOutParam.readDouble.map(Money::new),
          PgArrayCodec.textParsed());
  PgType<String> name =
      PgType.of(
          "name",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          PgArrayCodec.cast());
  PgType<OffsetTime> timetz =
      PgType.of(
          "timetz",
          PgRead.readOffsetTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.timetz,
          PgJson.timetz,
          PgOutParam.readOffsetTime,
          PgArrayCodec.textParsed());
  PgType<OidVector> oidvector =
      ofPgObject("oidvector", OidVector::new, OidVector::value, PgJson.oidvector);
  PgType<PGInterval> interval =
      PgType.of(
          "interval",
          PgRead.castJdbcObjectTo(PGInterval.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.interval,
          PgJson.interval,
          PgOutParam.castTo(PGInterval.class),
          PgArrayCodec.cast());
  PgType<PGbox> box =
      PgType.of(
          "box",
          PgRead.castJdbcObjectTo(PGbox.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.box,
          PgJson.box,
          PgOutParam.castTo(PGbox.class),
          PgArrayCodec.<PGbox>cast().withDelimiter(';'));
  PgType<PGcircle> circle =
      PgType.of(
          "circle",
          PgRead.castJdbcObjectTo(PGcircle.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.circle,
          PgJson.circle,
          PgOutParam.castTo(PGcircle.class),
          PgArrayCodec.<PGcircle>cast().withDelimiter(';'));
  PgType<PGline> line =
      PgType.of(
          "line",
          PgRead.castJdbcObjectTo(PGline.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.line,
          PgJson.line,
          PgOutParam.castTo(PGline.class),
          PgArrayCodec.<PGline>cast().withDelimiter(';'));
  PgType<PGlseg> lseg =
      PgType.of(
          "lseg",
          PgRead.castJdbcObjectTo(PGlseg.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.lseg,
          PgJson.lseg,
          PgOutParam.castTo(PGlseg.class),
          PgArrayCodec.<PGlseg>cast().withDelimiter(';'));
  PgType<PGpath> path =
      PgType.of(
          "path",
          PgRead.castJdbcObjectTo(PGpath.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.path,
          PgJson.path,
          PgOutParam.castTo(PGpath.class),
          PgArrayCodec.<PGpath>cast().withDelimiter(';'));
  PgType<PGpoint> point =
      PgType.of(
          "point",
          PgRead.castJdbcObjectTo(PGpoint.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.point,
          PgJson.point,
          PgOutParam.castTo(PGpoint.class),
          PgArrayCodec.<PGpoint>cast().withDelimiter(';'));
  PgType<PGpolygon> polygon =
      PgType.of(
          "polygon",
          PgRead.castJdbcObjectTo(PGpolygon.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.polygon,
          PgJson.polygon,
          PgOutParam.castTo(PGpolygon.class),
          PgArrayCodec.<PGpolygon>cast().withDelimiter(';'));
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
      PgType.of(
          "int2",
          PgRead.readShort,
          PgWrite.writeShort,
          PgText.textShort,
          PgCompositeText.int2,
          PgJson.int2,
          PgOutParam.readShort,
          PgArrayCodec.cast());
  PgType<Short> smallint =
      int2.withTypename(PgTypename.of("smallint"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));
  PgType<Short> smallserial =
      int2.withTypename(PgTypename.of("smallserial"))
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(PgTypename.of("int2")));

  @SuppressWarnings("unchecked")
  PgType<short[]> int2ArrayUnboxed =
      PgType.noArraySupport(
          (PgTypename<short[]>) (PgTypename<?>) PgTypename.of("int2").array(),
          PgRead.readShortArrayUnboxed,
          PgWrite.writeShortArrayUnboxed,
          PgText.shortArrayUnboxed,
          PgCompositeText.shortArrayUnboxed,
          PgJson.shortArrayUnboxed,
          PgOutParam.readShortArrayUnboxed);

  @SuppressWarnings("unchecked")
  PgType<short[]> smallintArrayUnboxed =
      int2ArrayUnboxed
          .renamed("smallint")
          .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames(
              (PgTypename<short[]>) (PgTypename<?>) PgTypename.of("int2").array()));
  PgType<String> bpchar =
      PgType.of(
          "bpchar",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          PgArrayCodec.cast());
  PgType<String> text =
      PgType.of(
          "text",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString,
          PgArrayCodec.cast());
  PgType<UUID> uuid =
      PgType.of(
          "uuid",
          PgRead.readUUID,
          PgWrite.writeUUID,
          PgText.textUuid,
          PgCompositeText.uuid,
          PgJson.uuid,
          PgOutParam.readUUID,
          PgArrayCodec.cast());
  PgType<Xid> xid = ofPgObject("xid", Xid::new, Xid::value, PgJson.xid);
  PgType<Xml> xml =
      PgType.of(
              "xml",
              PgRead.readString,
              PgWrite.pgObject("xml"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString,
              PgArrayCodec.of(obj -> ((PGobject) obj).getValue()))
          .transform(Xml::new, Xml::value);
  PgType<Vector> vector =
      PgType.of(
              "vector",
              PgRead.readString,
              PgWrite.pgObject("vector"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString,
              PgArrayCodec.of(obj -> ((PGobject) obj).getValue()))
          .transform(Vector::new, Vector::value);
  PgType<Unknown> unknown =
      PgType.noArraySupport(
              "unknown",
              PgRead.readString,
              PgWrite.pgObject("unknown"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString)
          .transform(Unknown::new, Unknown::value);
  PgType<byte[]> bytea =
      PgType.noArraySupport(
          "bytea",
          PgRead.readByteArray,
          PgWrite.writeByteArray,
          PgText.textByteArray,
          PgCompositeText.bytea,
          PgJson.bytea,
          PgOutParam.readByteArray);

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
    return PgType.of(
        sqlType,
        PgRead.readString.map(fromString::apply),
        PgWrite.writeString.contramap(Enum::name),
        PgText.textString.contramap(Enum::name),
        PgCompositeText.text.transform(fromString::apply, Enum::name),
        PgJson.text.transform(fromString::apply, Enum::name),
        PgOutParam.readString.map(fromString::apply),
        PgArrayCodec.fromString(fromString::apply));
  }

  static <T> PgType<T> ofPgObject(
      String sqlType,
      SqlFunction<String, T> constructor,
      Function<T, String> extractor,
      PgJson<T> json) {
    return PgType.of(
        sqlType,
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
        PgArrayCodec.pgObject(constructor));
  }

  // Default record type for generic composite/record columns
  PgType<Record> record = ofPgObject("record", Record::new, Record::value, PgJson.record);

  static PgType<Record> record(String sqlType) {
    return ofPgObject(sqlType, Record::new, Record::value, PgJson.record);
  }

  static <T extends PGobject> PgType<T> pgObject(String sqlType, Class<T> clazz, PgJson<T> json) {
    return PgType.of(
        sqlType,
        PgRead.castJdbcObjectTo(clazz),
        PgWrite.passObjectToJdbc(),
        PgText.textPGobject(),
        PgCompositeText.notSupported(),
        json,
        PgOutParam.castTo(clazz),
        PgArrayCodec.cast());
  }

  static PgType<String> bpchar(int precision) {
    return PgType.of(
        PgTypename.of("bpchar", precision),
        PgRead.readString,
        PgWrite.writeString,
        PgText.textString,
        PgCompositeText.text,
        PgJson.text,
        PgOutParam.readString,
        PgArrayCodec.cast());
  }

  static <T extends Comparable<? super T>> PgType<Range<T>> rangeType(
      String sqlType,
      SqlFunction<String, T> valueParser,
      java.util.function.BiFunction<RangeBound<T>, RangeBound<T>, Range<T>> rangeFactory,
      PgJson<Range<T>> json) {
    return PgType.of(
        sqlType,
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
        PgArrayCodec.of(obj -> {
          try {
            return RangeParser.parse(((PGobject) obj).getValue(), valueParser, rangeFactory);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException(e);
          }
        }));
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
