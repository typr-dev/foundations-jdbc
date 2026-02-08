package dev.typr.foundations;

/**
 * Type-safe stored procedure definitions with fully typed inputs and outputs.
 * <p>
 * The builder tracks both input types (via {@code .in()}) and output types (via {@code .out()}/{@code .inout()}).
 * The resulting interface has a {@code call()} method with typed parameters instead of varargs.
 * <p>
 * Usage:
 * <pre>{@code
 * // Procedure with typed inputs — compile-time checking!
 * DbProcedure.Def1_2<Integer, String, String> getUser = DbProcedure.define("get_user_by_id")
 *     .in(PgTypes.int4)
 *     .out(PgTypes.text)
 *     .out(PgTypes.text)
 *     .build();
 * Tuple.Tuple2<String, String> result = getUser.call(42).transact(tx);  // Integer enforced!
 * // getUser.call("wrong");  // COMPILE ERROR - String not Integer
 *
 * // Void procedure (no outputs)
 * DbProcedure.Def1_0<String> auditLog = DbProcedure.define("audit_log")
 *     .in(PgTypes.text)
 *     .build();
 *
 * // INOUT — value goes in and comes back modified
 * DbProcedure.Def2_1<String, BigDecimal, BigDecimal> applyDiscount = DbProcedure.define("apply_discount")
 *     .in(PgTypes.text)
 *     .inout(PgTypes.numeric)
 *     .build();
 * BigDecimal finalPrice = applyDiscount.call("SAVE20", price).transact(tx);
 * }</pre>
 *
 * @see DbFunction for stored functions (single return value via SELECT)
 */
public final class DbProcedure {
    private DbProcedure() {}

    /** Start defining a stored procedure. */
    public static Builder_0_0 define(String name) {
        return new Builder_0_0(name, new java.util.ArrayList<>());
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Procedure definition interfaces (121 total: 11×11 matrix of input×output arities)
    // ─────────────────────────────────────────────────────────────────────────────

    /** Procedure definition with 0 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def0_0 {
        Operation<Void> call();
    }

    /** Procedure definition with 0 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def0_1<O0> {
        Operation<O0> call();
    }

    /** Procedure definition with 0 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def0_2<O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call();
    }

    /** Procedure definition with 0 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def0_3<O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call();
    }

    /** Procedure definition with 0 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def0_4<O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call();
    }

    /** Procedure definition with 0 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def0_5<O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call();
    }

    /** Procedure definition with 0 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def0_6<O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call();
    }

    /** Procedure definition with 0 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def0_7<O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call();
    }

    /** Procedure definition with 0 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def0_8<O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call();
    }

    /** Procedure definition with 0 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call();
    }

    /** Procedure definition with 0 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call();
    }

    /** Procedure definition with 1 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def1_0<I0> {
        Operation<Void> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def1_1<I0, O0> {
        Operation<O0> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def1_2<I0, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def1_3<I0, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def1_4<I0, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def1_5<I0, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def1_6<I0, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def1_7<I0, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0);
    }

    /** Procedure definition with 1 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0);
    }

    /** Procedure definition with 2 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def2_0<I0, I1> {
        Operation<Void> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def2_1<I0, I1, O0> {
        Operation<O0> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def2_2<I0, I1, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def2_3<I0, I1, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def2_4<I0, I1, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def2_5<I0, I1, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def2_6<I0, I1, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 2 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1);
    }

    /** Procedure definition with 3 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def3_0<I0, I1, I2> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def3_1<I0, I1, I2, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def3_2<I0, I1, I2, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def3_3<I0, I1, I2, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def3_4<I0, I1, I2, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def3_5<I0, I1, I2, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 3 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2);
    }

    /** Procedure definition with 4 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def4_0<I0, I1, I2, I3> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def4_1<I0, I1, I2, I3, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def4_2<I0, I1, I2, I3, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def4_3<I0, I1, I2, I3, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def4_4<I0, I1, I2, I3, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 4 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3);
    }

    /** Procedure definition with 5 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def5_0<I0, I1, I2, I3, I4> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def5_1<I0, I1, I2, I3, I4, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def5_2<I0, I1, I2, I3, I4, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def5_3<I0, I1, I2, I3, I4, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 5 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4);
    }

    /** Procedure definition with 6 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def6_0<I0, I1, I2, I3, I4, I5> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def6_1<I0, I1, I2, I3, I4, I5, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def6_2<I0, I1, I2, I3, I4, I5, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 6 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5);
    }

    /** Procedure definition with 7 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def7_0<I0, I1, I2, I3, I4, I5, I6> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def7_1<I0, I1, I2, I3, I4, I5, I6, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 7 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6);
    }

    /** Procedure definition with 8 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def8_0<I0, I1, I2, I3, I4, I5, I6, I7> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 8 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7);
    }

    /** Procedure definition with 9 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 9 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8);
    }

    /** Procedure definition with 10 input(s) and 0 output(s). */
    @FunctionalInterface
    public interface Def10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> {
        Operation<Void> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 1 output(s). */
    @FunctionalInterface
    public interface Def10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> {
        Operation<O0> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 2 output(s). */
    @FunctionalInterface
    public interface Def10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> {
        Operation<Tuple.Tuple2<O0, O1>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 3 output(s). */
    @FunctionalInterface
    public interface Def10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> {
        Operation<Tuple.Tuple3<O0, O1, O2>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 4 output(s). */
    @FunctionalInterface
    public interface Def10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> {
        Operation<Tuple.Tuple4<O0, O1, O2, O3>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 5 output(s). */
    @FunctionalInterface
    public interface Def10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> {
        Operation<Tuple.Tuple5<O0, O1, O2, O3, O4>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 6 output(s). */
    @FunctionalInterface
    public interface Def10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> {
        Operation<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 7 output(s). */
    @FunctionalInterface
    public interface Def10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> {
        Operation<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 8 output(s). */
    @FunctionalInterface
    public interface Def10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> {
        Operation<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 9 output(s). */
    @FunctionalInterface
    public interface Def10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        Operation<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    /** Procedure definition with 10 input(s) and 10 output(s). */
    @FunctionalInterface
    public interface Def10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        Operation<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> call(I0 i0, I1 i1, I2 i2, I3 i3, I4 i4, I5 i5, I6 i6, I7 i7, I8 i8, I9 i9);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Procedure builders (121 total: 11×11 matrix)
    // ─────────────────────────────────────────────────────────────────────────────

    public static final class Builder_0_0 {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_0<I0> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_0<>(name, params);
        }
        public <O0> Builder_0_1<O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_1<>(name, params);
        }
        public <X> Builder_1_1<X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_0 build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_1<O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_1<I0, O0> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_1<>(name, params);
        }
        public <O1> Builder_0_2<O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_2<>(name, params);
        }
        public <X> Builder_1_2<X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_1<O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_2<O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_2<I0, O0, O1> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_2<>(name, params);
        }
        public <O2> Builder_0_3<O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_3<>(name, params);
        }
        public <X> Builder_1_3<X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_2<O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_3<O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_3<I0, O0, O1, O2> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_3<>(name, params);
        }
        public <O3> Builder_0_4<O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_4<>(name, params);
        }
        public <X> Builder_1_4<X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_3<O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_4<O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_4<I0, O0, O1, O2, O3> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_4<>(name, params);
        }
        public <O4> Builder_0_5<O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_5<>(name, params);
        }
        public <X> Builder_1_5<X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_4<O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_5<O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_5<I0, O0, O1, O2, O3, O4> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_5<>(name, params);
        }
        public <O5> Builder_0_6<O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_6<>(name, params);
        }
        public <X> Builder_1_6<X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_5<O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_6<O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_6<I0, O0, O1, O2, O3, O4, O5> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_6<>(name, params);
        }
        public <O6> Builder_0_7<O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_7<>(name, params);
        }
        public <X> Builder_1_7<X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_6<O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_7<O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_7<>(name, params);
        }
        public <O7> Builder_0_8<O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_8<>(name, params);
        }
        public <X> Builder_1_8<X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_7<O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_8<O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_8<>(name, params);
        }
        public <O8> Builder_0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_9<>(name, params);
        }
        public <X> Builder_1_9<X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_8<O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_9<>(name, params);
        }
        public <O9> Builder_0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_0_10<>(name, params);
        }
        public <X> Builder_1_10<X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_1_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_9<O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_0_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I0> Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I0> type) {
            params.add(ParamDef.in(type));
            return new Builder_1_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def0_10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return () -> delegate.call();
        }
    }

    public static final class Builder_1_0<I0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_0<I0, I1> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_0<>(name, params);
        }
        public <O0> Builder_1_1<I0, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_1<>(name, params);
        }
        public <X> Builder_2_1<I0, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_0<I0> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_1<I0, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_1<I0, I1, O0> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_1<>(name, params);
        }
        public <O1> Builder_1_2<I0, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_2<>(name, params);
        }
        public <X> Builder_2_2<I0, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_1<I0, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_2<I0, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_2<I0, I1, O0, O1> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_2<>(name, params);
        }
        public <O2> Builder_1_3<I0, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_3<>(name, params);
        }
        public <X> Builder_2_3<I0, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_2<I0, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_3<I0, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_3<I0, I1, O0, O1, O2> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_3<>(name, params);
        }
        public <O3> Builder_1_4<I0, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_4<>(name, params);
        }
        public <X> Builder_2_4<I0, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_3<I0, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_4<I0, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_4<I0, I1, O0, O1, O2, O3> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_4<>(name, params);
        }
        public <O4> Builder_1_5<I0, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_5<>(name, params);
        }
        public <X> Builder_2_5<I0, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_4<I0, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_5<I0, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_5<I0, I1, O0, O1, O2, O3, O4> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_5<>(name, params);
        }
        public <O5> Builder_1_6<I0, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_6<>(name, params);
        }
        public <X> Builder_2_6<I0, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_5<I0, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_6<I0, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_6<>(name, params);
        }
        public <O6> Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_7<>(name, params);
        }
        public <X> Builder_2_7<I0, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_6<I0, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_7<I0, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_7<>(name, params);
        }
        public <O7> Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_8<>(name, params);
        }
        public <X> Builder_2_8<I0, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_7<I0, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_8<>(name, params);
        }
        public <O8> Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_9<>(name, params);
        }
        public <X> Builder_2_9<I0, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_8<I0, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_9<>(name, params);
        }
        public <O9> Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_1_10<>(name, params);
        }
        public <X> Builder_2_10<I0, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_2_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_9<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_1_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I1> Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I1> type) {
            params.add(ParamDef.in(type));
            return new Builder_2_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def1_10<I0, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0) -> delegate.call(i0);
        }
    }

    public static final class Builder_2_0<I0, I1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_0<I0, I1, I2> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_0<>(name, params);
        }
        public <O0> Builder_2_1<I0, I1, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_1<>(name, params);
        }
        public <X> Builder_3_1<I0, I1, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_0<I0, I1> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_1<I0, I1, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_1<I0, I1, I2, O0> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_1<>(name, params);
        }
        public <O1> Builder_2_2<I0, I1, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_2<>(name, params);
        }
        public <X> Builder_3_2<I0, I1, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_1<I0, I1, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_2<I0, I1, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_2<I0, I1, I2, O0, O1> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_2<>(name, params);
        }
        public <O2> Builder_2_3<I0, I1, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_3<>(name, params);
        }
        public <X> Builder_3_3<I0, I1, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_2<I0, I1, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_3<I0, I1, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_3<I0, I1, I2, O0, O1, O2> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_3<>(name, params);
        }
        public <O3> Builder_2_4<I0, I1, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_4<>(name, params);
        }
        public <X> Builder_3_4<I0, I1, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_3<I0, I1, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_4<I0, I1, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_4<I0, I1, I2, O0, O1, O2, O3> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_4<>(name, params);
        }
        public <O4> Builder_2_5<I0, I1, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_5<>(name, params);
        }
        public <X> Builder_3_5<I0, I1, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_4<I0, I1, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_5<I0, I1, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_5<>(name, params);
        }
        public <O5> Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_6<>(name, params);
        }
        public <X> Builder_3_6<I0, I1, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_5<I0, I1, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_6<I0, I1, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_6<>(name, params);
        }
        public <O6> Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_7<>(name, params);
        }
        public <X> Builder_3_7<I0, I1, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_6<I0, I1, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_7<>(name, params);
        }
        public <O7> Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_8<>(name, params);
        }
        public <X> Builder_3_8<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_7<I0, I1, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_8<>(name, params);
        }
        public <O8> Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_9<>(name, params);
        }
        public <X> Builder_3_9<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_8<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_9<>(name, params);
        }
        public <O9> Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_2_10<>(name, params);
        }
        public <X> Builder_3_10<I0, I1, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_3_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_9<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_2_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I2> Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I2> type) {
            params.add(ParamDef.in(type));
            return new Builder_3_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def2_10<I0, I1, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1) -> delegate.call(i0, i1);
        }
    }

    public static final class Builder_3_0<I0, I1, I2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_0<I0, I1, I2, I3> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_0<>(name, params);
        }
        public <O0> Builder_3_1<I0, I1, I2, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_1<>(name, params);
        }
        public <X> Builder_4_1<I0, I1, I2, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_0<I0, I1, I2> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_1<I0, I1, I2, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_1<I0, I1, I2, I3, O0> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_1<>(name, params);
        }
        public <O1> Builder_3_2<I0, I1, I2, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_2<>(name, params);
        }
        public <X> Builder_4_2<I0, I1, I2, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_1<I0, I1, I2, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_2<I0, I1, I2, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_2<I0, I1, I2, I3, O0, O1> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_2<>(name, params);
        }
        public <O2> Builder_3_3<I0, I1, I2, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_3<>(name, params);
        }
        public <X> Builder_4_3<I0, I1, I2, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_2<I0, I1, I2, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_3<I0, I1, I2, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_3<I0, I1, I2, I3, O0, O1, O2> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_3<>(name, params);
        }
        public <O3> Builder_3_4<I0, I1, I2, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_4<>(name, params);
        }
        public <X> Builder_4_4<I0, I1, I2, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_3<I0, I1, I2, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_4<I0, I1, I2, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_4<>(name, params);
        }
        public <O4> Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_5<>(name, params);
        }
        public <X> Builder_4_5<I0, I1, I2, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_4<I0, I1, I2, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_5<I0, I1, I2, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_5<>(name, params);
        }
        public <O5> Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_6<>(name, params);
        }
        public <X> Builder_4_6<I0, I1, I2, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_5<I0, I1, I2, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_6<>(name, params);
        }
        public <O6> Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_7<>(name, params);
        }
        public <X> Builder_4_7<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_6<I0, I1, I2, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_7<>(name, params);
        }
        public <O7> Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_8<>(name, params);
        }
        public <X> Builder_4_8<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_7<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_8<>(name, params);
        }
        public <O8> Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_9<>(name, params);
        }
        public <X> Builder_4_9<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_8<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_9<>(name, params);
        }
        public <O9> Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_3_10<>(name, params);
        }
        public <X> Builder_4_10<I0, I1, I2, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_4_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_9<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_3_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I3> Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I3> type) {
            params.add(ParamDef.in(type));
            return new Builder_4_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def3_10<I0, I1, I2, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2) -> delegate.call(i0, i1, i2);
        }
    }

    public static final class Builder_4_0<I0, I1, I2, I3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_0<I0, I1, I2, I3, I4> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_0<>(name, params);
        }
        public <O0> Builder_4_1<I0, I1, I2, I3, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_1<>(name, params);
        }
        public <X> Builder_5_1<I0, I1, I2, I3, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_0<I0, I1, I2, I3> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_1<I0, I1, I2, I3, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_1<I0, I1, I2, I3, I4, O0> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_1<>(name, params);
        }
        public <O1> Builder_4_2<I0, I1, I2, I3, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_2<>(name, params);
        }
        public <X> Builder_5_2<I0, I1, I2, I3, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_1<I0, I1, I2, I3, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_2<I0, I1, I2, I3, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_2<I0, I1, I2, I3, I4, O0, O1> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_2<>(name, params);
        }
        public <O2> Builder_4_3<I0, I1, I2, I3, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_3<>(name, params);
        }
        public <X> Builder_5_3<I0, I1, I2, I3, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_2<I0, I1, I2, I3, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_3<I0, I1, I2, I3, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_3<>(name, params);
        }
        public <O3> Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_4<>(name, params);
        }
        public <X> Builder_5_4<I0, I1, I2, I3, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_3<I0, I1, I2, I3, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_4<I0, I1, I2, I3, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_4<>(name, params);
        }
        public <O4> Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_5<>(name, params);
        }
        public <X> Builder_5_5<I0, I1, I2, I3, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_4<I0, I1, I2, I3, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_5<>(name, params);
        }
        public <O5> Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_6<>(name, params);
        }
        public <X> Builder_5_6<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_5<I0, I1, I2, I3, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_6<>(name, params);
        }
        public <O6> Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_7<>(name, params);
        }
        public <X> Builder_5_7<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_6<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_7<>(name, params);
        }
        public <O7> Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_8<>(name, params);
        }
        public <X> Builder_5_8<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_7<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_8<>(name, params);
        }
        public <O8> Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_9<>(name, params);
        }
        public <X> Builder_5_9<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_8<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_9<>(name, params);
        }
        public <O9> Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_4_10<>(name, params);
        }
        public <X> Builder_5_10<I0, I1, I2, I3, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_5_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_9<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_4_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I4> Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I4> type) {
            params.add(ParamDef.in(type));
            return new Builder_5_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def4_10<I0, I1, I2, I3, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3) -> delegate.call(i0, i1, i2, i3);
        }
    }

    public static final class Builder_5_0<I0, I1, I2, I3, I4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_0<I0, I1, I2, I3, I4, I5> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_0<>(name, params);
        }
        public <O0> Builder_5_1<I0, I1, I2, I3, I4, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_1<>(name, params);
        }
        public <X> Builder_6_1<I0, I1, I2, I3, I4, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_0<I0, I1, I2, I3, I4> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_1<I0, I1, I2, I3, I4, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_1<I0, I1, I2, I3, I4, I5, O0> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_1<>(name, params);
        }
        public <O1> Builder_5_2<I0, I1, I2, I3, I4, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_2<>(name, params);
        }
        public <X> Builder_6_2<I0, I1, I2, I3, I4, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_1<I0, I1, I2, I3, I4, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_2<I0, I1, I2, I3, I4, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_2<>(name, params);
        }
        public <O2> Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_3<>(name, params);
        }
        public <X> Builder_6_3<I0, I1, I2, I3, I4, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_2<I0, I1, I2, I3, I4, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_3<I0, I1, I2, I3, I4, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_3<>(name, params);
        }
        public <O3> Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_4<>(name, params);
        }
        public <X> Builder_6_4<I0, I1, I2, I3, I4, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_3<I0, I1, I2, I3, I4, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_4<>(name, params);
        }
        public <O4> Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_5<>(name, params);
        }
        public <X> Builder_6_5<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_4<I0, I1, I2, I3, I4, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_5<>(name, params);
        }
        public <O5> Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_6<>(name, params);
        }
        public <X> Builder_6_6<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_5<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_6<>(name, params);
        }
        public <O6> Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_7<>(name, params);
        }
        public <X> Builder_6_7<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_6<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_7<>(name, params);
        }
        public <O7> Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_8<>(name, params);
        }
        public <X> Builder_6_8<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_7<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_8<>(name, params);
        }
        public <O8> Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_9<>(name, params);
        }
        public <X> Builder_6_9<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_8<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_9<>(name, params);
        }
        public <O9> Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_5_10<>(name, params);
        }
        public <X> Builder_6_10<I0, I1, I2, I3, I4, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_6_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_9<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_5_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I5> Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I5> type) {
            params.add(ParamDef.in(type));
            return new Builder_6_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def5_10<I0, I1, I2, I3, I4, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4) -> delegate.call(i0, i1, i2, i3, i4);
        }
    }

    public static final class Builder_6_0<I0, I1, I2, I3, I4, I5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_0<I0, I1, I2, I3, I4, I5, I6> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_0<>(name, params);
        }
        public <O0> Builder_6_1<I0, I1, I2, I3, I4, I5, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_1<>(name, params);
        }
        public <X> Builder_7_1<I0, I1, I2, I3, I4, I5, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_0<I0, I1, I2, I3, I4, I5> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_1<I0, I1, I2, I3, I4, I5, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_1<>(name, params);
        }
        public <O1> Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_2<>(name, params);
        }
        public <X> Builder_7_2<I0, I1, I2, I3, I4, I5, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_1<I0, I1, I2, I3, I4, I5, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_2<I0, I1, I2, I3, I4, I5, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_2<>(name, params);
        }
        public <O2> Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_3<>(name, params);
        }
        public <X> Builder_7_3<I0, I1, I2, I3, I4, I5, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_2<I0, I1, I2, I3, I4, I5, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_3<>(name, params);
        }
        public <O3> Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_4<>(name, params);
        }
        public <X> Builder_7_4<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_3<I0, I1, I2, I3, I4, I5, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_4<>(name, params);
        }
        public <O4> Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_5<>(name, params);
        }
        public <X> Builder_7_5<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_4<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_5<>(name, params);
        }
        public <O5> Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_6<>(name, params);
        }
        public <X> Builder_7_6<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_5<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_6<>(name, params);
        }
        public <O6> Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_7<>(name, params);
        }
        public <X> Builder_7_7<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_6<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_7<>(name, params);
        }
        public <O7> Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_8<>(name, params);
        }
        public <X> Builder_7_8<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_7<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_8<>(name, params);
        }
        public <O8> Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_9<>(name, params);
        }
        public <X> Builder_7_9<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_8<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_9<>(name, params);
        }
        public <O9> Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_6_10<>(name, params);
        }
        public <X> Builder_7_10<I0, I1, I2, I3, I4, I5, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_7_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_9<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_6_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I6> Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I6> type) {
            params.add(ParamDef.in(type));
            return new Builder_7_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def6_10<I0, I1, I2, I3, I4, I5, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4, i5) -> delegate.call(i0, i1, i2, i3, i4, i5);
        }
    }

    public static final class Builder_7_0<I0, I1, I2, I3, I4, I5, I6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_0<I0, I1, I2, I3, I4, I5, I6, I7> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_0<>(name, params);
        }
        public <O0> Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_1<>(name, params);
        }
        public <X> Builder_8_1<I0, I1, I2, I3, I4, I5, I6, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_0<I0, I1, I2, I3, I4, I5, I6> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_1<I0, I1, I2, I3, I4, I5, I6, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_1<>(name, params);
        }
        public <O1> Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_2<>(name, params);
        }
        public <X> Builder_8_2<I0, I1, I2, I3, I4, I5, I6, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_1<I0, I1, I2, I3, I4, I5, I6, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_2<>(name, params);
        }
        public <O2> Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_3<>(name, params);
        }
        public <X> Builder_8_3<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_2<I0, I1, I2, I3, I4, I5, I6, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_3<>(name, params);
        }
        public <O3> Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_4<>(name, params);
        }
        public <X> Builder_8_4<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_3<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_4<>(name, params);
        }
        public <O4> Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_5<>(name, params);
        }
        public <X> Builder_8_5<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_4<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_5<>(name, params);
        }
        public <O5> Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_6<>(name, params);
        }
        public <X> Builder_8_6<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_5<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_6<>(name, params);
        }
        public <O6> Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_7<>(name, params);
        }
        public <X> Builder_8_7<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_6<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_7<>(name, params);
        }
        public <O7> Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_8<>(name, params);
        }
        public <X> Builder_8_8<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_7<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_8<>(name, params);
        }
        public <O8> Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_9<>(name, params);
        }
        public <X> Builder_8_9<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_8<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_9<>(name, params);
        }
        public <O9> Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_7_10<>(name, params);
        }
        public <X> Builder_8_10<I0, I1, I2, I3, I4, I5, I6, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_8_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_9<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_7_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I7> Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I7> type) {
            params.add(ParamDef.in(type));
            return new Builder_8_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def7_10<I0, I1, I2, I3, I4, I5, I6, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4, i5, i6) -> delegate.call(i0, i1, i2, i3, i4, i5, i6);
        }
    }

    public static final class Builder_8_0<I0, I1, I2, I3, I4, I5, I6, I7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_0<>(name, params);
        }
        public <O0> Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_1<>(name, params);
        }
        public <X> Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_0<I0, I1, I2, I3, I4, I5, I6, I7> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_1<>(name, params);
        }
        public <O1> Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_2<>(name, params);
        }
        public <X> Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_1<I0, I1, I2, I3, I4, I5, I6, I7, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_2<>(name, params);
        }
        public <O2> Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_3<>(name, params);
        }
        public <X> Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_2<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_3<>(name, params);
        }
        public <O3> Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_4<>(name, params);
        }
        public <X> Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_3<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_4<>(name, params);
        }
        public <O4> Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_5<>(name, params);
        }
        public <X> Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_4<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_5<>(name, params);
        }
        public <O5> Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_6<>(name, params);
        }
        public <X> Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_5<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_6<>(name, params);
        }
        public <O6> Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_7<>(name, params);
        }
        public <X> Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_6<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_7<>(name, params);
        }
        public <O7> Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_8<>(name, params);
        }
        public <X> Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_7<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_8<>(name, params);
        }
        public <O8> Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_9<>(name, params);
        }
        public <X> Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_8<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_9<>(name, params);
        }
        public <O9> Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_8_10<>(name, params);
        }
        public <X> Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_9_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_9<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_8_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I8> Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I8> type) {
            params.add(ParamDef.in(type));
            return new Builder_9_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def8_10<I0, I1, I2, I3, I4, I5, I6, I7, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4, i5, i6, i7) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7);
        }
    }

    public static final class Builder_9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_0<>(name, params);
        }
        public <O0> Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_1<>(name, params);
        }
        public <X> Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_0<I0, I1, I2, I3, I4, I5, I6, I7, I8> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_1<>(name, params);
        }
        public <O1> Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_2<>(name, params);
        }
        public <X> Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_2<>(name, params);
        }
        public <O2> Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_3<>(name, params);
        }
        public <X> Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_3<>(name, params);
        }
        public <O3> Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_4<>(name, params);
        }
        public <X> Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_4<>(name, params);
        }
        public <O4> Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_5<>(name, params);
        }
        public <X> Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_5<>(name, params);
        }
        public <O5> Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_6<>(name, params);
        }
        public <X> Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_6<>(name, params);
        }
        public <O6> Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_7<>(name, params);
        }
        public <X> Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_7<>(name, params);
        }
        public <O7> Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_8<>(name, params);
        }
        public <X> Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_8<>(name, params);
        }
        public <O8> Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_9<>(name, params);
        }
        public <X> Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_9<>(name, params);
        }
        public <O9> Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_9_10<>(name, params);
        }
        public <X> Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, X, O0, O1, O2, O3, O4, O5, O6, O7, O8, X> inout(DbType<X> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
            return new Builder_10_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_9_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <I9> Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> in(DbType<I9> type) {
            params.add(ParamDef.in(type));
            return new Builder_10_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def9_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public static final class Builder_10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_0(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O0> Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> out(DbType<O0> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_1<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_0<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9> build() {
            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_1(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O1> Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> out(DbType<O1> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_2<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_1<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0> build() {
            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_2(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O2> Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> out(DbType<O2> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_3<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_2<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1> build() {
            Procedure<Tuple.Tuple2<O0, O1>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_3(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O3> Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> out(DbType<O3> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_4<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_3<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2> build() {
            Procedure<Tuple.Tuple3<O0, O1, O2>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_4(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O4> Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> out(DbType<O4> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_5<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_4<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3> build() {
            Procedure<Tuple.Tuple4<O0, O1, O2, O3>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_5(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O5> Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> out(DbType<O5> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_6<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_5<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4> build() {
            Procedure<Tuple.Tuple5<O0, O1, O2, O3, O4>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_6(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O6> Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> out(DbType<O6> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_7<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_6<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5> build() {
            Procedure<Tuple.Tuple6<O0, O1, O2, O3, O4, O5>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_7(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O7> Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> out(DbType<O7> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_8<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_7<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6> build() {
            Procedure<Tuple.Tuple7<O0, O1, O2, O3, O4, O5, O6>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_8(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O8> Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> out(DbType<O8> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_9<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_8<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7> build() {
            Procedure<Tuple.Tuple8<O0, O1, O2, O3, O4, O5, O6, O7>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_9(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }
        public <O9> Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> out(DbType<O9> type) {
            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
            return new Builder_10_10<>(name, params);
        }

        @SuppressWarnings("unchecked")
        public Def10_9<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8> build() {
            Procedure<Tuple.Tuple9<O0, O1, O2, O3, O4, O5, O6, O7, O8>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static final class Builder_10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> {
        private final String name;
        private final java.util.List<ParamDef> params;

        Builder_10_10(String name, java.util.List<ParamDef> params) {
            this.name = name;
            this.params = params;
        }

        @SuppressWarnings("unchecked")
        public Def10_10<I0, I1, I2, I3, I4, I5, I6, I7, I8, I9, O0, O1, O2, O3, O4, O5, O6, O7, O8, O9> build() {
            Procedure<Tuple.Tuple10<O0, O1, O2, O3, O4, O5, O6, O7, O8, O9>> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of((O0) values[0], (O1) values[1], (O2) values[2], (O3) values[3], (O4) values[4], (O5) values[5], (O6) values[6], (O7) values[7], (O8) values[8], (O9) values[9]));
            return (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9) -> delegate.call(i0, i1, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }
}
