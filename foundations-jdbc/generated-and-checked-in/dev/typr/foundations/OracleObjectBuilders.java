package dev.typr.foundations;

import java.util.List;

/**
 * Type-safe builders for Oracle OBJECT types.
 * <p>
 * Usage:
 * <pre>{@code
 * OracleObject<Address> obj = OracleObjectBuilders.<Address>builder("ADDRESS_T")
 *     .field("STREET", OracleTypes.varchar2, Address::street)
 *     .field("CITY", OracleTypes.varchar2, Address::city)
 *     .build(Address::new);  // No casts needed!
 * }</pre>
 */
public final class OracleObjectBuilders {
    private OracleObjectBuilders() {}

    public static <A> Builder0<A> builder(String objectTypeName) {
        return new Builder0<>(objectTypeName);
    }

    public static final class Builder0<A> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes = new java.util.ArrayList<>();

        Builder0(String objectTypeName) {
            this.objectTypeName = objectTypeName;
        }

        public <F> Builder1<A, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder1<>(objectTypeName, attributes);
        }
    }

    public static final class Builder1<A, T0> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder1(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(java.util.function.Function<T0, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder2<A, T0, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder2<>(objectTypeName, attributes);
        }
    }

    public static final class Builder2<A, T0, T1> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder2(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function2<T0, T1, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder3<A, T0, T1, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder3<>(objectTypeName, attributes);
        }
    }

    public static final class Builder3<A, T0, T1, T2> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder3(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function3<T0, T1, T2, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder4<A, T0, T1, T2, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder4<>(objectTypeName, attributes);
        }
    }

    public static final class Builder4<A, T0, T1, T2, T3> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder4(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function4<T0, T1, T2, T3, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder5<A, T0, T1, T2, T3, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder5<>(objectTypeName, attributes);
        }
    }

    public static final class Builder5<A, T0, T1, T2, T3, T4> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder5(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function5<T0, T1, T2, T3, T4, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder6<A, T0, T1, T2, T3, T4, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder6<>(objectTypeName, attributes);
        }
    }

    public static final class Builder6<A, T0, T1, T2, T3, T4, T5> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder6(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function6<T0, T1, T2, T3, T4, T5, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder7<A, T0, T1, T2, T3, T4, T5, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder7<>(objectTypeName, attributes);
        }
    }

    public static final class Builder7<A, T0, T1, T2, T3, T4, T5, T6> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder7(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function7<T0, T1, T2, T3, T4, T5, T6, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder8<A, T0, T1, T2, T3, T4, T5, T6, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder8<>(objectTypeName, attributes);
        }
    }

    public static final class Builder8<A, T0, T1, T2, T3, T4, T5, T6, T7> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder8(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function8<T0, T1, T2, T3, T4, T5, T6, T7, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder9<>(objectTypeName, attributes);
        }
    }

    public static final class Builder9<A, T0, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder9(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder10<>(objectTypeName, attributes);
        }
    }

    public static final class Builder10<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder10(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder11<>(objectTypeName, attributes);
        }
    }

    public static final class Builder11<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder11(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function11<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder12<>(objectTypeName, attributes);
        }
    }

    public static final class Builder12<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder12(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function12<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder13<>(objectTypeName, attributes);
        }
    }

    public static final class Builder13<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder13(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder14<>(objectTypeName, attributes);
        }
    }

    public static final class Builder14<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder14(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function14<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder15<>(objectTypeName, attributes);
        }
    }

    public static final class Builder15<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder15(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function15<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder16<>(objectTypeName, attributes);
        }
    }

    public static final class Builder16<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder16(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function16<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder17<>(objectTypeName, attributes);
        }
    }

    public static final class Builder17<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder17(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function17<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder18<>(objectTypeName, attributes);
        }
    }

    public static final class Builder18<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder18(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function18<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder19<>(objectTypeName, attributes);
        }
    }

    public static final class Builder19<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder19(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function19<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder20<>(objectTypeName, attributes);
        }
    }

    public static final class Builder20<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder20(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function20<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder21<>(objectTypeName, attributes);
        }
    }

    public static final class Builder21<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder21(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function21<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder22<>(objectTypeName, attributes);
        }
    }

    public static final class Builder22<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder22(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function22<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder23<>(objectTypeName, attributes);
        }
    }

    public static final class Builder23<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder23(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function23<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder24<>(objectTypeName, attributes);
        }
    }

    public static final class Builder24<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder24(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function24<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder25<>(objectTypeName, attributes);
        }
    }

    public static final class Builder25<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder25(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function25<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder26<>(objectTypeName, attributes);
        }
    }

    public static final class Builder26<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder26(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function26<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder27<>(objectTypeName, attributes);
        }
    }

    public static final class Builder27<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder27(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function27<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder28<>(objectTypeName, attributes);
        }
    }

    public static final class Builder28<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder28(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function28<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder29<>(objectTypeName, attributes);
        }
    }

    public static final class Builder29<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder29(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function29<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
        public <F> Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
            attributes.add(new OracleObject.Attribute<>(name, type, getter));
            return new Builder30<>(objectTypeName, attributes);
        }
    }

    public static final class Builder30<A, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29> {
        private final String objectTypeName;
        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;

        Builder30(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
            this.objectTypeName = objectTypeName;
            this.attributes = attributes;
        }

        @SuppressWarnings("unchecked")
        public OracleObject<A> build(Functions.Function30<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, T27, T28, T29, A> decode) {
            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
                try {
                    return decode.apply((T0) arr[0], (T1) arr[1], (T2) arr[2], (T3) arr[3], (T4) arr[4], (T5) arr[5], (T6) arr[6], (T7) arr[7], (T8) arr[8], (T9) arr[9], (T10) arr[10], (T11) arr[11], (T12) arr[12], (T13) arr[13], (T14) arr[14], (T15) arr[15], (T16) arr[16], (T17) arr[17], (T18) arr[18], (T19) arr[19], (T20) arr[20], (T21) arr[21], (T22) arr[22], (T23) arr[23], (T24) arr[24], (T25) arr[25], (T26) arr[26], (T27) arr[27], (T28) arr[28], (T29) arr[29]);
                } catch (ClassCastException e) {
                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    static <A> OracleObject<A> buildObject(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes, OracleObject.ObjectReader<A> reader) {
        OracleObject.ObjectWriter<A> writer = value -> {
            Object[] result = new Object[attributes.size()];
            for (int i = 0; i < attributes.size(); i++) {
                OracleObject.Attribute<A, Object> attr = (OracleObject.Attribute<A, Object>) attributes.get(i);
                result[i] = attr.getter().apply(value);
            }
            return result;
        };

        OracleTypename.ObjectOf<A> typename = OracleTypename.objectOf(objectTypeName);
        return new OracleObject<>(typename, List.copyOf(attributes), reader, writer);
    }
}
