package dev.typr.foundationssc

package object data:
  // Re-export data types
  type AclItem = dev.typr.foundations.data.AclItem
  type AnyArray = dev.typr.foundations.data.AnyArray
  type Arr[T] = dev.typr.foundations.data.Arr[T]
  type Cidr = dev.typr.foundations.data.Cidr
  type HierarchyId = dev.typr.foundations.data.HierarchyId
  type Inet = dev.typr.foundations.data.Inet
  type Int2Vector = dev.typr.foundations.data.Int2Vector
  type Json = dev.typr.foundations.data.Json
  type JsonValue = dev.typr.foundations.data.JsonValue
  object JsonValue:
    def parse(json: String): JsonValue = dev.typr.foundations.data.JsonValue.parse(json)
  type Jsonb = dev.typr.foundations.data.Jsonb
  type MacAddr = dev.typr.foundations.data.MacAddr
  type MacAddr8 = dev.typr.foundations.data.MacAddr8
  type Money = dev.typr.foundations.data.Money
  type Oid = dev.typr.foundations.data.Oid
  type OidVector = dev.typr.foundations.data.OidVector
  type OracleIntervalDS = dev.typr.foundations.data.OracleIntervalDS
  object OracleIntervalDS:
    def parse(s: String): OracleIntervalDS = dev.typr.foundations.data.OracleIntervalDS.parse(s)
  type OracleIntervalYM = dev.typr.foundations.data.OracleIntervalYM
  object OracleIntervalYM:
    def parse(s: String): OracleIntervalYM = dev.typr.foundations.data.OracleIntervalYM.parse(s)
  type PgName = dev.typr.foundations.data.PgName
  type PgNodeTree = dev.typr.foundations.data.PgNodeTree
  type Range[T <: Comparable[? >: T]] = dev.typr.foundations.data.Range[T]
  object Range:
    def empty[T <: Comparable[? >: T]](): dev.typr.foundations.data.Range[T] =
      dev.typr.foundations.data.Range.empty()
    def int4(from: dev.typr.foundations.data.RangeBound[Integer], to: dev.typr.foundations.data.RangeBound[Integer]): dev.typr.foundations.data.Range[Integer] =
      dev.typr.foundations.data.Range.int4(from, to)
    def int8(from: dev.typr.foundations.data.RangeBound[java.lang.Long], to: dev.typr.foundations.data.RangeBound[java.lang.Long]): dev.typr.foundations.data.Range[java.lang.Long] =
      dev.typr.foundations.data.Range.int8(from, to)
    def date(from: dev.typr.foundations.data.RangeBound[java.time.LocalDate], to: dev.typr.foundations.data.RangeBound[java.time.LocalDate]): dev.typr.foundations.data.Range[java.time.LocalDate] =
      dev.typr.foundations.data.Range.date(from, to)
    def numeric(from: dev.typr.foundations.data.RangeBound[java.math.BigDecimal], to: dev.typr.foundations.data.RangeBound[java.math.BigDecimal]): dev.typr.foundations.data.Range[java.math.BigDecimal] =
      dev.typr.foundations.data.Range.numeric(from, to)
    def timestamp(from: dev.typr.foundations.data.RangeBound[java.time.LocalDateTime], to: dev.typr.foundations.data.RangeBound[java.time.LocalDateTime]): dev.typr.foundations.data.Range[java.time.LocalDateTime] =
      dev.typr.foundations.data.Range.timestamp(from, to)
    def timestamptz(from: dev.typr.foundations.data.RangeBound[java.time.Instant], to: dev.typr.foundations.data.RangeBound[java.time.Instant]): dev.typr.foundations.data.Range[java.time.Instant] =
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
  type Regconfig = dev.typr.foundations.data.Regconfig
  type Regdictionary = dev.typr.foundations.data.Regdictionary
  type Regnamespace = dev.typr.foundations.data.Regnamespace
  type Regoper = dev.typr.foundations.data.Regoper
  type Regoperator = dev.typr.foundations.data.Regoperator
  type Regproc = dev.typr.foundations.data.Regproc
  type Regprocedure = dev.typr.foundations.data.Regprocedure
  type Regrole = dev.typr.foundations.data.Regrole
  type Regtype = dev.typr.foundations.data.Regtype
  type Uint1 = dev.typr.foundations.data.Uint1
  type Uint2 = dev.typr.foundations.data.Uint2
  type Uint4 = dev.typr.foundations.data.Uint4
  type Uint8 = dev.typr.foundations.data.Uint8
  type Unknown = dev.typr.foundations.data.Unknown
  type Vector = dev.typr.foundations.data.Vector
  type Xid = dev.typr.foundations.data.Xid
  type Xml = dev.typr.foundations.data.Xml
  type NonEmptyBlob = dev.typr.foundations.data.NonEmptyBlob
  type NonEmptyString = dev.typr.foundations.data.NonEmptyString
  type PaddedString = dev.typr.foundations.data.PaddedString

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
