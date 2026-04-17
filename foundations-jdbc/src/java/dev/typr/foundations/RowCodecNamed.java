package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link RowCodec} that carries column names alongside the types.
 *
 * <p>This enables convenience methods that require column names, such as {@link
 * Fragment#row(RowCodecNamed, Object, String...)} and {@link DbJsonRow#jsonObject(RowCodecNamed)}.
 *
 * <p>Create via {@link RowCodec#namedBuilder()} or {@link RowCodec#createNamed}.
 */
public final class RowCodecNamed<Row> extends RowCodec<Row> {
  private final List<String> columnNames;

  RowCodecNamed(
      List<String> columnNames,
      List<DbType<?>> columns,
      Function<Object[], Row> decode,
      Function<Row, Object[]> encode) {
    super(columns, decode, encode);
    if (columnNames.size() != columns.size()) {
      throw new IllegalArgumentException(
          "Column name count ("
              + columnNames.size()
              + ") doesn't match column type count ("
              + columns.size()
              + ")");
    }
    this.columnNames = List.copyOf(columnNames);
  }

  /** Column names in order, matching {@link #columns()}. */
  public List<String> columnNames() {
    return columnNames;
  }

  /**
   * Comma-separated column names as a Fragment, useful for SQL SELECT lists.
   *
   * <p>Throws when the codec carries duplicate column names — which happens after
   * {@link #join} / {@link #leftJoin} if the left and right sides share any
   * column name. Call {@link #aliased(String)} on each side <em>before</em> the join
   * to qualify columns with a table prefix:
   *
   * <pre>{@code
   * var joined = empCodec.aliased("e").leftJoin(deptCodec.aliased("d"));
   * // joined.columnList() -> "e.id, e.name, ..., d.id, d.name"
   * var q = Fragment.of("SELECT ").append(joined.columnList())
   *     .append(" FROM emp e LEFT JOIN dept d ON e.department = d.name")
   *     .query(joined.all());
   * }</pre>
   *
   * If you don't need named decoding, fall back to {@link RowCodec#leftJoin} which is
   * positional and doesn't carry names.
   */
  public Fragment columnList() {
    var duplicates = findDuplicateNames();
    if (!duplicates.isEmpty()) {
      throw new IllegalStateException(
          "RowCodecNamed.columnList() has duplicate column names "
              + duplicates
              + " (typically from a join of codecs sharing a column name). Call .aliased(\"x\") "
              + "on each side before composing, or fall back to RowCodec.leftJoin for a "
              + "positional result.");
    }
    return Fragment.comma(columnNames.stream().map(Fragment::of).toList());
  }

  /**
   * Return a copy of this codec where every column name is prefixed with {@code alias + "."}.
   * Use before a join to keep {@link #columnList()} unambiguous:
   *
   * <pre>{@code
   * empCodec.aliased("e").leftJoin(deptCodec.aliased("d"))
   * }</pre>
   *
   * <p>The aliased codec's {@code columnList} is SELECT-friendly but not INSERT-friendly
   * (an {@code INSERT INTO tbl (e.id, ...)} is almost never what you want). For INSERTs
   * use the unaliased codec.
   */
  public RowCodecNamed<Row> aliased(String alias) {
    var prefixed = new ArrayList<String>(columnNames.size());
    for (var name : columnNames) prefixed.add(alias + "." + name);
    return new RowCodecNamed<>(prefixed, columns(), decode(), encode());
  }

  private List<String> findDuplicateNames() {
    var seen = new java.util.HashSet<String>();
    var dupes = new java.util.LinkedHashSet<String>();
    for (var name : columnNames) {
      if (!seen.add(name)) dupes.add(name);
    }
    return List.copyOf(dupes);
  }

  @Override
  public RowCodecNamed<Optional<Row>> opt() {
    List<DbType<?>> optColumns = new ArrayList<>(columns().size());
    for (int i = 0; i < columns().size(); i++) {
      optColumns.add(columns().get(i).opt());
    }

    Function<Object[], Row> innerDecode = decode();
    Function<Row, Object[]> innerEncode = encode();

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
          Object[] unwrapped = new Object[values.length];
          for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Optional<?> opt) {
              unwrapped[i] = opt.orElse(null);
            } else {
              unwrapped[i] = values[i];
            }
          }
          var row = innerDecode.apply(unwrapped);
          return Optional.of(row);
        };
    Function<Optional<Row>, Object[]> optEncode =
        row -> {
          if (row.isEmpty()) {
            var none = Optional.empty();
            Object[] ret = new Object[columns().size()];
            for (int i = 0; i < columns().size(); i++) {
              ret[i] = none;
            }
            return ret;
          }
          return innerEncode.apply(row.get());
        };

    return new RowCodecNamed<>(columnNames, optColumns, optDecode, optEncode);
  }

  @Override
  public <Row2> RowCodecNamed<Row2> to(Bijection<Row, Row2> bijection) {
    Function<Object[], Row2> newDecode =
        values -> bijection.underlying(this.decode().apply(values));
    Function<Row2, Object[]> newEncode = row2 -> this.encode().apply(bijection.from(row2));
    return new RowCodecNamed<>(this.columnNames, this.columns(), newDecode, newEncode);
  }

  public <Row2> RowCodecNamed<Tuple.Tuple2<Row, Row2>> join(RowCodecNamed<Row2> right) {
    var allNames = new ArrayList<>(this.columnNames());
    allNames.addAll(right.columnNames());
    var allColumns = new ArrayList<>(this.columns());
    allColumns.addAll(right.columns());
    var left = this;
    Function<Object[], Tuple.Tuple2<Row, Row2>> joinDecode =
        allValues -> {
          Object[] leftValues = new Object[left.columns().size()];
          System.arraycopy(allValues, 0, leftValues, 0, leftValues.length);
          Object[] rightValues = new Object[right.columns().size()];
          System.arraycopy(allValues, leftValues.length, rightValues, 0, right.columns().size());
          return Tuple.of(left.decode().apply(leftValues), right.decode().apply(rightValues));
        };
    Function<Tuple.Tuple2<Row, Row2>, Object[]> joinEncode =
        t -> {
          Object[] leftValues = left.encode().apply(t._1());
          Object[] rightValues = right.encode().apply(t._2());
          Object[] allValues = new Object[leftValues.length + rightValues.length];
          System.arraycopy(leftValues, 0, allValues, 0, leftValues.length);
          System.arraycopy(rightValues, 0, allValues, leftValues.length, rightValues.length);
          return allValues;
        };
    return new RowCodecNamed<>(allNames, allColumns, joinDecode, joinEncode);
  }

  /**
   * Left join that preserves column names. Wraps every column on the right side in {@code .opt()}
   * so a row with all-null right-side columns decodes as {@code Optional.empty()}.
   *
   * <p>Unlike the positional {@link RowCodec#leftJoin}, this returns a {@link RowCodecNamed}
   * so {@code columnList()}, {@code Fragment.insertInto}, and JSON encoding continue to work on
   * the combined codec.
   */
  public <Row2> RowCodecNamed<Tuple.Tuple2<Row, Optional<Row2>>> leftJoin(
      RowCodecNamed<Row2> right) {
    return this.join(right.opt());
  }

  /**
   * Right join that preserves column names. Wraps every column on the left side in {@code .opt()}
   * so a row with all-null left-side columns decodes as {@code Optional.empty()}.
   */
  public <Row2> RowCodecNamed<Tuple.Tuple2<Optional<Row>, Row2>> rightJoin(
      RowCodecNamed<Row2> right) {
    return this.opt().join(right);
  }

  /**
   * Full outer join that preserves column names. Both sides are wrapped in {@code .opt()}.
   */
  public <Row2> RowCodecNamed<Tuple.Tuple2<Optional<Row>, Optional<Row2>>> fullJoin(
      RowCodecNamed<Row2> right) {
    return this.opt().join(right.opt());
  }
}
