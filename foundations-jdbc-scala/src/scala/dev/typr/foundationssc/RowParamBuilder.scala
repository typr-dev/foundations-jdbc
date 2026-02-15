package dev.typr.scalafoundations

class RowParamBuilder[Row] private[scalafoundations] (
  private val underlying: dev.typr.foundations.RowParamBuilder[Row]
):
  def append(s: String): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(s))

  def append(fragment: Fragment): RowParamBuilder[Row] = new RowParamBuilder(underlying.append(fragment.underlying))

  def query[Out](parser: ResultSetParser[Out]): RowSqlTemplate.Query[Row, Out] =
    new RowSqlTemplate.Query(underlying.query(parser.underlying))

  def update(): RowSqlTemplate.Update[Row] = new RowSqlTemplate.Update(underlying.update())

  def done(): Fragment = new Fragment(underlying.done())
