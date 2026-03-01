package dev.typr.foundationssc

import _root_.scala.jdk.CollectionConverters.*

class Cursor[Row](val underlying: dev.typr.foundations.Cursor[Row]) extends Iterator[Row] with AutoCloseable {

  override def hasNext: Boolean = underlying.hasNext()

  override def next(): Row = underlying.next()

  override def toList: List[Row] = underlying.toList().asScala.toList

  override def close(): Unit = underlying.close()
}
