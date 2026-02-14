#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.4

import java.nio.file.{Files, Path}

val N = 100
val PROC_N = 11
val STRUCT_N = 31
val baseDir = Path.of(sys.props.getOrElse("user.dir", "."))
val generatedOutputDir = baseDir.resolve("foundations-jdbc-scala/generated-and-checked-in/dev/typr/scalafoundations")

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
        |      val javaParser = dev.typr.foundations.RowParser.create[Row](
        |        java.util.List.copyOf(types.map(_.underlying).asJava),
        |        arr => decode($decodeArgs),
        |        row => capturedGetters.map(_(row)).toArray
        |      )
        |      new RowParser(javaParser)
        |    }$nextBuilder
        |  }""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
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

def generateScalaNamedRowParserBuilders(): String = {
  val maxArity = N - 1

  val builder0 = s"""|  class Builder0[Row] private[scalafoundations] () {
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

    s"""|  class Builder$n[Row, $tparams] private[scalafoundations] (
        |    private val names: scala.collection.mutable.ListBuffer[String],
        |    private val types: scala.collection.mutable.ListBuffer[DbType[?]],
        |    private val getters: scala.collection.mutable.ListBuffer[Row => Any]
        |  ) {
        |    def build(decode: ($decodeParams) => Row): RowParserNamed[Row] = {
        |      val capturedGetters = getters.toList
        |      val javaParser = dev.typr.foundations.RowParser.createNamed[Row](
        |        java.util.List.copyOf(names.asJava),
        |        java.util.List.copyOf(types.map(_.underlying).asJava),
        |        arr => decode($decodeArgs),
        |        row => capturedGetters.map(_(row)).toArray
        |      )
        |      new RowParserNamed(javaParser)
        |    }$nextBuilder
        |  }""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
      |import scala.jdk.CollectionConverters.*
      |
      |/** Type-safe named builders for Scala RowParser.
      |  *
      |  * Usage:
      |  * {{{
      |  * val parser: RowParserNamed[Product] = RowParser.namedBuilder[Product]()
      |  *   .field("id", PgTypes.int4)(_.id)
      |  *   .field("name", PgTypes.text)(_.name)
      |  *   .field("price", PgTypes.numeric)(_.price)
      |  *   .build(Product.apply)
      |  * }}}
      |  */
      |object RowParserNamedBuilders {
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
         |      new Builder_${i + 1}_${o}(underlying.in(tpe.underlying))""".stripMargin
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
         |      new Builder_${i + 1}(underlying.in(tpe.underlying))
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

  val builder0 = s"""|  class Builder0[A] private[scalafoundations] (
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

    s"""|  class Builder$n[A, $tparams] private[scalafoundations] (
        |    private val underlying: dev.typr.foundations.PgStructBuilders.Builder$n[A, $tparams]
        |  ):
        |    def build(decode: ($tparams) => A): PgStruct[A] =
        |      PgStruct(underlying.build(($lambdaParams) => decode($lambdaParams)))
        |$nextBuilder""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
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

  val builder0 = s"""|  class Builder0[A] private[scalafoundations] (
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

    s"""|  class Builder$n[A, $tparams] private[scalafoundations] (
        |    private val underlying: dev.typr.foundations.DuckDbStructBuilders.Builder$n[A, $tparams]
        |  ):
        |    def build(decode: ($tparams) => A): DuckDbStruct[A] =
        |      DuckDbStruct(underlying.build(($lambdaParams) => decode($lambdaParams)))
        |$nextBuilder""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
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

  val builder0 = s"""|  class Builder0[A] private[scalafoundations] (
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

    s"""|  class Builder$n[A, $tparams] private[scalafoundations] (
        |    private val underlying: dev.typr.foundations.OracleObjectBuilders.Builder$n[A, $tparams]
        |  ):
        |    def build(decode: ($tparams) => A): OracleObject[A] =
        |      OracleObject(underlying.build(($lambdaParams) => decode($lambdaParams)))
        |$nextBuilder""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
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

  s"""|package dev.typr.scalafoundations
      |
      |type Tuple = dev.typr.foundations.Tuple
      |
      |object Tuple:
      |${typeAliases.mkString("\n")}
      |
      |${factories.mkString("\n\n")}
      |""".stripMargin
}

def generateScalaSqlTemplate(): String = {
  val maxArity = PROC_N - 1 // 10

  val queryClasses = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")
    val allTparams = s"$tparams, Out"

    if (n == 1) {
      s"""|  class Query1[P0, Out](val underlying: dev.typr.foundations.SqlTemplate.Query1[P0, Out]) extends SqlTemplate[P0, Out]:
          |    override def on(input: P0): Operation.Query[Out] =
          |      new Operation.Query(underlying.on(input))""".stripMargin
    } else {
      val tupleType = s"(${range.map(i => s"P$i").mkString(", ")})"
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val onTupleArgs = range.map(i => s"input._${i + 1}").mkString(", ")
      val onArgs = range.map(i => s"p$i").mkString(", ")

      s"""|  class Query$n[$allTparams](val underlying: dev.typr.foundations.SqlTemplate.Query$n[$allTparams]) extends SqlTemplate[$tupleType, Out]:
          |    override def on(input: $tupleType): Operation.Query[Out] =
          |      new Operation.Query(underlying.on($onTupleArgs))
          |
          |    def on($onParams): Operation.Query[Out] =
          |      new Operation.Query(underlying.on($onArgs))""".stripMargin
    }
  }

  val updateClasses = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")

    if (n == 1) {
      s"""|  class Update1[P0](val underlying: dev.typr.foundations.SqlTemplate.Update1[P0]) extends SqlTemplate[P0, Int]:
          |    override def on(input: P0): Operation.Update =
          |      new Operation.Update(underlying.on(input))""".stripMargin
    } else {
      val tupleType = s"(${range.map(i => s"P$i").mkString(", ")})"
      val onParams = range.map(i => s"p$i: P$i").mkString(", ")
      val onTupleArgs = range.map(i => s"input._${i + 1}").mkString(", ")
      val onArgs = range.map(i => s"p$i").mkString(", ")

      s"""|  class Update$n[$tparams](val underlying: dev.typr.foundations.SqlTemplate.Update$n[$tparams]) extends SqlTemplate[$tupleType, Int]:
          |    override def on(input: $tupleType): Operation.Update =
          |      new Operation.Update(underlying.on($onTupleArgs))
          |
          |    def on($onParams): Operation.Update =
          |      new Operation.Update(underlying.on($onArgs))""".stripMargin
    }
  }

  s"""|package dev.typr.scalafoundations
      |
      |sealed trait SqlTemplate[In, Out]:
      |  def underlying: dev.typr.foundations.SqlTemplate[?, ?]
      |
      |  def on(input: In): Operation[Out]
      |
      |  def fragment: Fragment = new Fragment(underlying.fragment())
      |
      |object SqlTemplate:
      |
      |${queryClasses.mkString("\n\n")}
      |
      |${updateClasses.mkString("\n\n")}
      |""".stripMargin
}

def generateScalaParamBuilders(): String = {
  val maxArity = PROC_N - 1 // 10

  val builders = 1.to(maxArity).map { n =>
    val range = 0.until(n)
    val tparams = range.map(i => s"P$i").mkString(", ")

    val nextParamMethod = if (n < maxArity) {
      s"""|
          |    def param[P$n](tpe: DbType[P$n]): ParamBuilder${n + 1}[$tparams, P$n] =
          |      new ParamBuilder${n + 1}(underlying.param(tpe.underlying))""".stripMargin
    } else ""

    s"""|  class ParamBuilder$n[$tparams] private[scalafoundations] (
        |    private val underlying: dev.typr.foundations.ParamBuilders.ParamBuilder$n[$tparams]
        |  ):
        |    def append(s: String): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.append(s))
        |
        |    def value[T](tpe: DbType[T], value: T): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.value(tpe.underlying, value))
        |
        |    def append(fragment: Fragment): ParamBuilder$n[$tparams] = new ParamBuilder$n(underlying.append(fragment.underlying))
        |$nextParamMethod
        |    def query[Out](parser: ResultSetParser[Out]): SqlTemplate.Query$n[$tparams, Out] =
        |      new SqlTemplate.Query$n(underlying.query(parser.underlying))
        |
        |    def update(): SqlTemplate.Update$n[$tparams] =
        |      new SqlTemplate.Update$n(underlying.update())
        |
        |    def done(): Fragment = new Fragment(underlying.done())""".stripMargin
  }

  s"""|package dev.typr.scalafoundations
      |
      |object ParamBuilders:
      |${builders.mkString("\n\n")}
      |""".stripMargin
}

// ─────────────────────────────────────────────────────────────────────────────
// Write files
// ─────────────────────────────────────────────────────────────────────────────

Files.createDirectories(generatedOutputDir)

// RowParserBuilders.scala -> generated-and-checked-in
val scalaRowParserBuildersContent = generateScalaRowParserBuilders()
val scalaRowParserBuildersPath = generatedOutputDir.resolve("RowParserBuilders.scala")
Files.writeString(scalaRowParserBuildersPath, scalaRowParserBuildersContent)
println(s"Wrote ${scalaRowParserBuildersPath}")

// RowParserNamedBuilders.scala -> generated-and-checked-in
val scalaNamedRowParserBuildersContent = generateScalaNamedRowParserBuilders()
val scalaNamedRowParserBuildersPath = generatedOutputDir.resolve("RowParserNamedBuilders.scala")
Files.writeString(scalaNamedRowParserBuildersPath, scalaNamedRowParserBuildersContent)
println(s"Wrote ${scalaNamedRowParserBuildersPath}")

// DbProcedure.scala -> generated-and-checked-in
val scalaDbProcedureContent = generateScalaDbProcedure()
val scalaDbProcedurePath = generatedOutputDir.resolve("DbProcedure.scala")
Files.writeString(scalaDbProcedurePath, scalaDbProcedureContent)
println(s"Wrote ${scalaDbProcedurePath}")

// DbFunction.scala -> generated-and-checked-in
val scalaDbFunctionContent = generateScalaDbFunction()
val scalaDbFunctionPath = generatedOutputDir.resolve("DbFunction.scala")
Files.writeString(scalaDbFunctionPath, scalaDbFunctionContent)
println(s"Wrote ${scalaDbFunctionPath}")

// PgStruct.scala -> generated-and-checked-in
val scalaPgStructContent = generateScalaPgStructBuilders()
val scalaPgStructPath = generatedOutputDir.resolve("PgStruct.scala")
Files.writeString(scalaPgStructPath, scalaPgStructContent)
println(s"Wrote ${scalaPgStructPath}")

// DuckDbStruct.scala -> generated-and-checked-in
val scalaDuckDbStructContent = generateScalaDuckDbStructBuilders()
val scalaDuckDbStructPath = generatedOutputDir.resolve("DuckDbStruct.scala")
Files.writeString(scalaDuckDbStructPath, scalaDuckDbStructContent)
println(s"Wrote ${scalaDuckDbStructPath}")

// OracleObject.scala -> generated-and-checked-in
val scalaOracleObjectContent = generateScalaOracleObjectBuilders()
val scalaOracleObjectPath = generatedOutputDir.resolve("OracleObject.scala")
Files.writeString(scalaOracleObjectPath, scalaOracleObjectContent)
println(s"Wrote ${scalaOracleObjectPath}")

// Tuple.scala -> generated-and-checked-in
val scalaTupleContent = generateScalaTuple()
val scalaTuplePath = generatedOutputDir.resolve("Tuple.scala")
Files.writeString(scalaTuplePath, scalaTupleContent)
println(s"Wrote ${scalaTuplePath}")

// SqlTemplate.scala -> generated-and-checked-in
val scalaSqlTemplateContent = generateScalaSqlTemplate()
val scalaSqlTemplatePath = generatedOutputDir.resolve("SqlTemplate.scala")
Files.writeString(scalaSqlTemplatePath, scalaSqlTemplateContent)
println(s"Wrote ${scalaSqlTemplatePath}")

// ParamBuilders.scala -> generated-and-checked-in
val scalaParamBuildersContent = generateScalaParamBuilders()
val scalaParamBuildersPath = generatedOutputDir.resolve("ParamBuilders.scala")
Files.writeString(scalaParamBuildersPath, scalaParamBuildersContent)
println(s"Wrote ${scalaParamBuildersPath}")
