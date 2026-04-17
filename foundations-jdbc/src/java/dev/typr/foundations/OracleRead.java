package dev.typr.foundations;

import dev.typr.foundations.data.NonEmptyBlob;
import dev.typr.foundations.data.NonEmptyString;
import dev.typr.foundations.data.OracleIntervalDS;
import dev.typr.foundations.data.OracleIntervalYM;
import dev.typr.foundations.data.PaddedString;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;

/**
 * Describes how to read a column from a {@link ResultSet} for Oracle.
 *
 * <p>Similar to MariaRead but adapted for Oracle-specific types.
 *
 * <p>Reading is split into two phases: - extract: Determines which ResultSet method to use
 * (getString, getInt, getObject, etc.) - transform: Converts the extracted value to the target type
 * (BigDecimal → Long, String → NonEmptyString, etc.)
 */
public sealed interface OracleRead<A> extends DbRead<A>
    permits OracleRead.NonNullable, OracleRead.Nullable, OracleRead.MappedNullable {
  /**
   * Phase 1: Extract a value from the ResultSet. Determines which ResultSet method to use
   * (getString, getInt, getObject, etc.).
   */
  Object extract(ResultSet rs, int col) throws SQLException;

  /**
   * Phase 2: Transform the extracted value to the target type. Handles conversions like BigDecimal
   * → Long, String → NonEmptyString, etc. Returns null for SQL NULL values (checked via
   * rs.wasNull()).
   */
  A transform(Object value) throws SQLException;

  /**
   * Combined read operation: extract + transform. Handles nullability based on NonNullable vs
   * Nullable implementation.
   */
  A read(ResultSet rs, int col) throws SQLException;

  /**
   * Convert a raw Oracle value to its Java representation. This is the inverse of
   * OracleWrite.toOracleValue(). Uses only the transform phase since the value is already
   * extracted.
   */
  default A fromOracleValue(Object value) throws SQLException {
    return transform(value);
  }

  <B> OracleRead<B> map(SqlFunction<A, B> f);

  /** Derive an OracleRead which allows nullable values */
  OracleRead<Optional<A>> opt();

  @FunctionalInterface
  interface RawRead<A> {
    A apply(ResultSet rs, int column) throws SQLException;
  }

  @FunctionalInterface
  interface Extractor<A> {
    A apply(ResultSet rs, int col) throws SQLException;
  }

  @FunctionalInterface
  interface Transformer<U, A> {
    A apply(U value) throws SQLException;
  }

  /**
   * Create an instance of {@link OracleRead} from extract and transform functions.
   *
   * @param extractor Extracts raw value from ResultSet (which RS method to use)
   * @param transformer Transforms raw value to target type (may return null for SQL NULL)
   */
  static <U, A> NonNullable<U, A> of(Extractor<U> extractor, Transformer<U, A> transformer) {
    return new NonNullable<>(extractor, transformer);
  }

  /**
   * Create an instance of {@link OracleRead} from just an extractor. Uses identity transformation -
   * for cases where no conversion is needed.
   *
   * @param extractor Extracts raw value from ResultSet (which RS method to use)
   */
  static <A> NonNullable<A, A> of(Extractor<A> extractor) {
    return new NonNullable<>(extractor, a -> a);
  }

  final class NonNullable<U, A> implements OracleRead<A> {
    final Extractor<U> extractor;
    final Transformer<U, A> transformer;

    public NonNullable(Extractor<U> extractor, Transformer<U, A> transformer) {
      this.extractor = extractor;
      this.transformer = transformer;
    }

    @Override
    public Object extract(ResultSet rs, int col) throws SQLException {
      return extractor.apply(rs, col);
    }

    @Override
    @SuppressWarnings("unchecked")
    public A transform(Object value) throws SQLException {
      return transformer.apply((U) value);
    }

    @Override
    public A read(ResultSet rs, int col) throws SQLException {
      U raw = extractor.apply(rs, col);
      if (rs.wasNull()) {
        throw new SQLException("null value in column " + col);
      }
      return transformer.apply(raw);
    }

    @Override
    public <B> NonNullable<U, B> map(SqlFunction<A, B> f) {
      return new NonNullable<>(
          this.extractor,
          value -> {
            A a = this.transformer.apply(value);
            if (a == null) return null;
            return f.apply(a);
          });
    }

    @Override
    public OracleRead<Optional<A>> opt() {
      return new Nullable<>(this.extractor, this.transformer);
    }
  }

  final class Nullable<U, A> implements OracleRead<Optional<A>> {
    final Extractor<U> extractor;
    final Transformer<U, A> transformer;

    public Nullable(Extractor<U> extractor, Transformer<U, A> transformer) {
      this.extractor = extractor;
      this.transformer = transformer;
    }

    @Override
    public Object extract(ResultSet rs, int col) throws SQLException {
      return extractor.apply(rs, col);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<A> transform(Object value) throws SQLException {
      if (value == null) {
        return Optional.empty();
      }
      A result = transformer.apply((U) value);
      return Optional.ofNullable(result);
    }

    @Override
    public Optional<A> read(ResultSet rs, int col) throws SQLException {
      U raw = extractor.apply(rs, col);
      if (rs.wasNull()) {
        return Optional.empty();
      }
      return Optional.of(transformer.apply(raw));
    }

    @Override
    public <B> OracleRead<B> map(SqlFunction<Optional<A>, B> f) {
      return new MappedNullable<>(this.extractor, this.transformer, f);
    }

    @Override
    public OracleRead<Optional<Optional<A>>> opt() {
      return new Nullable<>(
          this.extractor,
          value -> {
            A result = this.transformer.apply(value);
            Optional<A> maybeA = result == null ? Optional.empty() : Optional.of(result);
            return maybeA.isEmpty() ? null : maybeA;
          });
    }
  }

  /**
   * Result of mapping a Nullable reader - handles NULL database values but returns a non-Optional
   * type. This is used when converting Optional[A] to Option[A] in Scala, where the Option type
   * already represents nullability.
   */
  final class MappedNullable<U, A, B> implements OracleRead<B> {
    final Extractor<U> extractor;
    final Transformer<U, A> transformer;
    final SqlFunction<Optional<A>, B> mapFunction;

    public MappedNullable(
        Extractor<U> extractor,
        Transformer<U, A> transformer,
        SqlFunction<Optional<A>, B> mapFunction) {
      this.extractor = extractor;
      this.transformer = transformer;
      this.mapFunction = mapFunction;
    }

    @Override
    public Object extract(ResultSet rs, int col) throws SQLException {
      return extractor.apply(rs, col);
    }

    @Override
    @SuppressWarnings("unchecked")
    public B transform(Object value) throws SQLException {
      A result = transformer.apply((U) value);
      Optional<A> opt = Optional.ofNullable(result);
      return mapFunction.apply(opt);
    }

    @Override
    @SuppressWarnings("unchecked")
    public B read(ResultSet rs, int col) throws SQLException {
      U raw = extractor.apply(rs, col);
      if (rs.wasNull()) {
        // Handle NULL by passing Optional.empty() to the map function
        return mapFunction.apply(Optional.empty());
      }
      A result = transformer.apply(raw);
      Optional<A> opt = Optional.ofNullable(result);
      return mapFunction.apply(opt);
    }

    @Override
    public <C> OracleRead<C> map(SqlFunction<B, C> f) {
      // Compose the map functions
      SqlFunction<Optional<A>, C> composed = opt -> f.apply(mapFunction.apply(opt));
      return new MappedNullable<>(extractor, transformer, composed);
    }

    @Override
    public OracleRead<Optional<B>> opt() {
      // Convert to Nullable by wrapping the result in Optional
      return new Nullable<>(
          extractor,
          value -> {
            A result = transformer.apply(value);
            Optional<A> opt = Optional.ofNullable(result);
            return mapFunction.apply(opt);
          });
    }
  }

  static <A> NonNullable<Object, A> castJdbcObjectTo(Class<A> cls) {
    return of(ResultSet::getObject, obj -> cls.cast(obj));
  }

  // Basic type readers
  OracleRead<String> readString = of(ResultSet::getString);
  OracleRead<NonEmptyString> readNonEmptyString =
      of(ResultSet::getString, s -> NonEmptyString.force((String) s));

  static OracleRead<PaddedString> readPaddedString(int length) {
    return of(ResultSet::getString, s -> PaddedString.force((String) s, length));
  }

  OracleRead<Boolean> readBoolean = of(ResultSet::getBoolean);

  // Numeric reads route through Object + Number-aware transform so the same function works at
  // top level (driver hands Integer/Long/etc. directly for INTEGER columns) and inside STRUCT
  // attribute decoding (where the driver always hands BigDecimal regardless of column precision).
  // Without this, `getInt(rs, col)` works at the top level but `transform(BigDecimal)` inside a
  // STRUCT casts directly to Integer and fails with "BigDecimal cannot be cast to Integer".
  OracleRead<Byte> readByte =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Byte b) return b;
            if (obj instanceof Number n) return n.byteValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Byte");
          });
  OracleRead<Short> readShort =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Short s) return s;
            if (obj instanceof Number n) return n.shortValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Short");
          });
  OracleRead<Integer> readInteger =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Integer i) return i;
            if (obj instanceof Number n) return n.intValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Integer");
          });
  OracleRead<Long> readLong =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Long l) return l;
            if (obj instanceof Number n) return n.longValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Long");
          });
  OracleRead<Float> readFloat =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Float f) return f;
            if (obj instanceof Number n) return n.floatValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Float");
          });
  OracleRead<Double> readDouble =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Double d) return d;
            if (obj instanceof Number n) return n.doubleValue();
            throw new SQLException("Cannot convert " + obj.getClass() + " to Double");
          });
  OracleRead<BigDecimal> readBigDecimal = of(ResultSet::getBigDecimal);

  // For RAW/BLOB - reads as byte[] directly
  OracleRead<byte[]> readByteArray = of(ResultSet::getBytes);
  OracleRead<NonEmptyBlob> readNonEmptyBlob =
      of(ResultSet::getBytes, bytes -> NonEmptyBlob.force((byte[]) bytes));

  // For BLOB types - Oracle returns Blob objects, need to extract bytes
  OracleRead<byte[]> readBlob =
      of(
          ResultSet::getBlob,
          blob -> {
            if (blob == null) return null;
            java.sql.Blob b = (java.sql.Blob) blob;
            return b.getBytes(1, (int) b.length());
          });

  // For CLOB types - Oracle returns Clob objects, need to extract string
  OracleRead<String> readClob =
      of(
          ResultSet::getClob,
          clob -> {
            if (clob == null) return null;
            java.sql.Clob c = (java.sql.Clob) clob;
            return c.getSubString(1, (int) c.length());
          });

  // Date/Time readers
  // Note: Oracle DATE includes time component (no separate TIME type)
  // Use getTimestamp() instead of getObject() because Oracle's getGeneratedKeys() ResultSet
  // throws ORA-17004 with getObject() for DATE columns.
  // The transformer handles both java.sql.Timestamp (from ResultSet) and oracle.sql.TIMESTAMP (from
  // STRUCT via fromOracleValue)
  OracleRead<LocalDateTime> readLocalDateTime =
      of(
          (rs, col) -> (Object) rs.getTimestamp(col),
          obj -> {
            if (obj == null) return null;
            if (obj instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
            if (obj instanceof oracle.sql.TIMESTAMP oracleTs) {
              // Use toJdbc() instead of timestampValue() - more reliable for STRUCT attributes
              return ((java.sql.Timestamp) oracleTs.toJdbc()).toLocalDateTime();
            }
            throw new SQLException("Unexpected type for DATE: " + obj.getClass().getName());
          });

  // Oracle's TIMESTAMP type
  // Use getTimestamp() instead of getObject() because Oracle's getGeneratedKeys() ResultSet
  // throws ORA-17004 with getObject() for TIMESTAMP columns.
  // The transformer handles both java.sql.Timestamp (from ResultSet) and oracle.sql.TIMESTAMP (from
  // STRUCT via fromOracleValue)
  OracleRead<LocalDateTime> readTimestamp =
      of(
          (rs, col) -> (Object) rs.getTimestamp(col),
          obj -> {
            if (obj == null) return null;
            if (obj instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
            if (obj instanceof oracle.sql.TIMESTAMP oracleTs) {
              // Use toJdbc() instead of timestampValue() - more reliable for STRUCT attributes
              return ((java.sql.Timestamp) oracleTs.toJdbc()).toLocalDateTime();
            }
            throw new SQLException("Unexpected type for TIMESTAMP: " + obj.getClass().getName());
          });

  // TIMESTAMP WITH TIME ZONE -> ZonedDateTime
  //
  // Oracle's TIMESTAMP WITH TIME ZONE stores either a fixed offset *or* a named zone region
  // (e.g. "America/Los_Angeles") — 13-byte on-disk representation. ZonedDateTime is the only
  // java.time type that round-trips both: fixed offsets become a ZonedDateTime with a ZoneOffset
  // zone, region names become a ZonedDateTime with a ZoneRegion zone. Mapping to OffsetDateTime
  // would discard the region and freeze the offset at its current DST state, so a round-trip
  // through the library would silently turn `… America/Los_Angeles` into `… -08:00`.
  //
  // Handles ZonedDateTime (from columns), oracle.sql.TIMESTAMPTZ (from STRUCT attributes),
  // and OffsetDateTime (defensive fallback for drivers that return it directly).
  OracleRead<ZonedDateTime> readZonedDateTime =
      of(
          (rs, col) -> (Object) rs.getObject(col, ZonedDateTime.class),
          obj -> {
            if (obj == null) return null;
            if (obj instanceof ZonedDateTime zdt) return zdt;
            if (obj instanceof OffsetDateTime odt) return odt.toZonedDateTime();
            if (obj instanceof oracle.sql.TIMESTAMPTZ oracleTzTs) {
              // zonedDateTimeValue() preserves region names; offsetDateTimeValue() does not.
              try {
                return oracleTzTs.zonedDateTimeValue();
              } catch (Exception e) {
                throw new SQLException(
                    "Failed to convert oracle.sql.TIMESTAMPTZ to ZonedDateTime: " + e.getMessage(),
                    e);
              }
            }
            throw new SQLException(
                "Unexpected type for TIMESTAMP WITH TIME ZONE: " + obj.getClass().getName());
          });

  // TIMESTAMP WITH LOCAL TIME ZONE -> Instant
  // Like PG timestamptz, this is an absolute point in time (stored in DB timezone, displayed in
  // session timezone). Mapping to Instant avoids timezone-dependent comparisons.
  OracleRead<Instant> readLocalTimezoneTimestamp =
      of(
          (rs, col) -> (Object) rs.getObject(col, OffsetDateTime.class),
          obj -> {
            if (obj == null) return null;
            if (obj instanceof OffsetDateTime odt) return odt.toInstant();
            throw new SQLException(
                "Unexpected type for TIMESTAMP WITH LOCAL TIME ZONE: " + obj.getClass().getName());
          });

  // INTERVAL YEAR TO MONTH -> OracleIntervalYM (parses both Oracle and ISO-8601 formats)
  // Handles both String (from columns) and oracle.sql.INTERVALYM (from STRUCT attributes)
  OracleRead<OracleIntervalYM> readIntervalYearToMonth =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof String str) {
              return OracleIntervalYM.parse(str);
            } else if (obj instanceof oracle.sql.INTERVALYM interval) {
              // Convert oracle.sql.INTERVALYM to string and parse
              return OracleIntervalYM.parse(interval.stringValue());
            } else {
              throw new SQLException(
                  "Unexpected type for INTERVAL YEAR TO MONTH: " + obj.getClass().getName());
            }
          });

  // INTERVAL DAY TO SECOND -> OracleIntervalDS (parses both Oracle and ISO-8601 formats)
  // Handles both String (from columns) and oracle.sql.INTERVALDS (from STRUCT attributes)
  OracleRead<OracleIntervalDS> readIntervalDayToSecond =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof String str) {
              return OracleIntervalDS.parse(str);
            } else if (obj instanceof oracle.sql.INTERVALDS interval) {
              // Convert oracle.sql.INTERVALDS to string and parse
              return OracleIntervalDS.parse(interval.stringValue());
            } else {
              throw new SQLException(
                  "Unexpected type for INTERVAL DAY TO SECOND: " + obj.getClass().getName());
            }
          });

  // ROWID types
  OracleRead<String> readRowId =
      of(
          ResultSet::getRowId,
          rowId -> {
            if (rowId == null) return null;
            return ((java.sql.RowId) rowId).toString();
          });

  // NUMBER type - Oracle returns BigDecimal for all NUMBER types
  // But we need specific readers for precision-based mapping
  OracleRead<Integer> readNumberAsInt =
      of(ResultSet::getBigDecimal, bd -> ((BigDecimal) bd).intValueExact());
  OracleRead<Long> readNumberAsLong =
      of(ResultSet::getBigDecimal, bd -> ((BigDecimal) bd).longValueExact());

  // BINARY_FLOAT
  OracleRead<Float> readBinaryFloat =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Float) return (Float) obj;
            if (obj instanceof Number) return ((Number) obj).floatValue();
            return Float.parseFloat(obj.toString());
          });

  // BINARY_DOUBLE
  OracleRead<Double> readBinaryDouble =
      of(
          ResultSet::getObject,
          obj -> {
            if (obj == null) return null;
            if (obj instanceof Double) return (Double) obj;
            if (obj instanceof Number) return ((Number) obj).doubleValue();
            return Double.parseDouble(obj.toString());
          });
}
