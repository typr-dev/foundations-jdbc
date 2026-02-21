package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*

object AnalyzableScanner:
  private def wrap(java: dev.typr.foundations.Analyzable): Analyzable =
    new Analyzable { def analyzable: dev.typr.foundations.Analyzable = java }

  def scan(packageName: String): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName).asScala.toList.map(wrap)

  def scan(packageName: String, transactor: Transactor): List[Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying).asScala.toList.map(wrap)

  def scanDetailed(packageName: String): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName).asScala.toList

  def scanDetailed(packageName: String, transactor: Transactor): List[dev.typr.foundations.AnalyzableScanner.Result] =
    dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying).asScala.toList

  def describe(analyzable: Analyzable): String =
    dev.typr.foundations.AnalyzableScanner.describe(analyzable.analyzable)
