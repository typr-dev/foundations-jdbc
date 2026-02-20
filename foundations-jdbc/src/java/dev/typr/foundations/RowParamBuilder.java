package dev.typr.foundations;

public final class RowParamBuilder<Row> {
  private final Fragment fragment;
  private final RowCodecNamed<Row> parser;
  private final int[] includedIndices;

  RowParamBuilder(Fragment fragment, RowCodecNamed<Row> parser, int[] includedIndices) {
    this.fragment = fragment;
    this.parser = parser;
    this.includedIndices = includedIndices;
  }

  public RowParamBuilder<Row> append(String s) {
    return new RowParamBuilder<>(fragment.append(s), parser, includedIndices);
  }

  public RowParamBuilder<Row> append(Fragment other) {
    return new RowParamBuilder<>(fragment.append(other), parser, includedIndices);
  }

  public <Out> RowSqlTemplate.Query<Row, Out> query(ResultSetParser<Out> resultParser) {
    return new RowSqlTemplate.Query<>(fragment, parser, includedIndices, resultParser);
  }

  public RowSqlTemplate.Update<Row> update() {
    return new RowSqlTemplate.Update<>(fragment, parser, includedIndices);
  }

  public Fragment done() {
    return fragment;
  }
}
