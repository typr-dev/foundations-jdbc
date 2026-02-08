package dev.typr.foundations.docs.analysis
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisUnchecked:
  //start
  case class Stats(name: String, count: Int)

  // .unchecked() skips type checking entirely for this column
  val statsParser: RowParser[Stats] = RowParser.builder[Stats]()
    .field(PgTypes.text)(_.name)
    .field(PgTypes.int4.unchecked())(_.count)
    .build(Stats.apply)
  //stop
