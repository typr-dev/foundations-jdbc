package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object FragmentBuilderBasic:
  //start
  def example(): Unit =
    // Build fragments with the builder pattern
    val frag =
      Fragment.of("SELECT * FROM users WHERE id = ")
        .value(PgTypes.int4, 42)
        .append(" AND active = ")
        .value(PgTypes.bool, true)
  //stop
