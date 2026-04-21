package dev.typr.foundationssc

class DuckDbType[T](override val underlying: dev.typr.foundations.DuckDbType[T]) extends DbType[T](underlying):
  override def opt: DuckDbType[Option[T]] =
    DuckDbType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: Bijection[T, B]): DuckDbType[B] =
    DuckDbType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): DuckDbType[B] =
    DuckDbType(underlying.transform(v => f(v), v => g(v)))

  def mapTo[V](valueType: DuckDbType[V]): DuckDbType[Map[T, V]] = DuckDbType(
    underlying
      .mapTo(valueType.underlying)
      .transform(
        jmap => scala.jdk.CollectionConverters.MapHasAsScala(jmap).asScala.toMap,
        smap => java.util.Map.copyOf(scala.jdk.CollectionConverters.MapHasAsJava(smap).asJava)
      )
  )

  /** Fixed-size ARRAY of this type ({@code T[size]} in DuckDB). Every row has exactly {@code size} elements. Use {@link #list} for variable-length lists.
    */
  def array(size: Int): DuckDbType[List[T]] = DuckDbType(
    underlying
      .array(size)
      .transform(
        jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
        slist => {
          val al = new java.util.ArrayList[T](slist.size)
          slist.foreach(al.add)
          al
        }
      )
  )

  def list: DuckDbType[List[T]] = DuckDbType(
    underlying
      .list()
      .transform(
        jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
        slist => {
          val al = new java.util.ArrayList[T](slist.size)
          slist.foreach(al.add)
          al
        }
      )
  )

  def encode(value: T): Fragment = new Fragment(underlying.encode(value))

  def withTypename(typename: DuckDbTypename[T]): DuckDbType[T] = DuckDbType(underlying.withTypename(typename))
  def withTypename(sqlType: String): DuckDbType[T] = DuckDbType(underlying.withTypename(sqlType))
  def renamed(value: String): DuckDbType[T] = DuckDbType(underlying.renamed(value))
  def renamedDropPrecision(value: String): DuckDbType[T] = DuckDbType(underlying.renamedDropPrecision(value))

  def withRead(read: DuckDbRead[T]): DuckDbType[T] = DuckDbType(underlying.withRead(read))
  def withWrite(write: DuckDbWrite[T]): DuckDbType[T] = DuckDbType(underlying.withWrite(write))
  def withStringifier(stringifier: DuckDbStringifier[T]): DuckDbType[T] = DuckDbType(underlying.withStringifier(stringifier))
  def withJson(json: DuckDbJson[T]): DuckDbType[T] = DuckDbType(underlying.withJson(json))
  def withAnalysis(opts: AnalysisOptions): DuckDbType[T] = DuckDbType(underlying.withAnalysis(opts))
  def withListCodec(codec: DuckDbListCodec[T]): DuckDbType[T] = DuckDbType(underlying.withListCodec(codec))

  def unchecked(): DuckDbType[T] = DuckDbType(underlying.unchecked())
  def nullableOk(): DuckDbType[T] = DuckDbType(underlying.nullableOk())
