package dev.typr.foundations;

import java.util.Arrays;
import java.util.Iterator;

public sealed interface RowTemplate<Row, Out> extends Template<Row, Out> {

  record Query<Row, Out>(
      Fragment fragment,
      RowCodecNamed<Row> codec,
      int[] includedIndices,
      ResultSetParser<Out> resultParser)
      implements RowTemplate<Row, Out> {
    @Override
    public Operation.Query<Out> on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++)
        params[i] = encoded[includedIndices[i]];
      return new Operation.Query<>(fragment.fill(Arrays.asList(params).iterator()), resultParser);
    }
  }

  record Update<Row>(
      Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices)
      implements RowTemplate<Row, Integer> {
    @Override
    public Operation.Update on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++)
        params[i] = encoded[includedIndices[i]];
      return new Operation.Update(fragment.fill(Arrays.asList(params).iterator()));
    }

    public Operation.UpdateManyTemplate<Row> onMany(Iterator<Row> rows) {
      return new Operation.UpdateManyTemplate<>(fragment, codec, includedIndices, rows);
    }
  }

  record GeneratedKeys<Row, Out>(
      Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices,
      String[] generatedColumns, ResultSetParser<Out> resultParser)
      implements RowTemplate<Row, Out> {
    @Override
    public Operation.UpdateReturningGeneratedKeys<Out> on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++)
        params[i] = encoded[includedIndices[i]];
      return new Operation.UpdateReturningGeneratedKeys<>(
          fragment.fill(Arrays.asList(params).iterator()),
          generatedColumns,
          resultParser);
    }
  }
}
