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
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("PgStruct.scala"), generateScalaPgStructBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("DuckDbStruct.scala"), generateScalaDuckDbStructBuilders())
      FileUtils.writeString(started.logger, Some("SourcegenScala"), outputDir.resolve("OracleObject.scala"), generateScalaOracleObjectBuilders())
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
         |    def call(${callParams(i)}): ProcedureOp[$retType]
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
  
      val castExpr = o match {
        case 0 => "_ => ()"
        case 1 => "_.asInstanceOf[O0]"
        case n =>
          val javaTupleType = s"dev.typr.foundations.Tuple.Tuple$n[${oParams(n).mkString(", ")}]"
          val accessors = 0.until(n).map(i => s"t._${i+1}()").mkString(", ")
          s"{ r => val t = r.asInstanceOf[$javaTupleType]; ($accessors) }"
      }
  
      val javaCallArgs = if (i == 0) "" else callArgNamesStr
  
      s"""  class Builder_${i}_${o}$tpDecl private[foundationssc] (
         |    private val underlying: dev.typr.foundations.DbProcedure.Builder_${i}_${o}$javaTpDecl
         |  ) {
         |$methodsBlock
         |    def build(): Def${i}_${o}$defTpDecl = {
         |      val javaDef = underlying.build()
         |      new Def${i}_${o}$defTpDecl {
         |        def call($callParamsStr): ProcedureOp[$retType] =
         |          new ProcedureOp(javaDef.call($javaCallArgs).asInstanceOf[dev.typr.foundations.Operation[Any]], $castExpr)
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
         |    def call(${callParams(i)}): ProcedureOp[R]
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
         |        def call($callParamsStr): ProcedureOp[R] =
         |          new ProcedureOp(javaDef.call($javaCallArgs).asInstanceOf[dev.typr.foundations.Operation[Any]], _.asInstanceOf[R])
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
  
  def generateScalaPgStructBuilders(): String = {
    val maxArity = STRUCT_N - 1
  
    val builder0 = s"""|  class Builder0[A] private[foundationssc] (
                       |    private val underlying: dev.typr.foundations.PgStructBuilders.Builder0[A]
                       |  ):
                       |    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder1[A, F] =
                       |      Builder1(underlying.field(name, tpe.underlying, a => getter(a)))
                       |    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder1[A, F] =
                       |      Builder1(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))""".stripMargin
  
    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"T$i").mkString(", ")
      val lambdaParams = range.map(i => s"t$i").mkString(", ")
  
      val nextBuilder = if (n < maxArity) {
        val nextTparams = range.map(i => s"T$i").mkString(", ")
        s"""|
            |    def field[F](name: String, tpe: PgType[F], getter: A => F): Builder${n + 1}[A, $nextTparams, F] =
            |      Builder${n + 1}(underlying.field(name, tpe.underlying, a => getter(a)))
            |    def nestedField[F](name: String, nestedStruct: PgStruct[F], getter: A => F): Builder${n + 1}[A, $nextTparams, F] =
            |      Builder${n + 1}(underlying.nestedField(name, nestedStruct.underlying, a => getter(a)))""".stripMargin
      } else ""
  
      s"""|  class Builder$n[A, $tparams] private[foundationssc] (
          |    private val underlying: dev.typr.foundations.PgStructBuilders.Builder$n[A, $tparams]
          |  ):
          |    def build(decode: ($tparams) => A): PgStruct[A] =
          |      PgStruct(underlying.build(($lambdaParams) => decode($lambdaParams)))
          |$nextBuilder""".stripMargin
    }
  
    s"""|package dev.typr.foundationssc
        |
        |class PgStruct[A](val underlying: dev.typr.foundations.PgStruct[A]):
        |  def asType(): PgType[A] = PgType(underlying.asType())
        |
        |object PgStruct:
        |  def builder[A](typeName: String): Builder0[A] =
        |    Builder0(dev.typr.foundations.PgStructBuilders.builder(typeName))
        |
        |$builder0
        |
        |${builders.mkString("\n\n")}
        |""".stripMargin
  }
  
  def generateScalaDuckDbStructBuilders(): String = {
    val maxArity = STRUCT_N - 1
  
    val builder0 = s"""|  class Builder0[A] private[foundationssc] (
                       |    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder0[A]
                       |  ):
                       |    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder1[A, F] =
                       |      Builder1(underlying.field(name, tpe.underlying, a => getter(a)))""".stripMargin
  
    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"T$i").mkString(", ")
      val lambdaParams = range.map(i => s"t$i").mkString(", ")
  
      val nextBuilder = if (n < maxArity) {
        val nextTparams = range.map(i => s"T$i").mkString(", ")
        s"""|
            |    def field[F](name: String, tpe: DuckDbType[F], getter: A => F): Builder${n + 1}[A, $nextTparams, F] =
            |      Builder${n + 1}(underlying.field(name, tpe.underlying, a => getter(a)))""".stripMargin
      } else ""
  
      s"""|  class Builder$n[A, $tparams] private[foundationssc] (
          |    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder$n[A, $tparams]
          |  ):
          |    def build(decode: ($tparams) => A): DuckDbStruct[A] =
          |      DuckDbStruct(underlying.build(($lambdaParams) => decode($lambdaParams)))
          |$nextBuilder""".stripMargin
    }
  
    s"""|package dev.typr.foundationssc
        |
        |class DuckDbStruct[A](val underlying: dev.typr.foundations.DuckDbStruct[A]):
        |  def asType(): DuckDbType[A] = DuckDbType(underlying.asType())
        |
        |object DuckDbStruct:
        |  def builder[A](structName: String): Builder0[A] =
        |    Builder0(dev.typr.foundations.DuckDbStructBuilders.builder(structName))
        |
        |$builder0
        |
        |${builders.mkString("\n\n")}
        |""".stripMargin
  }
  
  def generateScalaOracleObjectBuilders(): String = {
    val maxArity = STRUCT_N - 1
  
    val builder0 = s"""|  class Builder0[A] private[foundationssc] (
                       |    private val underlying: dev.typr.foundations.OracleObjectBuilders.Builder0[A]
                       |  ):
                       |    def field[F](name: String, tpe: OracleType[F], getter: A => F): Builder1[A, F] =
                       |      Builder1(underlying.field(name, tpe.underlying, a => getter(a)))""".stripMargin
  
    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"T$i").mkString(", ")
      val lambdaParams = range.map(i => s"t$i").mkString(", ")
  
      val nextBuilder = if (n < maxArity) {
        val nextTparams = range.map(i => s"T$i").mkString(", ")
        s"""|
            |    def field[F](name: String, tpe: OracleType[F], getter: A => F): Builder${n + 1}[A, $nextTparams, F] =
            |      Builder${n + 1}(underlying.field(name, tpe.underlying, a => getter(a)))""".stripMargin
      } else ""
  
      s"""|  class Builder$n[A, $tparams] private[foundationssc] (
          |    private val underlying: dev.typr.foundations.OracleObjectBuilders.Builder$n[A, $tparams]
          |  ):
          |    def build(decode: ($tparams) => A): OracleObject[A] =
          |      OracleObject(underlying.build(($lambdaParams) => decode($lambdaParams)))
          |$nextBuilder""".stripMargin
    }
  
    s"""|package dev.typr.foundationssc
        |
        |class OracleObject[A](val underlying: dev.typr.foundations.OracleObject[A]):
        |  def asType(): OracleType[A] = OracleType(underlying.asType())
        |
        |object OracleObject:
        |  def builder[A](objectTypeName: String): Builder0[A] =
        |    Builder0(dev.typr.foundations.OracleObjectBuilders.builder(objectTypeName))
        |
        |$builder0
        |
        |${builders.mkString("\n\n")}
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
  
    val queryClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val allTparams = s"$tparams, Out"
      val wildcards = range.map(_ => "?").mkString(", ")
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val transformLines = range.map { i =>
        s"      val v$i: AnyRef = _transforms($i).map(_(p$i.asInstanceOf[AnyRef])).getOrElse(p$i.asInstanceOf[AnyRef])"
      }.mkString("\n")
      val valuesList = range.map(i => s"v$i").mkString(", ")
  
      val fromFnParams = range.map(i => s"f$i: T => P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")
  
      if (n == 1) {
        s"""|  class Query1[P0, Out](
            |    private val _java: dev.typr.foundations.Template.Query1[?, Out],
            |    private val _transforms: List[Option[AnyRef => AnyRef]]
            |  ) extends Template[P0, Out]:
            |    def this(j: dev.typr.foundations.Template.Query1[?, Out]) = this(j, List(None))
            |    override def underlying: dev.typr.foundations.Template[?, ?] = _java
            |    override def on(input: P0): Operation.Query[Out] =
            |      val v0: AnyRef = _transforms(0).map(_(input.asInstanceOf[AnyRef])).getOrElse(input.asInstanceOf[AnyRef])
            |      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
            |        _java.fragment(), java.util.List.of(v0).iterator())
            |      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
            |    def from[T]($fromFnParams): From[T, Out] =
            |      new From(_java, (t: T) => on($fromApplyArgs))""".stripMargin
      } else {
        val tupleType = s"(${range.map(i => s"P$i").mkString(", ")})"
        val tupleDecompose = range.map(i => s"input._${i + 1}").mkString(", ")
  
        s"""|  class Query$n[$allTparams](
            |    private val _java: dev.typr.foundations.Template.Query$n[$wildcards, Out],
            |    private val _transforms: List[Option[AnyRef => AnyRef]]
            |  ) extends Template[$tupleType, Out]:
            |    def this(j: dev.typr.foundations.Template.Query$n[$wildcards, Out]) = this(j, List.fill($n)(None))
            |    override def underlying: dev.typr.foundations.Template[?, ?] = _java
            |    override def on(input: $tupleType): Operation.Query[Out] =
            |      on($tupleDecompose)
            |    def on($onParams): Operation.Query[Out] =
            |$transformLines
            |      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
            |        _java.fragment(), java.util.List.of($valuesList).iterator())
            |      new Operation.Query(new dev.typr.foundations.Operation.Query(resolved, _java.parser()))
            |    def from[T]($fromFnParams): From[T, Out] =
            |      new From(_java, (t: T) => on($fromApplyArgs))""".stripMargin
      }
    }
  
    val updateClasses = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val wildcards = range.map(_ => "?").mkString(", ")
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val transformLines = range.map { i =>
        s"      val v$i: AnyRef = _transforms($i).map(_(p$i.asInstanceOf[AnyRef])).getOrElse(p$i.asInstanceOf[AnyRef])"
      }.mkString("\n")
      val valuesList = range.map(i => s"v$i").mkString(", ")
  
      val fromFnParams = range.map(i => s"f$i: T => P$i").mkString(", ")
      val fromApplyArgs = range.map(i => s"f$i(t)").mkString(", ")
  
      if (n == 1) {
        s"""|  class Update1[P0](
            |    private val _java: dev.typr.foundations.Template.Update1[?],
            |    private val _transforms: List[Option[AnyRef => AnyRef]]
            |  ) extends Template[P0, Int]:
            |    def this(j: dev.typr.foundations.Template.Update1[?]) = this(j, List(None))
            |    override def underlying: dev.typr.foundations.Template[?, ?] = _java
            |    override def on(input: P0): Operation.Update =
            |      val v0: AnyRef = _transforms(0).map(_(input.asInstanceOf[AnyRef])).getOrElse(input.asInstanceOf[AnyRef])
            |      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
            |        _java.fragment(), java.util.List.of(v0).iterator())
            |      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
            |    def from[T]($fromFnParams): From[T, Int] =
            |      new From(_java, (t: T) => on($fromApplyArgs))""".stripMargin
      } else {
        val tupleType = s"(${range.map(i => s"P$i").mkString(", ")})"
        val tupleDecompose = range.map(i => s"input._${i + 1}").mkString(", ")
  
        s"""|  class Update$n[$tparams](
            |    private val _java: dev.typr.foundations.Template.Update$n[$wildcards],
            |    private val _transforms: List[Option[AnyRef => AnyRef]]
            |  ) extends Template[$tupleType, Int]:
            |    def this(j: dev.typr.foundations.Template.Update$n[$wildcards]) = this(j, List.fill($n)(None))
            |    override def underlying: dev.typr.foundations.Template[?, ?] = _java
            |    override def on(input: $tupleType): Operation.Update =
            |      on($tupleDecompose)
            |    def on($onParams): Operation.Update =
            |$transformLines
            |      val resolved = dev.typr.foundations.OptionallyResolver.resolve(
            |        _java.fragment(), java.util.List.of($valuesList).iterator())
            |      new Operation.Update(new dev.typr.foundations.Operation.Update(resolved))
            |    def from[T]($fromFnParams): From[T, Int] =
            |      new From(_java, (t: T) => on($fromApplyArgs))""".stripMargin
      }
    }
  
    s"""|package dev.typr.foundationssc
        |
        |sealed trait Template[In, Out] extends Analyzable:
        |  def underlying: dev.typr.foundations.Template[?, ?]
        |
        |  override def analyzable: dev.typr.foundations.Analyzable = underlying
        |
        |  def on(input: In): Operation[Out]
        |
        |  def fragment: Fragment = new Fragment(underlying.fragment())
        |
        |object Template:
        |
        |${queryClasses.mkString("\n\n")}
        |
        |${updateClasses.mkString("\n\n")}
        |
        |  class From[T, Out](
        |    private val _innerUnderlying: dev.typr.foundations.Template[?, ?],
        |    private val _resolver: T => Operation[Out]
        |  ) extends Template[T, Out]:
        |    override def underlying: dev.typr.foundations.Template[?, ?] = _innerUnderlying
        |    override def on(input: T): Operation[Out] = _resolver(input)
        |
        |sealed trait RowTemplate[Row, Out] extends Template[Row, Out]:
        |  override def underlying: dev.typr.foundations.RowTemplate[?, ?]
        |
        |object RowTemplate:
        |
        |  class Query[Row, Out](val underlying: dev.typr.foundations.RowTemplate.Query[Row, Out])
        |      extends RowTemplate[Row, Out]:
        |    override def on(input: Row): Operation.Query[Out] = new Operation.Query(underlying.on(input))
        |
        |  class Update[Row](val underlying: dev.typr.foundations.RowTemplate.Update[Row])
        |      extends RowTemplate[Row, Int]:
        |    override def on(input: Row): Operation.Update = new Operation.Update(underlying.on(input))
        |
        |    def onMany(rows: Iterator[Row]): Operation.UpdateManyTemplate[Row] = {
        |      import _root_.scala.jdk.CollectionConverters.*
        |      new Operation.UpdateManyTemplate(underlying.onMany(rows.asJava))
        |    }
        |""".stripMargin
  }
  
  def generateScalaParamBuilders(): String = {
    val maxArity = PROC_N - 1 // 10
  
    val builders = 1.to(maxArity).map { n =>
      val range = 0.until(n)
      val tparams = range.map(i => s"P$i").mkString(", ")
      val wildcards = range.map(_ => "?").mkString(", ")
  
      val nextParamMethod = if (n < maxArity) {
        s"""|
            |    def param[P$n](tpe: DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n] =
            |      new ParamBuilder${n + 1}(underlying.param(tpe.underlying), transforms :+ None)""".stripMargin
      } else ""
  
      val optionallyMethods = if (n < maxArity) {
        s"""|
            |    def optionally(inner: Fragment): ParamBuilder${n + 1}[$tparams, Boolean] =
            |      new ParamBuilder${n + 1}(underlying.optionally(inner.underlying), transforms :+ None)
            |
            |    def optionally[A](builder: ParamBuilder1[A]): ParamBuilder${n + 1}[$tparams, Option[A]] =
            |      new ParamBuilder${n + 1}(
            |        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder1[A]]),
            |        transforms :+ Some(OptionallyTransforms.optionToOptional))
            |
            |    def optionally[A, B](builder: ParamBuilder2[A, B]): ParamBuilder${n + 1}[$tparams, Option[(A, B)]] =
            |      new ParamBuilder${n + 1}(
            |        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder2[A, B]]),
            |        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple2))
            |
            |    def optionally[A, B, C](builder: ParamBuilder3[A, B, C]): ParamBuilder${n + 1}[$tparams, Option[(A, B, C)]] =
            |      new ParamBuilder${n + 1}(
            |        underlying.optionally(builder.underlying.asInstanceOf[dev.typr.foundations.ParamBuilders.ParamBuilder3[A, B, C]]),
            |        transforms :+ Some(OptionallyTransforms.optionTupleToOptionalTuple3))""".stripMargin
      } else ""
  
      s"""|  class ParamBuilder$n[$tparams] private[foundationssc] (
          |    private[foundationssc] val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$wildcards],
          |    private[foundationssc] val transforms: List[Option[AnyRef => AnyRef]]
          |  ):
          |    private[foundationssc] def this(u: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$wildcards]) = this(u, List.fill($n)(None))
          |
          |    def append(s: String): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.append(s), transforms)
          |
          |    def value[T](tpe: DbType[T], value: T): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.value(tpe.underlying, value), transforms)
          |
          |    def append(fragment: Fragment): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.append(fragment.underlying), transforms)
          |$nextParamMethod$optionallyMethods
          |    def query[Out](parser: ResultSetParser[Out]): Template.Query$n[$tparams, Out] =
          |      new Template.Query$n(underlying.query(parser.underlying), transforms)
          |
          |    def update(): Template.Update$n[$tparams] =
          |      new Template.Update$n(underlying.update(), transforms)
          |
          |    def done(): Fragment = new Fragment(underlying.done())""".stripMargin
    }
  
    s"""|package dev.typr.foundationssc
        |
        |object ParamBuilders:
        |${builders.mkString("\n\n")}
        |""".stripMargin
  }
  
  // ─────────────────────────────────────────────────────────────────────────────
}
