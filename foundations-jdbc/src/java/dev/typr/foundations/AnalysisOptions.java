package dev.typr.foundations;

import java.util.Set;

public record AnalysisOptions(Set<DbTypename<?>> vendorTypeNames, boolean nullableOk, boolean unchecked) {
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

  /** Apply .array() to each vendor typename alias, producing the array form. Only PG and DuckDB typenames support arrays. */
  public AnalysisOptions arrayForms() {
    if (vendorTypeNames.isEmpty()) return this;
    var arrayNames = new java.util.HashSet<DbTypename<?>>();
    for (var tn : vendorTypeNames) {
      switch (tn) {
        case PgTypename<?> pg -> arrayNames.add(pg.array());
        case DuckDbTypename<?> duck -> arrayNames.add(duck.array());
        default -> throw new UnsupportedOperationException(
            "Array vendor type names not supported for " + tn.getClass().getSimpleName());
      }
    }
    return new AnalysisOptions(Set.copyOf(arrayNames), nullableOk, unchecked);
  }
}
