package dev.typr.foundationssc

class RowParamBuilder[Row] private[foundationssc] (
  private val underlying: dev.typr.foundations.RowParamBuilder[Row]
):
  def append(s: String): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(s))

  def append(fragment: Fragment): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(fragment.underlying))

  def query[Out](parser: ResultSetParser[Out]): RowTemplate.Query[Row, Out] =
    new RowTemplate.Query(underlying.query(parser.underlying))

  def update(): RowTemplate.Update[Row] = new RowTemplate.Update(underlying.update())

  def done(): Fragment = new Fragment(underlying.done())
