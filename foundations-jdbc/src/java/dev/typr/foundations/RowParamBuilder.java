package dev.typr.foundations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public final class RowParamBuilder<Row> {
  private final Fragment fragment;
  private final RowCodecNamed<Row> codec;
  private final int[] includedIndices;

  RowParamBuilder(Fragment fragment, RowCodecNamed<Row> codec, int[] includedIndices) {
    this.fragment = fragment;
    this.codec = codec;
    this.includedIndices = includedIndices;
  }

  public RowParamBuilder<Row> append(String s) {
    return new RowParamBuilder<>(fragment.append(s), codec, includedIndices);
  }

  public RowParamBuilder<Row> append(Fragment other) {
    return new RowParamBuilder<>(fragment.append(other), codec, includedIndices);
  }

  // ─────────────────────────────────────────────────────────────────────
  // Row-driven execution. Each method takes either a single Row or an
  // Iterator<Row> and binds codec fields to the param holes added by
  // paramRow(...) (or the equivalent factories in Fragment).
  // ─────────────────────────────────────────────────────────────────────

  /** Execute the SQL once, binding values from the row. */
  public Operation.Update updateOne(Row row) {
    return new Operation.Update(applyRow(row));
  }

  /** Execute the SQL with RETURNING, binding values from the row. */
  public <Out> OperationRead.Query<Out> updateReturning(Row row, ResultSetParser<Out> parser) {
    return new OperationRead.Query<>(applyRow(row), parser);
  }

  /** Execute the SQL with RETURNING, parsing the returned row with this builder's codec. */
  public OperationRead.Query<Row> updateReturning(Row row) {
    return updateReturning(row, codec.exactlyOne());
  }

  /**
   * Batch-execute the SQL across all rows using JDBC batch mode. Returns per-row affected counts
   * when the driver reports them, or {@code Optional.empty()} when it reports {@code
   * SUCCESS_NO_INFO}.
   */
  public Operation.BatchUpdate<Row> updateMany(Iterator<Row> rows) {
    return new Operation.BatchUpdate<>(fragment, codec, includedIndices, rows);
  }

  /** INSERT with {@code getGeneratedKeys()}, binding values from the row. */
  public <Out> Operation.UpdateReturningGeneratedKeys<Out> updateOneGenerated(
      Row row, String[] generatedColumns, ResultSetParser<Out> parser) {
    return new Operation.UpdateReturningGeneratedKeys<>(applyRow(row), generatedColumns, parser);
  }

  /** Read-only query whose param holes are filled from the row. Niche but supported. */
  public <Out> OperationRead.Query<Out> queryOne(Row row, ResultSetParser<Out> parser) {
    return new OperationRead.Query<>(applyRow(row), parser);
  }

  public Fragment done() {
    return fragment;
  }

  // ─────────────────────────────────────────────────────────────────────
  // Internal helpers
  // ─────────────────────────────────────────────────────────────────────

  private Fragment applyRow(Row row) {
    Object[] encoded = codec.encode().apply(row);
    Object[] params = new Object[includedIndices.length];
    for (int i = 0; i < includedIndices.length; i++) params[i] = encoded[includedIndices[i]];
    return fragment.fill(Arrays.asList(params).iterator());
  }
}
