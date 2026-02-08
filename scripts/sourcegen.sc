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
                     |                elementType.pgJson().array(arrayFactory),
                     |                PgOutParam.parsedArray(arrayFactory, elementType.pgCompositeText()::decode),
                     |                dev.typr.foundations.analysis.AnalysisOptions.EMPTY);
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
          |                dev.typr.foundations.analysis.AnalysisOptions.EMPTY);
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

def generateKotlinRowParserBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|    class Builder0<Row : Any> internal constructor() {
                     |        private val types = mutableListOf<DbType<*>>()
                     |        private val getters = mutableListOf<(Row) -> Any?>()
                     |
                     |        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder1<Row, F> {
                     |            types.add(type)
                     |            @Suppress("UNCHECKED_CAST")
                     |            getters.add(getter as (Row) -> Any?)
                     |            return Builder1(types, getters)
                     |        }
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val decodeParams = range.map(i => s"T$i").mkString(", ")
    val decodeArgs = range.map(i => s"arr[$i] as T$i").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder${n + 1}<Row, $nextTparams, F> {
          |            types.add(type)
          |            @Suppress("UNCHECKED_CAST")
          |            getters.add(getter as (Row) -> Any?)
          |            return Builder${n + 1}(types, getters)
          |        }""".stripMargin
    } else ""

    s"""|    class Builder$n<Row : Any, $tparams> internal constructor(
        |        private val types: MutableList<DbType<*>>,
        |        private val getters: MutableList<(Row) -> Any?>
        |    ) {
        |        @Suppress("UNCHECKED_CAST")
        |        fun build(decode: ($decodeParams) -> Row): RowParser<Row> {
        |            val capturedGetters = getters.toList()
        |            val javaParser = dev.typr.foundations.RowParser<Row>(
        |                types.toList(),
        |                { arr -> decode($decodeArgs) },
        |                { row -> capturedGetters.map { it(row) }.toTypedArray() }
        |            )
        |            return RowParser(javaParser)
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.kotlinfoundations
      |
      |import dev.typr.foundations.DbType
      |
      |/**
      | * Type-safe builders for Kotlin RowParser.
      | *
      | * Usage:
      | * ```kotlin
      | * val parser: RowParser<Product> = RowParser.builder<Product>()
      | *     .field(PgTypes.int4, Product::id)
      | *     .field(PgTypes.text, Product::name)
      | *     .field(PgTypes.numeric, Product::price)
      | *     .build(::Product)
      | * ```
      | */
      |object RowParserBuilders {
      |    fun <Row : Any> builder(): Builder0<Row> = Builder0()
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateScalaRowParserBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|  class Builder0[Row] private[scalafoundations] () {
                     |    private val types = scala.collection.mutable.ListBuffer[DbType[?]]()
                     |    private val getters = scala.collection.mutable.ListBuffer[Row => Any]()
                     |
                     |    def field[F](tpe: DbType[F])(getter: Row => F): Builder1[Row, F] = {
                     |      types += tpe
                     |      getters += getter.asInstanceOf[Row => Any]
                     |      new Builder1(types, getters)
                     |    }
                     |  }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val decodeParams = range.map(i => s"T$i").mkString(", ")
    val decodeArgs = range.map(i => s"arr($i).asInstanceOf[T$i]").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = (0 until n).map(i => s"T$i").mkString(", ")
      s"""|
          |    def field[F](tpe: DbType[F])(getter: Row => F): Builder${n + 1}[Row, $nextTparams, F] = {
          |      types += tpe
          |      getters += getter.asInstanceOf[Row => Any]
          |      new Builder${n + 1}(types, getters)
          |    }""".stripMargin
    } else ""

    s"""|  class Builder$n[Row, $tparams] private[scalafoundations] (
        |    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
        |    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
        |  ) {
        |    def build(decode: ($decodeParams) => Row): RowParser[Row] = {
        |      val capturedGetters = getters.toList
        |      val javaParser = new dev.typr.foundations.RowParser[Row](
        |        java.util.List.copyOf(types.map(_.asInstanceOf[DbType[?]]).asJava),
        |        arr => decode($decodeArgs),
        |        row => capturedGetters.map(_(row)).toArray
        |      )
        |      new RowParser(javaParser)
        |    }$nextBuilder
        |  }""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
      |import dev.typr.foundations.DbType
      |import scala.jdk.CollectionConverters.*
      |
      |/** Type-safe builders for Scala RowParser.
      |  *
      |  * Usage:
      |  * {{{
      |  * val parser: RowParser[Product] = RowParser.builder[Product]()
      |  *   .field(PgTypes.int4)(_.id)
      |  *   .field(PgTypes.text)(_.name)
      |  *   .field(PgTypes.numeric)(_.price)
      |  *   .build(Product.apply)
      |  * }}}
      |  */
      |object RowParserBuilders {
      |  def builder[Row](): Builder0[Row] = new Builder0()
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// DbProcedure / DbFunction generators (max 10 inputs, max 10 outputs)
// ─────────────────────────────────────────────────────────────────────────────

val PROC_N = 11 // 0..10 inclusive

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
      s"""        public <$newI> Builder_${i + 1}_${o}${typeParamDecl(nextTp)} in(DbType<$newI> type) {
         |            params.add(ParamDef.in(type));
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
      | * The builder tracks both input types (via {@code .in()}) and output types (via {@code .out()}/{@code .inout()}).
      | * The resulting interface has a {@code call()} method with typed parameters instead of varargs.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * // Procedure with typed inputs — compile-time checking!
      | * DbProcedure.Def1_2<Integer, String, String> getUser = DbProcedure.define("get_user_by_id")
      | *     .in(PgTypes.int4)
      | *     .out(PgTypes.text)
      | *     .out(PgTypes.text)
      | *     .build();
      | * Tuple.Tuple2<String, String> result = getUser.call(42).transact(tx);  // Integer enforced!
      | * // getUser.call("wrong");  // COMPILE ERROR - String not Integer
      | *
      | * // Void procedure (no outputs)
      | * DbProcedure.Def1_0<String> auditLog = DbProcedure.define("audit_log")
      | *     .in(PgTypes.text)
      | *     .build();
      | *
      | * // INOUT — value goes in and comes back modified
      | * DbProcedure.Def2_1<String, BigDecimal, BigDecimal> applyDiscount = DbProcedure.define("apply_discount")
      | *     .in(PgTypes.text)
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
      s"""        public <$newI> Builder_${i + 1}${typeParamDecl(nextTp)} in(DbType<$newI> type) {
         |            inParams.add(ParamDef.in(type));
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
      | * The builder tracks input types (via {@code .in()}). The resulting interface has a
      | * {@code call()} method with typed parameters instead of varargs.
      | * <p>
      | * Usage:
      | * <pre>{@code
      | * // Function with typed inputs — compile-time checking!
      | * DbFunction.Def2<BigDecimal, String, BigDecimal> calcTax = DbFunction.define("calculate_tax", PgTypes.numeric)
      | *     .in(PgTypes.numeric)
      | *     .in(PgTypes.text)
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

def generateScalaDbProcedure(): String = {
  val maxArity = PROC_N - 1

  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def oParams(o: Int) = 0.until(o).map(n => s"O$n").toList
  def allTypeParams(i: Int, o: Int) = iParams(i) ++ oParams(o)
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"[${ps.mkString(", ")}]"
  def callParams(i: Int) = 0.until(i).map(n => s"i$n: I$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")
  def outType(o: Int): String = o match {
    case 0 => "Unit"
    case 1 => "O0"
    case n => s"dev.typr.foundations.Tuple.Tuple$n[${oParams(n).mkString(", ")}]"
  }

  // Def traits: 11x11
  val defs = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tpDecl = typeParamDecl(allTypeParams(i, o))
    val retType = outType(o)
    s"""  /** Procedure definition with $i input(s) and $o output(s). */
       |  trait Def${i}_${o}$tpDecl {
       |    def call(${callParams(i)}): ProcedureOp[$retType]
       |  }""".stripMargin
  }

  // Builder classes: 11x11
  val builders = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tp = allTypeParams(i, o)
    val tpDecl = typeParamDecl(tp)
    val javaTpDecl = if (tp.isEmpty) "" else s"[${tp.mkString(", ")}]"

    // in method
    val inMethod = if (i < maxArity) {
      val nextTp = allTypeParams(i + 1, o)
      s"""    def in[I$i](tpe: DbType[I$i]): Builder_${i + 1}_${o}${typeParamDecl(nextTp)} =
         |      new Builder_${i + 1}_${o}(underlying.in(tpe))""".stripMargin
    } else ""

    // out method
    val outMethod = if (o < maxArity) {
      val nextTp = allTypeParams(i, o + 1)
      s"""    def out[O$o](tpe: DbType[O$o]): Builder_${i}_${o + 1}${typeParamDecl(nextTp)} =
         |      new Builder_${i}_${o + 1}(underlying.out(tpe))""".stripMargin
    } else ""

    // inout method
    val inoutMethod = if (i < maxArity && o < maxArity) {
      val inoutTp = iParams(i) ::: List("X") ::: oParams(o) ::: List("X")
      s"""    def inout[X](tpe: DbType[X]): Builder_${i + 1}_${o + 1}${typeParamDecl(inoutTp)} =
         |      new Builder_${i + 1}_${o + 1}(underlying.inout(tpe))""".stripMargin
    } else ""

    val methods = List(inMethod, outMethod, inoutMethod).filter(_.nonEmpty).mkString("\n")
    val methodsBlock = if (methods.nonEmpty) s"$methods\n" else ""

    // build method
    val retType = outType(o)
    val defTpDecl = typeParamDecl(allTypeParams(i, o))
    val callParamsStr = callParams(i)
    val callArgNamesStr = callArgNames(i)

    val castExpr = o match {
      case 0 => "_ => ()"
      case 1 => "_.asInstanceOf[O0]"
      case n => s"_.asInstanceOf[$retType]"
    }

    val javaCallArgs = if (i == 0) "" else callArgNamesStr

    s"""  class Builder_${i}_${o}$tpDecl private[scalafoundations] (
       |    private val underlying: dev.typr.foundations.DbProcedure.Builder_${i}_${o}$javaTpDecl
       |  ) {
       |$methodsBlock
       |    def build(): Def${i}_${o}$defTpDecl = {
       |      val javaProc = underlying.build()
       |      new Def${i}_${o}$defTpDecl {
       |        def call($callParamsStr): ProcedureOp[$retType] =
       |          new ProcedureOp(javaProc.call($javaCallArgs).asInstanceOf[dev.typr.foundations.Operation[Any]], $castExpr)
       |      }
       |    }
       |  }""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
      |import dev.typr.foundations.DbType
      |
      |/** Type-safe stored procedure definitions with fully typed inputs and outputs.
      |  *
      |  * Usage:
      |  * {{{
      |  * val getUser: DbProcedure.Def1_2[Int, String, String] = DbProcedure.define("get_user_by_id")
      |  *   .in(PgTypes.int4)
      |  *   .out(PgTypes.text)
      |  *   .out(PgTypes.text)
      |  *   .build()
      |  * val result = getUser.call(42).transact(tx)  // Int enforced!
      |  * }}}
      |  *
      |  * @see [[DbFunction]] for stored functions (single return value via SELECT)
      |  */
      |object DbProcedure {
      |
      |  /** Start defining a stored procedure. */
      |  def define(name: String): Builder_0_0 =
      |    new Builder_0_0(dev.typr.foundations.DbProcedure.define(name))
      |
      |  // ─────────────────────────────────────────────────────────────────────────────
      |  // Procedure definition interfaces (${PROC_N * PROC_N} total: ${PROC_N}×${PROC_N} matrix of input×output arities)
      |  // ─────────────────────────────────────────────────────────────────────────────
      |
      |${defs.mkString("\n\n")}
      |
      |  // ─────────────────────────────────────────────────────────────────────────────
      |  // Procedure builders (${PROC_N * PROC_N} total: ${PROC_N}×${PROC_N} matrix)
      |  // ─────────────────────────────────────────────────────────────────────────────
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateScalaDbFunction(): String = {
  val maxArity = PROC_N - 1

  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"[${ps.mkString(", ")}]"
  def callParams(i: Int) = 0.until(i).map(n => s"i$n: I$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")

  // Def traits: 11 total
  val defs = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    s"""  /** Function definition with $i input(s). */
       |  trait Def$i${typeParamDecl(tp)} {
       |    def call(${callParams(i)}): ProcedureOp[R]
       |  }""".stripMargin
  }

  // Builder classes: 11 total
  val builders = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    val tpDecl = typeParamDecl(tp)
    val javaTpDecl = typeParamDecl(tp)

    val inMethod = if (i < maxArity) {
      val nextTp = iParams(i + 1) ::: List("R")
      s"""    def in[I$i](tpe: DbType[I$i]): Builder_${i + 1}${typeParamDecl(nextTp)} =
         |      new Builder_${i + 1}(underlying.in(tpe))
         |""".stripMargin
    } else ""

    val callParamsStr = callParams(i)
    val javaCallArgs = if (i == 0) "" else callArgNames(i)

    s"""  class Builder_$i$tpDecl private[scalafoundations] (
       |    private val underlying: dev.typr.foundations.DbFunction.Builder_$i$javaTpDecl
       |  ) {
       |$inMethod
       |    def build(): Def$i$tpDecl = {
       |      val javaFn = underlying.build()
       |      new Def$i$tpDecl {
       |        def call($callParamsStr): ProcedureOp[R] =
       |          new ProcedureOp(javaFn.call($javaCallArgs).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
       |      }
       |    }
       |  }""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
      |import dev.typr.foundations.DbType
      |
      |/** Type-safe stored function definitions with fully typed inputs.
      |  *
      |  * Usage:
      |  * {{{
      |  * val calcTax: DbFunction.Def2[BigDecimal, String, BigDecimal] = DbFunction.define("calculate_tax", PgTypes.numeric)
      |  *   .in(PgTypes.numeric)
      |  *   .in(PgTypes.text)
      |  *   .build()
      |  * val tax = calcTax.call(amount, "US").transact(tx)  // Types enforced!
      |  * }}}
      |  *
      |  * @see [[DbProcedure]] for stored procedures (with OUT/INOUT parameters)
      |  */
      |object DbFunction {
      |
      |  /** Start defining a stored function (single return value, uses SELECT). */
      |  def define[R](name: String, returnType: DbType[R]): Builder_0[R] =
      |    new Builder_0(dev.typr.foundations.DbFunction.define(name, returnType))
      |
      |  // ─────────────────────────────────────────────────────────────────────────────
      |  // Function definition interfaces (${PROC_N} total: 0-${maxArity} inputs)
      |  // ─────────────────────────────────────────────────────────────────────────────
      |
      |${defs.mkString("\n\n")}
      |
      |  // ─────────────────────────────────────────────────────────────────────────────
      |  // Function builders (${PROC_N} total: 0-${maxArity} inputs)
      |  // ─────────────────────────────────────────────────────────────────────────────
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinDbProcedure(): String = {
  val maxArity = PROC_N - 1

  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def oParams(o: Int) = 0.until(o).map(n => s"O$n").toList
  def allTypeParams(i: Int, o: Int) = iParams(i) ++ oParams(o)
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"<${ps.mkString(", ")}>"
  def callParams(i: Int) = 0.until(i).map(n => s"i$n: I$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")
  def outType(o: Int): String = o match {
    case 0 => "Unit"
    case 1 => "O0"
    case n => s"dev.typr.foundations.Tuple.Tuple$n<${oParams(n).mkString(", ")}>"
  }

  // Def interfaces: 11x11
  val defs = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tpDecl = typeParamDecl(allTypeParams(i, o))
    val retType = outType(o)
    s"""    /** Procedure definition with $i input(s) and $o output(s). */
       |    fun interface Def${i}_${o}$tpDecl {
       |        fun call(${ callParams(i) }): ProcedureOp<$retType>
       |    }""".stripMargin
  }

  // Builder classes: 11x11
  val builders = for {
    i <- 0 to maxArity
    o <- 0 to maxArity
  } yield {
    val tp = allTypeParams(i, o)
    val tpDecl = typeParamDecl(tp)
    val javaTpDecl = tpDecl

    // in method
    val inMethod = if (i < maxArity) {
      val nextTp = allTypeParams(i + 1, o)
      s"""        fun <I$i> `in`(type: DbType<I$i>): Builder_${i + 1}_${o}${typeParamDecl(nextTp)} =
         |            Builder_${i + 1}_${o}(underlying.`in`(type))""".stripMargin
    } else ""

    // out method
    val outMethod = if (o < maxArity) {
      val nextTp = allTypeParams(i, o + 1)
      s"""        fun <O$o> out(type: DbType<O$o>): Builder_${i}_${o + 1}${typeParamDecl(nextTp)} =
         |            Builder_${i}_${o + 1}(underlying.out(type))""".stripMargin
    } else ""

    // inout method
    val inoutMethod = if (i < maxArity && o < maxArity) {
      val inoutTp = iParams(i) ::: List("X") ::: oParams(o) ::: List("X")
      s"""        fun <X> inout(type: DbType<X>): Builder_${i + 1}_${o + 1}${typeParamDecl(inoutTp)} =
         |            Builder_${i + 1}_${o + 1}(underlying.inout(type))""".stripMargin
    } else ""

    val methods = List(inMethod, outMethod, inoutMethod).filter(_.nonEmpty).mkString("\n")
    val methodsBlock = if (methods.nonEmpty) s"$methods\n" else ""

    // build method
    val retType = outType(o)
    val callParamsStr = callParams(i)
    val javaCallArgs = if (i == 0) "" else callArgNames(i)

    // Lambda params for Kotlin
    val lambdaParams = if (i == 0) " ->" else s" ${callParams(i)} ->"

    val castExpr = o match {
      case 0 => "{ }"
      case 1 => "{ it as O0 }"
      case n => s"{ it as $retType }"
    }

    s"""    class Builder_${i}_${o}$tpDecl internal constructor(
       |        private val underlying: dev.typr.foundations.DbProcedure.Builder_${i}_${o}$javaTpDecl
       |    ) {
       |$methodsBlock
       |        fun build(): Def${i}_${o}$tpDecl {
       |            val javaProc = underlying.build()
       |            return Def${i}_${o} {$lambdaParams
       |                @Suppress("UNCHECKED_CAST")
       |                ProcedureOp(javaProc.call($javaCallArgs) as dev.typr.foundations.Operation<Any?>) $castExpr
       |            }
       |        }
       |    }""".stripMargin
  }

  s"""|package dev.typr.kotlinfoundations
      |
      |import dev.typr.foundations.DbType
      |
      |/**
      | * Type-safe stored procedure definitions with fully typed inputs and outputs.
      | *
      | * Usage:
      | * ```kotlin
      | * val getUser: DbProcedure.Def1_2<Int, String, String> = DbProcedure.define("get_user_by_id")
      | *     .`in`(PgTypes.int4)
      | *     .out(PgTypes.text)
      | *     .out(PgTypes.text)
      | *     .build()
      | * val result = getUser.call(42).transact(tx)  // Int enforced!
      | * ```
      | *
      | * @see DbFunction for stored functions (single return value via SELECT)
      | */
      |object DbProcedure {
      |
      |    /** Start defining a stored procedure. */
      |    fun define(name: String): Builder_0_0 =
      |        Builder_0_0(dev.typr.foundations.DbProcedure.define(name))
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

def generateKotlinDbFunction(): String = {
  val maxArity = PROC_N - 1

  def iParams(i: Int) = 0.until(i).map(n => s"I$n").toList
  def typeParamDecl(ps: List[String]) = if (ps.isEmpty) "" else s"<${ps.mkString(", ")}>"
  def callParams(i: Int) = 0.until(i).map(n => s"i$n: I$n").mkString(", ")
  def callArgNames(i: Int) = 0.until(i).map(n => s"i$n").mkString(", ")

  // Def interfaces: 11 total
  val defs = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    s"""    /** Function definition with $i input(s). */
       |    fun interface Def$i${typeParamDecl(tp)} {
       |        fun call(${callParams(i)}): ProcedureOp<R>
       |    }""".stripMargin
  }

  // Builder classes: 11 total
  val builders = (0 to maxArity).map { i =>
    val tp = iParams(i) ::: List("R")
    val tpDecl = typeParamDecl(tp)

    val inMethod = if (i < maxArity) {
      val nextTp = iParams(i + 1) ::: List("R")
      s"""        fun <I$i> `in`(type: DbType<I$i>): Builder_${i + 1}${typeParamDecl(nextTp)} =
         |            Builder_${i + 1}(underlying.`in`(type))
         |""".stripMargin
    } else ""

    val callParamsStr = callParams(i)
    val javaCallArgs = if (i == 0) "" else callArgNames(i)
    val lambdaParams = if (i == 0) " ->" else s" ${callParams(i)} ->"

    s"""    class Builder_$i$tpDecl internal constructor(
       |        private val underlying: dev.typr.foundations.DbFunction.Builder_$i$tpDecl
       |    ) {
       |$inMethod
       |        fun build(): Def$i$tpDecl {
       |            val javaFn = underlying.build()
       |            return Def$i {$lambdaParams
       |                @Suppress("UNCHECKED_CAST")
       |                ProcedureOp(javaFn.call($javaCallArgs) as dev.typr.foundations.Operation<Any?>) { it as R }
       |            }
       |        }
       |    }""".stripMargin
  }

  s"""|package dev.typr.kotlinfoundations
      |
      |import dev.typr.foundations.DbType
      |
      |/**
      | * Type-safe stored function definitions with fully typed inputs.
      | *
      | * Usage:
      | * ```kotlin
      | * val calcTax: DbFunction.Def2<BigDecimal, String, BigDecimal> = DbFunction.define("calculate_tax", PgTypes.numeric)
      | *     .`in`(PgTypes.numeric)
      | *     .`in`(PgTypes.text)
      | *     .build()
      | * val tax = calcTax.call(amount, "US").transact(tx)  // Types enforced!
      | * ```
      | *
      | * @see DbProcedure for stored procedures (with OUT/INOUT parameters)
      | */
      |object DbFunction {
      |
      |    /** Start defining a stored function (single return value, uses SELECT). */
      |    fun <R> define(name: String, returnType: DbType<R>): Builder_0<R> =
      |        Builder_0(dev.typr.foundations.DbFunction.define(name, returnType))
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

val kotlinOutputDir = baseDir.resolve("foundations-jdbc-kotlin/src/kotlin/dev/typr/kotlinfoundations")
val scalaOutputDir = baseDir.resolve("foundations-jdbc-scala/src/scala/dev/typr/scalafoundations")

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



// Generate Kotlin RowParserBuilders
Files.createDirectories(kotlinOutputDir)
val kotlinRowParserBuildersContent = generateKotlinRowParserBuilders()
val kotlinRowParserBuildersPath = kotlinOutputDir.resolve("RowParserBuilders.kt")
Files.writeString(kotlinRowParserBuildersPath, kotlinRowParserBuildersContent)
println(s"Wrote ${kotlinRowParserBuildersPath}")

// Generate Scala RowParserBuilders
Files.createDirectories(scalaOutputDir)
val scalaRowParserBuildersContent = generateScalaRowParserBuilders()
val scalaRowParserBuildersPath = scalaOutputDir.resolve("RowParserBuilders.scala")
Files.writeString(scalaRowParserBuildersPath, scalaRowParserBuildersContent)
println(s"Wrote ${scalaRowParserBuildersPath}")



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

// Generate Scala DbProcedure
val scalaDbProcedureContent = generateScalaDbProcedure()
val scalaDbProcedurePath = scalaOutputDir.resolve("DbProcedure.scala")
Files.writeString(scalaDbProcedurePath, scalaDbProcedureContent)
println(s"Wrote ${scalaDbProcedurePath}")

// Generate Scala DbFunction
val scalaDbFunctionContent = generateScalaDbFunction()
val scalaDbFunctionPath = scalaOutputDir.resolve("DbFunction.scala")
Files.writeString(scalaDbFunctionPath, scalaDbFunctionContent)
println(s"Wrote ${scalaDbFunctionPath}")

// Generate Kotlin DbProcedure
val kotlinDbProcedureContent = generateKotlinDbProcedure()
val kotlinDbProcedurePath = kotlinOutputDir.resolve("DbProcedure.kt")
Files.writeString(kotlinDbProcedurePath, kotlinDbProcedureContent)
println(s"Wrote ${kotlinDbProcedurePath}")

// Generate Kotlin DbFunction
val kotlinDbFunctionContent = generateKotlinDbFunction()
val kotlinDbFunctionPath = kotlinOutputDir.resolve("DbFunction.kt")
Files.writeString(kotlinDbFunctionPath, kotlinDbFunctionContent)
println(s"Wrote ${kotlinDbFunctionPath}")
