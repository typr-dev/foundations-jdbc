package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * SQLite JSON codecs. SQLite has no native JSON type — JSON1 ({@code json()}, {@code
 * json_extract()}, etc.) operates on TEXT columns — so the codecs here mirror the standard
 * (non-database) JSON conversions used by the rest of the library.
 */
public interface SqliteJson<A> extends DbJson<A> {

  @Override
  default SqliteJson<Optional<A>> opt() {
    SqliteJson<A> self = this;
    return new SqliteJson<>() {
      @Override
      public JsonValue toJson(Optional<A> value) {
        return value.map(self::toJson).orElse(JsonValue.JNull.INSTANCE);
      }

      @Override
      public Optional<A> fromJson(JsonValue json) {
        if (json instanceof JsonValue.JNull) return Optional.empty();
        return Optional.of(self.fromJson(json));
      }
    };
  }

  default <B> SqliteJson<B> transform(SqlFunction<A, B> f, Function<B, A> g) {
    SqliteJson<A> self = this;
    return new SqliteJson<>() {
      @Override
      public JsonValue toJson(B value) {
        return self.toJson(g.apply(value));
      }

      @Override
      public B fromJson(JsonValue json) {
        try {
          return f.apply(self.fromJson(json));
        } catch (java.sql.SQLException e) {
          throw new DatabaseException.Jdbc(e);
        }
      }
    };
  }

  // ==================== Primitive codecs ====================

  SqliteJson<Boolean> bool =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Boolean value) {
          return JsonValue.JBool.of(value);
        }

        @Override
        public Boolean fromJson(JsonValue json) {
          if (json instanceof JsonValue.JBool(boolean v)) return v;
          if (json instanceof JsonValue.JNumber(String v)) return Integer.parseInt(v) != 0;
          throw new IllegalArgumentException(
              "Expected boolean, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Byte> int1 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Byte value) {
          return JsonValue.JNumber.of(value.longValue());
        }

        @Override
        public Byte fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Byte.parseByte(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Short> int2 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Short value) {
          return JsonValue.JNumber.of(value.longValue());
        }

        @Override
        public Short fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Short.parseShort(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Integer> int4 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Integer value) {
          return JsonValue.JNumber.of(value.longValue());
        }

        @Override
        public Integer fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Integer.parseInt(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Long> int8 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Long value) {
          return JsonValue.JNumber.of(value);
        }

        @Override
        public Long fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Long.parseLong(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Float> float4 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Float value) {
          return JsonValue.JNumber.of(value.doubleValue());
        }

        @Override
        public Float fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Float.parseFloat(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Double> float8 =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Double value) {
          return JsonValue.JNumber.of(value);
        }

        @Override
        public Double fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return Double.parseDouble(v);
          throw new IllegalArgumentException(
              "Expected number, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<BigDecimal> numeric =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(BigDecimal value) {
          return JsonValue.JNumber.of(value.toPlainString());
        }

        @Override
        public BigDecimal fromJson(JsonValue json) {
          if (json instanceof JsonValue.JNumber(String v)) return new BigDecimal(v);
          if (json instanceof JsonValue.JString(String v)) return new BigDecimal(v);
          throw new IllegalArgumentException(
              "Expected number or string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<String> text =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(String value) {
          return new JsonValue.JString(value);
        }

        @Override
        public String fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) return v;
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<byte[]> blob =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(byte[] value) {
          return new JsonValue.JString(Base64.getEncoder().encodeToString(value));
        }

        @Override
        public byte[] fromJson(JsonValue json) {
          if (!(json instanceof JsonValue.JString(String v))) {
            throw new IllegalArgumentException(
                "Expected string, got: " + json.getClass().getSimpleName());
          }
          return Base64.getDecoder().decode(v);
        }
      };

  SqliteJson<LocalDate> date =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(LocalDate value) {
          return new JsonValue.JString(value.toString());
        }

        @Override
        public LocalDate fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) return LocalDate.parse(v);
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<LocalTime> time =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(LocalTime value) {
          return new JsonValue.JString(value.toString());
        }

        @Override
        public LocalTime fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) return LocalTime.parse(v);
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<LocalDateTime> timestamp =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(LocalDateTime value) {
          return new JsonValue.JString(value.toString());
        }

        @Override
        public LocalDateTime fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) {
            return LocalDateTime.parse(v.replace(' ', 'T'));
          }
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Instant> instant =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Instant value) {
          return new JsonValue.JString(value.toString());
        }

        @Override
        public Instant fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) return Instant.parse(v);
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<UUID> uuid =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(UUID value) {
          return new JsonValue.JString(value.toString());
        }

        @Override
        public UUID fromJson(JsonValue json) {
          if (json instanceof JsonValue.JString(String v)) return UUID.fromString(v);
          throw new IllegalArgumentException(
              "Expected string, got: " + json.getClass().getSimpleName());
        }
      };

  SqliteJson<Json> json =
      new SqliteJson<>() {
        @Override
        public JsonValue toJson(Json value) {
          return JsonValue.parse(value.value());
        }

        @Override
        public Json fromJson(JsonValue json) {
          return new Json(json.encode());
        }
      };
}
