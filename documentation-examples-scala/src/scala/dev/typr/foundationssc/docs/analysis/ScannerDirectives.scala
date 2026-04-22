package dev.typr.foundationssc.docs.analysis

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ScannerDirectives:
  private val transactor: Transactor = null // placeholder

  class ReportRepo:
    def generateReport(onProgress: Runnable): OperationRead[List[String]] =
      Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text)

    def allReports(): OperationRead[List[String]] =
      Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text)

  // start
  def checkWithDirectives(): Unit =
    val analyzables = AnalyzableScanner.scan(
      "com.myapp.reports",

      // Skip a method entirely — it won't be type-checked
      AnalyzableScanner.skip(classOf[ReportRepo], "generateReport")
    )

    val checker = QueryChecker.create(transactor)
    checker.checkAll(analyzables)
  // stop
