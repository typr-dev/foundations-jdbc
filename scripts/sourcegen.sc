#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.4

import java.nio.file.{Files, Path}

val N = 100
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

def generateRowParserBuilders(): String = {
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
        |        public RowParser<Row> build($functionType decode) {
        |            java.util.List<java.util.function.Function<Row, ?>> capturedGetters = java.util.List.copyOf(getters);
        |            return new RowParser<>(
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
      | * Type-safe builders for RowParser.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * RowParser<Product> parser = RowParserBuilders.<Product>builder()
      | *     .field(PgTypes.int4, Product::id)
      | *     .field(PgTypes.text, Product::name)
      | *     .field(PgTypes.numeric, Product::price)
      | *     .build(Product::new);
      | * }</pre>
      | */
      |public final class RowParserBuilders {
      |    private RowParserBuilders() {}
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
  val maxArity = N - 1  // N is 100, so 99 fields max (matching Functions.FunctionN)

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
  val maxArity = N - 1  // N is 100, so 99 fields max (matching Functions.FunctionN)

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
  val maxArity = N - 1  // N is 100, so 99 fields max (matching Functions.FunctionN)

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
                     |                elementType.pgJson().array(arrayFactory));
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
          |                elementType.pgJson().array(arrayFactory));
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

Files.createDirectories(outputDir)

val functionsContent = generateFunctions()
val functionsPath = outputDir.resolve("Functions.java")
Files.writeString(functionsPath, functionsContent)
println(s"Wrote ${functionsPath}")

// Delete old RowParsers.java if it exists
val oldRowParsersPath = outputDir.resolve("RowParsers.java")
if (Files.exists(oldRowParsersPath)) {
  Files.delete(oldRowParsersPath)
  println(s"Deleted ${oldRowParsersPath}")
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

val rowParserBuildersContent = generateRowParserBuilders()
val rowParserBuildersPath = outputDir.resolve("RowParserBuilders.java")
Files.writeString(rowParserBuildersPath, rowParserBuildersContent)
println(s"Wrote ${rowParserBuildersPath}")

val tupleContent = generateTuples()
val tuplePath = outputDir.resolve("Tuple.java")
Files.writeString(tuplePath, tupleContent)
println(s"Wrote ${tuplePath}")
