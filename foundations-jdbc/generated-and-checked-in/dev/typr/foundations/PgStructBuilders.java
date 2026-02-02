package dev.typr.foundations;

import dev.typr.foundations.data.JsonValue;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Type-safe builders for PostgreSQL composite types.
 * <p>
 * Usage:
 * <pre>{@code
 * PgStruct<Dimensions> pgStruct = PgStructBuilders.<Dimensions>builder("dimensions")
 *     .field("width", PgTypes.float8, Dimensions::width)
 *     .field("height", PgTypes.float8, Dimensions::height)
 *     .field("depth", PgTypes.float8, Dimensions::depth)
 *     .field("unit", PgTypes.varchar, Dimensions::unit)
 *     .build(Dimensions::new);  // No casts needed!
 * }</pre>
 */
public final class PgStructBuilders {
    private PgStructBuilders() {}

    public static <A> Builder0<A> builder(String typeName) {
        return new Builder0<>(typeName);
    }

    public static final class Builder0<A> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields = new java.util.ArrayList<>();

        Builder0(String typeName) {
            this.typeName = typeName;
        }

        public <F> Builder1<A, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder1<>(typeName, fields);
        }

        public <F> Builder1<A, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder1<A, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder1<A, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder1<A, java.util.Optional<F>> result = (Builder1<A, java.util.Optional<F>>) (Object) new Builder1<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder1<A, T0> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder1(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(java.util.function.Function<T0, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0]));
        }
        public <F> Builder2<A, T0, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder2<>(typeName, fields);
        }

        public <F> Builder2<A, T0, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder2<A, T0, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder2<A, T0, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder2<A, T0, java.util.Optional<F>> result = (Builder2<A, T0, java.util.Optional<F>>) (Object) new Builder2<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder2<A, T0, T1> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder2(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function2<T0, T1, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1]));
        }
        public <F> Builder3<A, T0, T1, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder3<>(typeName, fields);
        }

        public <F> Builder3<A, T0, T1, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder3<A, T0, T1, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder3<A, T0, T1, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder3<A, T0, T1, java.util.Optional<F>> result = (Builder3<A, T0, T1, java.util.Optional<F>>) (Object) new Builder3<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder3<A, T0, T1, T2> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder3(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function3<T0, T1, T2, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2]));
        }
        public <F> Builder4<A, T0, T1, T2, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder4<>(typeName, fields);
        }

        public <F> Builder4<A, T0, T1, T2, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder4<A, T0, T1, T2, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder4<A, T0, T1, T2, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder4<A, T0, T1, T2, java.util.Optional<F>> result = (Builder4<A, T0, T1, T2, java.util.Optional<F>>) (Object) new Builder4<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder4<A, T0, T1, T2, T3> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder4(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function4<T0, T1, T2, T3, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3]));
        }
        public <F> Builder5<A, T0, T1, T2, T3, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder5<>(typeName, fields);
        }

        public <F> Builder5<A, T0, T1, T2, T3, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder5<A, T0, T1, T2, T3, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder5<A, T0, T1, T2, T3, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder5<A, T0, T1, T2, T3, java.util.Optional<F>> result = (Builder5<A, T0, T1, T2, T3, java.util.Optional<F>>) (Object) new Builder5<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder5<A, T0, T1, T2, T3, T4> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder5(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function5<T0, T1, T2, T3, T4, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4]));
        }
        public <F> Builder6<A, T0, T1, T2, T3, T4, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder6<>(typeName, fields);
        }

        public <F> Builder6<A, T0, T1, T2, T3, T4, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder6<A, T0, T1, T2, T3, T4, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder6<A, T0, T1, T2, T3, T4, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder6<A, T0, T1, T2, T3, T4, java.util.Optional<F>> result = (Builder6<A, T0, T1, T2, T3, T4, java.util.Optional<F>>) (Object) new Builder6<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder6<A, T0, T1, T2, T3, T4, T5> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder6(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function6<T0, T1, T2, T3, T4, T5, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5]));
        }
        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder7<>(typeName, fields);
        }

        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder7<A, T0, T1, T2, T3, T4, T5, java.util.Optional<F>> result = (Builder7<A, T0, T1, T2, T3, T4, T5, java.util.Optional<F>>) (Object) new Builder7<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder7<A, T0, T1, T2, T3, T4, T5, T6> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder7(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function7<T0, T1, T2, T3, T4, T5, T6, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6]));
        }
        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder8<>(typeName, fields);
        }

        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder8<A, T0, T1, T2, T3, T4, T5, T6, java.util.Optional<F>> result = (Builder8<A, T0, T1, T2, T3, T4, T5, T6, java.util.Optional<F>>) (Object) new Builder8<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder8(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function8<T0, T1, T2, T3, T4, T5, T6, T7, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7]));
        }
        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder9<>(typeName, fields);
        }

        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, java.util.Optional<F>> result = (Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, java.util.Optional<F>>) (Object) new Builder9<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder9(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8]));
        }
        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder10<>(typeName, fields);
        }

        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, java.util.Optional<F>> result = (Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, java.util.Optional<F>>) (Object) new Builder10<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder10(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9]));
        }
        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder11<>(typeName, fields);
        }

        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, java.util.Optional<F>> result = (Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, java.util.Optional<F>>) (Object) new Builder11<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder11(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10]));
        }
        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder12<>(typeName, fields);
        }

        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, java.util.Optional<F>> result = (Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, java.util.Optional<F>>) (Object) new Builder12<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder12(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11]));
        }
        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder13<>(typeName, fields);
        }

        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, java.util.Optional<F>> result = (Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, java.util.Optional<F>>) (Object) new Builder13<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder13(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12]));
        }
        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder14<>(typeName, fields);
        }

        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, java.util.Optional<F>> result = (Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, java.util.Optional<F>>) (Object) new Builder14<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder14(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13]));
        }
        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder15<>(typeName, fields);
        }

        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, java.util.Optional<F>> result = (Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, java.util.Optional<F>>) (Object) new Builder15<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder15(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14]));
        }
        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder16<>(typeName, fields);
        }

        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, java.util.Optional<F>> result = (Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, java.util.Optional<F>>) (Object) new Builder16<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder16(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15]));
        }
        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder17<>(typeName, fields);
        }

        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, java.util.Optional<F>> result = (Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, java.util.Optional<F>>) (Object) new Builder17<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder17(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16]));
        }
        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder18<>(typeName, fields);
        }

        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, java.util.Optional<F>> result = (Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, java.util.Optional<F>>) (Object) new Builder18<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder18(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17]));
        }
        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder19<>(typeName, fields);
        }

        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, java.util.Optional<F>> result = (Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, java.util.Optional<F>>) (Object) new Builder19<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder19(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18]));
        }
        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder20<>(typeName, fields);
        }

        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, java.util.Optional<F>> result = (Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, java.util.Optional<F>>) (Object) new Builder20<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder20(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19]));
        }
        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder21<>(typeName, fields);
        }

        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, java.util.Optional<F>> result = (Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, java.util.Optional<F>>) (Object) new Builder21<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder21(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20]));
        }
        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder22<>(typeName, fields);
        }

        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, java.util.Optional<F>> result = (Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, java.util.Optional<F>>) (Object) new Builder22<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder22(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21]));
        }
        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder23<>(typeName, fields);
        }

        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, java.util.Optional<F>> result = (Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, java.util.Optional<F>>) (Object) new Builder23<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder23(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22]));
        }
        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder24<>(typeName, fields);
        }

        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, java.util.Optional<F>> result = (Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, java.util.Optional<F>>) (Object) new Builder24<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder24(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23]));
        }
        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder25<>(typeName, fields);
        }

        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, java.util.Optional<F>> result = (Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, java.util.Optional<F>>) (Object) new Builder25<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder25(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24]));
        }
        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder26<>(typeName, fields);
        }

        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, java.util.Optional<F>> result = (Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, java.util.Optional<F>>) (Object) new Builder26<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder26(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25]));
        }
        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder27<>(typeName, fields);
        }

        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, java.util.Optional<F>> result = (Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, java.util.Optional<F>>) (Object) new Builder27<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder27(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26]));
        }
        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder28<>(typeName, fields);
        }

        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, java.util.Optional<F>> result = (Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, java.util.Optional<F>>) (Object) new Builder28<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder28(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27]));
        }
        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder29<>(typeName, fields);
        }

        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, java.util.Optional<F>> result = (Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, java.util.Optional<F>>) (Object) new Builder29<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder29(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28]));
        }
        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder30<>(typeName, fields);
        }

        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, java.util.Optional<F>> result = (Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, java.util.Optional<F>>) (Object) new Builder30<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder30(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29]));
        }
        public <F> Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder31<>(typeName, fields);
        }

        public <F> Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, java.util.Optional<F>> result = (Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, java.util.Optional<F>>) (Object) new Builder31<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder31<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder31(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30]));
        }
        public <F> Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder32<>(typeName, fields);
        }

        public <F> Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, java.util.Optional<F>> result = (Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, java.util.Optional<F>>) (Object) new Builder32<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder32<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder32(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31]));
        }
        public <F> Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder33<>(typeName, fields);
        }

        public <F> Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, java.util.Optional<F>> result = (Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, java.util.Optional<F>>) (Object) new Builder33<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder33<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder33(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32]));
        }
        public <F> Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder34<>(typeName, fields);
        }

        public <F> Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, java.util.Optional<F>> result = (Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, java.util.Optional<F>>) (Object) new Builder34<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder34<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder34(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33]));
        }
        public <F> Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder35<>(typeName, fields);
        }

        public <F> Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, java.util.Optional<F>> result = (Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, java.util.Optional<F>>) (Object) new Builder35<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder35<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder35(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34]));
        }
        public <F> Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder36<>(typeName, fields);
        }

        public <F> Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, java.util.Optional<F>> result = (Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, java.util.Optional<F>>) (Object) new Builder36<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder36<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder36(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35]));
        }
        public <F> Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder37<>(typeName, fields);
        }

        public <F> Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, java.util.Optional<F>> result = (Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, java.util.Optional<F>>) (Object) new Builder37<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder37<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder37(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36]));
        }
        public <F> Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder38<>(typeName, fields);
        }

        public <F> Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, java.util.Optional<F>> result = (Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, java.util.Optional<F>>) (Object) new Builder38<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder38<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder38(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37]));
        }
        public <F> Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder39<>(typeName, fields);
        }

        public <F> Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, java.util.Optional<F>> result = (Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, java.util.Optional<F>>) (Object) new Builder39<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder39<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder39(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38]));
        }
        public <F> Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder40<>(typeName, fields);
        }

        public <F> Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, java.util.Optional<F>> result = (Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, java.util.Optional<F>>) (Object) new Builder40<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder40<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder40(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39]));
        }
        public <F> Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder41<>(typeName, fields);
        }

        public <F> Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, java.util.Optional<F>> result = (Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, java.util.Optional<F>>) (Object) new Builder41<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder41<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder41(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40]));
        }
        public <F> Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder42<>(typeName, fields);
        }

        public <F> Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, java.util.Optional<F>> result = (Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, java.util.Optional<F>>) (Object) new Builder42<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder42<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder42(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41]));
        }
        public <F> Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder43<>(typeName, fields);
        }

        public <F> Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, java.util.Optional<F>> result = (Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, java.util.Optional<F>>) (Object) new Builder43<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder43<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder43(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42]));
        }
        public <F> Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder44<>(typeName, fields);
        }

        public <F> Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, java.util.Optional<F>> result = (Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, java.util.Optional<F>>) (Object) new Builder44<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder44<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder44(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43]));
        }
        public <F> Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder45<>(typeName, fields);
        }

        public <F> Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, java.util.Optional<F>> result = (Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, java.util.Optional<F>>) (Object) new Builder45<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder45<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder45(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44]));
        }
        public <F> Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder46<>(typeName, fields);
        }

        public <F> Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, java.util.Optional<F>> result = (Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, java.util.Optional<F>>) (Object) new Builder46<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder46<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder46(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45]));
        }
        public <F> Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder47<>(typeName, fields);
        }

        public <F> Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, java.util.Optional<F>> result = (Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, java.util.Optional<F>>) (Object) new Builder47<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder47<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder47(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46]));
        }
        public <F> Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder48<>(typeName, fields);
        }

        public <F> Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, java.util.Optional<F>> result = (Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, java.util.Optional<F>>) (Object) new Builder48<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder48<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder48(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47]));
        }
        public <F> Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder49<>(typeName, fields);
        }

        public <F> Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, java.util.Optional<F>> result = (Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, java.util.Optional<F>>) (Object) new Builder49<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder49<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder49(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48]));
        }
        public <F> Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder50<>(typeName, fields);
        }

        public <F> Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, java.util.Optional<F>> result = (Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, java.util.Optional<F>>) (Object) new Builder50<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder50<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder50(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49]));
        }
        public <F> Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder51<>(typeName, fields);
        }

        public <F> Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, java.util.Optional<F>> result = (Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, java.util.Optional<F>>) (Object) new Builder51<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder51<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder51(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50]));
        }
        public <F> Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder52<>(typeName, fields);
        }

        public <F> Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, java.util.Optional<F>> result = (Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, java.util.Optional<F>>) (Object) new Builder52<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder52<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder52(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51]));
        }
        public <F> Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder53<>(typeName, fields);
        }

        public <F> Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, java.util.Optional<F>> result = (Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, java.util.Optional<F>>) (Object) new Builder53<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder53<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder53(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52]));
        }
        public <F> Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder54<>(typeName, fields);
        }

        public <F> Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, java.util.Optional<F>> result = (Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, java.util.Optional<F>>) (Object) new Builder54<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder54<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder54(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53]));
        }
        public <F> Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder55<>(typeName, fields);
        }

        public <F> Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, java.util.Optional<F>> result = (Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, java.util.Optional<F>>) (Object) new Builder55<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder55<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder55(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54]));
        }
        public <F> Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder56<>(typeName, fields);
        }

        public <F> Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, java.util.Optional<F>> result = (Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, java.util.Optional<F>>) (Object) new Builder56<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder56<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder56(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55]));
        }
        public <F> Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder57<>(typeName, fields);
        }

        public <F> Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, java.util.Optional<F>> result = (Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, java.util.Optional<F>>) (Object) new Builder57<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder57<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder57(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56]));
        }
        public <F> Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder58<>(typeName, fields);
        }

        public <F> Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, java.util.Optional<F>> result = (Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, java.util.Optional<F>>) (Object) new Builder58<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder58<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder58(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57]));
        }
        public <F> Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder59<>(typeName, fields);
        }

        public <F> Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, java.util.Optional<F>> result = (Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, java.util.Optional<F>>) (Object) new Builder59<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder59<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder59(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58]));
        }
        public <F> Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder60<>(typeName, fields);
        }

        public <F> Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, java.util.Optional<F>> result = (Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, java.util.Optional<F>>) (Object) new Builder60<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder60<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder60(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59]));
        }
        public <F> Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder61<>(typeName, fields);
        }

        public <F> Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, java.util.Optional<F>> result = (Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, java.util.Optional<F>>) (Object) new Builder61<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder61<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder61(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60]));
        }
        public <F> Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder62<>(typeName, fields);
        }

        public <F> Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, java.util.Optional<F>> result = (Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, java.util.Optional<F>>) (Object) new Builder62<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder62<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder62(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61]));
        }
        public <F> Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder63<>(typeName, fields);
        }

        public <F> Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, java.util.Optional<F>> result = (Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, java.util.Optional<F>>) (Object) new Builder63<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder63<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder63(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62]));
        }
        public <F> Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder64<>(typeName, fields);
        }

        public <F> Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, java.util.Optional<F>> result = (Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, java.util.Optional<F>>) (Object) new Builder64<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder64<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder64(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63]));
        }
        public <F> Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder65<>(typeName, fields);
        }

        public <F> Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, java.util.Optional<F>> result = (Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, java.util.Optional<F>>) (Object) new Builder65<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder65<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder65(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64]));
        }
        public <F> Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder66<>(typeName, fields);
        }

        public <F> Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, java.util.Optional<F>> result = (Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, java.util.Optional<F>>) (Object) new Builder66<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder66<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder66(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65]));
        }
        public <F> Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder67<>(typeName, fields);
        }

        public <F> Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, java.util.Optional<F>> result = (Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, java.util.Optional<F>>) (Object) new Builder67<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder67<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder67(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66]));
        }
        public <F> Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder68<>(typeName, fields);
        }

        public <F> Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, java.util.Optional<F>> result = (Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, java.util.Optional<F>>) (Object) new Builder68<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder68<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder68(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67]));
        }
        public <F> Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder69<>(typeName, fields);
        }

        public <F> Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, java.util.Optional<F>> result = (Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, java.util.Optional<F>>) (Object) new Builder69<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder69<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder69(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68]));
        }
        public <F> Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder70<>(typeName, fields);
        }

        public <F> Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, java.util.Optional<F>> result = (Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, java.util.Optional<F>>) (Object) new Builder70<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder70<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder70(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69]));
        }
        public <F> Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder71<>(typeName, fields);
        }

        public <F> Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, java.util.Optional<F>> result = (Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, java.util.Optional<F>>) (Object) new Builder71<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder71<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder71(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70]));
        }
        public <F> Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder72<>(typeName, fields);
        }

        public <F> Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, java.util.Optional<F>> result = (Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, java.util.Optional<F>>) (Object) new Builder72<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder72<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder72(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71]));
        }
        public <F> Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder73<>(typeName, fields);
        }

        public <F> Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, java.util.Optional<F>> result = (Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, java.util.Optional<F>>) (Object) new Builder73<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder73<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder73(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72]));
        }
        public <F> Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder74<>(typeName, fields);
        }

        public <F> Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, java.util.Optional<F>> result = (Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, java.util.Optional<F>>) (Object) new Builder74<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder74<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder74(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73]));
        }
        public <F> Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder75<>(typeName, fields);
        }

        public <F> Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, java.util.Optional<F>> result = (Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, java.util.Optional<F>>) (Object) new Builder75<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder75<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder75(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74]));
        }
        public <F> Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder76<>(typeName, fields);
        }

        public <F> Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, java.util.Optional<F>> result = (Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, java.util.Optional<F>>) (Object) new Builder76<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder76<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder76(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75]));
        }
        public <F> Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder77<>(typeName, fields);
        }

        public <F> Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, java.util.Optional<F>> result = (Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, java.util.Optional<F>>) (Object) new Builder77<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder77<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder77(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76]));
        }
        public <F> Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder78<>(typeName, fields);
        }

        public <F> Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, java.util.Optional<F>> result = (Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, java.util.Optional<F>>) (Object) new Builder78<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder78<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder78(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77]));
        }
        public <F> Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder79<>(typeName, fields);
        }

        public <F> Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, java.util.Optional<F>> result = (Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, java.util.Optional<F>>) (Object) new Builder79<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder79<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder79(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78]));
        }
        public <F> Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder80<>(typeName, fields);
        }

        public <F> Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, java.util.Optional<F>> result = (Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, java.util.Optional<F>>) (Object) new Builder80<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder80<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder80(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79]));
        }
        public <F> Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder81<>(typeName, fields);
        }

        public <F> Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, java.util.Optional<F>> result = (Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, java.util.Optional<F>>) (Object) new Builder81<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder81<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder81(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80]));
        }
        public <F> Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder82<>(typeName, fields);
        }

        public <F> Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, java.util.Optional<F>> result = (Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, java.util.Optional<F>>) (Object) new Builder82<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder82<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder82(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81]));
        }
        public <F> Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder83<>(typeName, fields);
        }

        public <F> Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, java.util.Optional<F>> result = (Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, java.util.Optional<F>>) (Object) new Builder83<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder83<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder83(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82]));
        }
        public <F> Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder84<>(typeName, fields);
        }

        public <F> Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, java.util.Optional<F>> result = (Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, java.util.Optional<F>>) (Object) new Builder84<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder84<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder84(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83]));
        }
        public <F> Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder85<>(typeName, fields);
        }

        public <F> Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, java.util.Optional<F>> result = (Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, java.util.Optional<F>>) (Object) new Builder85<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder85<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder85(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84]));
        }
        public <F> Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder86<>(typeName, fields);
        }

        public <F> Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, java.util.Optional<F>> result = (Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, java.util.Optional<F>>) (Object) new Builder86<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder86<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder86(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85]));
        }
        public <F> Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder87<>(typeName, fields);
        }

        public <F> Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, java.util.Optional<F>> result = (Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, java.util.Optional<F>>) (Object) new Builder87<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder87<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder87(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86]));
        }
        public <F> Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder88<>(typeName, fields);
        }

        public <F> Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, java.util.Optional<F>> result = (Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, java.util.Optional<F>>) (Object) new Builder88<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder88<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder88(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87]));
        }
        public <F> Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder89<>(typeName, fields);
        }

        public <F> Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, java.util.Optional<F>> result = (Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, java.util.Optional<F>>) (Object) new Builder89<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder89<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder89(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88]));
        }
        public <F> Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder90<>(typeName, fields);
        }

        public <F> Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, java.util.Optional<F>> result = (Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, java.util.Optional<F>>) (Object) new Builder90<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder90<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder90(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89]));
        }
        public <F> Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder91<>(typeName, fields);
        }

        public <F> Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, java.util.Optional<F>> result = (Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, java.util.Optional<F>>) (Object) new Builder91<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder91<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder91(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90]));
        }
        public <F> Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder92<>(typeName, fields);
        }

        public <F> Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, java.util.Optional<F>> result = (Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, java.util.Optional<F>>) (Object) new Builder92<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder92<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder92(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91]));
        }
        public <F> Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder93<>(typeName, fields);
        }

        public <F> Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, java.util.Optional<F>> result = (Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, java.util.Optional<F>>) (Object) new Builder93<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder93<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder93(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92]));
        }
        public <F> Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder94<>(typeName, fields);
        }

        public <F> Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, java.util.Optional<F>> result = (Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, java.util.Optional<F>>) (Object) new Builder94<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder94<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder94(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93]));
        }
        public <F> Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder95<>(typeName, fields);
        }

        public <F> Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, java.util.Optional<F>> result = (Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, java.util.Optional<F>>) (Object) new Builder95<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder95<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder95(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94]));
        }
        public <F> Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder96<>(typeName, fields);
        }

        public <F> Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, java.util.Optional<F>> result = (Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, java.util.Optional<F>>) (Object) new Builder96<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder96<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder96(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95]));
        }
        public <F> Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder97<>(typeName, fields);
        }

        public <F> Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, java.util.Optional<F>> result = (Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, java.util.Optional<F>>) (Object) new Builder97<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder97<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder97(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96]));
        }
        public <F> Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder98<>(typeName, fields);
        }

        public <F> Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, java.util.Optional<F>> result = (Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, java.util.Optional<F>>) (Object) new Builder98<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder98<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder98(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97]));
        }
        public <F> Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
            fields.add(new PgStruct.Field<>(name, type, getter));
            return new Builder99<>(typeName, fields);
        }

        public <F> Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
            return field(name, nestedStruct.asType(), getter);
        }

        public <F> Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
            PgType<F> elementType = nestedStruct.asType();
            PgType<F[]> arrayType = new PgType<>(
                elementType.typename().array(),
                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                elementType.write().array(elementType.typename()),
                elementType.pgText().array(),
                elementType.pgCompositeText().array(arrayFactory),
                elementType.pgJson().array(arrayFactory));
            return field(name, arrayType, getter);
        }

        public <F> Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
            @SuppressWarnings("unchecked")
            Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, java.util.Optional<F>> result = (Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, java.util.Optional<F>>) (Object) new Builder99<>(typeName, fields);
            return result;
        }
    }

    public static final class Builder99<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> {
        private final String typeName;
        private final java.util.List<PgStruct.Field<A, ?>> fields;

        Builder99(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
            this.typeName = typeName;
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public PgStruct<A> build(Functions.Function99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, A> decode) {
            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97], (T98) arr[98]));
        }
    }

    @SuppressWarnings("unchecked")
    private static <A> PgStruct<A> buildStruct(String typeName, java.util.List<PgStruct.Field<A, ?>> fields, PgStruct.StructReader<A> reader) {
        java.util.List<PgTypename.CompositeOf.CompositeField> typenameFields =
            fields.stream()
                .map(f -> new PgTypename.CompositeOf.CompositeField(f.name(), f.type().typename()))
                .toList();

        PgTypename.CompositeOf<A> typename = new PgTypename.CompositeOf<>(typeName, typenameFields);

        PgStruct.StructWriter<A> writer = structValue -> {
            Object[] values = new Object[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                values[i] = ((PgStruct.Field<A, Object>) fields.get(i)).getter().apply(structValue);
            }
            return values;
        };

        PgJson<A> json = new PgJson<>() {
            @Override
            public JsonValue toJson(A value) {
                LinkedHashMap<String, JsonValue> jsonFields = new LinkedHashMap<>();
                for (PgStruct.Field<A, ?> field : fields) {
                    jsonFields.put(field.name(), fieldToJson(field, value));
                }
                return new JsonValue.JObject(jsonFields);
            }

            @Override
            public A fromJson(JsonValue jsonValue) {
                if (jsonValue instanceof JsonValue.JObject obj) {
                    Object[] values = new Object[fields.size()];
                    for (int i = 0; i < fields.size(); i++) {
                        PgStruct.Field<A, ?> field = fields.get(i);
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
            private <F> JsonValue fieldToJson(PgStruct.Field<A, F> field, A structValue) {
                F value = field.getter().apply(structValue);
                if (value == null) return JsonValue.JNull.INSTANCE;
                return field.type().pgJson().toJson(value);
            }

            @SuppressWarnings("unchecked")
            private <F> Object fieldFromJson(PgStruct.Field<A, F> field, JsonValue jsonValue) {
                if (jsonValue == null || jsonValue instanceof JsonValue.JNull) return null;
                return field.type().pgJson().fromJson(jsonValue);
            }
        };

        return new PgStruct<>(typename, List.copyOf(fields), reader, writer, json);
    }
}
