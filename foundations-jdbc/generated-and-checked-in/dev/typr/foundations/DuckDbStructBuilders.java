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
        public <F> Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder31<>(structName, fields);
        }
    }

    public static final class Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder31(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder32<>(structName, fields);
        }
    }

    public static final class Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder32(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder33<>(structName, fields);
        }
    }

    public static final class Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder33(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder34<>(structName, fields);
        }
    }

    public static final class Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder34(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder35<>(structName, fields);
        }
    }

    public static final class Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder35(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder36<>(structName, fields);
        }
    }

    public static final class Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder36(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder37<>(structName, fields);
        }
    }

    public static final class Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder37(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder38<>(structName, fields);
        }
    }

    public static final class Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder38(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder39<>(structName, fields);
        }
    }

    public static final class Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder39(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder40<>(structName, fields);
        }
    }

    public static final class Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder40(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder41<>(structName, fields);
        }
    }

    public static final class Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder41(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder42<>(structName, fields);
        }
    }

    public static final class Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder42(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder43<>(structName, fields);
        }
    }

    public static final class Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder43(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder44<>(structName, fields);
        }
    }

    public static final class Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder44(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder45<>(structName, fields);
        }
    }

    public static final class Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder45(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder46<>(structName, fields);
        }
    }

    public static final class Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder46(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder47<>(structName, fields);
        }
    }

    public static final class Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder47(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder48<>(structName, fields);
        }
    }

    public static final class Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder48(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder49<>(structName, fields);
        }
    }

    public static final class Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder49(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder50<>(structName, fields);
        }
    }

    public static final class Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder50(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder51<>(structName, fields);
        }
    }

    public static final class Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder51(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder52<>(structName, fields);
        }
    }

    public static final class Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder52(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder53<>(structName, fields);
        }
    }

    public static final class Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder53(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder54<>(structName, fields);
        }
    }

    public static final class Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder54(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder55<>(structName, fields);
        }
    }

    public static final class Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder55(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder56<>(structName, fields);
        }
    }

    public static final class Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder56(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder57<>(structName, fields);
        }
    }

    public static final class Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder57(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder58<>(structName, fields);
        }
    }

    public static final class Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder58(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder59<>(structName, fields);
        }
    }

    public static final class Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder59(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder60<>(structName, fields);
        }
    }

    public static final class Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder60(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder61<>(structName, fields);
        }
    }

    public static final class Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder61(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder62<>(structName, fields);
        }
    }

    public static final class Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder62(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder63<>(structName, fields);
        }
    }

    public static final class Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder63(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder64<>(structName, fields);
        }
    }

    public static final class Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder64(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder65<>(structName, fields);
        }
    }

    public static final class Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder65(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder66<>(structName, fields);
        }
    }

    public static final class Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder66(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder67<>(structName, fields);
        }
    }

    public static final class Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder67(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder68<>(structName, fields);
        }
    }

    public static final class Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder68(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder69<>(structName, fields);
        }
    }

    public static final class Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder69(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder70<>(structName, fields);
        }
    }

    public static final class Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder70(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder71<>(structName, fields);
        }
    }

    public static final class Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder71(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder72<>(structName, fields);
        }
    }

    public static final class Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder72(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder73<>(structName, fields);
        }
    }

    public static final class Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder73(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder74<>(structName, fields);
        }
    }

    public static final class Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder74(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder75<>(structName, fields);
        }
    }

    public static final class Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder75(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder76<>(structName, fields);
        }
    }

    public static final class Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder76(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder77<>(structName, fields);
        }
    }

    public static final class Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder77(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder78<>(structName, fields);
        }
    }

    public static final class Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder78(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder79<>(structName, fields);
        }
    }

    public static final class Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder79(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder80<>(structName, fields);
        }
    }

    public static final class Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder80(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder81<>(structName, fields);
        }
    }

    public static final class Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder81(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder82<>(structName, fields);
        }
    }

    public static final class Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder82(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder83<>(structName, fields);
        }
    }

    public static final class Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder83(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder84<>(structName, fields);
        }
    }

    public static final class Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder84(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder85<>(structName, fields);
        }
    }

    public static final class Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder85(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder86<>(structName, fields);
        }
    }

    public static final class Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder86(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder87<>(structName, fields);
        }
    }

    public static final class Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder87(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder88<>(structName, fields);
        }
    }

    public static final class Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder88(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder89<>(structName, fields);
        }
    }

    public static final class Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder89(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder90<>(structName, fields);
        }
    }

    public static final class Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder90(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder91<>(structName, fields);
        }
    }

    public static final class Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder91(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder92<>(structName, fields);
        }
    }

    public static final class Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder92(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder93<>(structName, fields);
        }
    }

    public static final class Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder93(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder94<>(structName, fields);
        }
    }

    public static final class Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder94(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder95<>(structName, fields);
        }
    }

    public static final class Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder95(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder96<>(structName, fields);
        }
    }

    public static final class Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder96(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder97<>(structName, fields);
        }
    }

    public static final class Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder97(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder98<>(structName, fields);
        }
    }

    public static final class Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder98(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
                }
            });
        }
        public <F> Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new DuckDbStruct.Field<>(name, type, getter));
            return new Builder99<>(structName, fields);
        }
    }

    public static final class Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> {
        private final String structName;
        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;

        Builder99(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
            this.structName = structName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public DuckDbStruct<A> build(Functions.Function99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, A> decode) {
            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97], (T98) arr[98]);
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
