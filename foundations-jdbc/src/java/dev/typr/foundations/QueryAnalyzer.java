package dev.typr.foundations;

import dev.typr.foundations.internal.JdbcStatementAnalyzer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  public static List<QueryAnalysis> analyze(Analyzable analyzable, Connection conn) {
    return AnalysisRunner.analyze(analyzable, new JdbcStatementAnalyzer(conn.unwrap()));
  }

  public static List<QueryAnalysis> analyze(Template<?, ?> template, Connection conn) {
    return AnalysisRunner.analyze(template, new JdbcStatementAnalyzer(conn.unwrap()));
  }

  public static List<QueryAnalysis> analyze(Operation<?> op, Connection conn) {
    return AnalysisRunner.analyze(op, new JdbcStatementAnalyzer(conn.unwrap()));
  }

  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment, ResultSetParser<?> parser, Connection conn) {
    return AnalysisRunner.analyzeFragmentAndParser(
        Optional.empty(), fragment, parser, new JdbcStatementAnalyzer(conn.unwrap()));
  }

  /**
   * @deprecated Use the overload that takes {@link Connection} instead.
   */
  @Deprecated
  public static List<QueryAnalysis> analyze(Analyzable analyzable, java.sql.Connection conn) {
    return AnalysisRunner.analyze(analyzable, new JdbcStatementAnalyzer(conn));
  }

  /**
   * @deprecated Use the overload that takes {@link Connection} instead.
   */
  @Deprecated
  public static List<QueryAnalysis> analyze(Template<?, ?> template, java.sql.Connection conn) {
    return AnalysisRunner.analyze(template, new JdbcStatementAnalyzer(conn));
  }

  /**
   * @deprecated Use the overload that takes {@link Connection} instead.
   */
  @Deprecated
  public static List<QueryAnalysis> analyze(Operation<?> op, java.sql.Connection conn) {
    return AnalysisRunner.analyze(op, new JdbcStatementAnalyzer(conn));
  }

  /**
   * @deprecated Use the overload that takes {@link Connection} instead.
   */
  @Deprecated
  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment, ResultSetParser<?> parser, java.sql.Connection conn) {
    return AnalysisRunner.analyzeFragmentAndParser(
        Optional.empty(), fragment, parser, new JdbcStatementAnalyzer(conn));
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
  public static ResultSetParser<?> extractResultSetParser(Template<?, ?> template) {
    return switch (template) {
      case TemplateRead.Query1 q -> q.parser();
      case TemplateRead.Query2 q -> q.parser();
      case TemplateRead.Query3 q -> q.parser();
      case TemplateRead.Query4 q -> q.parser();
      case TemplateRead.Query5 q -> q.parser();
      case TemplateRead.Query6 q -> q.parser();
      case TemplateRead.Query7 q -> q.parser();
      case TemplateRead.Query8 q -> q.parser();
      case TemplateRead.Query9 q -> q.parser();
      case TemplateRead.Query10 q -> q.parser();
      case Template.From f -> extractResultSetParser(f.inner());
      case RowTemplate.Query<?, ?> q -> q.resultParser();
      default -> null;
    };
  }

  public static List<DbType<?>> extractColumnTypes(ResultSetParser<?> parser) {
    if (parser instanceof ResultSetParser.Mapped<?, ?> m) {
      return extractColumnTypes(m.inner());
    } else if (parser instanceof ResultSetParser.First<?>(RowCodec<?> rowCodec2)) {
      return rowCodec2.columns();
    } else if (parser instanceof ResultSetParser.MaxOne<?>(RowCodec<?> rowCodec1)) {
      return rowCodec1.columns();
    } else if (parser instanceof ResultSetParser.ExactlyOne<?>(RowCodec<?> codec)) {
      return codec.columns();
    } else if (parser instanceof ResultSetParser.All<?>(RowCodec<?> rowCodec)) {
      return rowCodec.columns();
    } else if (parser instanceof ResultSetParser.Foreach<?> f) {
      return f.rowCodec().columns();
    } else {
      return List.of();
    }
  }
}
