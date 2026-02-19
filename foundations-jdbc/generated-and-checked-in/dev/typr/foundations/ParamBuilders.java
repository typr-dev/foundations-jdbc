package dev.typr.foundations;

public class ParamBuilders {

  public static class ParamBuilder1<P0> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;

    ParamBuilder1(
        Fragment fragment,
        DbType<P0> p0Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
    }

    public ParamBuilder1<P0> append(String s) {
      return new ParamBuilder1<>(fragment.append(Fragment.of(s)), p0Type);
    }

    public <T> ParamBuilder1<P0> value(DbType<T> type, T value) {
      return new ParamBuilder1<>(fragment.append(Fragment.value(value, type)), p0Type);
    }

    public ParamBuilder1<P0> append(Fragment other) {
      return new ParamBuilder1<>(fragment.append(other), p0Type);
    }

    public <P1> ParamBuilder2<P0, P1> param(DbType<P1> type) {
      return new ParamBuilder2<>(fragment.append(new Fragment.Param<>(type)), p0Type, type);
    }
    public ParamBuilder2<P0, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder2<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder2<P0, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder2<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder2<P0, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder2<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder2<P0, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder2<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, null);
    }

    public <Out> SqlTemplate.Query1<P0, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query1<>(
          fragment, p0Type, parser);
    }

    public SqlTemplate.Update1<P0> update() {
      return new SqlTemplate.Update1<>(
          fragment, p0Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder2<P0, P1> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;

    ParamBuilder2(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
    }

    public ParamBuilder2<P0, P1> append(String s) {
      return new ParamBuilder2<>(fragment.append(Fragment.of(s)), p0Type, p1Type);
    }

    public <T> ParamBuilder2<P0, P1> value(DbType<T> type, T value) {
      return new ParamBuilder2<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type);
    }

    public ParamBuilder2<P0, P1> append(Fragment other) {
      return new ParamBuilder2<>(fragment.append(other), p0Type, p1Type);
    }

    public <P2> ParamBuilder3<P0, P1, P2> param(DbType<P2> type) {
      return new ParamBuilder3<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, type);
    }
    public ParamBuilder3<P0, P1, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder3<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder3<P0, P1, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder3<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder3<P0, P1, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder3<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder3<P0, P1, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder3<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, null);
    }

    public <Out> SqlTemplate.Query2<P0, P1, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query2<>(
          fragment, p0Type, p1Type, parser);
    }

    public SqlTemplate.Update2<P0, P1> update() {
      return new SqlTemplate.Update2<>(
          fragment, p0Type, p1Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder3<P0, P1, P2> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;

    ParamBuilder3(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
    }

    public ParamBuilder3<P0, P1, P2> append(String s) {
      return new ParamBuilder3<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type);
    }

    public <T> ParamBuilder3<P0, P1, P2> value(DbType<T> type, T value) {
      return new ParamBuilder3<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type);
    }

    public ParamBuilder3<P0, P1, P2> append(Fragment other) {
      return new ParamBuilder3<>(fragment.append(other), p0Type, p1Type, p2Type);
    }

    public <P3> ParamBuilder4<P0, P1, P2, P3> param(DbType<P3> type) {
      return new ParamBuilder4<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, type);
    }
    public ParamBuilder4<P0, P1, P2, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder4<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder4<P0, P1, P2, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder4<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder4<P0, P1, P2, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder4<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder4<P0, P1, P2, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder4<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, null);
    }

    public <Out> SqlTemplate.Query3<P0, P1, P2, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query3<>(
          fragment, p0Type, p1Type, p2Type, parser);
    }

    public SqlTemplate.Update3<P0, P1, P2> update() {
      return new SqlTemplate.Update3<>(
          fragment, p0Type, p1Type, p2Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder4<P0, P1, P2, P3> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;

    ParamBuilder4(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
    }

    public ParamBuilder4<P0, P1, P2, P3> append(String s) {
      return new ParamBuilder4<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type);
    }

    public <T> ParamBuilder4<P0, P1, P2, P3> value(DbType<T> type, T value) {
      return new ParamBuilder4<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type);
    }

    public ParamBuilder4<P0, P1, P2, P3> append(Fragment other) {
      return new ParamBuilder4<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type);
    }

    public <P4> ParamBuilder5<P0, P1, P2, P3, P4> param(DbType<P4> type) {
      return new ParamBuilder5<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, type);
    }
    public ParamBuilder5<P0, P1, P2, P3, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder5<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder5<P0, P1, P2, P3, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder5<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder5<P0, P1, P2, P3, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder5<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder5<P0, P1, P2, P3, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder5<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, null);
    }

    public <Out> SqlTemplate.Query4<P0, P1, P2, P3, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query4<>(
          fragment, p0Type, p1Type, p2Type, p3Type, parser);
    }

    public SqlTemplate.Update4<P0, P1, P2, P3> update() {
      return new SqlTemplate.Update4<>(
          fragment, p0Type, p1Type, p2Type, p3Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder5<P0, P1, P2, P3, P4> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;

    ParamBuilder5(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
    }

    public ParamBuilder5<P0, P1, P2, P3, P4> append(String s) {
      return new ParamBuilder5<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type);
    }

    public <T> ParamBuilder5<P0, P1, P2, P3, P4> value(DbType<T> type, T value) {
      return new ParamBuilder5<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type);
    }

    public ParamBuilder5<P0, P1, P2, P3, P4> append(Fragment other) {
      return new ParamBuilder5<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type);
    }

    public <P5> ParamBuilder6<P0, P1, P2, P3, P4, P5> param(DbType<P5> type) {
      return new ParamBuilder6<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, p4Type, type);
    }
    public ParamBuilder6<P0, P1, P2, P3, P4, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder6<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, p4Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder6<P0, P1, P2, P3, P4, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder6<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder6<P0, P1, P2, P3, P4, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder6<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder6<P0, P1, P2, P3, P4, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder6<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, null);
    }

    public <Out> SqlTemplate.Query5<P0, P1, P2, P3, P4, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query5<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, parser);
    }

    public SqlTemplate.Update5<P0, P1, P2, P3, P4> update() {
      return new SqlTemplate.Update5<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder6<P0, P1, P2, P3, P4, P5> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;
    private final DbType<P5> p5Type;

    ParamBuilder6(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type,
        DbType<P5> p5Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
      this.p5Type = p5Type;
    }

    public ParamBuilder6<P0, P1, P2, P3, P4, P5> append(String s) {
      return new ParamBuilder6<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type);
    }

    public <T> ParamBuilder6<P0, P1, P2, P3, P4, P5> value(DbType<T> type, T value) {
      return new ParamBuilder6<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type);
    }

    public ParamBuilder6<P0, P1, P2, P3, P4, P5> append(Fragment other) {
      return new ParamBuilder6<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type);
    }

    public <P6> ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> param(DbType<P6> type) {
      return new ParamBuilder7<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, type);
    }
    public ParamBuilder7<P0, P1, P2, P3, P4, P5, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder7<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder7<P0, P1, P2, P3, P4, P5, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder7<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder7<P0, P1, P2, P3, P4, P5, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder7<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder7<P0, P1, P2, P3, P4, P5, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder7<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, null);
    }

    public <Out> SqlTemplate.Query6<P0, P1, P2, P3, P4, P5, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query6<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, parser);
    }

    public SqlTemplate.Update6<P0, P1, P2, P3, P4, P5> update() {
      return new SqlTemplate.Update6<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;
    private final DbType<P5> p5Type;
    private final DbType<P6> p6Type;

    ParamBuilder7(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type,
        DbType<P5> p5Type,
        DbType<P6> p6Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
      this.p5Type = p5Type;
      this.p6Type = p6Type;
    }

    public ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> append(String s) {
      return new ParamBuilder7<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type);
    }

    public <T> ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> value(DbType<T> type, T value) {
      return new ParamBuilder7<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type);
    }

    public ParamBuilder7<P0, P1, P2, P3, P4, P5, P6> append(Fragment other) {
      return new ParamBuilder7<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type);
    }

    public <P7> ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> param(DbType<P7> type) {
      return new ParamBuilder8<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, type);
    }
    public ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder8<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder8<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder8<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder8<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, null);
    }

    public <Out> SqlTemplate.Query7<P0, P1, P2, P3, P4, P5, P6, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query7<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, parser);
    }

    public SqlTemplate.Update7<P0, P1, P2, P3, P4, P5, P6> update() {
      return new SqlTemplate.Update7<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;
    private final DbType<P5> p5Type;
    private final DbType<P6> p6Type;
    private final DbType<P7> p7Type;

    ParamBuilder8(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type,
        DbType<P5> p5Type,
        DbType<P6> p6Type,
        DbType<P7> p7Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
      this.p5Type = p5Type;
      this.p6Type = p6Type;
      this.p7Type = p7Type;
    }

    public ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> append(String s) {
      return new ParamBuilder8<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type);
    }

    public <T> ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> value(DbType<T> type, T value) {
      return new ParamBuilder8<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type);
    }

    public ParamBuilder8<P0, P1, P2, P3, P4, P5, P6, P7> append(Fragment other) {
      return new ParamBuilder8<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type);
    }

    public <P8> ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> param(DbType<P8> type) {
      return new ParamBuilder9<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, type);
    }
    public ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder9<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder9<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder9<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder9<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, null);
    }

    public <Out> SqlTemplate.Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query8<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, parser);
    }

    public SqlTemplate.Update8<P0, P1, P2, P3, P4, P5, P6, P7> update() {
      return new SqlTemplate.Update8<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;
    private final DbType<P5> p5Type;
    private final DbType<P6> p6Type;
    private final DbType<P7> p7Type;
    private final DbType<P8> p8Type;

    ParamBuilder9(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type,
        DbType<P5> p5Type,
        DbType<P6> p6Type,
        DbType<P7> p7Type,
        DbType<P8> p8Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
      this.p5Type = p5Type;
      this.p6Type = p6Type;
      this.p7Type = p7Type;
      this.p8Type = p8Type;
    }

    public ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> append(String s) {
      return new ParamBuilder9<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type);
    }

    public <T> ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> value(DbType<T> type, T value) {
      return new ParamBuilder9<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type);
    }

    public ParamBuilder9<P0, P1, P2, P3, P4, P5, P6, P7, P8> append(Fragment other) {
      return new ParamBuilder9<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type);
    }

    public <P9> ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> param(DbType<P9> type) {
      return new ParamBuilder10<>(fragment.append(new Fragment.Param<>(type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, type);
    }
    public ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder10<>(fragment.append(new Fragment.Optionally(inner, 0)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder10<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder10<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, null);
    }
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder10<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, null);
    }

    public <Out> SqlTemplate.Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query9<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, parser);
    }

    public SqlTemplate.Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8> update() {
      return new SqlTemplate.Update9<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type);
    }

    public Fragment done() {
      return fragment;
    }
  }

  public static class ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> {
    private final Fragment fragment;
    private final DbType<P0> p0Type;
    private final DbType<P1> p1Type;
    private final DbType<P2> p2Type;
    private final DbType<P3> p3Type;
    private final DbType<P4> p4Type;
    private final DbType<P5> p5Type;
    private final DbType<P6> p6Type;
    private final DbType<P7> p7Type;
    private final DbType<P8> p8Type;
    private final DbType<P9> p9Type;

    ParamBuilder10(
        Fragment fragment,
        DbType<P0> p0Type,
        DbType<P1> p1Type,
        DbType<P2> p2Type,
        DbType<P3> p3Type,
        DbType<P4> p4Type,
        DbType<P5> p5Type,
        DbType<P6> p6Type,
        DbType<P7> p7Type,
        DbType<P8> p8Type,
        DbType<P9> p9Type) {
      this.fragment = fragment;
      this.p0Type = p0Type;
      this.p1Type = p1Type;
      this.p2Type = p2Type;
      this.p3Type = p3Type;
      this.p4Type = p4Type;
      this.p5Type = p5Type;
      this.p6Type = p6Type;
      this.p7Type = p7Type;
      this.p8Type = p8Type;
      this.p9Type = p9Type;
    }

    public ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> append(String s) {
      return new ParamBuilder10<>(fragment.append(Fragment.of(s)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, p9Type);
    }

    public <T> ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> value(DbType<T> type, T value) {
      return new ParamBuilder10<>(fragment.append(Fragment.value(value, type)), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, p9Type);
    }

    public ParamBuilder10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> append(Fragment other) {
      return new ParamBuilder10<>(fragment.append(other), p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, p9Type);
    }


    public <Out> SqlTemplate.Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out> query(ResultSetParser<Out> parser) {
      return new SqlTemplate.Query10<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, p9Type, parser);
    }

    public SqlTemplate.Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> update() {
      return new SqlTemplate.Update10<>(
          fragment, p0Type, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type, p7Type, p8Type, p9Type);
    }

    public Fragment done() {
      return fragment;
    }
  }
}
