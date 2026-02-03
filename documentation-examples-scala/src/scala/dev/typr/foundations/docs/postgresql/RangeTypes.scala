package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.{Range, RangeBound}
import java.time.LocalDate

@SuppressWarnings(Array("unused"))
object RangeTypes:
  //start
  val intRangeType: PgType[Range[Integer]] = PgTypes.int4range
  val dateRangeType: PgType[Range[LocalDate]] = PgTypes.daterange

  // Create ranges with explicit bounds
  val range: Range[Integer] = Range.int4(
    new RangeBound.Closed[Integer](1),
    new RangeBound.Closed[Integer](10)
  ) // [1, 11) after normalization

  // Check containment
  val contains: Boolean = range.contains(5) // true
  //stop
