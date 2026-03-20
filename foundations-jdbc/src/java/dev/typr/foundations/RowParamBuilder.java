package dev.typr.foundations;

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

  public <Out> RowTemplate.Query<Row, Out> query(ResultSetParser<Out> resultParser) {
    return new RowTemplate.Query<>(fragment, codec, includedIndices, resultParser);
  }

  public RowTemplate.Update<Row> update() {
    return new RowTemplate.Update<>(fragment, codec, includedIndices);
  }

  public <Out> RowTemplate.GeneratedKeys<Row, Out> generatedKeys(
      String[] generatedColumns, ResultSetParser<Out> resultParser) {
    return new RowTemplate.GeneratedKeys<>(
        fragment.append(")"), codec, includedIndices, generatedColumns, resultParser);
  }

  public Fragment done() {
    return fragment;
  }
}
