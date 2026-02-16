package dev.typr.foundations;

public sealed interface SqlTemplate<In, Out>
    permits SqlTemplate.Query1,
        SqlTemplate.Query2,
        SqlTemplate.Query3,
        SqlTemplate.Query4,
        SqlTemplate.Query5,
        SqlTemplate.Query6,
        SqlTemplate.Query7,
        SqlTemplate.Query8,
        SqlTemplate.Query9,
        SqlTemplate.Query10,
        SqlTemplate.Update1,
        SqlTemplate.Update2,
        SqlTemplate.Update3,
        SqlTemplate.Update4,
        SqlTemplate.Update5,
        SqlTemplate.Update6,
        SqlTemplate.Update7,
        SqlTemplate.Update8,
        SqlTemplate.Update9,
        SqlTemplate.Update10 {

  Operation<Out> on(In in);

  Fragment fragment();

  record Query1<P0, Out>(Fragment fragment, DbType<P0> p0Type, ResultSetParser<Out> parser)
      implements SqlTemplate<P0, Out> {
    @Override
    public Operation.Query<Out> on(P0 p0) {
      return new Operation.Query<>(
          fragment.fill(java.util.List.of((Object) p0).iterator()), parser);
    }
  }

  record Query2<P0, P1, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<P0, P1>, Out> {
    @Override
    public Operation.Query<Out> on(And<P0, P1> in) {
      return on(in.left(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1).iterator()),
          parser);
    }
  }

  record Query3<P0, P1, P2, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<P0, P1>, P2>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<P0, P1>, P2> in) {
      return on(in.left().left(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2).iterator()),
          parser);
    }
  }

  record Query4<P0, P1, P2, P3, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<P0, P1>, P2>, P3>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<P0, P1>, P2>, P3> in) {
      return on(in.left().left().left(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3).iterator()),
          parser);
    }
  }

  record Query5<P0, P1, P2, P3, P4, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<P0, P1>, P2>, P3>, P4>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<P0, P1>, P2>, P3>, P4> in) {
      return on(in.left().left().left().left(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4).iterator()),
          parser);
    }
  }

  record Query6<P0, P1, P2, P3, P4, P5, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5> in) {
      return on(in.left().left().left().left().left(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5).iterator()),
          parser);
    }
  }

  record Query7<P0, P1, P2, P3, P4, P5, P6, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      DbType<P6> p6Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6> in) {
      return on(in.left().left().left().left().left().left(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6).iterator()),
          parser);
    }
  }

  record Query8<P0, P1, P2, P3, P4, P5, P6, P7, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      DbType<P6> p6Type,
      DbType<P7> p7Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7> in) {
      return on(in.left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7).iterator()),
          parser);
    }
  }

  record Query9<P0, P1, P2, P3, P4, P5, P6, P7, P8, Out>(
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
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8> in) {
      return on(in.left().left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8).iterator()),
          parser);
    }
  }

  record Query10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, Out>(
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
      DbType<P9> p9Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<And<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>, Out> {
    @Override
    public Operation.Query<Out> on(And<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9> in) {
      return on(in.left().left().left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9) {
      return new Operation.Query<>(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8, (Object) p9).iterator()),
          parser);
    }
  }

  record Update1<P0>(Fragment fragment, DbType<P0> p0Type)
      implements SqlTemplate<P0, Integer> {
    @Override
    public Operation.Update on(P0 p0) {
      return new Operation.Update(
          fragment.fill(java.util.List.of((Object) p0).iterator()));
    }
  }

  record Update2<P0, P1>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type)
      implements SqlTemplate<And<P0, P1>, Integer> {
    @Override
    public Operation.Update on(And<P0, P1> in) {
      return on(in.left(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1).iterator()));
    }
  }

  record Update3<P0, P1, P2>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type)
      implements SqlTemplate<And<And<P0, P1>, P2>, Integer> {
    @Override
    public Operation.Update on(And<And<P0, P1>, P2> in) {
      return on(in.left().left(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2).iterator()));
    }
  }

  record Update4<P0, P1, P2, P3>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type)
      implements SqlTemplate<And<And<And<P0, P1>, P2>, P3>, Integer> {
    @Override
    public Operation.Update on(And<And<And<P0, P1>, P2>, P3> in) {
      return on(in.left().left().left(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3).iterator()));
    }
  }

  record Update5<P0, P1, P2, P3, P4>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type)
      implements SqlTemplate<And<And<And<And<P0, P1>, P2>, P3>, P4>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<P0, P1>, P2>, P3>, P4> in) {
      return on(in.left().left().left().left(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4).iterator()));
    }
  }

  record Update6<P0, P1, P2, P3, P4, P5>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type)
      implements SqlTemplate<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5> in) {
      return on(in.left().left().left().left().left(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5).iterator()));
    }
  }

  record Update7<P0, P1, P2, P3, P4, P5, P6>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      DbType<P6> p6Type)
      implements SqlTemplate<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6> in) {
      return on(in.left().left().left().left().left().left(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6).iterator()));
    }
  }

  record Update8<P0, P1, P2, P3, P4, P5, P6, P7>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      DbType<P6> p6Type,
      DbType<P7> p7Type)
      implements SqlTemplate<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7> in) {
      return on(in.left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7).iterator()));
    }
  }

  record Update9<P0, P1, P2, P3, P4, P5, P6, P7, P8>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type,
      DbType<P5> p5Type,
      DbType<P6> p6Type,
      DbType<P7> p7Type,
      DbType<P8> p8Type)
      implements SqlTemplate<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8> in) {
      return on(in.left().left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8).iterator()));
    }
  }

  record Update10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>(
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
      DbType<P9> p9Type)
      implements SqlTemplate<And<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9>, Integer> {
    @Override
    public Operation.Update on(And<And<And<And<And<And<And<And<And<P0, P1>, P2>, P3>, P4>, P5>, P6>, P7>, P8>, P9> in) {
      return on(in.left().left().left().left().left().left().left().left().left(),
          in.left().left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().left().right(),
          in.left().left().left().left().left().left().right(),
          in.left().left().left().left().left().right(),
          in.left().left().left().left().right(),
          in.left().left().left().right(),
          in.left().left().right(),
          in.left().right(),
          in.right());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9) {
      return new Operation.Update(
          fragment.fill(
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8, (Object) p9).iterator()));
    }
  }
}
