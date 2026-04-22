package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Shared analysis tree-walker. Walks an {@link Analyzable} (operation or template), extracting SQL
 * + type information at each leaf, and delegates actual statement analysis to a {@link
 * StatementAnalyzer}.
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
      case Template<?, ?> t -> analyzeTemplate(t, analyzer);
    };
  }

  // ========== Template analysis ==========

  private static List<QueryAnalysis> analyzeTemplate(
      Template<?, ?> template, StatementAnalyzer analyzer) {
    Fragment fragment = template.fragment();
    List<Fragment> variants = OptionallyResolver.analysisVariants(fragment);
    ResultSetParser<?> parser = QueryAnalyzer.extractResultSetParser(template);
    List<QueryAnalysis> results = new ArrayList<>();
    for (Fragment variant : variants) {
      if (parser != null) {
        results.add(analyzeFragmentAndParser(Optional.empty(), variant, parser, analyzer));
      } else {
        results.add(analyzeUpdate(Optional.empty(), variant, analyzer));
      }
    }
    return results;
  }

  // ========== Operation analysis ==========

  private static List<QueryAnalysis> analyzeOperation(
      Optional<String> name, Operation<?> op, StatementAnalyzer analyzer) {
    return switch (op) {
      // Read leaves
      case OperationRead.Query<?> q ->
          List.of(analyzeFragmentAndParser(name, q.query(), q.parser(), analyzer));
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
      case Operation.UpdateReturning<?> ur ->
          List.of(analyzeFragmentAndParser(name, ur.query(), ur.parser(), analyzer));
      case Operation.Update u -> List.of(analyzeUpdate(name, u.query(), analyzer));
      case Operation.Execute e -> List.of(analyzeUpdate(name, e.query(), analyzer));

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
    return doAnalyzeThen(then.source(), then.continuation(), analyzer);
  }

  private static <A, B> List<QueryAnalysis> analyzeThen(
      Operation.Then<A, B> then, StatementAnalyzer analyzer) {
    return doAnalyzeThen(then.source(), then.continuation(), analyzer);
  }

  private static <A> List<QueryAnalysis> doAnalyzeThen(
      Operation<A> source, Template<A, ?> continuation, StatementAnalyzer analyzer) {
    var r = new ArrayList<>(analyzeOperation(Optional.empty(), source, analyzer));
    Fragment templateFragment = continuation.fragment();
    ResultSetParser<?> templateParser = QueryAnalyzer.extractResultSetParser(continuation);
    if (templateParser != null) {
      r.add(analyzeFragmentAndParser(Optional.empty(), templateFragment, templateParser, analyzer));
    } else {
      r.add(analyzeUpdate(Optional.empty(), templateFragment, analyzer));
    }
    return r;
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
                        r.parameterMetadataAvailable())
                    : r)
        .toList();
  }
}
