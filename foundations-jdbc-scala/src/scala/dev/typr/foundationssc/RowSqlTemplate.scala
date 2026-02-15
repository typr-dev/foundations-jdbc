package dev.typr.foundationssc

sealed trait RowSqlTemplate[Row, Out]:
  def underlying: dev.typr.foundations.RowSqlTemplate[?, ?]

  def on(row: Row): Operation[Out]

  def fragment: Fragment = new Fragment(underlying.fragment())

object RowSqlTemplate:

  class Query[Row, Out](val underlying: dev.typr.foundations.RowSqlTemplate.Query[Row, Out])
      extends RowSqlTemplate[Row, Out]:
    override def on(row: Row): Operation.Query[Out] = new Operation.Query(underlying.on(row))

  class Update[Row](val underlying: dev.typr.foundations.RowSqlTemplate.Update[Row])
      extends RowSqlTemplate[Row, Int]:
    override def on(row: Row): Operation.Update = new Operation.Update(underlying.on(row))
