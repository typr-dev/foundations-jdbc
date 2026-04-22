package dev.typr.foundations;

import dev.typr.foundations.data.Range;
import dev.typr.foundations.data.RangeBound;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.postgresql.geometric.*;
import org.postgresql.util.PGInterval;

public abstract class PgBinary<A> {
  public abstract A decode(byte[] raw, int offset, int len);

  public abstract byte[] encode(A value);

  public boolean prefersBinaryFormat() {
    return true;
  }

  public <B> PgBinary<B> bimap(Function<A, B> decoder, Function<B, A> encoder) {
    var self = this;
    return new PgBinary<>() {
      @Override
      public B decode(byte[] raw, int offset, int len) {
        return decoder.apply(self.decode(raw, offset, len));
      }

      @Override
      public byte[] encode(B value) {
        return self.encode(encoder.apply(value));
      }

      @Override
      public boolean prefersBinaryFormat() {
        return self.prefersBinaryFormat();
      }
    };
  }

  public PgBinary<Optional<A>> opt() {
    var self = this;
    return new PgBinary<>() {
      @Override
      public Optional<A> decode(byte[] raw, int offset, int len) {
        return Optional.of(self.decode(raw, offset, len));
      }

      @Override
      public byte[] encode(Optional<A> value) {
        return self.encode(value.get());
      }

      @Override
      public boolean prefersBinaryFormat() {
        return self.prefersBinaryFormat();
      }
    };
  }

  public PgBinary<A[]> array(IntFunction<A[]> arrayFactory) {
    var self = this;
    return new PgBinary<>() {
      @Override
      public A[] decode(byte[] raw, int offset, int len) {
        int pos = offset;
        int ndim = readInt32(raw, pos);
        pos += 4;
        pos += 4; // has_null flag
        pos += 4; // element OID

        if (ndim == 0) return arrayFactory.apply(0);

        int totalElements = 1;
        for (int d = 0; d < ndim; d++) {
          totalElements *= readInt32(raw, pos);
          pos += 4;
          pos += 4; // lower bound
        }

        A[] result = arrayFactory.apply(totalElements);
        for (int i = 0; i < totalElements; i++) {
          int elemLen = readInt32(raw, pos);
          pos += 4;
          if (elemLen == -1) {
            result[i] = null;
          } else {
            result[i] = self.decode(raw, pos, elemLen);
            pos += elemLen;
          }
        }
        return result;
      }

      @Override
      public byte[] encode(A[] value) {
        byte[][] encoded = new byte[value.length][];
        int totalDataLen = 0;
        for (int i = 0; i < value.length; i++) {
          if (value[i] != null) {
            encoded[i] = self.encode(value[i]);
            totalDataLen += encoded[i].length;
          }
        }
        // header: ndim(4) + has_null(4) + oid(4) + dim_size(4) + lower_bound(4) = 20
        // per element: len(4) + data
        int bufLen = 20 + value.length * 4 + totalDataLen;
        byte[] buf = new byte[bufLen];
        int pos = 0;
        writeInt32(buf, pos, value.length > 0 ? 1 : 0);
        pos += 4;
        int hasNull = 0;
        for (int i = 0; i < value.length; i++) {
          if (value[i] == null) {
            hasNull = 1;
            break;
          }
        }
        writeInt32(buf, pos, hasNull);
        pos += 4;
        writeInt32(buf, pos, 0); // oid
        pos += 4;
        writeInt32(buf, pos, value.length);
        pos += 4;
        writeInt32(buf, pos, 1); // lower bound
        pos += 4;
        for (int i = 0; i < value.length; i++) {
          if (value[i] == null) {
            writeInt32(buf, pos, -1);
            pos += 4;
          } else {
            writeInt32(buf, pos, encoded[i].length);
            pos += 4;
            System.arraycopy(encoded[i], 0, buf, pos, encoded[i].length);
            pos += encoded[i].length;
          }
        }
        return buf;
      }

      @Override
      public boolean prefersBinaryFormat() {
        return self.prefersBinaryFormat();
      }
    };
  }

  public static <A> PgBinary<A> textFallback(PgText<A> text) {
    return new PgBinary<>() {
      @Override
      public A decode(byte[] raw, int offset, int len) {
        return text.wireDecodeBytes(raw, offset, len);
      }

      @Override
      public byte[] encode(A value) {
        StringBuilder sb = new StringBuilder();
        text.wireEncode(value, sb);
        return sb.toString().getBytes(StandardCharsets.UTF_8);
      }

      @Override
      public boolean prefersBinaryFormat() {
        return false;
      }
    };
  }

  // ========== Byte reading helpers ==========

  static int readInt16(byte[] raw, int offset) {
    return (short) (((raw[offset] & 0xFF) << 8) | (raw[offset + 1] & 0xFF));
  }

  static int readInt32(byte[] raw, int offset) {
    return ((raw[offset] & 0xFF) << 24)
        | ((raw[offset + 1] & 0xFF) << 16)
        | ((raw[offset + 2] & 0xFF) << 8)
        | (raw[offset + 3] & 0xFF);
  }

  static long readInt64(byte[] raw, int offset) {
    return ((long) (raw[offset] & 0xFF) << 56)
        | ((long) (raw[offset + 1] & 0xFF) << 48)
        | ((long) (raw[offset + 2] & 0xFF) << 40)
        | ((long) (raw[offset + 3] & 0xFF) << 32)
        | ((long) (raw[offset + 4] & 0xFF) << 24)
        | ((long) (raw[offset + 5] & 0xFF) << 16)
        | ((long) (raw[offset + 6] & 0xFF) << 8)
        | (raw[offset + 7] & 0xFF);
  }

  static float readFloat32(byte[] raw, int offset) {
    return Float.intBitsToFloat(readInt32(raw, offset));
  }

  static double readFloat64(byte[] raw, int offset) {
    return Double.longBitsToDouble(readInt64(raw, offset));
  }

  // ========== Byte writing helpers ==========

  static void writeInt16(byte[] buf, int offset, int value) {
    buf[offset] = (byte) (value >> 8);
    buf[offset + 1] = (byte) value;
  }

  static void writeInt32(byte[] buf, int offset, int value) {
    buf[offset] = (byte) (value >> 24);
    buf[offset + 1] = (byte) (value >> 16);
    buf[offset + 2] = (byte) (value >> 8);
    buf[offset + 3] = (byte) value;
  }

  static void writeInt64(byte[] buf, int offset, long value) {
    buf[offset] = (byte) (value >> 56);
    buf[offset + 1] = (byte) (value >> 48);
    buf[offset + 2] = (byte) (value >> 40);
    buf[offset + 3] = (byte) (value >> 32);
    buf[offset + 4] = (byte) (value >> 24);
    buf[offset + 5] = (byte) (value >> 16);
    buf[offset + 6] = (byte) (value >> 8);
    buf[offset + 7] = (byte) value;
  }

  static void writeFloat64(byte[] buf, int offset, double value) {
    writeInt64(buf, offset, Double.doubleToRawLongBits(value));
  }

  static void writeFloat32(byte[] buf, int offset, float value) {
    writeInt32(buf, offset, Float.floatToRawIntBits(value));
  }

  // ========== PostgreSQL epoch constants ==========

  private static final long PG_EPOCH_DAYS = 10957L;
  private static final long PG_EPOCH_MICROS = 946684800_000000L;

  // ========== Standard scalar instances ==========

  public static final PgBinary<String> STRING =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          return new String(raw, offset, len, StandardCharsets.UTF_8);
        }

        @Override
        public byte[] encode(String value) {
          return value.getBytes(StandardCharsets.UTF_8);
        }
      };

  public static final PgBinary<Boolean> BOOLEAN =
      new PgBinary<>() {
        @Override
        public Boolean decode(byte[] raw, int offset, int len) {
          return raw[offset] != 0;
        }

        @Override
        public byte[] encode(Boolean value) {
          return new byte[] {(byte) (value ? 1 : 0)};
        }
      };

  public static final PgBinary<Short> INT2 =
      new PgBinary<>() {
        @Override
        public Short decode(byte[] raw, int offset, int len) {
          return (short) (((raw[offset] & 0xFF) << 8) | (raw[offset + 1] & 0xFF));
        }

        @Override
        public byte[] encode(Short value) {
          byte[] buf = new byte[2];
          writeInt16(buf, 0, value);
          return buf;
        }
      };

  public static final PgBinary<Integer> INT4 =
      new PgBinary<>() {
        @Override
        public Integer decode(byte[] raw, int offset, int len) {
          return readInt32(raw, offset);
        }

        @Override
        public byte[] encode(Integer value) {
          byte[] buf = new byte[4];
          writeInt32(buf, 0, value);
          return buf;
        }
      };

  public static final PgBinary<Long> INT8 =
      new PgBinary<>() {
        @Override
        public Long decode(byte[] raw, int offset, int len) {
          return readInt64(raw, offset);
        }

        @Override
        public byte[] encode(Long value) {
          byte[] buf = new byte[8];
          writeInt64(buf, 0, value);
          return buf;
        }
      };

  public static final PgBinary<Float> FLOAT4 =
      new PgBinary<>() {
        @Override
        public Float decode(byte[] raw, int offset, int len) {
          return readFloat32(raw, offset);
        }

        @Override
        public byte[] encode(Float value) {
          byte[] buf = new byte[4];
          writeFloat32(buf, 0, value);
          return buf;
        }
      };

  public static final PgBinary<Double> FLOAT8 =
      new PgBinary<>() {
        @Override
        public Double decode(byte[] raw, int offset, int len) {
          return readFloat64(raw, offset);
        }

        @Override
        public byte[] encode(Double value) {
          byte[] buf = new byte[8];
          writeFloat64(buf, 0, value);
          return buf;
        }
      };

  public static final PgBinary<UUID> UUID_BINARY =
      new PgBinary<>() {
        @Override
        public UUID decode(byte[] raw, int offset, int len) {
          long msb = readInt64(raw, offset);
          long lsb = readInt64(raw, offset + 8);
          return new UUID(msb, lsb);
        }

        @Override
        public byte[] encode(UUID value) {
          byte[] buf = new byte[16];
          writeInt64(buf, 0, value.getMostSignificantBits());
          writeInt64(buf, 8, value.getLeastSignificantBits());
          return buf;
        }
      };

  public static final PgBinary<byte[]> BYTEA =
      new PgBinary<>() {
        @Override
        public byte[] decode(byte[] raw, int offset, int len) {
          byte[] result = new byte[len];
          System.arraycopy(raw, offset, result, 0, len);
          return result;
        }

        @Override
        public byte[] encode(byte[] value) {
          byte[] result = new byte[value.length];
          System.arraycopy(value, 0, result, 0, value.length);
          return result;
        }
      };

  // ========== Date/time instances ==========

  public static final PgBinary<LocalDate> DATE =
      new PgBinary<>() {
        @Override
        public LocalDate decode(byte[] raw, int offset, int len) {
          int pgDays = readInt32(raw, offset);
          return LocalDate.ofEpochDay(pgDays + PG_EPOCH_DAYS);
        }

        @Override
        public byte[] encode(LocalDate value) {
          int pgDays = (int) (value.toEpochDay() - PG_EPOCH_DAYS);
          byte[] buf = new byte[4];
          writeInt32(buf, 0, pgDays);
          return buf;
        }
      };

  public static final PgBinary<LocalTime> TIME =
      new PgBinary<>() {
        @Override
        public LocalTime decode(byte[] raw, int offset, int len) {
          long pgMicros = readInt64(raw, offset);
          return LocalTime.ofNanoOfDay(pgMicros * 1000);
        }

        @Override
        public byte[] encode(LocalTime value) {
          long micros = value.toNanoOfDay() / 1000;
          byte[] buf = new byte[8];
          writeInt64(buf, 0, micros);
          return buf;
        }
      };

  public static final PgBinary<OffsetTime> TIMETZ =
      new PgBinary<>() {
        @Override
        public OffsetTime decode(byte[] raw, int offset, int len) {
          long pgMicros = readInt64(raw, offset);
          int tzOffsetSec = readInt32(raw, offset + 8);
          LocalTime localTime = LocalTime.ofNanoOfDay(pgMicros * 1000);
          ZoneOffset zone = ZoneOffset.ofTotalSeconds(-tzOffsetSec);
          return OffsetTime.of(localTime, zone);
        }

        @Override
        public byte[] encode(OffsetTime value) {
          byte[] buf = new byte[12];
          long micros = value.toLocalTime().toNanoOfDay() / 1000;
          writeInt64(buf, 0, micros);
          writeInt32(buf, 8, -value.getOffset().getTotalSeconds());
          return buf;
        }
      };

  public static final PgBinary<LocalDateTime> TIMESTAMP =
      new PgBinary<>() {
        @Override
        public LocalDateTime decode(byte[] raw, int offset, int len) {
          long pgMicros = readInt64(raw, offset);
          long unixMicros = pgMicros + PG_EPOCH_MICROS;
          long epochSecond = Math.floorDiv(unixMicros, 1_000_000L);
          int nanos = (int) (Math.floorMod(unixMicros, 1_000_000L) * 1000);
          return LocalDateTime.ofInstant(
              Instant.ofEpochSecond(epochSecond, nanos), ZoneOffset.UTC);
        }

        @Override
        public byte[] encode(LocalDateTime value) {
          Instant instant = value.toInstant(ZoneOffset.UTC);
          long unixMicros =
              instant.getEpochSecond() * 1_000_000L + instant.getNano() / 1000;
          long pgMicros = unixMicros - PG_EPOCH_MICROS;
          byte[] buf = new byte[8];
          writeInt64(buf, 0, pgMicros);
          return buf;
        }
      };

  public static final PgBinary<Instant> TIMESTAMPTZ =
      new PgBinary<>() {
        @Override
        public Instant decode(byte[] raw, int offset, int len) {
          long pgMicros = readInt64(raw, offset);
          long unixMicros = pgMicros + PG_EPOCH_MICROS;
          long epochSecond = Math.floorDiv(unixMicros, 1_000_000L);
          int nanos = (int) (Math.floorMod(unixMicros, 1_000_000L) * 1000);
          return Instant.ofEpochSecond(epochSecond, nanos);
        }

        @Override
        public byte[] encode(Instant value) {
          long unixMicros =
              value.getEpochSecond() * 1_000_000L + value.getNano() / 1000;
          long pgMicros = unixMicros - PG_EPOCH_MICROS;
          byte[] buf = new byte[8];
          writeInt64(buf, 0, pgMicros);
          return buf;
        }
      };

  public static final PgBinary<PGInterval> INTERVAL =
      new PgBinary<>() {
        @Override
        public PGInterval decode(byte[] raw, int offset, int len) {
          long microseconds = readInt64(raw, offset);
          int days = readInt32(raw, offset + 8);
          int months = readInt32(raw, offset + 12);
          int years = months / 12;
          int remainMonths = months % 12;
          int hours = (int) (microseconds / 3_600_000_000L);
          long rem = microseconds % 3_600_000_000L;
          int minutes = (int) (rem / 60_000_000L);
          rem = rem % 60_000_000L;
          double seconds = rem / 1_000_000.0;
          return new PGInterval(years, remainMonths, days, hours, minutes, seconds);
        }

        @Override
        public byte[] encode(PGInterval value) {
          byte[] buf = new byte[16];
          int months = value.getYears() * 12 + value.getMonths();
          long micros = value.getHours() * 3_600_000_000L
              + value.getMinutes() * 60_000_000L
              + (long) (value.getSeconds() * 1_000_000);
          writeInt64(buf, 0, micros);
          writeInt32(buf, 8, value.getDays());
          writeInt32(buf, 12, months);
          return buf;
        }
      };

  // ========== Numeric (BigDecimal) ==========

  public static final PgBinary<BigDecimal> NUMERIC =
      new PgBinary<>() {
        private static final int NUMERIC_NAN = 0xC000;

        @Override
        public BigDecimal decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndigits = readInt16(raw, pos) & 0xFFFF;
          pos += 2;
          int weight = (short) readInt16(raw, pos);
          pos += 2;
          int sign = readInt16(raw, pos) & 0xFFFF;
          pos += 2;
          int dscale = readInt16(raw, pos) & 0xFFFF;
          pos += 2;

          if (sign == NUMERIC_NAN) {
            throw new ArithmeticException("Cannot represent PostgreSQL NaN as BigDecimal");
          }

          if (ndigits == 0) {
            return BigDecimal.ZERO.setScale(dscale);
          }

          BigInteger unscaled = BigInteger.ZERO;
          for (int i = 0; i < ndigits; i++) {
            int digit = readInt16(raw, pos + i * 2) & 0xFFFF;
            unscaled =
                unscaled.multiply(BigInteger.valueOf(10000)).add(BigInteger.valueOf(digit));
          }
          if (sign == 0x4000) unscaled = unscaled.negate();

          int scaleAdjust = (weight + 1 - ndigits) * 4;
          int totalAdjust = scaleAdjust + dscale;
          BigInteger resultUnscaled;
          if (totalAdjust >= 0) {
            resultUnscaled = unscaled.multiply(BigInteger.TEN.pow(totalAdjust));
          } else {
            resultUnscaled = unscaled.divide(BigInteger.TEN.pow(-totalAdjust));
          }
          return new BigDecimal(resultUnscaled, dscale);
        }

        @Override
        public byte[] encode(BigDecimal value) {
          if (value.signum() == 0) {
            byte[] buf = new byte[8];
            writeInt16(buf, 6, Math.max(0, value.scale()));
            return buf;
          }

          int sign = value.signum() < 0 ? 0x4000 : 0x0000;
          int dscale = Math.max(0, value.scale());

          BigInteger unscaledAbs = value.unscaledValue().abs();
          int scale = value.scale();
          if (scale < 0) {
            unscaledAbs = unscaledAbs.multiply(BigInteger.TEN.pow(-scale));
            scale = 0;
          }

          String digits = unscaledAbs.toString();
          int totalLen = digits.length();
          int intLen = Math.max(0, totalLen - scale);
          int fracLen = totalLen - intLen;

          int intPad = intLen > 0 ? (4 - (intLen % 4)) % 4 : 0;
          int extraFracLeadingZeros = Math.max(0, scale - totalLen);
          int effectiveFracLen = extraFracLeadingZeros + fracLen;
          int effectiveFracPad = effectiveFracLen > 0 ? (4 - (effectiveFracLen % 4)) % 4 : 0;

          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < intPad; i++) sb.append('0');
          sb.append(digits, 0, intLen);
          for (int i = 0; i < extraFracLeadingZeros; i++) sb.append('0');
          sb.append(digits, intLen, totalLen);
          for (int i = 0; i < effectiveFracPad; i++) sb.append('0');

          String paddedDigits = sb.toString();
          int ngroups = paddedDigits.length() / 4;
          short[] base10000 = new short[ngroups];
          for (int i = 0; i < ngroups; i++) {
            base10000[i] = Short.parseShort(paddedDigits.substring(i * 4, i * 4 + 4));
          }

          int intGroups = (intLen + intPad) / 4;
          int weight = intGroups - 1;

          int firstNonZero = 0;
          while (firstNonZero < ngroups && base10000[firstNonZero] == 0) firstNonZero++;

          int lastNonZero = ngroups - 1;
          while (lastNonZero > firstNonZero && base10000[lastNonZero] == 0) lastNonZero--;

          int effectiveNdigits = lastNonZero - firstNonZero + 1;
          weight = weight - firstNonZero;

          byte[] buf = new byte[8 + effectiveNdigits * 2];
          writeInt16(buf, 0, effectiveNdigits);
          writeInt16(buf, 2, weight);
          writeInt16(buf, 4, sign);
          writeInt16(buf, 6, dscale);
          for (int i = 0; i < effectiveNdigits; i++) {
            writeInt16(buf, 8 + i * 2, base10000[firstNonZero + i]);
          }
          return buf;
        }
      };

  // ========== JSON ==========

  public static final PgBinary<String> JSONB_STRING =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          return new String(raw, offset + 1, len - 1, StandardCharsets.UTF_8);
        }

        @Override
        public byte[] encode(String value) {
          byte[] utf8 = value.getBytes(StandardCharsets.UTF_8);
          byte[] buf = new byte[1 + utf8.length];
          buf[0] = 0x01;
          System.arraycopy(utf8, 0, buf, 1, utf8.length);
          return buf;
        }
      };

  // ========== Geometry ==========

  public static final PgBinary<PGpoint> POINT =
      new PgBinary<>() {
        @Override
        public PGpoint decode(byte[] raw, int offset, int len) {
          return new PGpoint(readFloat64(raw, offset), readFloat64(raw, offset + 8));
        }

        @Override
        public byte[] encode(PGpoint value) {
          byte[] buf = new byte[16];
          writeFloat64(buf, 0, value.x);
          writeFloat64(buf, 8, value.y);
          return buf;
        }
      };

  public static final PgBinary<PGbox> BOX =
      new PgBinary<>() {
        @Override
        public PGbox decode(byte[] raw, int offset, int len) {
          return new PGbox(
              readFloat64(raw, offset),
              readFloat64(raw, offset + 8),
              readFloat64(raw, offset + 16),
              readFloat64(raw, offset + 24));
        }

        @Override
        public byte[] encode(PGbox value) {
          byte[] buf = new byte[32];
          writeFloat64(buf, 0, value.point[0].x);
          writeFloat64(buf, 8, value.point[0].y);
          writeFloat64(buf, 16, value.point[1].x);
          writeFloat64(buf, 24, value.point[1].y);
          return buf;
        }
      };

  public static final PgBinary<PGcircle> CIRCLE =
      new PgBinary<>() {
        @Override
        public PGcircle decode(byte[] raw, int offset, int len) {
          return new PGcircle(
              readFloat64(raw, offset),
              readFloat64(raw, offset + 8),
              readFloat64(raw, offset + 16));
        }

        @Override
        public byte[] encode(PGcircle value) {
          byte[] buf = new byte[24];
          writeFloat64(buf, 0, value.center.x);
          writeFloat64(buf, 8, value.center.y);
          writeFloat64(buf, 16, value.radius);
          return buf;
        }
      };

  public static final PgBinary<PGline> LINE =
      new PgBinary<>() {
        @Override
        public PGline decode(byte[] raw, int offset, int len) {
          return new PGline(
              readFloat64(raw, offset),
              readFloat64(raw, offset + 8),
              readFloat64(raw, offset + 16));
        }

        @Override
        public byte[] encode(PGline value) {
          byte[] buf = new byte[24];
          writeFloat64(buf, 0, value.a);
          writeFloat64(buf, 8, value.b);
          writeFloat64(buf, 16, value.c);
          return buf;
        }
      };

  public static final PgBinary<PGlseg> LSEG =
      new PgBinary<>() {
        @Override
        public PGlseg decode(byte[] raw, int offset, int len) {
          return new PGlseg(
              readFloat64(raw, offset),
              readFloat64(raw, offset + 8),
              readFloat64(raw, offset + 16),
              readFloat64(raw, offset + 24));
        }

        @Override
        public byte[] encode(PGlseg value) {
          byte[] buf = new byte[32];
          writeFloat64(buf, 0, value.point[0].x);
          writeFloat64(buf, 8, value.point[0].y);
          writeFloat64(buf, 16, value.point[1].x);
          writeFloat64(buf, 24, value.point[1].y);
          return buf;
        }
      };

  public static final PgBinary<PGpath> PATH =
      new PgBinary<>() {
        @Override
        public PGpath decode(byte[] raw, int offset, int len) {
          boolean open = raw[offset] == 0;
          int npoints = readInt32(raw, offset + 1);
          PGpoint[] points = new PGpoint[npoints];
          int pos = offset + 5;
          for (int i = 0; i < npoints; i++) {
            points[i] = new PGpoint(readFloat64(raw, pos), readFloat64(raw, pos + 8));
            pos += 16;
          }
          return new PGpath(points, open);
        }

        @Override
        public byte[] encode(PGpath value) {
          byte[] buf = new byte[5 + value.points.length * 16];
          buf[0] = (byte) (value.open ? 0 : 1);
          writeInt32(buf, 1, value.points.length);
          int pos = 5;
          for (PGpoint p : value.points) {
            writeFloat64(buf, pos, p.x);
            writeFloat64(buf, pos + 8, p.y);
            pos += 16;
          }
          return buf;
        }
      };

  public static final PgBinary<PGpolygon> POLYGON =
      new PgBinary<>() {
        @Override
        public PGpolygon decode(byte[] raw, int offset, int len) {
          int npoints = readInt32(raw, offset);
          PGpoint[] points = new PGpoint[npoints];
          int pos = offset + 4;
          for (int i = 0; i < npoints; i++) {
            points[i] = new PGpoint(readFloat64(raw, pos), readFloat64(raw, pos + 8));
            pos += 16;
          }
          return new PGpolygon(points);
        }

        @Override
        public byte[] encode(PGpolygon value) {
          byte[] buf = new byte[4 + value.points.length * 16];
          writeInt32(buf, 0, value.points.length);
          int pos = 4;
          for (PGpoint p : value.points) {
            writeFloat64(buf, pos, p.x);
            writeFloat64(buf, pos + 8, p.y);
            pos += 16;
          }
          return buf;
        }
      };

  // ========== Hstore ==========

  public static final PgBinary<Map<String, String>> HSTORE =
      new PgBinary<>() {
        @Override
        public Map<String, String> decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int count = readInt32(raw, pos);
          pos += 4;
          Map<String, String> result = new java.util.LinkedHashMap<>(count);
          for (int i = 0; i < count; i++) {
            int keyLen = readInt32(raw, pos);
            pos += 4;
            String key = new String(raw, pos, keyLen, StandardCharsets.UTF_8);
            pos += keyLen;
            int valLen = readInt32(raw, pos);
            pos += 4;
            String value;
            if (valLen == -1) {
              value = null;
            } else {
              value = new String(raw, pos, valLen, StandardCharsets.UTF_8);
              pos += valLen;
            }
            result.put(key, value);
          }
          return result;
        }

        @Override
        public byte[] encode(Map<String, String> value) {
          int totalLen = 4;
          byte[][] keys = new byte[value.size()][];
          byte[][] vals = new byte[value.size()][];
          int idx = 0;
          for (var entry : value.entrySet()) {
            keys[idx] = entry.getKey().getBytes(StandardCharsets.UTF_8);
            totalLen += 4 + keys[idx].length;
            if (entry.getValue() == null) {
              vals[idx] = null;
              totalLen += 4;
            } else {
              vals[idx] = entry.getValue().getBytes(StandardCharsets.UTF_8);
              totalLen += 4 + vals[idx].length;
            }
            idx++;
          }
          byte[] buf = new byte[totalLen];
          int pos = 0;
          writeInt32(buf, pos, value.size());
          pos += 4;
          for (int i = 0; i < keys.length; i++) {
            writeInt32(buf, pos, keys[i].length);
            pos += 4;
            System.arraycopy(keys[i], 0, buf, pos, keys[i].length);
            pos += keys[i].length;
            if (vals[i] == null) {
              writeInt32(buf, pos, -1);
              pos += 4;
            } else {
              writeInt32(buf, pos, vals[i].length);
              pos += 4;
              System.arraycopy(vals[i], 0, buf, pos, vals[i].length);
              pos += vals[i].length;
            }
          }
          return buf;
        }
      };

  // ========== Network types ==========

  public static final PgBinary<String> INET =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          int family = raw[offset] & 0xFF;
          int bits = raw[offset + 1] & 0xFF;
          // offset+2 = is_cidr
          int nb = raw[offset + 3] & 0xFF;
          byte[] addr = new byte[nb];
          System.arraycopy(raw, offset + 4, addr, 0, nb);
          try {
            String addrStr = InetAddress.getByAddress(addr).getHostAddress();
            if (family == 2 && bits != 32) {
              addrStr += "/" + bits;
            } else if (family == 3 && bits != 128) {
              addrStr += "/" + bits;
            }
            return addrStr;
          } catch (UnknownHostException e) {
            throw new IllegalStateException("Invalid network address", e);
          }
        }

        @Override
        public byte[] encode(String value) {
          return encodeInetCidr(value, false);
        }
      };

  public static final PgBinary<String> CIDR =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          int family = raw[offset] & 0xFF;
          int bits = raw[offset + 1] & 0xFF;
          // offset+2 = is_cidr
          int nb = raw[offset + 3] & 0xFF;
          byte[] addr = new byte[nb];
          System.arraycopy(raw, offset + 4, addr, 0, nb);
          try {
            String addrStr = InetAddress.getByAddress(addr).getHostAddress();
            return addrStr + "/" + bits;
          } catch (UnknownHostException e) {
            throw new IllegalStateException("Invalid CIDR address", e);
          }
        }

        @Override
        public byte[] encode(String value) {
          return encodeInetCidr(value, true);
        }
      };

  private static byte[] encodeInetCidr(String value, boolean isCidr) {
    String[] parts = value.split("/");
    String addr = parts[0];
    boolean isV6 = addr.contains(":");
    try {
      byte[] addrBytes = InetAddress.getByName(addr).getAddress();
      int mask;
      if (parts.length > 1) {
        mask = Integer.parseInt(parts[1]);
      } else {
        mask = isV6 ? 128 : 32;
      }
      byte family = (byte) (isV6 ? 3 : 2);
      byte[] buf = new byte[4 + addrBytes.length];
      buf[0] = family;
      buf[1] = (byte) mask;
      buf[2] = (byte) (isCidr ? 1 : 0);
      buf[3] = (byte) addrBytes.length;
      System.arraycopy(addrBytes, 0, buf, 4, addrBytes.length);
      return buf;
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("Invalid address: " + addr, e);
    }
  }

  public static final PgBinary<String> MACADDR =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          StringBuilder sb = new StringBuilder(17);
          for (int i = 0; i < 6; i++) {
            if (i > 0) sb.append(':');
            sb.append(String.format("%02x", raw[offset + i] & 0xFF));
          }
          return sb.toString();
        }

        @Override
        public byte[] encode(String value) {
          String[] parts = value.split(":");
          byte[] buf = new byte[6];
          for (int i = 0; i < 6; i++) {
            buf[i] = (byte) Integer.parseInt(parts[i], 16);
          }
          return buf;
        }
      };

  public static final PgBinary<String> MACADDR8 =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          StringBuilder sb = new StringBuilder(23);
          for (int i = 0; i < 8; i++) {
            if (i > 0) sb.append(':');
            sb.append(String.format("%02x", raw[offset + i] & 0xFF));
          }
          return sb.toString();
        }

        @Override
        public byte[] encode(String value) {
          String[] parts = value.split(":");
          byte[] buf = new byte[8];
          for (int i = 0; i < 8; i++) {
            buf[i] = (byte) Integer.parseInt(parts[i], 16);
          }
          return buf;
        }
      };

  // ========== Bit string ==========

  public static final PgBinary<String> BIT_STRING =
      new PgBinary<>() {
        @Override
        public String decode(byte[] raw, int offset, int len) {
          int bitCount = readInt32(raw, offset);
          StringBuilder sb = new StringBuilder(bitCount);
          int byteOffset = offset + 4;
          for (int i = 0; i < bitCount; i++) {
            int byteIdx = i / 8;
            int bitIdx = 7 - (i % 8);
            sb.append(((raw[byteOffset + byteIdx] >> bitIdx) & 1) == 1 ? '1' : '0');
          }
          return sb.toString();
        }

        @Override
        public byte[] encode(String value) {
          int bitCount = value.length();
          int byteCount = (bitCount + 7) / 8;
          byte[] buf = new byte[4 + byteCount];
          writeInt32(buf, 0, bitCount);
          for (int i = 0; i < bitCount; i++) {
            if (value.charAt(i) == '1') {
              int byteIdx = i / 8;
              int bitIdx = 7 - (i % 8);
              buf[4 + byteIdx] |= (byte) (1 << bitIdx);
            }
          }
          return buf;
        }
      };

  // ========== pgvector ==========

  public static final PgBinary<float[]> VECTOR_FLOATS =
      new PgBinary<>() {
        @Override
        public float[] decode(byte[] raw, int offset, int len) {
          int dim = ((raw[offset] & 0xFF) << 8) | (raw[offset + 1] & 0xFF);
          // skip 2 bytes unused
          float[] result = new float[dim];
          int pos = offset + 4;
          for (int i = 0; i < dim; i++) {
            result[i] = readFloat32(raw, pos);
            pos += 4;
          }
          return result;
        }

        @Override
        public byte[] encode(float[] value) {
          byte[] buf = new byte[4 + value.length * 4];
          writeInt16(buf, 0, value.length);
          writeInt16(buf, 2, 0);
          int pos = 4;
          for (float f : value) {
            writeFloat32(buf, pos, f);
            pos += 4;
          }
          return buf;
        }
      };

  // ========== Range ==========

  private static final int RANGE_EMPTY = 0x01;
  private static final int RANGE_LB_INC = 0x02;
  private static final int RANGE_UB_INC = 0x04;
  private static final int RANGE_LB_INF = 0x08;
  private static final int RANGE_UB_INF = 0x10;

  public static <T extends Comparable<? super T>> PgBinary<Range<T>> range(
      PgBinary<T> elementBinary,
      BiFunction<RangeBound<T>, RangeBound<T>, Range<T>> rangeFactory) {
    return new PgBinary<>() {
      @Override
      public Range<T> decode(byte[] raw, int offset, int len) {
        int flags = raw[offset] & 0xFF;
        if ((flags & RANGE_EMPTY) != 0) {
          return Range.empty();
        }
        int pos = offset + 1;

        RangeBound<T> lower;
        if ((flags & RANGE_LB_INF) != 0) {
          lower = RangeBound.infinite();
        } else {
          int elemLen = readInt32(raw, pos);
          pos += 4;
          T val = elementBinary.decode(raw, pos, elemLen);
          pos += elemLen;
          if ((flags & RANGE_LB_INC) != 0) {
            lower = new RangeBound.Closed<>(val);
          } else {
            lower = new RangeBound.Open<>(val);
          }
        }

        RangeBound<T> upper;
        if ((flags & RANGE_UB_INF) != 0) {
          upper = RangeBound.infinite();
        } else {
          int elemLen = readInt32(raw, pos);
          pos += 4;
          T val = elementBinary.decode(raw, pos, elemLen);
          pos += elemLen;
          if ((flags & RANGE_UB_INC) != 0) {
            upper = new RangeBound.Closed<>(val);
          } else {
            upper = new RangeBound.Open<>(val);
          }
        }

        return rangeFactory.apply(lower, upper);
      }

      @Override
      public byte[] encode(Range<T> value) {
        switch (value) {
          case Range.Empty<?> e -> {
            return new byte[] {RANGE_EMPTY};
          }
          case Range.NonEmpty<T> ne -> {
            int flags = 0;
            if (ne.from() instanceof RangeBound.Closed<?>) flags |= RANGE_LB_INC;
            if (ne.from() instanceof RangeBound.Infinite<?>) flags |= RANGE_LB_INF;
            if (ne.to() instanceof RangeBound.Closed<?>) flags |= RANGE_UB_INC;
            if (ne.to() instanceof RangeBound.Infinite<?>) flags |= RANGE_UB_INF;

            byte[] lowerEnc = null;
            if (!(ne.from() instanceof RangeBound.Infinite<?>)) {
              lowerEnc = elementBinary.encode(((RangeBound.Finite<T>) ne.from()).value());
            }
            byte[] upperEnc = null;
            if (!(ne.to() instanceof RangeBound.Infinite<?>)) {
              upperEnc = elementBinary.encode(((RangeBound.Finite<T>) ne.to()).value());
            }

            int totalLen = 1;
            if (lowerEnc != null) totalLen += 4 + lowerEnc.length;
            if (upperEnc != null) totalLen += 4 + upperEnc.length;

            byte[] buf = new byte[totalLen];
            buf[0] = (byte) flags;
            int pos = 1;
            if (lowerEnc != null) {
              writeInt32(buf, pos, lowerEnc.length);
              pos += 4;
              System.arraycopy(lowerEnc, 0, buf, pos, lowerEnc.length);
              pos += lowerEnc.length;
            }
            if (upperEnc != null) {
              writeInt32(buf, pos, upperEnc.length);
              pos += 4;
              System.arraycopy(upperEnc, 0, buf, pos, upperEnc.length);
            }
            return buf;
          }
        }
      }

      @Override
      public boolean prefersBinaryFormat() {
        return elementBinary.prefersBinaryFormat();
      }
    };
  }

  // ========== Unboxed primitive array instances ==========

  public static final PgBinary<boolean[]> BOOL_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public boolean[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; // has_null
          pos += 4; // element OID
          if (ndim == 0) return new boolean[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          boolean[] result = new boolean[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) { result[i] = raw[pos] != 0; pos += elemLen; }
          }
          return result;
        }

        @Override
        public byte[] encode(boolean[] value) {
          byte[] buf = new byte[20 + value.length * 5];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (boolean b : value) {
            writeInt32(buf, pos, 1); pos += 4;
            buf[pos] = (byte) (b ? 1 : 0); pos += 1;
          }
          return buf;
        }
      };

  public static final PgBinary<short[]> SHORT_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public short[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; pos += 4;
          if (ndim == 0) return new short[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          short[] result = new short[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) {
              result[i] = (short) (((raw[pos] & 0xFF) << 8) | (raw[pos + 1] & 0xFF));
              pos += elemLen;
            }
          }
          return result;
        }

        @Override
        public byte[] encode(short[] value) {
          byte[] buf = new byte[20 + value.length * 6];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (short s : value) {
            writeInt32(buf, pos, 2); pos += 4;
            writeInt16(buf, pos, s); pos += 2;
          }
          return buf;
        }
      };

  public static final PgBinary<int[]> INT_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public int[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; pos += 4;
          if (ndim == 0) return new int[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          int[] result = new int[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) { result[i] = readInt32(raw, pos); pos += elemLen; }
          }
          return result;
        }

        @Override
        public byte[] encode(int[] value) {
          byte[] buf = new byte[20 + value.length * 8];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (int v : value) {
            writeInt32(buf, pos, 4); pos += 4;
            writeInt32(buf, pos, v); pos += 4;
          }
          return buf;
        }
      };

  public static final PgBinary<long[]> LONG_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public long[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; pos += 4;
          if (ndim == 0) return new long[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          long[] result = new long[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) { result[i] = readInt64(raw, pos); pos += elemLen; }
          }
          return result;
        }

        @Override
        public byte[] encode(long[] value) {
          byte[] buf = new byte[20 + value.length * 12];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (long v : value) {
            writeInt32(buf, pos, 8); pos += 4;
            writeInt64(buf, pos, v); pos += 8;
          }
          return buf;
        }
      };

  public static final PgBinary<float[]> FLOAT_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public float[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; pos += 4;
          if (ndim == 0) return new float[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          float[] result = new float[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) { result[i] = readFloat32(raw, pos); pos += elemLen; }
          }
          return result;
        }

        @Override
        public byte[] encode(float[] value) {
          byte[] buf = new byte[20 + value.length * 8];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (float f : value) {
            writeInt32(buf, pos, 4); pos += 4;
            writeFloat32(buf, pos, f); pos += 4;
          }
          return buf;
        }
      };

  public static final PgBinary<double[]> DOUBLE_ARRAY_UNBOXED =
      new PgBinary<>() {
        @Override
        public double[] decode(byte[] raw, int offset, int len) {
          int pos = offset;
          int ndim = readInt32(raw, pos); pos += 4;
          pos += 4; pos += 4;
          if (ndim == 0) return new double[0];
          int total = 1;
          for (int d = 0; d < ndim; d++) { total *= readInt32(raw, pos); pos += 4; pos += 4; }
          double[] result = new double[total];
          for (int i = 0; i < total; i++) {
            int elemLen = readInt32(raw, pos); pos += 4;
            if (elemLen >= 0) { result[i] = readFloat64(raw, pos); pos += elemLen; }
          }
          return result;
        }

        @Override
        public byte[] encode(double[] value) {
          byte[] buf = new byte[20 + value.length * 12];
          int pos = 0;
          writeInt32(buf, pos, value.length > 0 ? 1 : 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, 0); pos += 4;
          writeInt32(buf, pos, value.length); pos += 4;
          writeInt32(buf, pos, 1); pos += 4;
          for (double d : value) {
            writeInt32(buf, pos, 8); pos += 4;
            writeFloat64(buf, pos, d); pos += 8;
          }
          return buf;
        }
      };
}
