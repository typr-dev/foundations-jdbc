#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.4

import java.nio.file.{Files, Path}

val N = 100
val PROC_N = 11
val STRUCT_N = 31
val baseDir = Path.of(sys.props.getOrElse("user.dir", "."))
val generatedOutputDir = baseDir.resolve("foundations-jdbc-kotlin/generated-and-checked-in/dev/typr/kotlinfoundations")

def generateKotlinRowParserBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|    class Builder0<Row : Any> internal constructor() {
                     |        private val types = mutableListOf<dev.typr.foundations.DbType<*>>()
                     |        private val getters = mutableListOf<(Row) -> Any?>()
                     |
                     |        fun <F> field(type: DbType<F>, getter: (Row) -> F): Builder1<Row, F> {
                     |            types.add(type.underlying)
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
          |            types.add(type.underlying)
          |            @Suppress("UNCHECKED_CAST")
          |            getters.add(getter as (Row) -> Any?)
          |            return Builder${n + 1}(types, getters)
          |        }""".stripMargin
    } else ""

    s"""|    class Builder$n<Row : Any, $tparams> internal constructor(
        |        private val types: MutableList<dev.typr.foundations.DbType<*>>,
        |        private val getters: MutableList<(Row) -> Any?>
        |    ) {
        |        @Suppress("UNCHECKED_CAST")
        |        fun build(decode: ($decodeParams) -> Row): RowParser<Row> {
        |            val capturedGetters = getters.toList()
        |            val javaParser = dev.typr.foundations.RowParser.create<Row>(
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

def generateKotlinNamedRowParserBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|    class Builder0<Row : Any> internal constructor() {
                     |        private val names = mutableListOf<String>()
                     |        private val types = mutableListOf<dev.typr.foundations.DbType<*>>()
                     |        private val getters = mutableListOf<(Row) -> Any?>()
                     |
                     |        fun <F> field(name: String, type: DbType<F>, getter: (Row) -> F): Builder1<Row, F> {
                     |            names.add(name)
                     |            types.add(type.underlying)
                     |            @Suppress("UNCHECKED_CAST")
                     |            getters.add(getter as (Row) -> Any?)
                     |            return Builder1(names, types, getters)
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
          |        fun <F> field(name: String, type: DbType<F>, getter: (Row) -> F): Builder${n + 1}<Row, $nextTparams, F> {
          |            names.add(name)
          |            types.add(type.underlying)
          |            @Suppress("UNCHECKED_CAST")
          |            getters.add(getter as (Row) -> Any?)
          |            return Builder${n + 1}(names, types, getters)
          |        }""".stripMargin
    } else ""

    s"""|    class Builder$n<Row : Any, $tparams> internal constructor(
        |        private val names: MutableList<String>,
        |        private val types: MutableList<dev.typr.foundations.DbType<*>>,
        |        private val getters: MutableList<(Row) -> Any?>
        |    ) {
        |        @Suppress("UNCHECKED_CAST")
        |        fun build(decode: ($decodeParams) -> Row): RowParserNamed<Row> {
        |            val capturedGetters = getters.toList()
        |            val javaParser = dev.typr.foundations.RowParser.createNamed<Row>(
        |                names.toList(),
        |                types.toList(),
        |                { arr -> decode($decodeArgs) },
        |                { row -> capturedGetters.map { it(row) }.toTypedArray() }
        |            )
        |            return RowParserNamed(javaParser)
        |        }$nextBuilder
        |    }""".stripMargin
  }

  s"""|package dev.typr.kotlinfoundations
      |
      |/**
      | * Type-safe named builders for Kotlin RowParser.
      | *
      | * Usage:
      | * ```kotlin
      | * val parser: RowParserNamed<Product> = RowParser.namedBuilder<Product>()
      | *     .field("id", PgTypes.int4, Product::id)
      | *     .field("name", PgTypes.text, Product::name)
      | *     .field("price", PgTypes.numeric, Product::price)
      | *     .build(::Product)
      | * ```
      | */
      |object RowParserNamedBuilders {
      |    fun <Row : Any> builder(): Builder0<Row> = Builder0()
      |
      |$builder0
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
         |            Builder_${i + 1}_${o}(underlying.`in`(type.underlying))""".stripMargin
    } else ""

    // out method
    val outMethod = if (o < maxArity) {
      val nextTp = allTypeParams(i, o + 1)
      s"""        fun <O$o> out(type: DbType<O$o>): Builder_${i}_${o + 1}${typeParamDecl(nextTp)} =
         |            Builder_${i}_${o + 1}(underlying.out(type.underlying))""".stripMargin
    } else ""

    // inout method
    val inoutMethod = if (i < maxArity && o < maxArity) {
      val inoutTp = iParams(i) ::: List("X") ::: oParams(o) ::: List("X")
      s"""        fun <X> inout(type: DbType<X>): Builder_${i + 1}_${o + 1}${typeParamDecl(inoutTp)} =
         |            Builder_${i + 1}_${o + 1}(underlying.inout(type.underlying))""".stripMargin
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
         |            Builder_${i + 1}(underlying.`in`(type.underlying))
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
      |        Builder_0(dev.typr.foundations.DbFunction.define(name, returnType.underlying))
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

def generateKotlinDuckDbStructBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    class Builder0<A> internal constructor(
                     |        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder0<A>
                     |    ) {
                     |        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder1<A, F> =
                     |            Builder1(underlying.field(name, type.underlying, getter))
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val lambdaParams = range.map(i => s"t$i").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = range.map(i => s"T$i").mkString(", ")
      s"""|
          |        fun <F> field(name: String, type: DuckDbType<F>, getter: (A) -> F): Builder${n + 1}<A, $nextTparams, F> =
          |            Builder${n + 1}(underlying.field(name, type.underlying, getter))""".stripMargin
    } else ""

    s"""|    class Builder$n<A, $tparams> internal constructor(
        |        private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder$n<A, $tparams>
        |    ) {
        |        fun build(decode: ($tparams) -> A): DuckDbStruct<A> =
        |            DuckDbStruct(underlying.build { $lambdaParams -> decode($lambdaParams) })
        |$nextBuilder
        |    }""".stripMargin
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |class DuckDbStruct<T>(val underlying: dev.typr.foundations.DuckDbStruct<T>) {
      |    fun asType(): DuckDbType<T> = DuckDbType(underlying.asType())
      |
      |    companion object {
      |        fun <A> builder(name: String): Builder0<A> =
      |            Builder0(dev.typr.foundations.DuckDbStructBuilders.builder(name))
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinPgStructBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    class Builder0<A> internal constructor(
                     |        private val underlying: dev.typr.foundations.PgStructBuilders.Builder0<A>
                     |    ) {
                     |        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder1<A, F> =
                     |            Builder1(underlying.field(name, type.underlying, getter))
                     |
                     |        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder1<A, F> =
                     |            Builder1(underlying.nestedField(name, nestedStruct.underlying, getter))
                     |
                     |        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder1<A, Array<F>> =
                     |            Builder1(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val lambdaParams = range.map(i => s"t$i").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = range.map(i => s"T$i").mkString(", ")
      s"""|
          |        fun <F> field(name: String, type: PgType<F>, getter: (A) -> F): Builder${n + 1}<A, $nextTparams, F> =
          |            Builder${n + 1}(underlying.field(name, type.underlying, getter))
          |
          |        fun <F> nestedField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> F): Builder${n + 1}<A, $nextTparams, F> =
          |            Builder${n + 1}(underlying.nestedField(name, nestedStruct.underlying, getter))
          |
          |        fun <F> nestedArrayField(name: String, nestedStruct: PgStruct<F>, getter: (A) -> Array<F>, arrayFactory: java.util.function.IntFunction<Array<F>>): Builder${n + 1}<A, $nextTparams, Array<F>> =
          |            Builder${n + 1}(underlying.nestedArrayField(name, nestedStruct.underlying, getter, arrayFactory))""".stripMargin
    } else ""

    s"""|    class Builder$n<A, $tparams> internal constructor(
        |        private val underlying: dev.typr.foundations.PgStructBuilders.Builder$n<A, $tparams>
        |    ) {
        |        fun build(decode: ($tparams) -> A): PgStruct<A> =
        |            PgStruct(underlying.build { $lambdaParams -> decode($lambdaParams) })
        |$nextBuilder
        |    }""".stripMargin
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |class PgStruct<T>(val underlying: dev.typr.foundations.PgStruct<T>) {
      |    fun asType(): PgType<T> = PgType(underlying.asType())
      |
      |    companion object {
      |        fun <A> builder(name: String): Builder0<A> =
      |            Builder0(dev.typr.foundations.PgStructBuilders.builder(name))
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinOracleObjectBuilders(): String = {
  val maxArity = STRUCT_N - 1

  val builder0 = s"""|    class Builder0<A> internal constructor(
                     |        private val underlying: dev.typr.foundations.OracleObjectBuilders.Builder0<A>
                     |    ) {
                     |        fun <F> field(name: String, type: OracleType<F>, getter: (A) -> F): Builder1<A, F> =
                     |            Builder1(underlying.field(name, type.underlying, getter))
                     |    }""".stripMargin

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val lambdaParams = range.map(i => s"t$i").mkString(", ")

    val nextBuilder = if (n < maxArity) {
      val nextTparams = range.map(i => s"T$i").mkString(", ")
      s"""|
          |        fun <F> field(name: String, type: OracleType<F>, getter: (A) -> F): Builder${n + 1}<A, $nextTparams, F> =
          |            Builder${n + 1}(underlying.field(name, type.underlying, getter))""".stripMargin
    } else ""

    s"""|    class Builder$n<A, $tparams> internal constructor(
        |        private val underlying: dev.typr.foundations.OracleObjectBuilders.Builder$n<A, $tparams>
        |    ) {
        |        fun build(decode: ($tparams) -> A): OracleObject<A> =
        |            OracleObject(underlying.build { $lambdaParams -> decode($lambdaParams) })
        |$nextBuilder
        |    }""".stripMargin
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |class OracleObject<T>(val underlying: dev.typr.foundations.OracleObject<T>) {
      |    fun asType(): OracleType<T> = OracleType(underlying.asType())
      |
      |    companion object {
      |        fun <A> builder(name: String): Builder0<A> =
      |            Builder0(dev.typr.foundations.OracleObjectBuilders.builder(name))
      |    }
      |
      |$builder0
      |
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinTuple(): String = {
  val classes = 1.to(N).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val ctorParams = range.map(i => s"private val v$i: T$i").mkString(", ")
    val overrides = range.map(i => s"        override fun _${i + 1}(): T$i = v$i").mkString("\n")
    val components = range.map(i => s"        operator fun component${i + 1}(): T$i = v$i").mkString("\n")
    val stars = range.map(_ => "*").mkString(", ")
    val eqFields = range.map(i => s"_${i + 1}() == other._${i + 1}()").mkString(" && ")
    val hashFields = range.map(i => s"v$i").mkString(", ")
    val toStringFields = range.map(i => s"$$v$i").mkString(", ")
    s"""    class Tuple$n<$tparams>($ctorParams) : JavaTuple.Tuple$n<$tparams> {
       |$overrides
       |$components
       |        override fun equals(other: Any?): Boolean =
       |            other is Tuple$n<$stars> && $eqFields
       |        override fun hashCode(): Int = java.util.Objects.hash($hashFields)
       |        override fun toString(): String = "Tuple$n($toStringFields)"
       |    }""".stripMargin
  }

  val factories = 1.to(N).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"T$i").mkString(", ")
    val params = range.map(i => s"v$i: T$i").mkString(", ")
    val args = range.map(i => s"v$i").mkString(", ")
    s"""    @JvmStatic
       |    fun <$tparams> of($params): Tuple$n<$tparams> =
       |        Tuple$n($args)""".stripMargin
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |import dev.typr.foundations.Tuple as JavaTuple
      |
      |object Tuple {
      |${classes.mkString("\n\n")}
      |
      |${factories.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinSqlTemplate(): String = {
  val maxArity = PROC_N - 1 // 10

  def andType(n: Int): String = {
    if (n == 1) "P0"
    else {
      val inner = andType(n - 1)
      s"dev.typr.foundations.And<$inner, P${n - 1}>"
    }
  }

  val queryClasses = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")
    val allTparams = s"$tparams, Out"

    if (n == 1) {
      s"""|    class Query1<P0, Out>(override val underlying: dev.typr.foundations.SqlTemplate.Query1<P0, Out>) : SqlTemplate<P0, Out>() {
          |        override fun on(input: P0): Operation.Query<Out> =
          |            Operation.Query(underlying.on(input))
          |    }""".stripMargin
    } else {
      val andTypeStr = andType(n)
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val onArgs = range.map(i => s"p$i").mkString(", ")

      s"""|    class Query$n<$allTparams>(override val underlying: dev.typr.foundations.SqlTemplate.Query$n<$allTparams>) : SqlTemplate<$andTypeStr, Out>() {
          |        override fun on(input: $andTypeStr): Operation.Query<Out> =
          |            Operation.Query(underlying.on(input))
          |
          |        fun on($onParams): Operation.Query<Out> =
          |            Operation.Query(underlying.on($onArgs))
          |    }""".stripMargin
    }
  }

  val updateClasses = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")

    if (n == 1) {
      s"""|    class Update1<P0>(override val underlying: dev.typr.foundations.SqlTemplate.Update1<P0>) : SqlTemplate<P0, Int>() {
          |        override fun on(input: P0): Operation.Update =
          |            Operation.Update(underlying.on(input))
          |    }""".stripMargin
    } else {
      val andTypeStr = andType(n)
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val onArgs = range.map(i => s"p$i").mkString(", ")

      s"""|    class Update$n<$tparams>(override val underlying: dev.typr.foundations.SqlTemplate.Update$n<$tparams>) : SqlTemplate<$andTypeStr, Int>() {
          |        override fun on(input: $andTypeStr): Operation.Update =
          |            Operation.Update(underlying.on(input))
          |
          |        fun on($onParams): Operation.Update =
          |            Operation.Update(underlying.on($onArgs))
          |    }""".stripMargin
    }
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |sealed class SqlTemplate<In, Out> {
      |    abstract val underlying: dev.typr.foundations.SqlTemplate<*, *>
      |
      |    abstract fun on(input: In): Operation<Out>
      |
      |    fun fragment(): Fragment = Fragment(underlying.fragment())
      |
      |${queryClasses.mkString("\n\n")}
      |
      |${updateClasses.mkString("\n\n")}
      |}
      |""".stripMargin
}

def generateKotlinParamBuilders(): String = {
  val maxArity = PROC_N - 1 // 10

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")

    val nextParamMethod = if (n < maxArity) {
      s"""|
          |        fun <P$n> param(type: DbType<P$n>): ParamBuilder${n + 1}<$tparams, P$n> =
          |            ParamBuilder${n + 1}(underlying.param(type.underlying))""".stripMargin
    } else ""

    s"""|    class ParamBuilder$n<$tparams>(
        |        private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n<$tparams>
        |    ) {
        |        fun append(s: String): ParamBuilder$n<$tparams> = ParamBuilder$n(underlying.append(s))
        |
        |        fun <T> value(type: DbType<T>, value: T): ParamBuilder$n<$tparams> = ParamBuilder$n(underlying.value(type.underlying, value))
        |
        |        fun append(fragment: Fragment): ParamBuilder$n<$tparams> = ParamBuilder$n(underlying.append(fragment.underlying))
        |$nextParamMethod
        |        fun <Out> query(parser: ResultSetParser<Out>): SqlTemplate.Query$n<$tparams, Out> =
        |            SqlTemplate.Query$n(underlying.query(parser.underlying))
        |
        |        fun update(): SqlTemplate.Update$n<$tparams> =
        |            SqlTemplate.Update$n(underlying.update())
        |
        |        fun done(): Fragment = Fragment(underlying.done())
        |    }""".stripMargin
  }

  s"""|@file:Suppress("unused")
      |package dev.typr.kotlinfoundations
      |
      |object ParamBuilders {
      |${builders.mkString("\n\n")}
      |}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// Write files
// ─────────────────────────────────────────────────────────────────────────────

Files.createDirectories(generatedOutputDir)

// RowParserBuilders.kt -> generated-and-checked-in
val kotlinRowParserBuildersContent = generateKotlinRowParserBuilders()
val kotlinRowParserBuildersPath = generatedOutputDir.resolve("RowParserBuilders.kt")
Files.writeString(kotlinRowParserBuildersPath, kotlinRowParserBuildersContent)
println(s"Wrote ${kotlinRowParserBuildersPath}")

// RowParserNamedBuilders.kt -> generated-and-checked-in
val kotlinNamedRowParserBuildersContent = generateKotlinNamedRowParserBuilders()
val kotlinNamedRowParserBuildersPath = generatedOutputDir.resolve("RowParserNamedBuilders.kt")
Files.writeString(kotlinNamedRowParserBuildersPath, kotlinNamedRowParserBuildersContent)
println(s"Wrote ${kotlinNamedRowParserBuildersPath}")

// DbProcedure.kt -> generated-and-checked-in
val kotlinDbProcedureContent = generateKotlinDbProcedure()
val kotlinDbProcedurePath = generatedOutputDir.resolve("DbProcedure.kt")
Files.writeString(kotlinDbProcedurePath, kotlinDbProcedureContent)
println(s"Wrote ${kotlinDbProcedurePath}")

// DbFunction.kt -> generated-and-checked-in
val kotlinDbFunctionContent = generateKotlinDbFunction()
val kotlinDbFunctionPath = generatedOutputDir.resolve("DbFunction.kt")
Files.writeString(kotlinDbFunctionPath, kotlinDbFunctionContent)
println(s"Wrote ${kotlinDbFunctionPath}")

// DuckDbStruct.kt -> generated-and-checked-in
val kotlinDuckDbStructContent = generateKotlinDuckDbStructBuilders()
val kotlinDuckDbStructPath = generatedOutputDir.resolve("DuckDbStruct.kt")
Files.writeString(kotlinDuckDbStructPath, kotlinDuckDbStructContent)
println(s"Wrote ${kotlinDuckDbStructPath}")

// PgStruct.kt -> generated-and-checked-in
val kotlinPgStructContent = generateKotlinPgStructBuilders()
val kotlinPgStructPath = generatedOutputDir.resolve("PgStruct.kt")
Files.writeString(kotlinPgStructPath, kotlinPgStructContent)
println(s"Wrote ${kotlinPgStructPath}")

// OracleObject.kt -> generated-and-checked-in
val kotlinOracleObjectContent = generateKotlinOracleObjectBuilders()
val kotlinOracleObjectPath = generatedOutputDir.resolve("OracleObject.kt")
Files.writeString(kotlinOracleObjectPath, kotlinOracleObjectContent)
println(s"Wrote ${kotlinOracleObjectPath}")

// Tuple.kt -> generated-and-checked-in
val kotlinTupleContent = generateKotlinTuple()
val kotlinTuplePath = generatedOutputDir.resolve("Tuple.kt")
Files.writeString(kotlinTuplePath, kotlinTupleContent)
println(s"Wrote ${kotlinTuplePath}")

// SqlTemplate.kt -> generated-and-checked-in
val kotlinSqlTemplateContent = generateKotlinSqlTemplate()
val kotlinSqlTemplatePath = generatedOutputDir.resolve("SqlTemplate.kt")
Files.writeString(kotlinSqlTemplatePath, kotlinSqlTemplateContent)
println(s"Wrote ${kotlinSqlTemplatePath}")

// ParamBuilders.kt -> generated-and-checked-in
val kotlinParamBuildersContent = generateKotlinParamBuilders()
val kotlinParamBuildersPath = generatedOutputDir.resolve("ParamBuilders.kt")
Files.writeString(kotlinParamBuildersPath, kotlinParamBuildersContent)
println(s"Wrote ${kotlinParamBuildersPath}")
