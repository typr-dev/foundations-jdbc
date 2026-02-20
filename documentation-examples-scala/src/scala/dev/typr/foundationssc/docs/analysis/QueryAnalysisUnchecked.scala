package dev.typr.foundationssc.docs.analysis
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisUnchecked:
  //start
  case class Stats(name: String, count: Int)

  // .unchecked() skips type checking entirely for this column
  val statsCodec: RowCodec[Stats] = RowCodec.builder[Stats]()
    .field(PgTypes.text)(_.name)
    .field(PgTypes.int4.unchecked())(_.count)
    .build(Stats.apply)
  //stop
