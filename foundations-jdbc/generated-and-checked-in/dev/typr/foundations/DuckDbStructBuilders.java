package dev.typr.foundations;

import dev.typr.foundations.data.JsonValue;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Type-safe builders for DuckDB STRUCT types.
 * <p>
 * Usage:
 * <pre>{@code
 * DuckDbStruct<Point> struct = DuckDbStructBuilders.<Point>builder("point")
 *     .field("x", DuckDbTypes.double_, Point::x)
 *     .field("y", DuckDbTypes.double_, Point::y)
 *     .build(Point::new);  // No casts needed!
 * }</pre>
 */
public final class DuckDbStructBuilders {
    private DuckDbStructBuilders() {}

    public static <A> Builder0<A> builder(String structName) {
        return new Builder0<>(structName);
    }

    public static final class Builder0<A> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields = new java.util.ArrayList<>();

        Builder0(String structName) {
            this.structName = structName;
        }

        public <F> Builder1<A, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder1<>(structName, fields);
        }
    }

    public static final class Builder1<A, T0> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder1(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(java.util.function.Function<T0, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder2<A, T0, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder2<>(structName, fields);
        }
    }

    public static final class Builder2<A, T0, T1> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder2(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function2<T0, T1, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder3<A, T0, T1, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder3<>(structName, fields);
        }
    }

    public static final class Builder3<A, T0, T1, T2> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder3(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function3<T0, T1, T2, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder4<A, T0, T1, T2, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder4<>(structName, fields);
        }
    }

    public static final class Builder4<A, T0, T1, T2, T3> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder4(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function4<T0, T1, T2, T3, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder5<A, T0, T1, T2, T3, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder5<>(structName, fields);
        }
    }

    public static final class Builder5<A, T0, T1, T2, T3, T4> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder5(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function5<T0, T1, T2, T3, T4, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder6<A, T0, T1, T2, T3, T4, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder6<>(structName, fields);
        }
    }

    public static final class Builder6<A, T0, T1, T2, T3, T4, T5> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder6(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function6<T0, T1, T2, T3, T4, T5, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder7<>(structName, fields);
        }
    }

    public static final class Builder7<A, T0, T1, T2, T3, T4, T5, T6> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder7(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function7<T0, T1, T2, T3, T4, T5, T6, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder8<>(structName, fields);
        }
    }

    public static final class Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder8(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function8<T0, T1, T2, T3, T4, T5, T6, T7, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder9<>(structName, fields);
        }
    }

    public static final class Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder9(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder10<>(structName, fields);
        }
    }

    public static final class Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder10(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder11<>(structName, fields);
        }
    }

    public static final class Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder11(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder12<>(structName, fields);
        }
    }

    public static final class Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder12(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder13<>(structName, fields);
        }
    }

    public static final class Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder13(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder14<>(structName, fields);
        }
    }

    public static final class Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder14(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder15<>(structName, fields);
        }
    }

    public static final class Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder15(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder16<>(structName, fields);
        }
    }

    public static final class Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder16(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder17<>(structName, fields);
        }
    }

    public static final class Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder17(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder18<>(structName, fields);
        }
    }

    public static final class Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder18(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder19<>(structName, fields);
        }
    }

    public static final class Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder19(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder20<>(structName, fields);
        }
    }

    public static final class Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder20(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder21<>(structName, fields);
        }
    }

    public static final class Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder21(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder22<>(structName, fields);
        }
    }

    public static final class Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder22(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder23<>(structName, fields);
        }
    }

    public static final class Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder23(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder24<>(structName, fields);
        }
    }

    public static final class Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder24(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder25<>(structName, fields);
        }
    }

    public static final class Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder25(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder26<>(structName, fields);
        }
    }

    public static final class Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder26(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder27<>(structName, fields);
        }
    }

    public static final class Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder27(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder28<>(structName, fields);
        }
    }

    public static final class Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder28(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder29<>(structName, fields);
        }
    }

    public static final class Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder29(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder30<>(structName, fields);
        }
    }

    public static final class Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder30(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    static <A> DuckDbStruct<A> buildStruct(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields, DuckDbStruct.StructReader<A> reader) {
        List<DuckDbTypename.StructOf.StructField> typenameFields =
            fields.stream()
                .map(f -> new DuckDbTypename.StructOf.StructField(f.name(), f.type().typename()))
                .toList();

        DuckDbTypename.StructOf<A> typename = new DuckDbTypename.StructOf<>(structName, typenameFields);

        DuckDbStruct.StructWriter<A> writer = structValue -> {
            Object[] values = new Object[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                values[i] = ((DuckDbStruct.Field<A, Object>) fields.get(i)).getter().apply(structValue);
            }
            return values;
        };

        DuckDbJson<A> json = new DuckDbJson<>() {
            @Override
            public JsonValue toJson(A value) {
                LinkedHashMap<String, JsonValue> jsonFields = new LinkedHashMap<>();
                for (DuckDbStruct.Field<A, ?> field : fields) {
                    jsonFields.put(field.name(), fieldToJson(field, value));
                }
                return new JsonValue.JObject(jsonFields);
            }

            @Override
            public A fromJson(JsonValue jsonValue) {
                if (jsonValue instanceof JsonValue.JObject obj) {
                    Object[] values = new Object[fields.size()];
                    for (int i = 0; i < fields.size(); i++) {
                        DuckDbStruct.Field<A, ?> field = fields.get(i);
                        JsonValue fieldJson = obj.fields().get(field.name());
                        values[i] = fieldFromJson(field, fieldJson);
                    }
                    try {
                        return reader.read(values);
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException("Failed to construct struct from JSON", e);
                    }
                }
                throw new IllegalArgumentException("Expected JSON object");
            }

            @SuppressWarnings("unchecked")
            private <F> JsonValue fieldToJson(DuckDbStruct.Field<A, F> field, A structValue) {
                F value = field.getter().apply(structValue);
                if (value == null) return JsonValue.JNull.INSTANCE;
                return field.type().duckDbJson().toJson(value);
            }

            @SuppressWarnings("unchecked")
            private <F> Object fieldFromJson(DuckDbStruct.Field<A, F> field, JsonValue jsonValue) {
                if (jsonValue == null || jsonValue instanceof JsonValue.JNull) return null;
                return field.type().duckDbJson().fromJson(jsonValue);
            }
        };

        return new DuckDbStruct<>(typename, List.copyOf(fields), reader, writer, json);
    }
}
