package dev.typr.foundations;

/**
 * Type-safe stored function definitions with fully typed inputs.
 * <p>
 * The builder tracks input types (via {@code .in()}). The resulting interface has a
 * {@code call()} method with typed parameters instead of varargs.
 * <p>
 * Usage:
 * <pre>{@code
 * // Function with typed inputs — compile-time checking!
 * DbFunction.Def2<BigDecimal, String, BigDecimal> calcTax = DbFunction.define("calculate_tax", PgTypes.numeric)
 *     .in(PgTypes.numeric)
 *     .in(PgTypes.text)
 *     .build();
 * BigDecimal tax = calcTax.call(amount, "US").transact(tx);  // Types enforced!
 * // calcTax.call("wrong", 42);  // COMPILE ERROR
 *
 * // Zero-argument function
 * DbFunction.Def0<java.time.LocalDateTime> now = DbFunction.define("now", PgTypes.timestamp)
 *     .build();
 * LocalDateTime serverTime = now.call().transact(tx);
 * }</pre>
 *
 * @see DbProcedure for stored procedures (with OUT/INOUT parameters)
 */
public final class DbFunction {
    private DbFunction() {}

    /** Start defining a stored function (single return value, uses SELECT). */
    public static <R> Builder_0<R> define(String name, DbType<R> returnType) {
        return new Builder_0<>(name, new java.util.ArrayList<>(), returnType);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Function definition interfaces (11 total: 0-10 inputs)
    // ─────────────────────────────────────────────────────────────────────────────

    /** Function definition with 0 input(s). */
    @FunctionalInterface
    public interface Def0<R> {
        Operation<R> call();
    }

    /** Function definition with 1 input(s). */
    @FunctionalInterface
    public interface Def1<I0, R> {
        Operation<R> call(I0 i0);
    }

    /** Function definition with 2 input(s). */
    @FunctionalInterface
    public interface Def2<I0, I1, R> {
        Operation<R> call(I0 i0, I1 i1);
    }

    /** Function definition with 3 input(s). */
    @FunctionalInterface
    public interface Def3<I0, I1, I2, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2);
    }

    /** Function definition with 4 input(s). */
    @FunctionalInterface
    public interface Def4<I0, I1, I2, I3, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Function definition with 5 input(s). */
    @FunctionalInterface
    public interface Def5<I0, I1, I2, I3, I4, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Function definition with 6 input(s). */
    @FunctionalInterface
    public interface Def6<I0, I1, I2, I3, I4, I5, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Function definition with 7 input(s). */
    @FunctionalInterface
    public interface Def7<I0, I1, I2, I3, I4, I5, I6, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Function definition with 8 input(s). */
    @FunctionalInterface
    public interface Def8<I0, I1, I2, I3, I4, I5, I6, I7, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Function definition with 9 input(s). */
    @FunctionalInterface
    public interface Def9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Function definition with 10 input(s). */
    @FunctionalInterface
    public interface Def10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> {
        Operation<R> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Function builders (11 total: 0-10 inputs)
    // ─────────────────────────────────────────────────────────────────────────────

    public static final class Builder_0<R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_0(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I0> Builder_1<I0, R> in(DbType<I0> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_1<>(name, inParams, returnType);
        }

        public Def0<R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return () -> delegate.call();
        }
    }

    public static final class Builder_1<I0, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_1(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I1> Builder_2<I0, I1, R> in(DbType<I1> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_2<>(name, inParams, returnType);
        }

        public Def1<I0, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_2<I0, I1, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_2(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I2> Builder_3<I0, I1, I2, R> in(DbType<I2> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_3<>(name, inParams, returnType);
        }

        public Def2<I0, I1, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_3<I0, I1, I2, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_3(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I3> Builder_4<I0, I1, I2, I3, R> in(DbType<I3> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_4<>(name, inParams, returnType);
        }

        public Def3<I0, I1, I2, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_4<I0, I1, I2, I3, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_4(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I4> Builder_5<I0, I1, I2, I3, I4, R> in(DbType<I4> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_5<>(name, inParams, returnType);
        }

        public Def4<I0, I1, I2, I3, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_5<I0, I1, I2, I3, I4, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_5(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I5> Builder_6<I0, I1, I2, I3, I4, I5, R> in(DbType<I5> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_6<>(name, inParams, returnType);
        }

        public Def5<I0, I1, I2, I3, I4, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_6<I0, I1, I2, I3, I4, I5, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_6(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I6> Builder_7<I0, I1, I2, I3, I4, I5, I6, R> in(DbType<I6> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_7<>(name, inParams, returnType);
        }

        public Def6<I0, I1, I2, I3, I4, I5, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_7<I0, I1, I2, I3, I4, I5, I6, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_7(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I7> Builder_8<I0, I1, I2, I3, I4, I5, I6, I7, R> in(DbType<I7> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_8<>(name, inParams, returnType);
        }

        public Def7<I0, I1, I2, I3, I4, I5, I6, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_8<I0, I1, I2, I3, I4, I5, I6, I7, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_8(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I8> Builder_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> in(DbType<I8> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_9<>(name, inParams, returnType);
        }

        public Def8<I0, I1, I2, I3, I4, I5, I6, I7, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_9(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }
        public <I9> Builder_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> in(DbType<I9> type) {
            inParams.add(ParamDef.in(type));
            return new Builder_10<>(name, inParams, returnType);
        }

        public Def9<I0, I1, I2, I3, I4, I5, I6, I7, I8, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> {
        private final String name;
        private final java.util.List<ParamDef> inParams;
        private final DbType<R> returnType;

        Builder_10(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
            this.name = name;
            this.inParams = inParams;
            this.returnType = returnType;
        }

        public Def10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, R> build() {
            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }
}
