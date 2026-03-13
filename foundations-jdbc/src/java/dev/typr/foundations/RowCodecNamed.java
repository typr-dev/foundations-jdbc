package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link RowCodec} that carries column names alongside the types.
 *
 * <p>This enables convenience methods that require column names, such as
 * {@link Fragment#row(RowCodecNamed, Object, String...)} and
 * {@link DbJsonRow#jsonObject(RowCodecNamed)}.
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
          "Column name count (" + columnNames.size()
              + ") doesn't match column type count (" + columns.size() + ")");
    }
    this.columnNames = List.copyOf(columnNames);
  }

  /** Column names in order, matching {@link #columns()}. */
  public List<String> columnNames() {
    return columnNames;
  }

  /** Comma-separated column names as a Fragment, useful for SQL SELECT lists. */
  public Fragment columnList() {
    return Fragment.comma(columnNames.stream().map(Fragment::of).toList());
  }

  /** Comma-separated column names prefixed with a table alias, e.g. {@code v.id, v.name, v.capacity}. */
  public Fragment columnList(String alias) {
    return Fragment.comma(columnNames.stream().map(name -> Fragment.of(alias + "." + name)).toList());
  }

  @Override
  public RowCodec<Optional<Row>> opt() {
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
    Function<Object[], Row2> newDecode = values -> bijection.underlying(this.decode().apply(values));
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
}
