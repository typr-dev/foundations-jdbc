package dev.typr.foundations;

/**
 * Tuple value types for the DSL.
 * <p>
 * Use {@link #of} factory methods to create tuple instances.
 * These are used as Row types in queries.
 */
public sealed interface Tuple {
    /** Returns all elements as an Object array. */
    Object[] asArray();

    // Tuple value types (interfaces with Impl records)
    /**
     * Tuple with 1 element.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple1<T0> extends Tuple {
        T0 _1();

        @Override
        default Object[] asArray() {
            return new Object[] { _1() };
        }

        /** Default implementation record for Tuple1. */
        record Impl<T0>(T0 _1) implements Tuple1<T0> {}
    }

    /**
     * Tuple with 2 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple2<T0, T1> extends Tuple {
        T0 _1();
        T1 _2();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2() };
        }

        /** Default implementation record for Tuple2. */
        record Impl<T0, T1>(T0 _1, T1 _2) implements Tuple2<T0, T1> {}
    }

    /**
     * Tuple with 3 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple3<T0, T1, T2> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3() };
        }

        /** Default implementation record for Tuple3. */
        record Impl<T0, T1, T2>(T0 _1, T1 _2, T2 _3) implements Tuple3<T0, T1, T2> {}
    }

    /**
     * Tuple with 4 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple4<T0, T1, T2, T3> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4() };
        }

        /** Default implementation record for Tuple4. */
        record Impl<T0, T1, T2, T3>(T0 _1, T1 _2, T2 _3, T3 _4) implements Tuple4<T0, T1, T2, T3> {}
    }

    /**
     * Tuple with 5 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple5<T0, T1, T2, T3, T4> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5() };
        }

        /** Default implementation record for Tuple5. */
        record Impl<T0, T1, T2, T3, T4>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5) implements Tuple5<T0, T1, T2, T3, T4> {}
    }

    /**
     * Tuple with 6 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple6<T0, T1, T2, T3, T4, T5> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6() };
        }

        /** Default implementation record for Tuple6. */
        record Impl<T0, T1, T2, T3, T4, T5>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6) implements Tuple6<T0, T1, T2, T3, T4, T5> {}
    }

    /**
     * Tuple with 7 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple7<T0, T1, T2, T3, T4, T5, T6> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7() };
        }

        /** Default implementation record for Tuple7. */
        record Impl<T0, T1, T2, T3, T4, T5, T6>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7) implements Tuple7<T0, T1, T2, T3, T4, T5, T6> {}
    }

    /**
     * Tuple with 8 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8() };
        }

        /** Default implementation record for Tuple8. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8) implements Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> {}
    }

    /**
     * Tuple with 9 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9() };
        }

        /** Default implementation record for Tuple9. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9) implements Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> {}
    }

    /**
     * Tuple with 10 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10() };
        }

        /** Default implementation record for Tuple10. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10) implements Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {}
    }

    /**
     * Tuple with 11 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11() };
        }

        /** Default implementation record for Tuple11. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11) implements Tuple11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {}
    }

    /**
     * Tuple with 12 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12() };
        }

        /** Default implementation record for Tuple12. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12) implements Tuple12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {}
    }

    /**
     * Tuple with 13 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13() };
        }

        /** Default implementation record for Tuple13. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13) implements Tuple13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {}
    }

    /**
     * Tuple with 14 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14() };
        }

        /** Default implementation record for Tuple14. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14) implements Tuple14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {}
    }

    /**
     * Tuple with 15 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15() };
        }

        /** Default implementation record for Tuple15. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15) implements Tuple15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {}
    }

    /**
     * Tuple with 16 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16() };
        }

        /** Default implementation record for Tuple16. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16) implements Tuple16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {}
    }

    /**
     * Tuple with 17 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17() };
        }

        /** Default implementation record for Tuple17. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17) implements Tuple17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {}
    }

    /**
     * Tuple with 18 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18() };
        }

        /** Default implementation record for Tuple18. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18) implements Tuple18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {}
    }

    /**
     * Tuple with 19 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19() };
        }

        /** Default implementation record for Tuple19. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19) implements Tuple19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {}
    }

    /**
     * Tuple with 20 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20() };
        }

        /** Default implementation record for Tuple20. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20) implements Tuple20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {}
    }

    /**
     * Tuple with 21 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21() };
        }

        /** Default implementation record for Tuple21. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21) implements Tuple21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {}
    }

    /**
     * Tuple with 22 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22() };
        }

        /** Default implementation record for Tuple22. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22) implements Tuple22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {}
    }

    /**
     * Tuple with 23 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23() };
        }

        /** Default implementation record for Tuple23. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23) implements Tuple23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {}
    }

    /**
     * Tuple with 24 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24() };
        }

        /** Default implementation record for Tuple24. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24) implements Tuple24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {}
    }

    /**
     * Tuple with 25 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25() };
        }

        /** Default implementation record for Tuple25. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25) implements Tuple25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {}
    }

    /**
     * Tuple with 26 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26() };
        }

        /** Default implementation record for Tuple26. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26) implements Tuple26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {}
    }

    /**
     * Tuple with 27 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27() };
        }

        /** Default implementation record for Tuple27. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27) implements Tuple27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {}
    }

    /**
     * Tuple with 28 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28() };
        }

        /** Default implementation record for Tuple28. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28) implements Tuple28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> {}
    }

    /**
     * Tuple with 29 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29() };
        }

        /** Default implementation record for Tuple29. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29) implements Tuple29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> {}
    }

    /**
     * Tuple with 30 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30() };
        }

        /** Default implementation record for Tuple30. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30) implements Tuple30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> {}
    }

    /**
     * Tuple with 31 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31() };
        }

        /** Default implementation record for Tuple31. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31) implements Tuple31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> {}
    }

    /**
     * Tuple with 32 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32() };
        }

        /** Default implementation record for Tuple32. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32) implements Tuple32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> {}
    }

    /**
     * Tuple with 33 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33() };
        }

        /** Default implementation record for Tuple33. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33) implements Tuple33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> {}
    }

    /**
     * Tuple with 34 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34() };
        }

        /** Default implementation record for Tuple34. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34) implements Tuple34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> {}
    }

    /**
     * Tuple with 35 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35() };
        }

        /** Default implementation record for Tuple35. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35) implements Tuple35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> {}
    }

    /**
     * Tuple with 36 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36() };
        }

        /** Default implementation record for Tuple36. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36) implements Tuple36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> {}
    }

    /**
     * Tuple with 37 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37() };
        }

        /** Default implementation record for Tuple37. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37) implements Tuple37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> {}
    }

    /**
     * Tuple with 38 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38() };
        }

        /** Default implementation record for Tuple38. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38) implements Tuple38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> {}
    }

    /**
     * Tuple with 39 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39() };
        }

        /** Default implementation record for Tuple39. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39) implements Tuple39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> {}
    }

    /**
     * Tuple with 40 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40() };
        }

        /** Default implementation record for Tuple40. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40) implements Tuple40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> {}
    }

    /**
     * Tuple with 41 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41() };
        }

        /** Default implementation record for Tuple41. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41) implements Tuple41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> {}
    }

    /**
     * Tuple with 42 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42() };
        }

        /** Default implementation record for Tuple42. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42) implements Tuple42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> {}
    }

    /**
     * Tuple with 43 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43() };
        }

        /** Default implementation record for Tuple43. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43) implements Tuple43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> {}
    }

    /**
     * Tuple with 44 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44() };
        }

        /** Default implementation record for Tuple44. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44) implements Tuple44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> {}
    }

    /**
     * Tuple with 45 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45() };
        }

        /** Default implementation record for Tuple45. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45) implements Tuple45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> {}
    }

    /**
     * Tuple with 46 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46() };
        }

        /** Default implementation record for Tuple46. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46) implements Tuple46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> {}
    }

    /**
     * Tuple with 47 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47() };
        }

        /** Default implementation record for Tuple47. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47) implements Tuple47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> {}
    }

    /**
     * Tuple with 48 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48() };
        }

        /** Default implementation record for Tuple48. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48) implements Tuple48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> {}
    }

    /**
     * Tuple with 49 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49() };
        }

        /** Default implementation record for Tuple49. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49) implements Tuple49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> {}
    }

    /**
     * Tuple with 50 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50() };
        }

        /** Default implementation record for Tuple50. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50) implements Tuple50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> {}
    }

    /**
     * Tuple with 51 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51() };
        }

        /** Default implementation record for Tuple51. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51) implements Tuple51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> {}
    }

    /**
     * Tuple with 52 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52() };
        }

        /** Default implementation record for Tuple52. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52) implements Tuple52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> {}
    }

    /**
     * Tuple with 53 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53() };
        }

        /** Default implementation record for Tuple53. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53) implements Tuple53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> {}
    }

    /**
     * Tuple with 54 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54() };
        }

        /** Default implementation record for Tuple54. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54) implements Tuple54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> {}
    }

    /**
     * Tuple with 55 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55() };
        }

        /** Default implementation record for Tuple55. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55) implements Tuple55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> {}
    }

    /**
     * Tuple with 56 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56() };
        }

        /** Default implementation record for Tuple56. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56) implements Tuple56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> {}
    }

    /**
     * Tuple with 57 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57() };
        }

        /** Default implementation record for Tuple57. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57) implements Tuple57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> {}
    }

    /**
     * Tuple with 58 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58() };
        }

        /** Default implementation record for Tuple58. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58) implements Tuple58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> {}
    }

    /**
     * Tuple with 59 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59() };
        }

        /** Default implementation record for Tuple59. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59) implements Tuple59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> {}
    }

    /**
     * Tuple with 60 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60() };
        }

        /** Default implementation record for Tuple60. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60) implements Tuple60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> {}
    }

    /**
     * Tuple with 61 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61() };
        }

        /** Default implementation record for Tuple61. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61) implements Tuple61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> {}
    }

    /**
     * Tuple with 62 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62() };
        }

        /** Default implementation record for Tuple62. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62) implements Tuple62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> {}
    }

    /**
     * Tuple with 63 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63() };
        }

        /** Default implementation record for Tuple63. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63) implements Tuple63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> {}
    }

    /**
     * Tuple with 64 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64() };
        }

        /** Default implementation record for Tuple64. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64) implements Tuple64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> {}
    }

    /**
     * Tuple with 65 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65() };
        }

        /** Default implementation record for Tuple65. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65) implements Tuple65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> {}
    }

    /**
     * Tuple with 66 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66() };
        }

        /** Default implementation record for Tuple66. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66) implements Tuple66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> {}
    }

    /**
     * Tuple with 67 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67() };
        }

        /** Default implementation record for Tuple67. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67) implements Tuple67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> {}
    }

    /**
     * Tuple with 68 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68() };
        }

        /** Default implementation record for Tuple68. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68) implements Tuple68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> {}
    }

    /**
     * Tuple with 69 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69() };
        }

        /** Default implementation record for Tuple69. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69) implements Tuple69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> {}
    }

    /**
     * Tuple with 70 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70() };
        }

        /** Default implementation record for Tuple70. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70) implements Tuple70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> {}
    }

    /**
     * Tuple with 71 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71() };
        }

        /** Default implementation record for Tuple71. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71) implements Tuple71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> {}
    }

    /**
     * Tuple with 72 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72() };
        }

        /** Default implementation record for Tuple72. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72) implements Tuple72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> {}
    }

    /**
     * Tuple with 73 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73() };
        }

        /** Default implementation record for Tuple73. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73) implements Tuple73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> {}
    }

    /**
     * Tuple with 74 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74() };
        }

        /** Default implementation record for Tuple74. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74) implements Tuple74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> {}
    }

    /**
     * Tuple with 75 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75() };
        }

        /** Default implementation record for Tuple75. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75) implements Tuple75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> {}
    }

    /**
     * Tuple with 76 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76() };
        }

        /** Default implementation record for Tuple76. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76) implements Tuple76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> {}
    }

    /**
     * Tuple with 77 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77() };
        }

        /** Default implementation record for Tuple77. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77) implements Tuple77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> {}
    }

    /**
     * Tuple with 78 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78() };
        }

        /** Default implementation record for Tuple78. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78) implements Tuple78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> {}
    }

    /**
     * Tuple with 79 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79() };
        }

        /** Default implementation record for Tuple79. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79) implements Tuple79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> {}
    }

    /**
     * Tuple with 80 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80() };
        }

        /** Default implementation record for Tuple80. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80) implements Tuple80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> {}
    }

    /**
     * Tuple with 81 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81() };
        }

        /** Default implementation record for Tuple81. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81) implements Tuple81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> {}
    }

    /**
     * Tuple with 82 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82() };
        }

        /** Default implementation record for Tuple82. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82) implements Tuple82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> {}
    }

    /**
     * Tuple with 83 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83() };
        }

        /** Default implementation record for Tuple83. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83) implements Tuple83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> {}
    }

    /**
     * Tuple with 84 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84() };
        }

        /** Default implementation record for Tuple84. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84) implements Tuple84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> {}
    }

    /**
     * Tuple with 85 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85() };
        }

        /** Default implementation record for Tuple85. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85) implements Tuple85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> {}
    }

    /**
     * Tuple with 86 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86() };
        }

        /** Default implementation record for Tuple86. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86) implements Tuple86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> {}
    }

    /**
     * Tuple with 87 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87() };
        }

        /** Default implementation record for Tuple87. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87) implements Tuple87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> {}
    }

    /**
     * Tuple with 88 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88() };
        }

        /** Default implementation record for Tuple88. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88) implements Tuple88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> {}
    }

    /**
     * Tuple with 89 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89() };
        }

        /** Default implementation record for Tuple89. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89) implements Tuple89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> {}
    }

    /**
     * Tuple with 90 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90() };
        }

        /** Default implementation record for Tuple90. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90) implements Tuple90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> {}
    }

    /**
     * Tuple with 91 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91() };
        }

        /** Default implementation record for Tuple91. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91) implements Tuple91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> {}
    }

    /**
     * Tuple with 92 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92() };
        }

        /** Default implementation record for Tuple92. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92) implements Tuple92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> {}
    }

    /**
     * Tuple with 93 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93() };
        }

        /** Default implementation record for Tuple93. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93) implements Tuple93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> {}
    }

    /**
     * Tuple with 94 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94() };
        }

        /** Default implementation record for Tuple94. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94) implements Tuple94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> {}
    }

    /**
     * Tuple with 95 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95() };
        }

        /** Default implementation record for Tuple95. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95) implements Tuple95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> {}
    }

    /**
     * Tuple with 96 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();
        T95 _96();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95(), _96() };
        }

        /** Default implementation record for Tuple96. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95, T95 _96) implements Tuple96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> {}
    }

    /**
     * Tuple with 97 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();
        T95 _96();
        T96 _97();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95(), _96(), _97() };
        }

        /** Default implementation record for Tuple97. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95, T95 _96, T96 _97) implements Tuple97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> {}
    }

    /**
     * Tuple with 98 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();
        T95 _96();
        T96 _97();
        T97 _98();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95(), _96(), _97(), _98() };
        }

        /** Default implementation record for Tuple98. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95, T95 _96, T96 _97, T97 _98) implements Tuple98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> {}
    }

    /**
     * Tuple with 99 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();
        T95 _96();
        T96 _97();
        T97 _98();
        T98 _99();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95(), _96(), _97(), _98(), _99() };
        }

        /** Default implementation record for Tuple99. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95, T95 _96, T96 _97, T97 _98, T98 _99) implements Tuple99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> {}
    }

    /**
     * Tuple with 100 elements.
     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
     */
    non-sealed interface Tuple100<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, T99> extends Tuple {
        T0 _1();
        T1 _2();
        T2 _3();
        T3 _4();
        T4 _5();
        T5 _6();
        T6 _7();
        T7 _8();
        T8 _9();
        T9 _10();
        T10 _11();
        T11 _12();
        T12 _13();
        T13 _14();
        T14 _15();
        T15 _16();
        T16 _17();
        T17 _18();
        T18 _19();
        T19 _20();
        T20 _21();
        T21 _22();
        T22 _23();
        T23 _24();
        T24 _25();
        T25 _26();
        T26 _27();
        T27 _28();
        T28 _29();
        T29 _30();
        T30 _31();
        T31 _32();
        T32 _33();
        T33 _34();
        T34 _35();
        T35 _36();
        T36 _37();
        T37 _38();
        T38 _39();
        T39 _40();
        T40 _41();
        T41 _42();
        T42 _43();
        T43 _44();
        T44 _45();
        T45 _46();
        T46 _47();
        T47 _48();
        T48 _49();
        T49 _50();
        T50 _51();
        T51 _52();
        T52 _53();
        T53 _54();
        T54 _55();
        T55 _56();
        T56 _57();
        T57 _58();
        T58 _59();
        T59 _60();
        T60 _61();
        T61 _62();
        T62 _63();
        T63 _64();
        T64 _65();
        T65 _66();
        T66 _67();
        T67 _68();
        T68 _69();
        T69 _70();
        T70 _71();
        T71 _72();
        T72 _73();
        T73 _74();
        T74 _75();
        T75 _76();
        T76 _77();
        T77 _78();
        T78 _79();
        T79 _80();
        T80 _81();
        T81 _82();
        T82 _83();
        T83 _84();
        T84 _85();
        T85 _86();
        T86 _87();
        T87 _88();
        T88 _89();
        T89 _90();
        T90 _91();
        T91 _92();
        T92 _93();
        T93 _94();
        T94 _95();
        T95 _96();
        T96 _97();
        T97 _98();
        T98 _99();
        T99 _100();

        @Override
        default Object[] asArray() {
            return new Object[] { _1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10(), _11(), _12(), _13(), _14(), _15(), _16(), _17(), _18(), _19(), _20(), _21(), _22(), _23(), _24(), _25(), _26(), _27(), _28(), _29(), _30(), _31(), _32(), _33(), _34(), _35(), _36(), _37(), _38(), _39(), _40(), _41(), _42(), _43(), _44(), _45(), _46(), _47(), _48(), _49(), _50(), _51(), _52(), _53(), _54(), _55(), _56(), _57(), _58(), _59(), _60(), _61(), _62(), _63(), _64(), _65(), _66(), _67(), _68(), _69(), _70(), _71(), _72(), _73(), _74(), _75(), _76(), _77(), _78(), _79(), _80(), _81(), _82(), _83(), _84(), _85(), _86(), _87(), _88(), _89(), _90(), _91(), _92(), _93(), _94(), _95(), _96(), _97(), _98(), _99(), _100() };
        }

        /** Default implementation record for Tuple100. */
        record Impl<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, T99>(T0 _1, T1 _2, T2 _3, T3 _4, T4 _5, T5 _6, T6 _7, T7 _8, T8 _9, T9 _10, T10 _11, T11 _12, T12 _13, T13 _14, T14 _15, T15 _16, T16 _17, T17 _18, T18 _19, T19 _20, T20 _21, T21 _22, T22 _23, T23 _24, T24 _25, T25 _26, T26 _27, T27 _28, T28 _29, T29 _30, T30 _31, T31 _32, T32 _33, T33 _34, T34 _35, T35 _36, T36 _37, T37 _38, T38 _39, T39 _40, T40 _41, T41 _42, T42 _43, T43 _44, T44 _45, T45 _46, T46 _47, T47 _48, T48 _49, T49 _50, T50 _51, T51 _52, T52 _53, T53 _54, T54 _55, T55 _56, T56 _57, T57 _58, T58 _59, T59 _60, T60 _61, T61 _62, T62 _63, T63 _64, T64 _65, T65 _66, T66 _67, T67 _68, T68 _69, T69 _70, T70 _71, T71 _72, T72 _73, T73 _74, T74 _75, T75 _76, T76 _77, T77 _78, T78 _79, T79 _80, T80 _81, T81 _82, T82 _83, T83 _84, T84 _85, T85 _86, T86 _87, T87 _88, T88 _89, T89 _90, T90 _91, T91 _92, T92 _93, T93 _94, T94 _95, T95 _96, T96 _97, T97 _98, T98 _99, T99 _100) implements Tuple100<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, T99> {}
    }

    // Factory methods for Tuple values
    /** Create a Tuple1 with the given values. */
    static <T0> Tuple1<T0> of(T0 v0) {
        return new Tuple1.Impl<>(v0);
    }

    /** Create a Tuple2 with the given values. */
    static <T0, T1> Tuple2<T0, T1> of(T0 v0, T1 v1) {
        return new Tuple2.Impl<>(v0, v1);
    }

    /** Create a Tuple3 with the given values. */
    static <T0, T1, T2> Tuple3<T0, T1, T2> of(T0 v0, T1 v1, T2 v2) {
        return new Tuple3.Impl<>(v0, v1, v2);
    }

    /** Create a Tuple4 with the given values. */
    static <T0, T1, T2, T3> Tuple4<T0, T1, T2, T3> of(T0 v0, T1 v1, T2 v2, T3 v3) {
        return new Tuple4.Impl<>(v0, v1, v2, v3);
    }

    /** Create a Tuple5 with the given values. */
    static <T0, T1, T2, T3, T4> Tuple5<T0, T1, T2, T3, T4> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4) {
        return new Tuple5.Impl<>(v0, v1, v2, v3, v4);
    }

    /** Create a Tuple6 with the given values. */
    static <T0, T1, T2, T3, T4, T5> Tuple6<T0, T1, T2, T3, T4, T5> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
        return new Tuple6.Impl<>(v0, v1, v2, v3, v4, v5);
    }

    /** Create a Tuple7 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6> Tuple7<T0, T1, T2, T3, T4, T5, T6> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6) {
        return new Tuple7.Impl<>(v0, v1, v2, v3, v4, v5, v6);
    }

    /** Create a Tuple8 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7> Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7) {
        return new Tuple8.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7);
    }

    /** Create a Tuple9 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8> Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8) {
        return new Tuple9.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8);
    }

    /** Create a Tuple10 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9) {
        return new Tuple10.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }

    /** Create a Tuple11 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Tuple11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10) {
        return new Tuple11.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
    }

    /** Create a Tuple12 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Tuple12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11) {
        return new Tuple12.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
    }

    /** Create a Tuple13 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Tuple13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12) {
        return new Tuple13.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
    }

    /** Create a Tuple14 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Tuple14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13) {
        return new Tuple14.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
    }

    /** Create a Tuple15 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Tuple15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14) {
        return new Tuple15.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
    }

    /** Create a Tuple16 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Tuple16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15) {
        return new Tuple16.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }

    /** Create a Tuple17 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Tuple17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16) {
        return new Tuple17.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
    }

    /** Create a Tuple18 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Tuple18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17) {
        return new Tuple18.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17);
    }

    /** Create a Tuple19 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Tuple19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18) {
        return new Tuple19.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18);
    }

    /** Create a Tuple20 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Tuple20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19) {
        return new Tuple20.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19);
    }

    /** Create a Tuple21 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Tuple21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20) {
        return new Tuple21.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20);
    }

    /** Create a Tuple22 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Tuple22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21) {
        return new Tuple22.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21);
    }

    /** Create a Tuple23 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Tuple23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22) {
        return new Tuple23.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22);
    }

    /** Create a Tuple24 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> Tuple24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23) {
        return new Tuple24.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23);
    }

    /** Create a Tuple25 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> Tuple25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24) {
        return new Tuple25.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24);
    }

    /** Create a Tuple26 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> Tuple26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25) {
        return new Tuple26.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25);
    }

    /** Create a Tuple27 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> Tuple27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26) {
        return new Tuple27.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26);
    }

    /** Create a Tuple28 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> Tuple28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27) {
        return new Tuple28.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27);
    }

    /** Create a Tuple29 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> Tuple29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28) {
        return new Tuple29.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28);
    }

    /** Create a Tuple30 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> Tuple30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29) {
        return new Tuple30.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29);
    }

    /** Create a Tuple31 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> Tuple31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30) {
        return new Tuple31.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30);
    }

    /** Create a Tuple32 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> Tuple32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31) {
        return new Tuple32.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31);
    }

    /** Create a Tuple33 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> Tuple33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32) {
        return new Tuple33.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32);
    }

    /** Create a Tuple34 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> Tuple34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33) {
        return new Tuple34.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33);
    }

    /** Create a Tuple35 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> Tuple35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34) {
        return new Tuple35.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34);
    }

    /** Create a Tuple36 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> Tuple36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35) {
        return new Tuple36.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35);
    }

    /** Create a Tuple37 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> Tuple37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36) {
        return new Tuple37.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36);
    }

    /** Create a Tuple38 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> Tuple38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37) {
        return new Tuple38.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37);
    }

    /** Create a Tuple39 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> Tuple39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38) {
        return new Tuple39.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38);
    }

    /** Create a Tuple40 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> Tuple40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39) {
        return new Tuple40.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39);
    }

    /** Create a Tuple41 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> Tuple41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40) {
        return new Tuple41.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40);
    }

    /** Create a Tuple42 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> Tuple42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41) {
        return new Tuple42.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41);
    }

    /** Create a Tuple43 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> Tuple43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42) {
        return new Tuple43.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42);
    }

    /** Create a Tuple44 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> Tuple44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43) {
        return new Tuple44.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43);
    }

    /** Create a Tuple45 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> Tuple45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44) {
        return new Tuple45.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44);
    }

    /** Create a Tuple46 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> Tuple46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45) {
        return new Tuple46.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45);
    }

    /** Create a Tuple47 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> Tuple47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46) {
        return new Tuple47.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46);
    }

    /** Create a Tuple48 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> Tuple48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47) {
        return new Tuple48.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47);
    }

    /** Create a Tuple49 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> Tuple49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48) {
        return new Tuple49.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48);
    }

    /** Create a Tuple50 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> Tuple50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49) {
        return new Tuple50.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49);
    }

    /** Create a Tuple51 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> Tuple51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50) {
        return new Tuple51.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50);
    }

    /** Create a Tuple52 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> Tuple52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51) {
        return new Tuple52.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51);
    }

    /** Create a Tuple53 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> Tuple53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52) {
        return new Tuple53.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52);
    }

    /** Create a Tuple54 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> Tuple54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53) {
        return new Tuple54.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53);
    }

    /** Create a Tuple55 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> Tuple55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54) {
        return new Tuple55.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54);
    }

    /** Create a Tuple56 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> Tuple56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55) {
        return new Tuple56.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55);
    }

    /** Create a Tuple57 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> Tuple57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56) {
        return new Tuple57.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56);
    }

    /** Create a Tuple58 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> Tuple58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57) {
        return new Tuple58.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57);
    }

    /** Create a Tuple59 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> Tuple59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58) {
        return new Tuple59.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58);
    }

    /** Create a Tuple60 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> Tuple60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59) {
        return new Tuple60.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59);
    }

    /** Create a Tuple61 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> Tuple61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60) {
        return new Tuple61.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60);
    }

    /** Create a Tuple62 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> Tuple62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61) {
        return new Tuple62.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61);
    }

    /** Create a Tuple63 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> Tuple63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62) {
        return new Tuple63.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62);
    }

    /** Create a Tuple64 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> Tuple64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63) {
        return new Tuple64.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63);
    }

    /** Create a Tuple65 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> Tuple65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64) {
        return new Tuple65.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64);
    }

    /** Create a Tuple66 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> Tuple66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65) {
        return new Tuple66.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65);
    }

    /** Create a Tuple67 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> Tuple67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66) {
        return new Tuple67.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66);
    }

    /** Create a Tuple68 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> Tuple68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67) {
        return new Tuple68.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67);
    }

    /** Create a Tuple69 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> Tuple69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68) {
        return new Tuple69.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68);
    }

    /** Create a Tuple70 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> Tuple70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69) {
        return new Tuple70.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69);
    }

    /** Create a Tuple71 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> Tuple71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70) {
        return new Tuple71.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70);
    }

    /** Create a Tuple72 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> Tuple72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71) {
        return new Tuple72.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71);
    }

    /** Create a Tuple73 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> Tuple73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72) {
        return new Tuple73.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72);
    }

    /** Create a Tuple74 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> Tuple74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73) {
        return new Tuple74.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73);
    }

    /** Create a Tuple75 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> Tuple75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74) {
        return new Tuple75.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74);
    }

    /** Create a Tuple76 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> Tuple76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75) {
        return new Tuple76.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75);
    }

    /** Create a Tuple77 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> Tuple77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76) {
        return new Tuple77.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76);
    }

    /** Create a Tuple78 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> Tuple78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77) {
        return new Tuple78.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77);
    }

    /** Create a Tuple79 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> Tuple79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78) {
        return new Tuple79.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78);
    }

    /** Create a Tuple80 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> Tuple80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79) {
        return new Tuple80.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79);
    }

    /** Create a Tuple81 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> Tuple81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80) {
        return new Tuple81.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80);
    }

    /** Create a Tuple82 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> Tuple82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81) {
        return new Tuple82.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81);
    }

    /** Create a Tuple83 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> Tuple83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82) {
        return new Tuple83.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82);
    }

    /** Create a Tuple84 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> Tuple84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83) {
        return new Tuple84.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83);
    }

    /** Create a Tuple85 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> Tuple85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84) {
        return new Tuple85.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84);
    }

    /** Create a Tuple86 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> Tuple86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85) {
        return new Tuple86.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85);
    }

    /** Create a Tuple87 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> Tuple87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86) {
        return new Tuple87.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86);
    }

    /** Create a Tuple88 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> Tuple88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87) {
        return new Tuple88.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87);
    }

    /** Create a Tuple89 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> Tuple89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88) {
        return new Tuple89.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88);
    }

    /** Create a Tuple90 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> Tuple90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89) {
        return new Tuple90.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89);
    }

    /** Create a Tuple91 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> Tuple91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90) {
        return new Tuple91.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90);
    }

    /** Create a Tuple92 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> Tuple92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91) {
        return new Tuple92.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91);
    }

    /** Create a Tuple93 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> Tuple93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92) {
        return new Tuple93.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92);
    }

    /** Create a Tuple94 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> Tuple94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93) {
        return new Tuple94.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93);
    }

    /** Create a Tuple95 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> Tuple95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94) {
        return new Tuple95.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94);
    }

    /** Create a Tuple96 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> Tuple96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94, T95 v95) {
        return new Tuple96.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94, v95);
    }

    /** Create a Tuple97 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> Tuple97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94, T95 v95, T96 v96) {
        return new Tuple97.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94, v95, v96);
    }

    /** Create a Tuple98 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> Tuple98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94, T95 v95, T96 v96, T97 v97) {
        return new Tuple98.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94, v95, v96, v97);
    }

    /** Create a Tuple99 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> Tuple99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94, T95 v95, T96 v96, T97 v97, T98 v98) {
        return new Tuple99.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94, v95, v96, v97, v98);
    }

    /** Create a Tuple100 with the given values. */
    static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, T99> Tuple100<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, T99> of(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16, T17 v17, T18 v18, T19 v19, T20 v20, T21 v21, T22 v22, T23 v23, T24 v24, T25 v25, T26 v26, T27 v27, T28 v28, T29 v29, T30 v30, T31 v31, T32 v32, T33 v33, T34 v34, T35 v35, T36 v36, T37 v37, T38 v38, T39 v39, T40 v40, T41 v41, T42 v42, T43 v43, T44 v44, T45 v45, T46 v46, T47 v47, T48 v48, T49 v49, T50 v50, T51 v51, T52 v52, T53 v53, T54 v54, T55 v55, T56 v56, T57 v57, T58 v58, T59 v59, T60 v60, T61 v61, T62 v62, T63 v63, T64 v64, T65 v65, T66 v66, T67 v67, T68 v68, T69 v69, T70 v70, T71 v71, T72 v72, T73 v73, T74 v74, T75 v75, T76 v76, T77 v77, T78 v78, T79 v79, T80 v80, T81 v81, T82 v82, T83 v83, T84 v84, T85 v85, T86 v86, T87 v87, T88 v88, T89 v89, T90 v90, T91 v91, T92 v92, T93 v93, T94 v94, T95 v95, T96 v96, T97 v97, T98 v98, T99 v99) {
        return new Tuple100.Impl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60, v61, v62, v63, v64, v65, v66, v67, v68, v69, v70, v71, v72, v73, v74, v75, v76, v77, v78, v79, v80, v81, v82, v83, v84, v85, v86, v87, v88, v89, v90, v91, v92, v93, v94, v95, v96, v97, v98, v99);
    }

    /**
     * Create a Tuple of the appropriate arity from an array of values.
     * @param values array of values (length 1-100)
     * @return a Tuple of the appropriate arity
     * @throws IllegalArgumentException if values.length is 0 or greater than 100
     */
    @SuppressWarnings("unchecked")
    static Tuple createTuple(Object[] values) {
        return switch (values.length) {
            case 1 -> Tuple.of(values[0]);
            case 2 -> Tuple.of(values[0], values[1]);
            case 3 -> Tuple.of(values[0], values[1], values[2]);
            case 4 -> Tuple.of(values[0], values[1], values[2], values[3]);
            case 5 -> Tuple.of(values[0], values[1], values[2], values[3], values[4]);
            case 6 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);
            case 7 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
            case 8 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
            case 9 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
            case 10 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9]);
            case 11 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10]);
            case 12 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11]);
            case 13 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12]);
            case 14 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13]);
            case 15 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14]);
            case 16 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
            case 17 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16]);
            case 18 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17]);
            case 19 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18]);
            case 20 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19]);
            case 21 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20]);
            case 22 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21]);
            case 23 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22]);
            case 24 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23]);
            case 25 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24]);
            case 26 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25]);
            case 27 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26]);
            case 28 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27]);
            case 29 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28]);
            case 30 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29]);
            case 31 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30]);
            case 32 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31]);
            case 33 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32]);
            case 34 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33]);
            case 35 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34]);
            case 36 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35]);
            case 37 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36]);
            case 38 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37]);
            case 39 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38]);
            case 40 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39]);
            case 41 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40]);
            case 42 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41]);
            case 43 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42]);
            case 44 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43]);
            case 45 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44]);
            case 46 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45]);
            case 47 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46]);
            case 48 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47]);
            case 49 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48]);
            case 50 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49]);
            case 51 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50]);
            case 52 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51]);
            case 53 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52]);
            case 54 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53]);
            case 55 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54]);
            case 56 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55]);
            case 57 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56]);
            case 58 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57]);
            case 59 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58]);
            case 60 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59]);
            case 61 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60]);
            case 62 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61]);
            case 63 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62]);
            case 64 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63]);
            case 65 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64]);
            case 66 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65]);
            case 67 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66]);
            case 68 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67]);
            case 69 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68]);
            case 70 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69]);
            case 71 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70]);
            case 72 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71]);
            case 73 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72]);
            case 74 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73]);
            case 75 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74]);
            case 76 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75]);
            case 77 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76]);
            case 78 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77]);
            case 79 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78]);
            case 80 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79]);
            case 81 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80]);
            case 82 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81]);
            case 83 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82]);
            case 84 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83]);
            case 85 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84]);
            case 86 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85]);
            case 87 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86]);
            case 88 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87]);
            case 89 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88]);
            case 90 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89]);
            case 91 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90]);
            case 92 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91]);
            case 93 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92]);
            case 94 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93]);
            case 95 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94]);
            case 96 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94], values[95]);
            case 97 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94], values[95], values[96]);
            case 98 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94], values[95], values[96], values[97]);
            case 99 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94], values[95], values[96], values[97], values[98]);
            case 100 -> Tuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19], values[20], values[21], values[22], values[23], values[24], values[25], values[26], values[27], values[28], values[29], values[30], values[31], values[32], values[33], values[34], values[35], values[36], values[37], values[38], values[39], values[40], values[41], values[42], values[43], values[44], values[45], values[46], values[47], values[48], values[49], values[50], values[51], values[52], values[53], values[54], values[55], values[56], values[57], values[58], values[59], values[60], values[61], values[62], values[63], values[64], values[65], values[66], values[67], values[68], values[69], values[70], values[71], values[72], values[73], values[74], values[75], values[76], values[77], values[78], values[79], values[80], values[81], values[82], values[83], values[84], values[85], values[86], values[87], values[88], values[89], values[90], values[91], values[92], values[93], values[94], values[95], values[96], values[97], values[98], values[99]);
            default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
        };
    }
}
