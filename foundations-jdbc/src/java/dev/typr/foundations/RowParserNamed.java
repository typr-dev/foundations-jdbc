package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link RowParser} that carries column names alongside the types.
 *
 * <p>This enables convenience methods that require column names, such as
 * {@link Fragment#row(RowParserNamed, Object, String...)} and
 * {@link DbJsonRow#jsonObject(RowParserNamed)}.
 *
 * <p>Create via {@link RowParser#namedBuilder()} or {@link RowParser#createNamed}.
 */
public final class RowParserNamed<Row> extends RowParser<Row> {
  private final List<String> columnNames;

  RowParserNamed(
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

  @Override
  public RowParser<Optional<Row>> opt() {
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

    return new RowParserNamed<>(columnNames, optColumns, optDecode, optEncode);
  }

  @Override
  public <Row2> RowParser<Row2> to(Bijection<Row, Row2> bijection) {
    Function<Object[], Row2> newDecode = values -> bijection.underlying(this.decode().apply(values));
    Function<Row2, Object[]> newEncode = row2 -> this.encode().apply(bijection.from(row2));
    return new RowParserNamed<>(this.columnNames, this.columns(), newDecode, newEncode);
  }
}
