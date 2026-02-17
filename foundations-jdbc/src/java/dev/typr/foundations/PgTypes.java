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
  PgType<AclItem[]> aclitemArray =
      aclitem.array(PgRead.pgObjectArray(AclItem::new, AclItem.class), AclItem[]::new);
  PgType<AnyArray> anyarray =
      ofPgObject(
          "anyarray",
          AnyArray::new,
          AnyArray::value,
          PgJson.text.transform(AnyArray::new, AnyArray::value));
  PgType<AnyArray[]> anyarrayArray =
      anyarray.array(PgRead.pgObjectArray(AnyArray::new, AnyArray.class), AnyArray[]::new);
  PgType<BigDecimal> numeric =
      PgType.of(
          "numeric",
          PgRead.readBigDecimal,
          PgWrite.writeBigDecimal,
          PgText.textBigDecimal,
          PgCompositeText.numeric,
          PgJson.numeric,
          PgOutParam.readBigDecimal);
  PgType<BigDecimal[]> numericArray = numeric.array(PgRead.readBigDecimalArray, BigDecimal[]::new);
  PgType<Boolean> bool =
      PgType.of(
          "bool",
          PgRead.readBoolean,
          PgWrite.writeBoolean,
          PgText.textBoolean,
          PgCompositeText.bool,
          PgJson.bool,
          PgOutParam.readBoolean);
  PgType<Boolean[]> boolArray = bool.array(PgRead.readBooleanArray, Boolean[]::new);

  @SuppressWarnings("unchecked")
  PgType<boolean[]> boolArrayUnboxed =
      PgType.of(
          (PgTypename<boolean[]>) (PgTypename<?>) PgTypename.of("bool").array(),
          PgRead.readBooleanArrayUnboxed,
          PgWrite.writeBooleanArrayUnboxed,
          PgText.boolArrayUnboxed,
          PgCompositeText.boolArrayUnboxed,
          PgJson.boolArrayUnboxed,
          PgOutParam.readBooleanArrayUnboxed);

  PgType<Bit> bit = bitType("bit");
  PgType<Bit[]> bitArray = bit.array(PgRead.bitStringArray(Bit::new, Bit.class), Bit[]::new);

  static PgType<Bit> bit(int n) {
    return PgType.of(
        PgTypename.of("bit", n),
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new));
  }

  static PgType<Bit[]> bitArray(int n) {
    return bit(n).array(PgRead.bitStringArray(Bit::new, Bit.class), Bit[]::new);
  }

  private static PgType<Bit> bitType(String sqlType) {
    return PgType.of(
        sqlType,
        PgRead.bitString.map(Bit::new),
        PgWrite.pgObject("bit").contramap(Bit::value),
        PgText.textString.contramap(Bit::value),
        PgCompositeText.text.transform(Bit::new, Bit::value),
        PgJson.bit,
        PgOutParam.bitString(Bit::new));
  }

  PgType<Varbit> varbit = ofPgObject("varbit", Varbit::new, Varbit::value, PgJson.varbit);
  PgType<Varbit[]> varbitArray =
      varbit.array(PgRead.pgObjectArray(Varbit::new, Varbit.class), Varbit[]::new);

  PgType<Double> float8 =
      PgType.of(
          "float8",
          PgRead.readDouble,
          PgWrite.writeDouble,
          PgText.textDouble,
          PgCompositeText.float8,
          PgJson.float8,
          PgOutParam.readDouble);
  PgType<Double[]> float8Array = float8.array(PgRead.readDoubleArray, Double[]::new);

  @SuppressWarnings("unchecked")
  PgType<double[]> float8ArrayUnboxed =
      PgType.of(
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
          PgOutParam.readFloat);
  PgType<Float[]> float4Array = float4.array(PgRead.readFloatArray, Float[]::new);

  @SuppressWarnings("unchecked")
  PgType<float[]> float4ArrayUnboxed =
      PgType.of(
          (PgTypename<float[]>) (PgTypename<?>) PgTypename.of("float4").array(),
          PgRead.readFloatArrayUnboxed,
          PgWrite.writeFloatArrayUnboxed,
          PgText.floatArrayUnboxed,
          PgCompositeText.floatArrayUnboxed,
          PgJson.floatArrayUnboxed,
          PgOutParam.readFloatArrayUnboxed);

  PgType<Inet> inet = ofPgObject("inet", Inet::new, Inet::value, PgJson.inet);
  PgType<Inet[]> inetArray = inet.array(PgRead.pgObjectArray(Inet::new, Inet.class), Inet[]::new);
  PgType<Cidr> cidr = ofPgObject("cidr", Cidr::new, Cidr::value, PgJson.cidr);
  PgType<Cidr[]> cidrArray = cidr.array(PgRead.pgObjectArray(Cidr::new, Cidr.class), Cidr[]::new);
  PgType<MacAddr> macaddr = ofPgObject("macaddr", MacAddr::new, MacAddr::value, PgJson.macaddr);
  PgType<MacAddr[]> macaddrArray =
      macaddr.array(PgRead.pgObjectArray(MacAddr::new, MacAddr.class), MacAddr[]::new);
  PgType<MacAddr8> macaddr8 =
      ofPgObject("macaddr8", MacAddr8::new, MacAddr8::value, PgJson.macaddr8);
  PgType<MacAddr8[]> macaddr8Array =
      macaddr8.array(PgRead.pgObjectArray(MacAddr8::new, MacAddr8.class), MacAddr8[]::new);
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
          PgOutParam.readInstant);
  PgType<Instant[]> timestamptzArray = timestamptz.array(PgRead.readInstantArray, Instant[]::new);
  PgType<Int2Vector> int2vector =
      ofPgObject("int2vector", Int2Vector::new, Int2Vector::value, PgJson.int2vector);
  PgType<Int2Vector[]> int2vectorArray =
      int2vector.array(PgRead.pgObjectArray(Int2Vector::new, Int2Vector.class), Int2Vector[]::new);
  PgType<Integer> int4 =
      PgType.of(
          "int4",
          PgRead.readInteger,
          PgWrite.writeInteger,
          PgText.textInteger,
          PgCompositeText.int4,
          PgJson.int4,
          PgOutParam.readInteger);
  PgType<Integer[]> int4Array = int4.array(PgRead.readIntegerArray, Integer[]::new);

  @SuppressWarnings("unchecked")
  PgType<int[]> int4ArrayUnboxed =
      PgType.of(
          (PgTypename<int[]>) (PgTypename<?>) PgTypename.of("int4").array(),
          PgRead.readIntArrayUnboxed,
          PgWrite.writeIntArrayUnboxed,
          PgText.intArrayUnboxed,
          PgCompositeText.intArrayUnboxed,
          PgJson.intArrayUnboxed,
          PgOutParam.readIntArrayUnboxed);

  PgType<Json> json = ofPgObject("json", Json::new, Json::value, PgJson.json);
  PgType<Json[]> jsonArray = json.array(PgRead.readJsonArray, Json[]::new);
  PgType<Jsonb> jsonb = ofPgObject("jsonb", Jsonb::new, Jsonb::value, PgJson.jsonb);
  PgType<Jsonb[]> jsonbArray = jsonb.array(PgRead.readJsonbArray, Jsonb[]::new);
  PgType<LocalDate> date =
      PgType.of(
          "date",
          PgRead.readLocalDate,
          PgWrite.passObjectToJdbc(),
          PgText.instance((d, sb) -> sb.append(d.toString())),
          PgCompositeText.of(LocalDate::toString, LocalDate::parse),
          PgJson.date,
          PgOutParam.readLocalDate);
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
          PgOutParam.readLocalDateTime);
  PgType<LocalDateTime[]> timestampArray =
      timestamp.array(PgRead.readLocalDateTimeArray, LocalDateTime[]::new);
  PgType<LocalDate[]> dateArray = date.array(PgRead.readLocalDateArray, LocalDate[]::new);
  PgType<LocalTime> time =
      PgType.of(
          "time",
          PgRead.readLocalTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.of(LocalTime::toString, LocalTime::parse),
          PgJson.time,
          PgOutParam.readLocalTime);
  PgType<LocalTime[]> timeArray = time.array(PgRead.readLocalTimeArray, LocalTime[]::new);
  PgType<Long> int8 =
      PgType.of(
          "int8",
          PgRead.readLong,
          PgWrite.writeLong,
          PgText.textLong,
          PgCompositeText.int8,
          PgJson.int8,
          PgOutParam.readLong);
  PgType<Long[]> int8Array = int8.array(PgRead.readLongArray, Long[]::new);

  @SuppressWarnings("unchecked")
  PgType<long[]> int8ArrayUnboxed =
      PgType.of(
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
          PgJson.int8.transform(Oid::new, Oid::value),
          PgOutParam.readLong.map(Oid::new));
  PgType<Oid[]> oidArray =
      oid.array(
          PgRead.readLongArray.map(
              arr -> {
                Oid[] result = new Oid[arr.length];
                for (int i = 0; i < arr.length; i++) {
                  result[i] = new Oid(arr[i]);
                }
                return result;
              }),
          Oid[]::new);

  PgType<Map<String, String>> hstore =
      PgType.of(
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
          PgOutParam.readDouble.map(Money::new));
  PgType<Money[]> moneyArray = money.array(PgRead.readMoneyArray, Money[]::new);
  PgType<String> name =
      PgType.of(
          "name",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString);
  PgType<String[]> nameArray = name.array(PgRead.readStringArray, String[]::new);
  PgType<OffsetTime> timetz =
      PgType.of(
          "timetz",
          PgRead.readOffsetTime,
          PgWrite.passObjectToJdbc(),
          PgText.instance((t, sb) -> sb.append(t.toString())),
          PgCompositeText.timetz,
          PgJson.timetz,
          PgOutParam.readOffsetTime);
  PgType<OffsetTime[]> timetzArray = timetz.array(PgRead.readOffsetTimeArray, OffsetTime[]::new);
  PgType<OidVector> oidvector =
      ofPgObject("oidvector", OidVector::new, OidVector::value, PgJson.oidvector);
  PgType<OidVector[]> oidvectorArray =
      oidvector.array(PgRead.pgObjectArray(OidVector::new, OidVector.class), OidVector[]::new);
  PgType<PGInterval> interval =
      PgType.of(
          "interval",
          PgRead.castJdbcObjectTo(PGInterval.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.interval,
          PgJson.interval,
          PgOutParam.castTo(PGInterval.class));
  PgType<PGInterval[]> intervalArray =
      interval.array(PgRead.castJdbcArrayTo(PGInterval.class), PGInterval[]::new);
  PgType<PGbox> box =
      PgType.of(
          "box",
          PgRead.castJdbcObjectTo(PGbox.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.box,
          PgJson.box,
          PgOutParam.castTo(PGbox.class));
  PgType<PGbox[]> boxArray = box.array(PgRead.castJdbcArrayTo(PGbox.class), PGbox[]::new, ';');
  PgType<PGcircle> circle =
      PgType.of(
          "circle",
          PgRead.castJdbcObjectTo(PGcircle.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.circle,
          PgJson.circle,
          PgOutParam.castTo(PGcircle.class));
  PgType<PGcircle[]> circleArray =
      circle.array(PgRead.castJdbcArrayTo(PGcircle.class), PGcircle[]::new, ';');
  PgType<PGline> line =
      PgType.of(
          "line",
          PgRead.castJdbcObjectTo(PGline.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.line,
          PgJson.line,
          PgOutParam.castTo(PGline.class));
  PgType<PGline[]> lineArray = line.array(PgRead.castJdbcArrayTo(PGline.class), PGline[]::new, ';');
  PgType<PGlseg> lseg =
      PgType.of(
          "lseg",
          PgRead.castJdbcObjectTo(PGlseg.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.lseg,
          PgJson.lseg,
          PgOutParam.castTo(PGlseg.class));
  PgType<PGlseg[]> lsegArray = lseg.array(PgRead.castJdbcArrayTo(PGlseg.class), PGlseg[]::new, ';');
  PgType<PGpath> path =
      PgType.of(
          "path",
          PgRead.castJdbcObjectTo(PGpath.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.path,
          PgJson.path,
          PgOutParam.castTo(PGpath.class));
  PgType<PGpath[]> pathArray = path.array(PgRead.castJdbcArrayTo(PGpath.class), PGpath[]::new, ';');
  PgType<PGpoint> point =
      PgType.of(
          "point",
          PgRead.castJdbcObjectTo(PGpoint.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.point,
          PgJson.point,
          PgOutParam.castTo(PGpoint.class));
  PgType<PGpoint[]> pointArray =
      point.array(PgRead.castJdbcArrayTo(PGpoint.class), PGpoint[]::new, ';');
  PgType<PGpolygon> polygon =
      PgType.of(
          "polygon",
          PgRead.castJdbcObjectTo(PGpolygon.class),
          PgWrite.passObjectToJdbc(),
          PgText.textPGobject(),
          PgCompositeText.polygon,
          PgJson.polygon,
          PgOutParam.castTo(PGpolygon.class));
  PgType<PGpolygon[]> polygonArray =
      polygon.array(PgRead.castJdbcArrayTo(PGpolygon.class), PGpolygon[]::new, ';');
  PgType<PgNodeTree> pgNodeTree =
      ofPgObject(
          "pg_node_tree",
          PgNodeTree::new,
          PgNodeTree::value,
          PgJson.text.transform(PgNodeTree::new, PgNodeTree::value));
  PgType<PgNodeTree[]> pgNodeTreeArray =
      pgNodeTree.array(PgRead.pgObjectArray(PgNodeTree::new, PgNodeTree.class), PgNodeTree[]::new);
  PgType<Regclass> regclass =
      ofPgObject("regclass", Regclass::new, Regclass::value, PgJson.regclass);
  PgType<Regclass[]> regclassArray =
      regclass.array(PgRead.pgObjectArray(Regclass::new, Regclass.class), Regclass[]::new);
  PgType<Regconfig> regconfig =
      ofPgObject("regconfig", Regconfig::new, Regconfig::value, PgJson.regconfig);
  PgType<Regconfig[]> regconfigArray =
      regconfig.array(PgRead.pgObjectArray(Regconfig::new, Regconfig.class), Regconfig[]::new);
  PgType<Regdictionary> regdictionary =
      ofPgObject("regdictionary", Regdictionary::new, Regdictionary::value, PgJson.regdictionary);
  PgType<Regdictionary[]> regdictionaryArray =
      regdictionary.array(
          PgRead.pgObjectArray(Regdictionary::new, Regdictionary.class), Regdictionary[]::new);
  PgType<Regnamespace> regnamespace =
      ofPgObject("regnamespace", Regnamespace::new, Regnamespace::value, PgJson.regnamespace);
  PgType<Regnamespace[]> regnamespaceArray =
      regnamespace.array(
          PgRead.pgObjectArray(Regnamespace::new, Regnamespace.class), Regnamespace[]::new);
  PgType<Regoper> regoper = ofPgObject("regoper", Regoper::new, Regoper::value, PgJson.regoper);
  PgType<Regoper[]> regoperArray =
      regoper.array(PgRead.pgObjectArray(Regoper::new, Regoper.class), Regoper[]::new);
  PgType<Regoperator> regoperator =
      ofPgObject("regoperator", Regoperator::new, Regoperator::value, PgJson.regoperator);
  PgType<Regoperator[]> regoperatorArray =
      regoperator.array(
          PgRead.pgObjectArray(Regoperator::new, Regoperator.class), Regoperator[]::new);
  PgType<Regproc> regproc = ofPgObject("regproc", Regproc::new, Regproc::value, PgJson.regproc);
  PgType<Regproc[]> regprocArray =
      regproc.array(PgRead.pgObjectArray(Regproc::new, Regproc.class), Regproc[]::new);
  PgType<Regprocedure> regprocedure =
      ofPgObject("regprocedure", Regprocedure::new, Regprocedure::value, PgJson.regprocedure);
  PgType<Regprocedure[]> regprocedureArray =
      regprocedure.array(
          PgRead.pgObjectArray(Regprocedure::new, Regprocedure.class), Regprocedure[]::new);
  PgType<Regrole> regrole = ofPgObject("regrole", Regrole::new, Regrole::value, PgJson.regrole);
  PgType<Regrole[]> regroleArray =
      regrole.array(PgRead.pgObjectArray(Regrole::new, Regrole.class), Regrole[]::new);
  PgType<Regtype> regtype = ofPgObject("regtype", Regtype::new, Regtype::value, PgJson.regtype);
  PgType<Regtype[]> regtypeArray =
      regtype.array(PgRead.pgObjectArray(Regtype::new, Regtype.class), Regtype[]::new);
  PgType<Short> int2 =
      PgType.of(
          "int2",
          PgRead.readShort,
          PgWrite.writeShort,
          PgText.textShort,
          PgCompositeText.int2,
          PgJson.int2,
          PgOutParam.readShort);
  PgType<Short> smallint = int2.withTypename(PgTypename.of("smallint"))
      .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames("int2"));
  PgType<Short[]> int2Array = int2.array(PgRead.readShortArray, Short[]::new);

  @SuppressWarnings("unchecked")
  PgType<short[]> int2ArrayUnboxed =
      PgType.of(
          (PgTypename<short[]>) (PgTypename<?>) PgTypename.of("int2").array(),
          PgRead.readShortArrayUnboxed,
          PgWrite.writeShortArrayUnboxed,
          PgText.shortArrayUnboxed,
          PgCompositeText.shortArrayUnboxed,
          PgJson.shortArrayUnboxed,
          PgOutParam.readShortArrayUnboxed);

  PgType<Short[]> smallintArray = int2Array.renamed("smallint")
      .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames("int2[]"));
  PgType<short[]> smallintArrayUnboxed = int2ArrayUnboxed.renamed("smallint")
      .withAnalysis(AnalysisOptions.EMPTY.withVendorTypeNames("int2[]"));
  PgType<String> bpchar =
      PgType.of(
          "bpchar",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString);
  PgType<String> text =
      PgType.of(
          "text",
          PgRead.readString,
          PgWrite.writeString,
          PgText.textString,
          PgCompositeText.text,
          PgJson.text,
          PgOutParam.readString);
  PgType<String[]> bpcharArray = bpchar.array(PgRead.readStringArray, String[]::new);
  PgType<String[]> textArray = text.array(PgRead.readStringArray, String[]::new);
  PgType<UUID> uuid =
      PgType.of(
          "uuid",
          PgRead.readUUID,
          PgWrite.writeUUID,
          PgText.textUuid,
          PgCompositeText.uuid,
          PgJson.uuid,
          PgOutParam.readUUID);
  PgType<UUID[]> uuidArray = uuid.array(PgRead.massageJdbcArrayTo(UUID[].class), UUID[]::new);
  PgType<Xid> xid = ofPgObject("xid", Xid::new, Xid::value, PgJson.xid);
  PgType<Xid[]> xidArray = xid.array(PgRead.pgObjectArray(Xid::new, Xid.class), Xid[]::new);
  PgType<Xml> xml =
      PgType.of(
              "xml",
              PgRead.readString,
              PgWrite.pgObject("xml"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString)
          .transform(Xml::new, Xml::value);
  PgType<Xml[]> xmlArray = xml.array(PgRead.pgObjectArray(Xml::new, Xml.class), Xml[]::new);
  PgType<Vector> vector =
      PgType.of(
              "vector",
              PgRead.readString,
              PgWrite.pgObject("vector"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString)
          .transform(Vector::new, Vector::value);
  PgType<Vector[]> vectorArray =
      vector.array(PgRead.pgObjectArray(Vector::new, Vector.class), Vector[]::new);
  PgType<Unknown> unknown =
      PgType.of(
              "unknown",
              PgRead.readString,
              PgWrite.pgObject("unknown"),
              PgText.textString,
              PgCompositeText.text,
              PgJson.text,
              PgOutParam.readString)
          .transform(Unknown::new, Unknown::value);
  PgType<Unknown[]> unknownArray =
      unknown.array(PgRead.pgObjectArray(Unknown::new, Unknown.class), Unknown[]::new);
  PgType<byte[]> bytea =
      PgType.of(
          "bytea",
          PgRead.readByteArray,
          PgWrite.writeByteArray,
          PgText.textByteArray,
          PgCompositeText.bytea,
          PgJson.bytea,
          PgOutParam.readByteArray);

  // Range types - discrete types (int, date) are normalized to canonical [) form via Range factory
  // methods
  PgType<Range<Integer>> int4range =
      rangeType("int4range", RangeParser.INT4_PARSER, Range.INT4, PgJson.int4range);
  PgType<Range<Integer>[]> int4rangeArray =
      int4range.array(rangeArrayRead(RangeParser.INT4_PARSER, Range.INT4), rangeArrayFactory());
  PgType<Range<Long>> int8range =
      rangeType("int8range", RangeParser.INT8_PARSER, Range.INT8, PgJson.int8range);
  PgType<Range<Long>[]> int8rangeArray =
      int8range.array(rangeArrayRead(RangeParser.INT8_PARSER, Range.INT8), rangeArrayFactory());
  PgType<Range<BigDecimal>> numrange =
      rangeType("numrange", RangeParser.NUMERIC_PARSER, Range.NUMERIC, PgJson.numrange);
  PgType<Range<BigDecimal>[]> numrangeArray =
      numrange.array(
          rangeArrayRead(RangeParser.NUMERIC_PARSER, Range.NUMERIC), rangeArrayFactory());
  PgType<Range<LocalDate>> daterange =
      rangeType("daterange", RangeParser.DATE_PARSER, Range.DATE, PgJson.daterange);
  PgType<Range<LocalDate>[]> daterangeArray =
      daterange.array(rangeArrayRead(RangeParser.DATE_PARSER, Range.DATE), rangeArrayFactory());
  PgType<Range<LocalDateTime>> tsrange =
      rangeType("tsrange", RangeParser.TIMESTAMP_PARSER, Range.TIMESTAMP, PgJson.tsrange);
  PgType<Range<LocalDateTime>[]> tsrangeArray =
      tsrange.array(
          rangeArrayRead(RangeParser.TIMESTAMP_PARSER, Range.TIMESTAMP), rangeArrayFactory());
  PgType<Range<Instant>> tstzrange =
      rangeType("tstzrange", RangeParser.TIMESTAMPTZ_PARSER, Range.TIMESTAMPTZ, PgJson.tstzrange);
  PgType<Range<Instant>[]> tstzrangeArray =
      tstzrange.array(
          rangeArrayRead(RangeParser.TIMESTAMPTZ_PARSER, Range.TIMESTAMPTZ), rangeArrayFactory());

  static <E extends Enum<E>> PgType<E> ofEnum(String sqlType, Function<String, E> fromString) {
    return PgType.of(
        sqlType,
        PgRead.readString.map(fromString::apply),
        PgWrite.writeString.contramap(Enum::name),
        PgText.textString.contramap(Enum::name),
        PgCompositeText.text.transform(fromString::apply, Enum::name),
        PgJson.text.transform(fromString::apply, Enum::name),
        PgOutParam.readString.map(fromString::apply));
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
                throw new RuntimeException(e);
              }
            },
            extractor),
        json,
        PgOutParam.pgObject(constructor));
  }

  // Default record type for generic composite/record columns
  PgType<Record> record = ofPgObject("record", Record::new, Record::value, PgJson.record);
  PgType<Record[]> recordArray =
      record.array(PgRead.pgObjectArray(Record::new, Record.class), Record[]::new);

  static PgType<Record> record(String sqlType) {
    return ofPgObject(sqlType, Record::new, Record::value, PgJson.record);
  }

  static PgType<Record[]> recordArray(String sqlType) {
    return record(sqlType).array(PgRead.pgObjectArray(Record::new, Record.class), Record[]::new);
  }

  static <T extends PGobject> PgType<T> pgObject(String sqlType, Class<T> clazz, PgJson<T> json) {
    return PgType.of(
        sqlType,
        PgRead.castJdbcObjectTo(clazz),
        PgWrite.passObjectToJdbc(),
        PgText.textPGobject(),
        PgCompositeText.notSupported(),
        json,
        PgOutParam.castTo(clazz));
  }

  static PgType<String> bpchar(int precision) {
    return PgType.of(
        PgTypename.of("bpchar", precision),
        PgRead.readString,
        PgWrite.writeString,
        PgText.textString,
        PgCompositeText.text,
        PgJson.text,
        PgOutParam.readString);
  }

  static PgType<String[]> bpcharArray(int n) {
    return bpchar(n).array(PgRead.readStringArray, String[]::new);
  }

  // Range type helpers
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
                throw new RuntimeException(e);
              }
            }),
        json,
        PgOutParam.pgObject(str -> RangeParser.parse(str, valueParser, rangeFactory)));
  }

  @SuppressWarnings("unchecked")
  static <T extends Comparable<? super T>> PgRead<Range<T>[]> rangeArrayRead(
      SqlFunction<String, T> valueParser,
      java.util.function.BiFunction<RangeBound<T>, RangeBound<T>, Range<T>> rangeFactory) {
    return PgRead.readPgArray.map(
        sqlArray -> {
          Object[] objects = (Object[]) sqlArray.getArray();
          Range<T>[] result =
              (Range<T>[]) java.lang.reflect.Array.newInstance(Range.class, objects.length);
          for (int i = 0; i < objects.length; i++) {
            var pgObj = (org.postgresql.util.PGobject) objects[i];
            result[i] = RangeParser.parse(pgObj.getValue(), valueParser, rangeFactory);
          }
          return result;
        });
  }

  @SuppressWarnings("unchecked")
  static <T extends Comparable<? super T>>
      java.util.function.IntFunction<Range<T>[]> rangeArrayFactory() {
    return n -> (Range<T>[]) java.lang.reflect.Array.newInstance(Range.class, n);
  }

  // ==================== JSON-Encoded Row Types ====================
  //
  // These methods create JSON column types from a RowParser. The row's fields are serialized
  // into JSON when writing and deserialized back when reading — the JSON column stores the
  // complete row structure.
  //
  // "Array encoded" means each row becomes a positional JSON array: [val1, val2, val3]
  // "Object encoded" means each row becomes a keyed JSON object: {"col1": val1, "col2": val2}

  // ── json ──

  /** A {@code json} column type that stores a single row as a positional JSON array. */
  static <Row> PgType<Row> jsonArrayEncoded(RowParser<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonArray(parser);
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Json(codec.toJson(row).encode()));
  }

  /** A {@code json} column type that stores a list of rows, each as a positional JSON array. */
  static <Row> PgType<List<Row>> jsonArrayEncodedList(RowParser<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonArray(parser).list();
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Json(codec.toJson(list).encode()));
  }

  /** A {@code json} column type that stores a single row as a keyed JSON object. */
  static <Row> PgType<Row> jsonObjectEncoded(RowParserNamed<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonObject(parser);
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Json(codec.toJson(row).encode()));
  }

  /** A {@code json} column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> PgType<List<Row>> jsonObjectEncodedList(RowParserNamed<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonObject(parser).list();
    return json.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Json(codec.toJson(list).encode()));
  }

  // ── jsonb ──

  /** A {@code jsonb} column type that stores a single row as a positional JSON array. */
  static <Row> PgType<Row> jsonbArrayEncoded(RowParser<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonArray(parser);
    return jsonb.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Jsonb(codec.toJson(row).encode()));
  }

  /** A {@code jsonb} column type that stores a list of rows, each as a positional JSON array. */
  static <Row> PgType<List<Row>> jsonbArrayEncodedList(RowParser<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonArray(parser).list();
    return jsonb.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Jsonb(codec.toJson(list).encode()));
  }

  /** A {@code jsonb} column type that stores a single row as a keyed JSON object. */
  static <Row> PgType<Row> jsonbObjectEncoded(RowParserNamed<Row> parser) {
    DbJson<Row> codec = DbJsonRow.jsonObject(parser);
    return jsonb.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        row -> new Jsonb(codec.toJson(row).encode()));
  }

  /** A {@code jsonb} column type that stores a list of rows, each as a keyed JSON object. */
  static <Row> PgType<List<Row>> jsonbObjectEncodedList(RowParserNamed<Row> parser) {
    DbJson<List<Row>> codec = DbJsonRow.jsonObject(parser).list();
    return jsonb.transform(
        j -> codec.fromJson(JsonValue.parse(j.value())),
        list -> new Jsonb(codec.toJson(list).encode()));
  }
}
