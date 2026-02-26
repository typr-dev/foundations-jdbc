package dev.typr.foundations;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

/**
 * DuckDB STRUCT type support.
 *
 * <p>A STRUCT is an ordered sequence of named fields with typed values. Example: STRUCT(name
 * VARCHAR, age INTEGER)
 *
 * <p>In Java, we represent a STRUCT as a generated record class with typed fields. This class
 * provides the machinery to read/write STRUCTs via JDBC.
 *
 * @param <A> the Java type representing this STRUCT (typically a generated record)
 */
public record DuckDbStruct<A>(
    DuckDbTypename.StructOf<A> typename,
    List<Field<A, ?>> fields,
    StructReader<A> reader,
    StructWriter<A> writer,
    DuckDbJson<A> json) {
  /**
   * A single field in a STRUCT with a getter function.
   *
   * @param <A> the struct type
   * @param <F> the field value type
   */
  public record Field<A, F>(String name, DuckDbType<F> type, Function<A, F> getter) {}

  /** Functional interface for reading a STRUCT from field values. */
  @FunctionalInterface
  public interface StructReader<A> {
    A read(Object[] fieldValues) throws SQLException;
  }

  /** Functional interface for writing a STRUCT to field values. */
  @FunctionalInterface
  public interface StructWriter<A> {
    Object[] write(A value);
  }

  /** Create a DuckDbType for this STRUCT. */
  public DuckDbType<A> asType() {
    DuckDbRead<A> duckDbRead =
        DuckDbRead.of(
            (rs, idx) -> {
              Object obj = rs.getObject(idx);
              if (obj == null) return null;
              if (obj instanceof java.sql.Struct struct) {
                Object[] attrs = struct.getAttributes();
                return reader.read(attrs);
              }
              throw new SQLException("Expected STRUCT, got: " + obj.getClass());
            });

    DuckDbStringifier<A> stringifier = structStringifier();

    DuckDbWrite<A> duckDbWrite =
        new DuckDbWrite.Instance<>(
            (ps, idx, str) -> ps.setString(idx, str),
            value -> {
              StringBuilder sb = new StringBuilder();
              stringifier.unsafeEncode(value, sb, false);
              return sb.toString();
            });

    DuckDbMapSupport<A> structMapSupport =
        DuckDbMapSupport.of(
            raw -> {
              if (raw instanceof java.sql.Struct struct) {
                try {
                  return reader.read(struct.getAttributes());
                } catch (java.sql.SQLException e) {
                  throw new DatabaseException(e);
                }
              }
              throw new IllegalArgumentException("Expected STRUCT, got: " + raw.getClass());
            },
            value -> value);

    return new DuckDbType<>(typename.asGeneric(), duckDbRead, duckDbWrite, stringifier, json,
        structMapSupport, AnalysisOptions.EMPTY);
  }

  /** Create an optional version of this STRUCT type. */
  public DuckDbType<java.util.Optional<A>> opt() {
    return asType().opt();
  }

  private DuckDbStringifier<A> structStringifier() {
    return DuckDbStringifier.instance(
        (value, sb, quoted) -> {
          sb.append("{");
          for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append(", ");
            Field<A, ?> field = fields.get(i);
            sb.append("'").append(field.name()).append("': ");
            appendFieldValue(sb, value, field);
          }
          sb.append("}");
        });
  }

  /** Append a field value in DuckDB literal format using DuckDbStringifier. */
  private <F> void appendFieldValue(StringBuilder sb, A structValue, Field<A, F> field) {
    F value = field.getter().apply(structValue);
    if (value == null) {
      sb.append("NULL");
      return;
    }
    field.type().stringifier().unsafeEncode(value, sb, true);
  }

  // ========================================================================
  // Builder API for creating STRUCT types
  // ========================================================================

  /**
   * Create a type-safe STRUCT builder.
   *
   * <p>Usage:
   *
   * <pre>{@code
   * DuckDbStruct<Point> struct = DuckDbStruct.<Point>builder("point")
   *     .field("x", DuckDbTypes.double_, Point::x)
   *     .field("y", DuckDbTypes.double_, Point::y)
   *     .build(Point::new);
   * }</pre>
   *
   * @param <A> the struct type (typically a record)
   */
  public static <A> DuckDbStructBuilders.Builder0<A> builder(String structName) {
    return DuckDbStructBuilders.builder(structName);
  }
}
