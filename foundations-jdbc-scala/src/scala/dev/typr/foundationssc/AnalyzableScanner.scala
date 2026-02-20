package dev.typr.foundationssc

import scala.jdk.CollectionConverters.*

object AnalyzableScanner:
  def scan(packageName: String): List[dev.typr.foundations.Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName).asScala.toList

  def scan(packageName: String, transactor: Transactor): List[dev.typr.foundations.Analyzable] =
    dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying).asScala.toList
