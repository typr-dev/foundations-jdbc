package dev.typr.foundations;

import java.util.Arrays;
import java.util.Iterator;

public sealed interface RowSqlTemplate<Row, Out> {
  Fragment fragment();

  Operation<Out> on(Row row);

  record Query<Row, Out>(
      Fragment fragment,
      RowCodecNamed<Row> parser,
      int[] includedIndices,
      ResultSetParser<Out> resultParser)
      implements RowSqlTemplate<Row, Out> {
    @Override
    public Operation.Query<Out> on(Row row) {
      Object[] encoded = parser.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++)
        params[i] = encoded[includedIndices[i]];
      return new Operation.Query<>(fragment.fill(Arrays.asList(params).iterator()), resultParser);
    }
  }

  record Update<Row>(
      Fragment fragment, RowCodecNamed<Row> parser, int[] includedIndices)
      implements RowSqlTemplate<Row, Integer> {
    @Override
    public Operation.Update on(Row row) {
      Object[] encoded = parser.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++)
        params[i] = encoded[includedIndices[i]];
      return new Operation.Update(fragment.fill(Arrays.asList(params).iterator()));
    }

    public Operation.UpdateManyTemplate<Row> onMany(Iterator<Row> rows) {
      return new Operation.UpdateManyTemplate<>(fragment, parser, includedIndices, rows);
    }
  }
}
