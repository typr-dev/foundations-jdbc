package dev.typr.foundations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.postgresql.util.PGobject;

/**
 * Unified bidirectional text codec for all three PostgreSQL text formats.
 *
 * <p>PostgreSQL has three distinct textual encoding contexts, each with different framing and
 * escaping rules but the same underlying value representation for scalar types:
 *
 * <ul>
 *   <li><b>COPY format</b> — {@link #unsafeEncode}/{@link #unsafeArrayEncode}: backslash-escaped
 *       for COPY FROM STDIN. Tab-delimited rows, {@code \N} for NULL.
 *   <li><b>Wire protocol</b> — {@link #wireEncode}/{@link #wireArrayEncode}/{@link #wireDecode}:
 *       raw text for the extended query protocol. Parameters are length-prefixed binary framing, so
 *       no escaping needed for scalar values.
 *   <li><b>Composite text</b> — {@link #compositeEncode}/{@link #compositeDecode}: for field values
 *       inside PostgreSQL composite types (records, structs). No escaping is applied here — quoting
 *       and escaping are handled by {@link PgRecordParser} at the container level. NULL is
 *       represented by {@link Optional#empty()}.
 * </ul>
 *
 * <p>For scalar types, wire and composite formats produce identical text representations — integers
 * are just digits, booleans are "t"/"f", UUIDs are hyphenated hex, etc. The {@code composite*}
 * methods delegate to the {@code wire*} methods by default, overriding only where the API shape
 * differs (e.g. NULL handling via Optional for composite).
 */
public abstract class PgText<A> implements DbText<A> {
  /** Encode value in PostgreSQL COPY text format (backslash-escaped for COPY FROM STDIN). */
  public abstract void unsafeEncode(A a, StringBuilder sb);

  /**
   * Encode value for use inside a COPY-format array literal (extra escaping for nested context).
   */
  public abstract void unsafeArrayEncode(A a, StringBuilder sb);

  /**
   * Encode value for PostgreSQL wire protocol text-format parameters.
   *
   * <p>Wire protocol parameters are length-prefixed binary, so no escaping is needed for scalar
   * values — the raw text representation is sent as-is. Array values use standard PG array literal
   * syntax ({elem,"elem with spaces",...}).
   *
   * <p>Default implementation delegates to {@link #unsafeEncode} which is correct for all
   * non-string scalar types (numbers, booleans, UUIDs, timestamps, etc). String types and array
   * types override this to avoid COPY-specific escaping.
   */
  public void wireEncode(A a, StringBuilder sb) {
    unsafeEncode(a, sb);
  }

  /**
   * Encode value for use inside a wire-protocol array literal. Elements that contain special
   * characters are double-quoted with backslash escaping for quotes and backslashes only.
   */
  public void wireArrayEncode(A a, StringBuilder sb) {
    wireEncode(a, sb);
  }

  /**
   * Decode a value from PostgreSQL wire-protocol text format (DataRow text representation).
   *
   * <p>The text representation is PostgreSQL's standard text output format — the same format
   * returned by a simple SELECT. For scalars: "42" for int4, "t"/"f" for bool, standard ISO format
   * for dates/times, "{1,2,3}" for arrays, "\x..." for bytea, etc.
   *
   * <p>Null columns are never passed to this method — null is indicated by -1 length in the wire
   * protocol and handled at the caller level.
   *
   * @param text the text representation from a DataRow message (never null)
   * @return the decoded Java value
   */
  public A wireDecode(String text) {
    throw new UnsupportedOperationException("wireDecode not implemented for this PgText instance");
  }

  /**
   * Decode a value directly from raw UTF-8 bytes, avoiding intermediate String creation where
   * possible.
   *
   * <p>Default implementation creates a String and delegates to {@link #wireDecode(String)}.
   * Overridden by types that can parse directly from bytes (int, long, bool, etc.).
   */
  public A wireDecodeBytes(byte[] raw, int offset, int len) {
    return wireDecode(new String(raw, offset, len, java.nio.charset.StandardCharsets.UTF_8));
  }

  /**
   * Decode a value from PostgreSQL binary wire format (result format code = 1).
   *
   * <p>Binary format is type-specific: int4 is 4 bytes big-endian, bool is 1 byte (0/1), text is
   * raw UTF-8, timestamp is 8 bytes (microseconds since 2000-01-01 00:00:00 UTC), etc.
   *
   * <p>Default throws — only types with binary support should override.
   */
  public A binaryDecode(byte[] raw, int offset, int len) {
    throw new UnsupportedOperationException(
        "binaryDecode not implemented for " + getClass().getSimpleName());
  }

  // ========== Composite text format ==========

  /**
   * Encode value for use inside PostgreSQL composite types (records, structs).
   *
   * <p>Returns {@link Optional#empty()} to represent SQL NULL. No escaping is applied — escaping
   * and quoting are handled by {@link PgRecordParser} at the container level.
   *
   * <p>Default delegates to {@link #wireEncode} since composite and wire text representations are
   * identical for scalar types.
   */
  public Optional<String> compositeEncode(A value) {
    StringBuilder sb = new StringBuilder();
    wireEncode(value, sb);
    return Optional.of(sb.toString());
  }

  /**
   * Decode a value from its composite type text representation.
   *
   * <p>Default delegates to {@link #wireDecode} since composite and wire text representations are
   * identical for scalar types.
   */
  public A compositeDecode(String text) {
    return wireDecode(text);
  }

  /**
   * Create a {@link PgCompositeText} adapter backed by this PgText's composite methods.
   *
   * <p>Useful for bridging code that still accepts {@link PgCompositeText} directly.
   */
  public PgCompositeText<A> asCompositeText() {
    var self = this;
    return new PgCompositeText<>() {
      @Override
      public Optional<String> encode(A value) {
        return self.compositeEncode(value);
      }

      @Override
      public A decode(String text) {
        return self.compositeDecode(text);
      }
    };
  }

  /**
   * Returns true if this PgText represents an array/list type. Used by the list combinator to
   * detect nested arrays for proper multi-dimensional array encoding.
   */
  boolean isArrayType() {
    return false;
  }

  /** Encode-only contramap. Does NOT propagate wireDecode (use {@link #bimap} for that). */
  public <B> PgText<B> contramap(Function<B, A> f) {
    var self = this;
    return new PgText<>() {
      @Override
      public void unsafeEncode(B b, StringBuilder sb) {
        self.unsafeEncode(f.apply(b), sb);
      }

      @Override
      public void unsafeArrayEncode(B b, StringBuilder sb) {
        self.unsafeArrayEncode(f.apply(b), sb);
      }

      @Override
      public void wireEncode(B b, StringBuilder sb) {
        self.wireEncode(f.apply(b), sb);
      }

      @Override
      public void wireArrayEncode(B b, StringBuilder sb) {
        self.wireArrayEncode(f.apply(b), sb);
      }

      @Override
      public Optional<String> compositeEncode(B b) {
        return self.compositeEncode(f.apply(b));
      }
    };
  }

  /**
   * Bidirectional transform. Propagates both encoding (via contramapFn) and decoding (via mapFn).
   * Use this instead of {@link #contramap} when wire decode support is needed.
   *
   * @param mapFn decode direction: A → B (applied after wireDecode)
   * @param contramapFn encode direction: B → A (applied before wireEncode)
   */
  public <B> PgText<B> bimap(Function<A, B> mapFn, Function<B, A> contramapFn) {
    var self = this;
    return new PgText<>() {
      @Override
      public void unsafeEncode(B b, StringBuilder sb) {
        self.unsafeEncode(contramapFn.apply(b), sb);
      }

      @Override
      public void unsafeArrayEncode(B b, StringBuilder sb) {
        self.unsafeArrayEncode(contramapFn.apply(b), sb);
      }

      @Override
      public void wireEncode(B b, StringBuilder sb) {
        self.wireEncode(contramapFn.apply(b), sb);
      }

      @Override
      public void wireArrayEncode(B b, StringBuilder sb) {
        self.wireArrayEncode(contramapFn.apply(b), sb);
      }

      @Override
      public B wireDecode(String text) {
        return mapFn.apply(self.wireDecode(text));
      }

      @Override
      public B wireDecodeBytes(byte[] raw, int offset, int len) {
        return mapFn.apply(self.wireDecodeBytes(raw, offset, len));
      }

      @Override
      public B binaryDecode(byte[] raw, int offset, int len) {
        return mapFn.apply(self.binaryDecode(raw, offset, len));
      }

      @Override
      public Optional<String> compositeEncode(B b) {
        return self.compositeEncode(contramapFn.apply(b));
      }

      @Override
      public B compositeDecode(String text) {
        return mapFn.apply(self.compositeDecode(text));
      }
    };
  }

  public PgText<Optional<A>> opt() {
    var self = this;
    return new PgText<>() {
      @Override
      public void unsafeEncode(Optional<A> a, StringBuilder sb) {
        if (a.isPresent()) self.unsafeEncode(a.get(), sb);
        else sb.append(PgText.NULL);
      }

      @Override
      public void unsafeArrayEncode(Optional<A> a, StringBuilder sb) {
        if (a.isPresent()) self.unsafeArrayEncode(a.get(), sb);
        else sb.append(PgText.NULL);
      }

      @Override
      public void wireEncode(Optional<A> a, StringBuilder sb) {
        if (a.isPresent()) self.wireEncode(a.get(), sb);
        // null optionals are handled at the caller level (not encoded, sent as -1 length)
      }

      @Override
      public void wireArrayEncode(Optional<A> a, StringBuilder sb) {
        if (a.isPresent()) self.wireArrayEncode(a.get(), sb);
        else sb.append("NULL");
      }

      @Override
      public Optional<A> wireDecode(String text) {
        return Optional.of(self.wireDecode(text));
      }

      @Override
      public Optional<A> wireDecodeBytes(byte[] raw, int offset, int len) {
        return Optional.of(self.wireDecodeBytes(raw, offset, len));
      }

      @Override
      public Optional<A> binaryDecode(byte[] raw, int offset, int len) {
        return Optional.of(self.binaryDecode(raw, offset, len));
      }

      @Override
      public Optional<String> compositeEncode(Optional<A> a) {
        return a.flatMap(self::compositeEncode);
      }

      @Override
      public Optional<A> compositeDecode(String text) {
        if (text == null) return Optional.empty();
        return Optional.of(self.compositeDecode(text));
      }
    };
  }

  public PgText<java.util.List<A>> list() {
    return list(',');
  }

  /**
   * Internal: list/array encoding with a specific delimiter. The delimiter is a property of the
   * element type (geometric types use ';', everything else ','). Users should call {@link #list()}
   * which uses the correct delimiter via {@link PgType#array()}.
   */
  PgText<java.util.List<A>> list(char delimiter) {
    var self = this;
    // Detect if elements are themselves arrays (nested list). For nested arrays,
    // PG multi-dimensional array syntax uses raw nested braces {}, not quoted elements.
    boolean elementsAreArrays = self.isArrayType();
    return new PgText<>() {
      @Override
      public void unsafeEncode(java.util.List<A> as, StringBuilder sb) {
        var first = true;
        sb.append("{");
        for (var a : as) {
          if (first) first = false;
          else sb.append(delimiter);
          self.unsafeArrayEncode(a, sb);
        }
        sb.append('}');
      }

      @Override
      public void unsafeArrayEncode(java.util.List<A> as, StringBuilder sb) {
        unsafeEncode(as, sb);
      }

      @Override
      public void wireEncode(java.util.List<A> as, StringBuilder sb) {
        sb.append("{");
        for (int i = 0; i < as.size(); i++) {
          if (i > 0) sb.append(delimiter);
          A a = as.get(i);
          if (a == null) {
            sb.append("NULL");
          } else if (elementsAreArrays) {
            self.wireEncode(a, sb);
          } else {
            var tmp = new StringBuilder();
            self.wireArrayEncode(a, tmp);
            wireArrayQuote(tmp, sb);
          }
        }
        sb.append('}');
      }

      @Override
      boolean isArrayType() {
        return true;
      }

      @Override
      public java.util.List<A> wireDecode(String text) {
        // PG text wire format for arrays: {elem1,elem2,...} (or {elem1;elem2;...} for geometric
        // types)
        java.util.List<String> elements = PgRecordParser.parseArray(text, delimiter);
        java.util.List<A> result = new java.util.ArrayList<>(elements.size());
        for (String elem : elements) {
          result.add(elem == null ? null : self.wireDecode(elem));
        }
        return result;
      }

      @Override
      public java.util.List<A> wireDecodeBytes(byte[] raw, int offset, int len) {
        return wireDecode(new String(raw, offset, len, java.nio.charset.StandardCharsets.UTF_8));
      }
    };
  }

  /** Encode-only array combinator. Use {@link #array(IntFunction)} for decode support. */
  public PgText<A[]> array() {
    return arrayImpl(null, ',');
  }

  /**
   * Bidirectional array combinator with decode support.
   *
   * @param arrayFactory creates arrays of the element type (e.g. {@code Integer[]::new})
   */
  public PgText<A[]> array(IntFunction<A[]> arrayFactory) {
    return arrayImpl(arrayFactory, ',');
  }

  /**
   * Bidirectional array combinator with decode support and custom composite delimiter.
   *
   * <p>PostgreSQL uses semicolon ({@code ;}) as the array delimiter for geometric types (box[],
   * circle[], line[], lseg[], path[], point[], polygon[]) because their element text
   * representations contain commas. The delimiter affects all array formats — wire protocol,
   * composite text, and COPY.
   *
   * @param arrayFactory creates arrays of the element type
   * @param compositeDelimiter delimiter for composite text arrays (default {@code ,})
   */
  public PgText<A[]> array(IntFunction<A[]> arrayFactory, char compositeDelimiter) {
    return arrayImpl(arrayFactory, compositeDelimiter);
  }

  private PgText<A[]> arrayImpl(IntFunction<A[]> arrayFactory, char compositeDelimiter) {
    var self = this;
    return new PgText<>() {
      @Override
      public void unsafeEncode(A[] as, StringBuilder sb) {
        var first = true;
        sb.append("{");
        for (var a : as) {
          if (first) first = false;
          else sb.append(',');
          self.unsafeArrayEncode(a, sb);
        }
        sb.append('}');
      }

      @Override
      public void unsafeArrayEncode(A[] as, StringBuilder sb) {
        unsafeEncode(as, sb);
      }

      @Override
      public void wireEncode(A[] as, StringBuilder sb) {
        sb.append('{');
        for (int i = 0; i < as.length; i++) {
          if (i > 0) sb.append(compositeDelimiter);
          if (as[i] == null) {
            sb.append("NULL");
          } else {
            var tmp = new StringBuilder();
            self.wireArrayEncode(as[i], tmp);
            wireArrayQuote(tmp, sb);
          }
        }
        sb.append('}');
      }

      @Override
      public void wireArrayEncode(A[] as, StringBuilder sb) {
        wireEncode(as, sb);
      }

      @Override
      public A[] wireDecode(String text) {
        if (arrayFactory == null) {
          throw new UnsupportedOperationException(
              "wireDecode requires array(IntFunction) — no array factory provided");
        }
        List<String> elements = PgRecordParser.parseArray(text, compositeDelimiter);
        A[] result = arrayFactory.apply(elements.size());
        for (int i = 0; i < elements.size(); i++) {
          String elem = elements.get(i);
          result[i] = elem == null ? null : self.wireDecode(elem);
        }
        return result;
      }

      @Override
      public Optional<String> compositeEncode(A[] values) {
        java.util.List<A> list = java.util.Arrays.asList(values);
        return Optional.of(
            PgRecordParser.encodeArray(
                list, v -> self.compositeEncode(v).orElse(null), compositeDelimiter));
      }

      @Override
      public A[] compositeDecode(String text) {
        if (arrayFactory == null) {
          throw new UnsupportedOperationException(
              "compositeDecode requires array(IntFunction) — no array factory provided");
        }
        List<String> elements = PgRecordParser.parseArray(text, compositeDelimiter);
        A[] result = arrayFactory.apply(elements.size());
        for (int i = 0; i < elements.size(); i++) {
          String elem = elements.get(i);
          result[i] = elem == null ? null : self.compositeDecode(elem);
        }
        return result;
      }
    };
  }

  public static char DELIMETER = '\t';
  public static String NULL = "\\N";

  /** Encode-only factory. */
  public static <A> PgText<A> instance(BiConsumer<A, StringBuilder> f) {
    return instance(f, f);
  }

  /** Encode-only factory with separate array encoder. */
  public static <A> PgText<A> instance(
      BiConsumer<A, StringBuilder> f, BiConsumer<A, StringBuilder> arrayF) {
    return new PgText<>() {
      @Override
      public void unsafeEncode(A a, StringBuilder sb) {
        f.accept(a, sb);
      }

      @Override
      public void unsafeArrayEncode(A a, StringBuilder sb) {
        arrayF.accept(a, sb);
      }
    };
  }

  /** Bidirectional factory: encode + decode. */
  public static <A> PgText<A> codec(
      BiConsumer<A, StringBuilder> encoder, Function<String, A> decoder) {
    return new PgText<>() {
      @Override
      public void unsafeEncode(A a, StringBuilder sb) {
        encoder.accept(a, sb);
      }

      @Override
      public void unsafeArrayEncode(A a, StringBuilder sb) {
        encoder.accept(a, sb);
      }

      @Override
      public A wireDecode(String text) {
        return decoder.apply(text);
      }
    };
  }

  @SuppressWarnings("unchecked")
  public static <A> PgText<A> from(RowCodec<A> rowCodec) {
    return instance(
        (row, sb) -> {
          var encoded = rowCodec.encode().apply(row);
          for (int i = 0; i < encoded.length; i++) {
            if (i > 0) {
              sb.append(PgText.DELIMETER);
            }
            DbText<Object> text = (DbText<Object>) rowCodec.columns().get(i).text().orElseThrow();
            text.unsafeEncode(encoded[i], sb);
          }
        });
  }

  public static <A> PgText<A> instanceToString() {
    return textString.contramap(Object::toString);
  }

  // ========== Standard instances ==========

  public static final PgText<String> textString =
      new PgText<>() {
        @Override
        public void unsafeEncode(String s, StringBuilder sb) {
          StringImpl.unsafeEncode(s, sb);
        }

        @Override
        public void unsafeArrayEncode(String s, StringBuilder sb) {
          StringImpl.unsafeArrayEncode(s, sb);
        }

        @Override
        public void wireEncode(String s, StringBuilder sb) {
          sb.append(s);
        }

        @Override
        public void wireArrayEncode(String s, StringBuilder sb) {
          sb.append(s);
        }

        @Override
        public String wireDecode(String text) {
          return text;
        }

        @Override
        public String binaryDecode(byte[] raw, int offset, int len) {
          return new String(raw, offset, len, java.nio.charset.StandardCharsets.UTF_8);
        }

        @Override
        public Optional<String> compositeEncode(String s) {
          return Optional.of(s);
        }

        @Override
        public String compositeDecode(String text) {
          return text;
        }
      };

  public static final PgText<Integer> textInteger =
      new PgText<>() {
        @Override
        public void unsafeEncode(Integer n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public void unsafeArrayEncode(Integer n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public Integer wireDecode(String text) {
          return Integer.parseInt(text);
        }

        @Override
        public Integer wireDecodeBytes(byte[] raw, int offset, int len) {
          return parseIntFromBytes(raw, offset, len);
        }

        @Override
        public Integer binaryDecode(byte[] raw, int offset, int len) {
          return ((raw[offset] & 0xFF) << 24)
              | ((raw[offset + 1] & 0xFF) << 16)
              | ((raw[offset + 2] & 0xFF) << 8)
              | (raw[offset + 3] & 0xFF);
        }
      };

  public static final PgText<Short> textShort =
      new PgText<>() {
        @Override
        public void unsafeEncode(Short n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public void unsafeArrayEncode(Short n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public Short wireDecode(String text) {
          return Short.parseShort(text);
        }

        @Override
        public Short wireDecodeBytes(byte[] raw, int offset, int len) {
          return (short) parseIntFromBytes(raw, offset, len);
        }
      };

  public static final PgText<Long> textLong =
      new PgText<>() {
        @Override
        public void unsafeEncode(Long n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public void unsafeArrayEncode(Long n, StringBuilder sb) {
          sb.append(n);
        }

        @Override
        public Long wireDecode(String text) {
          return Long.parseLong(text);
        }

        @Override
        public Long wireDecodeBytes(byte[] raw, int offset, int len) {
          return parseLongFromBytes(raw, offset, len);
        }
      };

  public static final PgText<Float> textFloat = codec((n, sb) -> sb.append(n), Float::parseFloat);

  public static final PgText<Double> textDouble =
      codec((n, sb) -> sb.append(n), Double::parseDouble);

  public static final PgText<BigDecimal> textBigDecimal =
      codec((n, sb) -> sb.append(n), BigDecimal::new);

  public static final PgText<Boolean> textBoolean =
      new PgText<>() {
        @Override
        public void unsafeEncode(Boolean b, StringBuilder sb) {
          sb.append(b ? "t" : "f");
        }

        @Override
        public void unsafeArrayEncode(Boolean b, StringBuilder sb) {
          sb.append(b ? "t" : "f");
        }

        @Override
        public Boolean wireDecode(String text) {
          return text.equals("t") || text.equals("true") || text.equals("1");
        }

        @Override
        public Boolean wireDecodeBytes(byte[] raw, int offset, int len) {
          return len > 0 && raw[offset] == 't';
        }

        @Override
        public Boolean binaryDecode(byte[] raw, int offset, int len) {
          return raw[offset] != 0;
        }
      };

  public static final PgText<UUID> textUuid = codec((n, sb) -> sb.append(n), UUID::fromString);

  public static final PgText<byte[]> textByteArray =
      new PgText<>() {
        @Override
        public void unsafeEncode(byte[] bs, StringBuilder sb) {
          sb.append("\\\\x");
          appendHex(bs, sb);
        }

        @Override
        public void unsafeArrayEncode(byte[] bs, StringBuilder sb) {
          unsafeEncode(bs, sb);
        }

        @Override
        public void wireEncode(byte[] bs, StringBuilder sb) {
          sb.append("\\x");
          appendHex(bs, sb);
        }

        @Override
        public void wireArrayEncode(byte[] bs, StringBuilder sb) {
          wireEncode(bs, sb);
        }

        @Override
        public byte[] wireDecode(String text) {
          String hex;
          if (text.startsWith("\\x")) {
            hex = text.substring(2);
          } else if (text.startsWith("\\\\x")) {
            hex = text.substring(3);
          } else {
            throw new IllegalArgumentException("Invalid bytea format: " + text);
          }
          if (hex.isEmpty()) return new byte[0];
          byte[] result = new byte[hex.length() / 2];
          for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
          }
          return result;
        }

        private void appendHex(byte[] bs, StringBuilder sb) {
          if (bs.length > 0) {
            var hex = new BigInteger(1, bs).toString(16);
            var pad = bs.length * 2 - hex.length();
            sb.append("0".repeat(Math.max(0, pad)));
            sb.append(hex);
          }
        }
      };

  public static <T extends PGobject> PgText<T> textPGobject(SqlFunction<String, T> constructor) {
    return PgText.textString.bimap(
        text -> {
          try {
            return constructor.apply(text);
          } catch (java.sql.SQLException e) {
            throw new DatabaseException.Jdbc("Failed to parse PGobject from text: " + text, e);
          }
        },
        x -> {
          if (x == null || x.isNull()) return "null";
          else return x.toString();
        });
  }

  public static final PgText<Map<String, String>> textMapStringString =
      new PgText<>() {
        @Override
        public void unsafeEncode(Map<String, String> m, StringBuilder sb) {
          var first = true;
          for (var e : m.entrySet()) {
            if (first) first = false;
            else sb.append(',');
            StringImpl.unsafeEncode(e.getKey(), sb);
            sb.append("=>");
            StringImpl.unsafeEncode(e.getValue(), sb);
          }
        }

        @Override
        public void unsafeArrayEncode(Map<String, String> m, StringBuilder sb) {
          unsafeEncode(m, sb);
        }

        @Override
        public Map<String, String> wireDecode(String text) {
          return PgCompositeText.hstore.decode(text);
        }
      };

  private interface StringImpl {
    static void stdChar(char c, StringBuilder sb) {
      switch (c) {
        case '\b':
          sb.append("\\b");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case 0x0b:
          sb.append("\\v");
          break;
        default:
          sb.append(c);
          break;
      }
    }

    static void unsafeEncode(String s, StringBuilder sb) {
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c == '\\') {
          sb.append("\\\\");
        } else {
          stdChar(c, sb);
        }
      }
    }

    // I am not confident about this encoder. Postgres seems not to be able to cope with low
    // control characters or high whitespace characters so these are simply filtered out in the
    // tests. It should accommodate arrays of non-pathological strings but it would be nice to
    // have a complete specification of what's actually happening.
    static void unsafeArrayEncode(String s, StringBuilder sb) {
      sb.append('"');
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
          case '\"':
            sb.append("\\\\\\\"");
            break;
          case '\\':
            sb.append("\\\\\\\\\\\\\\\\"); // srsly
            break;
          default:
            stdChar(c, sb);
            break;
        }
      }
      sb.append('"');
    }
  }

  /**
   * Quote a wire-protocol array element if it contains special characters. PG array syntax requires
   * double-quoting elements that contain commas, braces, quotes, backslashes, whitespace, or match
   * "NULL". Inside double quotes, only quotes and backslashes are escaped with a single backslash.
   */
  static void wireArrayQuote(StringBuilder element, StringBuilder out) {
    String text = element.toString();
    boolean needsQuoting =
        text.isEmpty() || text.equalsIgnoreCase("NULL") || needsWireArrayQuoting(text);
    if (!needsQuoting) {
      out.append(text);
    } else {
      out.append('"');
      for (int i = 0; i < text.length(); i++) {
        char c = text.charAt(i);
        if (c == '"' || c == '\\') out.append('\\');
        out.append(c);
      }
      out.append('"');
    }
  }

  static int parseIntFromBytes(byte[] raw, int offset, int len) {
    int result = 0;
    boolean negative = false;
    int i = offset;
    int end = offset + len;
    if (i < end && raw[i] == '-') {
      negative = true;
      i++;
    }
    while (i < end) {
      result = result * 10 + (raw[i++] - '0');
    }
    return negative ? -result : result;
  }

  static long parseLongFromBytes(byte[] raw, int offset, int len) {
    long result = 0;
    boolean negative = false;
    int i = offset;
    int end = offset + len;
    if (i < end && raw[i] == '-') {
      negative = true;
      i++;
    }
    while (i < end) {
      result = result * 10 + (raw[i++] - '0');
    }
    return negative ? -result : result;
  }

  private static boolean needsWireArrayQuoting(String text) {
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == ',' || c == '{' || c == '}' || c == '"' || c == '\\' || c == ' ' || c == '\t'
          || c == '\n') return true;
    }
    return false;
  }

  public static final PgText<Object> NotWorking =
      new PgText<>() {
        @Override
        public void unsafeEncode(Object t, StringBuilder sb) {
          throw new UnsupportedOperationException("streaming COPY is not supported for this type");
        }

        @Override
        public void unsafeArrayEncode(Object t, StringBuilder sb) {
          throw new UnsupportedOperationException("streaming COPY is not supported for this type");
        }
      };

  @Deprecated
  public static <T> PgText<T> NotWorking() {
    return (PgText<T>) NotWorking;
  }

  // ========== Unboxed primitive array codecs ==========

  public static final PgText<boolean[]> boolArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(boolean[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i] ? 't' : 'f');
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(boolean[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public boolean[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          boolean[] result = new boolean[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e != null && (e.equals("t") || e.equals("true") || e.equals("1"));
          }
          return result;
        }
      };

  public static final PgText<short[]> shortArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(short[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i]);
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(short[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public short[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          short[] result = new short[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e == null ? 0 : Short.parseShort(e);
          }
          return result;
        }
      };

  public static final PgText<int[]> intArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(int[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i]);
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(int[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public int[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          int[] result = new int[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e == null ? 0 : Integer.parseInt(e);
          }
          return result;
        }
      };

  public static final PgText<long[]> longArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(long[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i]);
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(long[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public long[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          long[] result = new long[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e == null ? 0L : Long.parseLong(e);
          }
          return result;
        }
      };

  public static final PgText<float[]> floatArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(float[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i]);
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(float[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public float[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          float[] result = new float[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e == null ? 0.0f : Float.parseFloat(e);
          }
          return result;
        }
      };

  public static final PgText<double[]> doubleArrayUnboxed =
      new PgText<>() {
        @Override
        public void unsafeEncode(double[] arr, StringBuilder sb) {
          sb.append('{');
          for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(arr[i]);
          }
          sb.append('}');
        }

        @Override
        public void unsafeArrayEncode(double[] arr, StringBuilder sb) {
          unsafeEncode(arr, sb);
        }

        @Override
        public double[] wireDecode(String text) {
          List<String> elements = PgRecordParser.parseArray(text);
          double[] result = new double[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
            String e = elements.get(i);
            result[i] = e == null ? 0.0 : Double.parseDouble(e);
          }
          return result;
        }
      };
}
