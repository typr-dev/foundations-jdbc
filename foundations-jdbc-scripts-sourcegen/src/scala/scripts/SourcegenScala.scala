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
          |      new ParamBuilder${n + 1}(createOps${n + 1}(underlying.param(tpe), $bijections, dev.typr.foundations.Bijection.identity()))"""
      } else ""

      val userNextParam = if (n < maxArity) {
        s"""
          |    def param[P$n](tpe: DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n] = ops.param(tpe.underlying)"""
      } else ""

      s"""|  trait ParamBuilder${n}Ops[$tparams]:
          |    def appendStr(s: String): ParamBuilder$n[$tparams]
          |    def addValue[T](tpe: dev.typr.foundations.DbType[T], value: T): ParamBuilder$n[$tparams]
          |    def appendFrag(other: dev.typr.foundations.Fragment): ParamBuilder$n[$tparams]
          |${
           if (n < maxArity) s"""    def param[P$n](tpe: dev.typr.foundations.DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n]"""
           else ""
         }
          |    def buildDone(): dev.typr.foundations.Fragment
          |
          |  private[foundationssc] def createOps$n[$allImplTparams](
          |    underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$jtparams],
          |    $bijectionParams
          |  ): ParamBuilder${n}Ops[$tparams] = new ParamBuilder${n}Ops[$tparams]:
          |    def appendStr(s: String) = new ParamBuilder$n(createOps$n(underlying.append(s), $bijections))
          |    def addValue[T](tpe: dev.typr.foundations.DbType[T], value: T) = new ParamBuilder$n(createOps$n(underlying.value(tpe, value), $bijections))
          |    def appendFrag(other: dev.typr.foundations.Fragment) = new ParamBuilder$n(createOps$n(underlying.append(other), $bijections))$opsNextParam
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
