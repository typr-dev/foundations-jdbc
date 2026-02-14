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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
                elementType.pgJson().array(arrayFactory),
                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                AnalysisOptions.EMPTY);
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
