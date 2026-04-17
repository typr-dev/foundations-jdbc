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

  def scan(packageName: String, directives: dev.typr.foundations.ScanDirective*): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, directives*).asScala.toList.map(wrap)

  def scan(packageName: String, transactor: Transactor, directives: dev.typr.foundations.ScanDirective*): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying, directives*).asScala.toList.map(wrap)

  def scanDetailed(packageName: String): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName).asScala.toList

  def scanDetailed(packageName: String, transactor: Transactor): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying).asScala.toList

  def scanDetailed(packageName: String, directives: dev.typr.foundations.ScanDirective*): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, directives*).asScala.toList

  def scanDetailed(
      packageName: String,
      transactor: Transactor,
      directives: dev.typr.foundations.ScanDirective*
  ): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying, directives*).asScala.toList

  def describe(analyzable: Analyzable): String =
    dev.typr.foundations.AnalyzableScanner.describe(analyzable.analyzable)

  def skip(clazz: Class[?], methodName: String): dev.typr.foundations.ScanDirective =
    dev.typr.foundations.ScanDirective.SkipMethod(clazz.getName, methodName)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: Analyzable): dev.typr.foundations.ScanDirective =
    dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result.analyzable)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: dev.typr.foundations.Analyzable): dev.typr.foundations.ScanDirective =
    dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result)

  def instance(obj: Any): dev.typr.foundations.ScanDirective =
    dev.typr.foundations.ScanDirective.InstanceScan(obj, java.util.List.of())

  def instance[T](obj: T)(config: InstanceScope[T] => Unit): dev.typr.foundations.ScanDirective =
    val scope = InstanceScope(obj)
    config(scope)
    dev.typr.foundations.ScanDirective.InstanceScan(obj, scope.directives.toList.asJava)

class InstanceScope[T](val obj: T):
  private[foundationssc] val directives = scala.collection.mutable.ListBuffer[dev.typr.foundations.ScanDirective]()

  def skip(clazz: Class[?], methodName: String): Unit =
    directives += dev.typr.foundations.ScanDirective.SkipMethod(clazz.getName, methodName)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: Analyzable): Unit =
    directives += dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result.analyzable)

  def manual(clazz: Class[?], methodName: String, variantName: String, result: dev.typr.foundations.Analyzable): Unit =
    directives += dev.typr.foundations.ScanDirective.ManualMethod(clazz.getName, methodName, variantName, result)
