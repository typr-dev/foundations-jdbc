package scripts

import bleep.*
import bleep.internal.FileUtils
import java.nio.file.Files

object SourcegenKotlin extends BleepCodegenScript("SourcegenKotlin") {

  val N = 100
  val PROC_N = 11
  val STRUCT_N = 31

  override def run(started: Started, commands: Commands, targets: List[Target], args: List[String]): Unit = {
    targets.foreach { target =>
      val outputDir = target.sources.resolve("dev/typr/foundationskt")
      Files.createDirectories(outputDir)
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("RowCodecBuilders.kt"), generateKotlinRowCodecBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("RowCodecNamedBuilders.kt"), generateKotlinNamedRowCodecBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("DbProcedure.kt"), generateKotlinDbProcedure())
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("DbFunction.kt"), generateKotlinDbFunction())

      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("Tuple.kt"), generateKotlinTuple())
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("Template.kt"), generateKotlinTemplate())
      FileUtils.writeString(started.logger, Some("SourcegenKotlin"), outputDir.resolve("ParamBuilders.kt"), generateKotlinParamBuilders())
    }
  }

  def generateKotlinRowCodecBuilders(): String = {
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
          |        fun build(decode: ($decodeParams) -> Row): RowCodec<Row> {
          |            val capturedGetters = getters.toList()
          |            val javaParser = dev.typr.foundations.RowCodec.create<Row>(
          |                types.toList(),
          |                { arr -> decode($decodeArgs) },
          |                { row -> capturedGetters.map { it(row) }.toTypedArray() }
          |            )
          |            return RowCodec(javaParser)
          |        }$nextBuilder
          |    }""".stripMargin
    }

    s"""|package dev.typr.foundationskt
        |
        |/**
        | * Type-safe builders for Kotlin RowCodec.
        | *
        | * Usage:
        | * ```kotlin
        | * val parser: RowCodec<Product> = RowCodec.builder<Product>()
        | *     .field(PgTypes.int4, Product::id)
        | *     .field(PgTypes.text, Product::name)
        | *     .field(PgTypes.numeric, Product::price)
        | *     .build(::Product)
        | * ```
        | */
        |object RowCodecBuilders {
        |    fun <Row : Any> builder(): Builder0<Row> = Builder0()
        |
        |$builder0
        |
        |${builders.mkString("\n\n")}
        |}
        |""".stripMargin
  }

  def generateKotlinNamedRowCodecBuilders(): String = {
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
          |        fun build(decode: ($decodeParams) -> Row): RowCodecNamed<Row> {
          |            val capturedGetters = getters.toList()
          |            val javaParser = dev.typr.foundations.RowCodec.createNamed<Row>(
          |                names.toList(),
          |                types.toList(),
          |                { arr -> decode($decodeArgs) },
          |                { row -> capturedGetters.map { it(row) }.toTypedArray() }
          |            )
          |            return RowCodecNamed(javaParser)
          |        }$nextBuilder
          |    }""".stripMargin
    }

    s"""|package dev.typr.foundationskt
        |
        |/**
        | * Type-safe named builders for Kotlin RowCodec.
        | *
        | * Usage:
        | * ```kotlin
        | * val parser: RowCodecNamed<Product> = RowCodec.namedBuilder<Product>()
        | *     .field("id", PgTypes.int4, Product::id)
        | *     .field("name", PgTypes.text, Product::name)
        | *     .field("price", PgTypes.numeric, Product::price)
        | *     .build(::Product)
        | * ```
        | */
        |object RowCodecNamedBuilders {
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
      case 2 => "Pair<O0, O1>"
      case 3 => "Triple<O0, O1, O2>"
      case n => "dev.typr.foundations.Tuple.Tuple" + n + "<" + oParams(n).mkString(", ") + ">"
    }

    // Def interfaces: 11x11
    val defs = for {
      i <- 0 to maxArity
      o <- 0 to maxArity
    } yield {
      val tpDecl = typeParamDecl(allTypeParams(i, o))
      val retType = outType(o)
      s"""    /** Procedure definition with $i input(s) and $o output(s). */
         |    interface Def${i}_${o}$tpDecl : dev.typr.foundations.RoutineDef {
         |        fun call(${callParams(i)}): Operation<$retType>
         |        override fun procedure(): dev.typr.foundations.Procedure<*>
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
        s"""        fun <I$i> input(type: DbType<I$i>): Builder_${i + 1}_${o}${typeParamDecl(nextTp)} =
           |            Builder_${i + 1}_${o}(underlying.input(type.underlying))""".stripMargin
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

      val procOpExpr = o match {
        case 0 => s"Operation.JavaWrapped(javaProc.call($javaCallArgs)).map { }"
        case 1 => s"Operation.JavaWrapped(javaProc.call($javaCallArgs))"
        case 2 => s"Operation.JavaWrapped(javaProc.call($javaCallArgs)).map { t -> Pair(t._1(), t._2()) }"
        case 3 => s"Operation.JavaWrapped(javaProc.call($javaCallArgs)).map { t -> Triple(t._1(), t._2(), t._3()) }"
        case _ => s"Operation.JavaWrapped(javaProc.call($javaCallArgs))"
      }

      s"""    class Builder_${i}_${o}$tpDecl internal constructor(
         |        private val underlying: dev.typr.foundations.DbProcedure.Builder_${i}_${o}$javaTpDecl
         |    ) {
         |$methodsBlock
         |        fun build(): Def${i}_${o}$tpDecl {
         |            val javaProc = underlying.build()
         |            return object : Def${i}_${o}$tpDecl {
         |                override fun call($callParamsStr): Operation<$retType> =
         |                    $procOpExpr
         |                override fun procedure(): dev.typr.foundations.Procedure<*> = javaProc.procedure()
         |            }
         |        }
         |    }""".stripMargin
    }

    s"""|package dev.typr.foundationskt
        |
        |/**
        | * Type-safe stored procedure definitions with fully typed inputs and outputs.
        | *
        | * Usage:
        | * ```kotlin
        | * val getUser: DbProcedure.Def1_2<Int, String, String> = DbProcedure.define("get_user_by_id")
        | *     .input(PgTypes.int4)
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
         |    interface Def$i${typeParamDecl(tp)} : dev.typr.foundations.RoutineDef {
         |        fun call(${callParams(i)}): Operation<R>
         |        override fun procedure(): dev.typr.foundations.Procedure<*>
         |    }""".stripMargin
    }

    // Builder classes: 11 total
    val builders = (0 to maxArity).map { i =>
      val tp = iParams(i) ::: List("R")
      val tpDecl = typeParamDecl(tp)

      val inMethod = if (i < maxArity) {
        val nextTp = iParams(i + 1) ::: List("R")
        s"""        fun <I$i> input(type: DbType<I$i>): Builder_${i + 1}${typeParamDecl(nextTp)} =
           |            Builder_${i + 1}(underlying.input(type.underlying))
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
         |            return object : Def$i$tpDecl {
         |                override fun call($callParamsStr): Operation<R> =
         |                    Operation.JavaWrapped(javaFn.call($javaCallArgs))
         |                override fun procedure(): dev.typr.foundations.Procedure<*> = javaFn.procedure()
         |            }
         |        }
         |    }""".stripMargin
    }

    s"""|package dev.typr.foundationskt
        |
        |/**
        | * Type-safe stored function definitions with fully typed inputs.
        | *
        | * Usage:
        | * ```kotlin
        | * val calcTax: DbFunction.Def2<BigDecimal, String, BigDecimal> = DbFunction.define("calculate_tax", PgTypes.numeric)
        | *     .input(PgTypes.numeric)
        | *     .input(PgTypes.text)
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
        |package dev.typr.foundationskt
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

  def generateKotlinTemplate(): String = {
    val maxArity = PROC_N - 1 // 10

    def inputType(n: Int): String = {
      if (n == 1) "P0"
      else {
        val tparams = 0.until(n).map(i => s"P$i").mkString(", ")
        s"dev.typr.foundations.Tuple.Tuple$n<$tparams>"
      }
    }

    val queryClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val allTparams = s"$tparams, Out"
      val inType = inputType(n)
      val fromFnParams = range.map(i => s"f$i: (T) -> P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")
      val fromTupleArgs = if (n == 1) fromApplyArgs else s"dev.typr.foundations.Tuple.of($fromApplyArgs)"

      val onMultiArg =
        if (n == 1) ""
        else {
          val onParams = range.map(i => s"p$i: P$i").mkString(", ")
          val onArgs = range.map(i => s"p$i").mkString(", ")
          s"""
            |
            |        fun on($onParams): OperationRead<Out> =
            |            on(dev.typr.foundations.Tuple.of($onArgs))"""
        }

      s"""|    class Query$n<$allTparams>(
          |        override val underlying: dev.typr.foundations.TemplateRead<$inType, Out>
          |    ) : TemplateRead<$inType, Out> {
          |        override fun on(input: $inType): OperationRead<Out> =
          |            OperationRead.JavaWrapped(underlying.on(input))$onMultiArg
          |
          |        fun <T> from($fromFnParams): TemplateRead.FromRead<T, Out> =
          |            TemplateRead.FromRead(dev.typr.foundations.TemplateRead.FromRead(underlying) { t -> underlying.on($fromTupleArgs) })
          |    }""".stripMargin
    }

    val updateClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val inType = inputType(n)
      val fromFnParams = range.map(i => s"f$i: (T) -> P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")
      val fromTupleArgs = if (n == 1) fromApplyArgs else s"dev.typr.foundations.Tuple.of($fromApplyArgs)"

      val onMultiArg =
        if (n == 1) ""
        else {
          val onParams = range.map(i => s"p$i: P$i").mkString(", ")
          val onArgs = range.map(i => s"p$i").mkString(", ")
          s"""
            |
            |        fun on($onParams): Operation<Int> =
            |            on(dev.typr.foundations.Tuple.of($onArgs))"""
        }

      s"""|    class Update$n<$tparams>(
          |        override val underlying: dev.typr.foundations.Template<$inType, Int>
          |    ) : Template<$inType, Int> {
          |        override fun on(input: $inType): Operation<Int> =
          |            Operation.JavaWrapped(underlying.on(input))$onMultiArg
          |
          |        fun <T> from($fromFnParams): Template.From<T, Int> =
          |            Template.From(dev.typr.foundations.Template.From(underlying) { t -> underlying.on($fromTupleArgs) })
          |    }""".stripMargin
    }

    s"""|@file:Suppress("unused")
        |package dev.typr.foundationskt
        |
        |interface Template<In, Out> : Analyzable {
        |    val underlying: dev.typr.foundations.Template<In, Out>
        |
        |    override val analyzable: dev.typr.foundations.Analyzable get() = underlying
        |
        |    fun on(input: In): Operation<Out> = Operation.JavaWrapped(underlying.on(input))
        |
        |    fun fragment(): Fragment = Fragment(underlying.fragment())
        |
        |${updateClasses.mkString("\n\n")}
        |
        |    class From<T, Out>(
        |        override val underlying: dev.typr.foundations.Template.From<T, Out>
        |    ) : Template<T, Out> {
        |        override fun on(input: T): Operation<Out> = Operation.JavaWrapped(underlying.on(input))
        |    }
        |
        |    class Contramapped<In2, In, Out>(
        |        override val underlying: dev.typr.foundations.Template.Contramapped<In2, In, Out>
        |    ) : Template<In2, Out> {
        |        override fun on(input: In2): Operation<Out> = Operation.JavaWrapped(underlying.on(input))
        |    }
        |}
        |
        |interface TemplateRead<In, Out> : Template<In, Out> {
        |    override val underlying: dev.typr.foundations.TemplateRead<In, Out>
        |
        |    override fun on(input: In): OperationRead<Out> = OperationRead.JavaWrapped(underlying.on(input))
        |
        |${queryClasses.mkString("\n\n")}
        |
        |    class FromRead<T, Out>(
        |        override val underlying: dev.typr.foundations.TemplateRead.FromRead<T, Out>
        |    ) : TemplateRead<T, Out> {
        |        override fun on(input: T): OperationRead<Out> = OperationRead.JavaWrapped(underlying.on(input))
        |    }
        |
        |    class ContramappedRead<In2, In, Out>(
        |        override val underlying: dev.typr.foundations.TemplateRead.ContramappedRead<In2, In, Out>
        |    ) : TemplateRead<In2, Out> {
        |        override fun on(input: In2): OperationRead<Out> = OperationRead.JavaWrapped(underlying.on(input))
        |    }
        |}
        |""".stripMargin
  }

  def generateKotlinParamBuilders(): String = {
    val maxArity = PROC_N - 1 // 10

    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val stars = range.map(_ => "*").mkString(", ")
      val jtparams = range.map(i => s"JP$i").mkString(", ")
      val allImplTparams = range.map(i => s"P$i, JP$i").mkString(", ")
      val implOutJava = range.map(i => s"P$i, out Any?").mkString(", ")
      val bijections = range.map(i => s"b$i").mkString(", ")
      val bijectionFields = range.map(i => s"val b$i: dev.typr.foundations.Bijection<JP$i, P$i>").mkString(", ")
      val bijectionParams = range.map(i => s"b$i: dev.typr.foundations.Bijection<JP$i, P$i>").mkString(", ")
      val identityBijections = range.map(_ => "dev.typr.foundations.Bijection.identity()").mkString(", ")

      // contramapInput function: converts Kotlin input to Java input
      val contramapBody = if (n == 1) {
        "b0.from(input)"
      } else {
        val components = range.map(i => s"b$i.from(input._${i + 1}())")
        s"dev.typr.foundations.Tuple.of(${components.mkString(", ")})"
      }
      val kotlinInputType = if (n == 1) "P0" else s"dev.typr.foundations.Tuple.Tuple$n<$tparams>"

      val nextParamMethod = if (n < maxArity) {
        s"""|
            |        fun <P$n> param(type: DbType<P$n>): ParamBuilder${n + 1}<$tparams, P$n> =
            |            _queryFn.param(type.underlying, dev.typr.foundations.Bijection.identity<P$n>())""".stripMargin
      } else ""

      val optionallyMethods = if (n < maxArity) {
        s"""|
            |        fun optionally(inner: Fragment): ParamBuilder${n + 1}<$tparams, Boolean> =
            |            _queryFn.optionallyFragment(inner.underlying)
            |
            |        fun <A : Any> optionally(builder: ParamBuilder1<A>): ParamBuilder${n + 1}<$tparams, A?> =
            |            _queryFn.optionally1(builder._queryFn)
            |
            |        fun <A : Any, B : Any> optionally(builder: ParamBuilder2<A, B>): ParamBuilder${n + 1}<$tparams, Pair<A, B>?> =
            |            _queryFn.optionally2(builder._queryFn)
            |
            |        fun <A : Any, B : Any, C : Any> optionally(builder: ParamBuilder3<A, B, C>): ParamBuilder${n + 1}<$tparams, Triple<A, B, C>?> =
            |            _queryFn.optionally3(builder._queryFn)""".stripMargin
      } else ""

      // The key insight: ParamBuilderOps captures the typed Java builder and bijections
      // in its closure. All methods work on typed values. The user-facing class delegates.
      s"""|    internal interface ParamBuilder${n}Ops<$tparams> {
          |        fun appendStr(s: String): ParamBuilder$n<$tparams>
          |        fun <T> addValue(type: dev.typr.foundations.DbType<T>, value: T): ParamBuilder$n<$tparams>
          |        fun appendFrag(other: dev.typr.foundations.Fragment): ParamBuilder$n<$tparams>
          |${
           if (n < maxArity)
             s"""        fun <P$n> param(type: dev.typr.foundations.DbType<P$n>, bij: dev.typr.foundations.Bijection<P$n, P$n>): ParamBuilder${n + 1}<$tparams, P$n>
          |        fun optionallyFragment(inner: dev.typr.foundations.Fragment): ParamBuilder${n + 1}<$tparams, Boolean>
          |        fun <A : Any> optionally1(inner: ParamBuilder1Ops<A>): ParamBuilder${n + 1}<$tparams, A?>
          |        fun <A : Any, B : Any> optionally2(inner: ParamBuilder2Ops<A, B>): ParamBuilder${n + 1}<$tparams, Pair<A, B>?>
          |        fun <A : Any, B : Any, C : Any> optionally3(inner: ParamBuilder3Ops<A, B, C>): ParamBuilder${n + 1}<$tparams, Triple<A, B, C>?>"""
           else ""
         }
          |        fun <Out> buildQuery(parser: dev.typr.foundations.ResultSetParser<Out>): TemplateRead.Query$n<$tparams, Out>
          |        fun buildUpdate(): Template.Update$n<$tparams>
          |        fun buildDone(): dev.typr.foundations.Fragment
          |        val javaUnderlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n<$stars>
          |    }
          |
          |    internal fun <$allImplTparams> createOps$n(
          |        underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n<$jtparams>,
          |        $bijectionParams
          |    ): ParamBuilder${n}Ops<$tparams> = object : ParamBuilder${n}Ops<$tparams> {
          |        override fun appendStr(s: String) = ParamBuilder$n(createOps$n(underlying.append(s), $bijections))
          |        override fun <T> addValue(type: dev.typr.foundations.DbType<T>, value: T) = ParamBuilder$n(createOps$n(underlying.value(type, value), $bijections))
          |        override fun appendFrag(other: dev.typr.foundations.Fragment) = ParamBuilder$n(createOps$n(underlying.append(other), $bijections))
          |${
           if (n < maxArity) s"""        override fun <P$n> param(type: dev.typr.foundations.DbType<P$n>, bij: dev.typr.foundations.Bijection<P$n, P$n>) =
          |            ParamBuilder${n + 1}(createOps${n + 1}(underlying.param(type), $bijections, bij))
          |        override fun optionallyFragment(inner: dev.typr.foundations.Fragment) =
          |            ParamBuilder${n + 1}(createOps${n + 1}(underlying.optionally(inner), $bijections, dev.typr.foundations.Bijection.identity()))
          |        override fun <A : Any> optionally1(inner: ParamBuilder1Ops<A>): ParamBuilder${n + 1}<$tparams, A?> {
          |            val innerFrag = inner.buildDone()
          |            val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |            val newJava = dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}<$jtparams, java.util.Optional<A>>(newFrag, ${range
               .map(i => s"underlying.p${i}Type()")
               .mkString(", ")}, null)
          |            return ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections, dev.typr.foundations.Bijection.optionalToNullable<A>()))
          |        }
          |        override fun <A : Any, B : Any> optionally2(inner: ParamBuilder2Ops<A, B>): ParamBuilder${n + 1}<$tparams, Pair<A, B>?> {
          |            val innerFrag = inner.buildDone()
          |            val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |            val newJava = dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}<$jtparams, java.util.Optional<dev.typr.foundations.Tuple.Tuple2<A, B>>>(newFrag, ${range
               .map(i => s"underlying.p${i}Type()")
               .mkString(", ")}, null)
          |            return ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections,
          |                dev.typr.foundations.Bijection.optionalToNullable<dev.typr.foundations.Tuple.Tuple2<A, B>>().andThen(Bijection.andToPair())))
          |        }
          |        override fun <A : Any, B : Any, C : Any> optionally3(inner: ParamBuilder3Ops<A, B, C>): ParamBuilder${n + 1}<$tparams, Triple<A, B, C>?> {
          |            val innerFrag = inner.buildDone()
          |            val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |            val newJava = dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}<$jtparams, java.util.Optional<dev.typr.foundations.Tuple.Tuple3<A, B, C>>>(newFrag, ${range
               .map(i => s"underlying.p${i}Type()")
               .mkString(", ")}, null)
          |            return ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections,
          |                dev.typr.foundations.Bijection.optionalToNullable<dev.typr.foundations.Tuple.Tuple3<A, B, C>>().andThen(
          |                    dev.typr.foundations.Bijection.of({ t: dev.typr.foundations.Tuple.Tuple3<A, B, C> -> Triple(t._1(), t._2(), t._3()) },
          |                        { t: Triple<A, B, C> -> dev.typr.foundations.Tuple.of(t.first, t.second, t.third) }))))
          |        }"""
           else ""
         }
          |        override fun <Out> buildQuery(parser: dev.typr.foundations.ResultSetParser<Out>): TemplateRead.Query$n<$tparams, Out> =
          |            TemplateRead.Query$n(underlying.query(parser).contramapInput { input: $kotlinInputType -> $contramapBody })
          |        override fun buildUpdate(): Template.Update$n<$tparams> =
          |            Template.Update$n(underlying.update().contramapInput { input: $kotlinInputType -> $contramapBody })
          |        override fun buildDone() = underlying.done()
          |        override val javaUnderlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n<$stars> get() = underlying
          |    }
          |
          |    class ParamBuilder$n<$tparams> internal constructor(
          |        internal val _queryFn: ParamBuilder${n}Ops<$tparams>
          |    ) {
          |        constructor(j: dev.typr.foundations.ParamBuilders.ParamBuilder$n<$tparams>) : this(createOps$n(j, $identityBijections))
          |
          |        fun append(s: String): ParamBuilder$n<$tparams> = _queryFn.appendStr(s)
          |        fun <T> value(type: DbType<T>, value: T): ParamBuilder$n<$tparams> = _queryFn.addValue(type.underlying, value)
          |        fun append(fragment: Fragment): ParamBuilder$n<$tparams> = _queryFn.appendFrag(fragment.underlying)
          |$nextParamMethod$optionallyMethods
          |        fun <Out> query(parser: ResultSetParser<Out>): TemplateRead.Query$n<$tparams, Out> = _queryFn.buildQuery(parser.underlying)
          |        fun update(): Template.Update$n<$tparams> = _queryFn.buildUpdate()
          |        fun done(): Fragment = Fragment(_queryFn.buildDone())
          |    }""".stripMargin
    }

    s"""|@file:Suppress("unused")
        |package dev.typr.foundationskt
        |
        |object ParamBuilders {
        |${builders.mkString("\n\n")}
        |}
        |""".stripMargin
  }

  // ─────────────────────────────────────────────────────────────────────────────
}
