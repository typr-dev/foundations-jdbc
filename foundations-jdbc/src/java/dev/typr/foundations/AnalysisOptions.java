package dev.typr.foundations;

import java.util.Set;

public record AnalysisOptions(
    Set<DbTypename<?>> vendorTypeNames, boolean nullableOk, boolean unchecked) {
  public static final AnalysisOptions EMPTY = new AnalysisOptions(Set.of(), false, false);

  public AnalysisOptions withNullableOk() {
    return new AnalysisOptions(vendorTypeNames, true, unchecked);
  }

  public AnalysisOptions withUnchecked() {
    return new AnalysisOptions(vendorTypeNames, nullableOk, true);
  }

  public AnalysisOptions withVendorTypeNames(DbTypename<?>... names) {
    return new AnalysisOptions(Set.of(names), nullableOk, unchecked);
  }

  /**
   * Apply the collection form (variable-length LIST) to each vendor typename alias. Only PG and
   * DuckDB typenames support collections. For DuckDB fixed-size {@code ARRAY(T, N)}, alias matching
   * via this mechanism isn't applicable — those types are fully qualified in the analyzer.
   */
  public AnalysisOptions listForms() {
    if (vendorTypeNames.isEmpty()) return this;
    var listNames = new java.util.HashSet<DbTypename<?>>();
    for (var tn : vendorTypeNames) {
      switch (tn) {
        case PgTypename<?> pg -> listNames.add(pg.array());
        case DuckDbTypename<?> duck -> listNames.add(duck.list());
        default ->
            throw new UnsupportedOperationException(
                "Collection vendor type names not supported for " + tn.getClass().getSimpleName());
      }
    }
    return new AnalysisOptions(Set.copyOf(listNames), nullableOk, unchecked);
  }
}
