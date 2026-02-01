#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.4

import java.nio.file.{Files, Path}

val N = 100
val baseDir = Path.of(sys.props.getOrElse("user.dir", "."))
val outputDir = baseDir.resolve("foundations-jdbc/generated-and-checked-in/dev/typr/foundations")

def generateRowParsers(): String = {
  // Unary method: RowParsers.of(DbType<T>) -> RowParser<T>
  val unaryMethod =
    s"""|    @SuppressWarnings("unchecked")
        |    static <T> RowParser<T> of(DbType<T> t0) {
        |        return new RowParser<>(unmodifiableList(asList(t0)), a -> (T) a[0], t -> new Object[]{t});
        |    }""".stripMargin

  val constructorMethods = 1
    .until(N)
    .map { n =>
      val range = 0.until(n)
      val tparamsDecl = range.map(nn => s"T$nn").mkString(", ")
      val params = range.map(nn => s"DbType<T$nn> t$nn").mkString(", ")
      val decodeFunction = s"Function$n<${range.map(nn => s"T$nn").mkString(", ")}, Row>"
      val decodeParams = range.map(nn => s"(T$nn) a[$nn]").mkString(", ")
      s"""|    @SuppressWarnings("unchecked")
          |    static <$tparamsDecl, Row> RowParser<Row> of($params, $decodeFunction decode, java.util.function.Function<Row, Object[]> encode) {
          |        return new RowParser<>(unmodifiableList(asList(${range.map(nn => s"t$nn").mkString(", ")})), a -> decode.apply($decodeParams), encode);
          |    }""".stripMargin
    }

  val functions = 1
    .until(N)
    .map { n =>
      s"""|    @FunctionalInterface
          |    interface Function$n<${0.until(n).map(nn => s"T$nn").mkString(", ")}, R> {
          |        R apply(${0.until(n).map(nn => s"T$nn t$nn").mkString(", ")});
          |    }""".stripMargin
    }

  s"""|package dev.typr.foundations;
      |
      |import static java.util.Arrays.asList;
      |import static java.util.Collections.unmodifiableList;
      |
      |public interface RowParsers {
      |$unaryMethod
      |
      |${constructorMethods.mkString("\n\n")}
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

Files.createDirectories(outputDir)

val rowParsersContent = generateRowParsers()
val rowParsersPath = outputDir.resolve("RowParsers.java")
Files.writeString(rowParsersPath, rowParsersContent)
println(s"Wrote ${rowParsersPath}")

val tupleContent = generateTuples()
val tuplePath = outputDir.resolve("Tuple.java")
Files.writeString(tuplePath, tupleContent)
println(s"Wrote ${tuplePath}")
