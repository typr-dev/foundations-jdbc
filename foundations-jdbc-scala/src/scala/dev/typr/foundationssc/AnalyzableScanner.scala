package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*

object AnalyzableScanner:
  private def wrap(java: dev.typr.foundations.Analyzable): Analyzable =
    new Analyzable {
      def analyzable: dev.typr.foundations.Analyzable = java
      override def toString: String = java.toString
    }

  def scan(packageName: String): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName).asScala.toList.map(wrap)

  def scan(packageName: String, transactor: Transactor): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying).asScala.toList.map(wrap)

  def scan(packageName: String, directives: ScanDirective*): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, directives*).asScala.toList.map(wrap)

  def scan(packageName: String, transactor: Transactor, directives: ScanDirective*): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying, directives*).asScala.toList.map(wrap)

  def scanDetailed(packageName: String): List[AnalyzableScannerResult] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName).asScala.toList

  def scanDetailed(packageName: String, transactor: Transactor): List[AnalyzableScannerResult] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying).asScala.toList

  def scanDetailed(packageName: String, directives: ScanDirective*): List[AnalyzableScannerResult] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, directives*).asScala.toList

  def scanDetailed(
      packageName: String,
      transactor: Transactor,
      directives: ScanDirective*
  ): List[AnalyzableScannerResult] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying, directives*).asScala.toList

  def describe(analyzable: Analyzable): String =
    dev.typr.foundations.AnalyzableScanner.describe(analyzable.analyzable)

  def skip(clazz: Class[?], methodName: String): ScanDirective =
    dev.typr.foundations.ScanDirective.SkipMethod(clazz.getName, methodName)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: Analyzable): ScanDirective =
    dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result.analyzable)

  def instance(obj: Any): ScanDirective =
    dev.typr.foundations.ScanDirective.InstanceScan(obj, java.util.List.of())

  def instance[T](obj: T)(config: InstanceScope[T] => Unit): ScanDirective =
    val scope = InstanceScope(obj)
    config(scope)
    dev.typr.foundations.ScanDirective.InstanceScan(obj, scope.directives.toList.asJava)

class InstanceScope[T](val obj: T):
  private[foundationssc] val directives = scala.collection.mutable.ListBuffer[ScanDirective]()

  def skip(clazz: Class[?], methodName: String): Unit =
    directives += dev.typr.foundations.ScanDirective.SkipMethod(clazz.getName, methodName)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: Analyzable): Unit =
    directives += dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result.analyzable)
