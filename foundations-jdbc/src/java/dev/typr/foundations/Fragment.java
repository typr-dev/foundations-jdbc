package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public sealed interface Fragment {
  Fragment EMPTY = lit("");

  default String render() {
    StringBuilder sb = new StringBuilder();
    render(sb);
    return sb.toString();
  }

  void render(StringBuilder sb);

  default void set(PreparedStatement stmt) throws SQLException {
    set(stmt, new AtomicInteger(1));
  }

  void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException;

  /**
   * Extract all parameter types from this fragment.
   * Traverses the fragment tree and collects DbType instances from all Value nodes.
   * The types are returned in the order they appear in the SQL (left to right).
   *
   * <p>Used by query analysis to compare declared types against JDBC metadata.
   *
   * @return list of DbType instances for all parameters
   */
  default List<DbType<?>> parameterTypes() {
    List<DbType<?>> types = new ArrayList<>();
    collectParameterTypes(types);
    return types;
  }

  /**
   * Collect parameter types into the provided list.
   * Called by parameterTypes() to traverse the fragment tree.
   */
  void collectParameterTypes(List<DbType<?>> types);

  default Fragment append(Fragment other) {
    return new Append(this, other);
  }

  default <T> Operation.Query<T> query(ResultSetParser<T> parser) {
    return new Operation.Query<>(this, parser);
  }

  default Operation.Update update() {
    return new Operation.Update(this);
  }

  default <T> Operation.UpdateReturning<T> updateReturning(ResultSetParser<T> parser) {
    return new Operation.UpdateReturning<>(this, parser);
  }

  default <T> Operation.UpdateReturningGeneratedKeys<T> updateReturningGeneratedKeys(
      String[] columnNames, ResultSetParser<T> parser) {
    return new Operation.UpdateReturningGeneratedKeys<>(this, columnNames, parser);
  }

  default <Row> Operation.UpdateMany<Row> updateMany(RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateMany<>(this, parser, rows);
  }

  default <Row> Operation.UpdateManyReturning<Row> updateManyReturning(
      RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateManyReturning<>(this, parser, rows);
  }

  default <Row> Operation.UpdateReturningEach<Row> updateReturningEach(
      RowParser<Row> parser, Iterator<Row> rows) {
    return new Operation.UpdateReturningEach<>(this, parser, rows);
  }

  record Literal(String value) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append(value);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {}

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      // Literals have no parameters
    }
  }

  static Literal lit(String value) {
    return new Literal(value);
  }

  static Fragment empty() {
    return EMPTY;
  }

  static Literal quotedDouble(String value) {
    return new Literal('"' + value + '"');
  }

  static Literal quotedSingle(String value) {
    return new Literal("'" + value + "'");
  }

  record Append(Fragment a, Fragment b) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      a.render(sb);
      b.render(sb);
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      a.set(stmt, idx);
      b.set(stmt, idx);
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      a.collectParameterTypes(types);
      b.collectParameterTypes(types);
    }
  }

  record Value<A>(A value, DbType<A> type) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      sb.append('?');
      // Add type cast if the database type supports it (PostgreSQL yes, MariaDB no)
      // Skip text type - PostgreSQL handles implicit string conversion well,
      // and casting to text can conflict with bpchar comparison semantics
      if (type.typename().renderTypeCast()) {
        String sqlType = type.typename().sqlType();
        if (sqlType != null && !sqlType.isEmpty() && !sqlType.equals("text")) {
          sb.append("::");
          sb.append(sqlType);
        }
      }
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      type.write().set(stmt, idx.getAndIncrement(), value);
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      types.add(type);
    }
  }

  static <A> Value<A> value(A value, DbType<A> type) {
    return new Value<>(value, type);
  }

  /** Encode a value into a SQL fragment using the provided database type. */
  static <A> Fragment encode(DbType<A> type, A value) {
    return new Value<>(value, type);
  }

  record Concat(List<? extends Fragment> frags) implements Fragment {
    @Override
    public void render(StringBuilder sb) {
      for (Fragment frag : frags) {
        frag.render(sb);
      }
    }

    @Override
    public void set(PreparedStatement stmt, AtomicInteger idx) throws SQLException {
      for (Fragment frag : frags) {
        frag.set(stmt, idx);
      }
    }

    @Override
    public void collectParameterTypes(List<DbType<?>> types) {
      for (Fragment frag : frags) {
        frag.collectParameterTypes(types);
      }
    }
  }

  /** Returns `(f1 AND f2 AND ... fn)`. */
  static Fragment and(Fragment... fs) {
    return and(Arrays.asList(fs));
  }

  /** Returns `(f1 AND f2 AND ... fn)` for a non-empty collection. */
  static Fragment and(List<? extends Fragment> fs) {
    if (fs.isEmpty()) return EMPTY;
    else return join(fs, lit(" AND "));
  }

  /** Returns `(f1 OR f2 OR ... fn)`. */
  static Fragment or(Fragment... fs) {
    return or(Arrays.asList(fs));
  }

  /** Returns `(f1 OR f2 OR ... fn)` */
  static Fragment or(List<? extends Fragment> fs) {
    if (fs.isEmpty()) return EMPTY;
    else return join(fs, lit(" OR "));
  }

  /** Returns `WHERE f1 AND f2 AND ... fn` */
  static Fragment whereAnd(Fragment... fs) {
    return whereAnd(Arrays.asList(fs));
  }

  /** Returns `WHERE f1 AND f2 AND ... fn` */
  static Fragment whereAnd(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return lit("WHERE ").append(and(fs));
    }
  }

  /** Returns `WHERE f1 OR f2 OR ... fn`. */
  static Fragment whereOr(Fragment... fs) {
    return whereOr(Arrays.asList(fs));
  }

  /** Returns `WHERE f1 OR f2 OR ... fn`. */
  static Fragment whereOr(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return lit("WHERE ").append(or(fs));
    }
  }

  /** Returns `SET f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment set(Fragment... fs) {
    return set(Arrays.asList(fs));
  }

  /** Returns `SET f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment set(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return lit("SET ").append(comma(fs));
    }
  }

  /** Returns `(f)`. */
  static Fragment parentheses(Fragment f) {
    return lit("(").append(f).append(lit(")"));
  }

  /** Returns `f1, f2, ... fn`. */
  static Fragment comma(Fragment... fs) {
    return comma(Arrays.asList(fs));
  }

  /** Returns `f1, f2, ... fn`. */
  static Fragment comma(List<? extends Fragment> fs) {
    return join(fs, lit(", "));
  }

  /** Returns `ORDER BY f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment orderBy(Fragment... fs) {
    return orderBy(Arrays.asList(fs));
  }

  /** Returns `ORDER BY f1, f2, ... fn` or the empty fragment if `fs` is empty. */
  static Fragment orderBy(List<? extends Fragment> fs) {
    if (fs.isEmpty()) {
      return EMPTY;
    } else {
      return lit("ORDER BY ").append(comma(fs));
    }
  }

  static Concat join(List<? extends Fragment> fs, Fragment sep) {
    var list = new ArrayList<Fragment>();
    var first = true;
    for (Fragment f : fs) {
      if (!first) {
        list.add(sep);
      }
      list.add(f);
      first = false;
    }
    return new Concat(list);
  }

  static Concat concat(Fragment... fs) {
    return new Concat(Arrays.asList(fs));
  }

  /** Builder for creating Fragments with a fluent API */
  class Builder {
    private final List<Fragment> fragments = new ArrayList<>();

    public Builder() {}

    /** Add a string fragment */
    public Builder sql(String s) {
      fragments.add(lit(s));
      return this;
    }

    /** Add a parameter with its type and value */
    public <T> Builder param(DbType<T> type, T value) {
      fragments.add(Fragment.value(value, type));
      return this;
    }

    /** Add a Fragment directly */
    public Builder param(Fragment fragment) {
      fragments.add(fragment);
      return this;
    }

    /** Build the final Fragment */
    public Fragment done() {
      return new Concat(fragments);
    }
  }

  /**
   * Start building a Fragment with a fluent API. Example: Fragment.interpolate("SELECT * FROM users
   * WHERE name = ") .param(PgTypes.text, "Alice") .sql(" AND age > ") .param(PgTypes.int4, 25)
   * .done()
   */
  static Builder interpolate(String initial) {
    return new Builder().sql(initial);
  }

  /**
   * Create a Fragment directly from varargs fragments. Example: Fragment.interpolate(lit("SELECT *
   * FROM users WHERE name = "), nameParam, lit(" AND age > "), ageParam)
   */
  static Fragment interpolate(Fragment... fragments) {
    return new Concat(Arrays.asList(fragments));
  }
}
