package dev.typr.foundationssc

/** Generic coercion: `F[Array[Object & T]] => F[Array[T]]`.
  *
  * Scala 3 imports Java's `A[]` as `Array[Object & T]` (intersection type) due to strict handling of Java generics. At runtime they are identical (`Object[]`
  * for reference types), so this cast is provably safe. This is the ONLY sanctioned cast in the Scala wrapper for Java/Scala array interop — do not add more.
  *
  * Parameterized over `F` so it works for any container (PgType, DuckDbType, Array itself) — one function covers all call sites.
  */
private[foundationssc] object ArrayCoerce:
  def apply[F[_], T](fa: F[Array[Object & T]]): F[Array[T]] = fa.asInstanceOf[F[Array[T]]]
