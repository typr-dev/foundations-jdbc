#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.4

import java.nio.file.{Files, Path}

val N = 100
val PROC_N = 11
val STRUCT_N = 31
val baseDir = Path.of(sys.props.getOrElse("user.dir", "."))
val outputDir = baseDir.resolve("foundations-jdbc/generated-and-checked-in/dev/typr/foundations")

def generateFunctions(): String = {
  val functions = 2
    .until(N)
    .map { n =>
      s"""|    @FunctionalInterface
          |    interface Function$n<${0.until(n).map(nn => s"T$nn").mkString(", ")}, R> {
          |        R apply(${0.until(n).map(nn => s"T$nn t$nn").mkString(", ")});
          |    }""".stripMargin
    }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Multi-arity function interfaces for type-safe builders.
      | * <p>
      | * Function2 through Function99 are defined here. For single-argument functions,
      | * use {@code java.util.function.Function}.
      | */
      |public interface Functions {
      |${functions.mkString("\n\n")}
      |}""".stripMargin
}

def generateTuples(): String = {
  val tupleInterfaces = 1.to(N).map { n =>
    val range = 0.until(n)
    val tparamsDecl = range.map(nn => s"T$nn").mkString(", ")
    val abstractMethods = range.map(nn => s"        T$nn _${nn + 1}();").mkString("\n")
    val asArrayBody = range.map(nn => s"_${nn + 1}()").mkString(", ")
    val implFields = range.map(nn => s"T$nn _${nn + 1}").mkString(", ")

    s"""    /**
       |     * Tuple with $n element${if (n > 1) "s" else ""}.
       |     * Use {@link Tuple#of} to create instances, or have your Row/ID records implement this interface.
       |     */
       |    non-sealed interface Tuple$n<$tparamsDecl> extends Tuple {
       |$abstractMethods
       |
       |        @Override
       |        default Object[] asArray() {
       |            return new Object[] { $asArrayBody };
       |        }
       |
       |        /** Default implementation record for Tuple$n. */
       |        record Impl<$tparamsDecl>($implFields) implements Tuple$n<$tparamsDecl> {}
       |    }""".stripMargin
  }

  val tupleOfMethods = 1.to(N).map { n =>
    val range = 0.until(n)
    val tparamsDecl = range.map(nn => s"T$nn").mkString(", ")
    val ofParams = range.map(nn => s"T$nn v$nn").mkString(", ")
    val ofArgs = range.map(nn => s"v$nn").mkString(", ")

    s"""    /** Create a Tuple$n with the given values. */
       |    static <$tparamsDecl> Tuple$n<$tparamsDecl> of($ofParams) {
       |        return new Tuple$n.Impl<>($ofArgs);
       |    }""".stripMargin
  }

  val createTupleCases = 1.to(N).map { n =>
    val range = 0.until(n)
    val args = range.map(nn => s"values[$nn]").mkString(", ")
    s"            case $n -> Tuple.of($args);"
  }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Tuple value types for the DSL.
      | * <p>
      | * Use {@link #of} factory methods to create tuple instances.
      | * These are used as Row types in queries.
      | */
      |public sealed interface Tuple {
      |    /** Returns all elements as an Object array. */
      |    Object[] asArray();
      |
      |    // Tuple value types (interfaces with Impl records)
      |${tupleInterfaces.mkString("\n\n")}
      |
      |    // Factory methods for Tuple values
      |${tupleOfMethods.mkString("\n\n")}
      |
      |    /**
      |     * Create a Tuple of the appropriate arity from an array of values.
      |     * @param values array of values (length 1-$N)
      |     * @return a Tuple of the appropriate arity
      |     * @throws IllegalArgumentException if values.length is 0 or greater than $N
      |     */
      |    @SuppressWarnings("unchecked")
      |    static Tuple createTuple(Object[] values) {
      |        return switch (values.length) {
      |${createTupleCases.mkString("\n")}
      |            default -> throw new IllegalArgumentException("Unsupported tuple arity: " + values.length);
      |        };
      |    }
      |}
      |""".stripMargin
}

def generateRowCodecBuilders(): String = {
  val maxArity = N - 1  // N is 100, so 99 fields max (matching Functions.FunctionN)

  val builder0 = s"""|    public static final class Builder0<Row> {
                     |        private final java.util.List<DbType<?>> types = new java.util.ArrayList<>();
                     |        private final java.util.List<java.util.function.Function<Row, ?>> getters = new java.util.ArrayList<>();
                     |
                     |        Builder0() {}
                     |
                     |        public <F> Builder1<Row, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
                     |            types.add(type);
                     |            getters.add(getter);
                     |            return new Builder1<>(types, getters);
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val functionType = if (n == 1) s"java.util.function.Function<T0, Row>" else s"Functions.Function$n<$tparams, Row>"
    val decodeArgs = range.map(i => s"(T$i) arr[$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        public <F> Builder${n + 1}<Row, $nextTparams, F> field(DbType<F> type, java.util.function.Function<Row, F> getter) {
          |            types.add(type);
          |            getters.add(getter);
          |            return new Builder${n + 1}<>(types, getters);
          |        }""".stripMargin
    } else ""

    s"""|    public static final class Builder$n<Row, $tparams> {
        |        private final java.util.List<DbType<?>> types;
        |        private final java.util.List<java.util.function.Function<Row, ?>> getters;
        |
        |        Builder$n(java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
        |            this.types = types;
        |            this.getters = getters;
        |        }
        |
        |        @SuppressWarnings("unchecked")
        |        public RowCodec<Row> build($functionType decode) {
        |            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
        |            return RowCodec.create(
        |                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
        |                arr -> decode.apply($decodeArgs),
        |                row -> {
        |                    Object[] result = new Object[capturedGetters.size()];
        |                    for (int i = 0; i < capturedGetters.size(); i++) {
        |                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
        |                    }
        |                    return result;
        |                });
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Type-safe builders for RowCodec.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * RowCodec<Product> parser = RowCodecBuilders.<Product>builder()
      | *     .field(PgTypes.int4, Product::id)
      | *     .field(PgTypes.text, Product::name)
      | *     .field(PgTypes.numeric, Product::price)
      | *     .build(Product::new);
      | * }</pre>
      | */
      |public final class RowCodecBuilders {
      |    private RowCodecBuilders() {}
      |
      |    public static <Row> Builder0<Row> builder() {
      |        return new Builder0<>();
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateNamedRowCodecBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|    public static final class Builder0<Row> {
                     |        private final java.util.List<String> names = new java.util.ArrayList<>();
                     |        private final java.util.List<DbType<?>> types = new java.util.ArrayList<>();
                     |        private final java.util.List<java.util.function.Function<Row, ?>> getters = new java.util.ArrayList<>();
                     |
                     |        Builder0() {}
                     |
                     |        public <F> Builder1<Row, F> field(String name, DbType<F> type, java.util.function.Function<Row, F> getter) {
                     |            names.add(name);
                     |            types.add(type);
                     |            getters.add(getter);
                     |            return new Builder1<>(names, types, getters);
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val functionType = if (n == 1) s"java.util.function.Function<T0, Row>" else s"Functions.Function$n<$tparams, Row>"
    val decodeArgs = range.map(i => s"(T$i) arr[$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        public <F> Builder${n + 1}<Row, $nextTparams, F> field(String name, DbType<F> type, java.util.function.Function<Row, F> getter) {
          |            names.add(name);
          |            types.add(type);
          |            getters.add(getter);
          |            return new Builder${n + 1}<>(names, types, getters);
          |        }""".stripMargin
    } else ""

    s"""|    public static final class Builder$n<Row, $tparams> {
        |        private final java.util.List<String> names;
        |        private final java.util.List<DbType<?>> types;
        |        private final java.util.List<java.util.function.Function<Row, ?>> getters;
        |
        |        Builder$n(java.util.List<String> names, java.util.List<DbType<?>> types, java.util.List<java.util.function.Function<Row, ?>> getters) {
        |            this.names = names;
        |            this.types = types;
        |            this.getters = getters;
        |        }
        |
        |        @SuppressWarnings("unchecked")
        |        public RowCodecNamed<Row> build($functionType decode) {
        |            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
        |            return RowCodec.createNamed(
        |                java.util.List.copyOf(names),
        |                java.util.Collections.unmodifiableList(new java.util.ArrayList<>(types)),
        |                arr -> decode.apply($decodeArgs),
        |                row -> {
        |                    Object[] result = new Object[capturedGetters.size()];
        |                    for (int i = 0; i < capturedGetters.size(); i++) {
        |                        result[i] = ((java.util.function.Function<Row, Object>) capturedGetters.get(i)).apply(row);
        |                    }
        |                    return result;
        |                });
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Type-safe named builders for RowCodec.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * RowCodecNamed<Product> parser = RowCodecNamedBuilders.<Product>builder()
      | *     .field("id", PgTypes.int4, Product::id)
      | *     .field("name", PgTypes.text, Product::name)
      | *     .field("price", PgTypes.numeric, Product::price)
      | *     .build(Product::new);
      | * }</pre>
      | */
      |public final class RowCodecNamedBuilders {
      |    private RowCodecNamedBuilders() {}
      |
      |    public static <Row> Builder0<Row> builder() {
      |        return new Builder0<>();
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateDuckDbStructBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    public static final class Builder0<A> {
                     |        private final String structName;
                     |        private final java.util.List<DuckDbStruct.Field<A, ?>> fields = new java.util.ArrayList<>();
                     |
                     |        Builder0(String structName) {
                     |            this.structName = structName;
                     |        }
                     |
                     |        public <F> Builder1<A, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
                     |            fields.add(new DuckDbStruct.Field<>(name, type, getter));
                     |            return new Builder1<>(structName, fields);
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val functionType = if (n == 1) s"java.util.function.Function<T0, A>" else s"Functions.Function$n<$tparams, A>"
    val decodeArgs = range.map(i => s"(T$i) arr[$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        public <F> Builder${n + 1}<A, $nextTparams, F> field(String name, DuckDbType<F> type, java.util.function.Function<A, F> getter) {
          |            fields.add(new DuckDbStruct.Field<>(name, type, getter));
          |            return new Builder${n + 1}<>(structName, fields);
          |        }""".stripMargin
    } else ""

    s"""|    public static final class Builder$n<A, $tparams> {
        |        private final String structName;
        |        private final java.util.List<DuckDbStruct.Field<A, ?>> fields;
        |
        |        Builder$n(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields) {
        |            this.structName = structName;
        |            this.fields = fields;
        |        }
        |
        |        @SuppressWarnings("unchecked")
        |        public DuckDbStruct<A> build($functionType decode) {
        |            return DuckDbStructBuilders.buildStruct(structName, fields, arr -> {
        |                try {
        |                    return decode.apply($decodeArgs);
        |                } catch (ClassCastException e) {
        |                    throw new java.sql.SQLException("Type mismatch reading STRUCT field", e);
        |                }
        |            });
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |import dev.typr.foundations.data.JsonValue;
      |import java.util.LinkedHashMap;
      |import java.util.List;
      |
      |/**
      | * Type-safe builders for DuckDB STRUCT types.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * DuckDbStruct<Point> struct = DuckDbStructBuilders.<Point>builder("point")
      | *     .field("x", DuckDbTypes.double_, Point::x)
      | *     .field("y", DuckDbTypes.double_, Point::y)
      | *     .build(Point::new);  // No casts needed!
      | * }</pre>
      | */
      |public final class DuckDbStructBuilders {
      |    private DuckDbStructBuilders() {}
      |
      |    public static <A> Builder0<A> builder(String structName) {
      |        return new Builder0<>(structName);
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |
      |    @SuppressWarnings("unchecked")
      |    static <A> DuckDbStruct<A> buildStruct(String structName, java.util.List<DuckDbStruct.Field<A, ?>> fields, DuckDbStruct.StructReader<A> reader) {
      |        List<DuckDbTypename.StructOf.StructField> typenameFields =
      |            fields.stream()
      |                .map(f -> new DuckDbTypename.StructOf.StructField(f.name(), f.type().typename()))
      |                .toList();
      |
      |        DuckDbTypename.StructOf<A> typename = new DuckDbTypename.StructOf<>(structName, typenameFields);
      |
      |        DuckDbStruct.StructWriter<A> writer = structValue -> {
      |            Object[] values = new Object[fields.size()];
      |            for (int i = 0; i < fields.size(); i++) {
      |                values[i] = ((DuckDbStruct.Field<A, Object>) fields.get(i)).getter().apply(structValue);
      |            }
      |            return values;
      |        };
      |
      |        DuckDbJson<A> json = new DuckDbJson<>() {
      |            @Override
      |            public JsonValue toJson(A value) {
      |                LinkedHashMap<String, JsonValue> jsonFields = new LinkedHashMap<>();
      |                for (DuckDbStruct.Field<A, ?> field : fields) {
      |                    jsonFields.put(field.name(), fieldToJson(field, value));
      |                }
      |                return new JsonValue.JObject(jsonFields);
      |            }
      |
      |            @Override
      |            public A fromJson(JsonValue jsonValue) {
      |                if (jsonValue instanceof JsonValue.JObject obj) {
      |                    Object[] values = new Object[fields.size()];
      |                    for (int i = 0; i < fields.size(); i++) {
      |                        DuckDbStruct.Field<A, ?> field = fields.get(i);
      |                        JsonValue fieldJson = obj.fields().get(field.name());
      |                        values[i] = fieldFromJson(field, fieldJson);
      |                    }
      |                    try {
      |                        return reader.read(values);
      |                    } catch (java.sql.SQLException e) {
      |                        throw new RuntimeException("Failed to construct struct from JSON", e);
      |                    }
      |                }
      |                throw new IllegalArgumentException("Expected JSON object");
      |            }
      |
      |            @SuppressWarnings("unchecked")
      |            private <F> JsonValue fieldToJson(DuckDbStruct.Field<A, F> field, A structValue) {
      |                F value = field.getter().apply(structValue);
      |                if (value == null) return JsonValue.JNull.INSTANCE;
      |                return field.type().duckDbJson().toJson(value);
      |            }
      |
      |            @SuppressWarnings("unchecked")
      |            private <F> Object fieldFromJson(DuckDbStruct.Field<A, F> field, JsonValue jsonValue) {
      |                if (jsonValue == null || jsonValue instanceof JsonValue.JNull) return null;
      |                return field.type().duckDbJson().fromJson(jsonValue);
      |            }
      |        };
      |
      |        return new DuckDbStruct<>(typename, List.copyOf(fields), reader, writer, json);
      |    }
      |}
      |""".stripMargin
}

def generateOracleObjectBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    public static final class Builder0<A> {
                     |        private final String objectTypeName;
                     |        private final java.util.List<OracleObject.Attribute<A, ?>> attributes = new java.util.ArrayList<>();
                     |
                     |        Builder0(String objectTypeName) {
                     |            this.objectTypeName = objectTypeName;
                     |        }
                     |
                     |        public <F> Builder1<A, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
                     |            attributes.add(new OracleObject.Attribute<>(name, type, getter));
                     |            return new Builder1<>(objectTypeName, attributes);
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val functionType = if (n == 1) s"java.util.function.Function<T0, A>" else s"Functions.Function$n<$tparams, A>"
    val decodeArgs = range.map(i => s"(T$i) arr[$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        public <F> Builder${n + 1}<A, $nextTparams, F> field(String name, OracleType<F> type, java.util.function.Function<A, F> getter) {
          |            attributes.add(new OracleObject.Attribute<>(name, type, getter));
          |            return new Builder${n + 1}<>(objectTypeName, attributes);
          |        }""".stripMargin
    } else ""

    s"""|    public static final class Builder$n<A, $tparams> {
        |        private final String objectTypeName;
        |        private final java.util.List<OracleObject.Attribute<A, ?>> attributes;
        |
        |        Builder$n(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes) {
        |            this.objectTypeName = objectTypeName;
        |            this.attributes = attributes;
        |        }
        |
        |        @SuppressWarnings("unchecked")
        |        public OracleObject<A> build($functionType decode) {
        |            return OracleObjectBuilders.buildObject(objectTypeName, attributes, arr -> {
        |                try {
        |                    return decode.apply($decodeArgs);
        |                } catch (ClassCastException e) {
        |                    throw new java.sql.SQLException("Type mismatch reading OBJECT attribute", e);
        |                }
        |            });
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |import java.util.List;
      |
      |/**
      | * Type-safe builders for Oracle OBJECT types.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * OracleObject<Address> obj = OracleObjectBuilders.<Address>builder("ADDRESS_T")
      | *     .field("STREET", OracleTypes.varchar2, Address::street)
      | *     .field("CITY", OracleTypes.varchar2, Address::city)
      | *     .build(Address::new);  // No casts needed!
      | * }</pre>
      | */
      |public final class OracleObjectBuilders {
      |    private OracleObjectBuilders() {}
      |
      |    public static <A> Builder0<A> builder(String objectTypeName) {
      |        return new Builder0<>(objectTypeName);
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |
      |    @SuppressWarnings("unchecked")
      |    static <A> OracleObject<A> buildObject(String objectTypeName, java.util.List<OracleObject.Attribute<A, ?>> attributes, OracleObject.ObjectReader<A> reader) {
      |        OracleObject.ObjectWriter<A> writer = value -> {
      |            Object[] result = new Object[attributes.size()];
      |            for (int i = 0; i < attributes.size(); i++) {
      |                OracleObject.Attribute<A, Object> attr = (OracleObject.Attribute<A, Object>) attributes.get(i);
      |                result[i] = attr.getter().apply(value);
      |            }
      |            return result;
      |        };
      |
      |        OracleTypename.ObjectOf<A> typename = OracleTypename.objectOf(objectTypeName);
      |        return new OracleObject<>(typename, List.copyOf(attributes), reader, writer);
      |    }
      |}
      |""".stripMargin
}

def generatePgStructBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    public static final class Builder0<A> {
                     |        private final String typeName;
                     |        private final java.util.List<PgStruct.Field<A, ?>> fields = new java.util.ArrayList<>();
                     |
                     |        Builder0(String typeName) {
                     |            this.typeName = typeName;
                     |        }
                     |
                     |        public <F> Builder1<A, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
                     |            fields.add(new PgStruct.Field<>(name, type, getter));
                     |            return new Builder1<>(typeName, fields);
                     |        }
                     |
                     |        public <F> Builder1<A, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
                     |            return field(name, nestedStruct.asType(), getter);
                     |        }
                     |
                     |        public <F> Builder1<A, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
                     |            PgType<F> elementType = nestedStruct.asType();
                     |            PgType<F[]> arrayType = new PgType<>(
                     |                elementType.typename().array(),
                     |                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
                     |                elementType.write().array(elementType.typename()),
                     |                elementType.pgText().array(),
                     |                elementType.pgCompositeText().array(arrayFactory),
                     |                elementType.pgJson().array(arrayFactory),
                     |                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                     |                AnalysisOptions.EMPTY);
                     |            return field(name, arrayType, getter);
                     |        }
                     |
                     |        public <F> Builder1<A, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
                     |            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
                     |            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
                     |            @SuppressWarnings("unchecked")
                     |            Builder1<A, java.util.Optional<F>> result = (Builder1<A, java.util.Optional<F>>) (Object) new Builder1<>(typeName, fields);
                     |            return result;
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val functionType = if (n == 1) s"java.util.function.Function<T0, A>" else s"Functions.Function$n<$tparams, A>"
    val decodeArgs = range.map(i => s"(T$i) arr[$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        public <F> Builder${n + 1}<A, $nextTparams, F> field(String name, PgType<F> type, java.util.function.Function<A, F> getter) {
          |            fields.add(new PgStruct.Field<>(name, type, getter));
          |            return new Builder${n + 1}<>(typeName, fields);
          |        }
          |
          |        public <F> Builder${n + 1}<A, $nextTparams, F> nestedField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F> getter) {
          |            return field(name, nestedStruct.asType(), getter);
          |        }
          |
          |        public <F> Builder${n + 1}<A, $nextTparams, F[]> nestedArrayField(String name, PgStruct<F> nestedStruct, java.util.function.Function<A, F[]> getter, java.util.function.IntFunction<F[]> arrayFactory) {
          |            PgType<F> elementType = nestedStruct.asType();
          |            PgType<F[]> arrayType = new PgType<>(
          |                elementType.typename().array(),
          |                PgRead.of((rs, idx) -> { throw new UnsupportedOperationException("Direct JDBC read not supported for nested arrays"); }),
          |                elementType.write().array(elementType.typename()),
          |                elementType.pgText().array(),
          |                elementType.pgCompositeText().array(arrayFactory),
          |                elementType.pgJson().array(arrayFactory),
          |                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
          |                AnalysisOptions.EMPTY);
          |            return field(name, arrayType, getter);
          |        }
          |
          |        public <F> Builder${n + 1}<A, $nextTparams, java.util.Optional<F>> optField(String name, PgType<F> type, java.util.function.Function<A, java.util.Optional<F>> getter) {
          |            java.util.function.Function<A, F> unwrappingGetter = a -> getter.apply(a).orElse(null);
          |            fields.add(new PgStruct.Field<>(name, type, unwrappingGetter));
          |            @SuppressWarnings("unchecked")
          |            Builder${n + 1}<A, $nextTparams, java.util.Optional<F>> result = (Builder${n + 1}<A, $nextTparams, java.util.Optional<F>>) (Object) new Builder${n + 1}<>(typeName, fields);
          |            return result;
          |        }""".stripMargin
    } else ""

    s"""|    public static final class Builder$n<A, $tparams> {
        |        private final String typeName;
        |        private final java.util.List<PgStruct.Field<A, ?>> fields;
        |
        |        Builder$n(String typeName, java.util.List<PgStruct.Field<A, ?>> fields) {
        |            this.typeName = typeName;
        |            this.fields = fields;
        |        }
        |
        |        @SuppressWarnings("unchecked")
        |        public PgStruct<A> build($functionType decode) {
        |            return PgStructBuilders.buildStruct(typeName, fields, arr -> decode.apply($decodeArgs));
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |import dev.typr.foundations.data.JsonValue;
      |import java.util.LinkedHashMap;
      |import java.util.List;
      |
      |/**
      | * Type-safe builders for PostgreSQL composite types.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * PgStruct<Dimensions> pgStruct = PgStructBuilders.<Dimensions>builder("dimensions")
      | *     .field("width", PgTypes.float8, Dimensions::width)
      | *     .field("height", PgTypes.float8, Dimensions::height)
      | *     .field("depth", PgTypes.float8, Dimensions::depth)
      | *     .field("unit", PgTypes.varchar, Dimensions::unit)
      | *     .build(Dimensions::new);  // No casts needed!
      | * }</pre>
      | */
      |public final class PgStructBuilders {
      |    private PgStructBuilders() {}
      |
      |    public static <A> Builder0<A> builder(String typeName) {
      |        return new Builder0<>(typeName);
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |
      |    @SuppressWarnings("unchecked")
      |    private static <A> PgStruct<A> buildStruct(String typeName, java.util.List<PgStruct.Field<A, ?>> fields, PgStruct.StructReader<A> reader) {
      |        java.util.List<PgTypename.CompositeOf.CompositeField> typenameFields =
      |            fields.stream()
      |                .map(f -> new PgTypename.CompositeOf.CompositeField(f.name(), f.type().typename()))
      |                .toList();
      |
      |        PgTypename.CompositeOf<A> typename = new PgTypename.CompositeOf<>(typeName, typenameFields);
      |
      |        PgStruct.StructWriter<A> writer = structValue -> {
      |            Object[] values = new Object[fields.size()];
      |            for (int i = 0; i < fields.size(); i++) {
      |                values[i] = ((PgStruct.Field<A, Object>) fields.get(i)).getter().apply(structValue);
      |            }
      |            return values;
      |        };
      |
      |        PgJson<A> json = new PgJson<>() {
      |            @Override
      |            public JsonValue toJson(A value) {
      |                LinkedHashMap<String, JsonValue> jsonFields = new LinkedHashMap<>();
      |                for (PgStruct.Field<A, ?> field : fields) {
      |                    jsonFields.put(field.name(), fieldToJson(field, value));
      |                }
      |                return new JsonValue.JObject(jsonFields);
      |            }
      |
      |            @Override
      |            public A fromJson(JsonValue jsonValue) {
      |                if (jsonValue instanceof JsonValue.JObject obj) {
      |                    Object[] values = new Object[fields.size()];
      |                    for (int i = 0; i < fields.size(); i++) {
      |                        PgStruct.Field<A, ?> field = fields.get(i);
      |                        JsonValue fieldJson = obj.fields().get(field.name());
      |                        values[i] = fieldFromJson(field, fieldJson);
      |                    }
      |                    try {
      |                        return reader.read(values);
      |                    } catch (java.sql.SQLException e) {
      |                        throw new RuntimeException("Failed to construct struct from JSON", e);
      |                    }
      |                }
      |                throw new IllegalArgumentException("Expected JSON object");
      |            }
      |
      |            @SuppressWarnings("unchecked")
      |            private <F> JsonValue fieldToJson(PgStruct.Field<A, F> field, A structValue) {
      |                F value = field.getter().apply(structValue);
      |                if (value == null) return JsonValue.JNull.INSTANCE;
      |                return field.type().pgJson().toJson(value);
      |            }
      |
      |            @SuppressWarnings("unchecked")
      |            private <F> Object fieldFromJson(PgStruct.Field<A, F> field, JsonValue jsonValue) {
      |                if (jsonValue == null || jsonValue instanceof JsonValue.JNull) return null;
      |                return field.type().pgJson().fromJson(jsonValue);
      |            }
      |        };
      |
      |        return new PgStruct<>(typename, List.copyOf(fields), reader, writer, json);
      |    }
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// DbProcedure / DbFunction generators (max 10 inputs, max 10 outputs)
// ─────────────────────────────────────────────────────────────────────────────

def generateDbProcedure(): String = {
  val maxArity = PROC_N - 1 // 10

  // Helpers
  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def oParams(o: Int) = 0.until(o).map(n => s"O$n").toList
  def allTypeParams(i: Int, o: Int) = iParams(i) ++ oParams(o)
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"<${ps.mkString(", ")}>"
  def callArgs(i: Int) = 0.until(i).map(n => s"I$n i$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")
  def outType(o: Int): String = o match {
    case 0 => "Void"
    case 1 => "O0"
    case n => s"Tuple.Tuple$n<${oParams(n).mkString(", ")}>"
  }

  // Def interfaces: 11x11 matrix
  val defs = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tpDecl = typeParamDecl(allTypeParams(i, o))
    val retType = outType(o)
    s"""    /** Procedure definition with $i input(s) and $o output(s). */
       |    @FunctionalInterface
       |    public interface Def${i}_${o}$tpDecl {
       |        Operation<$retType> call(${ callArgs(i) });
       |    }""".stripMargin
  }

  // Builder classes: 11x11 matrix
  val builders = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tp = allTypeParams(i, o)
    val tpDecl = typeParamDecl(tp)
    val tpDiamond = if (tp.isEmpty) "" else "<>"

    // in method: only if i < maxArity
    val inMethod = if (i < maxArity) {
      val newI = s"I$i"
      val nextTp = allTypeParams(i + 1, o)
      s"""        public <$newI> Builder_${i + 1}_${o}${typeParamDecl(nextTp)} input(DbType<$newI> type) {
         |            params.add(ParamDef.input(type));
         |            return new Builder_${i + 1}_${o}<>(name, params);
         |        }""".stripMargin
    } else ""

    // out method: only if o < maxArity
    val outMethod = if (o < maxArity) {
      val newO = s"O$o"
      val nextTp = allTypeParams(i, o + 1)
      s"""        public <$newO> Builder_${i}_${o + 1}${typeParamDecl(nextTp)} out(DbType<$newO> type) {
         |            params.add(ParamDef.of(type, ParamDef.Mode.OUT));
         |            return new Builder_${i}_${o + 1}<>(name, params);
         |        }""".stripMargin
    } else ""

    // inout method: only if i < maxArity AND o < maxArity
    val inoutMethod = if (i < maxArity && o < maxArity) {
      // INOUT: X goes at position I{i} in inputs and O{o} in outputs
      val inoutTp = iParams(i) ::: List("X") ::: oParams(o) ::: List("X")
      s"""        public <X> Builder_${i + 1}_${o + 1}${typeParamDecl(inoutTp)} inout(DbType<X> type) {
         |            params.add(ParamDef.of(type, ParamDef.Mode.INOUT));
         |            return new Builder_${i + 1}_${o + 1}<>(name, params);
         |        }""".stripMargin
    } else ""

    val methods = List(inMethod, outMethod, inoutMethod).filter(_.nonEmpty).mkString("\n")
    val methodsBlock = if (methods.nonEmpty) s"$methods\n" else ""

    // build method
    val retType = outType(o)
    val lambdaArgs = if (i == 0) "()" else s"(${callArgNames(i)})"
    val delegateCall = s"delegate.call(${callArgNames(i)})"

    val buildBody = o match {
      case 0 =>
        s"""            Procedure<Void> delegate = Procedure.buildVoid(name, java.util.List.copyOf(params));
           |            return $lambdaArgs -> $delegateCall;""".stripMargin
      case 1 =>
        s"""            Procedure<O0> delegate = Procedure.buildSingleOut(name, java.util.List.copyOf(params));
           |            return $lambdaArgs -> $delegateCall;""".stripMargin
      case n =>
        val castArgs = 0.until(n).map(k => s"(O$k) values[$k]").mkString(", ")
        s"""            Procedure<$retType> delegate = Procedure.buildMultiOut(name, java.util.List.copyOf(params), values -> Tuple.of($castArgs));
           |            return $lambdaArgs -> $delegateCall;""".stripMargin
    }

    s"""    public static final class Builder_${i}_${o}$tpDecl {
       |        private final String name;
       |        private final java.util.List<ParamDef> params;
       |
       |        Builder_${i}_${o}(String name, java.util.List<ParamDef> params) {
       |            this.name = name;
       |            this.params = params;
       |        }
       |$methodsBlock
       |        @SuppressWarnings("unchecked")
       |        public Def${i}_${o}${typeParamDecl(allTypeParams(i, o))} build() {
       |$buildBody
       |        }
       |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Type-safe stored procedure definitions with fully typed inputs and outputs.
      | * <p>
      | * The builder tracks both input types (via {@code .input()}) and output types (via {@code .out()}/{@code .inout()}).
      | * The resulting interface has a {@code call()} method with typed parameters instead of varargs.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * // Procedure with typed inputs — compile-time checking!
      | * DbProcedure.Def1_2<Integer, String, String> getUser = DbProcedure.define("get_user_by_id")
      | *     .input(PgTypes.int4)
      | *     .out(PgTypes.text)
      | *     .out(PgTypes.text)
      | *     .build();
      | * Tuple.Tuple2<String, String> result = getUser.call(42).transact(tx);  // Integer enforced!
      | * // getUser.call("wrong");  // COMPILE ERROR - String not Integer
      | *
      | * // Void procedure (no outputs)
      | * DbProcedure.Def1_0<String> auditLog = DbProcedure.define("audit_log")
      | *     .input(PgTypes.text)
      | *     .build();
      | *
      | * // INOUT — value goes in and comes back modified
      | * DbProcedure.Def2_1<String, BigDecimal, BigDecimal> applyDiscount = DbProcedure.define("apply_discount")
      | *     .input(PgTypes.text)
      | *     .inout(PgTypes.numeric)
      | *     .build();
      | * BigDecimal finalPrice = applyDiscount.call("SAVE20", price).transact(tx);
      | * }</pre>
      | *
      | * @see DbFunction for stored functions (single return value via SELECT)
      | */
      |public final class DbProcedure {
      |    private DbProcedure() {}
      |
      |    /** Start defining a stored procedure. */
      |    public static Builder_0_0 define(String name) {
      |        return new Builder_0_0(name, new java.util.ArrayList<>());
      |    }
      |
      |    // ─────────────────────────────────────────────────────────────────────────────
      |    // Procedure definition interfaces (${PROC_N * PROC_N} total: ${PROC_N}×${PROC_N} matrix of input×output arities)
      |    // ─────────────────────────────────────────────────────────────────────────────
      |
      |${defs.mkString("\n\n")}
      |
      |    // ─────────────────────────────────────────────────────────────────────────────
      |    // Procedure builders (${PROC_N * PROC_N} total: ${PROC_N}×${PROC_N} matrix)
      |    // ─────────────────────────────────────────────────────────────────────────────
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateDbFunction(): String = {
  val maxArity = PROC_N - 1 // 10

  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"<${ps.mkString(", ")}>"
  def callArgs(i: Int) = 0.until(i).map(n => s"I$n i$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")

  // Def interfaces: 11 total
  val defs = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    s"""    /** Function definition with $i input(s). */
       |    @FunctionalInterface
       |    public interface Def$i${typeParamDecl(tp)} {
       |        Operation<R> call(${callArgs(i)});
       |    }""".stripMargin
  }

  // Builder classes: 11 total
  val builders = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    val tpDecl = typeParamDecl(tp)

    val inMethod = if (i < maxArity) {
      val newI = s"I$i"
      val nextTp = iParams(i + 1) ::: List("R")
      s"""        public <$newI> Builder_${i + 1}${typeParamDecl(nextTp)} input(DbType<$newI> type) {
         |            inParams.add(ParamDef.input(type));
         |            return new Builder_${i + 1}<>(name, inParams, returnType);
         |        }
         |""".stripMargin
    } else ""

    val lambdaArgs = if (i == 0) "()" else s"(${callArgNames(i)})"
    val delegateCall = s"delegate.call(${callArgNames(i)})"

    s"""    public static final class Builder_$i$tpDecl {
       |        private final String name;
       |        private final java.util.List<ParamDef> inParams;
       |        private final DbType<R> returnType;
       |
       |        Builder_$i(String name, java.util.List<ParamDef> inParams, DbType<R> returnType) {
       |            this.name = name;
       |            this.inParams = inParams;
       |            this.returnType = returnType;
       |        }
       |$inMethod
       |        public Def$i$tpDecl build() {
       |            Procedure<R> delegate = Procedure.buildFunction(name, java.util.List.copyOf(inParams), returnType);
       |            return $lambdaArgs -> $delegateCall;
       |        }
       |    }""".stripMargin
  }

  s"""|package dev.typr.foundations;
      |
      |/**
      | * Type-safe stored function definitions with fully typed inputs.
      | * <p>
      | * The builder tracks input types (via {@code .input()}). The resulting interface has a
      | * {@code call()} method with typed parameters instead of varargs.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * // Function with typed inputs — compile-time checking!
      | * DbFunction.Def2<BigDecimal, String, BigDecimal> calcTax = DbFunction.define("calculate_tax", PgTypes.numeric)
      | *     .input(PgTypes.numeric)
      | *     .input(PgTypes.text)
      | *     .build();
      | * BigDecimal tax = calcTax.call(amount, "US").transact(tx);  // Types enforced!
      | * // calcTax.call("wrong", 42);  // COMPILE ERROR
      | *
      | * // Zero-argument function
      | * DbFunction.Def0<java.time.LocalDateTime> now = DbFunction.define("now", PgTypes.timestamp)
      | *     .build();
      | * LocalDateTime serverTime = now.call().transact(tx);
      | * }</pre>
      | *
      | * @see DbProcedure for stored procedures (with OUT/INOUT parameters)
      | */
      |public final class DbFunction {
      |    private DbFunction() {}
      |
      |    /** Start defining a stored function (single return value, uses SELECT). */
      |    public static <R> Builder_0<R> define(String name, DbType<R> returnType) {
      |        return new Builder_0<>(name, new java.util.ArrayList<>(), returnType);
      |    }
      |
      |    // ─────────────────────────────────────────────────────────────────────────────
      |    // Function definition interfaces (${PROC_N} total: 0-${maxArity} inputs)
      |    // ─────────────────────────────────────────────────────────────────────────────
      |
      |${defs.mkString("\n\n")}
      |
      |    // ─────────────────────────────────────────────────────────────────────────────
      |    // Function builders (${PROC_N} total: 0-${maxArity} inputs)
      |    // ─────────────────────────────────────────────────────────────────────────────
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// ParamBuilders / Template generators
// ─────────────────────────────────────────────────────────────────────────────

def generateParamBuilders(): String = {
  val maxArity = PROC_N - 1 // 10

  val paramBuilders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")
    val fields = range.map(i => s"    private final DbType<P$i> p${i}Type;").mkString("\n")
    val ctorParams = range.map(i => s"DbType<P$i> p${i}Type").mkString(",\n        ")
    val ctorAssignments = range.map(i => s"      this.p${i}Type = p${i}Type;").mkString("\n")
    val typeFieldArgs = range.map(i => s"p${i}Type").mkString(", ")

    val paramTypeMethod = if (n < maxArity) {
      s"""
    public <P$n> ParamBuilder${n + 1}<$tparams, P$n> param(DbType<P$n> type) {
      return new ParamBuilder${n + 1}<>(fragment.append(new Fragment.Param<>(type)), $typeFieldArgs, type);
    }"""
    } else ""

    // Generate optionally overloads (only if n+1 <= maxArity)
    val optionallyMethods = if (n < maxArity) {
      val nextN = n + 1

      // 0-param optionally (boolean flag)
      val boolOptionally = s"""
    public ParamBuilder$nextN<$tparams, Boolean> optionally(Fragment inner) {
      int paramCount = Fragment.countParams(inner);
      if (paramCount != 0) throw new IllegalArgumentException(
          "optionally(Fragment) requires 0 inner params, got " + paramCount + ". Use optionally(ParamBuilder) for parameterized fragments.");
      return new ParamBuilder$nextN<>(fragment.append(new Fragment.Optionally(inner, 0)), $typeFieldArgs, null);
    }"""

      // 1-param optionally
      val opt1 = s"""
    @SuppressWarnings("unchecked")
    public <A> ParamBuilder$nextN<$tparams, java.util.Optional<A>> optionally(ParamBuilder1<A> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder$nextN<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), $typeFieldArgs, null);
    }"""

      // 2-param optionally
      val opt2 = s"""
    @SuppressWarnings("unchecked")
    public <A, B> ParamBuilder$nextN<$tparams, java.util.Optional<Tuple.Tuple2<A, B>>> optionally(ParamBuilder2<A, B> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder$nextN<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), $typeFieldArgs, null);
    }"""

      // 3-param optionally
      val opt3 = s"""
    @SuppressWarnings("unchecked")
    public <A, B, C> ParamBuilder$nextN<$tparams, java.util.Optional<Tuple.Tuple3<A, B, C>>> optionally(ParamBuilder3<A, B, C> builder) {
      Fragment inner = builder.done();
      return new ParamBuilder$nextN<>(fragment.append(new Fragment.Optionally(inner, Fragment.countParams(inner))), $typeFieldArgs, null);
    }"""

      boolOptionally + opt1 + opt2 + opt3
    } else ""

    val queryTypeFields = range.map(i => s"p${i}Type").mkString(", ")
    val updateTypeFields = range.map(i => s"p${i}Type").mkString(", ")

    s"""  public static class ParamBuilder$n<$tparams> {
    private final Fragment fragment;
$fields

    ParamBuilder$n(
        Fragment fragment,
        $ctorParams) {
      this.fragment = fragment;
$ctorAssignments
    }

    public ParamBuilder$n<$tparams> append(String s) {
      return new ParamBuilder$n<>(fragment.append(Fragment.of(s)), $typeFieldArgs);
    }

    public <T> ParamBuilder$n<$tparams> value(DbType<T> type, T value) {
      return new ParamBuilder$n<>(fragment.append(Fragment.value(value, type)), $typeFieldArgs);
    }

    public ParamBuilder$n<$tparams> append(Fragment other) {
      return new ParamBuilder$n<>(fragment.append(other), $typeFieldArgs);
    }
$paramTypeMethod$optionallyMethods

    public <Out> Template.Query$n<$tparams, Out> query(ResultSetParser<Out> parser) {
      return new Template.Query$n<>(
          fragment, $queryTypeFields, parser);
    }

    public Template.Update$n<$tparams> update() {
      return new Template.Update$n<>(
          fragment, $updateTypeFields);
    }

    public Fragment done() {
      return fragment;
    }
  }"""
  }

  s"""|package dev.typr.foundations;
      |
      |public class ParamBuilders {
      |
      |${paramBuilders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateTemplate(): String = {
  val maxArity = PROC_N - 1 // 10

  def inputType(n: Int): String = {
    if (n == 1) "P0"
    else {
      val tparams = 0.until(n).map(i => s"P$i").mkString(", ")
      s"Tuple.Tuple$n<$tparams>"
    }
  }

  val permits = {
    val queryPermits = 1.to(maxArity).map(n => s"Template.Query$n")
    val updatePermits = 1.to(maxArity).map(n => s"Template.Update$n")
    (queryPermits ++ updatePermits :+ "Template.From").mkString(",\n        ")
  }

  val queryRecords = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")
    val allTparams = s"$tparams, Out"
    val fillArgs = range.map(i => s"(Object) p$i").mkString(", ")
    val onParams = range.map(i => s"P$i p$i").mkString(", ")

    val fromFnParams = range.map(i => s"java.util.function.Function<T, P$i> f$i").mkString(", ")
    val fromApplyArgs = range.map(i => s"f$i.apply(t)").mkString(", ")

    if (n == 1) {
      s"""  record Query1<P0, Out>(Fragment fragment, DbType<P0> p0Type, ResultSetParser<Out> parser)
      implements Template<P0, Out> {
    @Override
    public Operation.Query<Out> on(P0 p0) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment, java.util.List.of((Object) p0).iterator()), parser);
    }

    public <T> From<T, Out> from($fromFnParams) {
      return new From<>(this, t -> on($fromApplyArgs));
    }
  }"""
    } else {
      val inType = inputType(n)
      val extractArgs = range.map(i => s"in._${i + 1}()").mkString(",\n          ")

      s"""  record Query$n<$allTparams>(
      Fragment fragment,
${range.map(i => s"      DbType<P$i> p${i}Type,").mkString("\n")}
      ResultSetParser<Out> parser)
      implements Template<$inType, Out> {
    @Override
    public Operation.Query<Out> on($inType in) {
      return on($extractArgs);
    }

    public Operation.Query<Out> on($onParams) {
      return new Operation.Query<>(
          OptionallyResolver.resolve(fragment,
              java.util.List.of($fillArgs).iterator()),
          parser);
    }

    public <T> From<T, Out> from($fromFnParams) {
      return new From<>(this, t -> on($fromApplyArgs));
    }
  }"""
    }
  }

  val updateRecords = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")
    val fillArgs = range.map(i => s"(Object) p$i").mkString(", ")
    val onParams = range.map(i => s"P$i p$i").mkString(", ")

    val fromFnParams = range.map(i => s"java.util.function.Function<T, P$i> f$i").mkString(", ")
    val fromApplyArgs = range.map(i => s"f$i.apply(t)").mkString(", ")

    if (n == 1) {
      s"""  record Update1<P0>(Fragment fragment, DbType<P0> p0Type)
      implements Template<P0, Integer> {
    @Override
    public Operation.Update on(P0 p0) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment, java.util.List.of((Object) p0).iterator()));
    }

    public <T> From<T, Integer> from($fromFnParams) {
      return new From<>(this, t -> on($fromApplyArgs));
    }
  }"""
    } else {
      val inType = inputType(n)
      val extractArgs = range.map(i => s"in._${i + 1}()").mkString(",\n          ")

      s"""  record Update$n<$tparams>(
      Fragment fragment,
${range.map(i => s"      DbType<P$i> p${i}Type${if (i < n - 1) "," else ")"}").mkString("\n")}
      implements Template<$inType, Integer> {
    @Override
    public Operation.Update on($inType in) {
      return on($extractArgs);
    }

    public Operation.Update on($onParams) {
      return new Operation.Update(
          OptionallyResolver.resolve(fragment,
              java.util.List.of($fillArgs).iterator()));
    }

    public <T> From<T, Integer> from($fromFnParams) {
      return new From<>(this, t -> on($fromApplyArgs));
    }
  }"""
    }
  }

  s"""|package dev.typr.foundations;
      |
      |public sealed interface Template<In, Out> extends Analyzable
      |    permits $permits {
      |
      |  Operation<Out> on(In in);
      |
      |  Fragment fragment();
      |
      |${queryRecords.mkString("\n\n")}
      |
      |${updateRecords.mkString("\n\n")}
      |
      |  record From<T, Out>(
      |      Template<?, Out> inner,
      |      java.util.function.Function<T, ? extends Operation<Out>> resolver)
      |      implements Template<T, Out> {
      |    @Override
      |    public Operation<Out> on(T t) {
      |      return resolver.apply(t);
      |    }
      |
      |    @Override
      |    public Fragment fragment() {
      |      return inner.fragment();
      |    }
      |  }
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// InstrumentedConnection generator
// ─────────────────────────────────────────────────────────────────────────────

def generateInstrumentedConnection(): String = {
  // Each entry: (returnType, methodName, params, throwsClause)
  // params is a string like "int a, String b"
  // throwsStr: "" for none, "SQLException" for standard, or custom like "SQLClientInfoException"
  case class Method(ret: String, name: String, params: String, throwsStr: String) {
    def paramNames: String = {
      if (params.isEmpty) return ""
      // Split on commas that are not inside angle brackets
      val parts = new scala.collection.mutable.ArrayBuffer[String]()
      var depth = 0
      var start = 0
      for (i <- params.indices) {
        params.charAt(i) match {
          case '<' => depth += 1
          case '>' => depth -= 1
          case ',' if depth == 0 =>
            parts += params.substring(start, i).trim
            start = i + 1
          case _ =>
        }
      }
      parts += params.substring(start).trim
      parts.map(_.split("\\s+").last).mkString(", ")
    }
    def delegation: String = {
      val call = s"delegate.$name(${paramNames})"
      val body = if (ret == "void") s"$call;" else s"return $call;"
      val throwsPart = if (throwsStr.isEmpty) "" else s" throws $throwsStr"
      s"""    @Override
         |    public $ret $name($params)$throwsPart {
         |        $body
         |    }""".stripMargin
    }
  }

  val methods = List(
    // Statement creation
    Method("Statement", "createStatement", "", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql", "SQLException"),
    Method("CallableStatement", "prepareCall", "String sql", "SQLException"),
    Method("String", "nativeSQL", "String sql", "SQLException"),
    // Auto-commit / commit / rollback
    Method("void", "setAutoCommit", "boolean autoCommit", "SQLException"),
    Method("boolean", "getAutoCommit", "", "SQLException"),
    Method("void", "commit", "", "SQLException"),
    Method("void", "rollback", "", "SQLException"),
    Method("void", "close", "", "SQLException"),
    Method("boolean", "isClosed", "", "SQLException"),
    // Metadata
    Method("DatabaseMetaData", "getMetaData", "", "SQLException"),
    Method("void", "setReadOnly", "boolean readOnly", "SQLException"),
    Method("boolean", "isReadOnly", "", "SQLException"),
    Method("void", "setCatalog", "String catalog", "SQLException"),
    Method("String", "getCatalog", "", "SQLException"),
    Method("void", "setTransactionIsolation", "int level", "SQLException"),
    Method("int", "getTransactionIsolation", "", "SQLException"),
    Method("SQLWarning", "getWarnings", "", "SQLException"),
    Method("void", "clearWarnings", "", "SQLException"),
    // Statement with result set type/concurrency
    Method("Statement", "createStatement", "int resultSetType, int resultSetConcurrency", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql, int resultSetType, int resultSetConcurrency", "SQLException"),
    Method("CallableStatement", "prepareCall", "String sql, int resultSetType, int resultSetConcurrency", "SQLException"),
    // Type map
    Method("java.util.Map<String, Class<?>>", "getTypeMap", "", "SQLException"),
    Method("void", "setTypeMap", "java.util.Map<String, Class<?>> map", "SQLException"),
    // Holdability
    Method("void", "setHoldability", "int holdability", "SQLException"),
    Method("int", "getHoldability", "", "SQLException"),
    // Savepoints
    Method("Savepoint", "setSavepoint", "", "SQLException"),
    Method("Savepoint", "setSavepoint", "String name", "SQLException"),
    Method("void", "rollback", "Savepoint savepoint", "SQLException"),
    Method("void", "releaseSavepoint", "Savepoint savepoint", "SQLException"),
    // Statement with holdability
    Method("Statement", "createStatement", "int resultSetType, int resultSetConcurrency, int resultSetHoldability", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability", "SQLException"),
    Method("CallableStatement", "prepareCall", "String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql, int autoGeneratedKeys", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql, int[] columnIndexes", "SQLException"),
    Method("PreparedStatement", "prepareStatement", "String sql, String[] columnNames", "SQLException"),
    // LOB creation
    Method("Clob", "createClob", "", "SQLException"),
    Method("Blob", "createBlob", "", "SQLException"),
    Method("NClob", "createNClob", "", "SQLException"),
    Method("SQLXML", "createSQLXML", "", "SQLException"),
    // Validity
    Method("boolean", "isValid", "int timeout", "SQLException"),
    // Client info
    Method("void", "setClientInfo", "String name, String value", "SQLClientInfoException"),
    Method("void", "setClientInfo", "java.util.Properties properties", "SQLClientInfoException"),
    Method("String", "getClientInfo", "String name", "SQLException"),
    Method("java.util.Properties", "getClientInfo", "", "SQLException"),
    // Array/Struct creation
    Method("Array", "createArrayOf", "String typeName, Object[] elements", "SQLException"),
    Method("Struct", "createStruct", "String typeName, Object[] attributes", "SQLException"),
    // Schema
    Method("void", "setSchema", "String schema", "SQLException"),
    Method("String", "getSchema", "", "SQLException"),
    // Abort / timeout
    Method("void", "abort", "java.util.concurrent.Executor executor", "SQLException"),
    Method("void", "setNetworkTimeout", "java.util.concurrent.Executor executor, int milliseconds", "SQLException"),
    Method("int", "getNetworkTimeout", "", "SQLException"),
  )

  val delegations = methods.map(_.delegation).mkString("\n\n")

  s"""|package dev.typr.foundations;
      |
      |import java.sql.*;
      |import java.time.Duration;
      |import java.util.concurrent.atomic.AtomicInteger;
      |import org.jetbrains.annotations.Nullable;
      |
      |/**
      | * A Connection wrapper that carries query listener, name, and timeout metadata.
      | * Used internally by the Transactor when a QueryListener is configured.
      | * <p>
      | * All Connection methods delegate to the underlying connection. The wrapper
      | * adds no overhead beyond the extra field storage.
      | */
      |final class InstrumentedConnection implements Connection {
      |    private final Connection delegate;
      |    private final QueryListener listener;
      |    private @Nullable String queryName;
      |    private @Nullable Duration queryTimeout;
      |    private final AtomicInteger queryCounter = new AtomicInteger(0);
      |
      |    InstrumentedConnection(
      |            Connection delegate,
      |            QueryListener listener,
      |            @Nullable String queryName,
      |            @Nullable Duration queryTimeout) {
      |        this.delegate = delegate;
      |        this.listener = listener;
      |        this.queryName = queryName;
      |        this.queryTimeout = queryTimeout;
      |    }
      |
      |    Connection delegate() {
      |        return delegate;
      |    }
      |
      |    QueryListener queryListener() {
      |        return listener;
      |    }
      |
      |    @Nullable String queryName() {
      |        return queryName;
      |    }
      |
      |    void setQueryName(@Nullable String name) {
      |        this.queryName = name;
      |        this.queryCounter.set(0);
      |    }
      |
      |    @Nullable Duration queryTimeout() {
      |        return queryTimeout;
      |    }
      |
      |    void setQueryTimeout(@Nullable Duration timeout) {
      |        this.queryTimeout = timeout;
      |    }
      |
      |    @Nullable String currentQueryName() {
      |        return queryName;
      |    }
      |
      |    @Nullable String nextQueryName() {
      |        if (queryName == null) return null;
      |        int n = queryCounter.incrementAndGet();
      |        if (n == 1) return queryName;
      |        return queryName + "#" + n;
      |    }
      |
      |    void resetQueryCounter() {
      |        queryCounter.set(0);
      |    }
      |
      |    @Override
      |    @SuppressWarnings("unchecked")
      |    public <T> T unwrap(Class<T> iface) throws SQLException {
      |        if (iface.isInstance(this)) return (T) this;
      |        return delegate.unwrap(iface);
      |    }
      |
      |    @Override
      |    public boolean isWrapperFor(Class<?> iface) throws SQLException {
      |        if (iface.isInstance(this)) return true;
      |        return delegate.isWrapperFor(iface);
      |    }
      |
      |$delegations
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// Write files
// ─────────────────────────────────────────────────────────────────────────────

Files.createDirectories(outputDir)

val functionsContent = generateFunctions()
val functionsPath = outputDir.resolve("Functions.java")
Files.writeString(functionsPath, functionsContent)
println(s"Wrote ${functionsPath}")

// Delete old RowCodecs.java if it exists
val oldRowCodecsPath = outputDir.resolve("RowCodecs.java")
if (Files.exists(oldRowCodecsPath)) {
  Files.delete(oldRowCodecsPath)
  println(s"Deleted ${oldRowCodecsPath}")
}

val pgStructBuildersContent = generatePgStructBuilders()
val pgStructBuildersPath = outputDir.resolve("PgStructBuilders.java")
Files.writeString(pgStructBuildersPath, pgStructBuildersContent)
println(s"Wrote ${pgStructBuildersPath}")

val duckDbStructBuildersContent = generateDuckDbStructBuilders()
val duckDbStructBuildersPath = outputDir.resolve("DuckDbStructBuilders.java")
Files.writeString(duckDbStructBuildersPath, duckDbStructBuildersContent)
println(s"Wrote ${duckDbStructBuildersPath}")

val oracleObjectBuildersContent = generateOracleObjectBuilders()
val oracleObjectBuildersPath = outputDir.resolve("OracleObjectBuilders.java")
Files.writeString(oracleObjectBuildersPath, oracleObjectBuildersContent)
println(s"Wrote ${oracleObjectBuildersPath}")

val rowCodecBuildersContent = generateRowCodecBuilders()
val rowCodecBuildersPath = outputDir.resolve("RowCodecBuilders.java")
Files.writeString(rowCodecBuildersPath, rowCodecBuildersContent)
println(s"Wrote ${rowCodecBuildersPath}")

val namedRowCodecBuildersContent = generateNamedRowCodecBuilders()
val namedRowCodecBuildersPath = outputDir.resolve("RowCodecNamedBuilders.java")
Files.writeString(namedRowCodecBuildersPath, namedRowCodecBuildersContent)
println(s"Wrote ${namedRowCodecBuildersPath}")

val tupleContent = generateTuples()
val tuplePath = outputDir.resolve("Tuple.java")
Files.writeString(tuplePath, tupleContent)
println(s"Wrote ${tuplePath}")

// Generate DbProcedure.java
val dbProcedureContent = generateDbProcedure()
val dbProcedurePath = outputDir.resolve("DbProcedure.java")
Files.writeString(dbProcedurePath, dbProcedureContent)
println(s"Wrote ${dbProcedurePath}")

// Generate DbFunction.java
val dbFunctionContent = generateDbFunction()
val dbFunctionPath = outputDir.resolve("DbFunction.java")
Files.writeString(dbFunctionPath, dbFunctionContent)
println(s"Wrote ${dbFunctionPath}")

// Generate ParamBuilders.java
val paramBuildersContent = generateParamBuilders()
val paramBuildersPath = outputDir.resolve("ParamBuilders.java")
Files.writeString(paramBuildersPath, paramBuildersContent)
println(s"Wrote ${paramBuildersPath}")

// Generate Template.java
val sqlTemplateContent = generateTemplate()
val sqlTemplatePath = outputDir.resolve("Template.java")
Files.writeString(sqlTemplatePath, sqlTemplateContent)
println(s"Wrote ${sqlTemplatePath}")

// Generate InstrumentedConnection.java
val instrumentedConnectionContent = generateInstrumentedConnection()
val instrumentedConnectionPath = outputDir.resolve("InstrumentedConnection.java")
Files.writeString(instrumentedConnectionPath, instrumentedConnectionContent)
println(s"Wrote ${instrumentedConnectionPath}")
