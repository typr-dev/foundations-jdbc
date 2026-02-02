package dev.typr.foundations;

/**
 * Type-safe builders for RowParser.
 * <p>
 * Usage:
 * <pre>{@code
 * RowParser<Product> parser = RowParserBuilders.<Product>builder()
 *     .field(PgTypes.int4, Product::id)
 *     .field(PgTypes.text, Product::name)
 *     .field(PgTypes.numeric, Product::price)
 *     .build(Product::new);
 * }</pre>
 */
public final class RowParserBuilders {
    private RowParserBuilders() {}

    public static <Row> Builder0<Row> builder() {
        return new Builder0<>();
    }

    public static final class Builder0<Row> {
        private final java.util.List<DbType<?>> types = new java.util.ArrayList<>();
        private final java.util.List<java.util.function.Function<Row, ?>> getters = new java.util.ArrayList<>();

        Builder0() {}

        public <F> Builder1<Row, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder1<>(types, getters);
        }
    }

    public static final class Builder1<Row, T0> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder1(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(java.util.function.Function<T0, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder2<Row, T0, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder2<>(types, getters);
        }
    }

    public static final class Builder2<Row, T0, T1> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder2(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function2<T0, T1, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder3<Row, T0, T1, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder3<>(types, getters);
        }
    }

    public static final class Builder3<Row, T0, T1, T2> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder3(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function3<T0, T1, T2, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder4<Row, T0, T1, T2, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder4<>(types, getters);
        }
    }

    public static final class Builder4<Row, T0, T1, T2, T3> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder4(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function4<T0, T1, T2, T3, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder5<Row, T0, T1, T2, T3, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder5<>(types, getters);
        }
    }

    public static final class Builder5<Row, T0, T1, T2, T3, T4> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder5(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function5<T0, T1, T2, T3, T4, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder6<Row, T0, T1, T2, T3, T4, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder6<>(types, getters);
        }
    }

    public static final class Builder6<Row, T0, T1, T2, T3, T4, T5> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder6(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function6<T0, T1, T2, T3, T4, T5, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder7<Row, T0, T1, T2, T3, T4, T5, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder7<>(types, getters);
        }
    }

    public static final class Builder7<Row, T0, T1, T2, T3, T4, T5, T6> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder7(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function7<T0, T1, T2, T3, T4, T5, T6, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder8<Row, T0, T1, T2, T3, T4, T5, T6, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder8<>(types, getters);
        }
    }

    public static final class Builder8<Row, T0, T1, T2, T3, T4, T5, T6, T7> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder8(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function8<T0, T1, T2, T3, T4, T5, T6, T7, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder9<Row, T0, T1, T2, T3, T4, T5, T6, T7, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder9<>(types, getters);
        }
    }

    public static final class Builder9<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder9(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder10<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder10<>(types, getters);
        }
    }

    public static final class Builder10<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder10(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder11<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder11<>(types, getters);
        }
    }

    public static final class Builder11<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder11(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder12<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder12<>(types, getters);
        }
    }

    public static final class Builder12<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder12(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder13<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder13<>(types, getters);
        }
    }

    public static final class Builder13<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder13(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder14<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder14<>(types, getters);
        }
    }

    public static final class Builder14<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder14(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder15<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder15<>(types, getters);
        }
    }

    public static final class Builder15<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder15(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder16<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder16<>(types, getters);
        }
    }

    public static final class Builder16<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder16(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder17<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder17<>(types, getters);
        }
    }

    public static final class Builder17<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder17(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder18<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder18<>(types, getters);
        }
    }

    public static final class Builder18<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder18(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder19<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder19<>(types, getters);
        }
    }

    public static final class Builder19<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder19(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder20<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder20<>(types, getters);
        }
    }

    public static final class Builder20<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder20(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder21<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder21<>(types, getters);
        }
    }

    public static final class Builder21<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder21(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder22<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder22<>(types, getters);
        }
    }

    public static final class Builder22<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder22(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder23<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder23<>(types, getters);
        }
    }

    public static final class Builder23<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder23(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder24<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder24<>(types, getters);
        }
    }

    public static final class Builder24<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder24(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder25<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder25<>(types, getters);
        }
    }

    public static final class Builder25<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder25(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder26<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder26<>(types, getters);
        }
    }

    public static final class Builder26<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder26(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder27<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder27<>(types, getters);
        }
    }

    public static final class Builder27<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder27(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder28<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder28<>(types, getters);
        }
    }

    public static final class Builder28<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder28(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder29<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder29<>(types, getters);
        }
    }

    public static final class Builder29<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder29(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder30<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder30<>(types, getters);
        }
    }

    public static final class Builder30<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder30(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder31<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder31<>(types, getters);
        }
    }

    public static final class Builder31<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder31(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function31<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder32<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder32<>(types, getters);
        }
    }

    public static final class Builder32<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder32(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function32<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder33<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder33<>(types, getters);
        }
    }

    public static final class Builder33<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder33(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function33<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder34<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder34<>(types, getters);
        }
    }

    public static final class Builder34<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder34(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function34<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder35<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder35<>(types, getters);
        }
    }

    public static final class Builder35<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder35(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function35<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder36<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder36<>(types, getters);
        }
    }

    public static final class Builder36<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder36(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function36<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder37<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder37<>(types, getters);
        }
    }

    public static final class Builder37<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder37(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function37<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder38<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder38<>(types, getters);
        }
    }

    public static final class Builder38<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder38(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function38<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder39<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder39<>(types, getters);
        }
    }

    public static final class Builder39<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder39(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function39<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder40<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder40<>(types, getters);
        }
    }

    public static final class Builder40<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder40(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function40<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder41<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder41<>(types, getters);
        }
    }

    public static final class Builder41<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder41(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function41<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder42<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder42<>(types, getters);
        }
    }

    public static final class Builder42<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder42(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function42<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder43<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder43<>(types, getters);
        }
    }

    public static final class Builder43<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder43(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function43<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder44<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder44<>(types, getters);
        }
    }

    public static final class Builder44<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder44(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function44<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder45<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder45<>(types, getters);
        }
    }

    public static final class Builder45<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder45(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function45<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder46<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder46<>(types, getters);
        }
    }

    public static final class Builder46<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder46(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function46<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder47<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder47<>(types, getters);
        }
    }

    public static final class Builder47<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder47(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function47<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder48<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder48<>(types, getters);
        }
    }

    public static final class Builder48<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder48(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function48<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder49<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder49<>(types, getters);
        }
    }

    public static final class Builder49<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder49(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function49<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder50<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder50<>(types, getters);
        }
    }

    public static final class Builder50<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder50(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function50<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder51<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder51<>(types, getters);
        }
    }

    public static final class Builder51<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder51(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function51<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder52<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder52<>(types, getters);
        }
    }

    public static final class Builder52<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder52(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function52<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder53<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder53<>(types, getters);
        }
    }

    public static final class Builder53<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder53(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function53<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder54<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder54<>(types, getters);
        }
    }

    public static final class Builder54<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder54(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function54<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder55<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder55<>(types, getters);
        }
    }

    public static final class Builder55<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder55(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function55<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder56<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder56<>(types, getters);
        }
    }

    public static final class Builder56<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder56(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function56<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder57<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder57<>(types, getters);
        }
    }

    public static final class Builder57<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder57(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function57<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder58<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder58<>(types, getters);
        }
    }

    public static final class Builder58<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder58(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function58<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder59<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder59<>(types, getters);
        }
    }

    public static final class Builder59<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder59(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function59<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder60<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder60<>(types, getters);
        }
    }

    public static final class Builder60<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder60(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function60<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder61<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder61<>(types, getters);
        }
    }

    public static final class Builder61<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder61(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function61<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder62<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder62<>(types, getters);
        }
    }

    public static final class Builder62<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder62(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function62<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder63<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder63<>(types, getters);
        }
    }

    public static final class Builder63<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder63(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function63<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder64<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder64<>(types, getters);
        }
    }

    public static final class Builder64<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder64(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function64<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder65<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder65<>(types, getters);
        }
    }

    public static final class Builder65<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder65(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function65<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder66<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder66<>(types, getters);
        }
    }

    public static final class Builder66<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder66(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function66<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder67<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder67<>(types, getters);
        }
    }

    public static final class Builder67<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder67(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function67<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder68<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder68<>(types, getters);
        }
    }

    public static final class Builder68<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder68(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function68<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder69<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder69<>(types, getters);
        }
    }

    public static final class Builder69<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder69(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function69<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder70<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder70<>(types, getters);
        }
    }

    public static final class Builder70<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder70(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function70<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder71<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder71<>(types, getters);
        }
    }

    public static final class Builder71<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder71(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function71<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder72<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder72<>(types, getters);
        }
    }

    public static final class Builder72<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder72(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function72<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder73<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder73<>(types, getters);
        }
    }

    public static final class Builder73<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder73(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function73<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder74<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder74<>(types, getters);
        }
    }

    public static final class Builder74<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder74(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function74<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder75<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder75<>(types, getters);
        }
    }

    public static final class Builder75<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder75(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function75<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder76<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder76<>(types, getters);
        }
    }

    public static final class Builder76<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder76(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function76<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder77<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder77<>(types, getters);
        }
    }

    public static final class Builder77<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder77(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function77<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder78<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder78<>(types, getters);
        }
    }

    public static final class Builder78<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder78(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function78<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder79<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder79<>(types, getters);
        }
    }

    public static final class Builder79<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder79(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function79<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder80<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder80<>(types, getters);
        }
    }

    public static final class Builder80<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder80(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function80<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder81<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder81<>(types, getters);
        }
    }

    public static final class Builder81<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder81(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function81<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder82<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder82<>(types, getters);
        }
    }

    public static final class Builder82<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder82(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function82<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder83<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder83<>(types, getters);
        }
    }

    public static final class Builder83<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder83(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function83<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder84<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder84<>(types, getters);
        }
    }

    public static final class Builder84<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder84(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function84<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder85<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder85<>(types, getters);
        }
    }

    public static final class Builder85<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder85(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function85<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder86<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder86<>(types, getters);
        }
    }

    public static final class Builder86<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder86(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function86<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder87<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder87<>(types, getters);
        }
    }

    public static final class Builder87<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder87(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function87<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder88<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder88<>(types, getters);
        }
    }

    public static final class Builder88<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder88(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function88<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder89<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder89<>(types, getters);
        }
    }

    public static final class Builder89<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder89(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function89<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder90<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder90<>(types, getters);
        }
    }

    public static final class Builder90<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder90(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function90<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder91<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder91<>(types, getters);
        }
    }

    public static final class Builder91<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder91(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function91<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder92<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder92<>(types, getters);
        }
    }

    public static final class Builder92<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder92(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function92<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder93<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder93<>(types, getters);
        }
    }

    public static final class Builder93<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder93(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function93<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder94<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder94<>(types, getters);
        }
    }

    public static final class Builder94<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder94(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function94<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder95<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder95<>(types, getters);
        }
    }

    public static final class Builder95<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder95(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function95<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder96<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder96<>(types, getters);
        }
    }

    public static final class Builder96<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder96(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function96<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder97<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder97<>(types, getters);
        }
    }

    public static final class Builder97<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder97(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function97<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder98<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder98<>(types, getters);
        }
    }

    public static final class Builder98<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder98(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function98<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
        public <F> Builder99<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
            types.add(type);
            getters.add(getter);
            return new Builder99<>(types, getters);
        }
    }

    public static final class Builder99<Row, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98> {
        private final java.util.List<DbType<?>> types;
        private final java.util.List<java.util.function.Function<Row, ?>> getters;

        Builder99(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
            this.types = types;
            this.getters = getters;
        }

        @SuppressWarnings("unchecked")
        public RowParser<Row> build(Functions.Function99<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, T30, T31, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T46, T47, T48, T49, T50, T51, T52, T53, T54, T55, T56, T57, T58, T59, T60, T61, T62, T63, T64, T65, T66, T67, T68, T69, T70, T71, T72, T73, T74, T75, T76, T77, T78, T79, T80, T81, T82, T83, T84, T85, T86, T87, T88, T89, T90, T91, T92, T93, T94, T95, T96, T97, T98, Row> decode) {
            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
            return new RowParser<>(
                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
                arr -> decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29], (T30) arr[30], (T31) arr[31], (T32) arr[32], (T33) arr[33], (T34) arr[34], (T35) arr[35], (T36) arr[36], (T37) arr[37], (T38) arr[38], (T39) arr[39], (T40) arr[40], (T41) arr[41], (T42) arr[42], (T43) arr[43], (T44) arr[44], (T45) arr[45], (T46) arr[46], (T47) arr[47], (T48) arr[48], (T49) arr[49], (T50) arr[50], (T51) arr[51], (T52) arr[52], (T53) arr[53], (T54) arr[54], (T55) arr[55], (T56) arr[56], (T57) arr[57], (T58) arr[58], (T59) arr[59], (T60) arr[60], (T61) arr[61], (T62) arr[62], (T63) arr[63], (T64) arr[64], (T65) arr[65], (T66) arr[66], (T67) arr[67], (T68) arr[68], (T69) arr[69], (T70) arr[70], (T71) arr[71], (T72) arr[72], (T73) arr[73], (T74) arr[74], (T75) arr[75], (T76) arr[76], (T77) arr[77], (T78) arr[78], (T79) arr[79], (T80) arr[80], (T81) arr[81], (T82) arr[82], (T83) arr[83], (T84) arr[84], (T85) arr[85], (T86) arr[86], (T87) arr[87], (T88) arr[88], (T89) arr[89], (T90) arr[90], (T91) arr[91], (T92) arr[92], (T93) arr[93], (T94) arr[94], (T95) arr[95], (T96) arr[96], (T97) arr[97], (T98) arr[98]),
                row -> {
                    Object[] result = new Object[capturedGetters.size()];
                    for (int i = 0; i < capturedGetters.size(); i++) {
                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
                    }
                    return result;
                });
        }
    }
}
