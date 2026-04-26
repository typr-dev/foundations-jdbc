package dev.typr.foundationssc

package object data:
  // Re-export data types
  type AclItem = dev.typr.foundations.data.AclItem
  object AclItem:
    def apply(value: String): AclItem = new dev.typr.foundations.data.AclItem(value)
  type AnyArray = dev.typr.foundations.data.AnyArray
  type Arr[T] = dev.typr.foundations.data.Arr[T]
  type Cidr = dev.typr.foundations.data.Cidr
  object Cidr:
    def apply(value: String): Cidr = new dev.typr.foundations.data.Cidr(value)
  type HierarchyId = dev.typr.foundations.data.HierarchyId
  object HierarchyId:
    def parse(value: String): HierarchyId = dev.typr.foundations.data.HierarchyId.parse(value)
  type Inet = dev.typr.foundations.data.Inet
  object Inet:
    def apply(value: String): Inet = new dev.typr.foundations.data.Inet(value)
  type Int2Vector = dev.typr.foundations.data.Int2Vector
  type Json = dev.typr.foundations.data.Json
  object Json:
    def apply(value: String): Json = new dev.typr.foundations.data.Json(value)
  type JsonValue = dev.typr.foundations.data.JsonValue
  object JsonValue:
    def parse(json: String): JsonValue = dev.typr.foundations.data.JsonValue.parse(json)
  type Jsonb = dev.typr.foundations.data.Jsonb
  object Jsonb:
    def apply(value: String): Jsonb = new dev.typr.foundations.data.Jsonb(value)
  type MacAddr = dev.typr.foundations.data.MacAddr
  object MacAddr:
    def apply(value: String): MacAddr = new dev.typr.foundations.data.MacAddr(value)
  type MacAddr8 = dev.typr.foundations.data.MacAddr8
  object MacAddr8:
    def apply(value: String): MacAddr8 = new dev.typr.foundations.data.MacAddr8(value)
  type Money = dev.typr.foundations.data.Money
  object Money:
    def apply(value: Double): Money = new dev.typr.foundations.data.Money(value)
  type Oid = dev.typr.foundations.data.Oid
  object Oid:
    def apply(value: Long): Oid = new dev.typr.foundations.data.Oid(value)
    def parse(value: String): Oid = dev.typr.foundations.data.Oid.parse(value)
  type OidVector = dev.typr.foundations.data.OidVector
  type OracleIntervalDS = dev.typr.foundations.data.OracleIntervalDS
  object OracleIntervalDS:
    def parse(s: String): OracleIntervalDS = dev.typr.foundations.data.OracleIntervalDS.parse(s)
  type OracleIntervalYM = dev.typr.foundations.data.OracleIntervalYM
  object OracleIntervalYM:
    def parse(s: String): OracleIntervalYM = dev.typr.foundations.data.OracleIntervalYM.parse(s)
  type PgName = dev.typr.foundations.data.PgName
  object PgName:
    def apply(value: String): PgName = new dev.typr.foundations.data.PgName(value)
  type PgNodeTree = dev.typr.foundations.data.PgNodeTree
  object PgNodeTree:
    def apply(value: String): PgNodeTree = new dev.typr.foundations.data.PgNodeTree(value)
  type Range[T <: Comparable[? >: T]] = dev.typr.foundations.data.Range[T]
  object Range:
    def empty[T <: Comparable[? >: T]](): dev.typr.foundations.data.Range[T] =
      dev.typr.foundations.data.Range.empty()
    def int4(from: dev.typr.foundations.data.RangeBound[Integer], to: dev.typr.foundations.data.RangeBound[Integer]): dev.typr.foundations.data.Range[Integer] =
      dev.typr.foundations.data.Range.int4(from, to)
    def int8(
        from: dev.typr.foundations.data.RangeBound[java.lang.Long],
        to: dev.typr.foundations.data.RangeBound[java.lang.Long]
    ): dev.typr.foundations.data.Range[java.lang.Long] =
      dev.typr.foundations.data.Range.int8(from, to)
    def date(
        from: dev.typr.foundations.data.RangeBound[java.time.LocalDate],
        to: dev.typr.foundations.data.RangeBound[java.time.LocalDate]
    ): dev.typr.foundations.data.Range[java.time.LocalDate] =
      dev.typr.foundations.data.Range.date(from, to)
    def numeric(
        from: dev.typr.foundations.data.RangeBound[java.math.BigDecimal],
        to: dev.typr.foundations.data.RangeBound[java.math.BigDecimal]
    ): dev.typr.foundations.data.Range[java.math.BigDecimal] =
      dev.typr.foundations.data.Range.numeric(from, to)
    def timestamp(
        from: dev.typr.foundations.data.RangeBound[java.time.LocalDateTime],
        to: dev.typr.foundations.data.RangeBound[java.time.LocalDateTime]
    ): dev.typr.foundations.data.Range[java.time.LocalDateTime] =
      dev.typr.foundations.data.Range.timestamp(from, to)
    def timestamptz(
        from: dev.typr.foundations.data.RangeBound[java.time.Instant],
        to: dev.typr.foundations.data.RangeBound[java.time.Instant]
    ): dev.typr.foundations.data.Range[java.time.Instant] =
      dev.typr.foundations.data.Range.timestamptz(from, to)
  type RangeBound[T <: Comparable[? >: T]] = dev.typr.foundations.data.RangeBound[T]
  object RangeBound:
    def infinite[T <: Comparable[? >: T]](): dev.typr.foundations.data.RangeBound[T] =
      dev.typr.foundations.data.RangeBound.infinite()
    type Infinite[T] = dev.typr.foundations.data.RangeBound.Infinite[T]
    type Finite[T] = dev.typr.foundations.data.RangeBound.Finite[T]
    type Open[T] = dev.typr.foundations.data.RangeBound.Open[T]
    type Closed[T] = dev.typr.foundations.data.RangeBound.Closed[T]
  type RangeFinite[T <: Comparable[? >: T]] = dev.typr.foundations.data.RangeFinite[T]
  type RangeParser = dev.typr.foundations.data.RangeParser
  type Record = dev.typr.foundations.data.Record
  type Regclass = dev.typr.foundations.data.Regclass
  object Regclass:
    def apply(value: String): Regclass = new dev.typr.foundations.data.Regclass(value)
  type Regconfig = dev.typr.foundations.data.Regconfig
  object Regconfig:
    def apply(value: String): Regconfig = new dev.typr.foundations.data.Regconfig(value)
  type Regdictionary = dev.typr.foundations.data.Regdictionary
  object Regdictionary:
    def apply(value: String): Regdictionary = new dev.typr.foundations.data.Regdictionary(value)
  type Regnamespace = dev.typr.foundations.data.Regnamespace
  object Regnamespace:
    def apply(value: String): Regnamespace = new dev.typr.foundations.data.Regnamespace(value)
  type Regoper = dev.typr.foundations.data.Regoper
  object Regoper:
    def apply(value: String): Regoper = new dev.typr.foundations.data.Regoper(value)
  type Regoperator = dev.typr.foundations.data.Regoperator
  object Regoperator:
    def apply(value: String): Regoperator = new dev.typr.foundations.data.Regoperator(value)
  type Regproc = dev.typr.foundations.data.Regproc
  object Regproc:
    def apply(value: String): Regproc = new dev.typr.foundations.data.Regproc(value)
  type Regprocedure = dev.typr.foundations.data.Regprocedure
  object Regprocedure:
    def apply(value: String): Regprocedure = new dev.typr.foundations.data.Regprocedure(value)
  type Regrole = dev.typr.foundations.data.Regrole
  object Regrole:
    def apply(value: String): Regrole = new dev.typr.foundations.data.Regrole(value)
  type Regtype = dev.typr.foundations.data.Regtype
  object Regtype:
    def apply(value: String): Regtype = new dev.typr.foundations.data.Regtype(value)
  type Uint1 = dev.typr.foundations.data.Uint1
  object Uint1:
    def of(value: Int): Uint1 = dev.typr.foundations.data.Uint1.of(value)
  type Uint2 = dev.typr.foundations.data.Uint2
  object Uint2:
    def of(value: Int): Uint2 = dev.typr.foundations.data.Uint2.of(value)
  type Uint4 = dev.typr.foundations.data.Uint4
  object Uint4:
    def of(value: Long): Uint4 = dev.typr.foundations.data.Uint4.of(value)
  type Uint8 = dev.typr.foundations.data.Uint8
  object Uint8:
    def of(value: Long): Uint8 = dev.typr.foundations.data.Uint8.of(value)
    def of(value: java.math.BigInteger): Uint8 = dev.typr.foundations.data.Uint8.of(value)
  type Unknown = dev.typr.foundations.data.Unknown
  object Unknown:
    def apply(value: String): Unknown = new dev.typr.foundations.data.Unknown(value)
  type Vector = dev.typr.foundations.data.Vector
  type Xid = dev.typr.foundations.data.Xid
  object Xid:
    def apply(value: String): Xid = new dev.typr.foundations.data.Xid(value)
  type Bit = dev.typr.foundations.data.Bit
  object Bit:
    def apply(value: String): Bit = new dev.typr.foundations.data.Bit(value)
  type Varbit = dev.typr.foundations.data.Varbit
  object Varbit:
    def apply(value: String): Varbit = new dev.typr.foundations.data.Varbit(value)
  type Xml = dev.typr.foundations.data.Xml
  object Xml:
    def apply(value: String): Xml = new dev.typr.foundations.data.Xml(value)
  type NonEmptyBlob = dev.typr.foundations.data.NonEmptyBlob
  object NonEmptyBlob:
    def apply(value: Array[Byte]): Option[NonEmptyBlob] =
      { import _root_.scala.jdk.OptionConverters.*; dev.typr.foundations.data.NonEmptyBlob.apply(value).toScala }
    def force(value: Array[Byte]): NonEmptyBlob = dev.typr.foundations.data.NonEmptyBlob.force(value)
  type NonEmptyString = dev.typr.foundations.data.NonEmptyString
  object NonEmptyString:
    def apply(value: String): Option[NonEmptyString] =
      { import _root_.scala.jdk.OptionConverters.*; dev.typr.foundations.data.NonEmptyString.apply(value).toScala }
    def force(value: String): NonEmptyString = dev.typr.foundations.data.NonEmptyString.force(value)
  type PaddedString = dev.typr.foundations.data.PaddedString
  object PaddedString:
    def apply(value: String, length: Int): Option[PaddedString] =
      { import _root_.scala.jdk.OptionConverters.*; dev.typr.foundations.data.PaddedString.apply(value, length).toScala }
    def force(value: String, length: Int): PaddedString = dev.typr.foundations.data.PaddedString.force(value, length)

  // MariaDB data types
  type Inet4 = dev.typr.foundations.data.maria.Inet4
  object Inet4:
    def parse(value: String): Inet4 = dev.typr.foundations.data.maria.Inet4.parse(value)
    def fromBytes(bytes: Array[Byte]): Inet4 = dev.typr.foundations.data.maria.Inet4.fromBytes(bytes)
  type Inet6 = dev.typr.foundations.data.maria.Inet6
  object Inet6:
    def parse(value: String): Inet6 = dev.typr.foundations.data.maria.Inet6.parse(value)
    def fromIPv4(ipv4: Inet4): Inet6 = dev.typr.foundations.data.maria.Inet6.fromIPv4(ipv4)
  type MariaSet = dev.typr.foundations.data.maria.MariaSet
  object MariaSet:
    def fromString(commaSeparated: String): MariaSet = dev.typr.foundations.data.maria.MariaSet.fromString(commaSeparated)
    def of(values: String*): MariaSet = dev.typr.foundations.data.maria.MariaSet.of(values*)
    def of(values: java.util.Set[String]): MariaSet = dev.typr.foundations.data.maria.MariaSet.of(values)
    def empty(): MariaSet = dev.typr.foundations.data.maria.MariaSet.empty()
