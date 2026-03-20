package dev.typr.foundationssc.docs.analysis

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ScannerDirectivesManual:
  private val transactor: Transactor = null // placeholder

  case class ReportFilter(category: String, limit: Int)

  class ReportRepo:
    def filteredReport(filter: ReportFilter): Operation[List[String]] =
      Fragment
        .of("SELECT name FROM reports WHERE category = ")
        .value(PgTypes.text, filter.category)
        .queryAll(PgTypes.text)

    def allReports(): Operation[List[String]] =
      Fragment.of("SELECT name FROM reports").queryAll(PgTypes.text)

  // start
  def checkWithManualDirective(): Unit =
    val repo = ReportRepo()

    val analyzables = AnalyzableScanner.scan(
      "com.myapp.reports",

      // Provide specific arguments for a method
      AnalyzableScanner.manual(classOf[ReportRepo], "filteredReport", "defaults", repo.filteredReport(ReportFilter("all", 100)))
    )

    val checker = QueryChecker.create(transactor)
    checker.checkAll(analyzables)
  // stop
