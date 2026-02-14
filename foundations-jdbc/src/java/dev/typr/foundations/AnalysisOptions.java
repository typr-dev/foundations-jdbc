package dev.typr.foundations;

import java.util.Set;

public record AnalysisOptions(
    Set<String> vendorTypeNames,
    boolean nullableOk,
    boolean unchecked
) {
  public static final AnalysisOptions EMPTY = new AnalysisOptions(Set.of(), false, false);

  public AnalysisOptions withNullableOk() {
    return new AnalysisOptions(vendorTypeNames, true, unchecked);
  }

  public AnalysisOptions withUnchecked() {
    return new AnalysisOptions(vendorTypeNames, nullableOk, true);
  }

  public AnalysisOptions withVendorTypeNames(String... names) {
    return new AnalysisOptions(Set.of(names), nullableOk, unchecked);
  }
}
