package dev.typr.foundationssc

class RowParamBuilder[Row] private[foundationssc] (
    private[foundationssc] val underlying: dev.typr.foundations.RowParamBuilder[Row]
):
  def append(s: String): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(s))

  def append(fragment: Fragment): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(fragment.underlying))

  // Row-driven execution

  def updateOne(row: Row): Operation.Update = new Operation.Update(underlying.updateOne(row))

  def updateReturning[Out](row: Row, parser: ResultSetParser[Out]): OperationRead.Query[Out] =
    new OperationRead.Query(underlying.updateReturning(row, parser.underlying))

  def updateReturning(row: Row): OperationRead.Query[Row] =
    new OperationRead.Query(underlying.updateReturning(row))

  def updateMany(rows: Iterator[Row]): Operation.BatchUpdate[Row] =
    import _root_.scala.jdk.CollectionConverters.*
    new Operation.BatchUpdate(underlying.updateMany(rows.asJava))

  def updateOneGenerated[Out](row: Row, generatedColumns: Array[String], parser: ResultSetParser[Out]): Operation.UpdateReturningGeneratedKeys[Out] =
    new Operation.UpdateReturningGeneratedKeys(underlying.updateOneGenerated(row, generatedColumns, parser.underlying))

  def queryOne[Out](row: Row, parser: ResultSetParser[Out]): OperationRead.Query[Out] =
    new OperationRead.Query(underlying.queryOne(row, parser.underlying))

  def done(): Fragment = new Fragment(underlying.done())
