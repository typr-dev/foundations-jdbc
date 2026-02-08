package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*
import dev.typr.scalafoundations.data.{Range as DbRange, RangeBound}

import java.time.LocalDate

@SuppressWarnings(Array("unused"))
object RangeTypes:
  //start
  val intRangeType: PgType[DbRange[Integer]] = PgTypes.int4range
  val dateRangeType: PgType[DbRange[LocalDate]] = PgTypes.daterange

  // Create ranges with explicit bounds
  val range: DbRange[Integer] = DbRange.int4(
    new RangeBound.Closed[Integer](1),
    new RangeBound.Closed[Integer](10)
  ) // [1, 11) after normalization

  // Check containment
  val contains: Boolean = range.contains(5) // true
  //stop
