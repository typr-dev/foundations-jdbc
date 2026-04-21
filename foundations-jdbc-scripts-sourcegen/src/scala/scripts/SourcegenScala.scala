package scripts

import bleep.*
import bleep.internal.FileUtils
import java.nio.file.Files

object SourcegenScala extends BleepCodegenScript("SourcegenScala") {

  val N = 100
  val PROC_N = 11
  val STRUCT_N = 31

  override def run(started: Started, commands: Commands, targets: List[Target], args: List[String]): Unit = {
    targets.foreach { target =>
      val outputDir = target.sources.resolve("dev/typr/foundationssc")
      Files.createDirectories(outputDir)
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("RowCodecBuilders.scala"), generateScalaRowCodecBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("RowCodecNamedBuilders.scala"), generateScalaNamedRowCodecBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("DbProcedure.scala"), generateScalaDbProcedure())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("DbFunction.scala"), generateScalaDbFunction())

      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("Tuple.scala"), generateScalaTuple())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("Template.scala"), generateScalaTemplate())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("ParamBuilders.scala"), generateScalaParamBuilders())
    }
  }

  def generateScalaRowCodecBuilders(): String = {
    val maxArity = N - 1

    val builder0 = s"""|  class Builder0[Row] private[foundationssc] () {
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

      s"""|  class Builder$n[Row, $tparams] private[foundationssc] (
          |    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
          |    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
          |  ) {
          |    def build(decode: ($decodeParams) => Row): RowCodec[Row] = {
          |      val capturedGetters = getters.toList
          |      val javaParser = dev.typr.foundations.RowCodec.create[Row](
          |        java.util.List.copyOf(types.map(_.underlying).asJava),
          |        arr => decode($decodeArgs),
          |        row => capturedGetters.map(_(row)).toArray
          |      )
          |      new RowCodec(javaParser)
          |    }$nextBuilder
          |  }""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |import scala.jdk.CollectionConverters.*
        |
        |/** Type-safe builders for Scala RowCodec.
        |  *
        |  * Usage:
        |  * {{{
        |  * val parser: RowCodec[Product] = RowCodec.builder[Product]()
        |  *   .field(PgTypes.int4)(_.id)
        |  *   .field(PgTypes.text)(_.name)
        |  *   .field(PgTypes.numeric)(_.price)
        |  *   .build(Product.apply)
        |  * }}}
        |  */
        |object RowCodecBuilders {
        |  def builder[Row](): Builder0[Row] = new Builder0()
        |
        |$builder0
        |
        |${builders.mkString("\n\n")}
        |}
        |""".stripMargin
  }

  def generateScalaNamedRowCodecBuilders(): String = {
    val maxArity = N - 1

    val builder0 = s"""|  class Builder0[Row] private[foundationssc] () {
                       |    private val names = scala.collection.mutable.ListBuffer[String]()
                       |    private val types = scala.collection.mutable.ListBuffer[DbType[?]]()
                       |    private val getters = scala.collection.mutable.ListBuffer[Row => Any]()
                       |
                       |    def field[F](name: String, tpe: DbType[F])(getter: Row => F): Builder1[Row, F] = {
                       |      names += name
                       |      types += tpe
                       |      getters += getter.asInstanceOf[Row => Any]
                       |      new Builder1(names, types, getters)
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
            |    def field[F](name: String, tpe: DbType[F])(getter: Row => F): Builder${n + 1}[Row, $nextTparams, F] = {
            |      names += name
            |      types += tpe
            |      getters += getter.asInstanceOf[Row => Any]
            |      new Builder${n + 1}(names, types, getters)
            |    }""".stripMargin
      } else ""

      s"""|  class Builder$n[Row, $tparams] private[foundationssc] (
          |    private val names: scala.collection.mutable.ListBuffer[String],
          |    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
          |    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
          |  ) {
          |    def build(decode: ($decodeParams) => Row): RowCodecNamed[Row] = {
          |      val capturedGetters = getters.toList
          |      val javaParser = dev.typr.foundations.RowCodec.createNamed[Row](
          |        java.util.List.copyOf(names.asJava),
          |        java.util.List.copyOf(types.map(_.underlying).asJava),
          |        arr => decode($decodeArgs),
          |        row => capturedGetters.map(_(row)).toArray
          |      )
          |      new RowCodecNamed(javaParser)
          |    }$nextBuilder
          |  }""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |import scala.jdk.CollectionConverters.*
        |
        |/** Type-safe named builders for Scala RowCodec.
        |  *
        |  * Usage:
        |  * {{{
        |  * val parser: RowCodecNamed[Product] = RowCodec.namedBuilder[Product]()
        |  *   .field("id", PgTypes.int4)(_.id)
        |  *   .field("name", PgTypes.text)(_.name)
        |  *   .field("price", PgTypes.numeric)(_.price)
        |  *   .build(Product.apply)
        |  * }}}
        |  */
        |object RowCodecNamedBuilders {
        |  def builder[Row](): Builder0[Row] = new Builder0()
        |
        |$builder0
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
      case n => "(" + oParams(n).mkString(", ") + ")"
    }

    // Def traits: 11x11
    val defs = for {
      i <- 0 to maxArity
      o <- 0 to maxArity
    } yield {
      val tpDecl = typeParamDecl(allTypeParams(i, o))
      val retType = outType(o)
      s"""  /** Procedure definition with $i input(s) and $o output(s). */
         |  trait Def${i}_${o}$tpDecl extends dev.typr.foundations.RoutineDef {
         |    def call(${callParams(i)}): Operation[$retType]
         |    def procedure: dev.typr.foundations.Procedure[?]
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
        s"""    def input[I$i](tpe: DbType[I$i]): Builder_${i + 1}_${o}${typeParamDecl(nextTp)} =
           |      new Builder_${i + 1}_${o}(underlying.input(tpe.underlying))""".stripMargin
      } else ""

      // out method
      val outMethod = if (o < maxArity) {
        val nextTp = allTypeParams(i, o + 1)
        s"""    def out[O$o](tpe: DbType[O$o]): Builder_${i}_${o + 1}${typeParamDecl(nextTp)} =
           |      new Builder_${i}_${o + 1}(underlying.out(tpe.underlying))""".stripMargin
      } else ""

      // inout method
      val inoutMethod = if (i < maxArity && o < maxArity) {
        val inoutTp = iParams(i) ::: List("X") ::: oParams(o) ::: List("X")
        s"""    def inout[X](tpe: DbType[X]): Builder_${i + 1}_${o + 1}${typeParamDecl(inoutTp)} =
           |      new Builder_${i + 1}_${o + 1}(underlying.inout(tpe.underlying))""".stripMargin
      } else ""

      val methods = List(inMethod, outMethod, inoutMethod).filter(_.nonEmpty).mkString("\n")
      val methodsBlock = if (methods.nonEmpty) s"$methods\n" else ""

      // build method
      val retType = outType(o)
      val defTpDecl = typeParamDecl(allTypeParams(i, o))
      val callParamsStr = callParams(i)
      val callArgNamesStr = callArgNames(i)

      val mapExpr = o match {
        case 0 => ".map(_ => ())"
        case 1 => ""
        case n =>
          val accessors = 0.until(n).map(i => s"t._${i + 1}()").mkString(", ")
          s".map(t => ($accessors))"
      }

      val javaCallArgs = if (i == 0) "" else callArgNamesStr

      s"""  class Builder_${i}_${o}$tpDecl private[foundationssc] (
         |    private val underlying: dev.typr.foundations.DbProcedure.Builder_${i}_${o}$javaTpDecl
         |  ) {
         |$methodsBlock
         |    def build(): Def${i}_${o}$defTpDecl = {
         |      val javaDef = underlying.build()
         |      new Def${i}_${o}$defTpDecl {
         |        def call($callParamsStr): Operation[$retType] =
         |          new Operation.JavaWrapped(javaDef.call($javaCallArgs))$mapExpr
         |        override def procedure: dev.typr.foundations.Procedure[?] = javaDef.procedure()
         |      }
         |    }
         |  }""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |/** Type-safe stored procedure definitions with fully typed inputs and outputs.
        |  *
        |  * Usage:
        |  * {{{
        |  * val getUser: DbProcedure.Def1_2[Int, String, String] = DbProcedure.define("get_user_by_id")
        |  *   .input(PgTypes.int4)
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
         |  trait Def$i${typeParamDecl(tp)} extends dev.typr.foundations.RoutineDef {
         |    def call(${callParams(i)}): Operation[R]
         |    def procedure: dev.typr.foundations.Procedure[?]
         |  }""".stripMargin
    }

    // Builder classes: 11 total
    val builders = (0 to maxArity).map { i =>
      val tp = iParams(i) ::: List("R")
      val tpDecl = typeParamDecl(tp)
      val javaTpDecl = typeParamDecl(tp)

      val inMethod = if (i < maxArity) {
        val nextTp = iParams(i + 1) ::: List("R")
        s"""    def input[I$i](tpe: DbType[I$i]): Builder_${i + 1}${typeParamDecl(nextTp)} =
           |      new Builder_${i + 1}(underlying.input(tpe.underlying))
           |""".stripMargin
      } else ""

      val callParamsStr = callParams(i)
      val javaCallArgs = if (i == 0) "" else callArgNames(i)

      s"""  class Builder_$i$tpDecl private[foundationssc] (
         |    private val underlying: dev.typr.foundations.DbFunction.Builder_$i$javaTpDecl
         |  ) {
         |$inMethod
         |    def build(): Def$i$tpDecl = {
         |      val javaDef = underlying.build()
         |      new Def$i$tpDecl {
         |        def call($callParamsStr): Operation[R] =
         |          new Operation.JavaWrapped(javaDef.call($javaCallArgs))
         |        override def procedure: dev.typr.foundations.Procedure[?] = javaDef.procedure()
         |      }
         |    }
         |  }""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |/** Type-safe stored function definitions with fully typed inputs.
        |  *
        |  * Usage:
        |  * {{{
        |  * val calcTax: DbFunction.Def2[BigDecimal, String, BigDecimal] = DbFunction.define("calculate_tax", PgTypes.numeric)
        |  *   .input(PgTypes.numeric)
        |  *   .input(PgTypes.text)
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
        |    new Builder_0(dev.typr.foundations.DbFunction.define(name, returnType.underlying))
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

  def generateScalaTuple(): String = {
    val typeAliases = 1.to(N).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"T$i").mkString(", ")
      s"  type Tuple$n[$tparams] = dev.typr.foundations.Tuple.Tuple$n[$tparams]"
    }

    val factories = 1.to(N).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"T$i").mkString(", ")
      val params = range.map(i => s"v$i: T$i").mkString(", ")
      val args = range.map(i => s"v$i").mkString(", ")
      s"  def of[$tparams]($params): dev.typr.foundations.Tuple.Tuple$n[$tparams] =\n    dev.typr.foundations.Tuple.of($args)"
    }

    s"""|package dev.typr.foundationssc
        |
        |type Tuple = dev.typr.foundations.Tuple
        |
        |object Tuple:
        |${typeAliases.mkString("\n")}
        |
        |${factories.mkString("\n\n")}
        |""".stripMargin
  }

  def generateScalaTemplate(): String = {
    val maxArity = PROC_N - 1 // 10

    def inputType(n: Int): String =
      if (n == 1) "P0"
      else s"(${0.until(n).map(i => s"P$i").mkString(", ")})"

    def javaInputType(n: Int): String =
      if (n == 1) "P0"
      else {
        val tparams = 0.until(n).map(i => s"P$i").mkString(", ")
        s"dev.typr.foundations.Tuple.Tuple$n[$tparams]"
      }

    val queryClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val allTparams = s"$tparams, Out"
      val inType = inputType(n)
      val jInType = javaInputType(n)
      val fromFnParams = range.map(i => s"f$i: T => P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")

      val onMultiArg =
        if (n == 1) ""
        else {
          val onParams = range.map(i => s"p$i: P$i").mkString(", ")
          val scalaTuple = s"(${range.map(i => s"p$i").mkString(", ")})"
          s"""
            |    def on($onParams): OperationRead[Out] =
            |      on($scalaTuple)"""
        }

      s"""|  class Query$n[$allTparams](
          |    override val underlying: dev.typr.foundations.TemplateRead[$inType, Out]
          |  ) extends TemplateRead[$inType, Out]:
          |    override def on(input: $inType): OperationRead[Out] =
          |      new OperationRead.JavaWrapped(underlying.on(input))$onMultiArg
          |    def from[T]($fromFnParams): TemplateRead.FromRead[T, Out] =
          |      new TemplateRead.FromRead(new dev.typr.foundations.TemplateRead.FromRead(underlying, (t: T) => underlying.on(${
           if (n == 1) fromApplyArgs else s"(${fromApplyArgs})"
         })))""".stripMargin
    }

    val updateClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val inType = inputType(n)
      val jInType = javaInputType(n)
      val fromFnParams = range.map(i => s"f$i: T => P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")

      val onMultiArg =
        if (n == 1) ""
        else {
          val onParams = range.map(i => s"p$i: P$i").mkString(", ")
          val scalaTuple = s"(${range.map(i => s"p$i").mkString(", ")})"
          s"""
            |    def on($onParams): Operation[Integer] =
            |      on($scalaTuple)"""
        }

      s"""|  class Update$n[$tparams](
          |    override val underlying: dev.typr.foundations.Template[$inType, Integer]
          |  ) extends Template[$inType, Integer]:
          |    override def on(input: $inType): Operation[Integer] =
          |      new Operation.JavaWrapped(underlying.on(input))$onMultiArg
          |    def from[T]($fromFnParams): Template.From[T, Integer] =
          |      new Template.From(new dev.typr.foundations.Template.From(underlying, (t: T) => underlying.on(${
           if (n == 1) fromApplyArgs else s"(${fromApplyArgs})"
         })))""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |trait Template[In, Out] extends Analyzable:
        |  def underlying: dev.typr.foundations.Template[In, Out]
        |
        |  override def analyzable: dev.typr.foundations.Analyzable = underlying
        |
        |  def on(input: In): Operation[Out] = new Operation.JavaWrapped(underlying.on(input))
        |
        |  def fragment: Fragment = new Fragment(underlying.fragment())
        |
        |object Template:
        |
        |${updateClasses.mkString("\n\n")}
        |
        |  class From[T, Out](override val underlying: dev.typr.foundations.Template.From[T, Out])
        |      extends Template[T, Out]:
        |    override def on(input: T): Operation[Out] = new Operation.JavaWrapped(underlying.on(input))
        |
        |  class Contramapped[In2, In, Out](override val underlying: dev.typr.foundations.Template.Contramapped[In2, In, Out])
        |      extends Template[In2, Out]:
        |    override def on(input: In2): Operation[Out] = new Operation.JavaWrapped(underlying.on(input))
        |
        |trait TemplateRead[In, Out] extends Template[In, Out]:
        |  override def underlying: dev.typr.foundations.TemplateRead[In, Out]
        |
        |  override def on(input: In): OperationRead[Out] = new OperationRead.JavaWrapped(underlying.on(input))
        |
        |object TemplateRead:
        |
        |${queryClasses.mkString("\n\n")}
        |
        |  class FromRead[T, Out](override val underlying: dev.typr.foundations.TemplateRead.FromRead[T, Out])
        |      extends TemplateRead[T, Out]:
        |    override def on(input: T): OperationRead[Out] = new OperationRead.JavaWrapped(underlying.on(input))
        |
        |  class ContramappedRead[In2, In, Out](override val underlying: dev.typr.foundations.TemplateRead.ContramappedRead[In2, In, Out])
        |      extends TemplateRead[In2, Out]:
        |    override def on(input: In2): OperationRead[Out] = new OperationRead.JavaWrapped(underlying.on(input))
        |
        |sealed trait RowTemplate[Row, Out] extends Template[Row, Out]
        |
        |object RowTemplate:
        |  class Query[Row, Out](override val underlying: dev.typr.foundations.RowTemplate.Query[Row, Out])
        |      extends RowTemplate[Row, Out] with TemplateRead[Row, Out]:
        |    override def on(input: Row): OperationRead.Query[Out] = new OperationRead.Query(underlying.on(input))
        |
        |  class Update[Row](override val underlying: dev.typr.foundations.RowTemplate.Update[Row])
        |      extends RowTemplate[Row, Integer]:
        |    override def on(input: Row): Operation[Integer] =
        |      new Operation.JavaWrapped(underlying.on(input))
        |    def onMany(rows: Iterator[Row]): Operation.UpdateManyTemplate[Row] =
        |      import _root_.scala.jdk.CollectionConverters.*
        |      new Operation.UpdateManyTemplate(underlying.onMany(rows.asJava))
        |
        |  class GeneratedKeys[Row, Out](override val underlying: dev.typr.foundations.RowTemplate.GeneratedKeys[Row, Out])
        |      extends RowTemplate[Row, Out]:
        |    override def on(input: Row): Operation[Out] =
        |      new Operation.JavaWrapped(underlying.on(input))
        |""".stripMargin
  }

  def generateScalaParamBuilders(): String = {
    val maxArity = PROC_N - 1 // 10

    def pbInputType(n: Int): String =
      if (n == 1) "P0"
      else s"(${0.until(n).map(i => s"P$i").mkString(", ")})"

    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val stars = range.map(_ => "?").mkString(", ")
      val jtparams = range.map(i => s"JP$i").mkString(", ")
      val allImplTparams = range.map(i => s"P$i, JP$i").mkString(", ")
      val bijections = range.map(i => s"b$i").mkString(", ")
      val bijectionParams = range.map(i => s"b$i: dev.typr.foundations.Bijection[JP$i, P$i]").mkString(", ")
      val identityBijections = range.map(_ => "dev.typr.foundations.Bijection.identity()").mkString(", ")
      val inType = pbInputType(n)

      val contramapBody = if (n == 1) {
        "b0.from(input)"
      } else {
        val components = range.map(i => s"b$i.from(input._${i + 1})")
        s"dev.typr.foundations.Tuple.of(${components.mkString(", ")})"
      }

      val opsNextParam = if (n < maxArity) {
        s"""
          |    def param[P$n](tpe: dev.typr.foundations.DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n] =
          |      new ParamBuilder${n + 1}(createOps${n + 1}(underlying.param(tpe), $bijections, dev.typr.foundations.Bijection.identity()))
          |    def optionallyFragment(inner: dev.typr.foundations.Fragment): ParamBuilder${n + 1}[$tparams, Boolean] =
          |      new ParamBuilder${n + 1}(createOps${n + 1}(underlying.optionally(inner), $bijections,
          |        dev.typr.foundations.Bijection.of[java.lang.Boolean, Boolean]((jb: java.lang.Boolean) => jb: Boolean, (sb: Boolean) => sb: java.lang.Boolean)))
          |    def optionally1[A](inner: ParamBuilder1Ops[A]): ParamBuilder${n + 1}[$tparams, Option[A]] =
          |      val innerFrag = inner.buildDone()
          |      val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |      val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}[$jtparams, java.util.Optional[A]](newFrag, ${range
            .map(i => s"underlying.p${i}Type()")
            .mkString(", ")}, null)
          |      new ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections, Bijections.optionalToOption[A]))
          |    def optionally2[A, B](inner: ParamBuilder2Ops[A, B]): ParamBuilder${n + 1}[$tparams, Option[(A, B)]] =
          |      val innerFrag = inner.buildDone()
          |      val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |      val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}[$jtparams, java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]]](newFrag, ${range
            .map(i => s"underlying.p${i}Type()")
            .mkString(", ")}, null)
          |      val bij = dev.typr.foundations.Bijection.of[java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]], Option[(A, B)]](
          |        (opt: java.util.Optional[dev.typr.foundations.Tuple.Tuple2[A, B]]) => if opt.isPresent then Some((opt.get._1(), opt.get._2())) else None,
          |        (v: Option[(A, B)]) => { import _root_.scala.jdk.OptionConverters.*; v.map(t => dev.typr.foundations.Tuple.of(t._1, t._2)).toJava })
          |      new ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections, bij))
          |    def optionally3[A, B, C](inner: ParamBuilder3Ops[A, B, C]): ParamBuilder${n + 1}[$tparams, Option[(A, B, C)]] =
          |      val innerFrag = inner.buildDone()
          |      val newFrag = underlying.fragment().append(dev.typr.foundations.Fragment.Optionally(innerFrag, dev.typr.foundations.Fragment.countParams(innerFrag)))
          |      val newJava = new dev.typr.foundations.ParamBuilders.ParamBuilder${n + 1}[$jtparams, java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]]](newFrag, ${range
            .map(i => s"underlying.p${i}Type()")
            .mkString(", ")}, null)
          |      val bij = dev.typr.foundations.Bijection.of[java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]], Option[(A, B, C)]](
          |        (opt: java.util.Optional[dev.typr.foundations.Tuple.Tuple3[A, B, C]]) => if opt.isPresent then Some((opt.get._1(), opt.get._2(), opt.get._3())) else None,
          |        (v: Option[(A, B, C)]) => { import _root_.scala.jdk.OptionConverters.*; v.map(t => dev.typr.foundations.Tuple.of(t._1, t._2, t._3)).toJava })
          |      new ParamBuilder${n + 1}(createOps${n + 1}(newJava, $bijections, bij))"""
      } else ""

      val userNextParam = if (n < maxArity) {
        s"""
          |    def param[P$n](tpe: DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n] = ops.param(tpe.underlying)
          |    def optionally(inner: Fragment): ParamBuilder${n + 1}[$tparams, Boolean] = ops.optionallyFragment(inner.underlying)
          |    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder${n + 1}[$tparams, Option[A]] = ops.optionally1(builder.ops)
          |    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder${n + 1}[$tparams, Option[(A, B)]] = ops.optionally2(builder.ops)
          |    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder${n + 1}[$tparams, Option[(A, B, C)]] = ops.optionally3(builder.ops)"""
      } else ""

      s"""|  trait ParamBuilder${n}Ops[$tparams]:
          |    def appendStr(s: String): ParamBuilder$n[$tparams]
          |    def addValue[T](tpe: dev.typr.foundations.DbType[T], value: T): ParamBuilder$n[$tparams]
          |    def appendFrag(other: dev.typr.foundations.Fragment): ParamBuilder$n[$tparams]
          |${
           if (n < maxArity) s"""    def param[P$n](tpe: dev.typr.foundations.DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n]
          |    def optionallyFragment(inner: dev.typr.foundations.Fragment): ParamBuilder${n + 1}[$tparams, Boolean]
          |    def optionally1[A](inner: ParamBuilder1Ops[A]): ParamBuilder${n + 1}[$tparams, Option[A]]
          |    def optionally2[A, B](inner: ParamBuilder2Ops[A, B]): ParamBuilder${n + 1}[$tparams, Option[(A, B)]]
          |    def optionally3[A, B, C](inner: ParamBuilder3Ops[A, B, C]): ParamBuilder${n + 1}[$tparams, Option[(A, B, C)]]"""
           else ""
         }
          |    def buildQuery[Out](parser: dev.typr.foundations.ResultSetParser[Out]): TemplateRead.Query$n[$tparams, Out]
          |    def buildUpdate(): Template.Update$n[$tparams]
          |    def buildDone(): dev.typr.foundations.Fragment
          |
          |  private[foundationssc] def createOps$n[$allImplTparams](
          |    underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$jtparams],
          |    $bijectionParams
          |  ): ParamBuilder${n}Ops[$tparams] = new ParamBuilder${n}Ops[$tparams]:
          |    def appendStr(s: String) = new ParamBuilder$n(createOps$n(underlying.append(s), $bijections))
          |    def addValue[T](tpe: dev.typr.foundations.DbType[T], value: T) = new ParamBuilder$n(createOps$n(underlying.value(tpe, value), $bijections))
          |    def appendFrag(other: dev.typr.foundations.Fragment) = new ParamBuilder$n(createOps$n(underlying.append(other), $bijections))$opsNextParam
          |    def buildQuery[Out](parser: dev.typr.foundations.ResultSetParser[Out]): TemplateRead.Query$n[$tparams, Out] =
          |      new TemplateRead.Query$n(underlying.query(parser).contramapInput[$inType](input => $contramapBody))
          |    def buildUpdate(): Template.Update$n[$tparams] =
          |      new Template.Update$n(underlying.update().contramapInput[$inType](input => $contramapBody))
          |    def buildDone() = underlying.done()
          |
          |  class ParamBuilder$n[$tparams] private[foundationssc] (
          |    private[foundationssc] val ops: ParamBuilder${n}Ops[$tparams]
          |  ):
          |    private[foundationssc] def this(j: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$tparams]) = this(createOps$n(j, $identityBijections))
          |
          |    def append(s: String): ParamBuilder$n[$tparams] = ops.appendStr(s)
          |    def value[T](tpe: DbType[T], value: T): ParamBuilder$n[$tparams] = ops.addValue(tpe.underlying, value)
          |    def append(fragment: Fragment): ParamBuilder$n[$tparams] = ops.appendFrag(fragment.underlying)$userNextParam
          |    def query[Out](parser: ResultSetParser[Out]): TemplateRead.Query$n[$tparams, Out] = ops.buildQuery(parser.underlying)
          |    def update(): Template.Update$n[$tparams] = ops.buildUpdate()
          |    def done(): Fragment = new Fragment(ops.buildDone())""".stripMargin
    }

    s"""|package dev.typr.foundationssc
        |
        |object ParamBuilders:
        |${builders.mkString("\n\n")}
        |""".stripMargin
  }

  // ─────────────────────────────────────────────────────────────────────────────
}
