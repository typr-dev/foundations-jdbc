package dev.typr.foundations;

import dev.typr.foundations.data.JsonValue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed class RowCodec<Row> permits RowCodecNamed, RowCodecUnnamed {
  private final List<DbType<?>> columns;
  private final Function<Object[], Row> decode;
  private final Function<Row, Object[]> encode;

  RowCodec(
      List<DbType<?>> columns, Function<Object[], Row> decode, Function<Row, Object[]> encode) {
    this.columns = columns;
    this.decode = decode;
    this.encode = encode;
  }

  public List<DbType<?>> columns() {
    return columns;
  }

  public Function<Object[], Row> decode() {
    return decode;
  }

  public Function<Row, Object[]> encode() {
    return encode;
  }

  /**
   * Create a RowCodec without column names. Used by generated builders and Kotlin/Scala wrappers.
   */
  public static <Row> RowCodec<Row> create(
      List<DbType<?>> columns, Function<Object[], Row> decode, Function<Row, Object[]> encode) {
    return new RowCodecUnnamed<>(columns, decode, encode);
  }

  /**
   * Create a RowCodec with column names. Used by generated named builders and Kotlin/Scala
   * wrappers.
   */
  public static <Row> RowCodecNamed<Row> createNamed(
      List<String> columnNames,
      List<DbType<?>> columns,
      Function<Object[], Row> decode,
      Function<Row, Object[]> encode) {
    return new RowCodecNamed<>(columnNames, columns, decode, encode);
  }

  /**
   * Create a type-safe row codec builder.
   *
   * <p>Usage:
   *
   * <pre>{@code
   * RowCodec<Product> codec = RowCodec.<Product>builder()
   *     .field(PgTypes.int4, Product::id)
   *     .field(PgTypes.text, Product::name)
   *     .build(Product::new);
   * }</pre>
   *
   * @param <Row> the row type (typically a record)
   * @return a type-safe builder
   */
  public static <Row> RowCodecBuilders.Builder0<Row> builder() {
    return RowCodecBuilders.builder();
  }

  /**
   * Create a type-safe named row codec builder.
   *
   * <p>Usage:
   *
   * <pre>{@code
   * RowCodecNamed<Product> codec = RowCodec.<Product>namedBuilder()
   *     .field("id", PgTypes.int4, Product::id)
   *     .field("name", PgTypes.text, Product::name)
   *     .build(Product::new);
   * }</pre>
   *
   * @param <Row> the row type (typically a record)
   * @return a type-safe named builder
   */
  public static <Row> RowCodecNamedBuilders.Builder0<Row> namedBuilder() {
    return RowCodecNamedBuilders.builder();
  }

  /**
   * Create a single-column row codec.
   *
   * @param type the column type
   * @return a row codec that returns the column value directly
   */
  @SuppressWarnings("unchecked")
  public static <T> RowCodec<T> of(DbType<T> type) {
    return new RowCodecUnnamed<>(List.of(type), arr -> (T) arr[0], t -> new Object[] {t});
  }

  /** Create a multi-column row codec returning a Tuple2. */
  public static <T0, T1> RowCodec<Tuple.Tuple2<T0, T1>> of(DbType<T0> t0, DbType<T1> t1) {
    return RowCodecOf.of(t0, t1);
  }

  /** Create a multi-column row codec returning a Tuple3. */
  public static <T0, T1, T2> RowCodec<Tuple.Tuple3<T0, T1, T2>> of(
      DbType<T0> t0, DbType<T1> t1, DbType<T2> t2) {
    return RowCodecOf.of(t0, t1, t2);
  }

  /** Create a multi-column row codec returning a Tuple4. */
  public static <T0, T1, T2, T3> RowCodec<Tuple.Tuple4<T0, T1, T2, T3>> of(
      DbType<T0> t0, DbType<T1> t1, DbType<T2> t2, DbType<T3> t3) {
    return RowCodecOf.of(t0, t1, t2, t3);
  }

  /** Create a multi-column row codec returning a Tuple5. */
  public static <T0, T1, T2, T3, T4> RowCodec<Tuple.Tuple5<T0, T1, T2, T3, T4>> of(
      DbType<T0> t0, DbType<T1> t1, DbType<T2> t2, DbType<T3> t3, DbType<T4> t4) {
    return RowCodecOf.of(t0, t1, t2, t3, t4);
  }

  /** Create a multi-column row codec returning a Tuple6. */
  public static <T0, T1, T2, T3, T4, T5> RowCodec<Tuple.Tuple6<T0, T1, T2, T3, T4, T5>> of(
      DbType<T0> t0, DbType<T1> t1, DbType<T2> t2, DbType<T3> t3, DbType<T4> t4, DbType<T5> t5) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5);
  }

  /** Create a multi-column row codec returning a Tuple7. */
  public static <T0, T1, T2, T3, T4, T5, T6> RowCodec<Tuple.Tuple7<T0, T1, T2, T3, T4, T5, T6>> of(
      DbType<T0> t0,
      DbType<T1> t1,
      DbType<T2> t2,
      DbType<T3> t3,
      DbType<T4> t4,
      DbType<T5> t5,
      DbType<T6> t6) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6);
  }

  /** Create a multi-column row codec returning a Tuple8. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7>
      RowCodec<Tuple.Tuple8<T0, T1, T2, T3, T4, T5, T6, T7>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7);
  }

  /** Create a multi-column row codec returning a Tuple9. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8>
      RowCodec<Tuple.Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8);
  }

  /** Create a multi-column row codec returning a Tuple10. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
      RowCodec<Tuple.Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9);
  }

  /** Create a multi-column row codec returning a Tuple11. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
      RowCodec<Tuple.Tuple11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9,
          DbType<T10> t10) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
  }

  /** Create a multi-column row codec returning a Tuple12. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
      RowCodec<Tuple.Tuple12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9,
          DbType<T10> t10,
          DbType<T11> t11) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
  }

  /** Create a multi-column row codec returning a Tuple13. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
      RowCodec<Tuple.Tuple13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9,
          DbType<T10> t10,
          DbType<T11> t11,
          DbType<T12> t12) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
  }

  /** Create a multi-column row codec returning a Tuple14. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
      RowCodec<Tuple.Tuple14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9,
          DbType<T10> t10,
          DbType<T11> t11,
          DbType<T12> t12,
          DbType<T13> t13) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
  }

  /** Create a multi-column row codec returning a Tuple15. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
      RowCodec<Tuple.Tuple15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> of(
          DbType<T0> t0,
          DbType<T1> t1,
          DbType<T2> t2,
          DbType<T3> t3,
          DbType<T4> t4,
          DbType<T5> t5,
          DbType<T6> t6,
          DbType<T7> t7,
          DbType<T8> t8,
          DbType<T9> t9,
          DbType<T10> t10,
          DbType<T11> t11,
          DbType<T12> t12,
          DbType<T13> t13,
          DbType<T14> t14) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
  }

  /** Create a multi-column row codec returning a Tuple16. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
      RowCodec<Tuple.Tuple16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
  }

  /** Create a multi-column row codec returning a Tuple17. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
      RowCodec<
              Tuple.Tuple17<
                  T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16) {
    return RowCodecOf.of(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
  }

  /** Create a multi-column row codec returning a Tuple18. */
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
      RowCodec<
              Tuple.Tuple18<
                  T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16,
              DbType<T17> t17) {
    return RowCodecOf.of(
        t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
  }

  /** Create a multi-column row codec returning a Tuple19. */
  public static <
          T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
      RowCodec<
              Tuple.Tuple19<
                  T0,
                  T1,
                  T2,
                  T3,
                  T4,
                  T5,
                  T6,
                  T7,
                  T8,
                  T9,
                  T10,
                  T11,
                  T12,
                  T13,
                  T14,
                  T15,
                  T16,
                  T17,
                  T18>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16,
              DbType<T17> t17,
              DbType<T18> t18) {
    return RowCodecOf.of(
        t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
  }

  /** Create a multi-column row codec returning a Tuple20. */
  public static <
          T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
      RowCodec<
              Tuple.Tuple20<
                  T0,
                  T1,
                  T2,
                  T3,
                  T4,
                  T5,
                  T6,
                  T7,
                  T8,
                  T9,
                  T10,
                  T11,
                  T12,
                  T13,
                  T14,
                  T15,
                  T16,
                  T17,
                  T18,
                  T19>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16,
              DbType<T17> t17,
              DbType<T18> t18,
              DbType<T19> t19) {
    return RowCodecOf.of(
        t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
  }

  /** Create a multi-column row codec returning a Tuple21. */
  public static <
          T0,
          T1,
          T2,
          T3,
          T4,
          T5,
          T6,
          T7,
          T8,
          T9,
          T10,
          T11,
          T12,
          T13,
          T14,
          T15,
          T16,
          T17,
          T18,
          T19,
          T20>
      RowCodec<
              Tuple.Tuple21<
                  T0,
                  T1,
                  T2,
                  T3,
                  T4,
                  T5,
                  T6,
                  T7,
                  T8,
                  T9,
                  T10,
                  T11,
                  T12,
                  T13,
                  T14,
                  T15,
                  T16,
                  T17,
                  T18,
                  T19,
                  T20>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16,
              DbType<T17> t17,
              DbType<T18> t18,
              DbType<T19> t19,
              DbType<T20> t20) {
    return RowCodecOf.of(
        t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19,
        t20);
  }

  /** Create a multi-column row codec returning a Tuple22. */
  public static <
          T0,
          T1,
          T2,
          T3,
          T4,
          T5,
          T6,
          T7,
          T8,
          T9,
          T10,
          T11,
          T12,
          T13,
          T14,
          T15,
          T16,
          T17,
          T18,
          T19,
          T20,
          T21>
      RowCodec<
              Tuple.Tuple22<
                  T0,
                  T1,
                  T2,
                  T3,
                  T4,
                  T5,
                  T6,
                  T7,
                  T8,
                  T9,
                  T10,
                  T11,
                  T12,
                  T13,
                  T14,
                  T15,
                  T16,
                  T17,
                  T18,
                  T19,
                  T20,
                  T21>>
          of(
              DbType<T0> t0,
              DbType<T1> t1,
              DbType<T2> t2,
              DbType<T3> t3,
              DbType<T4> t4,
              DbType<T5> t5,
              DbType<T6> t6,
              DbType<T7> t7,
              DbType<T8> t8,
              DbType<T9> t9,
              DbType<T10> t10,
              DbType<T11> t11,
              DbType<T12> t12,
              DbType<T13> t13,
              DbType<T14> t14,
              DbType<T15> t15,
              DbType<T16> t16,
              DbType<T17> t17,
              DbType<T18> t18,
              DbType<T19> t19,
              DbType<T20> t20,
              DbType<T21> t21) {
    return RowCodecOf.of(
        t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19,
        t20, t21);
  }

  /**
   * Create a single-column named row codec.
   *
   * @param name the column name
   * @param type the column type
   * @return a named row codec that returns the column value directly
   */
  @SuppressWarnings("unchecked")
  public static <T> RowCodecNamed<T> ofNamed(String name, DbType<T> type) {
    return new RowCodecNamed<>(
        List.of(name), List.of(type), arr -> (T) arr[0], t -> new Object[] {t});
  }

  public Row readRow(ResultSet rs, int rowNum) throws SqlResultParseException {
    Object[] currentRow = new Object[columns.size()];
    for (int colNum = 0; colNum < columns.size(); colNum++) {
      DbType<?> dbType = columns.get(colNum);
      try {
        currentRow[colNum] = dbType.read().read(rs, colNum + 1);
      } catch (Exception e) {
        throw new SqlResultParseException(rs, rowNum, colNum + 1, dbType, e);
      }
    }
    return this.decode.apply(currentRow);
  }

  // Convenience method for compatibility with SelectBuilderSql
  public Row parse(ResultSet rs) throws SqlResultParseException {
    try {
      // Try to get row number for error reporting, but fall back to -1 if not supported (e.g.,
      // DuckDB)
      int rowNum = -1;
      try {
        rowNum = rs.getRow();
      } catch (SQLFeatureNotSupportedException ignored) {
        // Some databases (like DuckDB) don't support getRow()
      }
      return readRow(rs, rowNum);
    } catch (SQLException e) {
      throw new SqlResultParseException(rs, 0, 0, null, e);
    }
  }

  @SuppressWarnings("unchecked")
  public void writeRow(PreparedStatement stmt, Row row) throws SQLException {
    Object[] values = this.encode.apply(row);
    for (int colNum = 0; colNum < columns.size(); colNum++) {
      DbType<Object> dbType = (DbType<Object>) columns.get(colNum);
      dbType.write().set(stmt, colNum + 1, values[colNum]);
    }
  }

  /** Returns first row (if any), ignores the rest */
  public ResultSetParser<Optional<Row>> first() {
    return new ResultSetParser.First<>(this);
  }

  /** Returns at most one row, fails if there are more */
  public ResultSetParser<Optional<Row>> maxOne() {
    return new ResultSetParser.MaxOne<>(this);
  }

  /** Returns exactly one row, fails if there are more or less */
  public ResultSetParser<Row> exactlyOne() {
    return new ResultSetParser.ExactlyOne<>(this);
  }

  public ResultSetParser<List<Row>> all() {
    return new ResultSetParser.All<>(this);
  }

  public ResultSetParser<Void> foreach(Consumer<Row> consumer) {
    return new ResultSetParser.Foreach<>(this, consumer);
  }

  /**
   * if all values are `null` / `Optional.empty()` then return empty row. This is used for left
   * joins where all columns from the joined table can be null.
   */
  public RowCodec<Optional<Row>> opt() {
    List<DbType<?>> optColumns = new ArrayList<>(columns.size());
    for (int i = 0; i < columns.size(); i++) {
      optColumns.add(columns.get(i).opt());
    }

    Function<Object[], Optional<Row>> optDecode =
        values -> {
          var allNull = true;
          for (int i = 0; i < values.length && allNull; i++) {
            switch (values[i]) {
              case null -> {}
              case Optional<?> optional -> allNull = optional.isEmpty();
              default -> allNull = false;
            }
          }
          if (allNull) {
            return Optional.empty();
          }
          // Unwrap the Optional wrapper we added
          Object[] unwrapped = new Object[values.length];
          for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Optional<?> opt) {
              unwrapped[i] = opt.orElse(null);
            } else {
              unwrapped[i] = values[i];
            }
          }
          var row = this.decode.apply(unwrapped);
          return Optional.of(row);
        };
    Function<Optional<Row>, Object[]> optEncode =
        row -> {
          if (row.isEmpty()) {
            var none = Optional.empty();
            Object[] ret = new Object[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
              ret[i] = none;
            }
            return ret;
          }
          return this.encode.apply(row.get());
        };

    return new RowCodecUnnamed<>(optColumns, optDecode, optEncode);
  }

  public <Row2> RowCodec<Tuple.Tuple2<Row, Row2>> join(RowCodec<Row2> right) {
    var allColumns = new ArrayList<>(columns);
    allColumns.addAll(right.columns);
    var left = this;
    Function<Object[], Tuple.Tuple2<Row, Row2>> joinDecode =
        allValues -> {
          Object[] leftValues = new Object[left.columns.size()];
          System.arraycopy(allValues, 0, leftValues, 0, leftValues.length);
          Object[] rightValues = new Object[right.columns.size()];
          System.arraycopy(allValues, leftValues.length, rightValues, 0, right.columns.size());
          return Tuple.of(left.decode.apply(leftValues), right.decode.apply(rightValues));
        };
    Function<Tuple.Tuple2<Row, Row2>, Object[]> joinEncode =
        t -> {
          Object[] leftValues = left.encode.apply(t._1());
          Object[] rightValues = right.encode.apply(t._2());
          Object[] allValues = new Object[leftValues.length + rightValues.length];
          System.arraycopy(leftValues, 0, allValues, 0, leftValues.length);
          System.arraycopy(rightValues, 0, allValues, leftValues.length, rightValues.length);
          return allValues;
        };
    return new RowCodecUnnamed<>(allColumns, joinDecode, joinEncode);
  }

  public <Row2> RowCodec<Tuple.Tuple2<Row, Optional<Row2>>> leftJoin(RowCodec<Row2> other) {
    return join(other.opt());
  }

  public <Row2> RowCodec<Tuple.Tuple2<Optional<Row>, Row2>> rightJoin(RowCodec<Row2> other) {
    return opt().join(other);
  }

  public <Row2> RowCodec<Tuple.Tuple2<Optional<Row>, Optional<Row2>>> fullJoin(
      RowCodec<Row2> other) {
    return opt().join(other.opt());
  }

  /**
   * Transform the row type using a bijection. This is useful for language wrappers that need to
   * convert between Java and language-native types.
   */
  public <Row2> RowCodec<Row2> to(Bijection<Row, Row2> bijection) {
    Function<Object[], Row2> newDecode = values -> bijection.underlying(this.decode.apply(values));
    Function<Row2, Object[]> newEncode = row2 -> this.encode.apply(bijection.from(row2));
    return new RowCodecUnnamed<>(this.columns, newDecode, newEncode);
  }

  /**
   * Parse a list of rows from a JSON array. This is used for typed MULTISET support where the
   * database returns JSON.
   *
   * <p>The JSON array format can be:
   *
   * <ul>
   *   <li>Array of objects: [{"col1": val1, "col2": val2}, ...]
   *   <li>Compact array of arrays: [[val1, val2], [val3, val4], ...]
   * </ul>
   *
   * @param jsonStr JSON string from database
   * @param columnNames names of columns in order (for object format lookup)
   * @return list of parsed rows
   */
  @SuppressWarnings("unchecked")
  public List<Row> parseJsonArray(String jsonStr, List<String> columnNames) {
    if (jsonStr == null || jsonStr.isEmpty()) {
      return List.of();
    }

    JsonValue json = JsonValue.parse(jsonStr);
    if (!(json instanceof JsonValue.JArray(List<JsonValue> values))) {
      throw new IllegalArgumentException(
          "Expected JSON array, got: " + json.getClass().getSimpleName());
    }

    List<Row> result = new ArrayList<>(values.size());
    for (JsonValue elem : values) {
      Row row = parseJsonRow(elem, columnNames);
      result.add(row);
    }
    return result;
  }

  /**
   * Parse a single row from a JSON value. Supports both object format {"col": val} and array format
   * [val1, val2].
   */
  @SuppressWarnings("unchecked")
  private Row parseJsonRow(JsonValue json, List<String> columnNames) {
    Object[] values = new Object[columns.size()];

    if (json instanceof JsonValue.JArray(List<JsonValue> values1)) {
      // Compact array format: values in column order
      if (values1.size() != columns.size()) {
        throw new IllegalArgumentException(
            "JSON array size " + values1.size() + " doesn't match column count " + columns.size());
      }
      for (int i = 0; i < columns.size(); i++) {
        DbJson<Object> jsonCodec = (DbJson<Object>) columns.get(i).json();
        values[i] = jsonCodec.fromJson(values1.get(i));
      }
    } else if (json instanceof JsonValue.JObject(java.util.Map<String, JsonValue> fields)) {
      // Object format: lookup by column name
      for (int i = 0; i < columns.size(); i++) {
        String colName = columnNames.get(i);
        JsonValue colValue = fields.get(colName);
        if (colValue == null) {
          // Column not present in JSON - use null
          values[i] = null;
        } else {
          DbJson<Object> jsonCodec = (DbJson<Object>) columns.get(i).json();
          values[i] = jsonCodec.fromJson(colValue);
        }
      }
    } else {
      throw new IllegalArgumentException(
          "Expected JSON object or array for row, got: " + json.getClass().getSimpleName());
    }

    return decode.apply(values);
  }
}
