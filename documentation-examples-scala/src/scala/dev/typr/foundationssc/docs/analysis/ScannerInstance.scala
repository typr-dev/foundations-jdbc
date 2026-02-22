package dev.typr.foundationssc.docs.analysis

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ScannerInstance:
  private val transactor: Transactor = null // placeholder

  class ExternalRepo:
    val allItems: Operation[List[String]] =
      Fragment.of("SELECT name FROM items").queryAll(PgTypes.text)

    def activeItems(): Operation[List[String]] =
      Fragment.of("SELECT name FROM items WHERE active").queryAll(PgTypes.text)

    def generateReport(callback: Runnable): Operation[List[String]] =
      Fragment.of("SELECT name FROM items").queryAll(PgTypes.text)

  //start
  def checkWithExternalObjects(): Unit =
    val external = ExternalRepo()

    val analyzables = AnalyzableScanner.scan(
      "com.myapp.db",

      // Add an object from outside the scanned package
      AnalyzableScanner.instance(external),

      // Add with per-instance overrides
      AnalyzableScanner.instance(external) { scope =>
        scope.skip(classOf[ExternalRepo], "generateReport")
      }
    )

    val checker = QueryChecker.create(transactor)
    checker.checkAll(analyzables)
  //stop
