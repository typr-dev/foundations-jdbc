package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object FragmentBuilderBasic:
  //start
  // Build fragments with the builder pattern
  val frag: Fragment =
    Fragment.of("SELECT * FROM users WHERE id = ")
      .value(PgTypes.int4, 42)
      .append(" AND active = ")
      .value(PgTypes.bool, true)
  //stop
