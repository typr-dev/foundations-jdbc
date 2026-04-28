package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Shared analysis tree-walker. Walks an {@link Analyzable}, extracting SQL + type information at
 * each leaf, and delegates actual statement analysis to a {@link StatementAnalyzer}.
 *
 * <p>This is the analysis counterpart of {@link OperationRunner}: it eliminates duplicate
 * tree-walking between JDBC-based and PgPipe-based query checkers.
 */
public final class AnalysisRunner {

  private AnalysisRunner() {}

  public static List<QueryAnalysis> analyze(Analyzable analyzable, StatementAnalyzer analyzer) {
    return switch (analyzable) {
      case Analyzable.Named(var defaultName, var inner) ->
          applyDefaultName(defaultName, analyze(inner, analyzer));
      case Operation<?> op -> analyzeOperation(Optional.empty(), op, analyzer);
    };
  }

  // ========== Operation analysis ==========

  private static List<QueryAnalysis> analyzeOperation(
      Optional<String> name, Operation<?> op, StatementAnalyzer analyzer) {
    return switch (op) {
      // Read leaves
      case OperationRead.Query<?> q -> {
        List<Fragment> variants = OptionallyResolver.analysisVariants(q.query());
        List<QueryAnalysis> results = new ArrayList<>();
        for (Fragment variant : variants) {
          results.add(analyzeFragmentAndParser(name, variant, q.parser(), analyzer));
        }
        yield withVariantCount(results);
      }
      case OperationRead.Streaming<?> s ->
          List.of(analyzeFragmentAndParser(name, s.query(), s.codec().all(), analyzer));
      case OperationRead.Pure<?> ignored -> List.of();

      // Read structural
      case OperationRead.Configured<?> c ->
          analyzeOperation(name.or(() -> c.name()), c.inner(), analyzer);
      case OperationRead.Mapped<?, ?> m -> analyzeOperation(name, m.source(), analyzer);
      case OperationRead.Combine<?, ?> w -> {
        var r = new ArrayList<>(analyzeOperation(Optional.empty(), w.first(), analyzer));
        r.addAll(analyzeOperation(Optional.empty(), w.second(), analyzer));
        yield r;
      }
      case OperationRead.IfEmpty<?> ie -> {
        var r = new ArrayList<>(analyzeOperation(Optional.empty(), ie.check(), analyzer));
        r.addAll(analyzeOperation(Optional.empty(), ie.fallback(), analyzer));
        yield r;
      }
      case OperationRead.Then<?, ?> t -> analyzeThen(t, analyzer);

      // Write leaves
      case Operation.UpdateReturning<?> ur -> {
        List<Fragment> variants = OptionallyResolver.analysisVariants(ur.query());
        List<QueryAnalysis> results = new ArrayList<>();
        for (Fragment variant : variants) {
          results.add(analyzeFragmentAndParser(name, variant, ur.parser(), analyzer));
        }
        yield withVariantCount(results);
      }
      case Operation.Update u -> analyzeUpdateVariants(name, u.query(), analyzer);
      case Operation.Execute e -> analyzeUpdateVariants(name, e.query(), analyzer);

      // Write structural
      case Operation.Configured<?> c ->
          analyzeOperation(name.or(() -> c.name()), c.inner(), analyzer);
      case Operation.Mapped<?, ?> m -> analyzeOperation(name, m.source(), analyzer);
      case Operation.Combine<?, ?> w -> {
        var r = new ArrayList<>(analyzeOperation(Optional.empty(), w.first(), analyzer));
        r.addAll(analyzeOperation(Optional.empty(), w.second(), analyzer));
        yield r;
      }
      case Operation.IfEmpty<?> ie -> {
        var r = new ArrayList<>(analyzeOperation(Optional.empty(), ie.check(), analyzer));
        r.addAll(analyzeOperation(Optional.empty(), ie.fallback(), analyzer));
        yield r;
      }
      case Operation.Then<?, ?> t -> analyzeThen(t, analyzer);

      // Leaves we don't analyze
      default -> List.of();
    };
  }

  // ========== Then analysis ==========

  private static <A, B> List<QueryAnalysis> analyzeThen(
      OperationRead.Then<A, B> then, StatementAnalyzer analyzer) {
    // The continuation is a Function<A, OperationRead<B>> — opaque without a value
    // of A to invoke it with. Analyze the source; users should verify the
    // continuation by exposing it as its own method that the scanner discovers.
    return analyzeOperation(Optional.empty(), then.source(), analyzer);
  }

  private static <A, B> List<QueryAnalysis> analyzeThen(
      Operation.Then<A, B> then, StatementAnalyzer analyzer) {
    return analyzeOperation(Optional.empty(), then.source(), analyzer);
  }

  // ========== Leaf analysis ==========

  public static QueryAnalysis analyzeFragmentAndParser(
      Optional<String> name,
      Fragment fragment,
      ResultSetParser<?> parser,
      StatementAnalyzer analyzer) {
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();
    List<DbType<?>> columnTypes = QueryAnalyzer.extractColumnTypes(parser);

    StatementMeta meta;
    try {
      meta = analyzer.analyzeStatement(sql);
    } catch (RuntimeException ex) {
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(ex));
    }

    boolean paramMetaAvailable = !meta.parameters().isEmpty() || paramTypes.isEmpty();
    var paramAlignment = Alignment.align(paramTypes, meta.parameters());
    var colAlignment = Alignment.align(columnTypes, meta.columns());

    return new QueryAnalysis(sql, name, paramAlignment, colAlignment, paramMetaAvailable);
  }

  private static QueryAnalysis analyzeUpdate(
      Optional<String> name, Fragment fragment, StatementAnalyzer analyzer) {
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();

    StatementMeta meta;
    try {
      meta = analyzer.analyzeStatement(sql);
    } catch (RuntimeException ex) {
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(ex));
    }

    boolean paramMetaAvailable = !meta.parameters().isEmpty() || paramTypes.isEmpty();
    var paramAlignment = Alignment.align(paramTypes, meta.parameters());

    return new QueryAnalysis(sql, name, paramAlignment, List.of(), paramMetaAvailable);
  }

  private static List<QueryAnalysis> analyzeUpdateVariants(
      Optional<String> name, Fragment fragment, StatementAnalyzer analyzer) {
    List<Fragment> variants = OptionallyResolver.analysisVariants(fragment);
    List<QueryAnalysis> results = new ArrayList<>();
    for (Fragment variant : variants) {
      results.add(analyzeUpdate(name, variant, analyzer));
    }
    return withVariantCount(results);
  }

  private static List<QueryAnalysis> withVariantCount(List<QueryAnalysis> results) {
    if (results.size() <= 1) return results;
    int n = results.size();
    List<QueryAnalysis> tagged = new ArrayList<>(n);
    for (QueryAnalysis r : results) tagged.add(r.withVariantCount(n));
    return tagged;
  }

  // ========== Helpers ==========

  private static AlignmentError.PrepareFailure toPrepareFailure(RuntimeException ex) {
    String msg = ex.getMessage();
    String sqlState = null;
    if (ex instanceof DatabaseException de) {
      sqlState = de.sqlState();
    }
    return new AlignmentError.PrepareFailure(sqlState, msg, QueryAnalyzer.parsePgPrepareHint(msg));
  }

  private static List<QueryAnalysis> applyDefaultName(
      String defaultName, List<QueryAnalysis> results) {
    return results.stream()
        .map(
            r ->
                r.queryName().isEmpty()
                    ? new QueryAnalysis(
                        r.sql(),
                        Optional.of(defaultName),
                        r.parameterAlignment(),
                        r.columnAlignment(),
                        r.parameterMetadataAvailable(),
                        r.prepareFailure(),
                        r.variantCount())
                    : r)
        .toList();
  }
}
