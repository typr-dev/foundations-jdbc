package dev.typr.foundationssc

sealed trait RowTemplate[Row, Out]:
  def underlying: dev.typr.foundations.RowTemplate[?, ?]

  def on(row: Row): Operation[Out]

  def fragment: Fragment = new Fragment(underlying.fragment())

object RowTemplate:

  class Query[Row, Out](val underlying: dev.typr.foundations.RowTemplate.Query[Row, Out])
      extends RowTemplate[Row, Out]:
    override def on(row: Row): Operation.Query[Out] = new Operation.Query(underlying.on(row))

  class Update[Row](val underlying: dev.typr.foundations.RowTemplate.Update[Row])
      extends RowTemplate[Row, Int]:
    override def on(row: Row): Operation.Update = new Operation.Update(underlying.on(row))

    def onMany(rows: Iterator[Row]): Operation.UpdateManyTemplate[Row] = {
      import _root_.scala.jdk.CollectionConverters.*
      new Operation.UpdateManyTemplate(underlying.onMany(rows.asJava))
    }
