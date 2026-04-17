package dev.typr.foundations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  public static List<QueryAnalysis> analyze(Analyzable analyzable, Connection conn) {
    try {
      return switch (analyzable) {
        case Analyzable.Named(var defaultName, var inner) ->
            applyDefaultName(defaultName, analyze(inner, conn));
        case Operation<?> op -> analyzeChecked(op, conn);
        case Template<?, ?> t -> analyzeChecked(t, conn);
      };
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> applyDefaultName(
      String defaultName, List<QueryAnalysis> results) {
    return results.stream()
        .map(
            r ->
                r.queryName() == null
                    ? new QueryAnalysis(
                        r.sql(),
                        defaultName,
                        r.parameterAlignment(),
                        r.columnAlignment(),
                        r.parameterMetadataAvailable())
                    : r)
        .toList();
  }

  public static List<QueryAnalysis> analyze(Template<?, ?> template, Connection conn) {
    try {
      return analyzeChecked(template, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> analyzeChecked(Template<?, ?> template, Connection conn)
      throws SQLException {
    Fragment fragment = template.fragment();
    List<Fragment> variants = OptionallyResolver.analysisVariants(fragment);
    ResultSetParser<?> parser = extractResultSetParser(template);
    List<QueryAnalysis> results = new ArrayList<>();
    for (Fragment variant : variants) {
      if (parser != null) {
        results.add(analyzeFragmentAndParserChecked(variant, parser, conn));
      } else {
        results.add(analyzeUpdate(new Operation.Update(variant), conn));
      }
    }
    return results;
  }

  public static List<QueryAnalysis> analyze(Operation<?> op, Connection conn) {
    try {
      return analyzeChecked(op, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> analyzeChecked(Operation<?> op, Connection conn)
      throws SQLException {
    return analyzeNamed(null, op, conn);
  }

  private static List<QueryAnalysis> analyzeNamed(String name, Operation<?> op, Connection conn)
      throws SQLException {
    return switch (op) {
      case Operation.Query<?> q ->
          List.of(analyzeFragmentAndParserChecked(name, q.query(), q.parser(), conn));
      case Operation.UpdateReturning<?> ur ->
          List.of(analyzeFragmentAndParserChecked(name, ur.query(), ur.parser(), conn));
      case Operation.Update u -> List.of(analyzeUpdate(name, u, conn));
      case Operation.Configured<?> c ->
          analyzeNamed(name != null ? name : c.name(), c.inner(), conn);
      case Operation.Mapped<?, ?> m -> analyzeNamed(name, m.source(), conn);
      case Operation.Combine<?, ?> w -> {
        var r = new ArrayList<>(analyzeNamed(null, w.first(), conn));
        r.addAll(analyzeNamed(null, w.second(), conn));
        yield r;
      }
      case Operation.IfEmpty<?> ie -> {
        var r = new ArrayList<>(analyzeNamed(null, ie.check(), conn));
        r.addAll(analyzeNamed(null, ie.fallback(), conn));
        yield r;
      }
      case Operation.Then<?, ?, ?> t -> {
        var r = new ArrayList<>(analyzeNamed(null, t.source(), conn));
        Fragment templateFragment = t.continuation().fragment();
        List<DbType<?>> paramTypes = templateFragment.parameterTypes();
        String sql = templateFragment.render();
        PreparedStatement ps;
        try {
          ps = conn.prepareStatement(sql);
        } catch (SQLException prepareEx) {
          r.add(QueryAnalysis.prepareFailed(sql, null, paramTypes, toPrepareFailure(prepareEx)));
          yield r;
        }
        try (ps) {
          List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
          List<JdbcMeta.ColumnMeta> colMeta = JdbcMeta.extractColumns(ps);
          boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();
          List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
              Alignment.align(paramTypes, paramMeta);
          ResultSetParser<?> templateParser = extractResultSetParser(t.continuation());
          List<DbType<?>> columnTypes =
              templateParser != null ? extractColumnTypes(templateParser) : List.of();
          List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
              Alignment.align(columnTypes, colMeta);
          r.add(new QueryAnalysis(sql, null, paramAlignment, colAlignment, paramMetaAvailable));
        } catch (SQLException metaEx) {
          r.add(QueryAnalysis.prepareFailed(sql, null, paramTypes, toPrepareFailure(metaEx)));
        }
        yield r;
      }
      case Operation.Streaming<?> s ->
          List.of(analyzeFragmentAndParserChecked(name, s.query(), s.codec().all(), conn));
      case Operation.Execute e ->
          List.of(analyzeUpdate(name, new Operation.Update(e.query()), conn));
      case Operation.Pure<?> ignored -> List.of();
      default -> List.of();
    };
  }

  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment, ResultSetParser<?> parser, Connection conn) {
    try {
      return analyzeFragmentAndParserChecked(null, fragment, parser, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static QueryAnalysis analyzeFragmentAndParserChecked(
      Fragment fragment, ResultSetParser<?> parser, Connection conn) throws SQLException {
    return analyzeFragmentAndParserChecked(null, fragment, parser, conn);
  }

  private static QueryAnalysis analyzeFragmentAndParserChecked(
      String name, Fragment fragment, ResultSetParser<?> parser, Connection conn)
      throws SQLException {

    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();
    List<DbType<?>> columnTypes = extractColumnTypes(parser);

    PreparedStatement ps;
    try {
      ps = conn.prepareStatement(sql);
    } catch (SQLException prepareEx) {
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(prepareEx));
    }
    try (ps) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      List<JdbcMeta.ColumnMeta> colMeta = JdbcMeta.extractColumns(ps);

      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
          Alignment.align(columnTypes, colMeta);

      return new QueryAnalysis(sql, name, paramAlignment, colAlignment, paramMetaAvailable);
    } catch (SQLException metaEx) {
      // prepare() succeeded but metadata extraction failed — still report as a prepare-phase
      // failure so the user gets a structured exception instead of a raw DatabaseException.
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(metaEx));
    }
  }

  private static QueryAnalysis analyzeUpdate(Operation.Update update, Connection conn)
      throws SQLException {
    return analyzeUpdate(null, update, conn);
  }

  private static QueryAnalysis analyzeUpdate(String name, Operation.Update update, Connection conn)
      throws SQLException {
    Fragment fragment = update.query();
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();

    PreparedStatement ps;
    try {
      ps = conn.prepareStatement(sql);
    } catch (SQLException prepareEx) {
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(prepareEx));
    }
    try (ps) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);

      return new QueryAnalysis(sql, name, paramAlignment, List.of(), paramMetaAvailable);
    } catch (SQLException metaEx) {
      return QueryAnalysis.prepareFailed(sql, name, paramTypes, toPrepareFailure(metaEx));
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Prepare-time failure helpers
  // ─────────────────────────────────────────────────────────────────────────────

  private static AlignmentError.PrepareFailure toPrepareFailure(SQLException ex) {
    String msg = ex.getMessage();
    return new AlignmentError.PrepareFailure(ex.getSQLState(), msg, parsePgPrepareHint(msg));
  }

  /**
   * Best-effort parse of PostgreSQL's "operator does not exist: {@code <lhs>} {@code <op>} {@code
   * <rhs>}" message. When it matches, produce a hint that mirrors the structured
   * ParameterTypeMismatch report — "The column type appears to be X but your declared type is Y".
   */
  private static final Pattern PG_OPERATOR_MISMATCH =
      Pattern.compile("operator does not exist: (\\S+)\\s+\\S+\\s+(\\S+)");

  static String parsePgPrepareHint(String driverMessage) {
    if (driverMessage == null) return null;
    Matcher m = PG_OPERATOR_MISMATCH.matcher(driverMessage);
    if (m.find()) {
      String expected = m.group(1);
      String provided = m.group(2);
      if (expected.equals(provided)) return null;
      return "The column type is '"
          + expected
          + "' but the declared parameter type is '"
          + provided
          + "'. Change the parameter type to match the column.";
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  private static ResultSetParser<?> extractResultSetParser(Template<?, ?> template) {
    return switch (template) {
      case Template.Query1 q -> q.parser();
      case Template.Query2 q -> q.parser();
      case Template.Query3 q -> q.parser();
      case Template.Query4 q -> q.parser();
      case Template.Query5 q -> q.parser();
      case Template.Query6 q -> q.parser();
      case Template.Query7 q -> q.parser();
      case Template.Query8 q -> q.parser();
      case Template.Query9 q -> q.parser();
      case Template.Query10 q -> q.parser();
      case Template.From f -> extractResultSetParser(f.inner());
      case RowTemplate.Query<?, ?> q -> q.resultParser();
      default -> null;
    };
  }

  private static List<DbType<?>> extractColumnTypes(ResultSetParser<?> parser) {
    if (parser instanceof ResultSetParser.Mapped<?, ?> m) {
      return extractColumnTypes(m.inner());
    } else if (parser instanceof ResultSetParser.First<?> f) {
      return f.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.MaxOne<?> m) {
      return m.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.ExactlyOne<?> e) {
      return e.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.All<?> a) {
      return a.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.Foreach<?> f) {
      return f.rowCodec().columns();
    } else {
      return List.of();
    }
  }
}
