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
        SqlTemplate.Update10,
        SqlTemplate.From {

  Operation<Out> on(In in);

  Fragment fragment();

  record Query1<P0, Out>(Fragment fragment, DbType<P0> p0Type, ResultSetParser<Out> parser)
      implements SqlTemplate<P0, Out> {
    @Override
    public Operation.Query<Out> on(P0 p0) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment, java.util.List.of((Object) p0).iterator()), parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0) {
      return new From<>(this, t -> on(f0.apply(t)));
    }
  }

  record Query2<P0, P1, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<Tuple.Tuple2<P0, P1>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple2<P0, P1> in) {
      return on(in._1(),
          in._2());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t)));
    }
  }

  record Query3<P0, P1, P2, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<Tuple.Tuple3<P0, P1, P2>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple3<P0, P1, P2> in) {
      return on(in._1(),
          in._2(),
          in._3());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t)));
    }
  }

  record Query4<P0, P1, P2, P3, Out>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      ResultSetParser<Out> parser)
      implements SqlTemplate<Tuple.Tuple4<P0, P1, P2, P3>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple4<P0, P1, P2, P3> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple5<P0, P1, P2, P3, P4>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple5<P0, P1, P2, P3, P4> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple6<P0, P1, P2, P3, P4, P5>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple6<P0, P1, P2, P3, P4, P5> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8(),
          in._9());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7, java.util.function.Function<T, P8> f8) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>, Out> {
    @Override
    public Operation.Query<Out> on(Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8(),
          in._9(),
          in._10());
    }

    public Operation.Query<Out> on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8, (Object) p9).iterator()),
          parser);
    }

    public <T> From<T, Out> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7, java.util.function.Function<T, P8> f8, java.util.function.Function<T, P9> f9) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t)));
    }
  }

  record Update1<P0>(Fragment fragment, DbType<P0> p0Type)
      implements SqlTemplate<P0, Integer> {
    @Override
    public Operation.Update on(P0 p0) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment, java.util.List.of((Object) p0).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0) {
      return new From<>(this, t -> on(f0.apply(t)));
    }
  }

  record Update2<P0, P1>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type)
      implements SqlTemplate<Tuple.Tuple2<P0, P1>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple2<P0, P1> in) {
      return on(in._1(),
          in._2());
    }

    public Operation.Update on(P0 p0, P1 p1) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t)));
    }
  }

  record Update3<P0, P1, P2>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type)
      implements SqlTemplate<Tuple.Tuple3<P0, P1, P2>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple3<P0, P1, P2> in) {
      return on(in._1(),
          in._2(),
          in._3());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t)));
    }
  }

  record Update4<P0, P1, P2, P3>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type)
      implements SqlTemplate<Tuple.Tuple4<P0, P1, P2, P3>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple4<P0, P1, P2, P3> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t)));
    }
  }

  record Update5<P0, P1, P2, P3, P4>(
      Fragment fragment,
      DbType<P0> p0Type,
      DbType<P1> p1Type,
      DbType<P2> p2Type,
      DbType<P3> p3Type,
      DbType<P4> p4Type)
      implements SqlTemplate<Tuple.Tuple5<P0, P1, P2, P3, P4>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple5<P0, P1, P2, P3, P4> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple6<P0, P1, P2, P3, P4, P5>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple6<P0, P1, P2, P3, P4, P5> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple7<P0, P1, P2, P3, P4, P5, P6> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple8<P0, P1, P2, P3, P4, P5, P6, P7> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple9<P0, P1, P2, P3, P4, P5, P6, P7, P8> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8(),
          in._9());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7, java.util.function.Function<T, P8> f8) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t)));
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
      implements SqlTemplate<Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9>, Integer> {
    @Override
    public Operation.Update on(Tuple.Tuple10<P0, P1, P2, P3, P4, P5, P6, P7, P8, P9> in) {
      return on(in._1(),
          in._2(),
          in._3(),
          in._4(),
          in._5(),
          in._6(),
          in._7(),
          in._8(),
          in._9(),
          in._10());
    }

    public Operation.Update on(P0 p0, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of((Object) p0, (Object) p1, (Object) p2, (Object) p3, (Object) p4, (Object) p5, (Object) p6, (Object) p7, (Object) p8, (Object) p9).iterator()));
    }

    public <T> From<T, Integer> from(java.util.function.Function<T, P0> f0, java.util.function.Function<T, P1> f1, java.util.function.Function<T, P2> f2, java.util.function.Function<T, P3> f3, java.util.function.Function<T, P4> f4, java.util.function.Function<T, P5> f5, java.util.function.Function<T, P6> f6, java.util.function.Function<T, P7> f7, java.util.function.Function<T, P8> f8, java.util.function.Function<T, P9> f9) {
      return new From<>(this, t -> on(f0.apply(t), f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t)));
    }
  }

  record From<T, Out>(
      SqlTemplate<?, Out> inner,
      java.util.function.Function<T, ? extends Operation<Out>> resolver)
      implements SqlTemplate<T, Out> {
    @Override
    public Operation<Out> on(T t) {
      return resolver.apply(t);
    }

    @Override
    public Fragment fragment() {
      return inner.fragment();
    }
  }
}
