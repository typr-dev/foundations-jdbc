package dev.typr.foundations;

import java.util.Arrays;
import java.util.Iterator;

public sealed interface RowTemplate<Row, Out> extends Template<Row, Out>
    permits RowTemplate.Query, RowTemplate.Update, RowTemplate.GeneratedKeys {

  record Query<Row, Out>(
      Fragment fragment,
      RowCodecNamed<Row> codec,
      int[] includedIndices,
      ResultSetParser<Out> resultParser)
      implements RowTemplate<Row, Out>, TemplateRead<Row, Out> {
    @Override
    public OperationRead.Query<Out> on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++) params[i] = encoded[includedIndices[i]];
      return new OperationRead.Query<>(
          fragment.fill(Arrays.asList(params).iterator()), resultParser);
    }

    @Override
    public String description(boolean verbose) {
      return "RowTemplate.Query: " + fragment.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record Update<Row>(Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices)
      implements RowTemplate<Row, Integer> {
    @Override
    public Operation<Integer> on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++) params[i] = encoded[includedIndices[i]];
      return new Operation.Update(fragment.fill(Arrays.asList(params).iterator()));
    }

    public Operation.UpdateManyTemplate<Row> onMany(Iterator<Row> rows) {
      return new Operation.UpdateManyTemplate<>(fragment, codec, includedIndices, rows);
    }

    @Override
    public String description(boolean verbose) {
      return "RowTemplate.Update: " + fragment.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }

  record GeneratedKeys<Row, Out>(
      Fragment fragment,
      RowCodecNamed<Row> codec,
      int[] includedIndices,
      String[] generatedColumns,
      ResultSetParser<Out> resultParser)
      implements RowTemplate<Row, Out> {
    @Override
    public Operation<Out> on(Row row) {
      Object[] encoded = codec.encode().apply(row);
      Object[] params = new Object[includedIndices.length];
      for (int i = 0; i < includedIndices.length; i++) params[i] = encoded[includedIndices[i]];
      return new Operation.UpdateReturningGeneratedKeys<>(
          fragment.fill(Arrays.asList(params).iterator()), generatedColumns, resultParser);
    }

    @Override
    public String description(boolean verbose) {
      return "RowTemplate.GeneratedKeys["
          + String.join(",", generatedColumns)
          + "]: "
          + fragment.renderInterpolated();
    }

    @Override
    public String toString() {
      return description(false);
    }
  }
}
