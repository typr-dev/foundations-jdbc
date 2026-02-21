package dev.typr.foundationssc.docs.analysis
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisTestSuite:
  private val transactor: Transactor = null // placeholder

  //start
  def allQueriesTypeCheck(): Unit =
    val analyzables = AnalyzableScanner.scan("com.myapp.db")
    val checker = QueryChecker.create(transactor)
    checker.checkAll(analyzables)
  //stop
